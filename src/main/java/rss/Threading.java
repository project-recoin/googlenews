package rss;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import selenium.Article;

class Threading implements Runnable {
	final static Logger logger = Logger.getLogger(Threading.class);
	static final DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static final String host = "localhost";
	static final int port = 27017;
	static final String databaseName = "Syl";
	static final String collectionName = "RSS";
	static final String engineSource = "RSScollectorV1.1";

	static MongoClient mongoClient = new MongoClient(host, port);
	static MongoDatabase database = mongoClient.getDatabase(databaseName);

	public static final int wholeProcessIsRepeated = 20; // In Minutes

	private List<String> list;

	public Threading(List<String> subList) {
		this.list = subList;
	}

	public Feed getFeed(String url) {
		try {
			RSSFeedParser parser = new RSSFeedParser(url);
			Feed feed = parser.readFeed();
			if (feed == null) {
				return null;
			} else {
				return feed;
			}
		} catch (Exception e) {
			logger.error("Error parsing " + url + "\n" + e);
			return null;
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				for (int i = 0; i < list.size(); i++) {
					Feed feed = getFeed(list.get(i));
					if (feed != null) {
						for (FeedMessage message : feed.getMessages()) {
							Article article = new Article();
							article.setTitle(message.getTitle());
							article.setContent(message.getDescription());
							article.setPublisher(feed.getTitle());
							Date date;
							try {
								date = formatter.parse(message.getPubDate());
								article.setPublishedDate(df.format(date));
							} catch (ParseException e) {
								continue;
							}

							article.setTitleUrl(message.getLink());
							article.setImageUrl(message.getThumbnil());
							article.setEngineSource(engineSource);
							Date currentDate = new Date();
							article.setCollectionDate(df.format(currentDate));
							article.setLanguage(feed.getLanguage());
							article.setRssUrl(list.get(i));
							synchronized (this) {
								insertArticleIntoMongoDB(article);
							}

						}
					}
				}

			} catch (Exception e) {
				logger.error(e);
			}
			logger.info("Sleep for " + wholeProcessIsRepeated + " :)");
			try {
				Thread.sleep(wholeProcessIsRepeated * 60 * 1000);
			} catch (InterruptedException e) {
				logger.error(e);
			}

		}
	}

	public static void insertArticleIntoMongoDB(Article article) {
		try {
			if (article != null) {
				FindIterable<Document> iterable = database.getCollection(collectionName)
						.find(new Document("title", article.getTitle()).append("content", article.getContent()));
				if (iterable.first() == null) {
					database.getCollection(collectionName).insertOne(new Document().append("title", article.getTitle())
							.append("titleUrl", article.getTitleUrl()).append("content", article.getContent())
							.append("imageUrl", article.getImageUrl()).append("publisher", article.getPublisher())
							.append("publishedDate", article.getPublishedDate())
							.append("language", article.getLanguage()).append("engineSource", article.getEngineSource())
							.append("collectionDate", article.getCollectionDate())
							.append("RSSurl", article.getRssUrl()));
					logger.info("One Doc is inserted");
				} else {
					logger.error("Doc is already in the database");
				}
			} else {
				logger.error("Error with parsing the article tags");
			}
		} catch (Exception e) {
			logger.error("Error with feed titled " + article.getTitle() + e);
		}
	}
}