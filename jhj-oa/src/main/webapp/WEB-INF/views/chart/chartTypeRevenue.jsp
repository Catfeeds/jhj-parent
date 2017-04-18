<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<html>
<head>
<title>品类收入图表</title>

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
			

				<form:form modelAttribute="searchVo" method="GET" >
					<div class="form-inline">
						时间周期：
						<form:select path="selectCycle">
							<form:option value="1">最近一个月</form:option>
							<form:option value="3">最近三个月</form:option>
							<form:option value="6">最近半年</form:option>
							<form:option value="12">最近一年</form:option>
						</form:select> 
						<form:hidden path="searchType" value="0"/>
						<input type="submit"  value="搜索"  >
					</div>
				</form:form>
			
				<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
			
				<form:form modelAttribute="searchVo" method="GET" onsubmit="return checkEndTime()">
					<div class="form-inline">
						开始时间：
							<form:input path="startTimeStr" class="form-control form_datetime" style="width:110px;" readonly="true"  />
						结束时间：
							<form:input path="endTimeStr" class="form-control form_datetime" style="width:110px;" readonly="true" />

						<form:hidden path="searchType" value="1"/>
						<input type="submit"  value="搜索"  >
					</div>
				</form:form>
			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />			
			
			<div id="main" style="height: 400px; width: 950px"></div>
			
			<button id="exportExcel" class="btn btn-success">导出Excel</button></div>

			<table class="table table-striped table-advance table-hover table2excel" id="table2excel">
				<thead>
					<tr>
						<th>总单数</th>
						<th>总营业额(元)</th>
						<th>基础服务(单)</th>
						<th>基础服务营业额(元)</th>
						<th>基础服务营业额占比</th>
						<th>深度服务(单)</th>
						<th>深度服务营业额(元)</th>
						<th>深度服务营业额占比</th>
						<th>母婴到家(单)</th>
						<th>母婴到家营业额(元)</th>
						<th>母婴到家营业额占比</th>
					</tr>
				</thead>
				<tbody>
			<%-- 	<c:forEach items="${chartDatas.tableMap}" var="item"> --%>
					<tr>
						<%-- <td>${item.series}</td> --%>
						<td style="text-align: right;">${chartDatas.tableMap.总单数}</td>
						<td style="text-align: right;">${chartDatas.tableMap.总营业额}</td>
						<td style="text-align: right;">
							<a href="../order/order-hour-list">
								${chartDatas.tableMap.基础服务}
							</a>
						</td>
						<td style="text-align: right;">${chartDatas.tableMap.基础服务营业额}</td>
						<td style="text-align: right;">${chartDatas.tableMap.基础服务营业额占比}</td>
						<td style="text-align: right;">
							<a href="../order/order-exp-list">
								${chartDatas.tableMap.深度服务}
							</a>
						</th>
						<td style="text-align: right;">${chartDatas.tableMap.深度服务营业额}</td>
						<td style="text-align: right;">${chartDatas.tableMap.深度服务营业额占比}</td>
						<td style="text-align: right;">
							<a href="../order/order-exp-baby-list">
								${chartDatas.tableMap.母婴到家}
							</a>
						</td>
						<td style="text-align: right;">${chartDatas.tableMap.母婴到家营业额}</td>
						<td style="text-align: right;">${chartDatas.tableMap.母婴到家营业额占比}</td>
					</tr>
				<%-- </c:forEach> --%>
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
	<script type="text/javascript" src="<c:url value='/js/chart/chartTypeRevenue.js' />"></script>
	<script>
		var legend = ${chartDatas.legend};
		var xAxis = ${chartDatas.xAxis};
		var seriesDatas = ${chartDatas.series};
		loadOrderChart(legend, xAxis, seriesDatas);
	</script>
</body>
</html>
