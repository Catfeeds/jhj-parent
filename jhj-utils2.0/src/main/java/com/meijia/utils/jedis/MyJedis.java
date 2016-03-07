package com.meijia.utils.jedis;

import java.util.ResourceBundle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

/**
 *
 * @author :hulj
 * @Date : 2015年12月1日上午10:30:14
 * @Description: 
 *
 *		枚举实现单例创建 jedisPool
 */
public enum MyJedis {
	
	INSTANCE;
	
	private JedisPool jedisPool;
	
	private MyJedis() {
		
		ResourceBundle rb = ResourceBundle.getBundle("jedisPool_config");
		
		JedisPoolConfig config = new JedisPoolConfig();
		
		//设置最大连接数
		config.setMaxTotal(Integer.parseInt(rb.getString("maxTotal")));
		//设置最大空闲数
		config.setMaxIdle(Integer.parseInt(rb.getString("maxIdle")));
		//设置超时时间
		config.setMaxWaitMillis(Long.parseLong(rb.getString("maxWaitMillis")));
		//在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的
		config.setTestOnBorrow(Boolean.parseBoolean(rb.getString("testOnBorrow")));
		//在return给pool时，是否提前进行validate操作
		config.setTestOnReturn(Boolean.parseBoolean(rb.getString("testOnReturn")));
		
		
		String ip = rb.getString("ip");
		int port = Integer.parseInt(rb.getString("port"));
		
		//初始化一个连接池
		jedisPool = new JedisPool(config, ip, port);
		
	}
	
	/**
     * 同步获取Jedis实例
     * @return Jedis
     */
    @SuppressWarnings("deprecation")
	public synchronized  Jedis getJedis() {  
        Jedis jedis = null;
        try {
        	 if (jedisPool != null) {  
                 jedis = jedisPool.getResource(); 
             }
        } catch (JedisException e) {
        	if(jedis !=null){
        		jedisPool.returnBrokenResource(jedis);  
        	}
            throw e;
        } finally {
        	returnResource(jedis);
        }
        return jedis;
    }  
     
    
    
    /**
     * 释放jedis资源
     * @param jedis
     */
    @SuppressWarnings("deprecation")
	public  void returnResource(final Jedis jedis) {
        if (jedis != null && jedisPool !=null) {
            jedisPool.returnResource(jedis);
        }
    }
     
    
    public static void main(String[] args) {
//		Jedis jedis = MyJedis.INSTANCE.getJedis();
////		
//		System.out.println(jedis.toString());
//    	
//		Jedis jedis2 = MyJedis.INSTANCE.getJedis();
//		System.out.println(jedis2);
//		
//		System.out.println(jedis == jedis2);
    	
    	
	}
	
	
	
}
