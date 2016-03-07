package com.jhj.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.jhj.service.dict.DictService;

public class StartOnLoadPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object obj, String arg1)
            throws BeansException {
        if(obj instanceof DictService) {
            ((DictService)obj).loadData(); //调用方法加载数据
        }
        return obj;
    }

    @Override
	public Object postProcessBeforeInitialization(Object arg0, String arg1)
            throws BeansException {
        return arg0;
    }

}
