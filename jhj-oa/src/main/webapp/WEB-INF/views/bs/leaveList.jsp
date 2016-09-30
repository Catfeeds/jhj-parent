<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../shared/taglib.jsp"%>
<%@ include file="../shared/importCss.jsp"%>
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
<html>
<head>
<title>请假信息列表</title>
<!-- common css for all pages -->
<%@ include file="../shared/importCss.jsp"%>
<!-- css for this page -->
<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>" type="text/css" />
<link href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>" rel="stylesheet"
	type="text/css" />
</head>
<body>
	<section id="container"> <!--header start--> <%@ include file="../shared/pageHeader.jsp"%>
	<!--header end--> <!--sidebar start--> <%@ include file="../shared/sidebarMenu.jsp"%> <!--sidebar end-->
	<!--main content start--> <section id="main-content"> <section class="wrapper"> <!-- page start-->
	<div class="row">
		<div class="col-lg-12">
			<section class="panel"> <form:form class="form-inline" modelAttribute="leaveSearchVoModel"
				action="leave_list" method="GET">
				<header class="panel-info">
				<table class="table">
					<tr>
						<td>选择门店:</td>
						<td><orgSelectTag:select selectId="${searchModel.parentId }" sessionOrgId="${loginOrgId }" /></td>
						<td>选择云店:</td>
						<td><select name="orgId" id="orgId" class="form-control">
								<option value="0">全部</option>
							</select></td>
						<td>服务人员手机号:</td>
						<td><form:input path="mobile" maxlength="11" class="form-control" /></td>
						<td>请假时间:</td>
						<td><form:input path="leaveDateStr" class="form-control form_datetime" style="width:110px; margin-bottom:0"
								readonly="true" /></td>
								
						<td>
							<button type="submit" class="btn btn-primary">搜索</button>
							<button type="reset" class="btn btn-primary">重置</button>
						</td>
					</tr>
					
				</table>
				</header>
			</form:form>
			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
			<h4>请假信息</h4>
			<div class="pull-right">
				<button onClick="btn_add('newbs/leave_form?id=0')" class="btn btn-primary" type="button">
					<i class="icon-expand-alt"></i>
					新增
				</button>
			</div>
			</header>
			<table class="table table-striped table-advance table-hover">
				<thead>
					<tr>
						<th>云店</th>
						<th>服务人员姓名</th>
						<th>服务人员手机号</th>
						<th>请假申请时间</th>
						<th>假期时间</th>
						<th>员工状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${leaveModel.list}" var="item">
						<tr>
							<td>${ item.cloudOrgName }</td>
							<td>${ item.staffName }</td>
							<td>${ item.staffMobile}</td>
							<td><timestampTag:timestamp patten="yyyy-MM-dd" t="${item.addTime * 1000}" /></td>
							<td>${item.leaveDateStr }</td>
							<td><c:if test="${item.leaveStatus == 0 }">
									<font color="green">假期未开始</font>
								</c:if> <c:if test="${item.leaveStatus == 1 }">
									<font color="red">假期中</font>
								</c:if> <c:if test="${item.leaveStatus == 2 }">
											假期已结束
										</c:if></td>
							<td><c:choose>
									<c:when test="${item.leaveStatus == 1 }">
												 假期中不能修改												
											</c:when>
									<c:otherwise>
										<button id="btn_update" onClick="btn_update('newbs/leave_form?id=${item.id}')" class="btn btn-primary btn-xs"
											title="修改">
											<i class="icon-pencil"></i>
										</button>
									</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</section>
			<%@ include file="../shared/importJs.jsp"%>
			<c:import url="../shared/paging.jsp">
				<c:param name="pageModelName" value="leaveModel" />
				<c:param name="urlAddress" value="/newbs/leave_list" />
			</c:import>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include file="../shared/pageFooter.jsp"%>
	<!--footer end--> </section>
	
	<script type="text/javascript" src="<c:url value='/js/jhj/select-org-cloud.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
	<script type="text/javascript">
		$('.form_datetime').datepicker({
			format : "yyyy-mm-dd",
			language : "zh-CN",
			autoclose : true,
			startView : 1,
			todayBtn : true
		});
		
		$("#parentId").trigger("change");
	</script>
</body>
</html>