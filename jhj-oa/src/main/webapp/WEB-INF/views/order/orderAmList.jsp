<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<!-- taglib for this page -->
<%@ taglib prefix="orderFromTag" uri="/WEB-INF/tags/orderFromName.tld"%>
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
<%@ taglib prefix="orderVoStatusTag"
	uri="/WEB-INF/tags/orderVoStatusName.tld"%>
<%@ taglib prefix="payTypeNameTag" uri="/WEB-INF/tags/payTypeName.tld"%>
<%@ taglib prefix="orderServiceTimeTag"
	uri="/WEB-INF/tags/orderServiceTime.tld"%>
<%@ taglib prefix="serviceTypeTag"
	uri="/WEB-INF/tags/serviceTypeName.tld"%>


<%@ taglib prefix="cloudOrgSelectTag" uri="/WEB-INF/tags/CloudOrgSelect.tld"%>
<%@ taglib prefix="orderTypeNameTag"
	uri="/WEB-INF/tags/orderTypeName.tld"%>
<%@ taglib prefix="serviceTypeSelectTag"
	uri="/WEB-INF/tags/serviceTypeSelect.tld"%>
<%@ taglib prefix="orderStatusSelectTag"
	uri="/WEB-INF/tags/orderSatusSelect.tld"%>
<%@ taglib prefix="orderFromSelectTag"
	uri="/WEB-INF/tags/orderFromSelect.tld"%>

<html>
<head>

<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>
<!--css for this page-->
<link href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>"
	     rel="stylesheet" type="text/css" />
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
				<h4>数据搜索</h4>
				<form:form modelAttribute="oaOrderSearchVoModel" onsubmit="return checkEndTime()"
					action="order-am-list" class="form-inline"	method="GET" id="oaSearchForm">
					<div class="form-group">
						订单状态:
						<c:if test="${loginOrgId == 0 }">
							<form:select path="orderStatus" class="form-control">
								<option value="">请选择订单状态</option>
								<form:option value="0">已取消</form:option>
								<form:option value="1">已预约</form:option>
								<form:option value="2">已确认</form:option>
								<form:option value="3">已支付</form:option>
								<form:option value="4">已派工</form:option>
								<form:option value="5">开始服务</form:option>
								<form:option value="7">完成服务</form:option>
								<form:option value="9">已关闭</form:option>
							</form:select>
						</c:if>
						
						<!-- 如果是 店长登录,则 只能选择 已派工之后的 订单状态 -->
          				<c:if test="${loginOrgId > 0 }">
          					<form:select path="orderStatus" class="form-control">
          						<option value="">请选择订单状态</option>
          						<form:option value="4">已派工</form:option>
								<form:option value="5">开始服务</form:option>
								<form:option value="7">完成服务</form:option>
								<form:option value="9">已关闭</form:option>	
          					</form:select>
          				</c:if>
						
					</div>
					<div class="form-group">
						选择云店:<cloudOrgSelectTag:select 
									selectId="${oaOrderSearchVoModel.orgId }"
									logInParentOrgId="${loginOrgId }"/>
					</div>	
					
					<div class="form-group">
                       	下单开始时间：
						<form:input path="startTimeStr" class="form-control form_datetime"
						 style="width:110px; margin-bottom:0" readonly="true" />
					</div>
					<div class="form-group">
						下单结束时间：
						<form:input path="endTimeStr" class="form-control form_datetime" 
						style="width:110px; margin-bottom:0" readonly="true" />
					</div> 
					
					
					<button type="button" id="btnSearch" name="searchForm" class="btn btn-primary" >搜索</button>
					<button type="button" id="btnExport" name="searchForm" class="btn btn-success">导出excel</button>
					
				</div>
			</form:form>
			</header>

		<hr style="width: 100%; color: black; height: 1px; background-color: black;" />

		<header class="panel-heading">
		<h4>助理订单列表</h4>
		</header>
	
	
		<table class="table table-striped table-advance table-hover"
			id="targetTable">
			<thead>
				<tr >
					<th>门店名称</th>
					<th>云店名称</th>
					<th>服务人员</th>
					<th>下单时间</th>
					<th>订单类型</th>
					<th>服务时间</th> 
					<th>用户手机号</th>
					<th>服务地址</th>
					<!-- <th>派工状态</th> -->
					<th>订单状态</th>
					<th>支付方式</th>
					<th>总金额</th>
					<th>支付金额</th>
					<th class="noExl">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${oaOrderListVoModel.list}" var="item">
					<c:forEach items="${item.statusNameMap }" var="sta">

						<tr>
							<input type="hidden" id="itemPayType" value="${item.payType }">	
							<input type="hidden" id="itemOrderStatus" value="${item.orderStatus }">	
							<td>${ item.orgName }</td>
							<td>${item.cloudOrgName }</td>
							<td>${ item.staffName } </td>
							
							<td><timestampTag:timestamp patten="yyyy-MM-dd"
									t="${item.addTime * 1000}" /></td>

							<td>${item.orderTypeName }</td>

							<td><timestampTag:timestamp patten="yyyy-MM-dd HH:mm"
									t="${item.serviceDate * 1000}" /></td>
							<td>${ item.mobile }</td>
							<td>${ item.orderAddress }</td>
							<td id="payTypeStatus">
								<orderVoStatusTag:orderstatus
									orderStatus="${item.orderStatus }"
									orderType="${item.orderType }" />
							</td>		
							<td>
								${ item.payTypeName } 
							</td>		
							<td>${ item.orderMoney }</td>
							<td>${ item.orderPay }</td>

							<td>
								<button id="btn_update"
									onClick="btn_update('order/order-am-view?orderNo=${ item.orderNo }&disStatus=${fn:substring(sta.key,0,1) }')"
									class="btn btn-primary btn-xs" title="订单详情">
									<i class=" icon-ambulance"></i>
								</button>
								
								<!-- 如果 运营人员备注为 空，可以添加，不为空，不让添加 -->
								<c:if test="${empty item.remarksBussinessConfirm  }">
						       		<button  
						       			onClick="btn_update('order/remarks_bussiness_form?orderId=${ item.id }')" 
						       			class="btn btn-primary btn-xs" 
						       			title="添加订单备注">
						       				<i class="icon-plus-sign-alt"></i>
						       		</button>
							    </c:if>
							</td>
						</tr>
					</c:forEach>
				</c:forEach>
			</tbody>
		</table>
	</section> <c:import url="../shared/paging.jsp">
		<c:param name="pageModelName" value="oaOrderListVoModel" />
		<c:param name="urlAddress" value="/order/order-am-list" />
	</c:import>
	</div>
	</div>
	<!-- page end--> </section> </section>
	<!--main content end-->

	<!--footer start-->
	<%@ include file="../shared/pageFooter.jsp"%>
	<!--footer end-->
	</section>

	<!-- js placed at the end of the document so the pages load faster -->
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>
	
	 <script type="text/javascript" src="<c:url value='/assets/jquery.table2excel.js'/>"></script> 
	
	 <script type="text/javascript"
		src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>
	
	 <script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
		
	<script src="<c:url value='/js/order/orderAmList.js'/>"></script>
	
</body>
</html>
