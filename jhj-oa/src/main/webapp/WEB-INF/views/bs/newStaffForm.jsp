<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="citySelectTag" uri="/WEB-INF/tags/citySelect.tld"%>
<%@ taglib prefix="provinceSelectTag" uri="/WEB-INF/tags/provinceSelect.tld"%>
<%@ taglib prefix="cloudOrgSelectTag" uri="/WEB-INF/tags/CloudOrgSelect.tld"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
<%@ taglib prefix="staffLevelSelectTag" uri="/WEB-INF/tags/staffLevelSelect.tld"%>

<%@ taglib prefix="degreeSelectTag" uri="/WEB-INF/tags/degreeTypeSelect.tld" %>
<%@ taglib prefix="nationSelectTag" uri="/WEB-INF/tags/nationTypeSelect.tld" %>
<%@ taglib prefix="astroSelectTag" uri="/WEB-INF/tags/astroSelect.tld" %> 
<%@taglib  prefix="bloodTypeSelectTag" uri="/WEB-INF/tags/bloodTypeSelect.tld" %>
<html>
<head>
	<title>工作人员管理</title>
<%@ include file="../shared/importCss.jsp"%>

<link
	href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>"
	rel="stylesheet" type="text/css" />

	<%@ include file="../shared/importJs.jsp"%>

	<script type="text/javascript" src="<c:url value='/js/jquery.treeLite.js' /> "></script>

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
				<form:form modelAttribute="newStaffFormVoModel" class="form-horizontal"
					method="POST" action="new_staff_form" id="newStaff-form"  
					
					enctype="multipart/form-data">
					 
					 <form:hidden  path="staffId"/> 
					 
					<input type="hidden" name="skillIds" id="skillIds" value="${newStaffFormVoModel.skillIds }"/>
	
					<input type="hidden" name="authIds" id="authIds" value="${newStaffFormVoModel.authIds }">		
								
					<input type="hidden" name="tagIds" id="tagIds" value="${newStaffFormVoModel.tagIds }"/>	
								
						<div class="form-group required">
							<label class="col-md-2 control-label">选择门店*</label>
							<div class="col-md-5">
								<orgSelectTag:select selectId="${newStaffFormVoModel.parentOrgId }" sessionOrgId="${loginOrgId }"/>
							</div>
						</div>
						<div class="form-group required">
							<label class="col-md-2 control-label">选择云店*</label>
							<div class="col-md-5">
								
								<select name="orgId" id="orgId" class="form-control">
									<c:if test="${newStaffFormVoModel.orgId==0 }">
										<option value="0">全部</option>
									</c:if>
									<c:if test="${newStaffFormVoModel.orgId!=0 }">
										<option value="${org.orgId }" selected="selected">${org.orgName}</option>
									</c:if>
								</select>
								<%-- <cloudOrgSelectTag:select 
										logInParentOrgId="${loginOrgId}"
										selectId="${newStaffFormVoModel.orgId }"/>	 --%>
							    		
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
								<input type="hidden" id="oldMobile" value="${newStaffFormVoModel.mobile }">
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

									<fmt:formatDate var='formattedDate' value='${newStaffFormVoModel.birth}' type='both'
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
								<input type="hidden" id="oldCardId" value="${newStaffFormVoModel.cardId }">
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
									<provinceSelectTag:select selectId="${newStaffFormVoModel.provinceId }"/>
								</div>
						</div>
						<input type="hidden" id="citySelectedId" value="${newStaffFormVoModel.cityId}" />
						<div class="form-group" >
								<label class="col-md-2 control-label">户口所在城市:</label> 
								<div class="col-md-5" >
									 <select name="cityId" path="cityId" id="cityId" class="form-control">
										<option value="0">全部</option>
									</select> 
								</div>
						</div>
				
				
						<div class="form-group">
                                <label class="col-sm-2 col-sm-2 control-label">技能</label>
                                <div class="col-sm-5">
	                                   <div class="portlet-body">
					                  	   <c:import url = "../shared/treeSelector.jsp">
											 <c:param name="propertyName" value="skillId"/>
											 <c:param name="propertyValue" value="${newStaffFormVoModel.skillIdsStr}"/>
											 <c:param name="checkbox" value="true"/>
											 <c:param name="treeDataSourceName" value="treeDataSource"/>
										   </c:import>
			                    		</div>
                        		 </div>
                        </div>

						<div class="form-group required">
							<label class="col-md-2 control-label">
								员工状态
							</label>
							<div class="col-md-5">
								<form:radiobutton path="status" value="1" label="可用" />
								<form:radiobutton path="status" value="0" label="不可用" />
								<p>
									若员工离职或请假请勾选为
										<font color="red">不可用,不可用的员工不参与派工</font>
								</p>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">选择员工等级*</label>
							<div class="col-md-5">
								<staffLevelSelectTag:select level="${newStaffFormVoModel.level }"/>
								<form:errors path="level" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">学历*</label>
							<div class="col-md-5">
								<degreeSelectTag:select edu="${newStaffFormVoModel.edu}"/>
							</div>
						</div>
						
						 <div class="form-group required">
							<label class="col-md-2 control-label">民族*</label>
							<div class="col-md-5">
								<nationSelectTag:select nationName="${newStaffFormVoModel.nation}"/>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">星座</label>
							<div class="col-md-5">
								<astroSelectTag:select astroId="${ newStaffFormVoModel.astro }"/>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">血型</label>
							<div class="col-md-5">
								<bloodTypeSelectTag:select bloodTypeId="${ newStaffFormVoModel.bloodType }"/>
							</div>
						</div>
						
						
						<c:if test="${newStaffFormVoModel.headImg != '' }">												
							<div class="form-group ">
								<label class="col-md-2 control-label">图片</label>
								<div class="col-md-5">
									<img src="${ newStaffFormVoModel.headImg }" style="display:block;width:100%;max-width:200px;max-height:200px;"/>
								</div>
								
							</div>
						</c:if>

						<div class="form-group required">
							<label class="col-md-2 control-label">员工头像</label>
							<div class="col-md-5">
								<input id="headImg" type="file" name="headImg" accept="image/*"
									data-show-upload="false">
								<form:errors path="headImg" class="field-has-error"></form:errors>
							</div>
						</div> 
						
						<div class="form-group required">
							<label class="col-md-2 control-label">员工自我介绍</label>
							<div class="col-md-5">
								 <form:textarea path="intro" rows="5" cols="50" class="form-control" 
                                 	placeholder="不超过120字" maxlength="120"/>	
							</div>
						</div> 
						
						
						<div class="form-group required" style="display:none;">
								<label class="col-md-2 control-label">技能标签</label>
								<div class="col-md-5" id="allTag" >
									<c:forEach items="${newStaffFormVoModel.tagList }" var="tag">
										<input type="button" style="margin-top:10px;" name="tagName" value="${tag.tagName }" id="${tag.tagId }" onclick="setTagButton(this)" class="btn btn-default">
									</c:forEach>
								</div> 
						</div> 
						
						
						<c:if test="${ newStaffFormVoModel.staffId > 0 }">
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
									<c:forEach items="${newStaffFormVoModel.authList }" var="auth">
									
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
						
						</c:if>
						
						<br />
						<div class="form-actions fluid">
							<div class="col-md-offset-6 col-md-6" style="margin-left: 315px">
								<button type="submit"  id="orgStaffForm_btn" class="btn btn-success">保存</button>
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

	<!--script for this page-->
	<script type="text/javascript"	src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript"	src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
	
	<script	type="text/javascript"  src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>
	
	<script type="text/javascript"	src="<c:url value='/js/jquery.chained.remote.min.js'/>"></script>
	<!-- 二级联动 回显， 省市、门店助理 -->
	<script type="text/javascript"  src="<c:url value='/js/jhj/select-province.js'/>"	></script>
	<!-- 当前页面校验js -->
	<script type="text/javascript"  src="<c:url value='/js/jhj/validate-reg.js'/>"	></script>
	
	<script type="text/javascript" src="<c:url value='/js/jhj/select-org-cloud.js'/>"></script>
	
	<script type="text/javascript"  src="<c:url value='/js/jhj/bs/newStaffForm.js'/>"	></script> 
</body>
</html>
<script type="text/javascript">

	$(document).ready(function(){
		$('#provinceId').trigger('change');
	});

	
	
	//技能标签
	setReturnTagButton();
	
	//身份认证回显
	setReturnAuthButton();
	
</script>
