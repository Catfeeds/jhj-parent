package com.meijia.utils.redis;

import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 配置文件初始化类
 *
 */
@SuppressWarnings("serial")
public final class RedisConfigUtil extends Properties {
	public static RedisConfigUtil instance;
	public ResourceBundle rb;

	private RedisConfigUtil() {
		this.rb = ResourceBundle.getBundle("redis");
	}

	// 保持对象同步
	private static synchronized void makeInstance() {
		if (instance == null) {
			instance = new RedisConfigUtil();
		}
	}

	public static RedisConfigUtil getInstance() {
		if (instance != null) {
			return instance;
		} else {
			makeInstance();
			return instance;
		}
	}

	public ResourceBundle getRb() {
		return rb;
	}

	public void setRb(ResourceBundle rb) {
		this.rb = rb;
	}

	public static void setInstance(RedisConfigUtil instance) {
		RedisConfigUtil.instance = instance;
	}
}
