<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ include file="../shared/importCss.jsp"%>

<!-- s -->
<%@taglib prefix="astroName" uri="/WEB-INF/tags/astroName.tld" %>

<%@taglib prefix="bloodTypeName" uri="/WEB-INF/tags/bloodTypeName.tld" %>

<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld" %>
<html>
  <head>
  	<title> 验证码列表</title>
  
	<!-- common css for all pages -->
	<%@ include file="../shared/importCss.jsp"%>
	<!-- css for this page -->
	<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>" type="text/css" />
	
  </head>

  <body>
	
  <section id="container" >
	  
	  <!--header start-->
	  <%@ include file="../shared/pageHeader.jsp"%>
	  <!--header end-->
	  
      <!--sidebar start-->
	  <%@ include file="../shared/sidebarMenu.jsp"%>
      <!--sidebar end-->
      
      <!--main content start-->
      <section id="main-content">
          <section class="wrapper">
              <!-- page start-->

              <div class="row">
                  <div class="col-lg-12">
                      <section class="panel">
                      	
                      	<form:form modelAttribute="usersSmsTokenVo" action="token-list" method="GET">
                          <header class="panel-heading">
                          	<h4>数据搜索</h4>
                          		<div>
                          					
                          					手机号：<form:input path="mobile"/>
									<input type="submit"  value="搜索"  >
								</div>    
                          </header>
                           </form:form>   
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                          <header class="panel-heading">  
                          	<h4> 验证码列表</h4>
                          	      
                          </header>
                          <table class="table table-striped table-advance table-hover">
                              <thead>
	                              <tr>	  
	                              		
	                              		<th>手机号</th>
	                              		<th>验证码</th>
	                              		<th>添加时间</th>
	                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${userSmsTokenModel.list}" var="userSmsToken">
	                              <tr>	
										
									
										<td>${userSmsToken.mobile}</td>
										<td>${userSmsToken.smsToken}</td>
								<td>
							            	<timestampTag:timestamp patten="yyyy-MM-dd" t="${userSmsToken.addTime * 1000}"/>
							    </td>
							</tr>
                              </c:forEach>
                              </tbody>
                          </table>
                      
                      </section>
                      
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="userSmsTokenModel"/>
	        				<c:param name="urlAddress" value="/user/token-list"/>
	       			  </c:import>
                  </div>
              </div>
              
              <!-- page end-->
              
          </section> 
      </section>
      <!--main content end-->
      <!--footer start-->
      <%@ include file="../shared/pageFooter.jsp"%>	
      <!--footer end-->
  </section>
    <!--common script for all pages-->
    <%@ include file="../shared/importJs.jsp"%>

  </body>
</html>
