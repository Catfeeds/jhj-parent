<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<!-- 引入省份、城市标签 -->
<%@ taglib prefix="citySelectTag" uri="/WEB-INF/tags/citySelect.tld"%>
<%@ taglib prefix="provinceSelectTag" uri="/WEB-INF/tags/provinceSelect.tld"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
<%@ taglib prefix="amSelectTag" uri="/WEB-INF/tags/AmSelect.tld"%>
<html>
<head>
	<title>服务人员表单</title>
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
			<section class="panel"> <header class="panel-heading">
			<h4>员工信息</h4> 
			</header>

			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="orgStaffVoFormModel" class="form-horizontal"
					method="POST" action="doOrgStaffForm" id="orgStaff-form"
					enctype="multipart/form-data">
					 
					 <form:hidden  path="staffId"/> 
					<input type="hidden" name="loginOrgId" value="${loginOrgId }">
					 
					<input type="hidden" name="tagIds" id="tagIds" value="${orgStaffVoFormModel.tagIds }"/>
	
					<input type="hidden" name="authIds" id="authIds" value="${orgStaffVoFormModel.authIds }">		
								
					<div class="form-body">
					 
					<input type="hidden" id="selectAmId" value="${orgStaffVoFormModel.amId}">
					
						<div class="form-group required">
							<label class="col-md-2 control-label">选择小组*</label>
							<div class="col-md-5">
								<orgSelectTag:select selectId="${orgStaffVoFormModel.orgId }" />
							</div>
						</div>
							
						<div class="form-group required">

							<label class="col-md-2 control-label">姓名*</label>
							<div class="col-md-5">
								<form:input path="name" class="form-control"
									placeholder="员工姓名" maxLength="32" />
								<form:errors path="name" class="field-has-error"></form:errors>
							</div>
						</div>

						<div class="form-group required">
							<label class="col-md-2 control-label">手机号*</label>
							<div class="col-md-5">
								<input type="hidden" id="oldMobile" value="${orgStaffVoFormModel.mobile }">
								<form:input path="mobile" onchange="validMobileNum()" class="form-control" placeholder="手机号" maxLength="32" />
								<form:errors path="mobile" class="field-has-error"></form:errors>
								<div id="showResult" style="float:left"></div>
							</div>
						</div>
						<div class="form-group required">
							<label class="col-md-2 control-label">座机号</label>
							<div class="col-md-5">
								<form:input path="tel" id="tel" class="form-control"
									maxLength="32" />
								<form:errors path="tel" class="field-has-error"></form:errors>
							</div>
						</div>

						<div class="form-group required">
							<label class="col-md-2 control-label">性别</label>
							<div class="col-md-5">
								<form:radiobutton path="sex" value="0" label=" 男" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<form:radiobutton path="sex" value="1" label=" 女" />
							</div>
						</div>
						<div class="form-group required">
							<label class="col-md-2 control-label">出生年月*</label>
							<div class="col-md-5">
								<div class="input-group date">

									<fmt:formatDate var='formattedDate' value='${orgStaffVoFormModel.birth}' type='both'
										pattern="yyyy-MM-dd" />
									<input type="text" path="birth" id="birth" name="birth"
										value="${formattedDate}" readonly class="form-control"><span
										class="input-group-addon"><i
										class="glyphicon glyphicon-th"></i></span>
								</div>
							</div>
						</div>

						<div class="form-group required">

							<label class="col-md-2 control-label">身份证号*</label>
							<div class="col-md-5">
								<input type="hidden" id="oldCardId" value="${orgStaffVoFormModel.cardId }">
								<form:input path="cardId" onchange="validCardNum()" id="cardId" class="form-control" maxLength="32" />
								<form:errors path="cardId" class="field-has-error"></form:errors>
								<div id="showResults" style="float:left"></div>
							</div>
						</div>

						<div class="form-group required">
							<label class="col-md-2 control-label">地址*</label>
							<div class="col-md-5">
								<form:input path="addr" class="form-control" />
								<form:errors path="addr" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group required">
								<label class="col-md-2 control-label" >户口所在省份:</label> 
								<div class="col-md-5">
									<provinceSelectTag:select selectId="${orgStaffVoFormModel.provinceId }"/>
								</div>
						</div>
						<input type="hidden" id="citySelectedId" value="${orgStaffVoFormModel.cityId}" />
						<div class="form-group" >
								<label class="col-md-2 control-label">户口所在城市:</label> 
								<div class="col-md-5" >
									 <select name="cityId" path="cityId" id="cityId" class="form-control">
										<option value="0">全部</option>
									</select> 
								</div>
						</div>

						<div class="form-group required">
								<label class="col-md-2 control-label">标签</label>
								<div class="col-md-5" id="allTag" >
									<c:forEach items="${tagList }" var="tag">
										<input type="button" style="margin-top:10px;" name="tagName" value="${tag.tagName }" id="${tag.tagId }" onclick="setTagButton()" class="btn btn-default">
									</c:forEach>
								</div> 
						</div> 

						<div class="form-group required">
							<label class="col-md-2 control-label">员工状态</label>
							<div class="col-md-5">
								<form:radiobutton path="status" value="1" label="可用" />
								<form:radiobutton path="status" value="0" label="不可用" />
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">员工审核使用说明*</label>
							
							<p>1.审核规则:</p>
							<p>
								<span style="font-size:12px;line-height:1.5;margin-left: 200px;">
									&nbsp;&nbsp;&nbsp;&nbsp;1-1&gt; 身份验证:
								</span>
							</p>
							<p>
								<span style="font-size:12px;line-height:1.5;margin-left: 210px;">
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									当前服务人员必须满足姓名、手机号、身份证号都存在
								</span>
							</p>
							<p style="margin-left: 200px;">
								&nbsp; &nbsp; 1-2&gt; 钟点工,助理,快送:&nbsp;
							</p>
							<p style="margin-left: 200px;">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								只有在当前服务人员参加叮当大学考试,并且通过对应项目的考核时,按钮可以点击
							&nbsp;
							</p>
							<p>
								<span style="font-size:12px;line-height:1.5;margin-left: 200px;">
									2.审核按钮分为 可点击和 不可点击两种状态（不可点击时,单击不会变色）
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
							</p>
							<p style="margin-left: 200px;">
								&nbsp; &nbsp; 2-1&gt;若按钮不可点击,因为不满足审核规则 <br />
							&nbsp; &nbsp; 2-2&gt;按钮可以点击,绿色表示审核通过,反之表示未通过审核<br />
							</p>
							
						</div>
						
						<div class="form-group required" id="authStatusDiv">
							<label class="col-md-2 control-label">员工审核</label>
							<div class="col-md-5">
								<c:forEach items="${orgStaffVoFormModel.authList }" var="auth">
								
									<input type="hidden" id="authStatus" value="${auth.authStatus }">
									
									<c:if test="${auth.authStatus == 0 }">
									<!-- 后台计算未通过，禁用按钮 -->
											<input type="button"  name="authButton"
											 value="${auth.serviceTypeName }" disabled id="${auth.serviceTypeId }" 
											onclick="setAuthButton(this)" class="btn btn-default">
									</c:if>
									
									<c:if test="${auth.authStatus == 1 }">
									<!-- 后台计算通过，使按钮可点击，回显由另外的function完成 -->
											<input type="button" name="authButton"
											 value="${auth.serviceTypeName }" id="${auth.serviceTypeId }" 
											onclick="setAuthButton(this)" class="btn btn-default">
									</c:if>
									
								</c:forEach>
							</div>
						</div>
						
						
						
						<br />
						<div class="form-actions fluid">
							<div class="col-md-offset-6 col-md-6" style="margin-left: 315px">
								<button type="submit" id="orgStaffForm_btn" class="btn btn-success">保存</button>
							</div>
						</div>
					</div>
				</form:form>
			</div>
			</section>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include
		file="../shared/pageFooter.jsp"%> <!--footer end-->
	</section>
	<!-- js placed at the end of the document so the pages load faster -->
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>


	<!--script for this page-->
	<script type="text/javascript"	src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript"	src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
	<script	type="text/javascript"  src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>

	<script type="text/javascript"	src="<c:url value='/js/jquery.chained.remote.min.js'/>"></script>
	<!-- 二级联动 回显， 省市、门店助理 -->
	<script type="text/javascript"  src="<c:url value='/js/jhj/select-province.js'/>"	></script>
	<script type="text/javascript" src="<c:url value='/js/jhj/select-am-by-org.js'/>"></script>
	<!-- 当前页面校验js -->
	<script type="text/javascript"  src="<c:url value='/js/jhj/validate-reg.js'/>"	></script>
	<!-- 省市联动js -->
	<script type="text/javascript"  src="<c:url value='/js/jhj/bs/orgStaffForm.js'/>"	></script>
</body>
</html>
<script type="text/javascript">
	$('#provinceId').trigger('change');
	
	$("#orgId").trigger("change");
	//处理
	setTagButton();
	
	//身份认证回显
	setReturnAuthButton();
	
</script>
