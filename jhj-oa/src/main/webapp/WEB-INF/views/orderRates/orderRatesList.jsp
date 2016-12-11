<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html>
<%@ include file="../shared/taglib.jsp"%>
<!-- taglib for this page -->
<%@ taglib prefix="orderFromTag" uri="/WEB-INF/tags/orderFromName.tld"%>
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
<%@ taglib prefix="orderVoStatusTag" uri="/WEB-INF/tags/orderVoStatusName.tld"%>
<%@ taglib prefix="payTypeNameTag" uri="/WEB-INF/tags/payTypeName.tld"%>
<%@ taglib prefix="orderServiceTimeTag" uri="/WEB-INF/tags/orderServiceTime.tld"%>
<%@ taglib prefix="serviceTypeTag" uri="/WEB-INF/tags/serviceTypeName.tld"%>
<%@ taglib prefix="cloudOrgSelectTag" uri="/WEB-INF/tags/CloudOrgSelect.tld"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
<%@ taglib prefix="orderTypeNameTag" uri="/WEB-INF/tags/orderTypeName.tld"%>
<%@ taglib prefix="serviceTypeSelectTag" uri="/WEB-INF/tags/serviceTypeSelect.tld"%>
<%@ taglib prefix="orderStatusSelectTag" uri="/WEB-INF/tags/orderSatusSelect.tld"%>
<%@ taglib prefix="orderFromSelectTag" uri="/WEB-INF/tags/orderFromSelect.tld"%>
<html>
<head>
<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>
<!--css for this page-->
<link href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>" rel="stylesheet"
	type="text/css" />
<link href="<c:url value='/assets/bootstrap-datetimepicker/css/datetimepicker.css'/>" rel="stylesheet" type="text/css" />
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
								<h4>数据搜索</h4>
									<form:form modelAttribute="orderDispatchSearchVo"  class="form-inline"
									 method="GET" id="oaSearchForm">
									<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table table-bordered table-condensed" >
										<tr>
											<td width="10%">选择门店:</td>
											<td width="23%"><orgSelectTag:select selectId="${searchModel.parentId }" sessionOrgId="${loginOrgId }"/></td>
											<td width="10%">选择云店:</td>
											<td width="23%">
												<select name="orgId" id="orgId" class="form-control">
													<option value="0">全部</option>
												</select>
											</td>
										</tr>
										
										<tr>
											<td width="10%">用户手机号：</td>
											<td width="23%"><form:input path="mobile" class="form-control" placeholder="请输入手机号" /></td>
											<td width="10%">服务人员手机号：</td>
											<td width="23%"><form:input path="staffMobile" class="form-control" placeholder="请输入手机号" /></td>
										</tr>
										
										<tr>
											<td width="10%">服务人员名称：</td>
											<td width="10%">
												<form:input path="staffName" class="form-control" placeholder="请输入服务人员名称" />
											</td>
											<td width="10%">服务日期：</td>
											<td width="40%">
												<input id="serviceStartTimeStr" name="serviceStartTimeStr" value="${serviceStartTimeStr }"
												class="form-control form-datetime" style="width: 170px; margin-bottom: 0" readonly="true" /> <span>至</span>
												<input id="serviceEndTimeStr" name="serviceEndTimeStr" value="${serviceEndTimeStr }" class="form-control form-datetime"
												style="width: 170px; margin-bottom: 0" readonly="true" />
											</td>
										</tr>
										<tr>
											<td colspan="6">
												<button type="button" id="btnSearch" name="searchForm" class="btn btn-primary" value="${listUrl }">搜索</button>
												<button type="button" class="btn btn-primary" onclick="cleanForm()">清空</button>
											</td>					
										</tr>
									</table>
								</form:form>
							</header>
							
							<hr style="width: 100%; color: black; height: 1px; background-color: black;" />

							<table class="table table-condensed table-hover table-striped" id="table2excel">
								<thead>
									<tr>
										<th width="5%">门店</th>
										<th width="8%">云店</th>
										<th width="8%">服务类别</th>
										<th width="8%">服务时间</th>
										<th>用户手机号</th>
										<th width="10%">用户地址</th>
										<th width="7%">服务人员</th>
										<th>服务人员手机号</th>
										<th width="5%">是否准时到达</th>
										<th width="5%">服务态度</th>
										<th width="5%">服务技能</th>
										<th>评价内容</th>
										<th width="7%">操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${page.list}" var="item">
											<tr>
												<td>${ item.parentOrgName }</td>
												<td>${ item.orgName }</td>
												<td>${ item.serviceTypeName }</td>
												<td>
													<timestampTag:timestamp patten="MM-dd HH:mm" t="${item.serviceDate}" />
												</td>
												<td>${item.usermobile }</td>
												<td>${ item.userAddr }</td>
												<td>${ item.staffName }</td>
												<td>${ item.staffMobile }</td>
												<td>
													<c:if test="${item.rateArrival == 0 }">
														准时
													</c:if>
													<c:if test="${item.rateArrival == 1 }">
														迟到
													</c:if>
												</td>
												<td>${item.rateAttitude }颗星</td>
												<td>${ item.rateSkill }颗星</td>
												<td>${ item.rateContent }</td>
												<td>
													<c:if test="${item.orderType==0 }">
														<a href="order-hour-list?orderId=${item.orderId }">查看订单</a>
													</c:if>
													<c:if test="${item.orderType==1 }">
														<a href="order-exp-list?orderId=${item.orderId }">查看订单</a>
													</c:if>
												</td>
											</tr>
									</c:forEach>
								</tbody>
							</table>
						</section>
						<%@ include file="../shared/importJs.jsp"%>
						<c:import url="../shared/paging.jsp">
							<c:param name="pageModelName" value="page" />
							<c:param name="urlAddress" value="/order/order-rate-list" />
						</c:import>
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
	
	<script src="<c:url value='/assets/jquery.table2excel.js'/>"></script>
	<!--script for this page-->
	<script src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
	<script src="<c:url value='/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js'/>"></script>
	<script src="<c:url value='/assets/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js'/>"></script>
	
	<script type="text/javascript" src="<c:url value='/js/jhj/select-org-cloud.js'/>"></script>
	<script src="<c:url value='/js/order/orderList.js'/>"></script>
</body>
</html>
