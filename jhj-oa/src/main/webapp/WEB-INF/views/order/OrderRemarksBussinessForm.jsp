<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
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
			<h4>添加订单备注 </h4></header>

			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="orderModel" class="form-horizontal" action="remarks_bussiness_form"
					method="POST"  enctype="multipart/form-data">
					
					<form:hidden path="id"/>
					<div class="form-body">
						
						<div class="form-group">

							<label class="col-md-2 control-label"><font color="green">运营人员备注:</font></label>
							<div class="col-md-5">
								<form:textarea path="remarksBussinessConfirm" maxlength="100" placeholder="不超过100字" rows="5" cols="50" />
							</div>
						</div>
					</div>
					
					<div  class="form-actions fluid">
							<div class="col-md-offset-6 col-md-6"
								style="margin-left: 315px">
								<button type="submit" class="btn btn-success">提交</button>
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
