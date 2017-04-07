<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ include file="../shared/importCss.jsp"%>

<!-- s -->

<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
<%@ taglib prefix="serviceTypeTag" uri="/WEB-INF/tags/partnerServiceTypeName.tld"%>
<html>
<head>
<title>会员优惠券列表</title>

<!-- common css for all pages -->
<%@ include file="../shared/importCss.jsp"%>
<!-- css for this page -->
<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>"
	type="text/css" />

</head>

<body>

	<section id="container"> <!--header start--> <%@ include
		file="../shared/pageHeader.jsp"%> <!--header end-->

	<!--sidebar start--> <%@ include file="../shared/sidebarMenu.jsp"%>
	<!--sidebar end--> <!--main content start--> <section id="main-content">
	<section class="wrapper"> <!-- page start-->

	<div class="row">
		<div class="col-lg-12">
			<section class="panel"> <header class="panel-heading">
			<h4>会员优惠券列表</h4>

			</header>
			<table class="table table-striped table-advance table-hover">
				<thead>
					<tr>
						<th>优惠券名称</th>
						<th>优惠券面值</th>
						<th>使用金额条件</th>
						<th>使用类型</th>
						<th>开始日期</th>
						<th>结束日期</th>
						<th>使用情况</th>
						<th>使用时间</th>
						<th>对应订单号</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${userCouponsVos}" var="item">
						<tr>
							<td>${item.couponsName}</td>
							<td>${item.value}</td>
							<td>${item.maxValue }</td>
							<td><serviceTypeTag:typeId typeId="${item.serviceType }" /></td>
							<td><fmt:formatDate value="${ item.fromDate}"
									pattern="yyyy-MM-dd" /></td>
							<td><fmt:formatDate value="${ item.toDate}"
									pattern="yyyy-MM-dd" /></td>
							  <td>
								  <c:choose>
										<c:when test="${item.isUsed  == 0}">
												未使用
										</c:when>
										<c:when test="${item.isUsed  == 1}">
												已使用
										</c:when>
								  </c:choose>	
							  </td>
							  <td>
							     <timestampTag:timestamp patten="yyyy-MM-dd" t="${item.usedTime * 1000}"/>
							  </td>
							  <td>${item.orderNo}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			</section>

		</div>
	</div>

	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include
		file="../shared/pageFooter.jsp"%> <!--footer end-->
	</section>
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>

</body>
</html>
