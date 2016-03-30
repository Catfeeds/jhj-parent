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
			<h4>助理订单详情页</h4>
			</header>

			<hr
				style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="oaOrderListVoModel" id="amView"
					class="form-horizontal" method="POST" action="disStaffByAmOrderNo"
					enctype="multipart/form-data">

					<form:hidden path="id" />
					<form:hidden path="userId" />
					<form:hidden path="orderNo" />
					<form:hidden path="flag" />
					<form:hidden path="disStatus" />
					<form:hidden path="poiLatitude"/>
					<form:hidden path="poiLongitude"/>
					<input type="hidden" id="dyanmicPickAddrName" value="${oaOrderListVoModel.pickAddrName }">
					<input type="hidden" name="pickAddrs" value="${oaOrderListVoModel.pickAddr }">
					
					<form:hidden path="orderStatus"/>
					
					<form:hidden path="staffId"/>
					<div class="form-body">
					
						<div class="form-group ">
							
								<div class="col-md-5">
									<c:if test="${oaOrderListVoModel.orderStatus != 3 && oaOrderListVoModel.orderStatus !=4}">
												<font color="red">助理类订单  只有订单状态为  已支付 或  已派工,才可以进行 派工操作 </font>
									</c:if>
									
								</div>
						</div>
					
					
						<div class="form-group ">

							<label class="col-md-2 control-label">姓名</label>
							<div class="col-md-5">
								<form:input path="userName" class="form-control" maxLength="32"
									readonly="true" />
								<form:errors path="userName" class="field-has-error"></form:errors>
							</div>
						</div>
	
						<div class="form-group">

							<label class="col-md-2 control-label">下单时间</label>
							<div class="col-md-5">

								<form:input path="orderDate" class="form-control" maxLength="32"
									readonly="true" />
								<form:errors path="orderDate" class="field-has-error"></form:errors>
							</div>
						</div>
						
						
						<div class="form-group">

							<label class="col-md-2 control-label">用户手机号</label>
							<div class="col-md-5">
								<form:input path="mobile" class="form-control" maxLength="32"
									readonly="true" />
								<form:errors path="mobile" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label">备注</label>
							<div class="col-md-5">
								<form:textarea path="serviceContent" rows="5" cols="30" readonly="true" />
							</div>
						</div>
						

						<div class="form-group">
							<label class="col-md-2 control-label">订单状态</label>
							<div class="col-md-5">
								<form:input path="orderStatusName" class="form-control"
									maxLength="32" readonly="true" />
								<form:errors path="orderStatusName" class="field-has-error"></form:errors>
							</div>
						</div>
						
						
						<div class="form-group">

							<label class="col-md-2 control-label">与用户沟通后需求描述</label>
							<div class="col-md-5">
								<form:textarea path="remarksConfirm" rows="5" cols="60" maxlength="200" palceholder="最多200字"/>
							</div>
						</div>

						<div class="form-group">

							<label class="col-md-2 control-label">订单总金额</label>
							<div class="col-md-5">
								<form:input path="orderMoney" class="form-control" placeholder="在此输入订单价格" maxLength="10" />
								<form:errors path="orderMoney" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<!-- 支付后的 价格信息 -->
						<c:if test="${oaOrderListVoModel.orderStatus >=3  }">
							<div class="form-group">
	
								<label class="col-md-2 control-label">支付金额</label>
								<div class="col-md-5">
									<form:input path="orderPay" class="form-control" maxLength="10" readonly="true"/>
									<form:errors path="orderPay" class="field-has-error"></form:errors>
								</div>
							</div>
							<div class="form-group">
	
								<label class="col-md-2 control-label">支付方式</label>
								<div class="col-md-5">
	
									<form:input path="payTypeName" class="form-control"
										maxLength="32" readonly="true" />
									<form:errors path="payTypeName" class="field-has-error"></form:errors>
								</div>
							</div>
							
							<div class="form-group">
	
								<label class="col-md-2 control-label">优惠券</label>
								<div class="col-md-5">
									<form:input path="couponValue" class="form-control"
										maxLength="32" readonly="true" />
									<form:errors path="couponValue" class="field-has-error"></form:errors>
								</div>
							</div>
						</c:if>
						
						<div id="saveOrder" class="form-actions fluid">
								<div class="col-md-offset-6 col-md-6"
									style="margin-left: 315px">
									<button type="button" id="saveOrderSubmit" class="btn btn-success">提交订单</button>
								</div>
						</div>
						
						
						
						<c:if test="${oaOrderListVoModel.orderStatus == 3 or oaOrderListVoModel.orderStatus == 4  }">						
							<div class="form-group required" id="addrMap">
								<hr
									style="width: 100%; color: black; height: 1px; background-color: black;" />
								<div class="form-group">
								<label class="col-sm-2 col-sm-2 control-label">常用地址</label>
								<div class="col-md-5">
									<form:select path="userAddrKey"  multiple="" class="form-control">
										<option value="" selected>选择用户常用地址</option>
										<form:options items="${oaOrderListVoModel.userAddrMap}" />
									</form:select>
								</div>
								<div class="col-md-5">
									<font color="red">你可以选择用户常用地址筛选或者在服务地址直接输入地址关键字，并从下拉列表中选择一项.</font>
								</div>
							</div>
							
								<div class="form-group">
									<label class="col-md-2 control-label">服务地址*</label>
									<div class="col-md-5">
										<form:input path="pickAddrName" placeholder="请输入服务地址"
											class="form-control" maxLength="30" />
										
										<div id="searchResultPanel" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>
									</div>
								</div>
	
								<div class="form-group" id="addrNum">
									<label class="col-md-2 control-label">地址门牌号*</label>
									<div class="col-md-5">
										<form:input path="pickAddr" class="form-control" maxLength="30"
											placeholder="请输入门牌号" />
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
	
							<div class="form-actions fluid">
								<div class="col-md-offset-6 col-md-6"
									style="margin-left: 315px">
									<button type="button" id="viewForm" class="btn btn-success">确认派工</button>
								</div>
							</div>
						</c:if>
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
	<!-- 引入百度地图API,其中   申请的密钥   ak 和主机 ip绑定， -->
	<script type="text/javascript"
		src="http://api.map.baidu.com/api?v=2.0&ak=2sshjv8D4AOoOzozoutVb6WT"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
	<script
		src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"
		type="text/javascript"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-fileupload/fileinput.min.js'/>"></script>

	<script type="text/javascript"
		src="<c:url value='/js/order/orderAmViewForm.js'/>"></script>

</body>
</html>
