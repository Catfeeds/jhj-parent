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
						
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>手机号
							</label>
							<div class="col-md-5">
								<input name="mobile" class="form-control" />
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">
								<font color="red">*</font>地址
							</label>
							<div class="col-md-5">
								<select name="addrId" id="addrId" class="form-control">
									<option value="">--请选择地址--</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">
								<font color="red">*</font>套餐类型
							</label>
							<div class="col-md-5">
								<label class="checkbox-inline">
									<input type="radio" name="periodServiceTypeId" value="1" checked/>套餐一 
								</label>
								<label class="checkbox-inline">
									<input type="radio" name="periodServiceTypeId" value="2" />套餐二 
								</label>
								<label class="checkbox-inline">
									<input type="radio" name="periodServiceTypeId" value="3" />套餐三
								</label>
								<label class="checkbox-inline">
									<input type="radio" name="periodServiceTypeId" value="4" />套餐四
								</label>
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>定制服务类型
							</label>
							<div></div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>支付方式
							</label>
							 <label class="checkbox-inline">
                                 <input type="radio" name="orderPayType" id="orderPayType1"
                                        value="6" checked> 现金支付
                             </label>
                             <label class="checkbox-inline">
                                 <input type="radio" name="orderPayType" id="orderPayType2"
                                        value="7"> 平台已支付
                             </label>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>订单金额
							</label>
							<div class="col-md-5">
								<input name="orderMoney" class="form-control" />
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>支付金额
							</label>
							<div class="col-md-5">
								<input name="orderPrice" class="form-control" />
							</div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>订单来源
							</label>
							<div class="col-md-5">
							 <select id="orderFrom" name="orderFrom" class="form-control">
                                <option value="">--请选择订单来源--</option>
                                <option value="1">来电订单</option>
                                <c:forEach items="${cooperativeBusiness }" var="src">
                                    <option value="${src.id }">${src.businessName }</option>
                                </c:forEach>
                             </select>
                            </div>
						</div>
						
						<div class="form-group required">
							<label class="col-md-2 control-label">
								<font color="red">*</font>订单描述
							</label>
							<div class="col-md-5">
								<input name="remarks" class="form-control" />
							</div>
						</div>
					
						<button type="button" id="btn-save" class="btn btn-success">保存</button>
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
