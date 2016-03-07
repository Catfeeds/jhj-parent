<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="citySelectTag" uri="/WEB-INF/tags/citySelect.tld"%>
<%@ taglib prefix="payTypeNameTag" uri="/WEB-INF/tags/payTypeName.tld"%>
<html>
<head>

<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>

<!--css for this page-->
<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>"
	type="text/css" />

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
			<section class="panel"> <header class="panel-heading">
			<h4>配送订单详情页</h4>
			</header>

			<hr
				style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="oaOrderListVoModel"
					class="form-horizontal" method="POST" action="disStaffByDelOrderNo"
					enctype="multipart/form-data">

					<form:hidden path="id" />
					<form:hidden path="userId" />
					<form:hidden path="orderNo" />
					<div class="form-body">
						<div class="form-group ">

							<label class="col-md-2 control-label">姓名</label>
							<div class="col-md-5">
								<form:input path="userName" class="form-control" maxLength="32"
									readonly="true" />
								<form:errors path="userName" class="field-has-error"></form:errors>
							</div>
						</div>

						<div class="form-group" id="nowStaff">

							<label class="col-md-2 control-label">当前阿姨</label>
							<div class="col-md-5">

								<form:input path="staffName" class="form-control" maxLength="32"
									readonly="true" />
								<form:errors path="staffName" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group">

							<label class="col-md-2 control-label">下单时间</label>
							<div class="col-md-5">

								<form:input path="orderDate" class="form-control" maxLength="32"
									readonly="true" />
								<form:errors path="orderDate" class="field-has-error"></form:errors>
							</div>
						</div>

						<div class="form-group">

							<label class="col-md-2 control-label">用户手机号</label>
							<div class="col-md-5">
								<form:input path="mobile" class="form-control" maxLength="32"
									readonly="true" />
								<form:errors path="mobile" class="field-has-error"></form:errors>
							</div>
						</div>

						<div class="form-group required">

							<label class="col-md-2 control-label">用户服务地址</label>
							<div class="col-md-5">
								<form:input path="orderAddress" class="form-control"
									maxLength="32" readonly="true" />
								<form:errors path="orderAddress" class="field-has-error"></form:errors>
							</div>
						</div>

						<div class="form-group">

							<label class="col-md-2 control-label">优惠券</label>
							<div class="col-md-5">
								<form:input path="couponValue" class="form-control"
									maxLength="32" readonly="true" />
								<form:errors path="couponValue" class="field-has-error"></form:errors>
							</div>
						</div>

						<div class="form-group">

							<label class="col-md-2 control-label">订单状态</label>
							<div class="col-md-5">
								<form:input path="orderStatusName" class="form-control"
									maxLength="32" readonly="true" />
								<form:errors path="orderStatusName" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group">

							<label class="col-md-2 control-label">总金额</label>
							<div class="col-md-5">
								<form:input path="orderMoney" class="form-control"
									maxLength="32" readonly="true" />
								<form:errors path="orderMoney" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group">

							<label class="col-md-2 control-label">支付金额</label>
							<div class="col-md-5">
								<form:input path="orderPay" class="form-control" maxLength="32"
									readonly="true" />
								<form:errors path="orderPay" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group">

							<label class="col-md-2 control-label">支付方式</label>
							<div class="col-md-5">

								<form:input path="payTypeName" class="form-control"
									maxLength="32" readonly="true" />
								<form:errors path="payTypeName" class="field-has-error"></form:errors>
							</div>
						</div>
						<hr
							style="width: 100%; color: black; height: 1px; background-color: black;" />
						<table class="table table-striped table-advance table-hover"
							id="allStaff">
							<thead>
								<tr>
									<th>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;姓名</th>
									<th>手机号</th>
									<th>距离用户距离</th>
									<th>距离用户时间</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${oaOrderListVoModel.staffList}" var="item">
									<tr>
										<td><form:radiobutton path="staffId"
												value=" ${ item.staffId }" /> &nbsp; &nbsp; ${ item.name }</td>
										<td>${ item.mobile }</td>
										<td>${ item.locName }</td>
										<td>${ item.distanceText }</td>
										<td>${ item.durationText }</td>

									</tr>
								</c:forEach>
							</tbody>
						</table>
						<div class="form-actions fluid">
							<div class="col-md-offset-6 col-md-6" style="margin-left: 315px">
								<button type="submit" id="viewForm" class="btn btn-success">确认派工</button>
							</div>
						</div>
				</form:form>
			</div>
			</section>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include
		file="../shared/pageFooter.jsp"%> <!--footer end-->
	</section>

	<!-- js placed at the end of the document so the pages load faster -->
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>


	<!--script for this page-->
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
	<script
		src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"
		type="text/javascript"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-fileupload/fileinput.min.js'/>"></script>

	<script type="text/javascript"
		src="<c:url value='/js/order/orderViewForm.js'/>"></script>

</body>
</html>
