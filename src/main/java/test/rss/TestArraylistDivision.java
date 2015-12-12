package test.rss;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestArraylistDivision {

	public static void main(String[] args) {
		Scanner s;
		ArrayList<String> list = null;
		int threadNo = 8;
		try {
			s = new Scanner(
					new File(
							"/Users/user/Eclipse/GoogleNews/Multithreading/allWorkingRSS.txt"));
			list = new ArrayList<String>();
			while (s.hasNext()) {
				list.add(s.next());
			}
			s.close();

			System.out.println("size " + list.size());

			int division = (list.size() / threadNo)+1;
			for (int i = 0; i < list.size(); i += division) {
				int end = division + i;
				if (i+division > list.size()) {
					insertArraylist(list.subList(i, list.size()));
				} else {
					insertArraylist(list.subList(i, end));
				}

			}

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static void insertArraylist(List<String> urls) {

		System.out.println(urls.size());
	}

}
