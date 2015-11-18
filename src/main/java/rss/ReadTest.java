package rss;

import java.io.File;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class ReadTest {
	public static void main(String[] args) {
		// try {
		//
		// String content = "This is the content to write into file";
		//
		// File file = new File("/Users/user/googlenews/rss/errorRss.txt");
		//
		// // if file doesnt exists, then create it
		// if (!file.exists()) {
		// file.createNewFile();
		// }
		//
		// FileWriter fw = new FileWriter(file.getAbsoluteFile());
		// BufferedWriter bw = new BufferedWriter(fw);
		// bw.write(content);
		// bw.close();
		//
		// System.out.println("Done");
		//
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }

		try {

			File errorfile = new File("/Users/user/googlenews/rss/errorRss.txt");
			File workingfile = new File(
					"/Users/user/googlenews/rss/workingRss.txt");

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

			Scanner s = new Scanner(new File(
					"/Users/user/googlenews/rss/allRss.txt"));
			ArrayList<String> list = new ArrayList<String>();
			while (s.hasNext()) {
				list.add(s.next());
			}
			s.close();
			System.out.println("list is " + list.size());

			Iterator<String> iter = list.iterator();
			while (iter.hasNext()) {
				String url = iter.next();
				System.out.println(url);
				RSSFeedParser parser = new RSSFeedParser(url);
				Feed feed = parser.readFeed();
				if (feed == null) {
					System.err.println("error");
					errorfileBuffer.write(url);

				} else {
					System.out.println("parsed");
					workingfileBuffer.write(url);
				}
			}
			errorfileBuffer.close();
			workingfileBuffer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
