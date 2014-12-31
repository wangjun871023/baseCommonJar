package com.macrosoft.common.date;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import com.macrosoft.common.string.StringUtils;

public class DateUtils {
	private static int ONE_DAY_SECOND = 86400;
	private static final TimeZone GMT = TimeZone.getTimeZone("GMT+8:00");
	private static final String[] SEPARATE = { "-", ":" };
	public static final long ONE_DAY_TIME = 86400000L;
	private static boolean SET_TIMEZONE = false;
	public static final String FOR_Y = "yyyy";
	public static final String FOR_YM = "yyyyMM";
	public static final String FOR_YMD = "yyyyMMdd";
	public static final String FOR_YMDH = "yyyyMMddHH";
	public static final String FOR_YMDHM = "yyyyMMddHHmm";
	public static final String FOR_YMDHMS = "yyyyMMddHHmmss";
	public static final String FOR_YMDHMSSSS = "yyyyMMddHHmmssSSS";
	public static final String FOR_YMD_SEPARATE = "yyyy-MM-dd";
	public static final String FOR_YMDHMS_SEPARATE = "yyyy-MM-dd HH:mm:ss";
	private static final SimpleDateFormat DEFAULT_FORMAT = new SimpleDateFormat();
	private static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat(
			"yyyy", Locale.CHINESE);
	private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat(
			"yyyyMM", Locale.CHINESE);
	private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat(
			"yyyyMMdd", Locale.CHINESE);
	private static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat(
			"yyyyMMddHH", Locale.CHINESE);
	private static final SimpleDateFormat MINUTE_FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmm", Locale.CHINESE);
	private static final SimpleDateFormat SECOND_FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmmss", Locale.CHINESE);
	private static final SimpleDateFormat MILLISECOND_FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS", Locale.CHINESE);
	private static final SimpleDateFormat SEPARATE_DAY_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.CHINESE);
	private static final SimpleDateFormat SEPARATE_SECOND_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
	private static final DateFormatSymbols SYMBOLS = DateFormatSymbols
			.getInstance(Locale.CHINESE);
	private static final String[] SYMBOLS_DAYOFMONTH = { "null", "一", "二", "三",
			"四", "五", "六", "七", "八", "九", "十", "十一", "十二", "十三", "十四", "十五",
			"十六", "十七", "十八", "十九", "二十", "二十一", "二十二", "二十三", "二十四", "二十五",
			"二十六", "二十七", "二十八", "二十九", "三十", "三十一" };
	private static final Random random;
	private static SimpleDateFormat getSimpleDateFormat(String format) {
		if ((format == null) || ("".equals(format.trim()))) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format,
				Locale.CHINESE);
		simpleDateFormat.setTimeZone(GMT);
		return simpleDateFormat;
	}

	/**
	 * 得到两个时间之间相差的秒数
	 * 
	 * @param begintime
	 * @param endtime
	 * @return
	 */
	public static long getSecond(long begintime, long endtime) {
		long diff = endtime - begintime;
		long result = diff / (1000);
		return result;
	}
	
	
	
	/**
	 * 判断该字符串是否为日期格式
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static boolean isDate(String dateStr, String pattern) {
		boolean result = false;
		if (StringUtils.isEmpty(dateStr) == false) {
			SimpleDateFormat formatter = null;
			Date date = null;
			try {
				if (StringUtils.isEmpty(pattern) == true) {
					pattern = "yyyy-MM-dd";
				}
				formatter = new SimpleDateFormat(pattern);
				date = formatter.parse(dateStr);
				result = true;
			} catch (Exception ex) {
				date = null;
				result = false;
				ex.printStackTrace();
			}
			if (date == null) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * 得到服务器系统日期和时间 ,格式 yyyy-MM-dd HH:mm:ss.SSS
	 * 
	 * @return String
	 */
	public static String getServerCurrentDateAndTime() {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		java.util.Date date = new Date();
		return getServerCurrentDateAndTime(date, pattern);
	}


	public static synchronized String dateToStr(Date date, Resolution resolution) {
		if (date == null) {
			throw new IllegalArgumentException("参数时间为空!");
		}
		return timeToStr(date.getTime(), resolution);
	}

	public static synchronized String timeToStr(long time, Resolution resolution) {
		Calendar calInstance = Calendar.getInstance(GMT);
		calInstance.setTimeInMillis(round(time, resolution));
		Date date = calInstance.getTime();
		if (resolution == Resolution.YEAR)
			return YEAR_FORMAT.format(date);
		if (resolution == Resolution.MONTH)
			return MONTH_FORMAT.format(date);
		if (resolution == Resolution.DAY)
			return DAY_FORMAT.format(date);
		if (resolution == Resolution.HOUR)
			return HOUR_FORMAT.format(date);
		if (resolution == Resolution.MINUTE)
			return MINUTE_FORMAT.format(date);
		if (resolution == Resolution.SECOND)
			return SECOND_FORMAT.format(date);
		if (resolution == Resolution.MILLISECOND)
			return MILLISECOND_FORMAT.format(date);
		if (resolution == Resolution.SEPARATEDAY)
			return SEPARATE_DAY_FORMAT.format(date);
		if (resolution == Resolution.SEPARATESECOND) {
			return SEPARATE_SECOND_FORMAT.format(date);
		}
		throw new IllegalArgumentException("unknown resolution " + resolution);
	}

	public static synchronized long strToTime(String dateString) {
		return strToDate(dateString).getTime();
	}

	public static synchronized Date strToDate(String dateStr) {
		try {
			if ((dateStr.indexOf(SEPARATE[0]) > -1)
					&& (dateStr.indexOf(SEPARATE[1]) > -1))
				return SEPARATE_SECOND_FORMAT.parse(dateStr);
			if (dateStr.indexOf(SEPARATE[0]) > -1)
				return SEPARATE_DAY_FORMAT.parse(dateStr);
			if (dateStr.length() == 4)
				return YEAR_FORMAT.parse(dateStr);
			if (dateStr.length() == 6)
				return MONTH_FORMAT.parse(dateStr);
			if (dateStr.length() == 8)
				return DAY_FORMAT.parse(dateStr);
			if (dateStr.length() == 10)
				return HOUR_FORMAT.parse(dateStr);
			if (dateStr.length() == 12)
				return MINUTE_FORMAT.parse(dateStr);
			if (dateStr.length() == 14)
				return SECOND_FORMAT.parse(dateStr);
			if (dateStr.length() == 17) {
				return MILLISECOND_FORMAT.parse(dateStr);
			}
			return DEFAULT_FORMAT.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static synchronized Date round(Date date, Resolution resolution) {
		return new Date(round(date.getTime(), resolution));
	}

	public static synchronized long round(long time, Resolution resolution) {
		Calendar calInstance = Calendar.getInstance(GMT);
		calInstance.setTimeInMillis(time);
		if (resolution == Resolution.YEAR) {
			calInstance.set(2, 0);
			calInstance.set(5, 1);
			calInstance.set(11, 0);
			calInstance.set(12, 0);
			calInstance.set(13, 0);
			calInstance.set(14, 0);
		} else if (resolution == Resolution.MONTH) {
			calInstance.set(5, 1);
			calInstance.set(11, 0);
			calInstance.set(12, 0);
			calInstance.set(13, 0);
			calInstance.set(14, 0);
		} else if ((resolution == Resolution.DAY)
				|| (resolution == Resolution.SEPARATEDAY)) {
			calInstance.set(11, 0);
			calInstance.set(12, 0);
			calInstance.set(13, 0);
			calInstance.set(14, 0);
		} else if (resolution == Resolution.HOUR) {
			calInstance.set(12, 0);
			calInstance.set(13, 0);
			calInstance.set(14, 0);
		} else if (resolution == Resolution.MINUTE) {
			calInstance.set(13, 0);
			calInstance.set(14, 0);
		} else if ((resolution == Resolution.SECOND)
				|| (resolution == Resolution.SEPARATESECOND)) {
			calInstance.set(14, 0);
		} else if (resolution != Resolution.MILLISECOND) {
			throw new IllegalArgumentException("unknown resolution "
					+ resolution);
		}
		return calInstance.getTimeInMillis();
	}

	public static String dateToStr(Date date, String format) {
		return getSimpleDateFormat(format).format(date);
	}

	public static Date strToDate(String dateString, String format) {
		try {
			return getSimpleDateFormat(format).parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String timeToStr(long datetime, String format) {
		return getSimpleDateFormat(format).format(new Date(datetime));
	}

	private static String calendarDayStr(Calendar calendar, String format) {
		return getSimpleDateFormat(format).format(
				new Date(calendar.getTimeInMillis()));
	}

	public static String nowMonthFirstDayStr(String format) {
		return firstDayStr(new Date(), format);
	}

	public static String nowMonthLastDayStr(String format) {
		return lastDayStr(new Date(), format);
	}

	public static String firstDayStr(Date date, String format) {
		return firstDayStr(date.getTime(), format);
	}

	public static String lastDayStr(Date date, String format) {
		return lastDayStr(date.getTime(), format);
	}

	public static String firstDayStr(long datetime, String format) {
		Calendar calendar = Calendar.getInstance(GMT);
		calendar.setTimeInMillis(datetime);
		calendar.set(5, calendar.getActualMinimum(5));
		return calendarDayStr(calendar, format);
	}

	public static String lastDayStr(long datetime, String format) {
		Calendar calendar = Calendar.getInstance(GMT);
		calendar.setTimeInMillis(datetime);
		calendar.set(5, calendar.getActualMaximum(5));
		return calendarDayStr(calendar, format);
	}

	private static Calendar getCalendar(long datetime) {
		Calendar calendar = Calendar.getInstance(GMT);
		calendar.setTimeInMillis(datetime);
		return calendar;
	}

	public static int year(long millis) {
		return getCalendar(millis).get(1);
	}

	public static int month(long millis) {
		return getCalendar(millis).get(2);
	}

	public static int monthFull(long millis) {
		return month(millis) + 1;
	}

	public static int weekOfYear(long millis) {
		return getCalendar(millis).get(3);
	}

	public static int date(long millis) {
		return getCalendar(millis).get(5);
	}

	public static int hour(long millis) {
		return getCalendar(millis).get(10);
	}

	public static int minute(long millis) {
		return getCalendar(millis).get(12);
	}

	public static int second(long millis) {
		return getCalendar(millis).get(13);
	}

	public static int hourOfDay(long millis) {
		return getCalendar(millis).get(11);
	}

	public static int dayOfMonth(long millis) {
		return getCalendar(millis).get(5);
	}

	public static int dayOfWeek(long millis) {
		return getCalendar(millis).get(7);
	}

	public static int dayOfYear(long millis) {
		return getCalendar(millis).get(6);
	}

	public static int firstDayOfWeek(long millis) {
		int dayOfWeek = 1 - dayOfWeek(millis);
		Date date = removeDay(millis, dayOfWeek);
		return dayOfMonth(date.getTime());
	}

	public static int lastDayOfWeek(long millis) {
		int dayOfWeek = 7 - dayOfWeek(millis);
		return dayOfMonth(removeDay(millis, dayOfWeek).getTime());
	}

	public static int weekCount(long millis) {
		return getCalendar(millis).getActualMaximum(3);
	}

	public static String monthName(long millis) {
		return displayName(millis, 2);
	}

	public static String monthName(int index) {
		String[] months = SYMBOLS.getMonths();
		if ((index < 0) || (index >= months.length - 1)) {
			return "null";
		}
		return months[index];
	}

	public static String monthShortName(int index) {
		String[] months = SYMBOLS.getShortMonths();
		if ((index < 0) || (index >= months.length)) {
			return "null";
		}
		return months[index];
	}

	public static String dayOfWeekName(long millis) {
		return displayName(millis, 7);
	}

	public static String dayOfWeekName(int index) {
		String[] weeks = SYMBOLS.getWeekdays();
		if ((index <= 0) || (index >= weeks.length)) {
			return "null";
		}
		return weeks[index];
	}

	public static String dayOfWeekShortName(int index) {
		String[] weeks = SYMBOLS.getShortWeekdays();
		if ((index <= 0) || (index >= weeks.length)) {
			return "null";
		}
		return weeks[index];
	}

	public static String ampmName(long millis) {
		return displayName(millis, 9);
	}

	private static String displayName(long millis, int field) {
		return getCalendar(millis).getDisplayName(field, 0, Locale.CHINESE);
	}

	public static String dayOfMonthName(int index) {
		if ((index < 0) || (index > SYMBOLS_DAYOFMONTH.length)) {
			return "null";
		}
		return SYMBOLS_DAYOFMONTH[index];
	}

	public static String removeDayToStr(Date date, String format, int tian) {
		if (date == null) {
			throw new IllegalArgumentException("参数时间为空! date=" + date);
		}
		long datetime = date.getTime() + 86400000L * tian;
		return getSimpleDateFormat(format).format(new Date(datetime));
	}

	public static Date removeDay(long datetime, int tian) {
		datetime += 86400000L * tian;
		return new Date(datetime);
	}

	public static synchronized String randomDateToStr(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("参数时间为空! date=" + date);
		}
		return timeToStr(date.getTime(), Resolution.MILLISECOND);
	}

	public static boolean equals(String dateStr1, String dateStr2) {
		return ((dateStr1 == null) && (dateStr2 == null))
				|| ((dateStr1 != null) && (dateStr2 != null) && (equals(
						strToDate(dateStr1), strToDate(dateStr2))));
	}

	public static boolean equals(Date date1, Date date2) {
		return ((date1 == null) && (date2 == null))
				|| ((date1 != null) && (date2 != null) && (date1.equals(date2)));
	}

	public static boolean equals(long datetime1, long datetime2) {
		return datetime1 == datetime2;
	}

	public static String after(String dateStr1, String dateStr2) {
		String returnStr = "";
		if ((dateStr1 != null) && (dateStr2 != null)) {
			Date date1 = strToDate(dateStr1);
			Date date2 = strToDate(dateStr2);

			returnStr = date1.after(date2) ? dateStr1 : dateStr2;
		} else {
			returnStr = dateStr1 == null ? dateStr2 : dateStr1;
		}
		return returnStr;
	}

	public static String before(String dateStr1, String dateStr2) {
		String maxStr = after(dateStr1, dateStr2);
		if ((maxStr == null) || ("".equals(maxStr))) {
			return null;
		}
		return maxStr.equals(dateStr1) ? dateStr2 : dateStr1;
	}

	public static String UTCToMinDayStr(String dateStr) {
		Date date = UTC(strToDate(dateStr), 0, 0, 0);
		return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String UTCToMaxDayStr(String dateStr) {
		Date date = UTC(strToDate(dateStr), 23, 59, 59);
		return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String UTC(String dateStr, int hrs, int min, int sec) {
		Date date = UTC(strToDate(dateStr), hrs, min, sec);
		return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date UTC(Date date, int hrs, int min, int sec) {
		Calendar calendar = Calendar.getInstance(GMT);
		calendar.setTime(date);
		calendar.set(10, hrs);
		calendar.set(12, min);
		calendar.set(13, sec);
		return calendar.getTime();
	}

	public static Date UTC(int year, int month, int date, int hrs, int min,
			int sec) {
		Calendar calendar = Calendar.getInstance(GMT);
		calendar.set(1, year);
		calendar.set(2, month);
		calendar.set(5, date);
		calendar.set(10, hrs);
		calendar.set(12, min);
		calendar.set(13, sec);
		return calendar.getTime();
	}

	public static long UTC(long datetime, int hrs, int min, int sec) {
		Calendar calendar = Calendar.getInstance(GMT);
		calendar.setTimeInMillis(datetime);
		calendar.set(10, hrs);
		calendar.set(12, min);
		calendar.set(13, sec);
		return calendar.getTimeInMillis();
	}

	public static long getTimeDiff(Date begin, Date end) {
		long result = 0L;
		if ((begin != null) && (end != null)) {
			result = begin.getTime() - end.getTime();
		} else if ((begin != null) && (end == null)) {
			result = begin.getTime();
		}

		return result;
	}

	public static String getDateToStr(Date date) {
		String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
		return getServerCurrentDateAndTime(date, pattern);
	}

	public static String getDateYearMonthDay(Date date) {
		String pattern = "yyyy-MM-dd";
		return getServerCurrentDateAndTime(date, pattern);
	}

	public static String getServerYearMonthDay(Date date) {
		String pattern = "yyyy-MM-dd";
		return getServerCurrentDateAndTime(date, pattern);
	}

	public static String getCurrentDate() {
		String pattern = "yyyy-MM-dd";
		Date date = new Date();
		return getServerCurrentDateAndTime(date, pattern);
	}

	public static String getCurrentSimpleDate() {
		String pattern = "yyyyMMdd";
		Date date = new Date();
		return getServerCurrentDateAndTime(date, pattern);
	}

	public static String getCurrentYearMonth() {
		String pattern = "yyyy-MM";
		Date date = new Date();
		return getServerCurrentDateAndTime(date, pattern);
	}

	/**
	 * 从日期字符串中得到年份
	 * 
	 * @return String
	 */
	public static String getYearFromDateStr(String dateStr) {
		String result = null;
		dateStr = StringUtils.checkNull(dateStr, "");
		if (dateStr.length() == 0) {
			// 提取服务器年份
			result = getServerSysCurrentYear();
		} else {
			if (dateStr.length() > 4) {
				result = dateStr.substring(0, 4);
			} else {
				// 提取服务器年份
				result = getServerSysCurrentYear();
			}
		}
		return result;
	}

	/**
	 * @return String
	 */
	public static String getServerSysCurrentYear() {
		java.util.Date date = null;
		String pattern = "yyyy";
		date = new Date();
		return getServerCurrentDateAndTime(date, pattern);
	}

	/**
	 * 
	 * @param current_time
	 * @return
	 */
	public static String getServerCurrentDate(String current_time) {
		String result = "";
		long currentLong = 0;
		try {
			if (StringUtils.isEmpty(current_time) == false) {
				try {
					currentLong = Long.parseLong(current_time);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				result = getServerCurrentDate(currentLong);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param current
	 * @return
	 */
	public static String getServerCurrentDate(long current) {
		String result = "";
		try {
			String pattern = "yyyy-MM-dd HH:mm:ss";
			Date date = new Date(current);
			if (date != null) {
				result = getServerCurrentDateAndTime(date, pattern);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 得到服务器系统日期和时间,日期的字符串格式
	 * 
	 * @return String
	 */
	public static String getServerCurrentYearMonthDay() {
		String pattern = "yyyy-MM-dd";
		Date date = new Date();
		return getServerCurrentDateAndTime(date, pattern);
	}
	/**
	 * 得到日期的字符串格式
	 * 
	 * @param date
	 * @return String
	 */
	public static String getServerCurrentDateAndTime(Date date) {
		String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
		return getServerCurrentDateAndTime(date, pattern);
	}

	public static String getServerCurrentDateAndTime(Date date, String pattern) {
		String result = null;
		SimpleDateFormat df = null;
		try {
			if (StringUtils.isEmpty(pattern) == true) {
				pattern = "yyyy-MM-dd";
			}
			df = new SimpleDateFormat(pattern);
			if (!SET_TIMEZONE) {
				TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
				TimeZone.setDefault(tz);
				SET_TIMEZONE = true;
			}
			if (date != null) {
				result = df.format(date);
			}
			if (StringUtils.isEmpty(result) == true)
				result = "";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static String getServerSysDateAndTimeAsFileName() {
		String result = null;
		Date currentDate = null;
		SimpleDateFormat df = null;
		String yearAndMonth = null;
		String hourAndMinute = null;
		df = new SimpleDateFormat("yyyy_MM_dd");
		currentDate = new Date();
		yearAndMonth = df.format(currentDate);
		df = new SimpleDateFormat("_HH_mm_ss");
		hourAndMinute = df.format(currentDate);
		result = yearAndMonth + hourAndMinute;
		return result;
	}

	public static String getNow(Date date) {
		String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
		if (date == null) {
			date = new Date();
		}
		return getServerCurrentDateAndTime(date, pattern);
	}

	public static String afterDay(String dateStr, int day) {
		String result = afterDay(dateStr, day, "yyyy-MM-dd HH:mm:ss.SSS");
		return result;
	}

	public static String afterDay(Date date, int day, String pattern) {
		String result = null;
		SimpleDateFormat formatter = null;
		long myTime = 0L;
		try {
			if (StringUtils.isEmpty(pattern) == true) {
				pattern = "yyyy-MM-dd";
			}
			if (date != null) {
				myTime = date.getTime() / 1000L - day * ONE_DAY_SECOND;
				date.setTime(myTime * 1000L);
				result = getServerCurrentDateAndTime(date, pattern);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result = getServerCurrentYearMonthDay();
		}
		return result;
	}

	public static String afterDay(String dateStr, int day, String pattern) {
		String result = null;
		Date date = null;
		long myTime = 0L;
		try {
			date = getDateByStr(dateStr, pattern);
			myTime = date.getTime() / 1000L - day * ONE_DAY_SECOND;
			date.setTime(myTime * 1000L);
			result = getServerCurrentDateAndTime(date, pattern);
		} catch (Exception ex) {
			ex.printStackTrace();
			result = getServerCurrentYearMonthDay();
		}
		return result;
	}

	public static String afterDay(int day) {
		String result = null;
		Date date = null;
		String pattern = "yyyy-MM-dd";
		long myTime = 0L;
		try {
			date = new Date();
			myTime = date.getTime() / 1000L - day * ONE_DAY_SECOND;
			date.setTime(myTime * 1000L);
			result = getServerCurrentDateAndTime(date, pattern);
		} catch (Exception ex) {
			ex.printStackTrace();
			result = getServerCurrentYearMonthDay();
		}
		return result;
	}

	public static String afterMinute(String dateStr, int minute, String pattern) {
		String result = null;
		Date date = null;
		long myTime = 0L;
		try {
			date = getDateByStr(dateStr, pattern);
			myTime = date.getTime() / 1000L - minute * 60;
			date.setTime(myTime * 1000L);
			result = getServerCurrentDateAndTime(date, pattern);
		} catch (Exception ex) {
			ex.printStackTrace();
			result = getServerCurrentYearMonthDay();
		}
		return result;
	}

	public static String afterYear(String dateStr, int year) {
		GregorianCalendar now = null;
		String result = null;
		Date date = null;
		try {
			date = getDateByStr(dateStr);
			if (date != null) {
				now = new GregorianCalendar();
				now.setTime(date);
				now.add(1, year);
				result = getYearMonthDay(now);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static String getYearMonthDay(GregorianCalendar date) {
		String pattern = "yyyy-MM-dd";
		if (date != null) {
			return getServerCurrentDateAndTime(date.getTime(), pattern);
		}

		return "";
	}

	public static String getYearMonthDay(Date date) {
		String pattern = "yyyy-MM-dd";
		return getServerCurrentDateAndTime(date, pattern);
	}

	public static String getYearMonthDay(Timestamp date) {
		String pattern = "yyyy-MM-dd";
		return getServerCurrentDateAndTime(date, pattern);
	}

	public static String afterMonth(Date date, int month, String pattern) {
		GregorianCalendar now = null;
		String result = null;
		if (date != null) {
			now = new GregorianCalendar();
			now.setTime(date);
			now.add(2, month);
			result = getYearMonthDay(now);
		}
		return result;
	}

	public static String afterMonth(String dateStr, int month, String pattern) {
		GregorianCalendar now = null;
		String result = null;
		Date date = null;
		date = getDateByStr(dateStr, pattern);
		if (date != null) {
			now = new GregorianCalendar();
			now.setTime(date);
			now.add(2, month);
			result = getYearMonthDay(now);
		}
		return result;
	}

	public static String afterMonth(String dateStr, int month) {
		GregorianCalendar now = null;
		String result = null;
		Date date = null;
		date = getDateByStr(dateStr);
		if (date != null) {
			now = new GregorianCalendar();
			now.setTime(date);
			now.add(2, month);
			result = getYearMonthDay(now);
		}
		return result;
	}

	public static Date getDateByStr(String dateStr) {
		SimpleDateFormat formatter = null;
		Date result = null;
		String pattern = null;
		try {
			if ((!StringUtils.isEmpty(dateStr)) && (dateStr.length() > 9)) {
				if (dateStr.length() >= 19) {
					pattern = "yyyy-MM-dd HH:mm:ss";
				} else {
					pattern = "yyyy-MM-dd";
				}
				formatter = new SimpleDateFormat(pattern);
				result = formatter.parse(dateStr);
			}
		} catch (ParseException ex) {
			pattern = "yyyy/MM/dd";
			formatter = new SimpleDateFormat(pattern);
			try {
				result = formatter.parse(dateStr);
			} catch (ParseException ex2) {
				ex2.printStackTrace();
			}
		}
		return result;
	}

	public static Date getDateByStr(String dateStr, String pattern) {
		SimpleDateFormat formatter = null;
		Date date = null;
		try {
			if ((!StringUtils.isEmpty(dateStr)) && (dateStr.length() >= 9)) {
				if (StringUtils.isEmpty(pattern) == true) {
					pattern = "yyyy-MM-dd";
				}
				formatter = new SimpleDateFormat(pattern);
				date = formatter.parse(dateStr);
			}
		} catch (ParseException pareseEx) {
			pattern = "yyyy/MM/dd";
			formatter = new SimpleDateFormat(pattern);
			try {
				date = formatter.parse(dateStr);
			} catch (ParseException pareseEx2) {
				pareseEx2.printStackTrace();
			}
		}
		return date;
	}

	public static int compareDate(String date1, String date2) {
		return date1.compareTo(date2);
	}

	public static int compareTime(String time1, String time2) {
		return time1.compareTo(time2);
	}

	/**
	 * 得到服务器系统日期
	 * 
	 * @return String
	 */
	public static String getServerCurrentSysDate() {
		String pattern = "yyyy-MM-dd";
		java.util.Date date = new java.util.Date();
		return getServerCurrentDateAndTime(date, pattern);
	}

	/**
	 * 得到服务器系统日期
	 * 
	 * @return String
	 */
	public static String getServerCurrentSysDateChStr() {
		String pattern = "yyyy年MM月dd日";
		java.util.Date date = new java.util.Date();
		return getServerCurrentDateAndTime(date, pattern);
	}

	public static String getTime(long time) {
		String result = "";
		time = time / 1000;
		if (time > 24 * 60 * 60) {

		} else {
			if (time > 60 * 60) {

			} else {

			}
		}
		return result;
	}
	public static String getServerSysCurrentMonth() {
		Date date = null;
		String pattern = "MM";
		date = new Date();
		return getServerCurrentDateAndTime(date, pattern);
	}

	public static String getServerSysCurrentDay() {
		Date date = null;
		String pattern = "dd";
		date = new Date();
		return getServerCurrentDateAndTime(date, pattern);
	}

	public static String getServerSysCurrentYearMonthAndDay() {
		Date date = null;
		String pattern = "yyyyMMdd";
		date = new Date();
		return getServerCurrentDateAndTime(date, pattern);
	}

	public static boolean getBooleanValue(String booleanStr) {
		boolean result = false;
		if ((booleanStr == null) || (booleanStr.length() == 0)) {
			result = false;
		}
		booleanStr = booleanStr.trim();
		if ("TRUE".compareToIgnoreCase(booleanStr) == 0) {
			result = true;
		}
		if ("FALSE".compareToIgnoreCase(booleanStr) == 0) {
			result = false;
		}
		return result;
	}

	public static String getYearOfDate(Date date) {
		String pattern = "yyyy";
		return getServerCurrentDateAndTime(date, pattern);
	}

	public static String getMonthOfDate(Date date) {
		String pattern = "MM";
		return getServerCurrentDateAndTime(date, pattern);
	}

	public static String getDayOfDate(Date date) {
		String pattern = "dd";
		return getServerCurrentDateAndTime(date, pattern);
	}

	public static String getWeekOfDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		double d1980 = 3852.0D;
		double dl = Math.floor(date.getTime() / 1000L / 60L / 60L / 24L);
		String week = (int) Math.floor((dl - d1980) / 7.0D) + "";
		return week;
	}

	public static String getServerSysDateAndTimeAsCode() {
		int n = Math.abs(Thread.currentThread().getName().hashCode() % 85843);
		String pattern = "yyyyMMddHHmmssSSS";

		Date date = new Date();
		return getServerCurrentSysDateAndTime(date, pattern)
				+ new DecimalFormat("00000").format(random.nextInt(n));
	}

	public static String getServerSysDateAndTimeAsShortCode() {
		int n = Math.abs(Thread.currentThread().getName().hashCode() % 85843);
		return System.currentTimeMillis() + ""
				+ new DecimalFormat("00").format(random.nextInt(n));
	}

	public static Long getServerSysDateAndTimeAsLong() {
		int n = Math.abs(Thread.currentThread().getName().hashCode() % 85843);
		return Long.valueOf(Long.parseLong(System.currentTimeMillis() + ""
				+ new DecimalFormat("00").format(random.nextInt(n))));
	}

	public static String getServerCurrentSysDateAndTime(Date date,
			String pattern) {
		String result = null;
		SimpleDateFormat df = null;
		try {
			df = new SimpleDateFormat(pattern);
			if (date != null) {
				result = df.format(date);
			}
			if ((result == null) || (result.trim().length() <= 0))
				result = "";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	static {
		DEFAULT_FORMAT.setTimeZone(GMT);
		YEAR_FORMAT.setTimeZone(GMT);
		MONTH_FORMAT.setTimeZone(GMT);
		DAY_FORMAT.setTimeZone(GMT);
		HOUR_FORMAT.setTimeZone(GMT);
		MINUTE_FORMAT.setTimeZone(GMT);
		SECOND_FORMAT.setTimeZone(GMT);
		MILLISECOND_FORMAT.setTimeZone(GMT);
		SEPARATE_DAY_FORMAT.setTimeZone(GMT);
		SEPARATE_SECOND_FORMAT.setTimeZone(GMT);

		SYMBOLS.setShortMonths(new String[] { "一", "二", "三", "四", "五", "六",
				"七", "八", "九", "十", "十一", "十二" });
		SYMBOLS.setShortWeekdays(new String[] { "null", "日", "一", "二", "三",
				"四", "五", "六" });

		random = new Random(99999L);
	}

	public static class Resolution {
		public static final Resolution YEAR = new Resolution("year");
		public static final Resolution MONTH = new Resolution("month");
		public static final Resolution DAY = new Resolution("day");
		public static final Resolution HOUR = new Resolution("hour");
		public static final Resolution MINUTE = new Resolution("minute");
		public static final Resolution SECOND = new Resolution("second");
		public static final Resolution MILLISECOND = new Resolution(
				"millisecond");
		public static final Resolution SEPARATEDAY = new Resolution(
				"separateday");
		public static final Resolution SEPARATESECOND = new Resolution(
				"separatesecond");
		private String resolution;

		private Resolution() {
		}

		private Resolution(String resolution) {
			this.resolution = resolution;
		}

		public String toString() {
			return this.resolution;
		}
	}
}