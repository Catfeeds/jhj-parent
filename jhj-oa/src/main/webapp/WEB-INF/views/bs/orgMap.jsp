<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../shared/taglib.jsp"%>


<html>
<head>
<!--common css for all pages-->
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
</style>
</head>
<body>
	<section id="container"> <!--header start--> <%@ include file="../shared/pageHeader.jsp"%>
	<!--header end--> <!--sidebar start--> <%@ include file="../shared/sidebarMenu.jsp"%> <!--sidebar end-->
	<!--main content start--> <section id="main-content"> <section class="wrapper"> <!-- page start-->
	<div class="row">
		<div class="col-lg-12">
			<section class="panel"> <header class="panel-heading"> 门店管理 </header>
			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
			<div class="panel-body">
				<div id="allmap"></div>
			</div>
			</section>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include file="../shared/pageFooter.jsp"%>
	<!--footer end--> </section>
	<%@ include file="../shared/importJs.jsp"%>
	<!--script for this page-->
	<!-- 引入百度地图API,其中   申请的密钥   ak 和主机 ip绑定， -->
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=2sshjv8D4AOoOzozoutVb6WT">
		
	</script>
	<script type="text/javascript" src="<c:url value='/js/jhj/bs/orgMap.js'/>"></script>	
</body>
</html>
