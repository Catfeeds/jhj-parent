<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="../shared/taglib.jsp"%>
<%@ include file="../shared/importCss.jsp"%>
<%@ taglib prefix="cloudOrgSelectTag" uri="/WEB-INF/tags/CloudOrgSelect.tld"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
<%@ taglib prefix="leaveDuraSelectTag" uri="/WEB-INF/tags/LeaveDuartionSelect.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<!-- common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>
<link rel="stylesheet" href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>"
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
		<section id="main-content">
			<section class="wrapper">
				<!-- page start-->
				<div class="row">
					<div class="col-lg-12">
						<section class="panel">
							<header class="panel-heading"> 请假管理 </header>
							<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
							<div class="panel-body">
								<form:form modelAttribute="leaveModel" class="form-horizontal" method="POST" action="leave_form" id="leaveForm"
									enctype="multipart/form-data">
									<form:hidden path="id" />
									<input type="hidden" id="adminId" name="adminId" value="${accountAuth.id}" />
									<input type="hidden" id="logInParentOrgId" value="${logInParentOrgId }">
									<div class="form-body">
										<div class="form-group ">
											<div class="col-md-5">
												<font color="red">当前时间如果是 "假期中",则不能修改</font>
											</div>
										</div>
										<div class="form-group required">
											<label class="col-md-2 control-label">选择门店*</label>
											<div class="col-md-5">
												<orgSelectTag:select selectId="${leaveModel.parentId }" sessionOrgId="${loginOrgId }" />
											</div>
										</div>
										<div class="form-group required">
											<label class="col-md-2 control-label">选择云店*</label>
											<div class="col-md-5">
												<%-- <cloudOrgSelectTag:select selectId="${leaveModel.orgId }" logInParentOrgId="${logInParentOrgId }"/> --%>
												<c:if test="${leaveModel.orgId>0 }">
													<input name="orgName" value="${leaveModel.cloudOrgName }" class="form-control" readonly="readonly" />
												</c:if>
												<c:if test="${leaveModel.orgId==0 }">
													<select name="orgId" id="orgId" class="form-control">
														<c:if test="${leaveModel.cloudOrgName!=null }">
															<option value="${leaveModel.orgId}">${leaveModel.cloudOrgName }</option>
														</c:if>
														<c:if test="${leaveModel.cloudOrgName==null }">
															<option value="0">全部</option>
														</c:if>
													</select>
												</c:if>
											</div>
										</div>
										<input type="hidden" id="staffSelectedId" value="${leaveModel.staffId}">
										<div class="form-group required">
											<label class="col-md-2 control-label">选择服务人员*</label>
											<div class="col-md-5">
												<c:if test="${leaveModel.staffId>0 }">
													<input name="staffName" readonly="readonly" class="form-control" value="${leaveModel.staffName }" />
												</c:if>
												<c:if test="${leaveModel.staffId==0 }">
													<select name="staffId" path="staffId" id="staffId" class="form-control">
														<option value="">请选择服务人员</option>
													</select>
												</c:if>
											</div>
										</div>
										<div class="form-group required">
											<label class="col-md-2 control-label">请假开始日期*</label>
											<div class="col-md-5">
												<div class="input-group date">
													<fmt:formatDate var='leaveDate' value='${leaveModel.leaveDate}' type='both' pattern="yyyy-MM-dd" />
													<input type="text" id="leaveDate" name="leaveDate" value="${leaveDate}" onChange="leaveDateChange()"  readonly class="form-control form_datetime">
													<span class="input-group-addon">
														<i class="glyphicon glyphicon-th"></i>
													</span>
												</div>
											</div>
										</div>
										<div class="form-group required">
											<label class="col-md-2 control-label">请假结束日期*</label>
											<div class="col-md-5">
												<div class="input-group date">
													<fmt:formatDate var='leaveDateEnd' value='${leaveModel.leaveDateEnd}' type='both' pattern="yyyy-MM-dd" />
													<input type="text" id="leaveDateEnd" name="leaveDateEnd" value="${leaveDateEnd}" onChange="leaveDateChange()" readonly class="form-control form_datetime">
													<span class="input-group-addon">
														<i class="glyphicon glyphicon-th"></i>
													</span>
												</div>
											</div>
										</div>
										
										<div class="form-group required" id="halfDayDiv">
											<label class="col-md-2 control-label">全天/半天*</label>
											<div class="col-md-5">
												
												<select id="halfDay" name="halfDay" class="form-control">
													<option value="0">全天</option>
													<option value="1">上午</option>
													<option value="2">下午</option>
												</select>

											</div>
										</div>
										
										<div class="form-group required">
											<label class="col-md-2 control-label">请假状态*</label>
											<div class="col-md-5">
												<div class="input-group date">
													<input type="radio" id="leaveStatus" name="leaveStatus" value="1" <c:if test='${leaveModel.leaveStatus==1 }'>checked</c:if> />
												
													请假中
													<input type="radio" id="leaveStatus" name="leaveStatus" value="2" <c:if test='${leaveModel.leaveStatus==2 }'>checked</c:if> />
													请假取消
												</div>
											</div>
										</div>
										
										<input type="hidden" name="leaveDuration" value="1" />
										<div class="form-group required">
											<label class="col-md-2 control-label">备注</label>
											<div class="col-md-5">
												<form:textarea path="remarks" cols="57" rows="5" placeholder="不超过200字" maxlength="200" />
											</div>
										</div>
										
										<div class="form-actions fluid">
											<div class="col-md-offset-6 col-md-6" style="margin-left: 315px">
												<button type="button" class="btn btn-success" onclick="leaveSave()">保存</button>
											</div>
										</div>
									</div>
								</form:form>
							</div>
						</section>
					</div>
				</div>
				<!-- page end-->
			</section>
		</section>
		<!--main content end-->
		<!--footer start-->
		<%@ include file="../shared/pageFooter.jsp"%>
		<!--footer end-->
	</section>
	<%@ include file="../shared/importJs.jsp"%>
	<script type="text/javascript" src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery.chained.remote.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/moment/moment-with-locales.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jhj/select-org-cloud.js'/>"></script>
	<!-- 选择云店，动态选择服务人员，联动 -->
	<script type="text/javascript" src="<c:url value='/js/jhj/cloudSelect-staff.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jhj/bs/leaveForm.js'/>"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$("#orgId").trigger("change");
		});
	</script>
</body>
</html>
