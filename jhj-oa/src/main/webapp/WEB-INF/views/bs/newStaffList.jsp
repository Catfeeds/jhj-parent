<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="../shared/taglib.jsp"%>
<%@ include file="../shared/importCss.jsp"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
<%@ taglib prefix="cloudOrgSelectTag" uri="/WEB-INF/tags/CloudOrgSelect.tld"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作人员列表</title>
<%@ include file="../shared/importCss.jsp"%>
<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>" type="text/css" />
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
								<form:form class="form-inline" modelAttribute="staffSearchVoModel" action="new_staff_list" method="get">
									<table class="table">
										<tr>
											<td>选择门店:</td>
											<td><orgSelectTag:select selectId="${staffSearchVoModel.parentId }" sessionOrgId="${loginOrgId }" /></td>
											<td>选择云店:</td>
											<td><select name="orgId" id="orgId" class="form-control">
													<option value="0">全部</option>
												</select></td>
											<td>
												<button type="submit" class="btn btn-primary">搜索</button>
											</td>
											<td>
												<button onClick="btn_add('newbs/new_staff_form?orgStaffId=0')" class="btn btn-primary" type="button">
													<i class="icon-expand-alt"></i>
													新增
											</td>
											</button>
										</tr>
										<tr>
											<td>手机号:</td>
											<td><form:input path="mobile" class="form-control" type="test" /></td>
											<td>姓名:</td>
											<td><form:input path="name" class="form-control" type="text" /></td>
											<td>是否可用：</td>
											<td colspan="2"><form:radiobutton path="status" value="0" />否 <form:radiobutton path="status" value="1" />是
											</td>
										</tr>
									</table>
								</form:form>
							</header>
							<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
							<header class="panel-info">
								<h4>工作人员列表</h4>
								<div class="pull-right"></div>
							</header>
							<table class="table table-striped table-advance table-hover">
								<thead>
									<tr>
										<th>员工头像</th>
										<th>门店</th>
										<th>姓名</th>
										<th>性别</th>
										<th>出生日期</th>
										<th>籍贯</th>
										<th>身份证号</th>
										<th>实名认证</th>
										<th>电话号码</th>
										<th>是否可用</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${staffModel.list}" var="orgStaff">
										<tr>
											<td><img id="head_img" src="${ orgStaff.headImg }" width="60px" height="60px"
													onerror="this.onerror=null;this.src='/jhj-oa/upload/headImg/default-head-img.png'"></td>
											<td>${ orgStaff.parentOrgName }-${ orgStaff.orgName }</td>
											<td>${ orgStaff.name }</td>
											<td><c:choose>
													<c:when test="${ orgStaff.sex == 0}">
														男
												</c:when>
													<c:when test="${ orgStaff.sex == 1}">
														女
												</c:when>
												</c:choose></td>
											<td><fmt:formatDate value="${ orgStaff.birth}" pattern="yyyy-MM-dd" /></td>
											<td>${ orgStaff.nativePlace }</td>
											<td>${ orgStaff.cardId }</td>
											<td><c:if test="${orgStaff.isAuthIdCard == 0 }">
													
													<input type="button" value="未认证" onclick="openAuthView(${orgStaff.staffId})" class="btn btn-danger" >
													
												</c:if> <c:if test="${orgStaff.isAuthIdCard == 1 }">
													<input type="button" value="已认证" onclick="openAuthView(${orgStaff.staffId})" class="btn btn-success" >
													
												</c:if> <c:if test="${orgStaff.isAuthIdCard == 2 }">
													<input type="button" value="未认证" onclick="openAuthView(${orgStaff.staffId})" class="btn btn-danger" >
												</c:if></td>
											<td>${ orgStaff.mobile }</td>
											<td><c:choose>
													<c:when test="${ orgStaff.status == 0}">
														<font style="color: red">不可用</font>
													</c:when>
													<c:when test="${ orgStaff.status == 1}">
														可用
												</c:when>
												</c:choose>
											<td>
												<button id="btn_update" onClick="btn_update('newbs/new_staff_form?orgStaffId=${orgStaff.staffId}')"
													class="btn btn-primary btn-xs" title="修改">
													<i class="icon-pencil"></i>
												</button>
												<button onClick="btn_update('orderCanlendar/order-scheduling?org_staff_id=${orgStaff.staffId}')"
													class="btn btn-info">排班</button>
												</button>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</section>
						<%@ include file="../shared/importJs.jsp"%>
						<c:import url="../shared/paging.jsp">
							<c:param name="pageModelName" value="staffModel" />
							<c:param name="urlAddress" value="/newbs/new_staff_list" />
						</c:import>
					</div>
				</div>
			</section>
		</section>
		<div class="modal fade" id="idAuthViewModal" tabindex="-1" role="dialog" aria-labelledby="idAuthViewModalLabel"
			aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">服务人员认证信息</h4>
					</div>
					<div class="modal-body"></div>

				</div>
			</div>
		</div>
		<%@ include file="../shared/pageFooter.jsp"%>
	</section>
	<script type="text/javascript" src="<c:url value='/js/jhj/select-org-cloud.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jhj/bs/newStaffList.js'/>"></script>
</body>
</html>
