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
			黑名单管理 </header>

			<hr
				style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="contentModel" class="form-horizontal"
					method="POST" action="staffBlackForm" id="staffBlack-form">

					<form:hidden path="Id" />
					<div class="form-body">
						<div class="form-group">

							<label class="col-md-2 control-label">服务人员手机号</label>
							<div class="col-md-5">
								<form:input path="mobile" class="form-control"/>
								<form:errors path="mobile" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">类型</label>
							<div class="col-md-8">
								<div class="row">
									<div class="col-md-2" align="right">
										<label class="radio"> <form:radiobutton
												path="blackType" value="0" />系统判断
										</label>
									</div>
									<div class="col-md-2" align="left">
										<label class="radio"> <form:radiobutton
												path="blackType" value="1" />人工加入
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
							<div class="col-md-offset-6 col-md-6">
								<button type="submit" id="sraffBlackForm_btn"
									class="btn btn-success">保存</button>
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

	<script src="<c:url value='/js/jhj/staff/staffCashForm.js'/>"
		type="text/javascript"></script>

	<script src="<c:url value='/js/jhj/demo.js'/>"></script>

</body>
</html>
