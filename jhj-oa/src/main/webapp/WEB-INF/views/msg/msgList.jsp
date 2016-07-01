<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<!--taglib for this page-->
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
<%-- <%@ taglib prefix="msgSendGroupTag" uri="/WEB-INF/tags/msgSendGroup.tld"%> --%>
<%-- <%@ taglib prefix="msgSendGroupSelectTag" uri="/WEB-INF/tags/msgSendGroupSelect.tld" %> --%>
<%@ taglib prefix="statusTag" uri="/WEB-INF/tags/statusName.tld"%>
<%@ taglib prefix="sendStatusTag" uri="/WEB-INF/tags/sendStatusName.tld"%>

<html>
<head>

<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>

<!--css for this page-->
<link rel="stylesheet"
	href="<c:url value='/assets/bootstrap3-dialog-master/dist/css/bootstrap-dialog.min.css'/>"
	type="text/css" />
<link rel="stylesheet"
	href="<c:url value='/assets/data-tables/DT_bootstrap.css'/>"
	type="text/css" />
<style>
.modal-backdrop {
	position: relative;
}

#sum {
	width: 100px;
	overflow: hidden;
	text-overflow: ellipsis
}
</style>

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
			消息管理

			<div class="pull-right">
				<button onClick="btn_add('msg/msgForm?msg_id=0')"
					class="btn btn-primary" type="button">
					<i class="icon-expand-alt"></i>新增
				</button>
			</div>
			</header>

			<hr
				style="width: 100%; color: black; height: 1px; background-color: black;" />


			<table class="table table-striped table-advance table-hover">
				<thead>
					<tr>
						<!-- <th>序号</th> -->
						<th>标题</th>
						<th>摘要</th>
						<!-- <th>详细内容</th> -->
						<th>用户类型</th>
						<th>是否有效</th>
						<th>添加时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${contentModel.list}" var="item">
						<tr>
							<%-- <td>${ item.msgId }</td> --%>
							<td>${ item.title }</td>
							<td>${ item.summary }</td>
							<%-- <td>${ item.content }</td> --%>
							<td>
								<c:choose>
										<c:when test="${item.userType==0}">
											用户
										</c:when>
										<c:when test="${item.userType==1}">
											   服务人员
										</c:when>
								</c:choose>
							</td>
							
							<td>
								<c:choose>
									<c:when test="${item.isEnable==0}">
										无效
									</c:when>
									<c:when test="${item.isEnable==1}">
										 有效
									</c:when>
								</c:choose>
							</td>
							
							<td>
								<timestampTag:timestamp patten="yyyy-MM-dd HH:mm" t="${item.addTime * 1000}" />
							</td>
							<td>
								<button id="btn_update" class="btn btn-primary btn-xs" title="修改消息"
									onClick="btn_update('msg/msgForm?msg_id=${ item.msgId }')">
								    <i class=" icon-ambulance"></i>
								</button>
							</td> 
						</tr>
					</c:forEach>
				</tbody>
			</table>

			</section>

			<c:import url="../shared/paging.jsp">
				<c:param name="pageModelName" value="contentModel" />
				<c:param name="urlAddress" value="/msg/msgList" />
			</c:import>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include
		file="../shared/pageFooter.jsp"%> <!--footer end-->
	</section>

	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>

	<!--script for this page-->
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap3-dialog-master/dist/js/bootstrap-dialog.min.js'/>"></script>

</body>
</html>