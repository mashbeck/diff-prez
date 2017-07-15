import com.mongodb.*;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Console;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mashbeck on 7/14/17.
 */

//TODO: Fix adding objectIDs to array
//TODO: Create new class for crawler and put main in separate file
//TODO: Multi-Thread Application
public class Crawler {
    final static BasicDBObject UNSEARCHED_URL = new BasicDBObject("searched", false);
    final static String DB_NAME = "crawler";
    final static String URL_COLLECTION = "urls";
    final static String WORD_COLLECTION = "words";
    final static String PRESIDENT_COLLECTION = "presidents";
    final static String TRUMP_ROOT_URL = "https://www.whitehouse.gov";
    final static String OBAMA_ROOT_URL = "https://obamawhitehouse.archives.gov";
    static DBCollection urlCollection;
    static DBCollection wordCollection;
    static DBCollection presidentCollection;

    public static DBObject getNewUnSearchedURL(){
        if(urlCollection == null){
            return null;
        }
        return urlCollection.findOne(UNSEARCHED_URL);
    }

    public static void markURL(DBObject currentURL) {
        if(currentURL == null){
            return;
        }
        urlCollection.update(currentURL, new BasicDBObject("searched", true)
                .append("url", currentURL.get("url"))
                .append("description", null));
    }

    public static void addURLDescription(DBObject currentURL, String description) {
        urlCollection.update(currentURL, new BasicDBObject("searched", true)
                .append("url", currentURL.get("url"))
                .append("description", description));
    }

    public static void addInitialURLSToDB() {
        if(urlCollection == null || presidentCollection == null){
            return;
        }
        BasicDBObject trumpUrlObject = new BasicDBObject("url", TRUMP_ROOT_URL)
                .append("searched", false);
        urlCollection.insert(trumpUrlObject);

        BasicDBList trumpUrls = new BasicDBList();
        trumpUrls.add(trumpUrlObject.get("_id"));
        BasicDBObject trumpObject = new BasicDBObject("name", "Trump")
                .append("urls", trumpUrls);
        presidentCollection.insert(trumpObject);

        BasicDBObject obamaUrlObject = new BasicDBObject("url", OBAMA_ROOT_URL)
                .append("searched", false);
        urlCollection.insert(obamaUrlObject);

        BasicDBList obamaUrls = new BasicDBList();
        obamaUrls.add(obamaUrlObject.get("_id"));
        BasicDBObject obamaObject = new BasicDBObject("name", "Obama")
                .append("urls", obamaUrls);
        presidentCollection.insert(obamaObject);
    }

    public static void initDB() {
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient();
        } catch (UnknownHostException e){
            e.printStackTrace();
        }
        DB db = mongoClient.getDB(DB_NAME);
        db.dropDatabase();
        urlCollection = db.getCollection(URL_COLLECTION);
        wordCollection = db.getCollection(WORD_COLLECTION);
        presidentCollection = db.getCollection(PRESIDENT_COLLECTION);

        urlCollection.drop();
        wordCollection.drop();
        presidentCollection.drop();
    }

    public static String getDescription(Document doc){
        Elements descriptionElements = doc.select("meta[name=description]");
        if(descriptionElements != null && descriptionElements.size() > 0) {
            String description = descriptionElements.first().attr("content");
            return description;
        }
        return null;
    }

    public static void getKeywords(DBObject currentURL, Document doc){
        Elements keywordElements = doc.select("meta[name=keywords]");
        if(keywordElements != null && keywordElements.size() > 0){
            String keywords = keywordElements.first().attr("content");
            String[] keywordsArray= keywords.split(",");
            for(String entry: keywordsArray) {
                String[] entriesArray = entry.split(" ");
                for (String keyword : entriesArray) {
                    keyword = keyword.trim();
                    DBObject wordAlreadyInDB = wordCollection.findOne(new BasicDBObject("word", keyword));
                    if (wordAlreadyInDB == null) {
                        BasicDBList urls = new BasicDBList();
                        urls.add(currentURL.get("_id"));
                        wordCollection.insert(new BasicDBObject("word", keyword)
                                .append("urls", urls));
                    } else {
                        BasicDBList urls = (BasicDBList) wordAlreadyInDB.get("urls");
                        urls.add(currentURL.get("_id"));
                        wordCollection.update(wordAlreadyInDB, new BasicDBObject("word", keyword)
                                .append("urls", urls));
                    }

                }
            }
        }
    }

    public static void parseLinks(DBObject currentURL, Document doc) {
        Elements linkElements = doc.select("a[href]");
        for (Element linkElement: linkElements) {
            String link = linkElement.attr("abs:href");
            DBObject dbObject = urlCollection.findOne(new BasicDBObject("url", link));
            if(dbObject == null){
                if(link.contains("obamawhitehouse.archives.gov")){
                    urlCollection.insert(new BasicDBObject("url", link)
                            .append("searched", false)
                            .append("description", null));
                } else if (link.contains("whitehouse.gov")){
                    urlCollection.insert(new BasicDBObject("url", link)
                            .append("searched", false)
                            .append("description", null));
                }
            }
        }
        currentURL = getNewUnSearchedURL();
    }

    public static boolean isHTTP(String url){
        if (!url.split(":")[0].equals("http") && !url.split(":")[0].equals("https")) {
            return false;
        }
        return true;
    }

    public static void crawl() {
        DBObject currentURL = getNewUnSearchedURL();
        while (currentURL != null) {

            String url = (String) currentURL.get("url");
            System.out.println(url);

            markURL(currentURL);

            if (!isHTTP(url)) {
                currentURL = getNewUnSearchedURL();
                continue;
            }

            Connection.Response resp;

            try {
                resp = Jsoup.connect(url).ignoreContentType(true).timeout(10*1000).execute();
            } catch (IOException e){
                e.printStackTrace();
                currentURL = getNewUnSearchedURL();
                continue;
            }

            String contentType = resp.contentType();
            if (!contentType.contains("text/html")) {
                currentURL = getNewUnSearchedURL();
                continue;
            }

            Document doc;
            try {
                doc = resp.parse();
            } catch (IOException e){
                e.printStackTrace();
                currentURL = getNewUnSearchedURL();
                continue;
            }
            String description = getDescription(doc);
            if(description != null){
                addURLDescription(currentURL, description);
            }

            getKeywords(currentURL, doc);

            parseLinks(currentURL, doc);

            currentURL = getNewUnSearchedURL();
        }
    }

    public static void main(String[] args) {
        initDB();
        addInitialURLSToDB();
        crawl();
    }

}

/*
Schemas

President
name: String
urls: [Url]

Url
url: String
description: String
checked: Boolean

Word
word: String
urls: [Url]
 */
