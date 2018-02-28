package com.svnnotification.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * CalendarUtil 
 * @author Natsume
 * @date 2018-01-09
 * @email cug_zg@163.com
 *
 */
public class CalendarUtil {
	/**
	 * convert UTC to CST
	 * 
	 * @param UTCStr
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static String UTCToCST(String UTCStr, String format) throws ParseException {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		date = sdf.parse(UTCStr);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
	}
}
