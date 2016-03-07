package com.meijia.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MobileUtil {
	
    /**
     * 测试手机号码是来自哪个城市的，利用淘宝的API
     * @param mobile 手机号码
     * @return
     * @throws MalformedURLException
     */
    public static String calcMobileCity(String mobile) throws MalformedURLException{
        String jsonString = null;
        JSONArray array = null;
        JSONObject jsonObject = null;
        String urlString = "http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=" + mobile;
        StringBuffer sb = new StringBuffer();
        BufferedReader buffer;
        URL url = new URL(urlString);
        String province = "";
        try{
            InputStream in = url.openStream();

            // 解决乱码问题
            buffer = new BufferedReader(new InputStreamReader(in,"gb2312"));
            String line = null;
            while((line = buffer.readLine()) != null){
                sb.append(line);
            }
            in.close();
            buffer.close();
            // System.out.println(sb.toString());
            jsonString = sb.toString();
            // 替换掉“__GetZoneResult_ = ”，让它能转换为JSONArray对象
            jsonString = jsonString.replaceAll("^[__]\\w{14}+[_ = ]+", "[");
            // System.out.println(jsonString+"]");
            String jsonString2 = jsonString + "]";
            // 把STRING转化为json对象
            array = JSONArray.fromObject(jsonString2);

            // 获取JSONArray的JSONObject对象，便于读取array里的键值对
            jsonObject = array.getJSONObject(0);    
            if (jsonObject != null && jsonObject.getString("province") != null) {
            	province = jsonObject.getString("province");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return province;
    }

    /**
     * 计算多个号码的归属地
     * @param mobileNumbers 号码列表
     * @return
     * @throws MalformedURLException
     */
    public static JSONObject calcMobilesCities(List<String> mobiles) throws MalformedURLException{
        JSONObject jsonNumberCity = new JSONObject();
        for(String mobile : mobiles){
            jsonNumberCity.put(mobile, calcMobileCity(mobile));            ;
        }       
        return jsonNumberCity;
    }

    public static void main(String[] args) throws Exception{
        String testMobileNumber = "13832522226";
        System.out.println(calcMobileCity(testMobileNumber));
        List<String> mobileList = new ArrayList<String>();
//        for(int i = 1350345; i < 1350388; i++){
//            mobileList.add(String.valueOf(i));
//        }
//        System.out.println(calcMobilesCities(mobileList).toString());
    }

}
