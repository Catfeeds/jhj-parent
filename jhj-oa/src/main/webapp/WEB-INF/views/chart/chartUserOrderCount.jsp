<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<html>
<head>
<title>市场订单数据统计</title>

<!-- common css for all pages -->
<%@ include file="../shared/importCss.jsp"%>
<!-- css for this page -->
<link href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>" rel="stylesheet" type="text/css" />

<style>

</style>
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
			<form:form modelAttribute="searchVo" method="GET" onsubmit="return checkEndTime()">
				<div class="form-inline">
					<label>统计时间：</label>
					<form:input path="startTimeStr" class="form-control form-datetime" style="width:110px;" readonly="true"  />
					<input type="submit"  value="搜索"  >
				</div>
			</form:form>
			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />			
			
			<!-- <button id="exportExcel" class="btn btn-success">导出Excel</button></div> -->

			<table class="table table-bordered table-striped table-advance table-hover table2excel" id="table2excel">
				<thead>
					<tr><td colspan="23" >市场订单数据统计</td></tr>
					<tr>
						<td>日期</td>
						<td>美团 </td>
						<td>大众点评 </td>
						<td>到位 </td>
						<td>国安社区 </td>
						<td>千丁 </td>
						<td>格格小区 </td>
						<td>百度 </td>
						<td>360</td>
						<td>金色家园网 </td>
						<td>电询 </td>
						<td>糯米 </td>
						<td>居然之家 </td>
						<td>淘宝 </td>
						<td>京东 </td>
						<td>葡萄生活 </td>
						<td>北科 </td>
						<td>58同城 </td>
						<td>电梯广告 </td>
						<td>建功北里 </td>
						<td>社区 </td>
						<td>订单数量 </td>
						<td>订单金额 </td>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${chartDatas.tableDatas}" var="item">
					<tr>
						<td>${item.time}</td>
						<td>${item.美团 }</td>
						<td>${item.大众点评 }</td>
						<td>${item.到位 }</td>
						<td>${item.国安社区 }</td>
						<td>${item.千丁 }</td>
						<td>${item.格格小区 }</td>
						<td>${item.百度 }</td>
						<td>${item['360'] }</td>
						<td>${item.金色家园网 }</td>
						<td>${item.电询 }</td>
						<td>${item.糯米 }</td>
						<td>${item.居然之家 }</td>
						<td>${item.淘宝 }</td>
						<td>${item.京东 }</td>
						<td>${item.葡萄生活 }</td>
						<td>${item.北科 }</td>
						<td>${item['58同城']}</td>
						<td>${item.电梯广告 }</td>
						<td>${item.建功北里 }</td>
						<td>${item.社区 }</td>
						<td>${item.订单数量 }</td>
						<td>${item.订单金额 }</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>

		</div>
	</div>

	<!-- page end--> </section> </section> 
	<!--main content end--> 
	<!--footer start--> 
	<%@ include file="../shared/pageFooter.jsp"%>
	<!--footer end--> 
	</section>
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>
	<script type="text/javascript" src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
	
	<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
	<script type="text/javascript" src="<c:url value='/assets/jquery.table2excel.js'/>"></script>
	<%-- <script type="text/javascript" src="<c:url value='/js/chart/chartTypeRevenue.js' />"></script> --%>
	<!-- <script>
		var legend = ${chartDatas.legend};
		var xAxis = ${chartDatas.xAxis};
		var seriesDatas = ${chartDatas.series};
		loadOrderChart(legend, xAxis, seriesDatas);
	</script> -->
	
	<script type="text/javascript">
		$('.form-datetime').datepicker({
			format: 'yyyy-mm',
			language : "zh-CN",
			autoclose : true,
			startView: 1,  
			maxViewMode: 1,
			minViewMode:1,
			todayBtn : true
		});
	</script>
</body>
</html>
