package bob.eve.walle.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormat {
	public static String format(String format, String time) {
		String result = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			Date date = df.parse(time);
			SimpleDateFormat df1 = new SimpleDateFormat(format);
			result = df1.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
}
