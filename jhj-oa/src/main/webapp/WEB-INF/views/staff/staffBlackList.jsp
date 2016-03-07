<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<!-- taglib for this page -->
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
      
      <!--main content start-->
      <section id="main-content">
          <section class="wrapper">
              <!-- page start-->

              <div class="row">
                  <div class="col-lg-12">
                      <section class="panel">
                      	  
                      	 	<%-- <form:form modelAttribute="orgStaffDetailPaySearchVoModel" action="staffBlack-list" method="GET">
                          <header class="panel-heading">
                          	<h4>数据搜索</h4>
                          		<div>
                          					姓名：<form:input path="name" />
                          					手机号：<form:input path="mobile"/>
									<input type="submit"  value="搜索"  >
								</div>   
                          </header>
                           </form:form>  --%>    
                           <!-- <div class="pull-right">
                          		<button onClick="btn_add('/staff/staffBlackForm?id=0')" class="btn btn-primary" type="button"><i class="icon-expand-alt"></i>新增</button>
                    		</div> --> 
                      	<header class="panel-heading">
                          	<h4>服务人员黑名单列表</h4>
                          	
                          </header>
                      	<hr style="width: 100%; color: black; height: 1px; background-color:black;" />  
                      	
                          
                          
                         <!--  <button id="exportExcel" class="btn btn-success">导出Excel</button> -->
                          
                          <table class="table table-striped table-advance table-hover" id="table2excel">
                              <thead>
                              <tr>	  
                             		  <th >服务人员姓名 </th>
                                	  <th >服务人员手机号 </th>
		                              <th >黑名单类型</th>
		                              <th >备注</th>
		                              <th >添加时间</th>
		                              <!-- <th>操作</th>  -->
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${contentModel.list}" var="item">
                              
                              	
                              <tr>	
							            <td>${ item.name }</td>
                                  	    <td>${ item.mobile }</td>
                                  	    <%-- ${ item.blackType } --%>
                                  	    <td><c:choose>
										
										<c:when test="${item.blackType==0}">
																				　　
										 系统判断
										
										</c:when>
										
										<c:when test="${item.blackType==1}">
																				　　
										   人工加入
										
										</c:when>
										</c:choose>
										</td>
							            
							            <td>${ item.remarks }</td>
							            <td>
							            	<timestampTag:timestamp patten="yyyy-MM-dd" t="${item.addTime * 1000}"/>
							            </td>

							       <%-- 	<td><button id="btn_update" onClick="btn_update('order/orderView?orderNo=${ item.id }}')" class="btn btn-primary btn-xs" title="订单详情"><i class=" icon-ambulance"></i></button></td> --%>
							       		
                              </tr>
                              	</c:forEach>
                              </tbody>
                          </table>

                          
                      </section>
                      
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="contentModel"/>
	        				<c:param name="urlAddress" value="/staff/staffBlack-list"/>
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

    <!-- js placed at the end of the document so the pages load faster -->
    <!--common script for all pages-->
    <%@ include file="../shared/importJs.jsp"%>

    <%-- <script type="text/javascript" src="<c:url value='/assets/jquery.table2excel.js'/>"></script> --%>
    <!--script for this page-->	
    <script	type="text/javascript"  src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>
    
  </body>
</html>
