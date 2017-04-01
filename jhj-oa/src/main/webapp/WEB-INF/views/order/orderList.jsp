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
<%@ taglib prefix="adminAccountSelectTag" uri="/WEB-INF/tags/adminAccountSelect.tld"%>
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
									<form:form modelAttribute="searchModel" onsubmit="return checkEndTime()" class="form-inline"
									 method="GET" id="oaSearchForm">
									<form:hidden path="orderType"/>
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
											<td width="10%">订单状态：</td>
											<td width="23%"><c:if test="${loginOrgId == 0 }">
													<form:select path="orderStatus" class="form-control">
														<option value="">订单状态</option>
														<form:option value="0">已取消</form:option>
														<form:option value="1">未支付</form:option>
														<form:option value="2">已支付</form:option>
														<form:option value="3">已派工</form:option>
														<form:option value="5">开始服务</form:option>
														<form:option value="7">完成服务</form:option>
														<form:option value="8">已评价</form:option>
														<form:option value="9">已关闭</form:option>
													</form:select>
												</c:if> <!-- 如果是 店长登录,则 只能选择 已派工之后的 订单状态 --> <c:if test="${loginOrgId > 0 }">
													<form:select path="orderStatus" class="form-control">
														<option value="">订单状态</option>
														<form:option value="3">已派工</form:option>
														<form:option value="5">开始服务</form:option>
														<form:option value="7">完成服务</form:option>
														<form:option value="8">已评价</form:option>
														<form:option value="9">已关闭</form:option>
													</form:select>
												</c:if>
											</td>
										</tr>
										
										<tr>
											<td width="10%">用户手机号：</td>
											<td width="23%"><form:input path="mobile" class="form-control" placeholder="请输入手机号" /></td>
											<td width="10%">服务地址：</td>
											<td width="23%"><form:input path="addrName" class="form-control" placeholder="请输入服务地址" /></td>
											
											<td width="10%">是否接单</td>
											<td width="23%"><form:select path="isApply" class="form-control">
													<option value="">全部</option>
													<form:option value="1">是</form:option>
													<form:option value="0">否</form:option>
												</form:select>
											</td>
										</tr>
										<tr>
											<td  width="10%">服务人员手机号：</td>
											<td width="23%">
												<form:input path="staffMobile" class="form-control" placeholder="请输入手机号" />
											</td>
											<td  width="10%">订单来源：</td>
											<td >
												<form:select path="orderOpFrom" class="form-control">
													<form:option value="">--请选择订单来源--</form:option>
													<form:option value="1">来电订单</form:option>
													<form:option value="11">微网站</form:option>
													<c:forEach items="${businessList }" var="business">
														<form:option value="${business.id}">${business.businessName }</form:option>
													</c:forEach>
												</form:select>
											</td>
											<td  width="10%">支付方式</td>
											<td>
												<form:select path="payType" class="form-control">
													<form:option value="">--请选择支付方式--</form:option>
													<form:option value="0">余额支付</form:option>
													<form:option value="1">支付宝</form:option>
													<form:option value="2">微信</form:option>
													<form:option value="6">现金支付</form:option>
													<form:option value="7">第三方支付</form:option>
												</form:select>
											</td>
														
										</tr>
										<tr>
											<td width="100%" colspan="6" style="padding:0;border:0">
												<table width="100%" style="border:0" cellspacing="0" cellpadding="0" class="table-bordered table-condensed">
													<tbody>
														<tr>
															<td width="10%" style="border:0;border-bottom-width:0;border-left-width:0">下单时间：</td>
															<td width="40%" style="border-top-width:0;border-bottom-width:0;">
																<input id="startTimeStr" name="startTimeStr" value="${startTimeStr }" class="form-control form_datetime"
																	style="width: 170px; margin-bottom: 0" readonly="true" /> <span>至</span> 
																<input id="endTimeStr"
																	name="endTimeStr" value="${endTimeStr }" class="form-control form_datetime" style="width: 170px; margin-bottom: 0" readonly="true" />
															</td>
															<td width="10%" style="border-top-width:0;border-bottom-width:0;">服务日期：</td>
															<td width="40%" style="border-top-width:0;border-bottom-width:0;">
																<input id="serviceStartTimeStr" name="serviceStartTimeStr" value="${serviceStartTimeStr }"
																class="form-control form-datetime" style="width: 170px; margin-bottom: 0" readonly="true" /> <span>至</span>
																<input id="serviceEndTimeStr" name="serviceEndTimeStr" value="${serviceEndTimeStr }" class="form-control form-datetime"
																style="width: 170px; margin-bottom: 0" readonly="true" />
															</td>
														</tr>
													</tbody>
												</table>
											 </td>
										</tr>
										
										<tr>
											<td width="100%" colspan="6" style="padding:0;border:0">
												<table width="100%" style="border:0" cellspacing="0" cellpadding="0" class="table-bordered table-condensed">
													<tbody>
														<tr>
															<td width="10%" style="border:0;border-bottom-width:0;border-left-width:0">服务完成时间：</td>
															<td width="40%" style="border-top-width:0;border-bottom-width:0;">
																<input id="startUpdateTimeStr" name="startOrderDoneTimeStr" value="${startOrderDoneTimeStr }" class="form-control form_datetime"
																	style="width: 170px; margin-bottom: 0" readonly="true" /> <span>至</span> 
																<input id="endUpdateTimeStr"
																	name="endOrderDoneTimeStr" value="${endOrderDoneTimeStr }" class="form-control form_datetime" style="width: 170px; margin-bottom: 0" readonly="true" />
															</td>
															<td width="10%" style="border-top-width:0;border-bottom-width:0;">录入人</td>
															<td width="40%" style="border-top-width:0;border-bottom-width:0;">
																<adminAccountSelectTag:select roleId="${accountAuth.accountRole.id}" selectId="${searchModel.adminId }" />
															</td>
														</tr>
													</tbody>
												</table>
											 </td>
										</tr>
										
										<tr>
											<td width="10%">服务人员姓名：</td>
											<td width="23%">
												<form:input path="staffName" class="form-control" placeholder="请输入服务人员名称"/>
											</td>
											<td width="10%">订单号：</td>
											<td width="23%">
												<form:input path="orderNo" class="form-control" placeholder="请输入订单号"/>
											</td>
											<td colspan="6">
												<button type="button" id="btnSearch" name="searchForm" class="btn btn-primary" value="${listUrl }">搜索</button>
												<button type="button" id="btnExport" name="searchForm" class="btn btn-success">导出excel</button>
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
										<th width="10%">云店</th>
										<th width="10%">服务人员</th>
										<th>人数</th>
										<th>下单时间</th>
										<th>订单类型</th>
										<th>服务日期</th>
										<th>时长</th>
										<th>手机号</th>
										<th width="10%">地址</th>
										<th>是否接单</th>
										<th>订单来源</th>
										<th>订单状态</th>
										<th>支付方式</th>
										<th>支付金额</th>
										<th>补时/差价</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${oaOrderListVoModel.list}" var="item">
											<tr>
												<input type="hidden" id="orderno" value="${item.orderNo }">
												<input type="hidden" id="itemPayType" value="${item.payType }">
												<input type="hidden" id="itemOrderStatus" value="${item.orderStatus }">
												<input type="hidden" id="staffNums" value="${item.staffNums }">
												<input type="hidden" id="staffName" value="${item.staffName }">
												<td>${ item.orgName }</td>
												<td>${ item.cloudOrgName }</td>
												<td>${ item.staffName }</td>
												<td>${ item.staffNums }</td>
												<td><timestampTag:timestamp patten="MM-dd" t="${item.addTime * 1000}" /></td>
												<td>${item.orderTypeName }</td>
												<td><timestampTag:timestamp patten="MM-dd HH:mm" t="${item.serviceDate * 1000}" /></td>
												<td>${ item.serviceHour }</td>
												<td>${ item.mobile }</td>
												<td>${ item.orderAddress }</td>
												<td>${ item.applyStatus }</td>
												<td>${item.orderOpFromName }</td>
												<td><orderVoStatusTag:orderstatus orderStatus="${item.orderStatus }" orderType="${item.orderType }" /></td>
												<td>${ item.payTypeName }</td>
												<td>${ item.orderPay }</td>
												<td style="width:8%">
													<%-- <c:if test="${item.orderExtType == 0 and item.spreadMoeny>0 }">补差价：<payTypeNameTag:payType payType="${item.payTypeExt }" orderStatus="${item.orderStatus }"/></c:if>
													<c:if test="${item.orderExtType == 1 }">加时：<payTypeNameTag:payType payType="${item.payTypeExt }" orderStatus="${item.orderStatus }"/></c:if>
													<c:if test="${item.spreadMoeny>0 }">${ item.spreadMoeny }</c:if>
													<c:if test="${item.spreadMoeny==0 }">-</c:if> --%>
													<c:if test="${item.orderExtTyePayStr ne null && item.orderExtTyePayStr ne'' }">
														${item.orderExtTyePayStr}
													</c:if>
												</td>
												<td>
													<button id="btn_detail"
														onClick="javascript:btnDetail('${ item.orderNo }', '${item.orderType }', '${item.disStatus}')"
														class="btn btn-primary btn-xs" title="订单详情">
														<i class=" icon-ambulance"></i>
													</button> <!-- 如果 运营人员备注为 空，可以添加，不为空，不让添加 --> 
													<button id="btnremarks"
														class="btn btn-primary btn-xs" title="添加订单备注" data-toggle="modal" data-order-no="${item.orderNo}" data-target="#updateOrderRemark" onclick="showLog(this)">
														<i class="icon-plus-sign-alt"></i>
													</button>
													<c:if test="${item.remarks ne ''}">
														<i class="btn btn-success btn-xs glyphicon glyphicon-comment" title="${item.remarks }"></i>
													</c:if>
													<c:if test="${item.overworkTimeStr ne null and item.overworkTimeStr ne '' }">
														<i title="${item.overworkTimeStr }"><img alt="超" src="<c:url value='/img/over-time-icon.png' />" width="23px" height="20px"></i>
													</c:if>
												</td>
											</tr>
									</c:forEach>
								</tbody>
							</table>
						</section>
						<div>
							<label>每页总金额：</label><b>${pageMoney }元</b>&nbsp;&nbsp;&nbsp;&nbsp;
							<label>总金额：</label><b>${totalMoney }元</b>
						</div>
						<%@ include file="../shared/importJs.jsp"%>
						<c:import url="../shared/paging.jsp">
							<c:param name="pageModelName" value="oaOrderListVoModel" />
							<c:param name="urlAddress" value="/order/${listUrl }" />
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
	
	<div class="modal fade" id="updateOrderRemark" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
	        <h4 class="modal-title" id="myModalLabel"><b>订单备注</b></h4>
	      </div>
	      <div class="modal-body">
	        <form  name="cancleForms">
	        	<input type="hidden" id="modal-orderNo" value=""/>
	        	<textarea name="remarks" id="remarks" rows="3" cols="78" maxlength='200' placeholder="备注信息"></textarea>
	        	<span id="remark-error"></span>
	        </form>
	      </div>
	     <div class="modal-footer">
	        <button type="button" id="submit-remarks" class="btn btn-default" data-dismiss="modal" >添加/修改备注</button>
	      </div>
	    </div>
	  </div>
	</div>
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
