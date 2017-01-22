import java.sql.*;
import org.jsoup.*;
import org.jsoup.nodes.Element;

import javax.swing.*;
import javax.swing.text.Document;
import java.io.*;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SeanFlannery on 1/21/17.
 *
 * Crawls over root domain and grabs significant information
 *
 */
public class Crawler extends Thread {
    /** Class variables **/
    public static ArrayList<Crawler> crawlers = new ArrayList<Crawler>();
    public static ArrayList<Crawler> writers = new ArrayList<Crawler>();

    public static ConcurrentHashMap<Integer, String> urls =
            new ConcurrentHashMap<Integer, String>();
    public static ConcurrentHashMap<String, ArrayList<Integer>> words =
            new ConcurrentHashMap<String, ArrayList<Integer>>();

    public static String root;
    public static String rootURLTable;
    public static String rootWordTable;

    public static AtomicInteger currID = new AtomicInteger(0);
    public static AtomicInteger nextID = new AtomicInteger(1);

    /** Instance Variables **/
    enum CrawlerType {
        CRAWLER,
        WRITER,
        DEFAULT
    }

    public int id;
    public CrawlerType type;
    public boolean isBusy;
    public Connection conn;//ToDo set this up

    /** Constructors **/
    public Crawler() {
        this(-1, CrawlerType.DEFAULT, false);
    }

    public Crawler(int id, CrawlerType type, boolean isBusy) {
        this.id = id;
        this.type = type;
        this.isBusy = isBusy;
    }


    public static void main(String[] args) {
        /*Grab user preferences*/
        Scanner s = new Scanner(System.in);
        int crawlerNum, writerNum;
        System.out.print("Enter number of crawlers desired: ");
        crawlerNum = s.nextInt();System.out.println();
        System.out.print("Enter number of dbWriters desired: ");
        writerNum = s.nextInt();System.out.println();

        /* Add all crawlers*/
        for (int i = 0; i < crawlerNum; i++) {
            crawlers.add(new Crawler(i, CrawlerType.CRAWLER, false));
        }
        /* Add all writers*/
        for (int i = 0; i < writerNum; i++) {
            writers.add(new Crawler(i, CrawlerType.WRITER, false));
        }

        /* Start crawl over root */
        int rootChoice = JOptionPane.showOptionDialog(null, null, "Crawl over what domain?", 2,
                JOptionPane.QUESTION_MESSAGE, null, new String[] {"whitehouse.gov", "obamawhitehouse.gov"}, null);
        if (rootChoice == 1) {
            root = "obamawhitehouse.gov";
            rootURLTable = "obama";
            rootWordTable = "obamaWords";
        } else {
            root = "whitehouse.gov";
            rootURLTable = "trump";
            rootWordTable = "trumpWords";
        }

        /* begin crawl */
        crawl();
        System.out.println("Done crawling " + root);
    }

    public static void crawl() {
        /* root is first url to be crawled */
        urls.put(0, root);
        org.jsoup.nodes.Document d;
        String currURL = root;
        while (nextID.intValue() > currID.intValue()) {
            try {
                 d = Jsoup.connect(currURL).get();
                for (Element link : d.select("a[href]")) {
                    getFreeCrawler(CrawlerType.CRAWLER).checkUrlInDB(link.absUrl("href"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            currID.incrementAndGet();
        }
    }



    public static Crawler getFreeCrawler(Crawler.CrawlerType type) {
        if (type == Crawler.CrawlerType.WRITER) {
            while (writers.size() > 0) { /*ToDo determine if statement is stupid... Or may lead to inf. loop*/
                for (Crawler w: writers) {
                    if(!w.isBusy) {
                        w.isBusy = true;
                        return w;
                    }
                }
            }
        } else if (type == Crawler.CrawlerType.CRAWLER) {
            while (crawlers.size() > 0) {
                for (Crawler w: crawlers) {
                    if(!w.isBusy) {
                        w.isBusy = true;
                        return w;
                    }
                }
            }
        }
        return null;
    }

    public String parseDescription(String url, int urlID) {
        /* use Jsoup to grab title and p tags */
        StringBuilder sb = new StringBuilder("");
        try {
            org.jsoup.nodes.Document d = Jsoup.connect(url).get();
            sb.append(d.select("title").first().text());
            sb.append(" ");
            for (Element e: d.select("p")) {
                sb.append(e.text());
                sb.append(" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* escape dangerous characters */
        String s = sb.toString();
        s.replaceAll(",", "");
        s.replaceAll(":", "");
        //ToDO consider best options for what to escape
        return s;
    }

    public void checkWordInDB(String word, int urlID) {
        /* either word isn't in words yet, or ArrayList isn't there... */
        if (!words.containsKey(word) || words.get(word) == null) {
            words.put(word, (new ArrayList<Integer>(1)));
            Crawler.getFreeCrawler(Crawler.CrawlerType.WRITER).addWord(word, urlID);
        }
        /* word is already there, but urlID is not */
        else if (!words.get(word).contains(urlID)) {
            words.get(word).add(urlID);
            Crawler.getFreeCrawler(Crawler.CrawlerType.WRITER).addWord(word, urlID);
        }
        isBusy = false;
    }

    public void checkUrlInDB(String url) {
        int urlID;
        if (!urls.containsValue(url)) {
            urls.put((urlID = currID.getAndIncrement()), url);
            String description = parseDescription(url, urlID);
            Crawler.getFreeCrawler(CrawlerType.WRITER).addURL(url , urlID, description);
            String[] words = description.split(" ");
            for (String word: words) {
                getFreeCrawler(CrawlerType.CRAWLER).checkWordInDB(word, urlID);
            }
        }
        isBusy = false;
    }

    public void addURL(String url, int urlID, String description) {
        try {
            PreparedStatement s = conn.prepareStatement("INSERT INTO ? values(?, ?, ?);");
            s.setString(1, rootURLTable);
            s.setString(2, url);
            s.setString(3, description);
            s.setInt(4, urlID);
            s.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        isBusy = false;
    }

    public void addWord(String word, int urlID) {
        /* need to shorten word to 64 chars if necessary */
        if (word.length() > 64) {word = word.substring(0, 64);}
        try {
            PreparedStatement s = conn.prepareStatement("INSERT INTO ? values(?, ?);");
            s.setString(1, rootWordTable);
            s.setString(2, word);
            s.setInt(3, urlID);
            s.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        isBusy = false;
    }
    
}
