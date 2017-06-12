package com.meijia.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * String常用工具类..
 * 持续更新ing..
 * @author dylan
 *
 */
public class StringUtil {
	
	static String []bigNum={"零","一","二","三","四","五","六","七","八","九"};
	/**
	 * 判断字符串是否为空 or is NULL
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim()) || str.length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为空
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		return str == null;
	}

	/**
	* 首字母大写
	* @param srcStr
	* @return
	*/
	public static String firstCharacterToUpper(String srcStr) {
		return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1);
	}

    /**
     * 格式化字符串
     * 如果为空，返回“”
     * @param str
     * @return
     */
    public static String formatString(String str) {
        if(isEmpty(str)) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 截取字符串，字母、汉字都可以，汉字不会截取半
     * @param str 字符串
     * @param n 截取的长度，字母数，如果为汉字，一个汉字等于两个字母数
     * @return
     */
    public static String subStringByByte(String str, int n){
        int num = 0;
        try {
            byte[] buf = str.getBytes("GBK");
            if(n>=buf.length){
                return str;
            }
            boolean bChineseFirstHalf = false;
            for(int i=0;i<n;i++)
            {
                if(buf[i]<0 && !bChineseFirstHalf){
                    bChineseFirstHalf = true;
                }else{
                    num++;
                    bChineseFirstHalf = false;
                }
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str.substring(0,num);
    }

	/**
	 * MD5字符串加密
	 *
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public final static String md5(String str) throws NoSuchAlgorithmException {
		final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'a', 'b', 'c', 'd', 'e', 'f' };
		byte[] btInput = str.getBytes();
		// 获得MD5摘要算法的 MessageDigest 对象
		MessageDigest md5Inst = MessageDigest.getInstance("MD5");
		// 使用指定的字节更新摘要
		md5Inst.update(btInput);
		// 获得密文
		byte[] bytes = md5Inst.digest();

		StringBuffer strResult = new StringBuffer();
		// 把密文转换成十六进制的字符串形式
		for (int i = 0; i < bytes.length; i++) {
			strResult.append(hexDigits[(bytes[i] >> 4) & 0x0f]);
			strResult.append(hexDigits[bytes[i] & 0x0f]);
		}
		return strResult.toString();
	}

	/**
	 * SHA-1字符串加密
	 *
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public final static String sha1(String str) throws NoSuchAlgorithmException {
		final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'a', 'b', 'c', 'd', 'e', 'f' };
		byte[] btInput = str.getBytes();
		// 获得SHA-1摘要算法的 MessageDigest 对象
		MessageDigest sha1Inst = MessageDigest.getInstance("SHA-1");
		// 使用指定的字节更新摘要
		sha1Inst.update(btInput);
		// 获得密文
		byte[] bytes = sha1Inst.digest();

		StringBuffer strResult = new StringBuffer();
		// 把密文转换成十六进制的字符串形式
		for (int i = 0; i < bytes.length; i++) {
			strResult.append(hexDigits[(bytes[i] >> 4) & 0x0f]);
			strResult.append(hexDigits[bytes[i] & 0x0f]);
		}
		return strResult.toString();
	}
	
	
	public static String[] clean(final String[] v) {
	    List<String> list = new ArrayList<String>(Arrays.asList(v));
	    list.removeAll(Collections.singleton(""));
	    return list.toArray(new String[list.size()]);
	}
	
	//逗号分割
    public static String[] convertStrToArray(String str){
        String[] strArray = null;
        strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }

    //下划线分割
    public static String[] convertStrToArrayByUL(String str){
        String[] strArray = null;
        strArray = str.split("_"); //拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }	
	
    /*
     * 身份证号中间 用 * 显示
     */
    public static String replaceCardIdByStar(String cardId){
    	//去除空格，数据库中记录有空格
    	String cardNo = cardId.replaceAll(" ", "");
    	
    	String star = cardNo.substring(5,15);
    	String replaceAll = cardNo.replaceAll(star, "**********");
    	
    	return replaceAll;
    }
    
	public static String getZhNum(String str) {
		int t = Integer.parseInt(str);
		return bigNum[t];
	}
    /**
     * 米转公里
     */
	/*private int distance = 789;
	private double dis = 0;*/
	 
	//你的距离数据应该不是写死的吧，如果你是从服务器获取的距离数据，可能是String，赋值给//distance时候就要强制类型转换(Integer）,然后再执行以下四舍五入
	 
	/*dis = Math.round(dist/100d)/10d;
	disText.setText(dis+"公里");*/
	public static double getKilometre(int distance){
	  double dis = 0;
	  dis = Math.round(distance/100d)/10d;
	  
	  return dis;
	}
	
	public static void main(String[] args) throws ParseException {
//		Double m = StringUtil.getKilometre(3333);
//		System.out.println(m.toString());
//		
		//测试样例1    11点-13点为约满
//		String json = "[{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"00:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"00:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"01:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"01:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"02:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"02:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"03:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"03:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"04:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"04:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"05:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"05:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"215,\",\"is_full\":\"0\",\"total_dispatched\":\"1\",\"service_hour\":\"06:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"215,\",\"is_full\":\"0\",\"total_dispatched\":\"1\",\"service_hour\":\"06:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"100,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"07:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"100,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"07:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"100,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"08:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"100,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"08:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"100,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"09:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"100,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"09:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"215,100,215,\",\"is_full\":\"0\",\"total_dispatched\":\"3\",\"service_hour\":\"10:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"215,100,215,\",\"is_full\":\"0\",\"total_dispatched\":\"3\",\"service_hour\":\"10:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,100,215,\",\"is_full\":\"1\",\"total_dispatched\":\"4\",\"service_hour\":\"11:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,100,215,\",\"is_full\":\"1\",\"total_dispatched\":\"4\",\"service_hour\":\"11:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,100,215,\",\"is_full\":\"1\",\"total_dispatched\":\"4\",\"service_hour\":\"12:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,100,215,\",\"is_full\":\"1\",\"total_dispatched\":\"4\",\"service_hour\":\"12:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,100,215,\",\"is_full\":\"1\",\"total_dispatched\":\"4\",\"service_hour\":\"13:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"13:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"14:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"14:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"15:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"15:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"16:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"16:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"17:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,\",\"is_full\":\"0\",\"total_dispatched\":\"1\",\"service_hour\":\"17:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,\",\"is_full\":\"0\",\"total_dispatched\":\"1\",\"service_hour\":\"18:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"18:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"19:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"19:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"20:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"20:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"21:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"21:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"22:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"22:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"23:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"23:30\"}]";
		
		//测试样例2    11点-13点为约满  15 - 16约满
		String json = "[{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"00:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"00:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"01:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"01:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"02:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"02:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"03:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"03:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"04:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"04:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"05:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"05:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"215,\",\"is_full\":\"0\",\"total_dispatched\":\"1\",\"service_hour\":\"06:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"215,\",\"is_full\":\"0\",\"total_dispatched\":\"1\",\"service_hour\":\"06:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"100,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"07:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"100,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"07:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"100,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"08:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"100,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"08:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"100,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"09:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"100,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"09:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"215,100,215,\",\"is_full\":\"0\",\"total_dispatched\":\"3\",\"service_hour\":\"10:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"215,100,215,\",\"is_full\":\"0\",\"total_dispatched\":\"3\",\"service_hour\":\"10:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,100,215,\",\"is_full\":\"1\",\"total_dispatched\":\"4\",\"service_hour\":\"11:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,100,215,\",\"is_full\":\"1\",\"total_dispatched\":\"4\",\"service_hour\":\"11:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,100,215,\",\"is_full\":\"1\",\"total_dispatched\":\"4\",\"service_hour\":\"12:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,100,215,\",\"is_full\":\"1\",\"total_dispatched\":\"4\",\"service_hour\":\"12:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,100,215,\",\"is_full\":\"1\",\"total_dispatched\":\"4\",\"service_hour\":\"13:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"13:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"14:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"14:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,\",\"is_full\":\"1\",\"total_dispatched\":\"2\",\"service_hour\":\"15:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,\",\"is_full\":\"1\",\"total_dispatched\":\"2\",\"service_hour\":\"15:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,\",\"is_full\":\"1\",\"total_dispatched\":\"2\",\"service_hour\":\"16:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"16:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,215,\",\"is_full\":\"0\",\"total_dispatched\":\"2\",\"service_hour\":\"17:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,\",\"is_full\":\"0\",\"total_dispatched\":\"1\",\"service_hour\":\"17:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"228,\",\"is_full\":\"0\",\"total_dispatched\":\"1\",\"service_hour\":\"18:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"18:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"19:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"19:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"20:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"20:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"21:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"21:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"22:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"22:30\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"staffs\":\"\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"23:00\"},{\"total\":\"4\",\"order_service_hour\":\"3.0\",\"is_full\":\"0\",\"total_dispatched\":\"0\",\"service_hour\":\"23:30\"}]";

		List<Integer> autoSetMapIndex = new ArrayList<Integer>();
		String serviceDateStr = "2017-06-13";
		double serviceHours = 5;
		String beginServiceHour = "";
		String endServiceHour = "";
		double checkServiceHour = 0;
		List<Map> datas = GsonUtil.GsonToList(json, Map.class);
		for (int i = 0 ; i < datas.size(); i++) {
			Map<String, String> item = datas.get(i);
			String serviceHour = item.get("service_hour").toString();
			String hourStr = serviceHour.substring(0, 2);
			int hour = Integer.valueOf(hourStr).intValue();
			if (hour < 8) continue;
			String isFull = item.get("is_full").toString();
			System.out.println("service_hour = " + serviceHour + " --- isFull = " + isFull);
			if (isFull.equals("0")) {
				checkServiceHour+=0.5;
				autoSetMapIndex.add(i);
			}
			
			if (isFull.equals("0") && StringUtil.isEmpty(beginServiceHour))  {
				beginServiceHour = serviceHour;
			} 
			
			if (isFull.equals("1") && StringUtil.isEmpty(endServiceHour)) {
				endServiceHour = serviceHour;
			}
			
			//做相应的判断.
			if (!StringUtil.isEmpty(beginServiceHour) && !StringUtil.isEmpty(endServiceHour)) {
				if (checkServiceHour < serviceHours) {
					for (int j = 0; j < datas.size(); j++) {
						for (int k = 0; k < autoSetMapIndex.size(); k++) {
							if (j == autoSetMapIndex.get(k)) {
								Map<String, String> setitem = datas.get(j);
								setitem.put("is_full", "1");
								datas.set(j, setitem);
							}
						}
					}
					
					beginServiceHour = "";
					endServiceHour = "";
					autoSetMapIndex = new ArrayList<Integer>();
				}
			}
			
			
		}

		System.out.println("-------------------------------------------------");
		for (int i = 0 ; i < datas.size(); i++) {
			Map<String, String> item = datas.get(i);
			String serviceHour = item.get("service_hour").toString();
			String isFull = item.get("is_full").toString();
			System.out.println("service_hour = " + serviceHour + " --- isFull = " + isFull);
		}
		
	}
	
}
