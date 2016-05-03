<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<!-- taglib for this page -->
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld" %>
<%@ taglib prefix="orderVoStatusTag" uri="/WEB-INF/tags/orderVoStatusName.tld" %>


<html>
  <head>
	
	<!--common css for all pages-->
	<%@ include file="../shared/importCss.jsp"%>
	
	<!--css for this page-->
	<link href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>"
	     rel="stylesheet" type="text/css" />
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
                          	<h4>条件检索</h4>
                          	<form:form modelAttribute="oaOrderDisSearchVoModel" onsubmit="return checkEndTime()"
                          	action="cal-list" class="form-inline"
											method="GET">
								
								<div class="form-group">	
									员工姓名:<form:input path="staffName" class="form-control" maxlength="5" autocomplete="off" type="text"/>
								</div>
								
								<div class="form-group">
	                          		开始时间：
									<form:input path="startTimeStr" class="form-control form_datetime"
									 style="width:110px; margin-bottom:0" readonly="true" />
								</div>
								<div class="form-group">
									结束时间：
									<form:input path="endTimeStr" class="form-control form_datetime" 
									style="width:110px; margin-bottom:0" readonly="true" />
								</div> 
											
											
								<button type="submit" class="btn btn-primary" >搜索</button>
							</form:form>
                          </header>
                          
                       <hr style="width: 100%; color: black; height: 1px; background-color: black;" />   
							<header class="panel-heading">
								<h4>派工列表</h4>
							</header>	
							
                          <table class="table table-striped table-advance table-hover">
                              <thead>
                              <tr>	  
                                	  <th >订单号</th>
		                              <th >用户名</th>
		                              <th >用户手机</th>
		                              <th >服务地址</th>
		                              <th >服务时间</th>
		                              <th >云店名称</th>
		                              <th >阿姨名称</th>
		                              <th >阿姨手机号</th>
		                              <th >订单状态</th>
		                              <th >操作</th>
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${oaOrderDisVoModel.list}" var="item">
                              <tr>	
                                  	  <td>${ item.orderNo }</td>
                                  	  <td>${ item.userName }</td>
                                  	  <td>${ item.mobile }</td>
                                  	  <td>${ item.addrName }</td>
                                  	  <td>
                                  	  	<timestampTag:timestamp patten="yyyy-MM-dd HH:mm" t="${item.serviceDate * 1000}"/>
                                  	  </td>
									  <td>${ item.orgName }</td>
									  <td>${ item.staffName }</td>
									  <td>${ item.staffMobile }</td>
									  <td>
									  	 <orderVoStatusTag:orderstatus orderStatus="${item.orderStatus }" 
 											 	orderType="${item.orderType }" /> 
									  </td>
							       	  <td>
							       	  	<!-- 如果是钟点工订单 -->
							       	  	<c:if test="${item.orderType == 0 }">
								       		<button id="btn_update" 
								       				onClick="btn_update('order/order-hour-view?orderNo=${ item.orderNo }&disStatus=${item.dispatchStatus}')" 
								       				class="btn btn-primary btn-xs" title="订单详情">
								       				<i class=" icon-ambulance"></i>
								       		</button>
							       	  	</c:if>
							       	  	
							       	  	<!-- 如果是助理订单 -->
							       	  	<c:if test="${item.orderType == 2 }">
							       	  		<button id="btn_update" 
								       				onClick="btn_update('order/order-am-view?orderNo=${ item.orderNo }&disStatus=${item.dispatchStatus}')" 
								       				class="btn btn-primary btn-xs" title="订单详情">
								       				<i class=" icon-ambulance"></i>
								       		</button>
							       	  	</c:if>
							       	  </td>
                              </tr>
                              </c:forEach>
                              </tbody>
                          </table>
                      </section>
                      
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="oaOrderDisVoModel"/>
	        				<c:param name="urlAddress" value="/order/cal-list"/>
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
    
    <script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
	
	<script type="text/javascript">
	$('.form_datetime').datepicker({
	 	format: "yyyy-mm-dd",
	 	language: "zh-CN",
	 	autoclose: true,
	 	startView: 1,
	 	todayBtn:true
	 });
		
	 function checkEndTime(){  
	     var startTime=$("#startTimeStr").val();  
	     var start=new Date(startTime.replace("-", "/").replace("-", "/"));  
	     var endTime=$("#endTimeStr").val();  
	     var end=new Date(endTime.replace("-", "/").replace("-", "/"));  
	     if(end<start){ 
	     	
	     	alert('结束日期必须大于开始时间');
	     	 return false;  
	     }  
	     return true;  
	 }	
	
	
	</script>
	
	
  </body>
</html>