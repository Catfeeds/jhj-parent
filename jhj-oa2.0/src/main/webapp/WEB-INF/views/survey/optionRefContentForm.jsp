<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
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
				<h4>选项与服务内容的关联</h4> 
			</header>


			<div class="panel-body">
				<form:form modelAttribute="refContentVoFormModel" class="form-horizontal"
					method="POST" action="option_ref_content" 
					enctype="multipart/form-data">
					 
					 <!-- 当前题目的id -->
					 <input type="hidden" value="${refContentVoFormModel.qId }" id="qId">
					 <input type="hidden" value="${refContentVoFormModel.optionNo }" id="optionNo">
					 
					<!-- 页面标识，新增还是修改 -->
					 <input type="hidden" value="${flagId }" id="flagId">
					 
					 <!-- 回显被选中的内容 -->		
					 <input type="hidden" value="${refContentVoFormModel.selectContent }" id="selectIds" >		
					 			
					<div class="form-body">
						
						<div class="form-group required" >
							<label class="col-md-2 control-label">当前题目名称</label>
							<div class="col-md-5">
								
								<span>${refContentVoFormModel.questionTitle }</span>
							</div>
						</div> 
						
						<div class="form-group required" >
							<label class="col-md-2 control-label">当前选项</label>
							<div class="col-md-5">
								
								<span>${refContentVoFormModel.optionNo }</span>
							</div>
						</div> 
												
												
												
						<div class="form-group required" >
							<label class="col-md-2 control-label">所有服务内容名称</label>
							<div class="col-md-5">
								<c:forEach items="${refContentVoFormModel.contentList }" var="content">
										<label>
											<input type="checkbox" name="contentId"  value="${content.contentId }">
													${content.name }
										</label>
								</c:forEach>	
							</div>
						</div>
						

						<div class="form-actions fluid">
							<div class="col-md-offset-6 col-md-6" style="margin-left: 315px">
								
								<button type="button"  class="btn btn-success" 
										id="optionRefContentFormSubmit">保存</button>
							</div>
						</div>
						
						
						<!-- <div class="form-actions fluid">
							<div class="col-md-offset-6 col-md-6" style="margin-left: 315px">
								
								<button type="button btn-success" id="questionFormSubmit">保存</button>
							</div>
						</div> -->
						
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
	<script type="text/javascript" src="<c:url value='/js/jhj/survey/optionRefContentForm.js'/>"></script>
</body>
</html>
<script type="text/javascript">

	setSelectContent();
</script>
