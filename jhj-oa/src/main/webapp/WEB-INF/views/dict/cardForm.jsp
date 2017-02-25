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
			<section class="panel"> <header class="panel-heading"> 充值卡管理 </header>

			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="contentModel" class="form-horizontal" method="POST" action="cardForm" id="card-form">

					<form:hidden path="id" />
					<div class="form-body">

						<div class="form-group required">

							<label class="col-md-2 control-label">名称</label>
							<div class="col-md-5">
								<form:input path="name" class="form-control" placeholder="名称" maxLength="32" />
								<form:errors path="name" class="field-has-error"></form:errors>
							</div>
						</div>
					</div>
					<div class="form-body">

						<div class="form-group required">

							<label class="col-md-2 control-label">充值金额</label>
							<div class="col-md-5">
								<form:input path="cardValue" class="form-control" placeholder="充值金额" maxLength="32" />
								<form:errors path="cardValue" class="field-has-error"></form:errors>
							</div>
						</div>
					</div>
					<div class="form-body">

						<div class="form-group required">

							<label class="col-md-2 control-label">支付金额</label>
							<div class="col-md-5">
								<form:input path="cardPay" class="form-control" placeholder="支付金额" maxLength="32" />
								<form:errors path="cardPay" class="field-has-error"></form:errors>
							</div>
						</div>
					</div>
					<div class="form-body">

						<div class="form-group required">

							<label class="col-md-2 control-label">描述信息</label>
							<div class="col-md-5">
								<form:input path="description" class="form-control" placeholder="描述信息" maxLength="32" />
								<form:errors path="description" class="field-has-error"></form:errors>
							</div>
						</div>
					</div>

					<div class="form-body">

						<div class="form-group required">

							<label class="col-md-2 control-label">赠送礼包</label>
							<div class="col-md-5">
								<select id="selectGiftId" name="selectGiftId" class="form-control">
									<option value="0">不赠送</option>
									<c:forEach items="${gifts}" var="selectItem">
										<option value="${selectItem.getGiftId()}"
											<c:if test="${contentModel.giftId eq selectItem.getGiftId() }">
															selected = "selected" 
											</c:if>>${selectItem.getName()}</option>
									</c:forEach>

								</select>
							</div>
						</div>
					</div>


					<div class="form-actions fluid">
						<div class="col-md-offset-6 col-md-6">
							<%-- <c:if test="${channelModel.id == 0 }"> --%>
							<button type="submit" id="cardForm_btn" class="btn btn-success">保存</button>
							<%--  </c:if> --%>
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
	<script src="<c:url value='/js/jhj/dict/cardForm.js'/>" type="text/javascript"></script>

	<script src="<c:url value='/js/jhj/demo.js'/>"></script>

</body>
</html>
