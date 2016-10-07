<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="citySelectTag" uri="/WEB-INF/tags/citySelect.tld"%>
<%@ taglib prefix="payTypeNameTag" uri="/WEB-INF/tags/payTypeName.tld"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
<%@ taglib prefix="cloudOrgSelect" uri="/WEB-INF/tags/CloudOrgSelect.tld"%>
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
<html>
<head>
<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>
<!--css for this page-->
<link href="<c:url value='/assets/bootstrap-datetimepicker/css/datetimepicker.css'/>" rel="stylesheet" type="text/css" />
<link href="<c:url value='/assets/bootstrap-tagsinput/bootstrap-tagsinput.css'/>" rel="stylesheet"  />
<style>
.bootstrap-tagsinput input {
  border: none;
  box-shadow: none;
}

.bootstrap-tagsinput .label {
  font-size: 100%;
}

</style>
</head>
<body>
	<section id="container"> <!--header start--> <%@ include file="../shared/pageHeader.jsp"%>
	<!--header end--> <!--sidebar start--> <%@ include file="../shared/sidebarMenu.jsp"%> <!--sidebar end-->
	<!--main content start--> <section id="main-content"> <section class="wrapper"> <!-- page start-->
	<div class="row">
		<div class="col-lg-12">
			<section class="panel">
			<div class="panel-body">
				<form:form modelAttribute="oaOrderListVoModel" class="form-horizontal" method="POST" action="updateStaffByOrderNo"
					enctype="multipart/form-data">
					<form:hidden path="id" />
					<form:hidden path="userId" />
					<form:hidden path="orderNo" />
					<form:hidden path="orderStatus" />
					<form:hidden path="staffId" />
					<form:hidden path="isAuto"/>
					<form:hidden path="isMulti"/>
					<div class="form-body">
					<section class="panel"> <header class="panel-info"> <h4>订单基本信息</h4> </header>
						<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
						<div class="form-group ">
							<div class="col-md-5">
								<c:if test="${oaOrderListVoModel.orderStatus != 2 && oaOrderListVoModel.orderStatus !=3}">
									<font color="red">只有 订单状态为 已支付 或 已派工,才可以进行 派工操作 </font>
								</c:if>
							</div>
						</div>
						<div class="form-group ">
							<label class="col-md-2 control-label">姓名</label>
							<div class="col-md-5">
								<form:input path="userName" class="form-control" maxLength="32" readonly="true" />
								<form:errors path="userName" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">用户手机号</label>
							<div class="col-md-5">
								<form:input path="mobile" class="form-control" maxLength="32" readonly="true" />
								<form:errors path="mobile" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">服务类型</label>
							<div class="col-md-5">
								<form:input path="serviceTypeName" class="form-control" maxLength="32" readonly="true" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">服务子项</label>
							<div class="col-md-5">
								<table id="serviceAddonTable" border='1' class="table table-bordered">
									<thead>
										<tr>
											<td>类别</td>
											<td>折扣价</td>
											<td>时长参考</td>
											<td>数量</td>
										</tr>
									</thead>
									<tbody id="service-content">
									<c:forEach items="${orderAddonVos}" var="item">
										<tr>
											<td>${item.serviceAddonName }</td>
											<td>${item.price } ${item.itemUnit }</td>
											<td>${item.serviceHour }</td>
											<td>${item.itemNum }</td>
										</tr>
									</c:forEach>
									
									</tbody>
								</table>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">服务开始时间</label>
							<div class="col-md-5">
								<input type="text" readonly="true" value="${oaOrderListVoModel.serviceDateStr }" class="form-control"/>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">服务时长</label>
							<div class="col-md-5">
								<form:input path="serviceHour" class="form-control" maxLength="32" readonly="true" />
							</div>
						</div>
						
						
						
						
						<div class="form-group">
							<label class="col-md-2 control-label">订单状态</label>
							<div class="col-md-5">
								<form:input path="orderStatusName" class="form-control" maxLength="32" readonly="true" />
								<form:errors path="orderStatusName" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">总金额</label>
							<div class="col-md-5">
								<form:input path="orderMoney" class="form-control" maxLength="32" readonly="true" />
								<form:errors path="orderMoney" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">支付金额</label>
							<div class="col-md-5">
								<form:input path="orderPay" class="form-control" maxLength="32" readonly="true" />
								<form:errors path="orderPay" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">订单来源</label>
							<div class="col-md-5">
								<form:input path="orderOpFromName" class="form-control" readonly="true" />
								<form:errors path="orderOpFromName" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">支付方式</label>
							<div class="col-md-5">
								<form:input path="payTypeName" class="form-control" maxLength="32" readonly="true" />
								<form:errors path="payTypeName" class="field-has-error"></form:errors>
							</div>
						</div>
						<c:if test="${ oaOrderListVoModel.couponValue > 0 }">
						<div class="form-group">
							<label class="col-md-2 control-label">优惠券</label>
							<div class="col-md-5">
								<form:input path="couponValue" class="form-control" maxLength="32" readonly="true" />
								<form:errors path="couponValue" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">优惠券名称</label>
							<div class="col-md-5">
								<form:input path="couponName" class="form-control" maxLength="32" readonly="true" />
								<form:errors path="couponName" class="field-has-error"></form:errors>
							</div>
						</div>
						</c:if>
						
						<div class="form-group">
							<label class="col-md-2 control-label">用户备注:</label>
							<div class="col-md-5">
								<form:textarea path="remarks" readonly="true" rows="2" cols="50" class="form-control" />
							</div>
						</div>
					</section>
					<section class="panel"> <header class="panel-info"> <h4>派工信息</h4> </header>
						<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
						
						<div class="col-sm-12">
							<table class="table table-striped table-advance table-hover" >
								<thead>
									<tr>
										<th>门店</th>
										<th>云店</th>
										<th>服务人员</th>
										<th>手机号</th>
										<th>派工时间</th>
										<th>是否接单</th>
										<th>接单时间</th>
									</tr>
								</thead>
								<tbody >
									 <c:forEach items="${oaOrderListVoModel.orderDispatchs}" var="item">
										<tr>
											<td>${item.parentOrgName }</td>
											<td>${item.orgName }</td>
											<td>${item.staffName }</td>
											<td>${item.staffMobile }</td>
											<td><timestampTag:timestamp patten="MM-dd HH:mm" t="${item.addTime * 1000}" /></td>
											<td>${item.applyStatus }</td>
											<td>${item.applyTimeStr }</td>
										</tr>
									 </c:forEach>
								</tbody>
							</table>
						</div>
						
						
					</section>
					<section class="panel"> <header class="panel-info"> <h4>派工调整</h4> </header>
						<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
							
						<div class="form-group required">
							<label class="col-md-2 control-label">用户服务地址</label>
							<div class="col-md-5">
								<form:input path="orderAddress" class="form-control" maxLength="32" readonly="true" />
								<form:errors path="orderAddress" class="field-has-error"></form:errors>
							</div>
							<div class="col-md-5">
								<font color="red"><strong>如果用户地址不在服务范围内,则不会有可用派工</strong></font>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">
								<font color="red">派工方案(可调整)</font>
							</label>
							<div class="col-md-5">
								<form:radiobutton path="disWay" value="0" />
								智能推荐
								<form:radiobutton path="disWay" value="1" />
								指定派工
							</div>
						</div>
						<div class="form-group" id="disWayOne">
							<label class="col-md-2 control-label">
								<font color="red">服务时间(可调整)</font>
							</label>
							<div class="col-md-5">
								<form:input path="serviceDateStr" class="form-control form_datetime" readonly="true" />
								<form:hidden path="serviceDate" />
							</div>
							<div class="col-md-5">
								<font color="red"><strong>如果没有可用服务人员,可尝试更改服务时间</strong></font>
							</div>
						</div>
						<div class="form-group required" id="div-org-id">
							<label class="col-md-2 control-label">选择门店:</label>
							<div class="col-md-5">
								<orgSelectTag:select />
							</div>
						</div>
						<div class="form-group" id="div-cloud-id">
							<label class="col-md-2 control-label">选择云店:</label>
							<div class="col-md-5">
								<select name="orgId" id="orgId" class="form-control">
									<option value="0">全部</option>
								</select>
							</div>
						</div>
						<div id="staffList" class="col-sm-12">
							
							已选择：<input type="text" id="selectedStaffs" data-role="tagsinput" readonly="true"  />

							<table class="table table-striped table-advance table-hover" >
								<thead>
									<tr>
										<th>选派员工</th>
										<th>地区门店</th>
										<th>云店</th>
										<th>云店距用户距离</th>
										<th>服务人员</th>
										<th>手机号</th>
										<th>距用户距离</th>
										<th>今日接单数</th>
										<th>是否可派工</th>
										<th>原因</th>
									</tr>
								</thead>
								<tbody id="allStaff">
								</tbody>
							</table>
						</div>
						</section>
						<div class="form-actions fluid">
							<div class="col-md-offset-3 col-md-3">
								<button type="button" class="btn btn-success" id="submitForm">保存修改</button>
								<c:if test="${oaOrderListVoModel.payType==6 and (sessionScope.accountAuth.accountRole.id == 1 or sessionScope.accountAuth.accountRole.id == 3) }">
									<a href="cancelOrder/${oaOrderListVoModel.id }" class="btn btn-success">取消订单</a>
								</c:if>
							</div>
						</div>
				</form:form>
			</div>
			</section>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include file="../shared/pageFooter.jsp"%>
	<!--footer end--> </section>
	<!-- js placed at the end of the document so the pages load faster -->
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>
	<!--script for this page-->
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js'/>"></script>
	<script src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>" type="text/javascript"></script>
	<script src="<c:url value='/assets/bootstrap-tagsinput/bootstrap-tagsinput.js'/>" type="text/javascript"></script>
	
	<!-- 时间戳类库 -->
	<script type="text/javascript" src="<c:url value='/js/moment/moment-with-locales.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/order/orderExpViewForm.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jhj/select-org-cloud.js'/>"></script>
</body>
</html>
