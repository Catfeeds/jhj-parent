<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<html>
  <head>
    <!--common css for all pages-->
	<%@ include file="../shared/importCss.jsp"%>
  </head>

  <body class="login-body">

    <div class="container">

      <form:form modelAttribute="contentModel" class="form-signin" method="POST">
        <h2 class="form-signin-heading">家和家-运营平台</h2>
        <div class="login-wrap">
            <form:input path="username" class="form-control placeholder-no-fix" autocomplete="off" placeholder="用户名"/>
			
			<form:password path="password" class="form-control placeholder-no-fix" autocomplete="off" placeholder="密码"/>
			
            <label class="checkbox">
                <input type="checkbox" value="remember-me"> 记住我
            </label>
            <button class="btn btn-lg btn-login btn-block" type="submit">登录</button>
        </div>
      </form:form>
    </div>

    <!-- js placed at the end of the document so the pages load faster -->
    <!--common script for all pages-->
    <%@ include file="../shared/importJs.jsp"%>
	<script src="<c:url value='/assets/jquery.validate.min.js'/>" type="text/javascript"></script>
	<script src="<c:url value='/js/jhj/login.js'/>" type="text/javascript"></script>
  </body>
</html>