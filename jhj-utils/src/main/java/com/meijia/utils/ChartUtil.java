package com.meijia.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 有个管家的常用方法
 *
 */
public class ChartUtil {
	
	/**
	 * 
	 * @param 根据选择月份获得统计周期， 1 = day  >1 = month  12 = quarter
	 * @return
	 */
	public static String getStatTypeByCycle(int searchMonth) {

		String statType = "month";
		if (searchMonth == 1) statType = "day";
		if (searchMonth > 1 && searchMonth < 12) statType = "month";
		if (searchMonth == 12) statType = "month";
			
		return statType;
	}
	
	/**
	 * 根据开始和结束时间获得统计周期  <31 = day  > 31 = month
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String getStatTypeByTime(String startTime, String endTime) {

		String statType = "month";

		Long searchStartTime  = TimeStampUtil.getMillisOfDayFull(DateUtil.getBeginOfDay(startTime)) / 1000;
		Long searchEndTime = TimeStampUtil.getMillisOfDayFull(DateUtil.getEndOfDay(endTime)) / 1000;
		
		Long ago = (searchEndTime - searchStartTime) / 86400;
		
		
		/*
		 * 2015-11-21 14:56:51  这里的 天数判断 ，决定了 统计图 横坐标的 天数跨度，从原来的 30天，改为了 90天~
		 */
		if (ago.intValue() < 90) statType = "day";
		if (ago.intValue() > 90 && ago.intValue() < 181 ) statType = "month";
		
		if(ago.intValue() >= 181) statType = "quarter";
		
		return statType;
	}	
	
	/**
	 *  根据时间周期得到开始时间和结束时间
	 *  @param string startTime
	 *  @param string endTime
	 *  @return map   startTime => Long
	 *                endTime => Long
	 */
	
	public static HashMap<String, Long> getTimeRangeByCycle(String statType, int month) {

		HashMap<String, Long> result = new HashMap<String, Long>();
		Date nowDate = DateUtil.parse(DateUtil.getBeginOfDay());
		String startTimeStr = "";

		if (statType.equals("day")) {
			startTimeStr = DateUtil.addDay(nowDate, -1, Calendar.MONTH, DateUtil.DEFAULT_PATTERN);
			Date startTimeDate = DateUtil.parse(startTimeStr);
			startTimeStr = DateUtil.addDay(startTimeDate, -1, Calendar.DATE, DateUtil.DEFAULT_PATTERN);
		}
		
		if (statType.equals("month")) {
//			month = month + 1;
			startTimeStr = DateUtil.addDay(nowDate, -month, Calendar.MONTH, DateUtil.DEFAULT_PATTERN);			
			Date startTimeDate = DateUtil.parse(startTimeStr);
			startTimeStr = DateUtil.getFirstDayOfMonth(DateUtil.getYear(startTimeDate), DateUtil.getMonth(startTimeDate));
		
		}

		if (statType.equals("quarter")) {
			startTimeStr = DateUtil.getLastOfYear(DateUtil.getYear());  //2015 得到  2014-12-31
			Date startTimeDate = DateUtil.parse(startTimeStr);
			startTimeStr = DateUtil.addDay(startTimeDate, -2, Calendar.MONTH, DateUtil.DEFAULT_PATTERN);  //得到2014-10-31
			startTimeDate = DateUtil.parse(startTimeStr);
			startTimeStr = DateUtil.getFirstDayOfMonth(DateUtil.getYear(startTimeDate), DateUtil.getMonth(startTimeDate)); //得到 2014-10-01
		}

		startTimeStr = DateUtil.getBeginOfDay(startTimeStr);
		Long startTime = TimeStampUtil.getMillisOfDayFull(startTimeStr) / 1000;
		Long endTime = TimeStampUtil.getMillisOfDayFull(DateUtil.getEndOfDay()) / 1000; 
		
		result.put("startTime", startTime);
		result.put("endTime", endTime);
		return result;
	}	
	
	/**
	 *  根据开始时间和结束时间 得到开始时间(长整形)和结束时间(长整形)
	 *  @param string startTimeStr
	 *  @param string endTimeStr
	 *  @return map   startTime => Long
	 *                endTime => Long
	 */
	
	public static HashMap<String, Long> getTimeRangeByTime(String statType, String startTimeStr, String endTimeStr) {

		HashMap<String, Long> result = new HashMap<String, Long>();
		
		
		if (statType.equals("day")) {
		
			Date startTimeDate = DateUtil.parse(startTimeStr);
			startTimeStr = DateUtil.addDay(startTimeDate, -1, Calendar.DATE, DateUtil.DEFAULT_PATTERN);
			startTimeStr = DateUtil.getBeginOfDay(startTimeStr);
		}
		
		if (statType.equals("month")) {
			Date startTimeDate = DateUtil.parse(startTimeStr);
			startTimeStr = DateUtil.addDay(startTimeDate, -1, Calendar.MONTH, DateUtil.DEFAULT_PATTERN);
			startTimeStr = DateUtil.getBeginOfDay(startTimeStr);
		}
		
		//????
		if (statType.equals("quarter")) {
			Date startTimeDate = DateUtil.parse(startTimeStr);
			startTimeStr = DateUtil.addDay(startTimeDate, -1, Calendar.MONTH, DateUtil.DEFAULT_PATTERN);
			startTimeStr = DateUtil.getBeginOfDay(startTimeStr);
		}
		
		endTimeStr = DateUtil.getEndOfDay(endTimeStr);
		
			
		Long startTime = TimeStampUtil.getMillisOfDayFull(startTimeStr) / 1000;
		Long endTime = TimeStampUtil.getMillisOfDayFull(endTimeStr) / 1000;
		
		result.put("startTime", startTime);
		result.put("endTime", endTime);
		return result;
	}		
	
	/**
	 * 根据开始时间戳和结束时间戳，以及统计类型statType得到统计序列
	 * @param String statType  day/month/quarter
	 * @param Long startTime
	 * @param Long endTime
	 * @return List<String> timeSeries
	 */
	
	public static List<String> getTimeSeries(String statType, Long startTime, Long endTime) {
		List<String> timeSeries = new ArrayList<String>();
		
		int i = 0; 
		Long ago = (endTime - startTime) / 86400;
		Date startDate = TimeStampUtil.timeStampToDate(startTime * 1000);
		Date endDate = TimeStampUtil.timeStampToDate(endTime * 1000);		
		String curDate = DateUtil.formatDate(startDate);
		
		
		if (statType.equals("day")) {
			for (i = 0; i < ago.intValue(); i++) {
				curDate = DateUtil.addDay(startDate, i, Calendar.DATE, "Y-M-d");
				timeSeries.add(curDate);
			}
		}
		
		if (statType.equals("month")) {
			
			timeSeries = DateUtil.getCureMonth(startDate, endDate);
			
		}
		
		if (statType.equals("quarter")) {
			int startMonth = 1;
			int endMonth = DateUtil.getMonth(endDate);
			timeSeries.add(DateUtil.getYear(startDate) + "-4");
			for (i = startMonth; i <= endMonth; i++) {
				int quarter = DateUtil.getQuarter(i);
				String item = DateUtil.getYear(endDate) + "-" + String.valueOf(quarter);
				if (!timeSeries.contains(item)) {
					timeSeries.add(item);
				}
			}
		}
		
		return timeSeries;
	}
	
	public static String getTimeSeriesName(String statType, String series) {
		
		String name = "";

		if (statType.equals("day")) name = series;
		if (statType.equals("month")) name = series + "月";
		if (statType.equals("quarter")) {
//			String quarter = series.substring(5);
			
			String[] split = series.split("-");
			
			
			/*
			 * 2016年2月20日10:54:17  对于跨年的情况，应该展示为
			 * 
			 * 	如：  2015-第四季度， 2016-第一季度
			 * 
			 *   此处 返回  :  2015,第四季度   2016,第一季度
			 *   
			 *   在下方计算 时间段的 开始和 结束时间戳时 会用到
			 */
			
			name = StringUtil.getZhNum(split[1]);
			name = "第" + name + "季度";
			
			name = split[0]+","+name;
		}
		
		return name;
	}
	
	/*
	 * 根据 选择的 时间周期、 当前的 series 值，	确定 该行 的开始时间 和 结束时间
	 */
	public static HashMap<String, Long> getTimeDuration(String statType,String seriesName){
		
		HashMap<String, Long> map = new HashMap<String, Long>();
		
		//今年
		int nowYear = DateUtil.getYear();
		
		if (statType.equals("day")) {
			//当天的 时间戳
			
			long dayStart = TimeStampUtil.getMillisOfDayFull(seriesName+" "+"00:00:00")/1000;
			
			long dayEnd =  TimeStampUtil.getMillisOfDayFull(seriesName+" "+"23:59:59")/1000;
			
			map.put("startTime", dayStart);
			map.put("endTime", dayEnd);
			
		}
		
		if (statType.equals("month")) {

			if(seriesName.length() >=7){
				
				String yearName = seriesName.substring(0, 4);
				
				// 统一处理  月份的格式 为   2016-05、2015-12
				if(!yearName.equals(nowYear+"")){
					
					nowYear = Integer.parseInt(yearName);
				}
				
				seriesName = seriesName.substring(5,7);
				
			}else{
				seriesName = seriesName.replace("月","");
			}
			
			
			//月份开始时间
			long monthStart = TimeStampUtil.getMillisOfDayFull(nowYear+"-"+seriesName+"-1 00:00:00")/1000;
			
			//月份结束时间
			//当前月份有几天
			int maxDay = calculate(nowYear, Integer.parseInt(seriesName));
			
			long monthEnd =  TimeStampUtil.getMillisOfDayFull(nowYear +"-"+seriesName+"-"+maxDay+" 23:59:59")/1000;
			
			map.put("startTime", monthStart);
			map.put("endTime", monthEnd);
			
		}
		
		if (statType.equals("quarter")) {
			
			//此时的 seriesName 格式  ： 2015,第四季度
			String[] split = seriesName.split(",");
			
			long quarterStart = 0L;
			
			long quarterEnd = 0L;			
			
			if(seriesName.indexOf("第一季度") > 0){
				
				quarterStart = TimeStampUtil.getMillisOfDayFull(split[0]+"-"+"1-1 00:00:00")/1000;
				quarterEnd = TimeStampUtil.getMillisOfDayFull(split[0]+"-"+"3-31 00:00:00")/1000;
				
				map.put("startTime", quarterStart);
				map.put("endTime", quarterEnd);
				
			}else if(seriesName.indexOf("第二季度") > 0){
				
				quarterStart = TimeStampUtil.getMillisOfDayFull(split[0]+"-"+"4-1 00:00:00")/1000;
				quarterEnd = TimeStampUtil.getMillisOfDayFull(split[0]+"-"+"6-30 00:00:00")/1000;
				
				map.put("startTime", quarterStart);
				map.put("endTime", quarterEnd);
				
			}else if(seriesName.indexOf("第三季度") > 0){
				
				quarterStart = TimeStampUtil.getMillisOfDayFull(split[0]+"-"+"7-1 00:00:00")/1000;
				quarterEnd = TimeStampUtil.getMillisOfDayFull(split[0]+"-"+"9-30 00:00:00")/1000;
				
				map.put("startTime", quarterStart);
				map.put("endTime", quarterEnd);
				
			}else{
				
				quarterStart = TimeStampUtil.getMillisOfDayFull(split[0]+"-"+"10-1 00:00:00")/1000;
				quarterEnd = TimeStampUtil.getMillisOfDayFull(split[0]+"-"+"12-31 00:00:00")/1000;
				
				map.put("startTime", quarterStart);
				map.put("endTime", quarterEnd);
			}
			
		}
		
		return map;
		
	}
	
	//判断是否是闰年
	public static boolean judge(int year)  
    {  
        boolean yearleap = (year % 400 == 0) || (year % 4 == 0) && (year % 100 != 0);//采用布尔数据计算判断是否能整除   
        return yearleap;  
    } 
	
	//判断某年某月有几天
	public static int calculate(int year, int month)  
    {  
        boolean yearleap = judge(year);  
        int day;  
        if(yearleap && month == 2)  
        {  
            day = 29;  
        }  
        else if(!yearleap && month == 2)  
        {  
            day = 28;  
        }  
        else if(month == 4 || month == 6 || month == 9 || month == 11)  
        {  
            day = 30;  
        }  
        else  
        {  
            day = 31;  
        }  
        return day;  
    }  
	
	
	
	public static void main(String[] args) throws ParseException {
		
//		Date parse = new SimpleDateFormat("YYYY-MM-DD").parse();
		
//		long day = TimeStampUtil.getMillisOfDayFull(2015+"-"+"8-44"+"            "+"00:00:00")/1000;
//		
//		long day1 = TimeStampUtil.getMillisOfDayFull(2015+"-"+"8-16"+"            "+"23:59:59")/1000;
//		
//		System.out.println(day);
//		System.out.println(day1);
		
		int calculate = calculate(2015, Integer.parseInt("5"));
		System.out.println(calculate);
		
		
//		System.out.println(parse.getTime());
		
//		String statType = "day";
//		HashMap<String, Long> map = new HashMap<String, Long>();
//		List<String> timeSeries = new ArrayList<String>();
//		Long startTime = 0L;
//		Long endTime = 0L;
//		System.out.println("----------------按时间周期--------------------------");
//		int cycle = 12;
//		
//		statType = ChartUtil.getStatTypeByCycle(cycle);  
//		
//		System.out.println("statType = " + statType);
//		
//		map = ChartUtil.getTimeRangeByCycle(statType, cycle);
//		
//		startTime = Long.valueOf(map.get("startTime"));
//		endTime = Long.valueOf(map.get("endTime"));
//		
//		System.out.println("statrTime = " + TimeStampUtil.timeStampToDateStr(startTime * 1000, DateUtil.DEFAULT_FULL_PATTERN));
//		System.out.println("endTime = " + TimeStampUtil.timeStampToDateStr(endTime * 1000, DateUtil.DEFAULT_FULL_PATTERN));
//		
//		timeSeries = ChartUtil.getTimeSeries(statType, startTime, endTime);
//		
//		for (String item : timeSeries) {
//			System.out.println(item);
//			System.out.println(ChartUtil.getTimeSeriesName(statType, item));
//		}
//		
//		System.out.println("----------------按照开始和结束时间--------------------------");
//		String startTimeStr = "2015-06-2";
//		String endTimeStr = "2015-06-30";
//		statType = ChartUtil.getStatTypeByTime(startTimeStr, endTimeStr);
//		System.out.println("statType = " + statType);
//		map = ChartUtil.getTimeRangeByTime(statType, startTimeStr, endTimeStr);
//		startTime = Long.valueOf(map.get("startTime"));
//		endTime = Long.valueOf(map.get("endTime"));
//		System.out.println("statrTime = " + TimeStampUtil.timeStampToDateStr(startTime * 1000, DateUtil.DEFAULT_FULL_PATTERN));
//		System.out.println("endTime = " + TimeStampUtil.timeStampToDateStr(endTime * 1000, DateUtil.DEFAULT_FULL_PATTERN));
//
//		timeSeries = ChartUtil.getTimeSeries(statType, Long.valueOf(map.get("startTime")), Long.valueOf(map.get("endTime")));
//		
//		for (String item : timeSeries) {
//			System.out.println(item);
//		}
	}
}
