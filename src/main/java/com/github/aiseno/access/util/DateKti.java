package com.github.aiseno.access.util;

import java.util.Date;
import java.util.Locale;

import org.springframework.format.datetime.DateFormatter;

/**
 * @author admin
 */
public class DateKti {

	private DateKti() {

	}

	public static String format(Date date) {
		return Dfs.build().print(date, Locale.SIMPLIFIED_CHINESE);
	}

	public static String format(Date date, String format) {
		return Dfs.build(format).print(date, Locale.SIMPLIFIED_CHINESE);
	}

	public static String format(long dateTime) {
		return format(new Date(dateTime));
	}

	public static String format(long dateTime, String format) {
		return format(new Date(dateTime), format);
	}

	private static class Dfs {
		
		public static DateFormatter build(String format) {
			return new DateFormatter(format);
		}

		public static DateFormatter build() {
			return new DateFormatter("yyyy-MM-dd HH:mm:ss");
		}
	}

}
