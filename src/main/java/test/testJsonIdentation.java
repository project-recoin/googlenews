package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.json.JSONObject;

public class testJsonIdentation {

	public static void main(String[] args) {
		String queriesFile = "/Users/user/Eclipse/GoogleNews/googleNews/b/0.json";
		FileInputStream fis = null;
		BufferedReader br1 = null;
		String line1 = null;
//		File stats = new File("result.txt");
		JSONObject json = null;

		try {
			fis = new FileInputStream(queriesFile);
			br1 = new BufferedReader(new InputStreamReader(fis,
					Charset.forName("UTF-8")));
//			stats.createNewFile();
//			PrintWriter pwStats = new PrintWriter(stats);
			StringBuilder builder = new StringBuilder();
			while ((line1 = br1.readLine()) != null) {
				builder.append(line1);
			}

			json = new JSONObject(builder.toString());
			System.out.println(json);
			int responseStatus = json.getInt("responseStatus");
			if (responseStatus == 200) {
				System.out.println("passed");
			}
			// ObjectMapper mapper = new ObjectMapper();
			// Object indentedJson = mapper.readValue(json, Object.class);
			// pwStats.pr

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
