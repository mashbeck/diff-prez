import java.sql.*;
import org.jsoup.*;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by SeanFlannery on 1/21/17.
 *
 * Crawls over root domain
 *
 */
public class Crawler {
    public static ArrayList<Worker> crawlers = new ArrayList<Worker>();
    public static ArrayList<Worker> writers = new ArrayList<Worker>();

    public static ConcurrentHashMap<Integer, String> urls =
            new ConcurrentHashMap<Integer, String>();
    public static ConcurrentHashMap<String, ArrayList<Integer>> words =
            new ConcurrentHashMap<String, ArrayList<Integer>>();
    public static String root;

    public static int rootID;
    public static int nextID;

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
        } else {
            root = "whitehouse.gov";
        }

        /* begin crawl */
        crawl();

    }

    public static void crawl() {
        /* root is first url to be crawled */
        urls.put(0, root);
    }

}
