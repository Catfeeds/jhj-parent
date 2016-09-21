package com.jhj.action;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.meijia.utils.vo.AppResultData;

public class BaseController {

	public static Logger logger = LoggerFactory.getLogger(BaseController.class);

    /** 基于@ExceptionHandler异常处理 */
    @ExceptionHandler
    public AppResultData<String> exp(HttpServletRequest request, Exception ex) {

        request.setAttribute("ex", ex);

        logger.error("Global exception found, Exception is: {}", ex);
//        System.out.println("-----------------------message--------------------------------");
//        System.out.println(ex.getMessage());
//        System.out.println("-----------------------cause--------------------------------");
//        System.out.println(ex.getCause());
//        System.out.println("-----------------------print--------------------------------");
//        ex.printStackTrace();

		AppResultData<String> result = new AppResultData<String>(
				Constants.ERROR_100, ConstantMsg.ERROR_100_MSG, "");
		return result;
    }
    
    @InitBinder  
    protected void initBinder(WebDataBinder binder) {  
    	
    	binder.setBindEmptyMultipartFiles(false);
    	
    	binder.setDisallowedFields("serviceImgUrl");
    } 
    
}