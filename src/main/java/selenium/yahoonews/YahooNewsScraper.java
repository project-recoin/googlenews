package selenium.yahoonews;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

import selenium.Article;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import main.googleNewsApiMongoDB;

public class YahooNewsScraper {
	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static final DateFormat yahooDateStyle = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
	static final String ArticleXpath = ".//li[@class='list-story']";
	static final String showMoreXpath = ".//div[@class='inline-show-more']/a[@class='rapid-nf more-link']";

	static final String titleXpath = ".//div[@class='txt']/a[@class='title ']";
	static final String titleUrlXpath = ".//div[@class='txt']/a[@class='title ']";
	static final String contentXpath = ".//div[@class='txt']/p[@class='description']";
	static final String publisherXpath = ".//div[@class='txt']/cite/span[@class='provider']";
	static final String publishedDateXpath = ".//div[@class='txt']/cite/abbr";
	static final String imageUrlXpath = ".//a[@class='thumb']/img";

	static final String host = "localhost";
	static final int port = 27017;
	static final String databaseName = "Syl";
	static final String collectionName = "seleniumYahooNews";
	static final String engineSource = "yahooNewsV1.1";

	public static final int wholeProcessIsRepeated = 1; // In hours

	public static final String[] TOPICS = { "africa", "animals-pets", "asia", "business", "companies", "conservatives",
			"crime", "culture", "diet-nutrition", "economy", "education", "energy", "entertainment", "environment",
			"europe", "health", "healthcare", "illnesses-conditions", "industry", "labour", "liberal-democrats",
			"middle-east", "music", "nature", "oddly-enough", "politics", "reviews", "royal-family", "science",
			"showbiz", "space", "sports", "stockmarket", "tech", "tv-soap", "uk", "uk-politics", "usa", "usa-politics",
			"world", "world-politics", "your-money" };

	final static Logger logger = Logger.getLogger(YahooNewsScraper.class);

	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient(host, port);
		MongoDatabase database = mongoClient.getDatabase(databaseName);

		ProfilesIni pr = new ProfilesIni();
		FirefoxProfile fp = pr.getProfile("SeleniumUser");
		FirefoxDriver driver = new FirefoxDriver(fp);
		while (true) {
			for (int i = 0; i < TOPICS.length; i++) {
				logger.info("Processing the topic " + TOPICS[i]);
				String url = "https://uk.news.yahoo.com/" + TOPICS[i] + "/";
				driver.get(url);
				List<WebElement> tds = driver.findElements(By.xpath(ArticleXpath));
				for (WebElement td : tds) {
					try {
						while (true) {

							List<WebElement> showMore = driver.findElements(By.xpath(showMoreXpath));
							if (!showMore.isEmpty()) {
								logger.info("Getting more than one page");
								showMore.get(0).click();
								try {
									Thread.sleep(10000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								break;
							}
						}
						Article article = getArticle(td);
						if (article != null) {
							FindIterable<Document> iterable = database.getCollection(collectionName).find(
									new Document("title", article.getTitle()).append("content", article.getContent()));
							if (iterable.first() == null) {
								database.getCollection(collectionName).insertOne(new Document()
										.append("title", article.getTitle()).append("titleUrl", article.getTitleUrl())
										.append("content", article.getContent())
										.append("publisher", article.getPublisher())
										.append("publishedDate", article.getPublishedDate()).append("topic", TOPICS[i])
										.append("engineSource", article.getEngineSource())
										.append("collectionDate", article.getCollectionDate())
										.append("imageUrl", article.getImageUrl()));

							} else {
								logger.error("Doc is already in the database");
							}
						} else {
							logger.error("Error with parsing the article tags");
						}
					} catch (Exception e) {
						logger.error("Error with " + url);
						e.printStackTrace();
					}
				}
			}

			logger.info("Sleeping for " + wholeProcessIsRepeated + " Hours");
			logger.info("Leave me alone (*!*)");
			try {
				Thread.sleep(wholeProcessIsRepeated * 60 * 60 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static Article getArticle(WebElement td) {
		Article article = new Article();
		List<WebElement> title = td.findElements(By.xpath(titleXpath));
		List<WebElement> titleUrl = td.findElements(By.xpath(titleUrlXpath));
		List<WebElement> content = td.findElements(By.xpath(contentXpath));
		List<WebElement> publisher = td.findElements(By.xpath(publisherXpath));
		List<WebElement> publishedDate = td.findElements(By.xpath(publishedDateXpath));
		List<WebElement> imageUrl = td.findElements(By.xpath(imageUrlXpath));

		if (!title.isEmpty()) {
			article.setTitle(title.get(0).getText());
			article.setTitleUrl(titleUrl.get(0).getAttribute("href"));
		} else {
			return null;
		}

		if (!content.isEmpty()) {
			article.setContent(content.get(0).getText());
		} else {
			return null;
		}

		if (!publisher.isEmpty()) {
			article.setPublisher(publisher.get(0).getText());
		} else {
			article.setPublisher(null);
		}

		if (!publishedDate.isEmpty()) {
			article.setPublishedDate(df.format(fromTimeAgoToDate(publishedDate.get(0).getAttribute("title"))));
		} else {
			article.setPublishedDate(null);
		}
		if (!imageUrl.isEmpty()) {
			try {
				String a = imageUrl.get(0).getAttribute("style");
				String cleanUrl = a.substring(a.lastIndexOf("--/") + 3, a.lastIndexOf("\");"));

				article.setImageUrl(cleanUrl);
			} catch (Exception e) {
				article.setImageUrl(null);
			}
		} else {
			article.setImageUrl(null);
		}

		article.setEngineSource(engineSource);
		Date currentDate = new Date();
		article.setCollectionDate(df.format(currentDate));

		return article;
	}

	public static Date fromTimeAgoToDate(String timeAgo) {
		Date convertedDate = null;

		ParsePosition p = new ParsePosition(0);
		convertedDate = yahooDateStyle.parse(timeAgo, p);

		return convertedDate;

	}

	// Won't be used for now
	public static Date fromTimeAgoToDateOld(String timeAgo) {
		Date convertedDate = null;
		if (timeAgo.contains("ago")) {
			Pattern patter = Pattern.compile("([0-9]+) ([a-zA-Z]+) ago");
			Matcher matcher = patter.matcher(timeAgo);
			String timeNumber = null;
			String timeUnit = null;
			if (matcher.find()) {
				timeNumber = matcher.group(1);
				timeUnit = matcher.group(2);
			}

			if (timeNumber != null && timeUnit != null) {
				Date currentDate = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentDate);

				if (timeUnit.contains("second")) {
					cal.add(Calendar.SECOND, Integer.valueOf("-" + timeNumber));
					convertedDate = cal.getTime();
				} else if (timeUnit.contains("min")) {
					cal.add(Calendar.MINUTE, Integer.valueOf("-" + timeNumber));
					convertedDate = cal.getTime();

				} else if (timeUnit.contains("hour")) {
					cal.add(Calendar.HOUR, Integer.valueOf("-" + timeNumber));
					convertedDate = cal.getTime();

				} else if (timeUnit.contains("day")) {
					cal.add(Calendar.DAY_OF_MONTH, Integer.valueOf("-" + timeNumber));
					convertedDate = cal.getTime();

				} else if (timeUnit.contains("week")) {
					cal.add(Calendar.WEEK_OF_MONTH, Integer.valueOf("-" + timeNumber));
					convertedDate = cal.getTime();

				} else if (timeUnit.contains("month")) {
					cal.add(Calendar.MONTH, Integer.valueOf("-" + timeNumber));
					convertedDate = cal.getTime();

				} else if (timeUnit.contains("year")) {
					cal.add(Calendar.YEAR, Integer.valueOf("-" + timeNumber));
					convertedDate = cal.getTime();

				}

			}
		} else {

			ParsePosition p = new ParsePosition(0);
			convertedDate = yahooDateStyle.parse(timeAgo, p);

		}
		return convertedDate;

	}

}