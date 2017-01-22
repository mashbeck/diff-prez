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
public class Crawler extends Thread {
    public static ArrayList<Crawler> crawlers = new ArrayList<Crawler>(4);
    public static ArrayList<Crawler> writers = new ArrayList<Crawler>(4);
    enum type {
        CRAWLER,
        WRITER
    }

    public Crawler(int id, enum type) {

    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int crawlerNum, writerNum;
        System.out.print("Enter number of crawlers desired: ");
        crawlerNum = s.nextInt();System.out.println();
        System.out.print("Enter number of dbWriters desired: ");
        writerNum = s.nextInt();System.out.println();
        for (int i = 0; i < crawlerNum; i++) {
            crawlers.add(new Crawler());
        }

    }


}
