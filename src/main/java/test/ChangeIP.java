package test;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangeIP {

	public static void main(String[] args) {
		String url = "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&userip=234.543.74.267&&rsz=8";
		System.out.println(changeIP(url));

	}

	private static String changeIP(String url) {
		String newURL =null;

		Pattern patter = Pattern.compile("&userip=([0-9]+\\.[0-9]+\\.[0-9]+\\.([0-9]+))");
		Matcher matcher = patter.matcher(url);
		String lastIPtoken=null;
		String fullIP=null;
		if (matcher.find()) {
			fullIP = matcher.group(1);
			lastIPtoken = matcher.group(2);
		}
		
		Random ipToken = new Random();
		Integer generatedToken =  ipToken.nextInt(255);
		String newIP = fullIP.replace(lastIPtoken, generatedToken.toString());
		newURL = url.replaceAll(fullIP,newIP);
		System.out.println("Old ip " + fullIP);
		System.out.println("new ip " + newIP);

		return newURL;
	}

}
