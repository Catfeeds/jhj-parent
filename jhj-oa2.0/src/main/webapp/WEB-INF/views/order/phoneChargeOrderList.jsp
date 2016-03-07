<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
<%@ taglib prefix="orderStatusTag"
	uri="/WEB-INF/tags/orderStatusName.tld"%>
<html>
<head>
<title>话费充值订单</title>
<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>

<!--css for this page-->
<link
	href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>"
	rel="stylesheet" type="text/css" />
</head>

<body>

	<section id="container"> <!--header start--> <%@ include
		file="../shared/pageHeader.jsp"%> <!--header end-->

	<!--sidebar start--> <%@ include file="../shared/sidebarMenu.jsp"%>
	<!--sidebar end--> <!--main content start--> <section id="main-content">
	<section class="wrapper"> <!-- page start-->

	<div class="row">
		<div class="col-lg-12">
			<section class="panel"> <form:form
				modelAttribute="oaPhoneChargeOrderSearchVoModel"
				action="phone_charge_order_list" method="GET">
				<h4>数据搜索</h4>
				<div class="form-inline">
				
					用户号码：
					<form:input path="userMobile" maxlength="11"/>
					充值号码：
					<form:input path="chargeMobile" maxlength="11" />
					充值金额：
					<form:select path="money">
						<option value="">请选择充值金额</option>
						<form:option value="20">20元</form:option>
						<form:option value="30">30元</form:option>
						<form:option value="50">50元</form:option>
						<form:option value="100">100元</form:option>
						<form:option value="200">200元</form:option>
					</form:select>
					
					充值状态：
					<form:select path="searchStatus">
						<option value="">请选择充值状态</option>
						<form:option value="101">充值成功</form:option>
						<form:option value="102">充值失败</form:option>
						<form:option value="103">取消充值</form:option>
					</form:select>
					
					
					
				<input type="submit" value="搜索">
				</div>
			</form:form>

			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />

			<h4>话费订单列表</h4>

			<!--  <button id="exportExcel" class="btn btn-success">导出Excel</button> -->

			<table class="table table-striped table-advance table-hover" id="table2excel">
				<thead>
					<tr>
						<th>用户手机号</th>
						<th>充值手机号</th>
						<th>充值金额</th>
						<th>实际支付金额</th>
						<th>优惠券面值</th> 
						<th>充值状态</th>
						<th>充值时间</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${oaPhoneChargeOrderListVoModel.list}" var="item">
						<tr>

							<td>${ item.mobile }</td>
							
							<td>${ item.serviceContent }</td>
							
							<td>${ item.orderMoney }</td>
							
							<td>${ item.realMoney }</td>
							
							<td>
								<c:if test="${ item.couponValue == 0 }">
									未使用优惠券
								</c:if>
								<c:if test="${ item.couponValue != 0}">
									${ item.couponValue}
								</c:if>
							</td> 
							<td>
								<c:if test="${ (item.orderStatus == 16 || item.orderStatus == 13) && item.remarks == '10000' }">
									取消充值
								</c:if>
								
								<c:if test="${ item.orderStatus == 15 || ((item.orderStatus == 16 || item.orderStatus == 13) && item.remarks == '10001')}">
									<span style="color: red; font-weight: bold;">充值失败</span>
								</c:if> 
								<c:if test="${ item.orderStatus == 14 }">
									<span style="color: green; font-weight: bold;">充值成功</span>
								</c:if>
							</td>
							<td>
								<timestampTag:timestamp patten="yyyy-MM-dd HH:mm" t="${item.addTime * 1000}" />
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>


			</section>

			<c:import url="../shared/paging.jsp">
				<c:param name="pageModelName" value="oaPhoneChargeOrderListVoModel" />
				<c:param name="urlAddress" value="/order/phone_charge_order_list" />
			</c:import>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include
		file="../shared/pageFooter.jsp"%> <!--footer end-->
	</section>

	<!-- js placed at the end of the document so the pages load faster -->
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>

	<%-- <script type="text/javascript" src="<c:url value='/assets/jquery.table2excel.js'/>"></script> --%>

</body>
</html>
