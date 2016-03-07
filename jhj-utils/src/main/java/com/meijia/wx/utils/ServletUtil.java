package com.meijia.wx.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 控制器工具
 * @author json
 *
 */
public class ServletUtil {
	public static void forward(HttpServletRequest request,HttpServletResponse response,String url){
		try{
			request.getRequestDispatcher(url).forward(request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void jump(HttpServletRequest request,HttpServletResponse response,String url){
		try{
			response.sendRedirect(url);
		}catch (Exception e) {
				e.printStackTrace();
		}
	}
	public static void printJson(HttpServletRequest request,HttpServletResponse response,String json) throws IOException{
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
		out.close();
	}
	public static Object getAttr(HttpServletRequest request,String key){
		return request.getSession().getAttribute(key);
	}
	public static void setAttr(HttpServletRequest request,String key,Object value){
		request.getSession().setAttribute(key, value);
	}
}
