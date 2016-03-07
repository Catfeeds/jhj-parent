<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ include file="../shared/importCss.jsp"%>

<html>
  <head>
  	<title>助理管辖用户列表</title>
  
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
                      	
                          <header class="panel-heading">  
                          	<h4> 助理所辖用户列表</h4>
                          	<div class="pull-right">
                          		
                          		<input type="hidden" value="${amVoModel.userIdList }" id="userIdList"> 
                          		
                          		<input type="hidden" value="${amVoModel.oldAmId }" id="oldAmId">
                          		
                          		当前助理:<span>${amVoModel.amName }</span>
                          		&nbsp;&nbsp;&nbsp;&nbsp;
                          		<select id="amId">
                          			<option value="0">请选择助理</option>
	                         		<c:forEach items="${amVoModel.amList }" var="am">
	                         			<option value="${am.staffId }">${am.name }</option>
	                         		</c:forEach>
                          		</select>
                          		<button id="changeAm" class="btn btn-primary" type="button">更换助理</button>
                          		
                    		</div>  
                    		
                    		    
                          </header>
                              		<input type="hidden" id="mySelect" >

                          <table class="table table-striped table-advance table-hover">
                              <thead>
	                              <tr>	
	                              		<th>
	                              			<input type="checkbox" id="selectAll"  >全选
	                              		</th>  
	                              		<th>用户姓名</th>
	                              		<th>用户性别</th>
	                              		<th>电话号码</th>
	                              </tr>
                              </thead>
                              <tbody>
                              
                              <c:forEach items="${userListModel.list}" var="user">
	                              <tr>	
										<td><input type="checkbox" id="oneSelect" name="oneSelect" value="${user.id }"></td>	                              		
	                              		<td>${ user.name }</td>
	                              		<td>
	                              			<c:choose>
												<c:when test="${ user.sex == 0}">
														男
												</c:when>
												<c:when test="${ user.sex == 1}">
														女
												</c:when>
											</c:choose>	
	                              		</td>
										<td>${ user.mobile }</td>
							</tr>
                              </c:forEach>
                              </tbody>
                          </table>
                      
                      </section>
                      
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="userListModel"/>
	        				<c:param name="urlAddress" value="/bs/getOwnUser"/>
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
	<script type="text/javascript" src="<c:url value='/js/jhj/bs/amUserList.js'/>"></script>
  </body>
</html>
