package com.jhj.oa.auth;

import javax.servlet.http.HttpServletRequest;

public class AuthHelper {

	public static void setSessionAccountAuth(HttpServletRequest request, AccountAuth accountAuth){
		request.getSession().setAttribute("accountAuth", accountAuth);
	}

	public static AccountAuth getSessionAccountAuth(HttpServletRequest request){
		return (AccountAuth)request.getSession().getAttribute("accountAuth");
	}

	public static void setRequestPermissionMenu(HttpServletRequest request, PermissionMenu permissionMenu){
		request.setAttribute("permissionMenu", permissionMenu);
	}

	public static PermissionMenu getRequestPermissionMenu(HttpServletRequest request){
		return (PermissionMenu)request.getAttribute("permissionMenu");
	}

	public static void  removeSessionAccountAuth(HttpServletRequest request,String accountAuth){
		 request.getSession().removeAttribute(accountAuth);
	}
	
	public static void setSessionLoginOrg(HttpServletRequest request, Long orgId){
		request.getSession().setAttribute("loginOrgId", orgId);
	}
	
	public static Long getSessionLoginOrg(HttpServletRequest request){
		return (Long)request.getSession().getAttribute("loginOrgId");
	}	
}
