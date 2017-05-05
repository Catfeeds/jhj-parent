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
					<form:form method="post" name="form" id="form" commandName="periodServiceTypeVo" class="form-horizontal">
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
								<font color="red">*</font>定制类型
							</label>
							<div class="col-md-5 checkbox">
								<label>
									<form:checkbox path="packageTypeList" value="1" />套餐一
								</label>
								<label>
									<form:checkbox path="packageTypeList" value="2" />套餐二
								</label>
								<label>
									<form:checkbox path="packageTypeList" value="3" />套餐三
								</label>
								<label>
									<form:checkbox path="packageTypeList" value="4" />套餐四
								</label>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">
								<font color="red">*</font>服务类型
							</label>
							<div class="col-md-5">
								<form:select path="serviceTypeId" id="serviceTypeId" class="form-control">
									<form:option value="">--请选择服务类型--</form:option>
									<form:options items="${partnerServiceTypeList }" itemLabel="name" itemValue="serviceTypeId"/>
								</form:select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">服务子类型</label>
							<div class="col-md-5">
								<form:select path="serviceAddonId" id="serviceAddonId" class="form-control">
									<form:option value="">--请选择服务子类型--</form:option>
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
								<font color="red">*</font>频次数
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
									<form:option value="次/时">次/时</form:option>
									<form:option value="次/周">次/周</form:option>
									<form:option value="次/半月">次/半月</form:option>
									<form:option value="次/月">次/月</form:option>
									<form:option value="次/年">次/年</form:option>
									<form:option value="小时/年">小时/年</form:option>
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
							<div class="col-md-5 radio">
								<label>
									<form:radiobutton path="enbale" value="0" checked="true"/>是
								</label>
								<label>
									<form:radiobutton path="enbale" value="1" />否
								</label>
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
	<script src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>" type="text/javascript"></script>
	<script type="text/javascript" src="<c:url value='/js/period/periodServiceType.js'/>"></script>
</body>
</html>
