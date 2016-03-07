<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<html>
<head>

<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>

<!--css for this page-->
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
			礼包 </header>

			<hr
				style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="gifts" id="gifts-form"
					commandName="gifts" action="giftsForm"
					class="form-horizontal" method="POST">
					<form:hidden path="giftId"  id="giftId"/>
					<form:hidden path="couponId"  id="couponId"/>
		<%-- 			<input type="hidden" value="${gifts.giftCoupons.couponId}" id="couponIds">
					<input type="hidden" value="${selectDataSources.couponId}" id="sourceId"> --%>
					<div class="form-body">
						

						<div class="form-group">

							<!-- Text input-->
							<label class="col-md-2 control-label">礼包名称</label>
							<div class="col-md-5">
								<form:input path="name" class="form-control" id="giftName"
									placeholder="礼包名称" value="${gifts.name}" maxSize="10" 
									onblur="checkGiftNameExist();"
									onfocus="clearGiftNameCss();"/>
								<span class="field-has-error" id="giftNameResult"></span>
								<form:errors path="name" class="field-has-error"></form:errors>

							</div>
						</div>
<%-- 						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">有&nbsp;效&nbsp;期&nbsp;限</label>
							<div class="col-md-5">
								<form:select path="rangeMonth" class="form-control">
									<form:options items="${selectDataSource}" />
								</form:select>
							</div>
						</div> --%>
						<div class="form-group ">

						<label class="col-md-2 control-label">优惠券信息</label>
						<div class="col-md-5">

							<table id="giftCouponsTable"
								class="table table-hover table-condensed controls">
								<thead>
									<tr>
										<th>优惠券类型</th>
										<th>数量</th>
										<th>#</th>
									</tr>
								</thead>
								<c:forEach items="${gifts.giftCoupons}" var="item">
									<tr class="odd gradeX">
										<td>
										   <select id="selectCouponId" name="selectCouponId">
										   <c:forEach items="${selectDataSources}" var="selectItem">
										   		<option value="${selectItem.getKey()}"
													<c:if test="${item.couponId eq selectItem.getKey() }">
														selected = "selected" 
													</c:if>
										   		>${selectItem.getValue()}</option>
										   </c:forEach>

										   </select>
										</td>
										<td><input type="text" name="num" style="width: 100px;"
											value="${item.num}" onkeyup="this.value=this.value.replace(/\D/g,'')"  maxLength="16"
											class="form-control"></td>
										<td><span class="input-group-btn">
												<button class="btn btn-success btn-add" type="button">
													<span class="glyphicon glyphicon-plus"></span>
												</button>
										</span></td>
									</tr>
								</c:forEach>

							</table>
						</div>
					</div>
					</div>
					<div class="form-actions">
						<div class="row">
							<div class="col-md-4" align="right">
								<button class="btn btn-success" id="addCoupon_btn" type="button">
									保存</button>
							</div>
							<!-- Button -->
							<div class="col-md-8">
								<button class="btn btn-success" type="reset">重置</button>
							</div>

						</div>
					</div>
					<!-- </fieldset> -->
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
	<script
		src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"
		type="text/javascript"></script>
	<!--gift Form表单js  -->
	<script src="<c:url value='/js/jhj/bs/dictCoupons/giftsForm.js'/>"
		type="text/javascript"></script>
</body>
</html>
