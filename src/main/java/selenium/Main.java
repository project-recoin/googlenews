package selenium;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

public class Main {
	static final DateFormat df = new SimpleDateFormat(
			"EEE, d MMM yyyy HH:mm:ss Z");

	public static void main(String[] args) {
		ProfilesIni pr = new ProfilesIni();
		FirefoxProfile fp = pr.getProfile("SeleniumUser");
		FirefoxDriver driver = new FirefoxDriver(fp);
		driver.get("http://news.google.com/");
		List<WebElement> devs = driver.findElements(By
				.xpath(".//td[@class='lt-col']/div/div/div"));
		List<WebElement> topdivs = devs
				.get(1)
				.findElements(
						By.cssSelector("div.section-list-content div div.blended-wrapper.blended-wrapper-first.esc-wrapper"));
		List<WebElement> tds = driver.findElements(By
				.xpath(".//td[@class='esc-layout-article-cell']"));
		for (WebElement td : tds) {
			List<WebElement> title = td
					.findElements(By
							.xpath(".//div[@class='esc-lead-article-title-wrapper']/h2[@class='esc-lead-article-title']/a/span[@class='titletext']"));
			List<WebElement> content = td.findElements(By
					.xpath(".//div[@class='esc-lead-snippet-wrapper']"));
			List<WebElement> publisher = td
					.findElements(By
							.xpath(".//div[@class='esc-lead-article-source-wrapper']/table[@class='al-attribution single-line-height']/tbody/tr/td[@class='al-attribution-cell source-cell']"));
			List<WebElement> publishedDate = td
					.findElements(By
							.xpath(".//div[@class='esc-lead-article-source-wrapper']/table[@class='al-attribution single-line-height']/tbody/tr/td[@class='al-attribution-cell timestamp-cell']/span[@class='al-attribution-timestamp']"));
			System.out.println("Title: " + title.get(0).getText());
			System.out.println("Content: " + content.get(0).getText());
			System.out.println("Publisher: " + publisher.get(0).getText());
			System.out.println("publishedDate: "
					+ df.format(fromTimeAgoToDate(publishedDate.get(0)
							.getText())));

		}

	}

	public static Date fromTimeAgoToDate(String timeAgo) {
		Pattern patter = Pattern.compile("([0-9]+) ([a-zA-Z]+) ago");
		Matcher matcher = patter.matcher(timeAgo);
		String timeNumber = null;
		String timeUnit = null;
		if (matcher.find()) {
			timeNumber = matcher.group(1);
			timeUnit = matcher.group(2);
		}
		Date convertedDate = null;

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
				cal.add(Calendar.DAY_OF_MONTH,
						Integer.valueOf("-" + timeNumber));
				convertedDate = cal.getTime();

			} else if (timeUnit.contains("week")) {
				cal.add(Calendar.WEEK_OF_MONTH,
						Integer.valueOf("-" + timeNumber));
				convertedDate = cal.getTime();

			} else if (timeUnit.contains("month")) {
				cal.add(Calendar.MONTH, Integer.valueOf("-" + timeNumber));
				convertedDate = cal.getTime();

			} else if (timeUnit.contains("year")) {
				cal.add(Calendar.YEAR, Integer.valueOf("-" + timeNumber));
				convertedDate = cal.getTime();

			}

		}
		return convertedDate;

	}
}
