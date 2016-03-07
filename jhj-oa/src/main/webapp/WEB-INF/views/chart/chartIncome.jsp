<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
</html>
<html lang="en" class="no-js">
<!--<![endif]-->
<head>
<meta charset="utf-8" content="" />
<title>Conquer | Form Stuff - Form Controls</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<meta name="MobileOptimized" content="320">

<%@ include file="../shared/importCss.jsp"%>
<%@ include file="../shared/importJs.jsp"%>
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script type="text/javascript" src="<c:url value='/js/app.js'/>"></script>
<!-- END PAGE LEVEL SCRIPTS -->

<link rel="shortcut icon" href="favicon.ico" />

</head>
<!-- END HEAD -->

<!-- BEGIN BODY -->
<body class="page-header-fixed">

	<%@ include file="../shared/pageHeader.jsp"%>

	<div class="clearfix"></div>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">

		<%@ include file="../shared/sidebarMenu.jsp"%>

		<!-- BEGIN PAGE -->
		<div class="page-content">

			<!-- BEGIN PAGE HEADER-->
			<%@ include file="../shared/pageContentHeader.jsp"%>
			<!-- END PAGE HEADER-->
			<div class="row">

				<div id="main"
					style="position: absolute; left: 280px; top: 100px; height: 400px; width: 950px"></div>

			</div>
			<table class="table table-striped table-advance table-hover"
				style="position: absolute; left: 360px; top: 480px; height: 180px; width: 800px">
				<thead>
					<tr>
						<th>时间</th>
						<th>增长率(%)</th>
						<th>微网站来源</th>
						<th>营业额（元）</th>
						<th>App来源</th>
						<th>营业额小计(元)</th>


					</tr>
				</thead>
				<tbody>
					<tr>
						<td>七月</td>
						<td>20</td>
						<td>782</td>
						<td>1021</td>
						<td>1010</td>
						<td>345</td>

					</tr>
					<tr>
						<td>八月</td>
						<td>10</td>
						<td>500</td>
						<td>1309</td>
						<td>1324</td>
						<td>456</td>
						<td>790</td>
					</tr>
					<tr>
						<td>九月</td>
						<td>34</td>
						<td>343</td>
						<td>1234</td>
						<td>1080</td>
						<td>390</td>
						<td>890</td>
					</tr>
				</tbody>
			</table>

			<!-- END PAGE CONTENT-->
		</div>
		<!-- END PAGE -->
	</div>

	<!-- END CONTAINER -->

	<script type="text/javascript">
		$(function() {
			App.init();
		});
	</script>

	<!--script for this page-->
	<!-- ECharts单文件引入 -->
	<script src="http://echarts.baidu.com/build/dist/echarts.js"
		type="text/javascript"></script>
	<script src="<c:url value='/js/chart/charOrder.js'/>"
		type="text/javascript"></script>

</body>

<!-- END BODY -->
</html>