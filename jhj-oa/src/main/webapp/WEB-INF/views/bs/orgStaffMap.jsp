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
<style type="text/css">
body, html {
	width: 100%;
	height: 100%;
	margin: 0;
	font-family: "微软雅黑";
}

#allmap {
	width: 100%;
	height: 500px;
}
.baidu-maps label {
  max-width: none;
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
							<header class="panel-info">
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
												<button type="submit" class="btn btn-primary">搜索</button>
											</td>
											</button>
										</tr>
									</table>
								</form:form>
							</header>
							<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
							<div class="panel-body">
								<div class="baidu-maps" id="allmap"></div>
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
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=2sshjv8D4AOoOzozoutVb6WT">
	<script type="text/javascript" src="<c:url value='/js/jhj/select-org-cloud.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jhj/bs/orgStaffMap.js'/>"></script>
</body>
</html>
