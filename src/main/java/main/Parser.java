package main;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dataObject.NewsHolder;
import dataObject.Result;

public class Parser {

	public static void main(String[] args) {
		Gson gson = new Gson();
		Type listType = new TypeToken<NewsHolder>() {
		}.getType();
		String file = "/Users/user/Eclipse/GoogleNews/googleNews/ar_ae_business_1_.json";
		String requestURi = null;
		try {
			requestURi = readFile(file, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NewsHolder newsHolder;
		newsHolder = gson.fromJson(requestURi, listType);
		List<Result> resultList = newsHolder.getResponseData().getResults();
		for (int i = 0; i < resultList.size(); i++) {
			System.out.println(resultList.get(i).getTitle());
		}

	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
