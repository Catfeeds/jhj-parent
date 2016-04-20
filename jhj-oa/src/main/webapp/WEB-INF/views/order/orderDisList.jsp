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

  </body>
</html>