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
                      	  
                      	  <form:form modelAttribute="oaOrderSearchVoModel" action="cash-list" method="POST">
	                         <header class="panel-heading">
	                         	<h4>数据搜索</h4>
	                         		<div>
	                     					员工名称：
	                     					<form:input path="staffName" />
	                     					手机号码：
	                     					<form:input path="mobile" />
	                     					订单状态：
	                     					<form:select path="orderStatus">
	                     						<form:option value="">--请选择订单状态--</form:option>
	                     						<form:option value="0">申请中</form:option>
	                     						<form:option value="1">财务处理中</form:option>
	                     						<form:option value="2">申请被驳回</form:option>
	                     						<form:option value="3">已打款</form:option>
	                     					</form:select>
											
									<input type="submit"  value="搜索"  >
								</div>
								</div>   
	                         </header>
                           </form:form>   
                           
                      	<hr style="width: 100%; color: black; height: 1px; background-color:black;" />  
                      	
                          <header class="panel-heading">
                          	<h4>提现列表</h4>
                          </header>
                          
                         <!--  <button id="exportExcel" class="btn btn-success">导出Excel</button> -->
                          
                          <table class="table table-striped table-advance table-hover" id="table2excel">
                              <thead>
                              <tr>	  
                                	  <th>订单号 </th>
                                	  <th>员工名称</th>
		                              <th >手机号</th>
		                              <th >申请提现金额</th>
		                              <th >订单状态</th>
		                              <th >备注</th>
		                              <th >添加时间</th>
		                              <th >更新时间</th>
		                              <th>操作</th> 
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${contentModel.list}" var="item">
                             
                              	
                              <tr>	
							            
                                  	    <td>${ item.orderNo }</td>
                                  	    <td>${item.staffName }</td>
                                  	    <td>${ item.mobile }</td>
                                  	    <td>${ item.orderMoney }</td>
                                  	     <td>
							            	<c:if test = "${item.orderStatus== 0}">
							            		申请中
							            	</c:if>
							            	 <c:if test = "${item.orderStatus== 1}">
							            		财务处理中
							            	</c:if>
							            	<c:if test = "${item.orderStatus== 2}">
							            		申请被驳回
							            	</c:if> 
							            	<c:if test = "${item.orderStatus== 3}">
							            		已打款
							            	</c:if> 
							            </td>
							             <td>${ item.remarks }</td>
							            <td>
							            	<timestampTag:timestamp patten="yyyy-MM-dd" t="${item.addTime * 1000}"/>
							            </td>
							            <td>
							            	<timestampTag:timestamp patten="yyyy-MM-dd" t="${item.updateTime * 1000}"/>
							            </td>
							       	<td><button id="btn_update" onClick="btn_update('staff/cash-form?orderId=${ item.orderId }')" class="btn btn-primary btn-xs" title="审批"><i class=" icon-ambulance"></i></button></td>
							       		<%-- <td>
							       			<button id="btn_update" onClick="btn_update('msg/msgForm?id=${ item.id }')" class="btn btn-primary btn-xs" title="修改"><i class="icon-pencil"></i></button>
	                                  		<button id="btn_del" onClick="btn_del('/account/delete/${item.id}')" class="btn btn-danger btn-xs"  title="删除"><i class="icon-trash "></i></button>
                                  	   
							       		</td> --%>
                              </tr>
                             
                              </c:forEach>
                              </tbody>
                          </table>

                          
                      </section>
                      <!--common script for all pages-->
   					  <%@ include file="../shared/importJs.jsp"%>
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="contentModel"/>
	        				<c:param name="urlAddress" value="/staff/cash-list"/>
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
    

    <%-- <script type="text/javascript" src="<c:url value='/assets/jquery.table2excel.js'/>"></script> --%>
    <!--script for this page-->	
    <script	type="text/javascript"  src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>
    
	<script src="<c:url value='/js/jhj/order/orderList.js'/>"></script>
  </body>
</html>
