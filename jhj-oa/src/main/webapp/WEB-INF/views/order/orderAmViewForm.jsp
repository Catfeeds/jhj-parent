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
					
					<input type="hidden" id="parentId" value="${oaOrderListVoModel.parentServiceTypeId }">
					
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
								<form:textarea path="serviceContent" rows="5" cols="57" readonly="true" />
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
								<form:textarea path="remarksConfirm" rows="5" cols="57" maxlength="200" palceholder="最多200字"/>
							</div>
						</div>

						<div class="form-group required">

							<label class="col-md-2 control-label">订单总金额</label>
							<div class="col-md-5">
								<form:input path="orderMoney" class="form-control" placeholder="在此输入订单价格" maxLength="10" />
								<form:errors path="orderMoney" class="field-has-error"></form:errors>
							</div>
						</div>
						
						<!-- 助理订单状态 为   （已预约、深度养护）， 可以修改   服务开始时间 -->
						<c:if test="${oaOrderListVoModel.orderStatus == 1 && oaOrderListVoModel.parentServiceTypeId == 26  }">
								<div class="form-group required">
												
									<label class="col-md-2 control-label"><font color="red">服务开始时间(已预约,可调整)</font></label>
									<div class="col-md-5">
										<form:input path="serviceDateStartStr" class="form-control form_datetime" readonly="true" />
										<form:errors path="serviceDateStartStr" class="field-has-error"></form:errors>
									</div>
									
									<div class="col-md-5">
										<font color="red"><strong>深度养护类服务,需要满足  1 &lt;服务时长 &lt; 13 </strong></font>
									</div>
								</div>
								
								
								
								<div class="form-group required">
												
									<label class="col-md-2 control-label"><font color="red">服务结束时间(已预约,可调整)</font></label>
									<div class="col-md-5">
										<form:input path="serviceDateEndStr" class="form-control form_datetime" readonly="true" />
										<form:errors path="serviceDateEndStr" class="field-has-error"></form:errors>
									</div>
								</div>
								
						</c:if>
						
						<!-- 已预约的 深度养护 类 订单，修改后 展示 服务时间 ,此时不可更改-->
						<c:if test="${oaOrderListVoModel.orderStatus >= 2 && oaOrderListVoModel.parentServiceTypeId == 26  }">
								<div class="form-group required">
									<label class="col-md-2 control-label">服务开始时间<font color="red">(已确认,不可调整)</font></label>
									<div class="col-md-5">
										<form:input path="serviceDateStartStr" class="form-control" readonly="true" />
									</div>
								</div>
								
								<div class="form-group required">
									<label class="col-md-2 control-label">服务时长(小时)</label>
									<div class="col-md-5">
										<form:input path="serviceHour" class="form-control" readonly="true" />
									</div>
								</div>
						</c:if>
						
						
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
							
							<div class="form-group">
	
								<label class="col-md-2 control-label"><font color="red">当前派工阿姨</font></label>
								<div class="col-md-5">
									<form:input path="staffName" class="form-control"
										maxLength="32" readonly="true" />
									<form:errors path="staffName" class="field-has-error"></form:errors>
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
								
					
					
					<div id="staffList"  class="col-sm-8">
							<section class="panel">
								
								<header class="panel-heading control-label" style="margin-left:50px;font-weight:bold">
                             		可用派工人员列表
                         		 </header>
								<table class="table table-bordered table-hover table-condensed" 
										style="margin-left:180px;" >
									   
										<thead>
											<tr>
												<th>选派员工</th>
												<th>地区门店</th>
												<th>云店</th>
												<th>服务人员</th>
												<th>手机号</th>
												<th>距用户距离</th>
												<th>预计到达用时</th>
												<th>今日接单数</th>
												<th>是否可派工</th>
											</tr>
										</thead>
										<tbody id="allStaff">
										   <c:forEach items="${oaOrderListVoModel.voList}" var="item">
												<tr>
													<td>
														<c:if test="${item.dispathStaFlag == 1 }">
                                                  		 <input name="sample-radio"  id="radio-01" value="${item.staffId }" type="radio" > 
                                              			</c:if>
                                              			
                                              			<input  type="hidden" id="selectStaffId" name="selectStaffId" 
														value="${staffVo.staffId }">
                                              			
                                              			<input type="hidden" value="${item.distanceValue }" id="distanceValue">
													</td>
													
													<td>${ item.staffOrgName }</td>
													<td>${ item.staffCloudOrgName }</td>
													<td>${ item.name }</td>
													<td>${ item.mobile }</td>
													<td>${ item.distanceText }</td>
													<td>${ item.durationText }</td>
													<td>${ item.todayOrderNum }</td>
													<td>${item.dispathStaStr }</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
							</section>
								
								
								<div class="form-actions fluid">
									<div class="col-md-offset-6 col-md-6"
										style="margin-left: 315px">
										<button type="submit" id="viewForm" class="btn btn-success">确认派工</button>
										<c:if test="${role.role eq 'show' and oaOrderListVoModel.payType==6}">
											<a href="cancelOrder/${oaOrderListVoModel.id }" class="btn btn-success">取消订单</a>
										</c:if> 
									</div>
								</div>
						</div> 
					
					
					</c:if>
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
		src="<c:url value='/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js'/>"></script>
	
	<script
		src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"
		type="text/javascript"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-fileupload/fileinput.min.js'/>"></script>

	<script type="text/javascript"
		src="<c:url value='/js/order/orderAmViewForm.js'/>"></script>

</body>
</html>
