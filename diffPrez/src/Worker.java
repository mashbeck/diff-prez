import java.sql.*;
import org.jsoup.*;
import java.io.*;
import java.sql.Connection;
import java.util.*;

/**
 * Created by SeanFlannery on 1/21/17.
 *
 * Crawls over root domain
 *
 */

public class Worker extends Thread {
    enum WorkerType {
        CRAWLER,
        WRITER,
        DEFAULT
    }

    public int id;
    public WorkerType type;
    public boolean isBusy;
    public Connection conn;//ToDo set this up


    public Worker() {
        this(-1, WorkerType.DEFAULT, false);
    }

    public Worker(int id, WorkerType type, boolean isBusy) {
        this.id = id;
        this.type = type;
        this.isBusy = isBusy;
    }

    public void addURL(String url, int urlID, String description, String table) {
        try {
            PreparedStatement s = conn.prepareStatement("INSERT INTO ? values(?, ?, ?);");
            s.setString(1, table);
            s.setString(2, url);
            s.setString(3, description);
            s.setInt(4, urlID);
            s.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        isBusy = false;
    }

    public void addWord(String word, int urlID, String table) {
        /* need to shorten word to 64 chars if necessary */
        if (word.length() > 64) {word = word.substring(0, 64);}
        try {
            PreparedStatement s = conn.prepareStatement("INSERT INTO ? values(?, ?);");
            s.setString(1, table);
            s.setString(2, word);
            s.setInt(3, urlID);
            s.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        isBusy = false;
    }
}
