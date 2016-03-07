<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>

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
			用户管理 </header>

			<hr
				style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="adminAccount" action="adminForm"
					id="admin-form" class="form-horizontal " method="POST">
					<form:hidden path="id" />
					<div class="form-group">
						<label class="col-sm-2 col-sm-2 control-label">姓名</label>
						<div class="col-sm-5">
							<form:input path="name" class="form-control placeholder-no-fix"
								autocomplete="off" placeholder="姓名" />
							<br />
							<form:errors path="name" class="field-has-error"></form:errors>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 col-sm-2 control-label">昵称</label>
						<div class="col-sm-5">
							<form:input path="nickname"
								class="form-control placeholder-no-fix" autocomplete="off"
								placeholder="昵称" />
							<br />
							<form:errors path="nickname" class="field-has-error"></form:errors>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 col-sm-2 control-label">邮箱</label>
						<div class="col-sm-5">
							<form:input path="email" class="form-control placeholder-no-fix"
								autocomplete="off" placeholder="邮箱" />
							<br />
							<form:errors path="email" class="field-has-error"></form:errors>

						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 col-sm-2 control-label">用户名</label>
						<div class="col-sm-5">
							<form:input path="username"
								class="form-control placeholder-no-fix" autocomplete="off"
								placeholder="用户名" />
							<br />
							<form:errors path="username" class="field-has-error"></form:errors>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 col-sm-2 control-label">密码</label>
						<div class="col-sm-5">
							<form:password path="password"
								class="form-control placeholder-no-fix" autocomplete="off"
								placeholder="密码" />
							<br />
							<form:errors path="password" class="field-has-error"></form:errors>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 col-sm-2 control-label">确认密码</label>
						<div class="col-sm-5">
							<form:password path="confirmPassword"
								class="form-control placeholder-no-fix" autocomplete="off"
								placeholder="确认密码" />
							<br />
							<form:errors path="confirmPassword" class="field-has-error"></form:errors>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 col-sm-2 control-label">绑定角色</label>
						<div class="col-sm-5">
							<form:select path="roleId" class="form-control">
								<option value="">请选择</option>
								<form:options items="${selectDataSource}" />
							</form:select>
						</div>
					</div>
					
					<div class="form-group"  id="orgSelect">
						<label class="col-sm-2 col-sm-2 control-label">绑定门店</label>
						<div class="col-sm-5">
							<orgSelectTag:select selectId="${orgId }" />
						</div>
					</div>
					
					<div class="form-actions fluid">

						<div class="col-md-offset-3">
							<button type="button" id="btn_submit" class="btn btn-success">保存</button>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<button class="btn btn-success" type="reset">重置</button>
						</div>
					</div>

				</form:form>
			</div>
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
	<script
		src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"
		type="text/javascript"></script>
	<script src="<c:url value='/js/jhj/admin/adminForm.js'/>"
		type="text/javascript"></script>
</body>
</html>
