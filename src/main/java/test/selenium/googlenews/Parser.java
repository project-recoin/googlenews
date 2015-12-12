package test.selenium.googlenews;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import dataObject.googlenews.NewsHolder;
import dataObject.googlenews.Result;

public class Parser {

	public static void main(String[] args) {
		Gson gson = new Gson();
		Type listType = new TypeToken<NewsHolder>() {
		}.getType();
		String dir = "/Users/user/Eclipse/GoogleNews/recoinServer/googlenews4/googleNews2015-11-12_13-13-33/";

		File folder = new File(dir);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileOutputStream(new File(
					folder.getParent() + "/AllJson.json"), true));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()
					&& listOfFiles[i].getAbsoluteFile().toString()
							.endsWith("json")) {
				System.out.println("File " + listOfFiles[i].getName());
				String fileName = listOfFiles[i].getName();

				String requestURi = null;
				try {
					requestURi = readFile(dir + fileName,
							StandardCharsets.UTF_8);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				NewsHolder newsHolder;
				newsHolder = gson.fromJson(requestURi, listType);
				Gson gson2 = new Gson();

				if (newsHolder.getResponseStatus() == 200) {
					List<Result> resultList = newsHolder.getResponseData()
							.getResults();
					String topicAndLang = newsHolder.getResponseData()
							.getCursor().getMoreResultsUrl();
					Pattern topicPattern = Pattern.compile("&topic=(.)");
					Pattern langPattern = Pattern.compile("&ned=(.*)&");
					Matcher topicMatcher = topicPattern.matcher(topicAndLang);
					String foundTopic = null;
					String foundLang = null;
					if (topicMatcher.find()) {
						foundTopic = topicMatcher.group(1);
					}
					Matcher langMatcher = langPattern.matcher(topicAndLang);
					if (langMatcher.find()) {
						foundLang = langMatcher.group(1);
					}

					for (int j = 0; j < resultList.size(); j++) {
						JsonElement jsonElement = gson.toJsonTree(resultList
								.get(j));
						jsonElement.getAsJsonObject().addProperty("topic",
								foundTopic);
						jsonElement.getAsJsonObject().addProperty("region",
								foundLang);
						System.out.println(gson2.toJson(jsonElement));
						writer.println(gson2.toJson(jsonElement));
					}
				} else {
					System.err.println("File has bad response "
							+ listOfFiles[i].getAbsoluteFile());
				}

			} else {
				System.err.println("file has issues " + listOfFiles[i]);
			}
		}

	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
