<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

	<!--taglib for this page  -->
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld" %>
<%@ taglib prefix="orderTypeTag" uri="/WEB-INF/tags/orderTypeName.tld" %>
<%@ taglib prefix="payTypeTag" uri="/WEB-INF/tags/payTypeName.tld" %>
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
			<%--  <%@ include file="../common/user/userDetailSearch.jsp"%> --%>
              <div class="row">
                  <div class="col-lg-12">
                      <section class="panel">
                          <header class="panel-heading">
                          	<h4>会员消费明细列表</h4>
                        	
                        	<form:form class="form-inline" onsubmit="return checkEndTime()"
                        		modelAttribute="userPayDetailSearchVoModel" action="user-pay-detail" method="get" >
                       	  
                          		<div class="form-group">
                          				会员手机号:<form:input path="mobile" class="form-control"  type="number"/>
                          		</div>	
                          		
                          		<div class="form-group">
                          				订单号:<form:input path="orderNo" class="form-control"  type="number"/>
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
                          
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                          

                          <table class="table table-striped table-advance table-hover">
                              <thead>
                              <tr>
                            		  <th >订单号</th>
		                              <th >会员手机号</th>
		                              <th >订单类型</th>
		                              <th>订单总金额</th>
		                              <th >订单实际支付</th>
		                              <th>交易状态</th>
		                              <th>付款类型</th>
		                              <th >添加时间</th>
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${userPayDetailList.list}" var="item">
                              <tr>
                              			<td>
											${ item.orderNo }
							            </td>
							            <td>${ item.mobile }</td>
							            <td>
							            
							            	<!-- todo 不能显示 充值记录！！ -->
							            	<orderTypeTag:orderTypeId orderTypeId="${ item.orderType }"/>
							            </td>
							            <td align="center">
											￥${ item.orderMoney }
							            </td>
							            <td align="center" >
									    	       ￥ ${ item.orderPay }
							            </td>
							            <td>
												支付成功
							            </td>
							            <td>

											<payTypeTag:payType payType="${ item.payType }" orderStatus="2"/>
							            </td>
							            <td>
							            	<timestampTag:timestamp patten="yyyy-MM-dd HH:mm:ss" t="${item.addTime * 1000}"/>
							            </td>
                              </tr>
                              </c:forEach>
                              </tbody>
                          </table>

                          
                      </section>
                      
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="userPayDetailList"/>
	        				<c:param name="urlAddress" value="/user/user-pay-detail"/>
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

    <!--script for this page-->	
    
    <script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
    
 	<script src="<c:url value='/js/jhj/user/userDetailList.js'/>"></script>

  </body>
</html>