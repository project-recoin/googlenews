package rss;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class ReadTest {
	public static void main(String[] args) {
		Set<String> lines = null;
		File stats = new File("/Users/user/googlenews/rss/errorRss.txt");
		File stats2 = new File("/Users/user/googlenews/rss/workingRss.txt");

		PrintWriter errorRss = null;
		PrintWriter workingRss = null;
		try {
			stats.createNewFile();
			stats2.createNewFile();
			errorRss = new PrintWriter(stats);
			workingRss = new PrintWriter(stats2);

			lines = new HashSet<String>(FileUtils.readLines(new File(
					"/Users/user/googlenews/rss/allRss.txt")));
			Iterator<String> iter = lines.iterator();
			while (iter.hasNext()) {
				String url = iter.next();
				System.out.println(url);
				RSSFeedParser parser = new RSSFeedParser(url);
				Feed feed = parser.readFeed();
				if (feed == null) {
					System.err.println("error");
					errorRss.println(url);

				} else {
					System.out.println("parsed");
					workingRss.println(url);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			errorRss.close();
			workingRss.close();
		}

	}
}
