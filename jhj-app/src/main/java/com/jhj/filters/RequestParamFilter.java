package com.jhj.filters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meijia.utils.vo.AppResultData;
import com.meijia.utils.TimeStampUtil;
@Component
public class RequestParamFilter implements HandlerInterceptor{


	public static Logger logger = LoggerFactory.getLogger(RequestParamFilter.class);

	private NamedThreadLocal<Long>  startTimeThreadLocal =
			new NamedThreadLocal<Long>("StopWatch-StartTime");


	// preHandle()方法在业务处理器处理请求之前被调用
	@Override
	public boolean preHandle(HttpServletRequest request,
											  HttpServletResponse response,
											  Object handler)
		throws Exception {
		request.setCharacterEncoding("utf-8");

		//设置请求开始时间，便于返回值时，打印运行时间
		long beginTime = TimeStampUtil.getNow();
		startTimeThreadLocal.set(beginTime);//线程绑定变量（该数据只有当前请求的线程可见）

		logger.info("============== 1. preHandle ===================");


		logger.info("request begin_time: "+ TimeStampUtil.timeStampToDateStr(beginTime));
		logger.info(request.getMethod() + " " + request.getRequestURL());


		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
			params.put(name, valueStr);
		}
		logger.info(params.toString());
		System.out.println(params.toString());
		return true;
	}

	// postHandle()方法在业务处理器处理请求之后被调用
	@Override
	public void postHandle(HttpServletRequest request,
										  HttpServletResponse response,
										  Object handler,
										  ModelAndView modelAndView)
		throws Exception {

		logger.info("============== 2. postHandle ==================");

		logger.info(handler.toString());

		if (modelAndView != null) {
			logger.info(modelAndView.getViewName());
			AppResultData<Object> result = (AppResultData<Object>) modelAndView.getModel().get("appResultData");

			if (result != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				String str = objectMapper.writeValueAsString(result);
				logger.info(str);
			}
		}

	}

	// afterCompletion()方法在DispatcherServlet完全处理完请求后被调用
	@Override
	public void afterCompletion(HttpServletRequest request,
												 HttpServletResponse response,
												 Object handler, Exception ex)
			throws Exception {

		//2、结束时间
		long endTime = TimeStampUtil.getNow();

		//得到线程绑定的局部变量（开始时间）
        long beginTime = startTimeThreadLocal.get();

        //3、消耗的时间
        long consumeTime = endTime - beginTime;
        logger.info("execute consume_time: " + consumeTime + "ms" );
        //此处认为处理时间超过500毫秒的请求为慢请求
//        if(consumeTime > 500) {
//            //TODO 记录到日志文件
//        	logger.info("execute end_time: " + consumeTime + "ms" );
//        }

        if (ex !=null) {
        	logger.info(ex.getMessage());
        }
        logger.info("============== 3. afterCompletion ===============");
	}

}
