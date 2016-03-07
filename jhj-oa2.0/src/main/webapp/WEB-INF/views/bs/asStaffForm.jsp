<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<!-- 引入省份、城市标签 -->
<%@ taglib prefix="citySelectTag" uri="/WEB-INF/tags/citySelect.tld"%>
<%@ taglib prefix="provinceSelectTag" uri="/WEB-INF/tags/provinceSelect.tld"%>
<!-- 学历选择标签 -->
<%@ taglib prefix="degreeSelectTag" uri="/WEB-INF/tags/degreeTypeSelect.tld" %>
<!-- 民族选择标签 -->
<%@ taglib prefix="nationSelectTag" uri="/WEB-INF/tags/nationTypeSelect.tld" %>
<!-- 门店选择标签 -->
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
 <!-- 星座选择标签 -->
<%@ taglib prefix="astroSelectTag" uri="/WEB-INF/tags/astroSelect.tld" %> 
<!-- 血型选择标签 -->
<%@taglib prefix="bloodTypeSelectTag" uri="/WEB-INF/tags/bloodTypeSelect.tld" %>
<html>
<head>
	<title>助理信息表单</title>

<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>
<!-- 图片上传 -->
<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>"
	type="text/css" />
<!-- 日期选择 -->
<link href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>"
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
				<form:form modelAttribute="orgStaffVoModel" class="form-horizontal"
					method="POST" action="doOrgStaffAsForm" id="orgStaffAs-form"
					enctype="multipart/form-data">

					<form:hidden path="staffId" />
					
					<input type="hidden" name="tagIds" id="tagIds" value="${orgStaffVoModel.tagIds }"/>

					
					<input type="hidden" name="authIds" id="authIds" value="${orgStaffVoModel.authIds }">
					
					<div class="form-body required">
						<!-- 当前登录角色 的 orgId，店长或 admin -->
						<input type="hidden" name="loginOrgId" value="${loginOrgId }">
						<c:choose>
							<c:when test="${loginOrgId == 0 }">
								<!-- 如果当前登录的是 admin ，门店id为选中 的 id -->
								<div class="form-group required">
									<label class="col-md-2 control-label">所属门店*</label>
									<div class="col-md-5">
										<orgSelectTag:select selectId="${orgStaffVoModel.orgId }" />
									</div>
								</div>
							</c:when>
							<c:otherwise> 
								<!-- 如果当前登录的是 店长 ，门店id 为当前店长 id -->
								<input type="hidden" name="orgId" id="orgId" value=" ${loginOrgId  }">							
							</c:otherwise>
						</c:choose>

						<div class="form-group required">

							<label class="col-md-2 control-label">姓名*</label>
							<div class="col-md-5">
								<form:input path="name" class="form-control"
									placeholder="员工姓名" maxLength="32" />
								<form:errors path="name" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">性别</label>
							<div class="col-md-5">
								<form:radiobutton path="sex" value="0" label="男" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<form:radiobutton path="sex" value="1" label="女" />
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">出生年月*</label>
							<div class="col-md-5">
								<div class="input-group date">
									<fmt:formatDate var='formattedDate' value='${orgStaffVoModel.birth}' type='both'
										pattern="yyyy-MM-dd" />
									<input type="text" onchange="getAstro()" path="birth" id="birth" name="birth" value="${formattedDate}" readonly class="form-control">
									<span class="input-group-addon"><i class="glyphicon glyphicon-th"></i></span>
								</div>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">电话号码*</label>
							<div class="col-md-5">
								<form:input path="mobile" id="mobile" onchange="validMobile(this)"	class="form-control" placeholder="手机号" maxLength="11" />
								<form:errors path="mobile" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">学历*</label>
							<div class="col-md-5">
								<degreeSelectTag:select degreeId="${ orgStaffVoModel.edu}"/>
							</div>
						</div>
						
						 <div class="form-group required">
							<label class="col-md-2 control-label">民族*</label>
							<div class="col-md-5">
								<nationSelectTag:select nameId ="${orgStaffVoModel.nation}"/>
							</div>
						</div>
						
						
						<div class="form-group required">

							<label class="col-md-2 control-label">身份证号*</label>
							<div class="col-md-5">
								<input type="hidden" id="oldCardId" value="${orgStaffVoModel.cardId }">
								<form:input path="cardId"  onchange="validCardNum()" id="cardId" class="form-control" maxLength="22" />
								<form:errors path="cardId" class="field-has-error"></form:errors>
								<div id="showResults" style="float:left"></div>
							</div>
						</div>
						
						
						<!-- onkeydown="validCardNum()"   onblur="checkIdcard(this)" -->
						<div class="form-group required">
							<label class="col-md-2 control-label">星座</label>
							<div class="col-md-5">
								<astroSelectTag:select astroId="${ orgStaffVoModel.astro }"/>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">血型</label>
							<div class="col-md-5">
								
								<bloodTypeSelectTag:select bloodTypeId="${ orgStaffVoModel.bloodType }"/>
							</div>
						</div>
						
						
						<input type="hidden" id="citySelectedId" value="${orgStaffVoModel.cityId}" />
						<div class="form-group required">
								<label class="col-md-2 control-label" >户口所在省份:</label> 
								<div class="col-md-5">
									<provinceSelectTag:select selectId="${ orgStaffVoModel.provinceId }"/>
								</div>
							</div>
							<div class="form-group" >
								<label class="col-md-2 control-label">户口所在城市:</label> 
								
								<div class="col-md-5" >
									 <select name="cityId" path="cityId" id="cityId" class="form-control">
										<option value="0">全部</option>
									</select> 
								</div>
						</div>

							
						<c:if test="${orgStaffVoModel.headImg != null && orgStaffVoModel.headImg != '' }">
							<div class="form-group ">
								<label class="col-md-2 control-label">图片</label>
								<div class="col-md-5">
									<img src="${ orgStaffVoModel.headImg }" style="display:block;width:100%;max-width:200px;max-height:200px;"/>
								</div>
								
							</div>
						</c:if>	
						

						<div class="form-group required">

							<label class="col-md-2 control-label">助理头像</label>
							<div class="col-md-5">
								<input id="headImg" type="file" name="headImg" accept="image/*"
									data-show-upload="false">
								<form:errors path="headImg" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group required">
								<label class="col-md-2 control-label">标签</label>
								<div class="col-md-5" id="allTag" >
									<c:forEach items="${tagList }" var="tag">
										<input type="button" name="tagName" value="${tag.tagName }" id="${tag.tagId }" onclick="setTagButton()" class="btn btn-default">
									</c:forEach>
								</div> 
						</div> 
						
						<div class="form-group required">
							<label class="col-md-2 control-label">自我介绍</label>
							<div class="col-md-5">
								<form:textarea path="intro" rows="10" cols="60" />
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
								<c:forEach items="${orgStaffVoModel.authList }" var="auth">
								
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
								<button type="submit" id="orgStaffAsForm_btn" class="btn btn-success">保存</button>
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
	<!-- 日期处理js -->
	<script type="text/javascript"	src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript"	src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>

	<!-- 头像上传按钮美化js -->
	<script type="text/javascript"	src="<c:url value='/assets/bootstrap-fileupload/fileinput.min.js'/>"></script>
	<script	type="text/javascript"  src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>
	<!-- 省市联动js -->
	<script type="text/javascript"  src="<c:url value='/js/jhj/select-province.js'/>"	></script>
	<!-- 当前页面校验js -->
	<script type="text/javascript"  src="<c:url value='/js/jhj/validate-reg.js'/>"	></script>
	<script type="text/javascript"  src="<c:url value='/js/jhj/bs/asStaffForm.js'/>"	></script>
</body>
</html>
<script tyep="text/javascript">
$('#provinceId').trigger('change');
setTagButton();

//身份认证回显
setReturnAuthButton();	
		
</script>
