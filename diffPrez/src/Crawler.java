import java.sql.*;
import org.jsoup.*;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by SeanFlannery on 1/21/17.
 *
 * Crawls over root domain and grabs significant information
 *
 */
public class Crawler {
    /* Class variables */
    public static ArrayList<Worker> crawlers = new ArrayList<Worker>();
    public static ArrayList<Worker> writers = new ArrayList<Worker>();

    public static ConcurrentHashMap<Integer, String> urls =
            new ConcurrentHashMap<Integer, String>();
    public static ConcurrentHashMap<String, ArrayList<Integer>> words =
            new ConcurrentHashMap<String, ArrayList<Integer>>();

    public static String root;
    public static String rootURLTable;
    public static String rootWordTable;

    public static int currID = 0;
    public static int nextID = 1;

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
            crawlers.add(new Worker(i, Worker.WorkerType.CRAWLER, false));
        }
        /* Add all writers*/
        for (int i = 0; i < writerNum; i++) {
            writers.add(new Worker(i, Worker.WorkerType.WRITER, false));
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

    }

    public static void crawl() {
        /* root is first url to be crawled */
        urls.put(0, root);

        while (nextID != currID) {

        }
    }

    public static boolean urlInDB(String URL) {
        return urls.containsValue(URL);
    }

    public static String[] parseText(String url, int urlID) {
        /* use Jsoup to grab title and p tags */

        /* remove crappy characters */
        return null;
    }

    public static void addWord(String word, int urlID) {
        /* either word isn't in words yet, or ArrayList isn't there... */
        if (!words.containsKey(word) || words.get(word) == null) {
            words.put(word, (new ArrayList<Integer>(1)));
            getFreeWorker(Worker.WorkerType.WRITER).addWord(word, urlID, rootWordTable);
        }
        /* word is already there, but urlID is not */
        else if (!words.get(word).contains(urlID)) {
            words.get(word).add(urlID);
            getFreeWorker(Worker.WorkerType.WRITER).addWord(word, urlID, rootWordTable);
        }
    }

    public static Worker getFreeWorker(Worker.WorkerType type) {
        if (type == Worker.WorkerType.WRITER) {
            while (writers.size() > 0) { //ToDo determine if statement is stupid...
                for (Worker w: writers) {
                    if(!w.isBusy) {
                        w.isBusy = true;
                        return w;
                    }
                }
            }
        } else if (type == Worker.WorkerType.CRAWLER) {
            while (crawlers.size() > 0) {
                for (Worker w: crawlers) {
                    if(!w.isBusy) {
                        w.isBusy = true;
                        return w;
                    }
                }
            }
        }
        return null;
    }

}
