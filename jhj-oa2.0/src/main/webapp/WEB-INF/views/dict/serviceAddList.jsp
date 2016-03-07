<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
 <%@ taglib prefix="addServiceTypeTag" uri="/WEB-INF/tags/addServiceTypeName.tld" %>
<html>
<head>

<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>


<!--css for this page-->

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
			服务类型附加列表

			<div class="pull-right">
				<button onClick="btn_add('/base/serviceAddForm?serviceAddonId=0&serviceType=${serviceTypeId}')"
					class="btn btn-primary" type="hidden">
					<i class="icon-expand-alt"></i>新增
				</button>
			</div>
			</header> <%-- <form:form modelAttribute="contentModel" class="form-horizontal"
				method="POST" action="serviceAdd-list" id="dict-form"
				enctype="multipart/form-data">

				<form:hidden path="serviceType" /> --%>

				<table class="table table-striped table-advance table-hover">
					<thead>
						<tr>
							<th>服务类型</th>
							<th>名称</th>
							<th>单价</th>
							<th>介绍</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${contentModel.list}" var="item">
						<tr>
							<td>
							   <addServiceTypeTag:addServicetype addServiceType="${item.serviceType }"/>
							</td>
							           
							<td>${ item.name }</td>
							<td>${ item.price }</td>
							<td>${ item.descUrl }</td>

							<td>
								<button id="btn_update"
									onClick="btn_update('/base/serviceAddForm?serviceAddonId=${item.serviceAddonId}&serviceType=${serviceTypeId}')"
									class="btn btn-primary btn-xs" title="修改">
									<i class="icon-pencil"></i>
								</button>
								<button id="btn_del"
									onClick="btn_del('/base/delete?serviceAddonId=${item.serviceAddonId}&&id=${item.serviceType }')"
									class="btn btn-danger btn-xs" title="删除">
									<i class="icon-trash "></i>
								</button>
								<%-- <button id="btn_del"
									onClick="btn_del('/base/delete/${item.serviceAddonId}?id=${item.serviceType }')"
									class="btn btn-danger btn-xs" title="删除">
									<i class="icon-trash "></i>
								</button> --%>
							</td>
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

	<!-- js placed at the end of the document so the pages load faster -->
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>

	<!--script for this page-->
	<script src="<c:url value='/js/jhj/account/list.js'/>"></script>

</body>
</html>