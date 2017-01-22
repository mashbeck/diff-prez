import java.sql.*;
import org.jsoup.*;
import java.io.*;
import java.util.*;

/**
 * Created by SeanFlannery on 1/21/17.
 *
 * Crawls over root domain
 *
 */
public class Crawler {
    public static ArrayList<Worker> crawlers = new ArrayList<Worker>();
    public static ArrayList<Worker> writers = new ArrayList<Worker>();


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
            crawlers.add(new Worker(i, Worker.WorkerType.CRAWLER));
        }
        /* Add all writers*/
        for (int i = 0; i < writerNum; i++) {
            writers.add(new Worker(i, Worker.WorkerType.WRITER));
        }
    }



}
