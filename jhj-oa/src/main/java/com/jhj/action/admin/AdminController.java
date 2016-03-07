package com.jhj.action.admin;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;


public abstract class AdminController {

	protected final String searchVoName="searchVo";
	protected final String treeDataSourceName="treeDataSource";
	protected final String selectDataSourceName="selectDataSource";

	public static Logger logger = LoggerFactory.getLogger(AdminController.class);
	@ExceptionHandler
    public String exception(HttpServletRequest request, Exception e) {

        request.setAttribute("exceptionMessage", e.getMessage());
        logger.error("Global exception found, Exception is: {}", e);
        // 根据不同错误转向不同页面
        if(e instanceof SQLException)
            return "testerror";
        else
            return "error";
    }

    public String tips(HttpServletRequest request, String tips) {
        request.setAttribute("tips", tips);
        return "common/tips";
    }
}
