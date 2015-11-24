package main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import dataObject.NewsHolder;
import dataObject.Result;

import selenium.Article;

public class googleNewsApiMongoDBDump {
	final static Logger logger = Logger
			.getLogger(googleNewsApiMongoDBDump.class);

	static final String host = "localhost";
	static final int port = 27017;
	static final String databaseName = "Syl";
	static final String collectionName = "GoogleNewsAPIDump";
	static final String engineSource = "GoogleNewsAPIDumpV1.0";

	static MongoClient mongoClient = new MongoClient(host, port);
	static MongoDatabase database = mongoClient.getDatabase(databaseName);

	static final DateFormat formatter = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss zzz");
	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void usage() {
		System.out.println("The program accepts only one arg dir");
		System.out.println("java dir");
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			usage();
			System.exit(-1);
		}

		Gson gson = new Gson();
		Type listType = new TypeToken<NewsHolder>() {
		}.getType();
		String dir = args[0];

		File folder = new File(dir);

		File[] listOfFiles = folder.listFiles();
		// Repeat every some hours!! See end of this block
		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()
					&& listOfFiles[i].getAbsoluteFile().toString()
							.endsWith("json")) {
				System.out.println("File " + listOfFiles[i].getName());
				String fileName = listOfFiles[i].getName();
				String parent = listOfFiles[i].getParentFile().toString();

				String requestURi = null;
				try {
					requestURi = readFile(dir + fileName,
							StandardCharsets.UTF_8);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				NewsHolder newsHolder;
				newsHolder = gson.fromJson(requestURi, listType);

				if (newsHolder.getResponseStatus() == 200) {
					List<Result> resultList = newsHolder.getResponseData()
							.getResults();
					String topicAndLang = newsHolder.getResponseData()
							.getCursor().getMoreResultsUrl();
					Pattern topicPattern = Pattern.compile("&topic=(.)");
					Pattern langPattern = Pattern.compile("&ned=(.*)&");
					Matcher topicMatcher = topicPattern.matcher(topicAndLang);
					String foundTopic = null;
					String foundLang = null;
					if (topicMatcher.find()) {
						foundTopic = topicMatcher.group(1);
					}
					Matcher langMatcher = langPattern.matcher(topicAndLang);
					if (langMatcher.find()) {
						foundLang = langMatcher.group(1);
					}

					for (int j = 0; j < resultList.size(); j++) {
						Article article = new Article();
						Result re = resultList.get(j);
						article.setTitle(re.getTitle());

						article.setTitleUrl(re.getUnescapedUrl());
						article.setContent(re.getContent());
						if (re.getImage() != null) {
							if (re.getImage().getUrl() != null) {
								article.setImageUrl(re.getImage().getUrl());
							} else if (re.getImage().getTbUrl() != null) {
								article.setImageUrl(re.getImage().getTbUrl());
							} else {
								article.setImageUrl("");
							}
						} else {
							article.setImageUrl("");
						}

						article.setPublisher(re.getPublisher());
						article.setLanguage(re.getLanguage());
						article.setEngineSource(engineSource);

						String b = parent.substring(parent.length() - 19,
								parent.length() - 1);
						SimpleDateFormat dateFormat = new SimpleDateFormat(
								"yyyy-MM-dd_HH-mm-ss");
						Date date = null;
						try {
							date = dateFormat.parse(b);
							article.setCollectionDate(df.format(date));
						} catch (ParseException e) {
							logger.error("Couldn't parse date from file name");
							article.setCollectionDate("");
						}

						Date pubDate;
						try {
							pubDate = formatter.parse(re.getPublishedDate());
							article.setPublishedDate(df.format(pubDate));
						} catch (ParseException e) {
							logger.error("Couldn't parse publication date");
							continue;
						}

						insertArticleIntoMongoDB(article, foundTopic, foundLang);

					}
				} else {
					logger.error("File has bad response "
							+ listOfFiles[i].getAbsoluteFile());
				}

			} else {
				logger.error("file has issues " + listOfFiles[i]);
			}

		}

	}

	public static void insertArticleIntoMongoDB(Article article, String topic,
			String ned) {

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
											.append("topic", topic)
											.append("ned", ned));

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

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
