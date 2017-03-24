<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../shared/taglib.jsp"%>
<html>
<head>
<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>
</head>
<body class="login-body">
	<!--header start-->
	<%@ include file="../shared/pageHeader.jsp"%>
	<!--header end-->
	<!--sidebar start-->
	<%@ include file="../shared/sidebarMenu.jsp"%>
	<!--sidebar end-->
	<div class="container">
		<form:form modelAttribute="contentModel" class="form-signin" method="POST">
			<h2 class="form-signin-heading"></h2>
			<div class="login-wrap">
				<div class="alert alert-success alert-block fade in">
					
					<h4>
						<i class="icon-ok-sign"></i>
						操作成功
					</h4>
					<p>点击查看操作结果</p>
				</div>
				<a href="#" class="btn btn-lg btn-login btn-block" onclick="btn_select('${nextUrl}')">查看</a>
			</div>
		</form:form>
	</div>
	<!-- js placed at the end of the document so the pages load faster -->
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>
</body>
</html>