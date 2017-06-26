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
			<form:form class="form-inline" name="from" id="searchForm" onsubmit="return checkEndTime()" modelAttribute="searchModel"
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
					服务人员姓名：
					<form:input path="staffName" id="staffName" class="form-control" />
				</div>
				
				<div class="form-group">
					订单号：
					<form:input path="orderNo" id="orderNo" class="form-control" />
				</div>
					<div class="form-group">
					支付方式：
					<form:select path="repaymentType" id="payType" class="form-control" >
						<form:option value="">--请选择支付方式--</form:option>
						<form:option value="1">支付宝支付</form:option>
						<form:option value="2">微信支付</form:option>
					</form:select>
				</div>
				<div class="form-group">
					还款时间：
					<form:input path="startTimeStr" id="startTimeStr" class="form-control form_datetime" style="width:110px; margin-bottom:0"
						readonly="true" />
					<span>至</span>
					<form:input path="endTimeStr" id="endTimeStr" class="form-control form_datetime" style="width:110px; margin-bottom:0"
					readonly="true" /> 
				</div>
				
				<input type="button" id="searchBtn" class="btn btn-primary" onclick="searchSubmit()" value="搜索"/>
				<input type="button" id="cleanBtn" class="btn btn-primary"  value="清空"/>
				<button type="button" id="exportPayDept" class="btn btn-warning" onclick="exportStaffPayDept()">导出还款明细</button>
				<br>
			</form:form> 
			</header>
			<div>
				<label>还款总金额：</label>
				<u style="color:red;">${totalPayDept }元</u>
			</div>
			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
			
			<table class="table table-striped table-advance table-hover" id="table2excel">
				<thead>
					<tr>
						<th>员工姓名</th>
						<th>员工手机号</th>
						<th>订单类型</th>
						<th>订单号</th>
						<th>还款金额</th>
						<th>支付方式</th>
						<th>订单状态</th>
						<th>还款时间</th>
						<th>支付账户</th>
						<th>交易号</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${contentModel.list}" var="item">
						<tr>
							<td>${ item.name }</td>
							<td>${ item.mobile }</td>
							<td>${ item.orderTypeName }</td>
							<td>${ item.orderNo }</td>
							<td>${ item.repaymentMoney }</td>
							<td>${item.payTypeName }</td>
							<td>${ item.orderStatusStr }</td>
							<td><timestampTag:timestamp patten="yyyy-MM-dd HH:MM:ss" t="${item.addTime * 1000}" /></td>
							<td>${item.payAccount }</td>
							<td>${item.trandId }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</section>
			<%@ include file="../shared/importJs.jsp"%>
			<c:import url="../shared/paging.jsp">
				<c:param name="pageModelName" value="contentModel" />
				<c:param name="urlAddress" value="/staff/staff-repayment" />
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
	<script type="text/javascript" src="<c:url value='/js/staff/staffRepaymentList.js'/>"></script>
</body>
</html>
