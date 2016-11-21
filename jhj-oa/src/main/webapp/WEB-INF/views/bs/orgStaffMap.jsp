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
	width: 200px;
	background: #000;
	height: 50%;
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
	width: 160px;
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
										<td>选择门店:</td>
										<td><orgSelectTag:select selectId="${sessionOrgId }" sessionOrgId="${loginOrgId }" /></td>
										<td>选择云店:</td>
										<td><select name="orgId" id="orgId" class="form-control">
												<option value="0">全部</option>
											</select></td>
										<td>姓名:</td>
										<td><input id="name" name="name" value="" class="form-control" type="text" /></td>
										<td>状态:</td>
										<td><select name="status" id="status" class="form-control">
												<option value="0">全部</option>
												<option value="1">在线(红色)</option>
												<option value="2">在途中(蓝色)</option>
												<option value="3">服务中(绿色)</option>
											</select></td>
										<td>
											<button type="button" onclick="loadStaffMapDatas()" class="btn btn-primary">搜索</button>
										</td>
									</tr>
									
									<tr>
										<td>日期:</td>
										<td>
											<input id="serviceDateStr" name="serviceDateStr" value="" class="form-control form_datetime"
																	style="width: 170px; margin-bottom: 0" readonly="true" />
										
										</td>
										
										<td>手机号:</td>
										<td>
											<input id="mobile" name="mobile" value="" class="form-control" type="text" />
										
										</td>
										<td colspan="4">
											<button type="button" onclick="loadStaffTrail()" class="btn btn-primary">轨迹查询</button>
										</td>
									</tr>
								</table>
							</form:form>
							<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
							<div class="panel-body">
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
	<script type="text/javascript" src="<c:url value='/js/jhj/select-org-cloud.js'/>"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=2sshjv8D4AOoOzozoutVb6WT"></script>
	<script type="text/javascript" src="<c:url value='/js/jhj/bs/orgStaffMap.js'/>"></script>
</body>
</html>
