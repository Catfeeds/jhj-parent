<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="citySelectTag" uri="/WEB-INF/tags/citySelect.tld"%>
<html>
<head>

<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>

<!--css for this page-->
<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>"
	type="text/css" />

<link
	href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>"
	rel="stylesheet" type="text/css" />
</head>

<body>

	<section id="container"> <!--header start--> <%@ include
		file="../shared/pageHeader.jsp"%> <!--header end-->

	<!--sidebar start--> <%@ include file="../shared/sidebarMenu.jsp"%>
	<!--sidebar end--> <!--main content start--> <section id="main-content">
	<section class="wrapper"> <!-- page start-->
	<div class="row">
		<div class="col-lg-12">
			<section class="panel"> <header class="panel-heading">
			用户管理 </header>

			<hr
				style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="secModel" class="form-horizontal"
					method="POST" action="listForm" id="sec-form"
					enctype="multipart/form-data">

					<form:hidden path="id" />
					<div class="form-body">
						<div class="form-group required">

							<label class="col-md-2 control-label">姓名</label>
							<div class="col-md-5">
								<form:input path="name" class="form-control" placeholder="姓名"
									maxLength="32" />
								<form:errors path="name" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group required">

							<label class="col-md-2 control-label">手机号</label>
							<div class="col-md-5">
								<form:input path="mobile" class="form-control" placeholder="手机号"
									maxLength="32" />
								<form:errors path="mobile" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group">

							<label class="col-md-2 control-label">昵称</label>
							<div class="col-md-5">
								<form:input path="nickName" class="form-control"
									placeholder="昵称" maxLength="32" />
								<form:errors path="nickName" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group required">
						<label class="col-md-2 control-label">性别</label>
										<div class="col-md-10" >

											<div class="row">
												<div class="col-md-2" align="right">
													<label class="radio">
														<form:radiobutton path="sex" value="0"/>男
													</label>
												</div>
												<div class="col-md-2" align="left">
													<label class="radio">
													<form:radiobutton path="sex" value="1"/>女
													</label>
												</div>
											</div>
                                      </div>
                                      </div>
						<div class="form-group required">
							<label class="col-md-2 control-label">出生日期</label>
							<div class="col-md-5">
								<div class="input-group date">
									<fmt:formatDate var='formattedDate'
										value='${secModel.birthDay}' type='both' pattern="yyyy-MM-dd" />
									<input type="text" name="birthDay" path="birthDay"
										id="birthDay" value="${formattedDate}" readonly
										class="form-control">
										 <span class="input-group-addon">
										<i class ="glyphicon glyphicon-th">
										</i>
									</span>
									<form:errors path="birthDay" class="field-has-error"></form:errors>

								</div>
							</div>
						</div>

						<c:if
							test="${secModel.headImg != null && secModel.headImg != '' }">
							<div class="form-group ">

								<label class="col-md-2 control-label">图片</label>
								<div class="col-md-5">
									<img src="${ secModel.headImg }" />
								</div>
							</div>
						</c:if>

						<div class="form-group required">

							<label class="col-md-2 control-label">头像</label>
							<div class="col-md-5">
								<input id="headImg" type="file" name="headImg" accept="image/*"
									data-show-upload="false">
								<form:errors path="headImg" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group required">

							<label class="col-md-2 control-label">所在城市</label>
							<div class="col-md-5">
								<citySelectTag:select selectId="${secModel.cityId}" />
							</div>
						</div>

						<div class="form-group required">
						<label class="col-md-2 control-label">状态</label>
										<div class="col-md-10" >

											<div class="row">
												<div class="col-md-2" align="right">

													<label class="radio">
														<form:radiobutton path="status" value="0"/>注册中
													</label>
												</div>

												<div class="col-md-2" align="left">
													<label class="radio">
													<form:radiobutton path="status" value="1"/>审核中
													</label>
												</div>

												<%-- <div class="col-md-2" align="left">
													<label class="radio">
													<form:radiobutton path="status" value="2"/>审核通过
													</label>
												</div> --%>
											</div>
                                      </div>
                                      </div>
                           <%--  <c:if
							test="${secModel.headImg != null && secModel.headImg != '' }">
							<div class="form-group ">

								<label class="col-md-2 control-label">图片</label>
								<div class="col-md-5">
								<form:input path="nickName" class="form-control"
									placeholder="昵称" maxLength="32" />
								<form:errors path="nickName" class="field-has-error"></form:errors>
								</div>
							</div>
						</c:if> --%>
						<div class="form-actions fluid">
							<div class="col-md-offset-6 col-md-6">
								<button type="submit" id="secForm_btn" class="btn btn-success">保存</button>
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
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
	<script
		src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"
		type="text/javascript"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-fileupload/fileinput.min.js'/>"></script>

	<script src="<c:url value='/js/jhj/sec/listForm.js'/>"
		type="text/javascript"></script>

	<script src="<c:url value='/js/jhj/demo.js'/>"></script>

</body>
</html>
