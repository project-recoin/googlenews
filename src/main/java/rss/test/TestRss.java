package rss.test;

import rss.Feed;
import rss.FeedMessage;
import rss.RSSFeedParser;

public class TestRss {

	public static void main(String[] args) {

		RSSFeedParser parser = new RSSFeedParser(
				"http://feeds.bbci.co.uk/news/rss.xml");
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
