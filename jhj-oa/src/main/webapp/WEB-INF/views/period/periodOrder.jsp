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
				<h4>定制订单</h4>
				</header>
				<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
				<div class="panel-body">
					<form name="form" id="form" class="form-horizontal">
						<form:hidden path="id"/>
						<input type="hidden" name="id" value="${periodOrder.id }" />
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>订单号
							</label>
							<div class="col-md-5">
								<input name="orderNo" class="form-control" value="${periodOrder.orderNo }" readonly="readonly"/>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>手机号
							</label>
							<div class="col-md-5">
								<input name="mobile" class="form-control" value="${periodOrder.mobile }" readonly="readonly"/>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">
								<font color="red">*</font>地址
							</label>
							<div class="col-md-5">
								<form:select path="addrId" id="addrId" class="form-control">
									<form:option value="">--请选择地址--</form:option>
								</form:select>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>订单状态
							</label>
							<div class="col-md-5">
								<input name="orderStatus" class="form-control" value="${periodOrder.orderStatus }" readonly="readonly"/>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>支付方式
							</label>
							<div class="col-md-5">
								<input name="mobile" class="form-control" value="${periodOrder.orderStatus }" readonly="readonly"/>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>订单金额
							</label>
							<div class="col-md-5">
								<input name="orderMoney" class="form-control" value="${periodOrder.orderMoney }" readonly="readonly"/>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>支付金额
							</label>
							<div class="col-md-5">
								<input name="orderPrice" class="form-control" value="${periodOrder.orderPrice }" readonly="readonly"/>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>优惠券
							</label>
							<div class="col-md-5">
								<input name="userCouponsId" class="form-control" value="${periodOrder.userCouponsId }" readonly="readonly"/>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>订单来源
							</label>
							<div class="col-md-5">
								<input name="orderFrom" class="form-control" value="${periodOrder.orderFrom }" readonly="readonly"/>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>订单来源
							</label>
							<div class="col-md-5">
								<input name="remarks" class="form-control" value="${periodOrder.remarks }" readonly="readonly"/>
							</div>
						</div>
					
						<!-- <button type="button" id="btn-save" class="btn btn-success">保存</button> -->
					</form>
				</div>
			</section> 
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include file="../shared/pageFooter.jsp"%>
	<!--footer end--> </section>
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>
	<script src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>" type="text/javascript"></script>
	
</body>
</html>
