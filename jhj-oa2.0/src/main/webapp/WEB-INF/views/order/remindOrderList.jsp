<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
<%@ taglib prefix="orderStatusTag"
	uri="/WEB-INF/tags/orderStatusName.tld"%>
<html>
<head>
<title>提醒订单</title>
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
				modelAttribute="oaRemindOrderSearchVoModel" onsubmit="return checkEndTime()"
				action="remind-order-list" method="GET">
				<h4>数据搜索</h4>
				<div class="form-inline">
					用户手机：
					<form:input path="mobile" maxlength="11" />
					订单状态：
					<form:select path="orderStatus">
						<option value="">请选择订单状态</option>
						<form:option value="10">已预约</form:option>
						<form:option value="11">已完成</form:option>
						<form:option value="12">已取消</form:option>
					</form:select>
					<%-- <form:hidden path="time" /> --%>
					下单开始时间：
					<form:input path="startTimeStr" class="form-control form_datetime" style="width:110px; margin-bottom:0" readonly="true" />
					下单结束时间：
					<form:input path="endTimeStr" class="form-control form_datetime" style="width:110px; margin-bottom:0" readonly="true" />
					<input type="submit" value="搜索">
				</div>
			</form:form>

			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />

			<h4>提醒订单列表</h4>

			<!--  <button id="exportExcel" class="btn btn-success">导出Excel</button> -->

			<table class="table table-striped table-advance table-hover"
				id="table2excel">
				<thead>
					<tr>
						<th>用户手机号</th>
						<th>预约时间</th>
						<th>下单时间</th>
						<th>订单状态</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${oaRemindOrderListVoModel.list}" var="item">
						<tr>

							<td>${ item.mobile }</td>

							<td><timestampTag:timestamp patten="yyyy-MM-dd HH:mm"
									t="${item.serviceDate * 1000}" /></td>
							<td><timestampTag:timestamp patten="yyyy-MM-dd HH:mm"
									t="${item.addTime * 1000}" /></td>
							<td><c:if test="${ item.orderStatus == 12 }">
									<orderStatusTag:orderstatus orderStatus="${ item.orderStatus }" />
								</c:if> <c:if test="${ item.orderStatus == 10 }">
									<span style="color: red; font-weight: bold;"><orderStatusTag:orderstatus
											orderStatus="${ item.orderStatus }" /></span>
								</c:if> <c:if test="${ item.orderStatus == 11 }">
									<span style="color: green; font-weight: bold;"><orderStatusTag:orderstatus
											orderStatus="${ item.orderStatus }" /></span>
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>


			</section>

			<c:import url="../shared/paging.jsp">
				<c:param name="pageModelName" value="oaRemindOrderListVoModel" />
				<c:param name="urlAddress" value="/order/remind-order-list" />
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
	<!--script for this page-->
	<script type="text/javascript"
		src="<c:url value='/js/order/remindOrderList.js'/>"></script>
</body>
</html>
