package com.meijia.utils;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.imageio.ImageIO;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException; 
/**
 * HTTP工具类
 *
 *
 */
public class ImgServerUtil {

	private static Log log = LogFactory.getLog(ImgServerUtil.class);

	/**
	 * 定义编码格式 UTF-8
	 */
	public static final String URL_PARAM_DECODECHARSET_UTF8 = "UTF-8";

	private static final String URL_PARAM_CONNECT_FLAG = "&";

	private static final String EMPTY = "";

	private static MultiThreadedHttpConnectionManager connectionManager = null;

	private static int connectionTimeOut = 25000;

	private static int socketTimeOut = 25000;

	private static int maxConnectionPerHost = 20;

	private static int maxTotalConnections = 20;

	private static HttpClient client;

	static {
		connectionManager = new MultiThreadedHttpConnectionManager();
		connectionManager.getParams().setConnectionTimeout(connectionTimeOut);
		connectionManager.getParams().setSoTimeout(socketTimeOut);
		connectionManager.getParams().setDefaultMaxConnectionsPerHost(
				maxConnectionPerHost);
		connectionManager.getParams().setMaxTotalConnections(
				maxTotalConnections);
		client = new HttpClient(connectionManager);
	}
	
	 /**
     * 向指定 URL 发送POST方法的请求
     * @param url  发送请求的 URL
     * @param PostData 请求参数，请求参数应该是 byte[] 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPostBytes(String url, byte[] PostData, String fileType) {
        OutputStream outStream = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", fileType);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //二进制
            outStream = conn.getOutputStream();
            outStream.write(PostData);
            outStream.flush();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    } 	
    
    @SuppressWarnings("unused")
	private static String macthContentType(String fileType){
        String contentType = "image/jpeg";
        switch (fileType.toLowerCase()) {
        case "bmp":
            contentType = "application/x-bmp";
            break;
        case "img":
            contentType = "application/x-img";
            break;
        case "jpeg":
            contentType = "image/jpeg";
            break;
        case "jpg":
            contentType = "application/x-jpg";
            break;
        case "png":
            contentType = "image/png";
            break;
        default:
            break;
        }
        return contentType;
    }    
    

    public static String getImgSize(String imgUrl, String w, String h) {
    	return imgUrl + "?w="+ w + "&h=" + h;
    }
    
    //文字加底图.
    public static byte[] ImgYin(String s,String ImgName, int offsetWeight, int offsetHeight){
        byte[] bytes = null;
        try{
            String str = s;
            Image src = null;
            
            if (ImgName.indexOf("http://") >= 0) {
            	URL url = new URL(ImgName);
            	src = ImageIO.read(url);
            } else {
            	File _file = new File(ImgName);
                src = ImageIO.read(_file);
            }
            
            int wideth=src.getWidth(null);
            int height=src.getHeight(null);
            BufferedImage image=new BufferedImage(wideth,height,BufferedImage.TYPE_INT_RGB);
            Graphics g=image.createGraphics();
            g.drawImage(src,0,0,wideth,height,null);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SimHei",Font.BOLD,80));
            Font aa=new Font("SimHei",Font.BOLD,80);

            g.drawString(str,wideth/2+offsetWeight,height/2+offsetHeight);
            g.dispose();
            ByteArrayOutputStream out1 = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", out1);
            bytes = out1.toByteArray();
            out1.close();
           
        }
        catch(Exception e){
            System.out.println(e);
        }
        return bytes;
    }
     
    
    
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {

//		String jsonStr = "{\"ret\":true,\"info\":{\"md5\":\"d2a771e58a0f93b92758260bc46c311a\",\"size\":17728}}";
//		ObjectMapper mapper = new ObjectMapper();
//		
//
//		
//		HashMap<String,Object> o = mapper.readValue(jsonStr, HashMap.class); 
//		
//		System.out.println(o.get("ret"));
//		System.out.println(o.get("info"));
//		
//		HashMap<String,String> info = (HashMap<String, String>) o.get("info");
//		
//		System.out.println(info.get("md5").toString());
//		System.out.println(info.get("size").toString());
		
		//中文两个字
		ImgYin("马云" , "http://img.51xingzheng.cn/85ebd46f40d90977ee01ead3e71bd6fa",-82, 22); 
//		
//		//英文两个字
//		ImgYin("MC" , "/Users/lnczx/Downloads/b.jpg", "/Users/lnczx/Downloads/b2.jpg", -40 , 20); 
	}

}