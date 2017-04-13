<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html>
<%@ include file="../shared/taglib.jsp"%>
<!-- taglib for this page -->
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
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
			<section class="panel"> <header class="panel-heading">
				<h4>定制服务类型</h4>
				</header>
				<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
				<div class="panel-body">
					<form:form method="post" name="form" id="form" commandName="periodServiceType" class="form-horizontal">
						<form:hidden path="id"/>
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>定制服务类型名称
							</label>
							<div class="col-md-5">
								<form:input path="name" class="form-control"/>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">
								<font color="red">*</font>服务类型
							</label>
							<div class="col-md-5">
								<form:select path="serviceTypeId" class="form-control">
									<form:option value="">--请选择服务类型--</form:option>
									<form:option value="1">套餐一</form:option>
									<form:option value="2">套餐二</form:option>
									<form:option value="3">套餐三</form:option>
									<form:option value="4">套餐四</form:option>
								</form:select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">
								<font color="red">*</font>服务子类型
							</label>
							<div class="col-md-5">
								<form:select path="serviceAddonId" class="form-control">
									<form:option value="">--请选择服务子类型--</form:option>
									<form:options items="${partnerServiceTypeList }" itemLabel="name" itemValue="serviceTypeId"/>
								</form:select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">
								<font color="red">*</font>原价
							</label>
							<div class="col-md-5">
								<form:input path="price" class="form-control"/>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">
								<font color="red">*</font>会员价
							</label>
							<div class="col-md-5">
								<form:input path="vipPrice" class="form-control"/>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">
								<font color="red">*</font>次数
							</label>
							<div class="col-md-5">
								<form:input path="num" class="form-control"/>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">
								<font color="red">*</font>频次
							</label>
							<div class="col-md-5">
								<form:select path="punit" class="form-control">
									<form:option value="">--请选择频率--</form:option>
									<form:option value="week">周</form:option>
									<form:option value="month">月</form:option>
									<form:option value="half-year">半年</form:option>
									<form:option value="year">年</form:option>
								</form:select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">
								<font color="red">*</font>总次数
							</label>
							<div class="col-md-5">
								<form:input path="total" class="form-control"/>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">
								<font color="red">*</font>是否有效
							</label>
							<div class="col-md-5">
								<form:radiobutton path="enbale" value="0" checked="true"/>是
								<form:radiobutton path="enbale" value="1" />否
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">备注</label>
							<div class="col-md-5">
								<form:input path="remarks" class="form-control"/>
							</div>
						</div>
						
						<button type="button" id="btn-save" class="btn btn-success">保存</button>
					</form:form>
				</div>
			</section>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include file="../shared/pageFooter.jsp"%>
	<!--footer end--> </section>
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>
</body>
</html>
