package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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
	public static String userip;

	public static final int maxWait = 50; // In seconds
	public static final int minWait = 20; // In seconds
	public static final int wholeProcessIsRepeated = 12; // In hours
	public static final int ResultLimit = 8;
	public static final int rsz = 8;
	public static final int Start = 8;
	public static final String dirName = "googleNews";
	public static int GlobalErrorCounter = 0;

	public static void usage() {
		System.out
				.println("The program accepts only one arg which is the IP address");
		System.out.println("java IP address");
	}

	public static void main(String[] args) {

		if (args.length != 1) {
			usage();
			System.exit(-1);
		}

		userip = args[0];

		StringBuilder urlWithIntialParams = new StringBuilder();
		urlWithIntialParams.append(endPoint);
		urlWithIntialParams.append("v=" + version);
		urlWithIntialParams.append("&userip=" + userip);
		urlWithIntialParams.append("&rsz=" + rsz);

		try {
			// Repeat every some hours!! See end of this block
			while (true) {
				// upper level counter for waiting a bit more time
				GlobalErrorCounter = 0;
				Date date = new Date();
				// dir is printed with the time and date of creation
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd_HH-mm-ss");
				String DirFullName = dirName + dateFormat.format(date);
				File file = new File(DirFullName);
				file.mkdirs();
				// looping through countries and versions
				for (int l = 0; l < NED.length; l++) {
					String urlWithCountryParam = urlWithIntialParams + "&ned="
							+ NED[l];
					// looping through the 10 topics
					for (int i = 0; i < TOPICS.length; i++) {
						String urlWithTopicParam = urlWithCountryParam
								+ "&topic=" + TOPICS[i];
						// loop throguh the 8 pages
						for (int j = 0; j < Start * ResultLimit; j += ResultLimit) {
							String urlWithPageParam = urlWithTopicParam
									+ "&start=" + j;
							// inner counter level
							int counterToExitLoop = 0;
							// doing the same request over until it passed or
							// countenue failong
							// and therefore exiting the app
							while (true) {
								if (counterToExitLoop > 5) {
									System.out
											.println("403 error is being encountered 5 times");
									if (GlobalErrorCounter < 5) {
										GlobalErrorCounter++;
										Thread.sleep(3600000);
										// System.out.println("chainging the topic!");
										// break topicLoop;
									} else {
										System.out
												.println("Keep getting the error!");
										System.out
												.println("Exiting the crawler");
										System.exit(1);
									}
								}
								// time between reqests is randomised between
								// max and min time
								Random rn = new Random();
								int randomWait = rn
										.nextInt((maxWait - minWait) + 1)
										+ minWait;
								Thread.sleep(randomWait + 1000);
								JSONObject json = runJson(urlWithPageParam);
								if (json == null) {
									continue;
								}
								int responseStatus = json
										.getInt("responseStatus");
								if (responseStatus == 200) {
									// Reset the global error to 0 which means
									// the
									// crawler is back to work normally!
									GlobalErrorCounter = 0;
									System.out.println("Processed " + NED[l]
											+ "_" + TOPICSDes[i] + "_"
											+ ((j / 8) + 1) + "_" + ".json");
									PrintWriter writer = new PrintWriter(
											DirFullName + "/" + NED[l] + "_"
													+ TOPICSDes[i] + "_"
													+ ((j / 8) + 1) + "_"
													+ ".json", "UTF-8");
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
													+ "_"
													+ ((j / 8) + 1));
									counterToExitLoop++;
									Thread.sleep(300000);

								}
							}

						}
					}

				}

				System.out.println("Sleeping for " + wholeProcessIsRepeated
						+ " Hours");
				System.out.println("Leave me alone (*!*)");
				Thread.sleep(wholeProcessIsRepeated * 60 * 60 * 1000);
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
