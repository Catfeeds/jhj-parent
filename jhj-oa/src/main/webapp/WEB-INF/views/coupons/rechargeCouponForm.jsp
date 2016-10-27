<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html>

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
			优惠券 </header>

			<hr
				style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="dictCoupons" id="recharge-coupon-form"
					commandName="dictCoupons" 
					class="form-horizontal">
					<form:hidden path="id" id="form1-id"/>
					<div class="form-body">
						

						<div class="form-group">

							<!-- Text input-->
							<label class="col-md-2 control-label">优惠券金额</label>
							<div class="col-md-5">
								<form:input path="value" class="form-control"
									placeholder="优惠券金额" value="${dictCoupons.value}" maxSize="10" />
								<form:errors path="value" class="field-has-error"></form:errors>

							</div>
						</div>
						<div class="form-group">

							<!-- Text input-->
							<label class="col-md-2 control-label">订单满金额</label>
							<div class="col-md-5">
								<form:input path="maxValue" class="form-control"
									placeholder="订单满金额" value="${dictCoupons.maxValue}" maxSize="10" />
								<form:errors path="value" class="field-has-error"></form:errors>
							</div>
						</div>

						<div class="form-group">
							<label class="col-md-2 control-label">服务类型 </label>

							<div class="col-md-5">
								<form:radiobuttons path="serviceType" items="${serviceTypeMap}"/><br/>
							</div>

						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">优惠券类型 </label>

							<div class="col-md-5">
								<%-- <form:radiobuttons path="useCondition" items="${couponsUseCondition}"/> --%>
								<form:select path="couponsTypeId" items="${couponsType }" itemLabel="couponsTypeDesc" itemValue="couponsTypeId">
								</form:select>
								<br />
							</div>
						</div>
						<div class="form-group">

							<!-- Text input-->
							<label class="col-md-2 control-label">描&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述*</label>
							<div class="col-md-5">
								<form:input path="introduction" name="introduction" 
									class="form-control" placeholder="请输入描述" />
								<p class="help-block"></p>
							</div>
						</div>
						<div class="form-group">

							<!-- Textarea -->
							<label class="col-md-2 control-label">详&nbsp;细&nbsp;说&nbsp;明*</label>
							<div class="col-md-5">
								<div class="textarea">
								<%-- 	<textarea type="textarea" name="description"
										class="form-control">${dictCoupons.description}</textarea> --%>
									<form:textarea class="form-control" path="description" placeholder="一句话描述"/><br/>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">日&nbsp;期&nbsp;范&nbsp;围</label>
							<div class="col-md-5">
								<form:select path="rangMonth" class="form-control">
									<form:options items="${selectDataSource}" />
								</form:select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">是否有效</label>
							<div class="col-md-5">
								<form:radiobutton path="isValid" value="1"/>是
								<form:radiobutton path="isValid" value="0"/>否
							</div>
						</div>
						
					</div>
					<div class="form-actions">
						<div class="row">
							<div class="col-md-4" align="right">
								<button class="btn btn-success" id="addCoupon_btn" type="button">
									保存</button>
							</div>
							<!-- Button -->
							<div class="col-md-1">
								<button class="btn btn-success" type="reset">重置</button>
							</div>
						</div>
					</div>
					<!-- </fieldset> -->
				</form:form>
			</div>
			</section>
		</div>
		<c:if test="${isForm!=0 and dictCoupons.isValid == '1'}">
			<div id="form2-div">
				<div class="col-lg-12">
					<form id="form2" method="post" >
						<input type="hidden" id="form2_id" name="id" value="${dictCoupons.id }"/>
						<input type="checkbox" name="sendCouponsCondtion" value="0" />注册未使用的用户
						<input type="checkbox" name="sendCouponsCondtion" value="1" />1个月内使用的用户
						<input type="checkbox" name="sendCouponsCondtion" value="2" />3个月内使用的用户
						<input type="checkbox" name="sendCouponsCondtion" value="3" />3个月以上使用的用户
						<input type="button" id="from2-btn" value="发送优惠券" />
					</form>
				</div>
			</div>
		</c:if>
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
	<!-- rechargeCoupon Form表单 js -->
	<script src="../js/jhj/bs/dictCoupons/rechargeCouponForm.js"
		type="text/javascript"></script>
	
</body>
</html>
