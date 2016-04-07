<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<%@ taglib prefix="roleSelectTag" uri="/WEB-INF/tags/roleSelect.tld"%>
<html>
<head>

<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>

<!--css for this page-->
<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>"
	type="text/css" />


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
			<header class="panel-heading">
			商户信息管理 </header>

			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form  class="form-horizontal" commandName="cooBusinessModel"
					method="POST" action="coo_business_form"  id="cooBusForm">

					<form:hidden path="id" />
					<div class="form-body">
					
						<div class="form-group">
							<label class="col-md-2 control-label">商户名称</label>
							<div class="col-md-5">
								<form:input path="businessName" class="form-control" 
											placeholder="请输入商户名称,不超过50字" 
											maxlength="50" />
								<form:errors path="businessName" class="field-has-error"/>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">商户应用名称</label>
							<div class="col-md-5">
								<form:input path="appName" class="form-control" 
											placeholder="商户应用名称,不超过50字,如**公司的**app" 
											maxlength="50" />
								<form:errors path="appName" class="field-has-error"/>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">商户登录名称</label>
							<div class="col-md-5">
								<form:input path="businessLoginName" class="form-control " autocomplete="off"
											placeholder="商户登录名,不超过20字" 
											maxlength="20" />
								<form:errors path="businessLoginName"  class="field-has-error"/>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">登录密码</label>
							<div class="col-md-5">
								<form:input path="businessPassWord" type="password" class="form-control" autocomplete="off"
											maxlength="50" placeholder="请输入密码,不超过50个字符"/>
								<form:errors path="businessPassWord"  class="field-has-error" />
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">确认密码</label>
							<div class="col-md-5">
								<form:input path="confirmPassWord" type="password" class="form-control" autocomplete="off"
											maxlength="50" placeholder="请输入确认密码"/>
								<form:errors path="confirmPassWord" class="field-has-error"/>
							</div>
						</div>
						
						
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">商户角色</label>
							<div class="col-sm-5">
								<roleSelectTag:select selectId="${cooBusinessModel.roleId }"/>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">是否可用</label>
							<div class="col-md-8">
								<form:radiobutton path="enable" value="0" label=" 否" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<form:radiobutton path="enable" value="1" label=" 是" />
							</div>
						</div>

						<div class="form-actions fluid">
							<div class="col-md-offset-6 col-md-6">
								<button type="submit" id="coopBusForm_btn"
									class="btn btn-success">保存</button>
							</div>
						</div>
				</form:form>
			</div>
			</section>
		</div>
	</div>
	<!-- page end--> 
	</section> </section> 
	<!--main content end--> 
	<!--footer start--> 
	<%@ include file="../shared/pageFooter.jsp"%> 
	<!--footer end-->
	</section>

	<%@ include file="../shared/importJs.jsp"%>

	<script
		src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"
		type="text/javascript"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-fileupload/fileinput.min.js'/>"></script>
	
	<script type="text/javascript" src="<c:url value='/js/jhj/cooperate/coopForm.js'/> "></script>
</body>
</html>
