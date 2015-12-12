package test.rss;

import dataObject.rss.Feed;
import dataObject.rss.FeedMessage;
import dataObject.rss.RSSFeedParser;

public class TestRss {

	public static void main(String[] args) {

		RSSFeedParser parser = new RSSFeedParser(
				"http://rss.news.sohu.com/rss/business.xml");
		Feed feed = parser.readFeed();
		if (feed == null) {
			System.err.println("error");
		} else {
			System.out.println(feed);
			for (FeedMessage message : feed.getMessages()) {
				System.out.println(message);

			}
		}
	}
}
