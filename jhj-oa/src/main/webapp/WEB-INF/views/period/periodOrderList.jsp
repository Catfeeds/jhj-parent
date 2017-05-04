<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html>
<%@ include file="../shared/taglib.jsp"%>
<!-- taglib for this page -->
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
<%@ taglib prefix="payTypeName" uri="/WEB-INF/tags/payTypeName.tld"%>
<%@ taglib prefix="orderFromName" uri="/WEB-INF/tags/orderFromName.tld"%>
<%@ taglib prefix="addressName" uri="/WEB-INF/tags/addressName.tld"%>
<html>
<head>
<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>
<!--css for this page-->
</head>
<body>
	<section id="container"> <!--header start--> <%@ include file="../shared/pageHeader.jsp"%>
	<!--header end--> <!--sidebar start--> <%@ include file="../shared/sidebarMenu.jsp"%> <!--sidebar end-->
	<!--main content start--> <section id="main-content"> <section class="wrapper"> <!-- page start-->
	<div class="row">
		<div class="col-lg-12">
			<section class="panel"> 
			<header class="panel-heading">
			<h4>定制服务订单</h4>
			<h4>数据搜索</h4>
				<form:form modelAttribute="periodSearchModel"  class="form-inline" method="GET" >
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table table-bordered table-condensed" >
					<tr class="tr-hidden">
						<td width="10%">用户手机号：</td>
						<td width="23%"><form:input path="mobile" class="form-control" placeholder="请输入手机号" /></td>
						<td width="10%">订单状态：</td>
						<td width="23%">
							<form:select path="orderStatus" class="form-control">
								<option value="">订单状态</option>
								<form:option value="0">已取消</form:option>
								<form:option value="1">未支付</form:option>
								<form:option value="2">已支付</form:option>
								<form:option value="3">未完成</form:option>
								<form:option value="4">已完成</form:option>
							</form:select>
						</td>
						<td width="23%">
							<button type="submit" id="btnSearch" name="searchFormBtn" class="btn btn-primary">搜索</button>
						</td>
					</tr>
				</table>
				</form:form>
			</header>
			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
			<table class="table table-striped table-advance table-hover" id="table2excel">
				<thead>
					<tr>
						<th>下单时间</th>
						<th>手机号</th>
						<th>地址</th>
						<th>订单状态</th>
						<th>支付类型</th>
						<th>订单金额</th>
						<th>优惠金额</th>
						<th>订单来源</th>
						<th>订单数</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${periodOrderListPage.list}" var="item">
						<tr>
							<td><timestampTag:timestamp patten="yyyy-MM-dd" t="${item.addTime*1000 }"/></td> 
							<td>${item.mobile }</td>
							<td><addressName:addr addrId="${item.addrId }" /></td>
							<td>
								<c:if test="${item.orderStatus==0 }">已取消</c:if>
								<c:if test="${item.orderStatus==1 }">未支付</c:if> 
								<c:if test="${item.orderStatus==2 }">已支付</c:if>  
								<c:if test="${item.orderStatus==3 }">未完成</c:if>
								<c:if test="${item.orderStatus==4 }">已完成</c:if> 
							</td>
							<td><payTypeName:payType payType="${item.payType }" orderStatus="2"/> </td>
							<td>${item.orderMoney }</td>
							<td>${item.userCouponsId }</td>
							<td><orderFromName:orderfrom orderFrom="${item.orderFrom}"/></td>
							<td>${item.totalOrder }</td>
							<td>
								
								<button id="btn_detail"
									onClick="javascript:btn_select('/period/updatePeriodOrder?periodOrderId=${item.id }')"
									class="btn btn-primary btn-xs" title="订单详情">
									<i class=" icon-ambulance"></i>
								</button>
								
								<button id="btn_detail"
									onClick="javascript:btn_select('/order/order-list?periodOrderId=${item.id }')"
									class="btn btn-primary btn-xs" title="关联订单">
									<i class="  icon-search"></i>
								</button>
							
							</td>
						</tr>
					</c:forEach>
				</tbody> 
			</table>
			</section>
			<c:import url="../shared/paging.jsp">
				<c:param name="pageModelName" value="periodOrderListPage" />
				<c:param name="urlAddress" value="/period/periodOrderList" />
			</c:import>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include file="../shared/pageFooter.jsp"%>
	<!--footer end--> </section>
	<!-- js placed at the end of the document so the pages load faster -->
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>
</body>
</html>
