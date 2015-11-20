package rss;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import selenium.Article;

public class RSScollector {
	static final DateFormat formatter = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss zzz");
	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static final String host = "localhost";
	static final int port = 27017;
	static final String databaseName = "Syl";
	static final String collectionName = "RSS";
	static final String engineSource = "RSScollectorV1.0";

	public static void main(String[] args) {
		Scanner s;
		try {
			s = new Scanner(new File(args[0]));
			ArrayList<String> list = new ArrayList<String>();
			while (s.hasNext()) {
				list.add(s.next());
			}
			s.close();

			for (int i = 0; i < list.size(); i++) {
				System.out.println("Processing " + list.get(i));
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
						insertArticleIntoMongoDB(article);
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	static MongoClient mongoClient = new MongoClient(host, port);
	static MongoDatabase database = mongoClient.getDatabase(databaseName);

	public static void insertArticleIntoMongoDB(Article article) {
		try {
			if (article != null) {
				FindIterable<Document> iterable = database.getCollection(
						collectionName).find(
						new Document("title", article.getTitle()).append(
								"content", article.getContent()));
				if (iterable.first() == null) {
					database.getCollection(collectionName)
							.insertOne(
									new Document()
											.append("title", article.getTitle())
											.append("titleUrl",
													article.getTitleUrl())
											.append("content",
													article.getContent())
											.append("imageUrl",
													article.getImageUrl())
											.append("publisher",
													article.getPublisher())
											.append("publishedDate",
													article.getPublishedDate())
											.append("language",
													article.getLanguage())
											.append("engineSource",
													article.getEngineSource())
											.append("collectionDate",
													article.getCollectionDate())
											.append("RSSurl",
													article.getRssUrl()));

				} else {
					System.err.println("Doc is already in the database");
				}
			} else {
				System.err.println("Error with parsing the article tags");
			}
		} catch (Exception e) {
			System.err.println("Error with feed titled " + article.getTitle());
			e.printStackTrace();
		}
	}

	public static Feed getFeed(String url) {
		try {
			RSSFeedParser parser = new RSSFeedParser(url);
			Feed feed = parser.readFeed();
			if (feed == null) {
				return null;
			} else {
				return feed;
			}
		} catch (Exception e) {
			System.err.println("Error parsing " + url);
			return null;
		}
	}
}
