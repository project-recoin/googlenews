package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {

	public static String endPoint = "https://ajax.googleapis.com/ajax/services/search/news?";

	public static final String[] TOPICS = { "h", "w", "b", "n", "t", "el", "p",
			"e", "s", "m" };
	public static final String[] TOPICSDes = { "headlines", "world",
			"business", "nation", "technology", "elections", "politics",
			"entertainment", "sports", "health" };
	public static final String[] NED = { "uk", "au", "in", "en_il", "en_my",
			"nz", "en_pk", "en_ph", "en_sg", "ar_me", "ar_ae", "ar_lb",
			"ar_sa", "cn", "hk", "hi_in", "ta_in", "ml_in", "te_in", "iw_il",
			"jp", "kr", "tw", "vi_vn", "nl_be", "fr_be", "en_bw", "cs_cz",
			"de", "es", "en_et", "fr", "en_gh", "en_ie", "it", "en_ke",
			"hu_hu", "fr_ma", "en_na", "nl_nl", "en_ng", "no_no", "de_at",
			"pl_pl", "pt-PT_pt", "de_ch", "fr_sn", "en_za", "fr_ch", "sv_se",
			"en_tz", "tr_tr", "en_ug", "en_zw", "ar_eg", "el_gr", "ru_ru",
			"sr_rs", "ru_ua", "uk_ua", "es_ar", "pt-BR_br", "ca", "fr_ca",
			"es_cl", "es_co", "es_cu", "es_us", "es_mx", "es_pe", "us", "es_ve" };

	public static final String version = "1.0";
	public static String userip = "80.44.198.119";
	// public static String userip = "152.78.65.193";

	public static final int ResultLimit = 8;
	public static final int rsz = 8;
	public static final int Start = 8;
	public static final String dirName = "googleNews";

	public static void usage() {

		System.out.println("");
		System.out.println("");
	}

	public static void main(String[] args) {

		StringBuilder urlWithIntialParams = new StringBuilder();
		urlWithIntialParams.append(endPoint);
		urlWithIntialParams.append("v=" + version);
		urlWithIntialParams.append("&userip=" + userip);
		urlWithIntialParams.append("&rsz=" + rsz);
		File file = new File(dirName);
		file.mkdirs();

		try {
			for (int l = 0; l < NED.length; l++) {
				String urlWithCountryParam = urlWithIntialParams + "&ned="
						+ NED[l];
				for (int i = 0; i < TOPICS.length; i++) {
					String urlWithTopicParam = urlWithCountryParam + "&topic="
							+ TOPICS[i];
					for (int j = 0; j < Start * ResultLimit; j += ResultLimit) {
						String urlWithPageParam = urlWithTopicParam + "&start="
								+ j;
						int counterToExitLoop = 0;
						while (true) {
							if (counterToExitLoop > 5) {
								System.out
										.println("403 error is being encountered 5 times");
								System.out.println("Exiting the crawler");
								System.exit(1);

							}
							JSONObject json = runJson(urlWithPageParam);
							if (json == null) {
								continue;
							}
							int responseStatus = json.getInt("json");
							if (responseStatus == 200) {
								PrintWriter writer = new PrintWriter(dirName
										+ "/" + NED[l] + "_" + TOPICSDes[i]
										+ "_" + ((j / 8) + 1) + "_" + ".json",
										"UTF-8");
								writer.println(json.toString());
								writer.close();
								break;
								// if Suspected Terms of Service Abuse
							} else if (responseStatus == 403) {
								System.out
										.println("403 error is encountered with "
												+ NED[l]
												+ "_"
												+ TOPICSDes[i]
												+ "_" + ((j / 8) + 1));
								counterToExitLoop++;
								Thread.sleep(3600000);

							}
						}

					}
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static JSONObject runJson(String stringurl) {
		JSONObject json = null;

		HashSet<String> hashUrl = new HashSet<String>();
		ArrayList<String> arrayUrl = new ArrayList<String>();

		try {

			URL url = new URL(stringurl);
			URLConnection connection = url.openConnection();

			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			json = new JSONObject(builder.toString());

		} catch (IOException e) {
			e.printStackTrace();

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

}
