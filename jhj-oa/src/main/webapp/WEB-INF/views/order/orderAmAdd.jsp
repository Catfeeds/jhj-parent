<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
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
								<h4>深度服务订单</h4>
							</header>
							<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
							<div class="panel-body">
								<form:form class="form-horizontal" method="POST" name="form">
									<input id="from-user-id" name="userId" type="hidden"/>
									<div class="form-body">
										<div class="form-group">
											<label class="col-md-2 control-label"><font
												color="red">*</font>用户手机号</label>
											<div class="col-md-5">
												<input name="mobile" id="from-mobile" class="form-control" onblur="getAddrByMobile()" />
											</div>
										</div>
										<div class="form-group required">
											<label class="col-md-2 control-label"><font
												color="red">*</font>服务地址</label>
											<div class="col-md-5">
												<select name="addrId" class="form-control" id="from-addr" onclick="address()">
													<option value="">--请选择服务地址--</option>
												</select>
											</div>
											<div id="from-add-addr" style="display:none">
												<button class="btn btn-primary" data-toggle="modal" data-target="#exampleModal" data-whatever="">添加地址</button>
												<%@include file="address.jsp"%>
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label"><font
												color="red">*</font>服务类型</label>
											<div class="col-md-5">
												<select name="serviceType" class="form-control" onchange="serviceTypeChange(this)">
													<option value="">--请选择服务类型--</option>
													<c:forEach items="${serviceType }" var="service">
														<option value="${service.serviceTypeId }">${service.name }</option>
													</c:forEach>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label"><font
												color="red">*</font>服务子项</label>
											<div class="col-md-5">
												类别：<input type="checkbox" name="">
												数量：<input name=""/>
												原价：
												折扣价：
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label"><font
												color="red">*</font>订单来源</label>
											<div class="col-md-5">
												<select name="orderFrom" id="from-src" class="form-control">
													<option value="">--请选择订单来源--</option>
													<c:forEach items="${cooperativeBusiness }" var="src">
														<option value="${src.id }">${src.businessName }</option>
													</c:forEach>
												</select>
											</div>
										</div>
										<div class="form-group" id="disWayOne">
											<label class="col-md-2 control-label"><font
												color="red">*</font>服务时间</label>
											<div class="col-md-5">
												<input name="serviceDate" id="from-servicedate" class="form-control form_datetime" readonly="readonly"/>
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label"><font
												color="red">*</font>支付方式</label>
											<div class="col-md-5">
												<select id="f-paywawy" name="payway" class="form-control">
													<option value="">--请选择支付方式--</option>
													<option value="6">现金支付</option>
													<option value="4">已支付</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label">用户备注:</label>
											<div class="col-md-5">
												<textarea id="ft-eara" name="remarks" rows="5" cols="50"
													class="form-control"></textarea>
											</div>
										</div>
										<div class="form-actions fluid">
											<div class="col-md-offset-3 col-md-3">
												<button type="button" class="btn btn-success"
													id="submitForm" onclick="saveFrom()">保存</button>
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
	<script type="text/javascript" src="<c:url value='/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/assets/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js'/>"></script>
	<script src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>" type="text/javascript"></script>
	<script type="text/javascript" src="<c:url value='/assets/bootstrap-fileupload/fileinput.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/order/orderAmAdd.js'/>"></script>
	
</body>
</html>
