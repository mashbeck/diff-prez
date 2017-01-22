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

public class Worker extends Thread {
    enum WorkerType {
        CRAWLER,
        WRITER,
        DEFAULT
    }

    public int id;
    public WorkerType type;
    public boolean busy;


    public Worker() {
        this(-1, WorkerType.DEFAULT, false);
    }

    public Worker(int id, WorkerType type, boolean busy) {
        this.id = id;
        this.type = type;
        this.busy = busy;
    }
}
