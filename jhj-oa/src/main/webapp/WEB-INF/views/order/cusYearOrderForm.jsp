<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
<html>
<head>

<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>

<!--css for this page-->
<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>"
	type="text/css" />

<body>

	<section id="container"> <!--header start--> <%@ include
		file="../shared/pageHeader.jsp"%> <!--header end-->

	<!--sidebar start--> 
	<%@ include file="../shared/sidebarMenu.jsp"%>
	<!--sidebar end--> 
	<!--main content start--> 
	<section id="main-content">
	<section class="wrapper"> 
	<!-- page start-->
	<div class="row">
		<div class="col-lg-12">
			<section class="panel"> <header class="panel-heading">
			<h4>订单详情页 </h4></header>

			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="cusYearDetailVoModel" class="form-horizontal"
					method="POST"  enctype="multipart/form-data">
					
					<form:hidden path="id"/>
					<div class="form-body">
						<div class="form-group ">
						
							<label class="col-md-2 control-label">用户手机号</label>
							<div class="col-md-5">
								<form:input path="userMobile" class="form-control" readonly="true"/>
							</div>
						</div>
						
						<div class="form-group" >
	
							<label class="col-md-2 control-label">服务名称</label>
							<div class="col-md-5">
							
								<form:input path="serviceName" class="form-control" readonly="true"/>
							</div>
						</div>
						
						<div class="form-group">

							<label class="col-md-2 control-label">下单时间</label>
							<div class="col-md-5">
								<form:input path="addTimeStr" class="form-control" readonly="true" />
							</div>
						</div>
						
						<div class="form-group">

							<label class="col-md-2 control-label">年服务频次</label>
							<div class="col-md-5">
								<form:input path="serviceTimeYear" class="form-control" readonly="true"/>
							</div>
						</div>
						
						<div class="form-group">

							<label class="col-md-2 control-label">服务原价(年):</label>
							<div class="col-md-5">
								<form:input path="price" class="form-control" readonly="true"/>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">年付(85折):</label>
							<div class="col-md-5">
								<form:input path="yearPrice" class="form-control" readonly="true"/>
							</div>
						</div>
						
						<div class="form-group">

							<label class="col-md-2 control-label">月付(95折):</label>
							<div class="col-md-5">
								<form:input path="monthPrice" class="form-control" readonly="true"/>
							</div>
						</div>
						
				</form:form>
			</div>
			</section>
		</div>
	</div>
	<!-- page end--> 
	</section>
	 </section> 
	  <%@ include file="../shared/pageFooter.jsp"%> 
	  <!--footer end-->
	</section>

	<%@ include file="../shared/importJs.jsp"%>
	
</body>
</html>
