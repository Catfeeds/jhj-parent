package com.meijia.utils.baidu;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import com.meijia.utils.HttpClientUtil;
import com.meijia.utils.JsonUtil;

public class DwzUtil {
	
    /**
     * 百度生成短地址Api
     * @param url 长地址
     * @return String
     * @throws MalformedURLException 
     */
    public static String dwzApi(String longUrl) throws MalformedURLException {

        String urlString = "http://dwz.cn/create.php";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("url", longUrl);
        
        String result = HttpClientUtil.post(urlString, params);
        Map<String, Object> resultMap = JsonUtil.stringToMap(result);
        String tinyUrl = resultMap.get("tinyurl").toString();
        return tinyUrl;
    }

 
    public static void main(String[] args) throws Exception{
        String longUrl = "http://yougeguanjia.com/onecare-oa/d/ty-sz1";
        System.out.println(dwzApi(longUrl));
        

    }
    
    
    

}
