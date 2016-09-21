package com.meijia.wx.utils;

import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 配置文件初始化类
 *
 */
public final class WxConfig extends Properties {
	public static WxConfig instance;
	public ResourceBundle rb;

	private WxConfig() {
		this.rb = ResourceBundle.getBundle("wx-pay");
	}

	// 保持对象同步
	private static synchronized void makeInstance() {
		if (instance == null) {
			instance = new WxConfig();
		}
	}

	public static WxConfig getInstance() {
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

	public static void setInstance(WxConfig instance) {
		WxConfig.instance = instance;
	}
}
