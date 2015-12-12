package test.selenium.googlenews;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestTimeAgo {
	static  DateFormat df2 = new SimpleDateFormat(
			"MMM dd, yyyy");
	static DateFormat df = new SimpleDateFormat(
			"EEE, d MMM yyyy HH:mm:ss Z");
	public static void main(String[] args) {
		
//		Date date2 = new Date();
//		Date currentDate = new Date();
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(currentDate);
//		date2 = cal.getTime();
//		System.out.println(df2.format(date2));
		String d1 = "‎Nov 15, 2015";
		 
		ParsePosition p = new ParsePosition(1);
		Date date = df2.parse(d1, p);
		System.out.println(df.format(date));
		

	}

}
