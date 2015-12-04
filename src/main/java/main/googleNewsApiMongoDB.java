package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import dataObject.NewsHolder;
import dataObject.Result;

import selenium.Article;

public class googleNewsApiMongoDB {
	final static Logger logger = Logger.getLogger(googleNewsApiMongoDB.class);

	public static String endPoint = "https://ajax.googleapis.com/ajax/services/search/news?";

	public static final String[] TOPICS = { "h", "w", "b", "n", "t", "el", "p",
			"e", "s", "m" };
	public static final String[] TOPICSDes = { "headlines", "world",
			"business", "nation", "technology", "elections", "politics",
			"entertainment", "sports", "health" };
	public static final String[] NED = { "uk", "au", "in", "en_il", "en_my",
			"nz", "en_pk", "en_ph", "en_sg", "ar_me", "ar_ae", "ar_lb",
			"ar_sa", "cn", "hk", "hi_in", "ta_in", "ml_in", "te_in", "iw_il",
			"jp", "kr", "tw", "vi_vn", "nl_be", "fr_be", "en_bw", "cs_cz",
			"de", "es", "en_et", "fr", "en_gh", "en_ie", "it", "en_ke",
			"hu_hu", "fr_ma", "en_na", "nl_nl", "en_ng", "no_no", "de_at",
			"pl_pl", "pt-PT_pt", "de_ch", "fr_sn", "en_za", "fr_ch", "sv_se",
			"en_tz", "tr_tr", "en_ug", "en_zw", "ar_eg", "el_gr", "ru_ru",
			"sr_rs", "ru_ua", "uk_ua", "es_ar", "pt-BR_br", "ca", "fr_ca",
			"es_cl", "es_co", "es_cu", "es_us", "es_mx", "es_pe", "us", "es_ve" };

	public static final String version = "1.0";
	public static String userip;

	public static final int maxWait = 50; // In seconds
	public static final int minWait = 20; // In seconds
	public static final int wholeProcessIsRepeated = 12; // In hours
	public static final int ResultLimit = 8;
	public static final int rsz = 8;
	public static final int Start = 8;
	public static int GlobalErrorCounter = 0;

	static final String host = "localhost";
	static final int port = 27017;
	static final String databaseName = "Syl";
	static final String collectionName = "GoogleNewsAPI";
	static final String engineSource = "GoogleNewsAPIV1.0";

	static MongoClient mongoClient = new MongoClient(host, port);
	static MongoDatabase database = mongoClient.getDatabase(databaseName);

	static final DateFormat formatter = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss zzz");
	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void usage() {
		System.out
				.println("The program accepts only one arg which is the IP address");
		System.out.println("java IP address");
	}

	public static void main(String[] args) {

		if (args.length != 1) {
			usage();
			System.exit(-1);
		}

		userip = args[0];

		StringBuilder urlWithIntialParams = new StringBuilder();
		urlWithIntialParams.append(endPoint);
		urlWithIntialParams.append("v=" + version);
		urlWithIntialParams.append("&userip=" + userip);
		urlWithIntialParams.append("&rsz=" + rsz);

		try {
			// Repeat every some hours!! See end of this block
			while (true) {
				// upper level counter for waiting a bit more time
				GlobalErrorCounter = 0;

				// looping through countries and versions
				for (int l = 0; l < NED.length; l++) {
					String urlWithCountryParam = urlWithIntialParams + "&ned="
							+ NED[l];
					// looping through the 10 topics
					for (int i = 0; i < TOPICS.length; i++) {
						String urlWithTopicParam = urlWithCountryParam
								+ "&topic=" + TOPICS[i];
						// loop throguh the 8 pages
						for (int j = 0; j < Start * ResultLimit; j += ResultLimit) {
							String urlWithPageParam = urlWithTopicParam
									+ "&start=" + j;
							// inner counter level
							int counterToExitLoop = 0;
							// doing the same request over until it passed or
							// countenue failong
							// and therefore exiting the app
							while (true) {
								if (counterToExitLoop > 5) {
									logger.error("403 error is being encountered 5 times");
									if (GlobalErrorCounter < 5) {
										GlobalErrorCounter++;
										Thread.sleep(3600000);
										// System.out.println("chainging the topic!");
										// break topicLoop;
									} else {
										logger.error("Keep getting the error!");
										logger.error("Exiting the crawler");
										System.exit(1);
									}
								}
								// time between reqests is randomised between
								// max and min time
								Random rn = new Random();
								int randomWait = rn
										.nextInt((maxWait - minWait) + 1)
										+ minWait;
								Thread.sleep(randomWait + 1000);
								JSONObject json = runJson(urlWithPageParam);
								if (json == null) {
									continue;
								}
								int responseStatus = json
										.getInt("responseStatus");
								if (responseStatus == 200) {
									// Reset the global error to 0 which means
									// the
									// crawler is back to work normally!
									GlobalErrorCounter = 0;
									logger.info("Processed " + NED[l] + "_"
											+ TOPICSDes[i] + "_"
											+ ((j / 8) + 1) + "_" + ".json");

									parseJson(json, TOPICSDes[i], NED[l]);

									break;
									// if Suspected Terms of Service Abuse
								} else if (responseStatus == 403) {
									logger.info("403 error is encountered with "
											+ NED[l]
											+ "_"
											+ TOPICSDes[i]
											+ "_"
											+ ((j / 8) + 1));
									counterToExitLoop++;
									Thread.sleep(300000);

								}
							}

						}
					}

				}

				logger.info("Sleeping for " + wholeProcessIsRepeated + " Hours");
				logger.info("Leave me alone (*!*)");
				Thread.sleep(wholeProcessIsRepeated * 60 * 60 * 1000);
			}
		} catch (InterruptedException e) {
			logger.error(e);
		}

	}

	public static JSONObject runJson(String stringurl) {
		JSONObject json = null;

		try {

			URL url = new URL(stringurl);
			URLConnection connection = url.openConnection();

			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			json = new JSONObject(builder.toString());

		} catch (IOException e) {
			logger.error(e);
		} catch (JSONException e) {
			logger.error(e);
		}

		return json;
	}

	public static void parseJson(JSONObject json, String topic, String ned) {
		Gson gson = new Gson();
		Type listType = new TypeToken<NewsHolder>() {
		}.getType();
		NewsHolder newsHolder;
		newsHolder = gson.fromJson(json.toString(), listType);
		List<Result> resultList = newsHolder.getResponseData().getResults();

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
			Date currentDate = new Date();
			article.setCollectionDate(df.format(currentDate));
			Date date;
			try {
				date = formatter.parse(re.getPublishedDate());
				article.setPublishedDate(df.format(date));
			} catch (ParseException e) {
				logger.error(e);
				continue;
			}

			insertArticleIntoMongoDB(article, topic, ned);

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
