package com.meijia.utils.baiduMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author :hulj
 * @Date : 2015年6月25日下午3:26:36
 *
 */
public class MyStringUtils {
	/*
	 * 去掉字符串中的中文
	 */ 
	public static String delChinese(String s){
		
		 String reg = "[\u4e00-\u9fa5]";
	   	 Pattern pat = Pattern.compile(reg);  
	   	 Matcher mat=pat.matcher(s); 
	   	 String repickStr = mat.replaceAll("");
		 return repickStr;
	}
	
}  
