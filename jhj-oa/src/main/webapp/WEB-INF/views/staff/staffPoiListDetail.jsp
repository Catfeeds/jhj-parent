<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="../shared/taglib.jsp"%>
<%@ include file="../shared/importCss.jsp"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
<%@ taglib prefix="cloudOrgSelectTag" uri="/WEB-INF/tags/CloudOrgSelect.tld"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作人员轨迹列表</title>
<%@ include file="../shared/importCss.jsp"%>
<link href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>" rel="stylesheet"
	type="text/css" />
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
							<header class="panel-info">
								<h4>数据搜索</h4>
								<form:form class="form-inline"  action="poi-list-detail" method="get">
									<input type="hidden" id="staffId" name="staffId" value="${staffId }"/>
									<table class="table">
										<tr>
											<td>日期:</td>
											<td><input id="serviceDateStr" name="serviceDateStr" value="${today}" class="form-control form_datetime"
												style="width: 170px; margin-bottom: 0" readonly="true" /></td>
											<td>
												<button type="submit" class="btn btn-primary">搜索</button>
											</td>
										</tr>
										
									</table>
								</form:form>
							</header>
							<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
							<header class="panel-info">
								<h4>工作人员轨迹列表</h4>
								<div class="pull-right"></div>
							</header>
							<table class="table table-striped table-advance table-hover">
								<thead>
									<tr>
										<th>姓名</th>
										<th>位置时间</th>
										<th>位置</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${contentModel.list}" var="item">
										
										<tr>
											
											<td>${ item.name }</td>
											<td>${ item.addTimeStr }</td>
											
											<td>${ item.poiName }</td>
											
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</section>
						<%@ include file="../shared/importJs.jsp"%>
						<c:import url="../shared/paging.jsp">
							<c:param name="pageModelName" value="contentModel" />
							<c:param name="urlAddress" value="/staff/poi-list-detail" />
						</c:import>
					</div>
				</div>
			</section>
		</section>
		
		<%@ include file="../shared/pageFooter.jsp"%>
		<script src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
		<script src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>		
		<script src="<c:url value='/js/staff/staffPoiListDetail.js'/>"></script>

	</section>
</body>
</html>
