import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public static Object gatekeeper = new Object();

    public static ArrayList<Crawler> crawlers = new ArrayList<Crawler>();
    public static ArrayList<Crawler> writers = new ArrayList<Crawler>();

    public static ConcurrentHashMap<Integer, String> urls =
            new ConcurrentHashMap<Integer, String>();
    public static ConcurrentHashMap<String, ArrayList<Integer>> words =
            new ConcurrentHashMap<String, ArrayList<Integer>>();

    public static String root = "http://whitehouse.gov";
    public static String rootCheck = "whitehouse";
    public static String rootURLTable;
    public static String rootWordTable;

    public static AtomicInteger currID = new AtomicInteger(0);
    public static AtomicInteger nextID = new AtomicInteger(1);

    public static long start = 0;

    public static Connection connS;
    public int id;
    public CrawlerType type;
    public boolean isBusy = false;
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

    public static void main(String[] args) throws SQLException {
        /*Grab user preferences*/
/*        Scanner s = new Scanner(System.in);
        int crawlerNum, writerNum;

        crawlerNum = writerNum = 0;

        /* Add all crawlers
        for (int i = 0; i < crawlerNum; i++) {
            crawlers.add(new Crawler(i, CrawlerType.CRAWLER, false));
        }
        /* Add all writers
        for (int i = 0; i < writerNum; i++) {
            writers.add(new Crawler(i, CrawlerType.WRITER, false));
        }
*/
        /* Start crawl over root */
        int rootChoice = JOptionPane.showOptionDialog(null, null, "Crawl over what domain?", 2,
                JOptionPane.QUESTION_MESSAGE, null, new String[] {"http://www.whitehouse.gov", "http://www.obamawhitehouse.archives.gov"}, null);
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }
        if (rootChoice == 1) {
            root = "http://www.obamawhitehouse.gov";
            rootCheck = "obamawhitehouse";
            rootURLTable = "obama";
            rootWordTable = "obamaWords";
//            connS = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "root");
//            PreparedStatement s = connS.prepareStatement("USE ObamaGov;");
            System.out.println("USE ObamaGov;");
          //  s.executeUpdate();
        } else {
            root = "http://www.whitehouse.gov";
            rootCheck = "whitehouse.gov";
            rootURLTable = "trump";
            rootWordTable = "trumpWords";
//            connS = DriverManager.getConnection("jdbc:mysql://128.210.106.77/TrumpGov", "sean", "password");
            System.out.println("USE TrumpGov;");
//            PreparedStatement s = connS.prepareStatement("USE TrumpGov;");
            //s.executeUpdate();
        }
        start = System.nanoTime();
        /* begin crawl */
        crawl();
        //System.out.println("Done crawling " + root);
    }

    public static void crawl() {
        /* root is first url to be crawled */
        //System.out.println("root: " + root);
        urls.put(0, root);
        org.jsoup.nodes.Document d;
        String currURL = root;
        for (int i = 0; i < 12; i++) {
            (new Crawler()).start();
            long start = System.nanoTime();
            while ((start - System.nanoTime()) / 1000000000 < 1) {}
        }

        /*while (true) {
            try {
                //System.out.println("currURL " + currURL);
                 d = Jsoup.connect(currURL).get();
                if (d == null || d.select("a[href]") == null) {currID.incrementAndGet();continue;}
                for (Element link : d.select("a[href]")) {
                    //System.out.println("Link: " + link.absUrl("href"));
                    mainGuy.checkUrlInDB(link.absUrl("href"));
                    //getFreeCrawler(CrawlerType.CRAWLER).checkUrlInDB(link.absUrl("href"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            currURL = urls.get(currID.incrementAndGet());
            //System.out.println(currURL);
        }*/
    }

    public static Crawler getFreeCrawler(Crawler.CrawlerType type) {
        if (type == Crawler.CrawlerType.WRITER) {
            while (writers.size() > 0) { /*ToDo determine if statement is stupid... Or may lead to inf. loop*/
                for (Crawler w : writers) {
                    //System.out.println("I'm stuck in the writer loop!");
                    //System.out.println(w.isBusy);
                    if (!w.isBusy) {
                        w.isBusy = true;
                        return w;
                    }
                }
            }
        } else if (type == Crawler.CrawlerType.CRAWLER) {
            while (crawlers.size() > 0) {
                for (Crawler w : crawlers) {
                    //System.out.println("I'm stuck in the crawler loop!");
                    if (!w.isBusy) {
                        w.isBusy = true;
                        return w;
                    }
                }
            }
        }
        return null;
    }

    public void run() {
        org.jsoup.nodes.Document d;
        int myID;
        String currURL = urls.get(myID = currID.get());
        //System.out.println(currURL);
        while (true) {
            //System.out.println("stuck with url: " + currURL);
            myID = currID.get();
            //System.out.println(myID);
            currURL = urls.get(myID);
            try {
                //System.out.println("currURL " + currURL);
                if (currURL == null) {continue;}
                d = Jsoup.connect(currURL).get();
                if (d == null || d.select("a[href]") == null) {myID = currID.get();continue;}
                for (Element link : d.select("a[href]")) {
                    //System.out.println("Link: " + link.absUrl("href"));
                    checkUrlInDB(link.absUrl("href"));
                    //getFreeCrawler(CrawlerType.CRAWLER).checkUrlInDB(link.absUrl("href"));
                }
            } catch (Exception e) {
                //System.out.println("caught an exception");
            }
            currURL = urls.get(myID = currID.incrementAndGet());
            //System.out.println(currURL);
        }
    }

    public String parseDescription(String url, int urlID) {
        if (url == null || url.length() < 1) {
            return "";
        }
        /* use Jsoup to grab title and p tags */
        StringBuilder sb = new StringBuilder("");
        try {
            org.jsoup.nodes.Document d = Jsoup.connect(url).get();
            for (Element e: d.select("p")) {
                sb.append(e.text());
                sb.append(" ");
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        /* escape dangerous characters */
        String s = sb.toString();
        s.replaceAll(",", "");
        s.replaceAll(":", "");
        //ToDO consider best options for what to escape
        s.replaceAll("Jump to main content Jump to navigation", "");
        //System.out.println("Description: " + s);
        //System.out.println("Description Length: " + s.length());
        //System.out.println("Time elapsed: " + ((System.nanoTime() - start)) / 1000000000 + " seconds!");

        return s;
    }

    public void checkWordInDB(String word, int urlID) {
        /* either word isn't in words yet, or ArrayList isn't there... */
        if (!words.containsKey(word) || words.get(word) == null) {
            words.put(word, (new ArrayList<Integer>(1)));
            addWord(word, urlID);
            //Crawler.getFreeCrawler(Crawler.CrawlerType.WRITER).addWord(word, urlID);
        }
        /* word is already there, but urlID is not */
        else if (!words.get(word).contains(urlID)) {
            words.get(word).add(urlID);
            addWord(word, urlID);
            //Crawler.getFreeCrawler(Crawler.CrawlerType.WRITER).addWord(word, urlID);
        }
        isBusy = false;
    }

    public void checkUrlInDB(String url) {
        synchronized (gatekeeper) {
            if (!url.contains(rootCheck) || url.contains("pdf") || url.contains("jpg") ||
                    url.contains("png") || url.contains("doc")) {
                return;
            }
            if (rootCheck.equals("whitehouse.gov") && url.contains("obamawhitehouse")) {
                return;
            }
            int urlID;
            if (!urls.containsValue(url)) {
                //System.out.println("Crawling #" + nextID.intValue() + " " + url);
                urls.put((urlID = nextID.getAndIncrement()), url);
                String description = parseDescription(url, urlID);
                addURL(url, urlID, description);
                //Crawler.getFreeCrawler(CrawlerType.WRITER).addURL(url , urlID, description);
                String[] words = description.split(" ");
                for (String word : words) {
                    checkWordInDB(word, urlID);
                    //getFreeCrawler(CrawlerType.CRAWLER).checkWordInDB(word, urlID);
                }
            }
            isBusy = false;
        }
    }

    public void addURL(String url, int urlID, String description) {
        /*try {
            PreparedStatement s = conn.prepareStatement("INSERT INTO ? values(?, ?, ?);");
            s.setString(1, rootURLTable);
            s.setString(2, url);
            s.setString(3, description);
            s.setInt(4, urlID);
            s.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
        }*/
        System.out.println("INSERT INTO " + rootURLTable + " values(" + url + ", " + description + ", " + urlID +");");
        isBusy = false;

    }

    public void addWord(String word, int urlID) {

        /* need to shorten word to 64 chars if necessary */

        if (word.length() > 64) {word = word.substring(0, 64);}
        /*try {
            PreparedStatement s = conn.prepareStatement("INSERT INTO ? values(?, ?);");
            s.setString(1, rootWordTable);
            s.setString(2, word);
            s.setInt(3, urlID);
            s.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
        }*/
            System.out.println("INSERT INTO " + rootWordTable + " values(" + word + ", "+ urlID +");");
        isBusy = false;
    }

    /**
     * Instance Variables
     **/
    enum CrawlerType {
        CRAWLER,
        WRITER,
        DEFAULT
    }
    
}
