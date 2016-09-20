<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
			<h4>服务人员黑名单列表</h4>
			</header>
			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
			<table class="table table-striped table-advance table-hover" id="table2excel">
				<thead>
					<tr>
						<th>服务人员姓名</th>
						<th>服务人员手机号</th>
						<th>总收入</th>
						<th>总欠款</th>
						<th>时间</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${contentModel.list}" var="item">
						<tr>
							<td>${ item.name }</td>
							<td>${ item.mobile }</td>
							<td>${ item.totalIncoming }</td>
							<td>${ item.totalDept }</td>
							<td><timestampTag:timestamp patten="yyyy-MM-dd HH:mm:ss" t="${item.updateTime * 1000}" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</section>
			<c:import url="../shared/paging.jsp">
				<c:param name="pageModelName" value="contentModel" />
				<c:param name="urlAddress" value="/staff/staffBlack-list" />
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
