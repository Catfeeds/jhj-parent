<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld" %>

<html>
  <head>
	<!--common css for all pages-->
	<%@ include file="../shared/importCss.jsp"%>
	<!--css for this page-->
  </head>
  <body>

  <section id="container" >
	  
	  <!--header start-->
	  <%@ include file="../shared/pageHeader.jsp"%>
	  <!--header end-->
	  
      <!--sidebar start-->
	  <%@ include file="../shared/sidebarMenu.jsp"%>
      <!--sidebar end-->
      
      <section id="main-content">
          <section class="wrapper">

            <div class="row">
                  <div class="col-lg-12">
                      <section class="panel">
                          <header class="panel-heading">
                          	 <h4>调查用户列表</h4>
                          </header>
                          
                          <table class="table table-striped table-advance table-hover" >
                              <thead>
                              <tr>		
                              		  <th>用户名称</th>
                              		  <th>用户手机号</th>
                              		  <th>年龄</th>
		                              <th>性别</th>
		                              <th>调查时间</th>
                                  	  <th>操作</th>
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${surveyUserListModel.list}" var="item">
	                              <tr>  
	                              		<td>${item.userName }</td>
	                              		<td>${item.mobile }</td>
	                              		<td>${item.age }</td>
	                              		<td>
	                              			<c:if test="${item.sex == 0 }">
	                              				男
	                              			</c:if>
	                              			<c:if test="${item.sex == 1 }">
	                              				女
	                              			</c:if>
	                              		</td>
										<td>
							            	<timestampTag:timestamp patten="yyyy-MM-dd HH:mm" t="${item.addTime * 1000}"/>
							            </td>
										
										<td>
											<button id="btn_update" onClick="btn_update('survey/user_result_detail?id=${item.id}')" class="btn btn-primary btn-xs" title="查看详情">
													<i class="icon-pencil"></i>
											</button>
										</td>
								 </tr>
                              </c:forEach>
                              </tbody>
                          </table>
					</section>		
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="surveyUserListModel"/>
	        				<c:param name="urlAddress" value="/survey/survey_user_list"/>
	       			  </c:import>
               	</div>
              </div>
          </section> 
      </section>
      
      <%@ include file="../shared/pageFooter.jsp"%>
  </section>

   <%@ include file="../shared/importJs.jsp"%> 
	
  </body>
</html>