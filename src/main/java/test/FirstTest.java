package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class FirstTest {

	public static void main(String[] args) {
		try {
			URL url = new URL(
					"https://ajax.googleapis.com/ajax/services/search/news?"
							+ "v=1.0&userip=152.78.65.193");
			URLConnection connection = url.openConnection();
			connection.addRequestProperty("rsz", "8");
			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			JSONObject json = new JSONObject(builder.toString());
			System.out.println(json);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
