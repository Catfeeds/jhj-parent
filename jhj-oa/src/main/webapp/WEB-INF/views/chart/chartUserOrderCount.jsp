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

			<table class="table table-bordered table-striped table-advance table-hover table2excel" id="table2excel">
				<thead>
					<tr>
						<td colspan="29" style="text-align:center">市场订单数据统计</td>
					</tr>
					<tr>
						<td rowspan="2">时间</td>
						<c:forEach items="${chartUserOrderVoList }" var="m">
							<td colspan="${m.count }">${m.name }</td>
						</c:forEach>
					</tr>
					<tr>
						<c:forEach items="${chartUserOrderVoList}" var="m">
							<c:forEach items="${m.bussineNameList }" var="business">
								<td>${business }</td>
							</c:forEach>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${chartDatas.tableDatas}" var="item">
						<tr>
							<td>${item.time }</td>
							<c:forEach items="${list }" var="b">
								<td>
									${item[b] }
								</td>
							</c:forEach>
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
