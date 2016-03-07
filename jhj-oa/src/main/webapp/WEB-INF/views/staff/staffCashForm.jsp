<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="citySelectTag" uri="/WEB-INF/tags/citySelect.tld"%>
<html>
<head>

<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>

<!--css for this page-->
<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>"
	type="text/css" />

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
			用户管理 </header>

			<hr
				style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="contentModel" class="form-horizontal"
					method="POST"  id="staffCash-form">

					<form:hidden path="orderId" />
					<div class="form-body">
						<div class="form-group">

							<label class="col-md-2 control-label">订单号</label>
							<div class="col-md-5">
								<form:input path="orderNo" class="form-control" readonly="true" />
								<form:errors path="orderNo" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group">

							<label class="col-md-2 control-label">提现账号</label>
							<div class="col-md-5">
								<form:input path="account" class="form-control" readonly="true" />
								<form:errors path="account" class="field-has-error"></form:errors>
							</div>
						</div>

						<div class="form-group required">

							<label class="col-md-2 control-label">手机号</label>
							<div class="col-md-5">
								<form:input path="mobile" class="form-control" maxLength="32"
									readonly="true" />
								<form:errors path="mobile" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group">

							<label class="col-md-2 control-label">申请提现金额</label>
							<div class="col-md-5">
								<form:input path="orderMoney" class="form-control"
									maxLength="32" readonly="true" />
								<form:errors path="orderMoney" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">状态</label>
							<div class="col-md-8">
								<div class="row">
									<div class="col-md-2" align="right">

										<label class="radio"> <form:radiobutton
												path="orderStatus" value="0" />申请中
										</label>
									</div>

									<div class="col-md-2" align="left">
										<label class="radio"> <form:radiobutton
												path="orderStatus" value="1" />服务处理中
										</label>
									</div>

									<div class="col-md-2" align="left">
										<label class="radio"> <form:radiobutton
												path="orderStatus" value="2" />申请被驳回
										</label>
									</div>

									<div class="col-md-2" align="left">
										<label class="radio"> <form:radiobutton
												path="orderStatus" value="3" />已打款
										</label>
									</div>
								</div>
							</div>
						</div>
						<div class="form-group">
						<label class="col-md-2 control-label">备&nbsp;&nbsp;&nbsp;注*</label>
						<div class="col-md-5">
						
								<div class="textarea">
									<form:textarea class="form-control" path="remarks" placeholder="备注"/><br/>
								</div>
							</div>
						</div>

						<div class="form-actions fluid">
							<div class="col-md-offset-2 col-md-2">
								<c:if test="${contentModel.orderStatus != 3}">
								<button type="submit" id="sraffCashForm_btn"
									class="btn btn-success">保存</button>
								</c:if>
								<button type="button" id="return_btn" onclick="javascript:history.back();"
									class="btn btn-success">返回</button>
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

	<script
		src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"
		type="text/javascript"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-fileupload/fileinput.min.js'/>"></script>

	<script src="<c:url value='/js/jhj/staff/staffCashForm.js'/>"
		type="text/javascript"></script>

	<script src="<c:url value='/js/jhj/demo.js'/>"></script>

</body>
</html>
