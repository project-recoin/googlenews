package test.rss;

import java.io.File;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import dataObject.rss.Feed;
import dataObject.rss.RSSFeedParser;

public class ReadTest {
	public static void main(String[] args) {

		try {

			String path = args[0];
			File errorfile = new File(path + "errorRss.txt");
			File workingfile = new File(path + "workingRss.txt");

			if (!errorfile.exists()) {
				errorfile.createNewFile();
			}
			if (!workingfile.exists()) {
				workingfile.createNewFile();
			}
			FileWriter errorfileWriter = new FileWriter(
					errorfile.getAbsoluteFile());
			FileWriter workingfileWriter = new FileWriter(
					workingfile.getAbsoluteFile());
			BufferedWriter errorfileBuffer = new BufferedWriter(errorfileWriter);
			BufferedWriter workingfileBuffer = new BufferedWriter(
					workingfileWriter);
			Scanner s = new Scanner(new File(args[1]));
			ArrayList<String> list = new ArrayList<String>();
			while (s.hasNext()) {
				list.add(s.next());
			}
			s.close();

			System.out.println("list is " + list.size());

			Iterator<String> iter = list.iterator();
			while (iter.hasNext()) {
				String url = iter.next();
				// System.out.println(url);
				Feed feed = null;
				try {
					RSSFeedParser parser = new RSSFeedParser(url);
					feed = parser.readFeed();
				} catch (Exception e) {
					System.err.println("rss url is not parsed " + url);
				}
				if (feed == null) {
					errorfileBuffer.write(url);
					errorfileBuffer.newLine();
					errorfileBuffer.flush();

				} else {
					workingfileBuffer.write(url);
					workingfileBuffer.newLine();
					workingfileBuffer.flush();
				}
			}
			errorfileBuffer.close();
			workingfileBuffer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
