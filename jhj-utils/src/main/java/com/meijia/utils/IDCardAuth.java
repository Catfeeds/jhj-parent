package com.meijia.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class IDCardAuth {

	public static String request(String httpUrl, String httpArg) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		httpUrl = httpUrl + "?" + httpArg;

		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			// 填入apix-key到HTTP header
			connection.setRequestProperty("apix-key", "87578b75e50a4e7062b2afbbb8964989");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Map<String, String> getResultMap(String jsonResult) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("msg", "");
		result.put("code", "");
		result.put("mobileProv", "");
		result.put("sex", "");
		result.put("birthday", "");
		result.put("address", "");
		result.put("mobileOperator", "");
		result.put("mobileCity", "");
		result.put("img", "");

		if (StringUtil.isEmpty(jsonResult))
			return result;

		try {
			JsonParser parser = new JsonParser(); // 创建JSON解析器
			JsonObject object = (JsonObject) parser.parse(jsonResult); // 创建JsonObject对象

			String msg = object.get("msg").getAsString();
			result.put("msg", msg);

			String code = object.get("code").getAsString();
			result.put("code", code);
			
			if (code.equals("200")) {
				result.put("msg", "查询失败，余额不足，请充值");
			}

			if (object.get("data").isJsonObject()) {
				JsonObject dataObj = object.get("data").getAsJsonObject();

//				System.out.println("data = " + dataObj);
				
				if (dataObj.get("moible_prov") != null) {
					String mobileProv = dataObj.get("moible_prov").getAsString();
					result.put("mobileProv", mobileProv);
				}
				
				if (dataObj.get("sex") != null) {
					String sex = dataObj.get("sex").getAsString();
					if (sex.equals("M")) result.put("sex", "男");
					if (sex.equals("F")) result.put("sex", "女");
					
				}
				
				if (dataObj.get("birthday") != null) {
					String birthday = dataObj.get("birthday").getAsString();
					result.put("birthday", birthday);
				}
				
				if (dataObj.get("address") != null && !dataObj.get("address").isJsonNull()) {
					String address = dataObj.get("address").getAsString();
					if (!StringUtil.isEmpty(address)) {
						result.put("address", address);
					}
				}
				
				if (dataObj.get("mobile_operator") != null) {
					String mobileOperator = dataObj.get("mobile_operator").getAsString();
					result.put("mobileOperator", mobileOperator);
				}
				
				if (dataObj.get("mobile_city") != null) {
					String mobileCity = dataObj.get("mobile_city").getAsString();
					result.put("mobileCity", mobileCity);
				}
				
				if (dataObj.get("idcardphoto") != null) {
					String idcardphoto = dataObj.get("idcardphoto").getAsString();
					result.put("img", idcardphoto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}

		return result;
	}

	public static void main(String[] args) {
		// 发送 GET 请求
		 String httpUrl = " http://v.apix.cn/apixcredit/idcheck/idcard";
		 String httpArg = "type=idcard_photo&name=张现葵&cardno=37152219890621781x";
		 String jsonResult = request(httpUrl, httpArg);
		 System.out.println(jsonResult);

//		String jsonResult = "{\"msg\":\"成功：验证信息一致\",\"code\":0,\"data\":{\"moible_prov\":\"北京\",\"sex\":\"M\",\"birthday\":\"1983-11-12\",\"address\":\"江苏省苏州市吴中区\",\"mobile_operator\":\"联通185卡\",\"mobile_city\":\"北京\"}}";
//		String jsonResult = "{\"msg\":\"库中不存在该身份证号\",\"code\":101,\"data\":\"\"}";
//		String jsonResult = "{\"msg\":\"姓名和身份证号一致\",\"code\":0,\"data\":{\"cardno\":\"372523197609273316\",\"birthday\":\"1976-09-27\",\"sex\":\"M\",\"name\":\"张彦敏\",\"address\":null}}";
		JsonParser parser = new JsonParser(); // 创建JSON解析器
		JsonObject object = (JsonObject) parser.parse(jsonResult); // 创建JsonObject对象

		String msg = object.get("msg").getAsString();
		String code = object.get("code").getAsString();
		System.out.println("msg = " + msg);
		System.out.println("code = " + code);
		if (object.get("data").isJsonObject()) {
			JsonObject dataObj = object.get("data").getAsJsonObject();

			System.out.println("data = " + dataObj);

			String mobileProv = dataObj.get("moible_prov").getAsString();
			System.out.println("mobileProv = " + mobileProv);

			String sex = dataObj.get("sex").getAsString();
			String birthday = dataObj.get("birthday").getAsString();
			String address = dataObj.get("address").getAsString();
			String mobileOperator = dataObj.get("mobile_operator").getAsString();
			String mobileCity = dataObj.get("mobile_city").getAsString();

			System.out.println("sex = " + sex);
			System.out.println("birthday = " + birthday);
			System.out.println("address = " + address);
			System.out.println("mobileOperator = " + mobileOperator);
			System.out.println("mobileCity = " + mobileCity);
		}
	}
}
