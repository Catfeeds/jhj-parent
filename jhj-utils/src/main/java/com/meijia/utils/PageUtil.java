package com.meijia.utils;

public class PageUtil {


    public static String resolveUrl(String url, String queryString, Integer pageNo, Integer pageSize){
    	if(queryString==null)
    		queryString=new String();
    	else
    		queryString=queryString.replaceAll("&pageNo=\\d*", "").replaceAll("pageNo=\\d*", "").replaceAll("&pageSize=\\d*", "").replaceAll("pageSize=\\d*", "");

    	if(pageNo!=null)
			queryString=queryString.isEmpty()?"pageNo="+pageNo.toString():queryString+"&pageNo="+pageNo.toString();
		if(pageSize!=null)
			queryString=queryString.isEmpty()?"pageSize="+pageSize.toString():queryString+"&pageSize="+pageSize.toString();

		if (url.indexOf("?") > 0) {
			return queryString.isEmpty()?url:url+"&"+queryString;
		} else {
			return queryString.isEmpty()?url:url+"?"+queryString;
		}
    }

}
