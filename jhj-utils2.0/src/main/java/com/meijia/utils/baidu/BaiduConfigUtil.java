package com.meijia.utils.baidu;

import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 配置文件初始化类
 *
 */
public final class BaiduConfigUtil extends Properties {
	public static BaiduConfigUtil instance;
	public ResourceBundle rb;

	private BaiduConfigUtil() {
		this.rb = ResourceBundle.getBundle("baidu_config");
	}

	// 保持对象同步
	private static synchronized void makeInstance() {
		if (instance == null) {
			instance = new BaiduConfigUtil();
		}
	}

	public static BaiduConfigUtil getInstance() {
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

	public static void setInstance(BaiduConfigUtil instance) {
		BaiduConfigUtil.instance = instance;
	}
}
