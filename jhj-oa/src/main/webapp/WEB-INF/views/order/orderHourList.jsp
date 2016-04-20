<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<!-- taglib for this page -->
<%@ taglib prefix="orderFromTag" uri="/WEB-INF/tags/orderFromName.tld" %>
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld" %>
<%@ taglib prefix="orderVoStatusTag" uri="/WEB-INF/tags/orderVoStatusName.tld" %>
<%@ taglib prefix="payTypeNameTag" uri="/WEB-INF/tags/payTypeName.tld" %>
<%@ taglib prefix="orderServiceTimeTag" uri="/WEB-INF/tags/orderServiceTime.tld" %>
<%@ taglib prefix="serviceTypeTag" uri="/WEB-INF/tags/serviceTypeName.tld" %>


<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
<%@ taglib prefix="orderTypeNameTag" uri="/WEB-INF/tags/orderTypeName.tld" %>
<%@ taglib prefix="serviceTypeSelectTag" uri="/WEB-INF/tags/serviceTypeSelect.tld" %>
<%@ taglib prefix="orderStatusSelectTag" uri="/WEB-INF/tags/orderSatusSelect.tld" %>
<%@ taglib prefix="orderFromSelectTag" uri="/WEB-INF/tags/orderFromSelect.tld" %>

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
                         	<h4>数据搜索</h4>
	                      	  <form:form modelAttribute="oaOrderSearchVoModel" class="form-inline" action="order-hour-list" method="GET">
		                         		<div class="form-group">
		                     					订单状态：
		                     					<form:select path="orderStatus" class="form-control">
		                     							<option value="">请选择订单状态</option>
		                     							<form:option value="0">已取消</form:option>
		                     							<form:option value="1">未支付</form:option>
		                     							<form:option value="2">已支付</form:option>
		                     							<form:option value="3">已派工</form:option>
		                     							<form:option value="5">开始服务</form:option>
		                     							<form:option value="7">完成服务</form:option>
		                     							<form:option value="8">已评价</form:option>
		                     							<form:option value="9">已关闭</form:option>
		                     					</form:select>
		                     			</div>
										<div class="form-group">		
											<c:if test="${loginOrgId == 0 }">
												选择门店:
												<orgSelectTag:select/>
											</c:if>	
										</div>	
										
										<button type="submit" class="btn btn-primary" >搜索</button>
	                           </form:form>   
	                         </header>
                           
                      	<hr style="width: 100%; color: black; height: 1px; background-color:black;" />  
                      	
                          <header class="panel-heading">
                          	<h4>钟点工订单列表</h4>
                          </header>
                          
                         <!--  <button id="exportExcel" class="btn btn-success">导出Excel</button> -->
                          
                          <table class="table table-striped table-advance table-hover" id="table2excel">
                              <thead>
                              <tr>	  
                                	  <th >门店名称 </th>
                                	  <th >云店名称</th>
		                              <th >下单时间</th>
		                              <th >订单类型</th>
		                              <th >服务日期</th>
		                              <th >用户手机号</th>
		                              <th >服务地址</th>
		                              <th >服务人员</th>
		                             <!--  <th >派工状态</th> -->
		                              <th >订单状态</th>
		                              <th >总金额</th>
		                              <th >支付金额</th>
		                              <th>操作</th> 
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${oaOrderListVoModel.list}" var="item">
                              	<c:forEach items="${item.statusNameMap }" var="sta">
                              	
                              <tr>	
							            
                                  	    <td>${ item.orgName }</td>
                                  	    
                                  	    <td>${ item.cloudOrgName }</td>
							            <td>
							            	<timestampTag:timestamp patten="yyyy-MM-dd" t="${item.addTime * 1000}"/>
							            </td>

							            <td>
											 ${item.orderTypeName } 
							            </td>
							            <td>
											<timestampTag:timestamp patten="yyyy-MM-dd" t="${item.serviceDate * 1000}"/>
										</td>


										<td>${ item.mobile }</td>
							            <td>${ item.orderAddress }</td>
							            <td>
							            	${ item.staffName }
							            </td>
							            <%-- <td>
							            	${item.disStatusName }
							          
							            </td> --%>
							            <td>
							             <orderVoStatusTag:orderstatus orderStatus="${item.orderStatus }" 
 											 	orderType="${item.orderType }" /> 
							            </td>
							            <td>${ item.orderMoney }</td>
							            <td>${ item.orderPay }</td>
							            
							       	<td>
							       		<button id="btn_update" 
							       			onClick="btn_update('order/order-hour-view?orderNo=${ item.orderNo }&disStatus=${fn:substring(sta.key,0,1) }')" 
							       			class="btn btn-primary btn-xs" 
							       			title="订单详情">
							       				<i class=" icon-ambulance"></i>
							       			</button>
							       	</td>
							       		<%-- <td>
							       			<button id="btn_update" onClick="btn_update('msg/msgForm?id=${ item.id }')" class="btn btn-primary btn-xs" title="修改"><i class="icon-pencil"></i></button>
	                                  		<button id="btn_del" onClick="btn_del('/account/delete/${item.id}')" class="btn btn-danger btn-xs"  title="删除"><i class="icon-trash "></i></button>
                                  	   
							       		</td> --%>
                              </tr>
                              	</c:forEach>
                              </c:forEach>
                              </tbody>
                          </table>

                          
                      </section>
                      
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="oaOrderListVoModel"/>
	        				<c:param name="urlAddress" value="/order/order-hour-list"/>
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
    
	<script src="<c:url value='/js/jhj/order/orderList.js'/>"></script>
  </body>
</html>
