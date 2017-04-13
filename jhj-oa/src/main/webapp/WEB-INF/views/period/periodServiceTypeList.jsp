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
			<h4>定制服务类型列表</h4>
			</header>
			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
			<table class="table table-striped table-advance table-hover" id="table2excel">
				<thead>
					<tr>
						<th>定制服务类型名称</th>
						<th>服务类型</th>
						<th>服务子类型</th>
						<th>原价</th>
						<th>优惠价</th>
						<th>频次</th>
						<th>总次数</th>
						<th>是否有效</th>
						<th>备注</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.list}" var="item">
						<tr>
							<td>${item.name }</td>
							<td>${item.serviceTypeId }</td>
							<td>${item.serviceAddonId }</td>
							<td>${item.price }</td>
							<td>${item.vipPrice }</td>
							<td>${item.num }/{item.punit }</td>
							<td>${item.total }</td>
							<td>
								<c:if test="${item.enbale==0 }">是</c:if>
								<c:if test="${item.enbale==1 }">否</c:if>
							</td>
							<td>${item.remarks }</td>
						</tr>
					</c:forEach>
				</tbody> 
			</table>
			</section>
			<c:import url="../shared/paging.jsp">
				<c:param name="pageModelName" value="page" />
				<c:param name="urlAddress" value="/period/getList" />
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
