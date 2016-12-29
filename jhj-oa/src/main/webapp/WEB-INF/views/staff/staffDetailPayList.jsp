<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.jhj.oa.common.UrlHelper"%>
<%@ page import="com.jhj.common.Constants"%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../shared/taglib.jsp"%>
<!-- taglib for this page -->
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
<html>
<head>
<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>
<link href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>" rel="stylesheet"
	type="text/css" />
<!--css for this page-->
</head>
<body>
	<section id="container"> <!--header start--> <%@ include file="../shared/pageHeader.jsp"%>
	<!--header end--> <!--sidebar start--> <%@ include file="../shared/sidebarMenu.jsp"%> <!--sidebar end-->
	<!--main content start--> <section id="main-content"> <section class="wrapper"> <!-- page start-->
	<div class="row">
		<div class="col-lg-12">
			<section class="panel"> <header class="panel-heading">
			<h4>服务人员财务明细列表</h4>
			<form:form class="form-inline" id="searchForm" onsubmit="return checkEndTime()" modelAttribute="searchModel"
				action="staffPay-list" method="GET">
				
				<td>门店:</td>
				<td><orgSelectTag:select selectId="${accountAuth.parentOrgId}" sessionOrgId="${accountAuth.parentOrgId}" /></td>
				<td>云店:</td>
				<td><select name="orgId" id="orgId" class="form-control">
						<option value="0">全部</option>
					</select></td>
				<td>姓名:</td>
				<td>
					<input type="hidden" id="staffSelectedId" value="${searchModel.staffId }"/>
					<select name="selectStaff" id="selectStaff" class="form-control">
						<option value="0">全部</option>
					</select>
				</td>
				<div class="form-group">
					开始时间：
					<form:input path="startTimeStr" class="form-control form_datetime" style="width:110px; margin-bottom:0"
						readonly="true" />
				</div>
				<div class="form-group">
					结束时间：
					<form:input path="endTimeStr" class="form-control form_datetime" style="width:110px; margin-bottom:0"
						readonly="true" />
				</div>
				
				<button type="submit" class="btn btn-primary">搜索</button>
				
				<input type="button" id="exportOrder" class="btn btn-warning" onclick="exportStaffOrder()" value="导出财务明细"/>
			</form:form> 
			</header>
			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />

			<table class="table table-striped table-advance table-hover">
				<thead>
					<tr>
						<!-- <td>订单总金额（元）</td> -->
						<td>订单支付金额（元）</td>
						<!-- <td>使用优惠劵金额（元）</td> -->
						<td>订单收入（元）</td>
						<td>余额支付（元）</td>
						<td>支付宝（元）</td>
						<td>微信（元）</td>
						<td>现金支付（元）</td>
						<td>平台已支付（元）</td>
						<td>还款金额</td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<%-- <td>${totalOrderMoney }</td> --%>
						<td>${totalOrderPay }</td>
						<%-- <td>${totalOrderCoupon }</td> --%>
						<td>${totalOrderIncoming }</td>
						<td>${totalOrderPayType0 }</td>
						<td>${totalOrderPayType1 }</td>
						<td>${totalOrderPayType2 }</td>
						<td>${totalOrderPayType6 }</td>
						<td>${totalOrderPayType7 }</td>
						<td>${totalPayDept }</td>
					</tr>
				</tbody>
			</table>
			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
			
			<table class="table table-striped table-advance table-hover" id="table2excel">
				<thead>
					<tr>
						<th>员工姓名</th>
						<th>员工手机号</th>
						<th>订单类型</th>
						<th>订单号</th>
						<th>客户手机号</th>
						<th>服务地址</th>
						<th>订单金额</th>
						<th>订单收入</th>
						<th>支付方式</th>
						<th>订单状态</th>
						<th>添加时间</th>
						<th>备注</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${contentModel.list}" var="item">
						<tr>
							<td style="width: 8%">${ item.name }</td>
							<td style="width: 10%">${ item.mobile }</td>
							<td style="width: 8%">${ item.orderTypeName }</td>
							<c:choose>
								<c:when test="${item.orderListLink != '' }">
									<td style="width: 8%"><a href="${item.orderListLink}" target="_blank">${ item.orderNo }</a></td>
								</c:when>
								
								<c:otherwise>
									<td style="width: 8%">${ item.orderNo }</td>
								</c:otherwise>
							</c:choose>
							
							<td style="width: 8%">${ item.userMobile }</td>
							<td style="width: 15%">${ item.addr }</td>
							<td style="width: 6%">${ item.orderMoney }</td>
							<td style="width: 6%">${ item.orderPay }</td>
							<td style="width: 12%">${item.payTypeName }</td>
							<td style="width: 6%">${ item.orderStatusStr }</td>
							<td style="width: 8%"><timestampTag:timestamp patten="yyyy-MM-dd" t="${item.addTime * 1000}" /></td>
							<td style="width: 25%">${ item.remarks }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</section>
			<%@ include file="../shared/importJs.jsp"%>
			<c:import url="../shared/paging.jsp">
				<c:param name="pageModelName" value="contentModel" />
				<c:param name="urlAddress" value="/staff/staffPay-list" />
			</c:import>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include file="../shared/pageFooter.jsp"%>
	<!--footer end--> </section>
	<!-- js placed at the end of the document so the pages load faster -->
	<!--common script for all pages-->
	
	<%-- <script type="text/javascript" src="<c:url value='/assets/jquery.table2excel.js'/>"></script> --%>
	<!--script for this page-->
	<script type="text/javascript" src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jhj/select-org-cloud.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jhj/select-staff.js'/>"></script>
	
	<script type="text/javascript" src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/staff/staffDetailPayList.js'/>"></script>
</body>
</html>
