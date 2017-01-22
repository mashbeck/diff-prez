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
    public int id;
    public WorkerType type;

    enum WorkerType {
        CRAWLER,
        WRITER,
        DEFAULT
    }

    public Worker() {

    }

    public Worker(int id, WorkerType type) {
        this.id = id;
        this.type = type;
    }
}
