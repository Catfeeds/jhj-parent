<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<html>
<head>

<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>

<!--css for this page-->

</head>

<body>

	<section id="container"> <!--header start--> <%@ include file="../shared/pageHeader.jsp"%>
	<!--header end--> <!--sidebar start--> <%@ include file="../shared/sidebarMenu.jsp"%> <!--sidebar end-->

	<!--main content start--> <section id="main-content"> <section class="wrapper"> <!-- page start-->
	<div class="row">
		<div class="col-lg-12">
			<section class="panel"> <header class="panel-heading">app参数配置管理 </header>

			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="jhjSettingFormModel" class="form-horizontal" 
					method="POST" action="jhj_setting_form" id="jhjSettingForm">

					<form:hidden path="id" />
					<div class="form-body">

						<div class="form-group required">

							<label class="col-md-2 control-label">名称</label>
							<div class="col-md-5">
								<form:input path="name" class="form-control" placeholder="名称" maxLength="32" />
								<form:errors path="name" class="field-has-error"></form:errors>
							</div>
						</div>

						<div class="form-group required">

							<label class="col-md-2 control-label">配置类型</label>
							<div class="col-md-5">
								<form:input path="settingType" class="form-control" placeholder="配置类型简称,如hour_incoming" maxLength="32" />
								<form:errors path="settingType" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">配置数值填写说明</label>
							
							<p style="margin-left: 220px;">
								(1)如果是钟点工提成、助理提成类型的数值, 应该填写 0~1之间的小数数值
							</p>
							<p style="margin-left: 220px;">
								&nbsp; &nbsp; 如：0.75
							</p>
							<p style="margin-left: 220px;">
								(2)如果是客服电话、服务人员电话之类的数值,应该填写 固定电话或手机号码数字
							</p>
							<p style="margin-left: 220px;">
								&nbsp; &nbsp; 如：01058734880或15210316330
							</p>
							<p style="margin-left: 220px;">
								(3)如果是欠款或者其他金额类数字,应该填写具体的金额数字
							</p>
							<p style="margin-left: 220px;">
								&nbsp; &nbsp; 如： 1000
							</p>
						</div>
						
						<div class="form-group required">

							<label class="col-md-2 control-label">配置数值</label>
							<div class="col-md-5">
								<form:input path="settingValue" class="form-control" 
									placeholder="请按照说明填写对应项目的合法数值" maxLength="12" />
								<form:errors path="settingValue" class="field-has-error"></form:errors>
							</div>
						</div>
						
					</div>

					<div class="form-actions fluid">
						<div class="col-md-offset-6 col-md-6">
							<button type="submit" id="jhjSettingForm_btn" class="btn btn-success">保存</button>
						</div>
					</div>
				</form:form>
			</div>
			</section>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include file="../shared/pageFooter.jsp"%>
	<!--footer end--> </section>

	<!-- js placed at the end of the document so the pages load faster -->
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>


	<!--script for this page-->
	<script src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>" type="text/javascript"></script>
	<script src="<c:url value='/js/jhj/dict/jhjSettingForm.js'/>" type="text/javascript"></script>


</body>
</html>
