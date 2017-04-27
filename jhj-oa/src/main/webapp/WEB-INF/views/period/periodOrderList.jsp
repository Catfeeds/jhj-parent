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
			<section class="panel"> <header class="panel-heading">
			<h4>定制服务订单</h4>
			</header>
			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
			<table class="table table-striped table-advance table-hover" id="table2excel">
				<thead>
					<tr>
						<th>用户</th>
						<th>手机号</th>
						<th>地址</th>
						<th>订单状态</th>
						<th>支付类型</th>
						<th>订单金额</th>
						<th>优惠金额</th>
						<th>订单来源</th>
						<th>下单时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${periodOrderListPage.list}" var="item">
						<tr>
							<td>${item.userId }</td>
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
							<td><timestampTag:timestamp patten="yyyy-MM-dd" t="${item.addTime*1000 }"/></td> 
							<td><a href="updatePeriodOrder?periodOrderId=${item.id }"><button type="button">修改</button></a></td>
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
