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
			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
			<div class="panel-body">
				<form:form modelAttribute="dictCoupons" id="recharge-coupon-form"
					commandName="dictCoupons" 
					class="form-horizontal">
					<form:hidden path="id" id="form1-id"/>
					<div class="form-body">
						<div class="form-group">
							<!-- Text input-->
							<label class="col-md-2 control-label">优惠券金额*</label>
							<div class="col-md-5">
								<form:input path="value" class="form-control"
									placeholder="优惠券金额" value="${dictCoupons.value}" maxSize="10" />
								<form:errors path="value" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">满减金额</label>
							<div class="col-md-5">
								<form:input path="maxValue" class="form-control"
									placeholder="满减金额" value="${dictCoupons.maxValue}" maxSize="10" />
								<form:errors path="maxValue" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">服务类型* </label>
							<div class="col-md-5">
								<%-- <form:select path="serviceType" cssClass="form-control" items="${serviceTypeList}" itemLabel="name" itemValue="serviceTypeId"/> --%>
								<form:select path="serviceType" cssClass="form-control">
									<option value="">--请选择服务类型--</option>
									<c:forEach items="${serviceTypeList}" var="serviceType">
										<form:option value="${serviceType.serviceTypeId }">${serviceType.name }</form:option>
									</c:forEach>
									<option value="0">全部</option> 
								</form:select>
							</div>
						</div>
						<%-- <div class="form-group">
							<!-- Text input-->
							<label class="col-md-2 control-label">描&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述*</label>
							<div class="col-md-5">
								<form:input path="introduction" name="introduction" 
									class="form-control" placeholder="请输入描述" />
								<p class="help-block"></p>
							</div>
						</div> --%>
						<%-- <div class="form-group">
							<!-- Textarea -->
							<label class="col-md-2 control-label">详&nbsp;细&nbsp;说&nbsp;明*</label>
							<div class="col-md-5">
								<div class="textarea">
									<form:textarea class="form-control" path="description" placeholder="一句话描述"/><br/>
								</div>
							</div>
						</div> --%>
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">日&nbsp;期&nbsp;范&nbsp;围*</label>
							<div class="col-md-5">
								<form:select path="rangMonth" class="form-control">
									<form:option value="0">--请选择日期范围--</form:option>
									<form:options items="${selectDataSource}" />
								</form:select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">有效日期*</label>
							<div class="col-md-5">
								<fmt:formatDate var='formattedDate1' value='${dictCoupons.fromDate}' type='both' pattern="yyyy-MM-dd" />
								<form:input id="fromDate" path="fromDate"  class="form-control select-time" value="${formattedDate1 }" readonly="true"/>
							</div>
							
						</div>
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label"></label>
							<div class="col-md-5">
								<fmt:formatDate var='formattedDate2' value='${dictCoupons.toDate}' type='both' pattern="yyyy-MM-dd" />
								<form:input id="toDate" path="toDate" class="form-control select-time" value="${formattedDate2 }" readonly="true"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">是否有效*</label>
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
		<c:if test="${dictCoupons.id>0}">
			<div id="form2-div">
				<div class="col-lg-12">
					<form id="form2" method="post" >
						<input type="hidden" id="form2_id" name="id" value="${dictCoupons.id }"/>
						<input type="checkbox" id="isAll" name="sendCouponsCondtion" value="0" onclick='selectOne()'/>全部
						<input type="checkbox" class='isVip' name="sendCouponsCondtion" value="1" />会员用户
						<input type="checkbox" class='isVip' name="sendCouponsCondtion" value="2" />非会员用户
						<input type="checkbox" class="other" name="sendCouponsCondtion" value="3" />注册未使用的用户
						<input type="checkbox" class="other" name="sendCouponsCondtion" value="4" />1个月内使用的用户
						<input type="checkbox" class="other" name="sendCouponsCondtion" value="5" />3个月内使用的用户
						<input type="checkbox" class="other" name="sendCouponsCondtion" value="6" />6个月内使用的用户
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

	<script type="text/javascript" src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
	<!--script for this page-->
	<script src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"
		type="text/javascript"></script>
	<!-- rechargeCoupon Form表单 js -->
	<script src="../js/jhj/bs/dictCoupons/rechargeCouponForm.js"
		type="text/javascript"></script>
	
</body>
</html>
