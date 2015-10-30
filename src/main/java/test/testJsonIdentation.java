package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class testJsonIdentation {

	public static void main(String[] args) {
		String dir = args[0];
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();
		File dis = new File(dir + "/filtered");
		dis.mkdirs();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				if (file.getAbsoluteFile().toString().endsWith(".json")) {
					try {
						System.out.println(file.getAbsoluteFile());
						BufferedReader br = new BufferedReader(new FileReader(
								file));
						PrintWriter writer = new PrintWriter(dis + "/"
								+ file.getName(), "UTF-8");
						String line;
						StringBuilder builder = new StringBuilder();
						while ((line = br.readLine()) != null) {
							builder.append(line);

						}
						Gson gson = new GsonBuilder().setPrettyPrinting()
								.create();
						JsonParser jp = new JsonParser();
						JsonElement je = jp.parse(builder.toString());
						String prettyJsonString = gson.toJson(je);
						writer.println(prettyJsonString);

						br.close();
						writer.close();
					} catch (Exception e) {
					}

				}
			}

		}

	}

}
