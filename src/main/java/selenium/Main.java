package selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;


public class Main {
	
	public static void main(String[] args) {
		ProfilesIni pr=new ProfilesIni();
		FirefoxProfile fp=pr.getProfile("SeleniumUser");
		FirefoxDriver driver=new FirefoxDriver(fp);
		driver.get("http://news.google.com/");
		List<WebElement> devs = driver.findElements(By.xpath(".//td[@class='lt-col']/div/div/div"));
		List<WebElement> topdivs = devs.get(1).findElements(By.cssSelector("div.section-list-content div div.blended-wrapper.blended-wrapper-first.esc-wrapper"));
		List<WebElement> tds = driver.findElements(By.xpath(".//td[@class='esc-layout-article-cell']"));
		for (WebElement td : tds) {
			List<WebElement> title = td.findElements(By.xpath(".//div[@class='esc-lead-article-title-wrapper']/h2[@class='esc-lead-article-title']/a/span[@class='titletext']"));
			System.out.println("Title: " + title.get(0));
			
			
		}
		
		
	}
	

}
