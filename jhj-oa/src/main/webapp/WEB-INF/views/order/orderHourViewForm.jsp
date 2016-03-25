<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="citySelectTag" uri="/WEB-INF/tags/citySelect.tld"%>
<%@ taglib prefix="payTypeNameTag" uri="/WEB-INF/tags/payTypeName.tld"%>
<html>
<head>

<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>

<!--css for this page-->
<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>"
	type="text/css" />

<link
	href="<c:url value='/assets/bootstrap-datetimepicker/css/datetimepicker.css'/>"
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
			<h4>钟点工订单详情页 </h4></header>

			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="oaOrderListVoModel" class="form-horizontal"
					method="POST" action="updateStaffByOrderNo" 
					enctype="multipart/form-data">
					
					<form:hidden path="id"/>
					<form:hidden path="userId"/>
					<form:hidden path="orderNo" />
					
					<form:hidden path="orderStatus"/>
					
					<form:hidden path="staffId"/>
					
					<div class="form-body">
					
						<div class="form-group ">
							
								<div class="col-md-5">
									<c:if test="${oaOrderListVoModel.orderStatus != 2 && oaOrderListVoModel.orderStatus !=3}">
												<font color="red">只有 订单状态为  已支付 或  已派工,才可以进行 派工操作 </font>
									</c:if>
									
								</div>
						</div>
						
						<div class="form-group ">
						
							<label class="col-md-2 control-label">姓名</label>
							<div class="col-md-5">
								<form:input path="userName" class="form-control"
									maxLength="32" readonly="true"/>
								<form:errors path="userName" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group" id="nowStaff">
	
							<label class="col-md-2 control-label">当前阿姨</label>
							<div class="col-md-5">
							
								<form:input path="staffName" class="form-control"
									maxLength="32" readonly="true"/>
								<form:errors path="staffName" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<%-- <div class="form-group">

							<label class="col-md-2 control-label">下单时间</label>
							<div class="col-md-5">
								<form:input path="orderDate" class="form-control form_datetime" readonly="true" />
							</div>
							
							<div class="col-md-5">
								<font color="red">tip*:如果没有可用服务人员,可尝试更改服务时间</font>
							</div>
						</div>
						 --%>
						
						<div class="form-group">

							<label class="col-md-2 control-label">服务时间</label>
							<div class="col-md-5">
								<form:input path="serviceDateStr" class="form-control form_datetime" readonly="true" />
							</div>
							
							<div class="col-md-5">
								<font color="red">tip*:如果没有可用服务人员,可尝试更改服务时间</font>
							</div>
						</div>
						
						<div class="form-group">

							<label class="col-md-2 control-label">用户手机号</label>
							<div class="col-md-5">
								<form:input path="mobile" class="form-control"
									maxLength="32" readonly="true"/>
								<form:errors path="mobile" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group required">

							<label class="col-md-2 control-label">用户服务地址</label>
							<div class="col-md-5">								
								<form:input path="orderAddress" class="form-control"
									maxLength="32" readonly="true"/>
								<form:errors path="orderAddress" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group">

							<label class="col-md-2 control-label">优惠券</label>
							<div class="col-md-5">
								<form:input path="couponValue" class="form-control"
									maxLength="32" readonly="true"/>
								<form:errors path="couponValue" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group">

							<label class="col-md-2 control-label">订单状态</label>
							<div class="col-md-5">
								<form:input path="orderStatusName" class="form-control"
									maxLength="32" readonly="true"/>
								<form:errors path="orderStatusName" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group">

							<label class="col-md-2 control-label">总金额</label>
							<div class="col-md-5">
								<form:input path="orderMoney" class="form-control"
									maxLength="32" readonly="true"/>
								<form:errors path="orderMoney" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group">

							<label class="col-md-2 control-label">支付金额</label>
							<div class="col-md-5">
								<form:input path="orderPay" class="form-control"
									maxLength="32" readonly="true"/>
								<form:errors path="orderPay" class="field-has-error"></form:errors>
							</div>
						</div>
						<div class="form-group">

							<label class="col-md-2 control-label">支付方式</label>
							<div class="col-md-5">
								
								<form:input path="payTypeName" class="form-control"
									maxLength="32" readonly="true"/>
									<form:errors path="payTypeName" class="field-has-error"></form:errors> 
							</div>
						</div>
						
						
						<div class="form-group required">
								<label class="col-md-2 control-label">可选派工人员信息</label>
								<div class="col-md-5" id="displayProperStaff">
								
									 <c:if test="${oaOrderListVoModel.voList.size() > 0 }" >
										<c:forEach items="${oaOrderListVoModel.voList }" var="staffVo">
										    <button id="successStaff" type="button" style="margin-top:10px"
											    data-original-title="参考信息" 
											    data-content="预计到达用时:${staffVo.durationText } &nbsp;&nbsp;&nbsp;
											    			         今日派单数:${staffVo.todayOrderNum } &nbsp;&nbsp;&nbsp;
											    	                      距用户地址距离:${staffVo.distanceText }" 
											    data-placement="top" 
											    data-trigger="hover" 
											    class="btn btn-default popovers">
													
												${staffVo.name }	
												
											<input  type="hidden" id="selectStaffId" name="selectStaffId" 
													value="${staffVo.staffId }">											    
													
											<input type="hidden" id="distanceValue" value="${staffVo.distanceValue }">		
											</button>
										</c:forEach>
									 </c:if>
								 
									 <c:if test="${oaOrderListVoModel.voList.size() <= 0 }">
										  <button type="button" id="failStaff" style="margin-top:10px" disabled
											    data-original-title="员工信息" 
											    data-content="预计到达用时: 无
											    	                        今日派单数: 无
											    	                        距用户地址距离: 无" 
											    data-placement="top" 
											    data-trigger="hover" 
											    class="btn btn-default popovers">
												
												暂无可用派工											
											</button>
									 </c:if>	
								</div>
						</div>
						
						
					<%-- <hr style="width: 100%; color: black; height: 1px; background-color: black;" />
						<h4 id="allow_title">服务人员派工信息</h4>
						   <table class="table table-striped table-advance table-hover" id="allStaff">
                              <thead>
                              <tr>	  
                               	  <th >姓名 </th>
	                              <th >手机号</th>
                              </tr>
                              </thead>
                              <tbody>
                              <tr>	
                           	    <td>
                           	    	${oaOrderListVoModel.staffName}
                           	    </td>
                           	    <td>${oaOrderListVoModel.staffMobile }</td>
							    </tr>
                              </tbody>
                          </table>
						
						<div class="form-actions fluid">
							<div class="col-md-offset-6 col-md-6" style="margin-left: 315px">
								<button type="button" id="viewForm" class="btn btn-success">暂无派工信息</button>
							</div>
						</div> --%>
						
						<div class="form-actions fluid">
							<div class="col-md-offset-3 col-md-3" >
								<button type="button" class="btn btn-success" id="submitForm" >保存修改</button>
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
		src="<c:url value='/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js'/>"></script>
	<script
		src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"
		type="text/javascript"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-fileupload/fileinput.min.js'/>"></script>

	<script type="text/javascript" src="<c:url value='/js/order/orderHourViewForm.js'/>"></script>

</body>
</html>
