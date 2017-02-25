<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
<%@ taglib prefix="cloudOrgSelectTag" uri="/WEB-INF/tags/CloudOrgSelect.tld"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作人员地图展现</title>
<%@ include file="../shared/importCss.jsp"%>
<!--css for this page-->
<link href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>" rel="stylesheet"
	type="text/css" />
<style type="text/css">
body, html {
	width: 100%;
	height: 100%;
	margin: 0;
	font-family: "微软雅黑";
}

#allmap {
	width: 100%;
	height: 400px;
}

.baidu-maps label {
	max-width: none;
}

.houtai-waiter-list {
	width: 160px;
	background: #000;
	height: 65%;
	overflow: hidden;
	position: fixed;
	top: 210px;
	left: 241px;
}

.houtai-waiter-list p {
	margin: 0;
	width: 90%;
	margin-left: 5%;
	background: #fff;
	color: #000;
	height: 40px;
	text-align: center;
	line-height: 40px;
	margin-top: 15px;
	border-radius: 5px;
}

.houtai-waiter-list ul {
	width: 120px;
	height: 80%;
	margin: 0;
	margin-left: 20px;
	margin-top: 15px;
	overflow: auto;
	padding: 0;
}

.houtai-waiter-list ul li {
	width: 90%;
	text-align: left;
	line-height: 30px;
	border-bottom: 1px solid #fff;
	color: #fff;
	list-style-type: disc;
}

::-webkit-scrollbar {
	width: 5px;
}
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
						<section class="panel">
							<form:form class="form-inline" action="" method="get">
								<table class="table">
									<tr>
										<td>门店:</td>
										<td><orgSelectTag:select selectId="${sessionOrgId }" sessionOrgId="${loginOrgId }" /></td>
										<td>云店:</td>
										<td><select name="orgId" id="orgId" class="form-control">
												<option value="0">全部</option>
											</select></td>
										<td>状态:</td>
										<td><select name="status" id="status" class="form-control">
												<option value="0">全部</option>
												<option value="1">在线(红色)</option>
												<option value="2">在途中(蓝色)</option>
												<option value="3">服务中(绿色)</option>
											</select></td>
										<td>
											<button type="button" onclick="loadStaffMapDatas()" class="btn btn-primary">实时定位</button>
										</td>
									</tr>
									<tr>
										<td>日期:</td>
										<td><input id="serviceDateStr" name="serviceDateStr" value="${today}" class="form-control form_datetime"
												style="width: 170px; margin-bottom: 0" readonly="true" /></td>
										<td>姓名:</td>
										<td><select name="selectStaff" id="selectStaff" class="form-control">
												<option value="0">全部</option>
											</select></td>
										<td>合并范围(单位:米):</td>
										<td><input id="mergeDistance" name="mergeDistance" value="2000" class="form-control" type="text" /></td>
										<td colspan="2">
											<button type="button" onclick="loadStaffRoute()" class="btn btn-primary">轨迹查询</button>
										</td>
										
										<td colspan="2">
											<button type="button" id="btn-fullscreen" class="btn btn-warning">全屏地图</button>
										</td>
									</tr>
								</table>
							</form:form>
							<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
							<div class="panel-body">
								<table class="table table-striped table-advance table-hover" id="order-list-table" style="display:none">
									<thead>
										<tr>
											<th width="10%">服务人员</th>
											<th>人数</th>
											<th>订单类型</th>
											<th>服务日期</th>
											<th>时长</th>
											<th width="10%">地址</th>
											<th>订单来源</th>
											<th>订单状态</th>
											<th>支付方式</th>
											<th>支付金额</th>
										</tr>
									</thead>
									<tbody id="order-list-tbody">
									</tbody>
								</table>
								<div class="baidu-maps" id="allmap"></div>
								<div class="houtai-waiter-list" id="offline-div">
									<p>不在线人员名单</p>
									<ul id="offline-list">
									</ul>
								</div>
							</div>
						</section>
					</div>
				</div>
			</section>
		</section>
		<%@ include file="../shared/pageFooter.jsp"%>
	</section>
	<%@ include file="../shared/importJs.jsp"%>
	<!-- 引入百度地图API,其中   申请的密钥   ak 和主机 ip绑定， -->
	<script src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
	<script src="<c:url value='/assets/screenfull.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jhj/select-org-cloud.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jhj/select-staff.js'/>"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=2sshjv8D4AOoOzozoutVb6WT"></script>
	<script type="text/javascript" src="<c:url value='/js/jhj/bs/orgStaffMap.js'/>"></script>
</body>
</html>
