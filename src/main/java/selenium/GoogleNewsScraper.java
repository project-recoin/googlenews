package selenium;

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

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

public class GoogleNewsScraper {
	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static final DateFormat googleDateStyle = new SimpleDateFormat("MMM dd, yyyy");

	static final String ArticleXpath = ".//div[@class='esc-default-layout-wrapper esc-expandable-wrapper']/table/tbody/tr";

	static final String titleXpath = ".//td[@class='esc-layout-article-cell']/div[@class='esc-lead-article-title-wrapper']/h2[@class='esc-lead-article-title']/a/span[@class='titletext']";
	static final String titleUrlXpath = ".//td[@class='esc-layout-article-cell']/div[@class='esc-lead-article-title-wrapper']/h2[@class='esc-lead-article-title']/a";
	static final String contentXpath = ".//td[@class='esc-layout-article-cell']/div[@class='esc-lead-snippet-wrapper']";
	static final String publisherXpath = ".//td[@class='esc-layout-article-cell']/div[@class='esc-lead-article-source-wrapper']/table[@class='al-attribution single-line-height']/tbody/tr/td[@class='al-attribution-cell source-cell']";
	static final String publishedDateXpath = ".//td[@class='esc-layout-article-cell']/div[@class='esc-lead-article-source-wrapper']/table[@class='al-attribution single-line-height']/tbody/tr/td[@class='al-attribution-cell timestamp-cell']/span[@class='al-attribution-timestamp']";
	static final String imageUrlXpath = ".//td[@class='esc-layout-thumbnail-cell']/div/div/div/a/div/img";

	static final String engineSource = "GoogleNewsV1.1";

	static final String host = "localhost";
	static final int port = 27017;
	static final String databaseName = "Syl";
	static final String collectionName = "seleniumGoogleNews";

	public static final int wholeProcessIsRepeated = 1; // In hours

	public static final String[] TOPICS = { "h", "w", "b", "n", "t", "el", "p", "e", "s", "m" };
	public static final String[] TOPICSDes = { "headlines", "world", "business", "nation", "technology", "elections",
			"politics", "entertainment", "sports", "health" };
	public static final String[] NED = { "uk", "au", "in", "en_il", "en_my", "nz", "en_pk", "en_ph", "en_sg", "en_bw",
			"en_et", "en_gh", "en_ie", "en_ke", "en_na", "en_ng", "en_za", "en_tz", "en_ug", "en_zw", "ca", "us" };

	final static Logger logger = Logger.getLogger(GoogleNewsScraper.class);

	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient(host, port);
		MongoDatabase database = mongoClient.getDatabase(databaseName);

		ProfilesIni pr = new ProfilesIni();
		FirefoxProfile fp = pr.getProfile("SeleniumUser");
		FirefoxDriver driver = new FirefoxDriver(fp);
		while (true) {
			for (int j = 0; j < NED.length; j++) {
				logger.info("Processing the language " + NED[j]);
				for (int i = 0; i < TOPICS.length; i++) {
					logger.info("Processing the topic " + TOPICS[i]);
					String url = "http://news.google.com/news/section?cf=all&ned=" + NED[j] + "&topic=" + TOPICS[i];
					driver.get(url);
					List<WebElement> tds = driver.findElements(By.xpath(ArticleXpath));
					for (WebElement td : tds) {
						try {
							Article article = getArticle(td);
							if (article != null) {
								FindIterable<Document> iterable = database.getCollection(collectionName)
										.find(new Document("title", article.getTitle()).append("content",
												article.getContent()));
								if (iterable.first() == null) {
									database.getCollection(collectionName)
											.insertOne(new Document().append("title", article.getTitle())
													.append("titleUrl", article.getTitleUrl())
													.append("content", article.getContent())
													.append("publisher", article.getPublisher())
													.append("publishedDate", article.getPublishedDate())
													.append("topic", TOPICSDes[i]).append("ned", NED[i])
													.append("engineSource", article.getEngineSource())
													.append("collectionDate", article.getCollectionDate())
													.append("imageUrl", article.getImageUrl()));
									logger.info("One Doc is inserted");
								} else {
									logger.error("Doc is already in the database");
								}
							} else {
								logger.error("Error with parsing the article tags");
							}
						} catch (Exception e) {
							logger.error("Error with " + url);
							logger.error(e);
						}
					}
				}
			}
			logger.info("Sleeping for " + wholeProcessIsRepeated + " Hours");
			logger.info("Leave me alone (*!*)");
			try {
				Thread.sleep(wholeProcessIsRepeated * 60 * 60 * 1000);
			} catch (InterruptedException e) {
				logger.error(e);
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

		if (!titleUrl.isEmpty()) {
			article.setTitleUrl(titleUrl.get(0).getAttribute("url"));
		} else {
			article.setTitleUrl(null);
		}

		if (!publishedDate.isEmpty()) {
			article.setPublishedDate(df.format(fromTimeAgoToDate(publishedDate.get(0).getText())));
		} else {
			article.setPublishedDate(null);
		}

		if (!imageUrl.isEmpty()) {
			article.setImageUrl(imageUrl.get(0).getAttribute("src"));
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
				} else if (timeUnit.contains("minute")) {
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
			convertedDate = googleDateStyle.parse(timeAgo, p);

		}
		return convertedDate;

	}
}
