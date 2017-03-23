<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html>
<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="payTypeNameTag" uri="/WEB-INF/tags/payTypeName.tld"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
<%@ taglib prefix="cloudOrgSelect" uri="/WEB-INF/tags/CloudOrgSelect.tld"%>
<%@ taglib prefix="parentServiceTypeSelectTag" uri="/WEB-INF/tags/parentServiceTypeSelect.tld"%>
<html>
<head>
<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>
<!--css for this page-->
<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>" type="text/css" />
<link href="<c:url value='/assets/bootstrap-datetimepicker/css/datetimepicker.css'/>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href='<c:url value='/css/order-calendar.css'/>' type="text/css" />
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
								<h4>统一下单</h4>
							</header>
							<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
							<div class="panel-body">
								<form:form class="form-horizontal" method="POST" name="form" id="orderForm">
									<input type="hidden" id="userId" name="userId" />
									<input type="hidden" id="orderType" name="orderType" value="0" />
									<input type="hidden" id="orderForm" name="orderFrom" value="2">
									<input type="hidden" id="isVip" name="isVip" value="0">
									<input type="hidden" id="price" name="price" " value="50">
									<input type="hidden" id="mprice" name="mprice" " value="45">
									<input type="hidden" id="pprice" name="pprice" " value="149">
									<input type="hidden" id="mpprice" name="pprice" " value="135">
									<input type="hidden" id="maxServiceHour" name="maxServiceHour" " value="6">
									<input type="hidden" id="minServiceHour" name="minServiceHour" " value="3">
									<input type="hidden" id="user_id" value="${accountAuth.id }" />
									<input type="hidden" id="username" value="${accountAuth.username }" />
									<input type="hidden" id="serviceAddonDatas" name="serviceAddonDatas" value="" />
									<div class="form-body">
										<div class="form-group">
											<label class="col-md-2 control-label">
												<font color="red">*</font>用户手机号
											</label>
											<div class="col-md-5">
												<input type="text" name="mobile" id="mobile" class="form-control" onblur="getAddrByMobile()" />
											</div>
											<div>
												<label class="control-label" id="userTypeStr"></label>
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
												<font color="red">*</font>服务大类
											</label>
											<div class="col-md-5">
												<parentServiceTypeSelectTag:select />
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label">
												<font color="red">*</font>服务类型
											</label>
											<div class="col-md-5">
												<select name="serviceType" id="serviceType" class="form-control" onchange="serviceTypeChange()">
												</select>
											</div>
										</div>
										<div class="form-group" id="divServiceAddons" style="display:none">
											<label class="col-md-2 control-label">
												<font color="red">*</font>服务子项
											</label>
											<div class="col-md-8">
												<table id="serviceAddonTable" border='1' class="table table-bordered">
													<thead>
														<tr>
															<td>类别</td>
															<td>折扣价</td>
															<td>时长参考</td>
															<td>数量</td>
														</tr>
													</thead>
													<tbody id="service-content"></tbody>
												</table>
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
											<div class="col-md-5" onclick="selectServiceDateTime()">
												<input type="text" id="serviceDate" name="serviceDate" class="form-control form_datetime"
													readonly="readonly" data-toggle="modal" data-target="" />
												<!-- <input type="text" id="serviceDate" name="serviceDate"  class="form-control form_datetime" readonly="readonly" /> -->
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label">
												<font color="red">*</font>服务时长
											</label>
											<div class="col-md-5">
												<input type="text" id="serviceHour" name="serviceHour" onkeyup="changePrice()" onafterpaste="changePrice()"
													class="form-control" value="3" />
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label">
												<font color="red">*</font>服务人员数量
											</label>
											<div class="col-md-5">
												<input type="text" id="staffNums" name="staffNums" onkeyup="changePrice()" onafterpaste="changePrice()"
													class="form-control" value="1" />
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label">
												<font color="red">*</font>价格
											</label>
											<div class="col-md-5">
												<input type="text" id="orderPay" name="orderPay" class="form-control" value="" oninput="setValue()" />
												<input type="hidden" id="hour-price" value="${hour.price }" />
												<input type="hidden" id='cook-price' value="${cook.price }" />
											</div>
										</div>
										<div class="form-group">
											<label class="col-md-2 control-label"> 优惠券 </label>
											<div class="col-md-5">
												<select name="couponsId" id="couponsId" class="form-control" onchange="selectCoupons()">
													<option value="">0元</option>
													<option value="4176">3元</option>
													<option value="4177">5元</option>
													<option value="4178">10元</option>
													<option value="4179">15元</option>
													<option value="4180">20元</option>
													<option value="4181">30元</option>
													<option value="4182">50元</option>
													<option value="4183">100元</option>
												</select>
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
												<textarea id="remarks" name="remarks" rows="5" maxlength='200' cols="50" class="form-control"></textarea>
											</div>
										</div>
										<div class="form-actions fluid">
											<div class="col-md-offset-3 col-md-3">
												<input type="button" class="btn btn-success" onclick="saveFrom()" value="保存" />
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
		<!-- 日期选择 -->
		<div class="modal fade" id="orderCalendar" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
			aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="rili">
						<div class="rili1">
							<div class="rili1-1" id="show-year">2016</div>
							<ul class="rili1-2">
								<li id="substranc-day">
									<span>&lt;</span>
								</li>
								<li id="show-month">九月</li>
								<li id="add-day">
									<span>&gt;</span>
								</li>
							</ul>
							<ul class="rili1-3" id="show-week">
							</ul>
						</div>
						<ul class="rili1-4" id="show-day">
						</ul>
						<!-- 填充时间 -->
						<ul class="rili1-5" id="show-dateTime">
						</ul>
						<div class="rili1-6">
							<a href="#">
								<p class="rili1-6-2" id="checkDate" data-dismiss="">确定</p>
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
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
	<script src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>" type="text/javascript"></script>
	<script type="text/javascript" src="<c:url value='/assets/bootstrap-fileupload/fileinput.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/validate-methods.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jhj/select-servicetype.js'/>"></script
	<script type="text/javascript" src="<c:url value='/js/baidu-map.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/order/order-calendar.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/order/orderAdd.js'/>"></script>

</body>
</html>
