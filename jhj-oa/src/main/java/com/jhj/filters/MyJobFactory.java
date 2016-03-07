package com.jhj.filters;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

/**对于quartz中的Job为了实现spring注入，
 * 必须实现自定义的Job。
 * @author kerry
 *
 */
public class MyJobFactory extends AdaptableJobFactory {
	//spring会自动对该工厂进行注入
	 @Autowired
	  private AutowireCapableBeanFactory capableBeanFactory;

	  @Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
	    //调用父类的方法
	    Object jobInstance = super.createJobInstance(bundle);
	    //进行注入,这属于Spring的技术,不清楚的可以查看Spring的API.
	    capableBeanFactory.autowireBean(jobInstance);
	    return jobInstance;
	  }
}
