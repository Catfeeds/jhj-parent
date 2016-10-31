<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html>
<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="citySelectTag" uri="/WEB-INF/tags/citySelect.tld"%>
<%@ taglib prefix="payTypeNameTag" uri="/WEB-INF/tags/payTypeName.tld"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
<%@ taglib prefix="cloudOrgSelect" uri="/WEB-INF/tags/CloudOrgSelect.tld"%>
<html>
<head>
<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>
<!--css for this page-->
<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>" type="text/css" />
<link href="<c:url value='/assets/bootstrap-datetimepicker/css/datetimepicker.css'/>" rel="stylesheet" type="text/css" />
<style>
.tangram-suggestion-main {
	z-index: 1060;
}
</style>
</head>
<body>
	<section id="container">
		<!--header start-->
		<%@ include file="../shared/pageHeader.jsp"%>
		<!--header end-->
		<!--sidebar start-->
		<%@ include file="../shared/sidebarMenu.jsp"%>
		<!--sidebar end-->
		<!--main content start-->
		<section id="main-content">
			<section class="wrapper">
				<!-- page start-->
				<div class="row">
					<div class="col-lg-12">
						<section class="panel">
							<header class="panel-heading">
								<h4>基础保洁订单</h4>
							</header>
							<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
							<div class="panel-body">
								<form:form class="form-horizontal" method="POST" name="form" id="orderHourForm">
									<input type="hidden" id="userId" name="userId" />
									<input type="hidden" id="orderType" name="orderType" value="0" />
									<input type="hidden" id="orderForm" name="orderFrom" value="2">
									<div class="form-body">
										<div class="form-group">
											<label class="col-md-2 control-label">
												<font color="red">*</font>用户手机号
											</label>
											<div class="col-md-5">
												<input type="text" name="mobile" id="mobile" class="form-control" onblur="getAddrByMobile()" />
											</div>
										</div>
										<div class="form-group required">
											<label class="col-md-2 control-label">
												<font color="red">*</font>服务地址
											</label>
											<div class="col-md-5">
												<select id="addrId" name="addrId" class="form-control">
													<option value="">--请选择服务地址--</option>
												</select>
											</div>
											<div>
												<button class="btn btn-primary" data-toggle="modal" data-target="#exampleModal" data-whatever=""
													onclick="address()">添加地址</button>
											</div>
											<div id="from-add-addr" style="display: none">
												<%@include file="address.jsp"%>
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label">
												<font color="red">*</font>服务类型
											</label>
											<div class="col-md-5">
												<%-- <input type="hidden" id="serviceType" name="serviceType" class="form-control"
													value="${serviceType.serviceTypeId }" />
												<input class="form-control" value="${serviceType.name }" readonly="readonly"> --%>
												<select id="serviceType" name="serviceType" class="form-control" onchange="chagePrice()">
													<option value="">--请选择服务类型--</option>
													<option value="${hour.serviceTypeId }">${hour.name  }</option>
													<option value="${cook.serviceTypeId }">${cook.name  }</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label">
												<font color="red">*</font>价格
											</label>
											<div class="col-md-5">
												<input type="text" id="orderPay" name="orderPay" class="form-control" value="">
												<input type="hidden" id="hour-price" value="${hour.price }"/>
												<input type="hidden" id='cook-price' value="${cook.price }"/>
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label">
												<font color="red">*</font>订单来源
											</label>
											<div class="col-md-5">
												<select id="orderOpFrom" name="orderOpFrom" class="form-control">
													<option value="">--请选择订单来源--</option>
													<option value="1">来电订单</option>
													<c:forEach items="${cooperativeBusiness }" var="src">
														<option value="${src.id }">${src.businessName }</option>
													</c:forEach>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label">
												<font color="red">*</font>服务时间
											</label>
											<div class="col-md-5">
												<input type="text" id="serviceDate" name="serviceDate"  class="form-control form_datetime" readonly="readonly" />
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label">
												<font color="red">*</font>服务时长
											</label>
											<div class="col-md-5">
												<input type="text" id="serviceHour" name="serviceHour" class="form-control" value="2" />
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label">
												<font color="red">*</font>支付方式
											</label>
											<div class="col-md-5">
												<select id="orderPayType" name="orderPayType" class="form-control">
													<option value="">--请选择支付方式--</option>
													<option value="6">现金支付</option>
													<option value="7">平台已支付</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label">用户备注:</label>
											<div class="col-md-5">
												<textarea id="remarks" name="remarks" rows="5" cols="50" class="form-control"></textarea>
											</div>
										</div>
										<div class="form-actions fluid">
											<div class="col-md-offset-3 col-md-3">
												<button type="button" class="btn btn-success" id="submitForm" onclick="saveFrom()">保存</button>
											</div>
										</div>
									</div>
								</form:form>
							</div>
						</section>
					</div>
				</div>
				<!-- page end-->
			</section>
		</section>
		<!--main content end-->
		<!--footer start-->
		<%@ include file="../shared/pageFooter.jsp"%>
		<!--footer end-->
	</section>
	<!-- js placed at the end of the document so the pages load faster -->
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=2sshjv8D4AOoOzozoutVb6WT"></script>
	<!--script for this page-->
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js'/>"></script>
	<script src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>" type="text/javascript"></script>
	<script type="text/javascript" src="<c:url value='/assets/bootstrap-fileupload/fileinput.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/validate-methods.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/order/orderHourAdd.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/baidu-map.js'/>"></script>
</body>
</html>
