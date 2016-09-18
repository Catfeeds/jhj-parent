package com.meijia.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.google.zxing.client.result.ResultParser;

public class DateUtil {

	public static final String DEFAULT_PATTERN = "yyyy-MM-dd";

	public static final String DEFAULT_TIME = "HH:mm:ss";

	public static final String DEFAULT_FULL_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final Calendar DEFAULT_CALENDAR = Calendar.getInstance();

	private static final int[] quarters = { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4 };

	/**
	 * 返回固定格式的日期
	 */
	public static String getDefaultDate(Object date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(date);
	}

	/**
	 * 返回固定格式的时间
	 */
	public static String getDefaultTime(Object Time) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_TIME);
		return sdf.format(Time);
	}

	/**
	 * 判断字符串是否为日期
	 * 
	 * @param day
	 *            :yyyy-MM-dd
	 * @return
	 */
	public static boolean isDate(String date) {
		return isDate(date, DEFAULT_PATTERN);
	}

	/**
	 * 判断是否为日期
	 * 
	 * @param date
	 * @param pattern
	 * @return boolean
	 */
	public static boolean isDate(String date, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			df.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 当前时间
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getNow() {
		return DateUtil.format(new Date(), DEFAULT_FULL_PATTERN);
	}

	/**
	 * 当前时间
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static Date getNowOfDate() {
		return new Date();
	}

	/**
	 * 当前时间
	 * 
	 * @return pattern格式
	 */
	public static String getNow(String pattern) {
		return DateUtil.format(new Date(), pattern);
	}

	/**
	 * 当前日期
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getToday() {
		return DateUtil.format(new Date(), DEFAULT_PATTERN);
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date date) {
		DateFormat df = new SimpleDateFormat(DEFAULT_PATTERN);
		return df.format(date);
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDateFull(Date date) {
		DateFormat df = new SimpleDateFormat(DEFAULT_FULL_PATTERN);
		return df.format(date);
	}

	/**
	 * 字符串转换为日期对象 String --> Date
	 * 
	 * @param strDate
	 * @param dateFormat
	 * @return
	 */
	public static Date parse(String strDate, String dateFormat) {
		if (strDate == null || strDate.length() == 0) {
			return null;
		}
		Date date = null;
		DateFormat df = new SimpleDateFormat(dateFormat);
		try {
			date = df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 默认格式:yyyy-MM-dd
	 * 
	 * @param strDate
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static Date parse(String strDate) {
		return parse(strDate, DEFAULT_PATTERN);
	}

	/**
	 * 格式:yyyy-MM-dd HH:mm:ss
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date parseFull(String strDate) {
		return parse(strDate, DEFAULT_FULL_PATTERN);
	}

	/**
	 * 比较日期大小
	 * 
	 * @param strStartDate
	 * @param strEndDate
	 * @return
	 */
	public static boolean compare(String strStartDate, String strEndDate) {
		Date startDate = parse(strStartDate, DEFAULT_PATTERN);
		Date endDate = parse(strEndDate, DEFAULT_PATTERN);
		long startTime = TimeStampUtil.getMillisOfDate(startDate);
		long endTime = TimeStampUtil.getMillisOfDate(endDate);
		return endTime - startTime > 0;
	}

	/**
	 * 返回两个日期相差毫秒
	 * 
	 * @param strStartDate
	 * @param strEndDate
	 * @return
	 */
	public static long compareDateStr(String strStartDate, String strEndDate) {
		Date startDate = parse(strStartDate, DEFAULT_PATTERN);
		Date endDate = parse(strEndDate, DEFAULT_PATTERN);
		long startTime = TimeStampUtil.getMillisOfDate(startDate);
		long endTime = TimeStampUtil.getMillisOfDate(endDate);
		return endTime - startTime;
	}

	/**
	 * 返回两个时间相差毫秒
	 * 
	 * @param strStartDate
	 * @param strEndDate
	 * @return
	 */
	public static long compareTimeStr(String strStartDate, String strEndDate) {
		Date startDate = parse(strStartDate, DEFAULT_FULL_PATTERN);
		Date endDate = parse(strEndDate, DEFAULT_FULL_PATTERN);
		long startTime = TimeStampUtil.getMillisOfDate(startDate) / 1000;
		long endTime = TimeStampUtil.getMillisOfDate(endDate) / 1000;
		return endTime - startTime;
	}

	public static String addDay(Date date, int count, int field, String format) {

		DEFAULT_CALENDAR.setTime(date);
		int year = getYear();
		int month = getMonth() - 1;
		int day = getDay();
		int hours = getHours();
		int minutes = getMinutes();
		int seconds = getSeconds();
		Calendar calendar = new GregorianCalendar(year, month, day, hours,
				minutes, seconds);
		calendar.add(field, count);
		String tmpDate = format(calendar.getTime(), format);
		DEFAULT_CALENDAR.setTime(new Date());
		return tmpDate;
	}

	/**
	 * 得到本月最后一天
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getLastDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getYear());
		cal.set(Calendar.MONTH, getMonth());
		cal.set(Calendar.DATE, 0);
		return format(cal.getTime(), DEFAULT_PATTERN);
	}

	/**
	 * 获得某年某月的最后一天时间戳
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DATE, 0);
		String lastDay = format(cal.getTime(), DEFAULT_PATTERN);
		// Long lastDayLong = TimeStampUtil.getMillisOfDay(format(cal.getTime(),
		// DEFAULT_PATTERN));
		return lastDay;
	}

	/**
	 * 得到本月第一天
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getfirstDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getYear());
		cal.set(Calendar.MONTH, (getMonth() - 1));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return format(cal.getTime(), DEFAULT_PATTERN);
	}

	/**
	 * 获得当天0点时间
	 * 
	 * @return
	 */
	public static Long getTimesmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (Long) (cal.getTimeInMillis() / 1000);
	}

	/**
	 * 获得当天24点时间
	 * 
	 * @return
	 */
	public static Long getTimesnight() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (Long) (cal.getTimeInMillis() / 1000);
	}

	/**
	 * 获得某年某月的第一天时间戳
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getFirstDayOfMonth(int year, int month) {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, (month - 1));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		// cal.set(Calendar.DATE, 1);
		String firstDay = format(cal.getTime(), DEFAULT_PATTERN);
		// Long firstDayLong =
		// TimeStampUtil.getMillisOfDay(format(cal.getTime(), DEFAULT_PATTERN));
		return firstDay;
	}

	/**
	 * 本周第一天
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getFirstDayOfWeek() {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		return format(c.getTime(), DEFAULT_PATTERN);
	}

	/**
	 * 昨天的日期字符串
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */

	public static String getYesterday() {
		return addDay(new Date(), -1, Calendar.DATE, DEFAULT_PATTERN);
	}

	/**
	 * 今天的开始时间
	 */
	public static String getBeginOfDay() {
		return getBeginOfDay(getToday());
	}

	/**
	 * 今天的结束时间
	 */
	public static String getEndOfDay() {
		return getEndOfDay(getToday());
	}

	/**
	 * 某一天的开始
	 * 
	 * @param day
	 *            :yyyy-MM-dd
	 * @return
	 */
	public static String getBeginOfDay(String day) {
		if (StringUtil.isEmpty(day) || !isDate(day)) {
			return null;
		}
		return day + " 00:00:00";
	}

	/**
	 * 某一天的结束
	 * 
	 * @param day
	 *            :yyyy-MM-dd
	 * @return
	 */
	public static String getEndOfDay(String day) {
		if (StringUtil.isEmpty(day) || !isDate(day)) {
			return null;
		}
		return day + " 23:59:59";
	}
	
	/**
	 * 转换日期+时+分的时间转化
	 * 
	 * */
	public static String toDate(String day) {
		if (StringUtil.isEmpty(day) || !isDate(day)) {
			return null;
		}
		return day + ":00";
	}

	/**
	 * 某一天的结束
	 * 
	 * @param day
	 *            :yyyy-MM-dd
	 * @return
	 */
	public static String getBeginOfYear() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return format(c.getTime(), "yyyy-MM-dd");
	}

	public static String getLastOfYear(int year) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year - 1);
		c.set(Calendar.MONTH, 11);
		c.set(Calendar.DAY_OF_MONTH, 31);
		return format(c.getTime(), "yyyy-MM-dd");
	}

	/**
	 * 当前年份
	 * 
	 * @return
	 */
	public static int getYear() {
		return DEFAULT_CALENDAR.get(Calendar.YEAR);
	}

	/**
	 * 当前年份
	 * 
	 * @return
	 */
	public static int getYear(Date day) {
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		return c.get(Calendar.YEAR);
	}

	/**
	 * 当前月份
	 * 
	 * @return
	 */
	public static int getMonth() {
		return DEFAULT_CALENDAR.get(Calendar.MONTH) + 1;
	}

	/**
	 * 当前月份
	 * 
	 * @return
	 */
	public static int getMonth(Date day) {
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * 当天
	 * 
	 * @return
	 */
	public static int getDay() {
		return DEFAULT_CALENDAR.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 当前小时
	 * 
	 * @return
	 */
	public static int getHours() {
		return DEFAULT_CALENDAR.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 当前分钟
	 * 
	 * @return
	 */
	public static int getMinutes() {
		return DEFAULT_CALENDAR.get(Calendar.MINUTE);
	}

	/**
	 * 获取日期的星期。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @return 星期
	 */
	public static Week getWeek(Date date) {
		Week week = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int weekNumber = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		switch (weekNumber) {
		case 0:
			week = Week.SUNDAY;
			break;
		case 1:
			week = Week.MONDAY;
			break;
		case 2:
			week = Week.TUESDAY;
			break;
		case 3:
			week = Week.WEDNESDAY;
			break;
		case 4:
			week = Week.THURSDAY;
			break;
		case 5:
			week = Week.FRIDAY;
			break;
		case 6:
			week = Week.SATURDAY;
			break;
		}
		return week;
	}

	/**
	 * 当前秒
	 * 
	 * @return
	 */
	public static int getSeconds() {
		return DEFAULT_CALENDAR.get(Calendar.SECOND);
	}

	public static String getDefaultPattern() {
		return DEFAULT_PATTERN;
	}

	public static String getDefaultFullPattern() {
		return DEFAULT_FULL_PATTERN;
	}

	public static Calendar getDefaultCalendar() {
		return DEFAULT_CALENDAR;
	}

	/**
	 * 根据月份获得过去几个月的数组
	 */
	public static List<String> getLastMonth(int curMonth, int lastCount) {

		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.MONTH, curMonth - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		List<String> list = new ArrayList<String>();
		String yyyyMM = format(cal.getTime(), "yyyy-MM");

		list.add(yyyyMM);
		for (int i = 0; i < lastCount - 1; i++) {

			cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH) - 1));
			yyyyMM = format(cal.getTime(), "yyyy-MM");
			list.add(yyyyMM);
		}
		return list;
	}

	public static List<String> getLastWeekArray(String startTimeStr) {

		if (StringUtil.isEmpty(startTimeStr)) {

			// 如果没有 时间。。则默认 从 今天起的 7天的 时间
			startTimeStr = sevenDayBeforeToday();
		}

		Date startDate = DateUtil.parse(startTimeStr);

		// 保存 一段 7天的 时间段
		List<String> weekTime = new ArrayList<String>();

		String curDate = "";

		for (int i = 0; i < 7; i++) {
			curDate = DateUtil
					.addDay(startDate, i, Calendar.DATE, "yyyy-MM-dd");
			weekTime.add(curDate);
		}

		return weekTime;
	}

	/*
	 * 得到 从今天起 7天前的 日期，格式为 yyyy-MM-dd
	 */
	public static String sevenDayBeforeToday() {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		cal.setTime(new Date());

		 cal.add(Calendar.DATE, -6);

		// System.out.println(df.format(cal.getTime()));

		return df.format(cal.getTime());
	}

	public static String sevenDayAfterToday() {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		cal.setTime(new Date());

		cal.add(Calendar.DATE, +6);

		// System.out.println(df.format(cal.getTime()));

		return df.format(cal.getTime());
	}

	/*
	 * long 型时间戳转换为 日期类型（yyyy-MM-dd）
	 */
	public static Long convTimeStampToDate(Long timeStamp) {
		SimpleDateFormat format1 = new SimpleDateFormat(DEFAULT_FULL_PATTERN);

		long unixLong = timeStamp * 1000;

		String date = format1.format(unixLong);

		// System.out.println(date);
		return Long.parseLong(date);
	}

	/*
	 * long 型时间戳转换为 String 类型 字符串日期（yyyy-MM-dd）
	 */
	public static String convTimeStampToStringDate(Long timeStamp,
			String pattern) {
		SimpleDateFormat format1 = new SimpleDateFormat(pattern);

		long unixLong = timeStamp * 1000;

		String date = format1.format(unixLong);

		return date;
	}

	/**
	 * 根据当前月份获得季度
	 * 
	 * @param month
	 * 
	 *            12月 会越界，因而是 month-1
	 */

	public static int getQuarter(int month) {
		return quarters[month - 1];
	}

	/*
	 * 根据 日期格式字符串，得到 对应的 时间戳 ，秒值 “2015-10-10 00:00:00” --> 1444406400
	 */
	public static long getUnixTimeStamp(String date) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		Date parse = dateFormat.parse(date);

		long time = parse.getTime() / 1000;

		return time;
	}
	
	/*
	 * 字符串时间戳,转换为 日期
	 */
	public static String timeStamp2Date(String seconds, String format) {
		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty())
			format = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}

	/*
	 * 2016年2月19日18:40:35 针对运营平台 统计 图表 ， 时间 出现 “跨年” 时，
	 * 
	 * 处理 按月份搜索时的 月份跨度, 目前来说 只有 按月时，
	 * 
	 * exp: 当前 2016-1,查找 最近 3个月 ，返回 {2015-11,2015-12,2016-1}
	 */

	public static List<String> getCureMonth(Date startDate, Date endDate) {

		List<String> timeSeries = new ArrayList<String>();

		int i = 0;

		String curDate = DateUtil.formatDate(startDate);

		int startMonth = DateUtil.getMonth(startDate);
		int endMonth = DateUtil.getMonth(endDate);

		int startYear = DateUtil.getYear(startDate);

		int endYear = DateUtil.getYear(endDate);

		if (startYear == endYear) {
			for (i = 0; i <= (endMonth - startMonth); i++) {
				curDate = DateUtil.addDay(startDate, i, Calendar.MONTH, "M");

				if (Integer.parseInt(curDate) < 10) {
					timeSeries.add(startYear + "-0" + curDate);
				} else {
					timeSeries.add(startYear + "-" + curDate);
				}
			}
		} else {
			// 2016年4月11日15:35:28 如果是横跨 超过 1年以上
			int duraYear = endYear - startYear;

			for (int j = startMonth; j <= 12; j++) {

				/*
				 * 对于月份 统一 时间的格式。为 YYYY-MM 如 2015-07、 2015-11
				 * 
				 * 为 最后处理 页面 table时，处理 时间时 使用
				 */

				if (j < 10) {
					timeSeries.add(startYear + "-0" + j);
				} else {
					timeSeries.add(startYear + "-" + j);
				}
			}

			for (int k = 1; k <= endMonth; k++) {

				if (k < 10) {
					timeSeries.add(endYear + "-0" + k);
				} else {
					timeSeries.add(startYear + "-" + k);
				}
			}

			if (duraYear > 1) {

				// 横框超过1年。则 加上中间年份 全年的月份。

				for (int m = startYear + 1; m < endYear; m++) {
					for (int p = 1; p <= 12; p++) {

						if (p < 10) {
							timeSeries.add(m + "-0" + p);
						} else {
							timeSeries.add(m + "-" + p);
						}

					}
				}
			}

		}

		return timeSeries;
	}

	/** 计算年龄 */
	public static String getAge(Date birthDay) throws Exception {

		if (birthDay == null)
			return "";

		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			return "";
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthDay);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				// monthNow>monthBirth
				age--;
			}
		}

		return age + "";
	}
	
	//日期添加月份
	public static Date toDate(int month){
		int mth = getMonth()+month;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, mth);
		return cal.getTime();
	}
	
	//获取每个月的开始日期
	public static Long curStartDate(int mouth){
		Calendar cal = Calendar.getInstance();
		int mou=cal.get(Calendar.MONTH)-mouth;
		cal.set(Calendar.MONTH, mou);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		String strDate = format(cal.getTime(), "yyyy-MM-dd");
		return TimeStampUtil.getMillisOfDay(strDate)/1000;
	}
	
	public static Long curLastDate(int mouth){
		Calendar cal = Calendar.getInstance();
		int mou=cal.get(Calendar.MONTH)+1-mouth;
		cal.set(Calendar.MONTH, mou);
		cal.set(Calendar.DAY_OF_MONTH, 0);
		String strDate = format(cal.getTime(), "yyyy-MM-dd");
		return TimeStampUtil.getMillisOfDayFull(getEndOfDay(strDate))/1000;
	}
}