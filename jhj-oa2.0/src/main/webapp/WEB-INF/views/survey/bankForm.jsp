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
				<form:form modelAttribute="bankFormModel" class="form-horizontal"
					method="POST" action="bank_form" enctype="multipart/form-data">

					<form:hidden path="bankId" />
					<div class="form-body">

						
						<div class="form-group required">
							<label class="col-md-2 control-label">题库名称*</label>
							<div class="col-md-5">
								<form:input path="name" class="form-control" placeholder="建议不超过20字" 
									maxLength="20" />
								<form:errors path="name" class="field-has-error"></form:errors>
							</div>
						</div>
						


						<div class="form-actions fluid">
							<div class="col-md-offset-6 col-md-6">
								<button type="submit" class="btn btn-success">保存</button>
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
