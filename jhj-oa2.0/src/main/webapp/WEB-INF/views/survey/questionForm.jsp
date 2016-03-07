<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="bankSelect" uri="/WEB-INF/tags/SurveyBankSelect.tld" %> 
<%@ taglib prefix="beforeQuestionSelect" uri="/WEB-INF/tags/SurveyBeforeQuestionSelect.tld" %> 
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
				<h4>考题信息</h4> 
			</header>


			<div class="panel-body">
				<form:form modelAttribute="questionVoFormModel" class="form-horizontal"
					method="POST" action="question_form" id="question-form"
					enctype="multipart/form-data">
					 
					<form:hidden  path="qId"/> 
					
								
					<div class="form-body">
						
						<div class="form-group required" >
								<label class="col-md-2 control-label">题库*</label> 
								<div class="col-md-5" >
									<bankSelect:select selectId="${questionVoFormModel.bankId }"/>
								</div>
						</div>
					 
					 
						<div class="form-group required">
							<label class="col-md-2 control-label">题干*</label>
							<div class="col-md-5">
								<form:textarea path="title" class="form-control " 
									placeholder="建议不超过100字,输入框可拖动" maxLength="100" />
							</div>
						</div>


						<div class="form-group required">
							<label class="col-md-2 control-label">是否多选</label>
							<div class="col-md-5">
								<form:radiobutton path="isMulti" value="1" label="是" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<form:radiobutton path="isMulti" value="0" label="否" />
							</div>
						</div> 
						
						<div class="form-group required">
							<label class="col-md-2 control-label">题目位置*</label>
							<div class="col-md-5">
								<form:radiobutton path="isFirst" value="0" label="第一题" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<form:radiobutton path="isFirst" value="1" label="位于中间" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<form:radiobutton path="isFirst" value="2" label="最后一题" />
							</div>
						</div> 
						
						<div class="form-group required" id="selectBefore" >
							<label class="col-md-2 control-label">设置上一题*</label>
							<div class="col-md-5">
								<beforeQuestionSelect:select selectId="${questionVoFormModel.beforeQId }"/>									
							</div>
						</div> 
						
						<%-- <div class="form-group required" id="selectAfter" style="display:none">
							<label class="col-md-2 control-label">选择下一题*</label>
							<div class="col-md-5">
								<nextQuestionSelect:select selectId="${questionVoFormModel.beforeQId }"/>
							</div>
						</div>  --%>
						
						
						
						<div class="form-group  required" >
							<label class="col-md-2 control-label">设置选项*</label>
							
							<div class="col-lg-5 " id="optionTemplate" >
							
							<!-- 对于选项,添加时是两个 空白选项框, 修改时回显所有选项,并选中正确答案 -->
							
							<c:if test="${questionVoFormModel.qId eq 0 }">
								<div class="input-group m-bot15" >
                                        <span class="input-group-addon">
                                          <input type="radio"  name="optionRadio" id="optionRadio">
                                        </span>
                                        <textarea name="optionText" maxlength="100" placeholder="提示:点击左边单选框,选择正确答案" class="form-control"></textarea>
                                        <span class="input-group-addon">
                                        	<button type="button" name="delOption" onclick="myDelOption(this)" class="close">&times;</button>
                                        </span>
								</div>
								
								<div class="input-group m-bot15" >
                                        <span class="input-group-addon">
                                          <input type="radio"  name="optionRadio" id="optionRadio" >
                                        </span>
                                        <textarea name="optionText"  maxlength="100" placeholder="提示:建议不超过100字,输入框可拖动" class="form-control"></textarea>
                                        <span class="input-group-addon">
                                        	<button type="button" name="delOption" onclick="myDelOption(this)" class="close">&times;</button>
                                        </span>
								</div>
							 </c:if>
							 
							 <c:if test="${questionVoFormModel.qId > 0 }">
							 	 	
							 	 	<c:forEach items="${questionVoFormModel.optionList }" var="item" varStatus="option">
							 	 		
							 	 				<div class="input-group m-bot15" >
			                                        <span class="input-group-addon">
			                                          <input type="radio"  name="optionRadio" id="optionRadio" >
			                                        </span>
			                                        <textarea name="optionText"  maxlength="100"  class="form-control">${item.title }</textarea>
			                                        <span class="input-group-addon">
			                                        	<button type="button" name="delOption" onclick="myDelOption(this)" class="close">&times;</button>
			                                        </span>
												</div>
							 	 	</c:forEach>
							 </c:if>
							 		
                                 <button type="button" id="addOption" class="btn btn-info "><i class="icon-plus"></i>添加一个选项</button> 
							</div>
							
							
						</div>
						

						<div class="form-actions fluid">
							<div class="col-md-offset-6 col-md-6" style="margin-left: 315px">
								<!-- <button type="submit" id="" class="btn btn-success">保存</button> -->
								
								<button type="button" id="questionFormSubmit">保存</button>
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
	
	<script type="text/javascript" src="../js/jhj/survey/questionForm.js"></script>
		
</body>
</html>
