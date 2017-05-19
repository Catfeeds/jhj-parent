package com.meijia.utils;

import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 配置文件初始化类
 *
 */
@SuppressWarnings("serial")
public final class ConfigUtil extends Properties {
	public static ConfigUtil instance;
	public ResourceBundle rb;

	private ConfigUtil() {
		this.rb = ResourceBundle.getBundle("config");
	}

	// 保持对象同步
	private static synchronized void makeInstance() {
		if (instance == null) {
			instance = new ConfigUtil();
		}
	}

	public static ConfigUtil getInstance() {
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

	public static void setInstance(ConfigUtil instance) {
		ConfigUtil.instance = instance;
	}
}
