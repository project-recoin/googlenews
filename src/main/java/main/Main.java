package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class Main {

	public static String endPoint = "https://ajax.googleapis.com/ajax/services/search/news?";

	public static final String[] TOPICS = { "h", "w", "b", "n", "t", "el", "p",
			"e", "s", "m" };
	public static final String[] NED = { "au", "in", "en_il", "en_my", "nz",
			"en_pk", "en_ph", "en_sg", "ar_me", "ar_ae", "ar_lb", "ar_sa",
			"cn", "hk", "hi_in", "ta_in", "ml_in", "te_in", "iw_il", "jp",
			"kr", "tw", "vi_vn", "nl_be", "fr_be", "en_bw", "cs_cz", "de",
			"es", "en_et", "fr", "en_gh", "en_ie", "it", "en_ke", "hu_hu",
			"fr_ma", "en_na", "nl_nl", "en_ng", "no_no", "de_at", "pl_pl",
			"pt-PT_pt", "de_ch", "fr_sn", "en_za", "fr_ch", "sv_se", "en_tz",
			"tr_tr", "en_ug", "uk", "en_zw", "ar_eg", "el_gr", "ru_ru",
			"sr_rs", "ru_ua", "uk_ua", "es_ar", "pt-BR_br", "ca", "fr_ca",
			"es_cl", "es_co", "es_cu", "es_us", "es_mx", "es_pe", "us", "es_ve" };

	public static final String version = "1.0";
	public static String userip = "152.78.65.193";

	public static final int ResultLimit = 8;
	public static final int rsz = 8;
	public static final int Start = 8;

	public static void usage() {

		System.out.println("");
		System.out.println("");
	}

	public static void main(String[] args) {

		StringBuilder urlWithParams = new StringBuilder();
		urlWithParams.append(endPoint);
		urlWithParams.append("v=" + version);
		urlWithParams.append("&userip=" + userip);
		urlWithParams.append("&rsz=" + rsz);

		for (int i = 0; i < TOPICS.length; i++) {
			String urlWithParams1 = urlWithParams + "&topic=" + TOPICS[i];
			for (int j = 0; j < Start * ResultLimit; j += ResultLimit) {
				String urlWithParams2 = urlWithParams1 + "&start=" + j;

				System.out.println(urlWithParams2);
				

			}
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
