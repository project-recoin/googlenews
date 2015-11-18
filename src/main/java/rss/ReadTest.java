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

			File errorfile = new File("errorRss.txt");
			File workingfile = new File("workingRss.txt");

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
			Scanner s = new Scanner(new File(args[0]));
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
				RSSFeedParser parser = new RSSFeedParser(url);
				Feed feed = parser.readFeed();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
