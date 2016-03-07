<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<!-- BEGIN BODY -->
<body class="page-header-fixed">


	<!-- BEGIN CONTAINER -->
	<div class="row">
		<div id="totalChart" style="height: 400px"></div>
	</div>
	<!-- END PAGE CONTENT-->

	<script type="text/javascript">
		$(function() {
			App.init();
		});
	</script>
	<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
	<script src="<c:url value='/js/jhj/user/userStat.js'/>"
		type="text/javascript"></script>

	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>