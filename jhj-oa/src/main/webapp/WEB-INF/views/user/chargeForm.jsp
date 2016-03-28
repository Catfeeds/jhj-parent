<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<html>
<head>

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
			<section class="panel"> <header class="panel-heading">
			会员-充值</header>

			<hr
				style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="userChargeVo" id="charge-form"
					commandName="userChargeVo" action="charge-form"
					class="form-horizontal" method="POST">
					<form:hidden path="userId" />
						<div class="form-body">
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">充&nbsp;值&nbsp;方&nbsp;式</label>
							<div class="col-md-5">
								<form:select path="chargeWay" class="form-control select1">
									<form:options items="${chargeWayDataSource}" />
								</form:select>
								<form:errors path="chargeWay" class="field-has-error"></form:errors>
							</div>
						</div>
					</div>
					<div class="form-body" id="fixed">
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">固&nbsp;定&nbsp;金&nbsp;额</label>
							<div class="col-md-5">
								<form:select path="dictCardId" class="form-control">
									<form:options items="${selectDataSource}" />
								</form:select>
								<form:errors path="dictCardId" class="field-has-error"></form:errors>
							</div>
						</div>
					</div>
					<div class="form-body" id="any">
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">任&nbsp;意&nbsp;金&nbsp;额</label>
							<div class="col-md-5">
								<form:input path="chargeMoney" class="form-control" 
									placeholder="请输入充值金额"  maxSize="6" />
								<form:errors path="chargeMoney" class="field-has-error"></form:errors>
							</div>
						</div>
					</div>
					
					<div class="form-body" >
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">用户手机号</label>
							<div class="col-md-5">
								<form:input path="userMobile" class="form-control" readonly="true"/>
							</div>
						</div>
					</div>
					
					
					<div class="form-body">
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">验证码</label>
							<div class="col-md-5">
								<input type="text" class="col-lg-3"  style="width:200px"
									placeholder="请输入验证码"	 id="userCode" value="" maxlength="4" >
							</div>
							<button type="button" id="getUserCode" style="margin-left:-280px" class="btn btn-success">获取验证码</button>
							<font color="red">验证码30分钟内有效</font>
						</div>
					</div>
					
					<div class="form-actions">
						<div class="row">
							<div class="col-md-4" align="right">
								<button class="btn btn-success" id="addCoupon_btn" type="button">
									保存</button>
							</div>
						</div>
					</div>
					<!-- </fieldset> -->
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
	<script src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"
		type="text/javascript"></script>
	<!-- 会员充值Form表单 js -->
	<script type="text/javascript" src="<c:url value='/js/jhj/user/chargeForm.js'/>"></script>
</body>
</html>
