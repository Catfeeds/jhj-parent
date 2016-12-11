<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ include file="../shared/taglib.jsp"%>

<html>
<head>
<title>订单来源统计</title>

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
							<form:option value="12">按季度统计</form:option>
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
						<th>时间</th>
						<th>微网站</th>
						<th>来电订单</th>
						<th>淘宝</th>
						<th>京东</th>
						<th>到位</th>
						<th>糯米</th>
						<th>国安社区</th>
						<th>大众点评</th>
						<th>美团</th>
						<th>葡萄生活</th>
						<th>居然之家</th>
						<th>百度</th>
						<th>朝阳门店</th>
						<th>海淀门店</th>
						<th>包月定制</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${chartDatas.tableDatas}" var="item">
					<tr>
						<td>${item.series}</td>
						<td>${item.微网站}</td>
						<td>${item.来电订单}</td>
						<td>${item.淘宝}</td>
						<td>${item.京东}</td>
						<td>${item.到位}</td>
						<td>${item.糯米}</td>
						<td>${item.国安社区}</td>
						<td>${item.大众点评}</td>
						<td>${item.美团}</td>
						<td>${item.葡萄生活}</td>
						<td>${item.居然之家}</td>
						<td>${item.百度}</td>
						<td>${item.朝阳门店}</td>
						<td>${item.海淀门店}</td>
						<td>${item.包月定制}</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<br/>
			<table class="table table-striped table-advance table-hover table2excel" id="table2excel">
				<thead>
					<tr>
						<th>时间</th>
						<th>微网站金额</th>
						<th>来电订单金额</th>
						<th>淘宝金额</th>
						<th>京东金额</th>
						<th>到位金额</th>
						<th>糯米金额</th>
						<th>国安社区金额</th>
						<th>大众点评金额</th>
						<th>美团金额</th>
						<th>葡萄生活金额</th>
						<th>居然之家金额</th>
						<th>百度金额</th>
						<th>朝阳门店金额</th>
						<th>海淀门店金额</th>
						<th>包月定制金额</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${chartDatas.tableDatas}" var="item">
					<tr>
						<td>${item.series}</td>
						<td>${item.微网站金额}</td>
						<td>${item.来电订单金额}</td>
						<td>${item.淘宝金额}</td>
						<td>${item.京东金额}</td>
						<td>${item.到位金额}</td>
						<td>${item.糯米金额}</td>
						<td>${item.国安社区金额}</td>
						<td>${item.大众点评金额}</td>
						<td>${item.美团金额}</td>
						<td>${item.葡萄生活金额}</td>
						<td>${item.居然之家金额}</td>
						<td>${item.百度金额}</td>
						<td>${item.朝阳门店金额}</td>
						<td>${item.海淀门店金额}</td>
						<td>${item.包月定制金额}</td>
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
	<script type="text/javascript" src="<c:url value='/js/chart/chartOrderSrc.js'/>"></script>
	<script>
		var legend = ${chartDatas.legend};
		var xAxis = ${chartDatas.xAxis};
		var seriesDatas = ${chartDatas.series};
		loadOrderChart(legend, xAxis, seriesDatas);
	</script>
	
</body>
</html>
