<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="surveyService" uri="/WEB-INF/tags/SurveyServiceSelect.tld"%>
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
			服务内容</header>

			<hr
				style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="contentFormModel" class="form-horizontal"
					method="POST" action="content_form" enctype="multipart/form-data">
					
					<form:hidden path="contentId" />
					
					<div class="form-body">
						<div class="form-group required">
							<label class="col-md-2 control-label">服务所属大类*</label>
							<div class="col-md-5">
								<surveyService:select selectId="${contentFormModel.serviceId }"/>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">服务名称*</label>
							<div class="col-md-5">
								<form:input path="name" class="form-control" placeholder="建议不超过20字" 
									maxLength="20" />
								<form:errors path="name" class="field-has-error"></form:errors>
							</div>
						</div>
						
						
						<div class="form-group required">
							<label class="col-md-2 control-label">价格(数字)*</label>
							<div class="col-md-5">
								<form:input path="price" class="form-control" placeholder="如:20、30等具体数字,不可带单位" 
									maxLength="20" />
								<form:errors path="price" class="field-has-error"></form:errors>
							</div>
						</div>
						
						
						<div class="form-group required">
							<label class="col-md-2 control-label">详细价格(可包含中文)*</label>
							<div class="col-md-5">
								<form:input path="priceDescription" class="form-control" placeholder="如:1厨2卫930元；1厨3卫1170元" 
									maxLength="30" />
								<form:errors path="priceDescription" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<%-- <div class="form-group required">
							<label class="col-md-2 control-label">服务说明</label>
							<div class="col-md-5">
								<form:input path="description" class="form-control" placeholder="默认次数,若没有子服务,或选择项,可以不填" 
									maxLength="20" />
								<form:errors path="description" class="field-has-error"></form:errors>
							</div>
						</div> --%>
						
						<%-- <div class="form-group required">
							<label class="col-md-2 control-label">子服务说明</label>
							<div class="col-md-5">
								<form:input path="contentChildDescription" class="form-control" placeholder="用逗号隔开" 
									maxLength="20" />
								<form:errors path="contentChildDescription" class="field-has-error"></form:errors>
							</div>
						</div> --%>
						
						<%-- <div class="form-group required">
							<label class="col-md-2 control-label">服务范围</label>
							<div class="col-md-5">
								<form:input path="remark" class="form-control" placeholder="休闲鞋" 
									maxLength="20" />
								<form:errors path="remark" class="field-has-error"></form:errors>
							</div>
						</div> --%>
						
						
						<div class="form-group required">
							<label class="col-md-2 control-label">子服务展示类型</label>
							<div class="col-md-5">
								<form:radiobutton path="contentChildType" value="1" label="单选题" />
								&nbsp;&nbsp;&nbsp;
								<form:radiobutton path="contentChildType" value="2" label="多选题" />
								&nbsp;&nbsp;&nbsp;
								<form:radiobutton path="contentChildType" value="0" label="无" />
							</div>
						</div>
						
						<div class="form-group required" id="childServiceDiv">
							<label class="col-md-2 control-label">设置子服务</label>
							<div class="col-md-5" id="childService">
								<%-- <!-- 子服务是 填空题，回显 -->	
								<c:if test="${ }">	
									
									<input type='text' id='contentChildDescription' value="${childDescription }"
										 class='form-control' placeholder='如(家电清洗的子服务)空调1次....' maxLength='200' >
								</c:if> --%>
								<!-- 子服务是单选题或者 多选题,回显 -->
								<c:if test="${contentFormModel.contentChildType == 1  || contentFormModel.contentChildType == 2 }">
									<c:forEach items="${childList }" var="child">
										<%-- <div class='input-group m-bot15' >
												<span class='input-group-addon'>
													<button type='button' id='addOption' title='添加选项'
														onclick='myAddOption(this)' ><i class='icon-plus'></i></button>
												</span>
                								<textarea name='optionText' maxlength='100' class='form-control'>${child.optionStr }</textarea>
                								<span class='input-group-addon'>
                									<button type='button' title='删除该选项' name='delOption' 
                										onclick='myDelOption(this)' class='close'>&times;</button>
               								    </span>
              							 </div> --%>
              							 
              							 
              							 
              							 <div class='input-group m-bot15' >
												<span class='input-group-addon'>
													<button type='button' id='addOption' title='添加选项'
														onclick='myAddOption(this)' ><i class='icon-plus'></i></button>
													<textarea id="defaultTimeChild" row="1" cols="10">${child.defaultTimeChild }</textarea>
                									<textarea id="childPrice"  row='1' cols="10">${child.childPrice }</textarea>
												</span>
                								<textarea name='optionText' maxlength='100' class='form-control'>${child.optionStr }</textarea>
                								<span class='input-group-addon'>
                									<button type='button' title='删除该选项' name='delOption' 
                										onclick='myDelOption(this)' class='close'>&times;</button>
               								    </span>
              							 </div>
              							 
									</c:forEach>
								</c:if>
							</div>
						</div>
						
						<%--
						 <div class="form-group required">
							<label class="col-md-2 control-label">折扣价*</label>
							<div class="col-md-5">
								<form:input path="unitPrice" class="form-control" placeholder="建议不超过20字" 
									maxLength="20" />
								<form:errors path="unitPrice" class="field-has-error"></form:errors>
							</div>
						</div> --%>
						
						<%-- <div class="form-group required">
							<label class="col-md-2 control-label">计量单位(默认'次')</label>
							<div class="col-md-5">
								<form:input path="itemUnit" class="form-control" placeholder="次、单等量词" 
									maxLength="10" />
								<form:errors path="itemUnit" class="field-has-error"></form:errors>
							</div>
						</div> --%>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">计数期限</label>
							<div class="col-md-5">
								<form:radiobutton path="measurement" value="0" label="月" />
								&nbsp;&nbsp;&nbsp;
								<form:radiobutton path="measurement" value="1" label="年" />
								&nbsp;&nbsp;&nbsp;
								<form:radiobutton path="measurement" value="2" label="次" />
								&nbsp;&nbsp;&nbsp;
								<form:radiobutton path="measurement" value="3" label="赠送" />
							</div>
						</div>
						
						
						<div class="form-group required">
							<label class="col-md-2 control-label">默认次数</label>
							<div class="col-md-5">
								<form:input path="defaultTime" class="form-control" placeholder="默认次数" 
									maxLength="20" />
								<form:errors path="defaultTime" class="field-has-error"></form:errors>
							</div>
						</div>
						
						
						<div class="form-group required">
							<label class="col-md-2 control-label">是否可用</label>
							<div class="col-md-5">
								<form:radiobutton path="enable" value="0" label="否" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<form:radiobutton path="enable" value="1" label=" 是" />
							</div>
						</div>

						<div class="form-actions fluid">
							<div class="col-md-offset-6 col-md-6">
								<button type="button" id="contentFormSubmit" class="btn btn-success">保存</button>
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
	
	<script type="text/javascript" src="<c:url value='/js/jhj/survey/contentForm.js'/>"></script>
	
</body>
</html>
