<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html>
<%@ include file="../shared/taglib.jsp"%>
<!-- taglib for this page -->
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
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
							<td>${item.addrId }</td>
							<td>${item.orderStatus }</td>
							<td>${item.payType }</td>
							<td>${item.orderMoney }</td>
							<td>${item.userCouponsId }</td>
							<td>${item.orderFrom }</td>
							<td>${item.addTime }</td>
							<td><button type="button">修改</button></td>
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
