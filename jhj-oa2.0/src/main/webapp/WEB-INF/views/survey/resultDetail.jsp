<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<html>
<head>

<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>

<!--css for this page-->
<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>"
	type="text/css" />

</head>

<body>

	<section id="container">  
	<%@ include file="../shared/pageHeader.jsp"%> 

	 <%@ include file="../shared/sidebarMenu.jsp"%>
	 
	<section id="main-content">
	<section class="wrapper"> 
	<div class="row">
		<div class="col-lg-12">
			<section class="panel"> <header class="panel-heading">
			题库</header>

			<hr
				style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form  class="form-horizontal" enctype="multipart/form-data">

					<div class="form-body">
						
						<div class="form-group required">
							<label class="col-md-2 control-label">当前用户姓名</label>
							<div class="col-md-5">
									${userInfoModel.userName }								
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">当前用户手机号</label>
							<div class="col-md-5">
									${userInfoModel.mobile }								
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">月计费服务</label>
							<div class="col-md-5">
									<c:forEach items="${monthContent}" var="month">
										<input type="button" class="btn btn-success" value="${month.key.name }" style="width:35%;margin-right:2%;margin-top:5px;float:left;">
										<p style="line-height:45px;width:10%;margin:0;float:left;">次数:${month.value }</p>
									</c:forEach>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">年计费服务</label>
							<div class="col-md-5">
									<c:forEach items="${yearContent}" var="year">
										<input type="button" class="btn btn-success" value="${year.key.name }" style="width:35%;margin-right:2%;margin-top:5px;float:left;">
										<p style="line-height:45px;width:10%;margin:0;float:left;">次数:${year.value }</p>
									</c:forEach>
							</div>
						</div>
						
						
						<div class="form-group required">
							<label class="col-md-2 control-label">按年计费：预计花费</label>
							<div class="col-md-5" style="margin-top:5px;">
								 	${yearPrice }
							</div>
						</div>
						
						
						<div class="form-group required">
							<label class="col-md-2 control-label">按次计费服务</label>
							<div class="col-md-5">
									<c:forEach items="${timeContent}" var="time">
										<input type="button" class="btn btn-success" value="${time.key.name }" style="width:35%;margin-right:2%;margin-top:5px;float:left;">
										
										<p style="line-height:45px;width:10%;margin:0;float:left;">次数:${time.value }</p>
									</c:forEach>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">按次计费：预计花费</label>
							<div class="col-md-5" style="margin-top:5px;">
								 	${timePrice }
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">套餐计费服务</label>
							<div class="col-md-5">
									<c:forEach items="${childContent}" var="child">
										<input type="button" class="btn btn-success" value="${child.key }" style="width:65%;margin-right:2%;margin-top:5px;float:left;">
										
										<p style="line-height:45px;width:10%;margin:0;float:left;">次数:${child.value }</p>
									</c:forEach>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">套餐计费：预计花费</label>
							<div class="col-md-5" style="margin-top:5px;">
								 	${childPrice }
							</div>
						</div>
				</form:form>
			</div>
			</section>
		</div>
	</div>
		</section> 
	</section> 
	<%@ include file="../shared/pageFooter.jsp"%> 
	
	</section>
	<%@ include file="../shared/importJs.jsp"%>

</body>
</html>
