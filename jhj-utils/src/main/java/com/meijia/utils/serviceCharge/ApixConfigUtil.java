package com.meijia.utils.serviceCharge;

import java.util.Properties;
import java.util.ResourceBundle;

/**
 *
 * @author :hulj
 * @Date : 2015年10月13日下午8:07:30
 * @Description: 
 *		
 *		处理  来自	http://www.apix.cn/ 这个 服务提供商的 各种接口参数
 *			
 *			配置在 apix_config.properties 文件中
 *		
 *		可通用，只需在 配置文件加入 各种  服务的  "key" 即可
 */
public class ApixConfigUtil extends Properties {

	private static final long serialVersionUID = 1L;
	
	public static ApixConfigUtil instance;
	
	public ResourceBundle resourceBundle;
	
	public ApixConfigUtil() {
		this.resourceBundle = ResourceBundle.getBundle("apix_config");
	}
	
	
	// 保持对象同步
	private static synchronized void makeInstance() {
		if (instance == null) {
			instance = new ApixConfigUtil();
		}
	}

	public static ApixConfigUtil getInstance() {
		if (instance != null) {
			return instance;
		} else {
			makeInstance();
			return instance;
		}
	}
	


	public static void setInstance(ApixConfigUtil instance) {
		ApixConfigUtil.instance = instance;
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	
	
	
}
