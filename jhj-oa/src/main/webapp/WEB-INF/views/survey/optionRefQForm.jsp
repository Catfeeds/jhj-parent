<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="nextQuestionSelect" uri="/WEB-INF/tags/SurveyNextQuestionSelect.tld" %> 
<html>
<head>
	<title>考题管理表单</title>
<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>

<link
	href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>"
	rel="stylesheet" type="text/css" />

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
			<header class="panel-heading">
				<h4>选项与下一题的关联</h4> 
			</header>


			<div class="panel-body">
				<form:form modelAttribute="optionRefQFormModel" class="form-horizontal"
					method="POST" action="option_relate" 
					enctype="multipart/form-data">
					 
					 <!-- 当前题目id -->
					 <input type="text" value="${optionRefQFormModel.qId }" id="qId">
								
					<div class="form-body">
						
						<div class="form-group required" >
							<label class="col-md-2 control-label">当前题目名称</label>
							<div class="col-md-5">
								
								<span>${optionRefQFormModel.questionTitle }</span>
							</div>
						</div> 
						
						<input type="hidden" value="${optionRefQFormModel.optionNoList }" id="optionNoList">
					
						<div class="form-group required" id="optionTemplate">
							<label class="col-md-2 control-label">关联关系</label>
							<div class="col-md-5" >
								<c:forEach items="${optionRefQFormModel.optionNoList }" var = "optionNo">
											
									<label><input type="checkbox" name="optionNo" value="${optionNo }">${optionNo }</label>	
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;													
								</c:forEach>								
								<nextQuestionSelect:select selectId="${optionRefQFormModel.nextQId }"/>								
								
							</div>
							
							
							<br>
							<button type="button" onclick="myDelOption(this)" class="btn btn-info ">删除对应关系</button> 
						</div> 
						<button type="button" id="addOption" class="btn btn-info "><i class="icon-plus"></i>添加对应关系</button> 
						
						
						

						<div class="form-actions fluid">
							<div class="col-md-offset-6 col-md-6" style="margin-left: 315px">
								
								<button type="button"  class="btn btn-success" id="questionFormSubmit">保存</button>
							</div>
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
	<%@ include
		file="../shared/pageFooter.jsp"%> 
	</section>
	<%@ include file="../shared/importJs.jsp"%>


	<!--script for this page-->
	<script	type="text/javascript"  src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>
	
	<script	type="text/javascript"  src="<c:url value='/js/jhj/survey/optionRefQForm.js'/>"></script>	
</body>
</html>
