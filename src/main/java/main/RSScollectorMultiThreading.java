package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import dataObject.rss.Threading;

public class RSScollectorMultiThreading {
	final static Logger logger = Logger
			.getLogger(RSScollectorMultiThreading.class);
	static final int threadNo = 16;

	public static void main(String[] args) {
		Scanner s;
		ArrayList<String> list = null;
		try {
			s = new Scanner(new File(args[0]));
			list = new ArrayList<String>();
			while (s.hasNext()) {
				list.add(s.next());
			}
			s.close();
			ExecutorService executorService = Executors
					.newFixedThreadPool(threadNo);
			int division = (list.size() / threadNo) + 1;
			for (int i = 0; i < list.size(); i += division) {
				int end = division + i;
				if (i + division > list.size()) {
					executorService.execute(new Threading(list.subList(i,
							list.size())));
				} else {
					executorService
							.execute(new Threading(list.subList(i, end)));
				}

			}

		} catch (FileNotFoundException e1) {
			logger.error(e1);
		}
	}

	public static void ProcessList(List<String> list) {

	}
}
