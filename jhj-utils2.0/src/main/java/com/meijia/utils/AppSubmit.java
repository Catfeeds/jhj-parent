package com.meijia.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;


public class AppSubmit {


    /**
     * 建立请求，以模拟远程HTTP的POST请求方式构造并获取支付宝的处理结果
     * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值
     * 如：buildRequest("", "",sParaTemp)
     * @param strParaFileName 文件类型的参数名
     * @param strFilePath 文件路径
     * @param sParaTemp 请求参数数组
     * @return 支付宝处理结果
     * @throws Exception
     */
	public static void appAliay(String url, Map<String, String> sPara) {

	       PostMethod postMethod = new PostMethod(url);
	       postMethod.setRequestBody(generatNameValuePair(sPara));

	       HttpClient httpClient = new HttpClient();
	       // 执行postMethod
	       int statusCode = 0;
	       try {
	           statusCode = httpClient.executeMethod(postMethod);
	       } catch (HttpException e) {
	           e.printStackTrace();
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	       // HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
	       // 301或者302
	       if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
	               || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
	           // 从头中取出转向的地址
	           Header locationHeader = postMethod.getResponseHeader("location");
	           String location = null;
	           if (locationHeader != null) {
	               location = locationHeader.getValue();
	               System.out.println("diandianLogin:" + location);
	           } else {
	               System.err.println("Location field value is null.");
	           }
	           return;
	       } else {
	           System.out.println(postMethod.getStatusLine());
	           String str = "";
	           try {
	               str = postMethod.getResponseBodyAsString();
	           } catch (IOException e) {
	               e.printStackTrace();
	           }
	           System.out.println(str);
	       }
	       postMethod.releaseConnection();
	       return;
	   }

    /**
     * MAP类型数组转换成NameValuePair类型
     * @param properties  MAP类型数组
     * @return NameValuePair类型数组
     */
    private static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }

        return nameValuePair;
    }

	public static void main (String[] args){

		Map<String,String> params = new HashMap<String,String>();
	    params.put("discount", "0.00");
		params.put("payment_type", "1");
		params.put("subject", "储值卡测试");
		params.put("trade_no", "2014083156533993");
		params.put("buyer_email", "lnczx@tom.com");
		params.put("gmt_create", "2014-08-31 00:10:23");
		params.put("notify_type", "trade_status_sync");
		params.put("quantity", "1");
		params.put("out_trade_no", "CARD-20140828000009");
		params.put("seller_id", "2088801327645613");
		params.put("notify_time", "2014-08-31 09:34:06");
		params.put("body", "储值卡测试");
		params.put("trade_status", "TRADE_SUCCESS");
		params.put("is_total_fee_adjust", "N");
		params.put("total_fee", "0.01");
		params.put("gmt_payment", "2014-08-31 00:10:34");
		params.put("seller_email", "finance@ayi360.com");
		params.put("price", "0.01");
		params.put("buyer_id", "2088002579362936");
		params.put("notify_id", "4fe26e3e2b5d7e17a4de05dff08e8de376");
		params.put("use_coupon", "N");
		params.put("sign_type", "MD5");
		params.put("sign", "f4ea14da7bbeec2c88f8f03d85941f98");

	    String url = "http://localhost:8080/app/order/alipay_notify";

	    AppSubmit.appAliay(url, params);

	}
}