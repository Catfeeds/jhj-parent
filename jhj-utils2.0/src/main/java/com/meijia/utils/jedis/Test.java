package com.meijia.utils.jedis;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author :hulj
 * @Date : 2015年11月30日上午11:16:13
 * @Description: 
 *
 *		jedis	--2.8.0 测试
 *
 *		redis版本 --redis64-2.8.2101
 */
public class Test {
	
	JedisPool jedisPool;
	Jedis jedis;
	
	@Before
	public void setUp(){
//		jedisPool = new JedisPool(new JedisPoolConfig(),"192.168.1.101");
//		jedis = jedisPool.getResource();
		
		jedis = new Jedis("192.168.37.9");
	}
	
	@org.junit.Test
    public void testBasicString(){  
        //-----添加数据----------  
        jedis.set("name","minxr");//向key-->name中放入了value-->minxr  
        System.out.println(jedis.get("name"));//执行结果：minxr  
  
        //-----修改数据-----------  
        //1、在原来基础上修改  
        jedis.append("name","jarorwar");   //很直观，类似map 将jarorwar append到已经有的value之后  
        System.out.println(jedis.get("name"));//执行结果:minxrjarorwar  
  
        //2、直接覆盖原来的数据  
        jedis.set("name","闵晓荣");  
        System.out.println(jedis.get("name"));//执行结果：闵晓荣  
  
        //删除key对应的记录  
        jedis.del("name");  
        System.out.println(jedis.get("name"));//执行结果：null  
  
        /** 
         * mset相当于 
         * jedis.set("name","minxr"); 
         * jedis.set("jarorwar","闵晓荣"); 
         */  
        jedis.mset("name","minxr","jarorwar","闵晓荣");  
        
        jedis.mset("value","hhha","sex","ddd");
        
        System.out.println(jedis.mget("name","jarorwar","value","sex"));  
  
    } 
	
	@org.junit.Test
	public void testaaa() throws UnknownHostException{
		
		
		InetAddress address = InetAddress.getLocalHost();
		
		Inet6Address address2 = (Inet6Address) Inet6Address.getLocalHost();
		
		System.out.println(address2.getHostName());
		System.out.println(address2.getHostAddress());
		
		
		
	}
	
	/**
     * IceWee 2013.07.19
     * 获取本地IP列表（针对多网卡情况）
     *
     * @return
     */
    public static List<String> getLocalIPList() {
        List<String> ipList = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
                        ip = inetAddress.getHostAddress();
                        ipList.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        
        for (String ip : ipList) {
			System.out.println(ip);
		}
        
        return ipList;
    }
    
    public static void main(String[] args) {
    	
    	System.out.println(getMap().get("KEY"));
    	
	}
    
    public static Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("KEY", "INIT");
         
        
        try {
            map.put("KEY", "TRY");
            return map;
        }
        catch (Exception e) {
            map.put("KEY", "CATCH");
        }
        finally {
            map.put("KEY", "FINALLY");
            map = null;
//            return map1;
        }
        return map;
    }
    
    
}
