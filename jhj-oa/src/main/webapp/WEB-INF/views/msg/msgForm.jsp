<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<html>
<head>

<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>
<!--css for this page-->
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
			消息管理 </header>

			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<form:form modelAttribute="contentModel" action="msgForm" 
					class="form-horizontal" method="POST" id="msg-form">
					
					<form:hidden path="msgId" />
					
					<!-- 隐藏域存储 选择的时间戳 -->
					<form:hidden path="sendTime"/>
					
					<div class="form-group required">
						<label class="col-md-2 control-label">标题</label>
						<div class="col-md-5">
							<form:input path="title" class="form-control" placeholder="标题"
								id="title" maxLength="32" />
							<form:errors path="title" class="field-has-error"></form:errors>
						</div>
					</div>

					<div class="form-group required">
						<label class="col-md-2 control-label">摘要</label>
						<div class="col-md-5">
							<form:textarea path="summary" class="form-control"  rows="5" cols="50"
                                 	placeholder="不超过500个字" maxlength="500"/>
							<form:errors path="summary" class="field-has-error"></form:errors>
						</div>
					</div>
					
					<div class="form-group required">
						<label class="col-md-2 control-label">发送方式</label>
						<div class="col-md-10">
							
							<!-- <div class="row">
								<div class="col-md-2" align="right">
									<label class="radio"> 
										<input checked value="0" name="sendWay"  type="radio">立即发送
									</label>
								</div>
								<div class="col-md-2" align="left">
									<label class="radio"> 
										<input value="1" name="sendWay" type="radio">定时发送
									</label>
								</div>
							</div> -->
							
							<form:radiobutton path="sendWay" value="0" label="立即发送" />
							<form:radiobutton path="sendWay" value="1" label="定时发送" />
							
						</div>
					</div>
					
					<div class="form-group required" id="sendTimeDiv" style="display:none">
						<label class="col-md-2 control-label">发送时间</label>
						<div class="col-md-5">
							<input type="text" id="displaySendTime" value="${sendTimeDateStr}" class="form_datetime form-control" id="sendTimeSelect" readOnly style="width:200px; margin-bottom:0">
						</div>
					</div>
					
					<div class="form-group required">
						<label class="col-md-2 control-label">用户类型</label>
						<div class="col-md-10">
							<!--
							<div class="row">
							 	<div class="col-md-2" align="right">
									<label class="radio"> <input value="0" name="userType"
										type="radio"> 用户
									</label>
								</div> 
								<div class="col-md-2" align="left">
									<label class="radio"> 
										服务人员
									</label>
								</div>
								
							</div>
							-->
							<span>服务人员</span>
						</div>
					</div>

					<div class="form-group required">

						<label class="col-md-2 control-label">是否有效</label>
						<div class="col-md-10">
							<form:radiobutton path="isEnable" value="0" label="无效" />
							<form:radiobutton path="isEnable" value="1" label="有效" />
						</div>
					</div>
				
					<div class="form-actions">
						<div class="row">
							<div class="col-md-6">
								<div class="col-md-offset-7">
									<button type="button" id="editMsg_btn" class="btn btn-success">保存</button>
								</div>
							</div>
						</div>
					</div>
				</form:form>
			</div>
			</section>
		</div>
	</div>
	<!-- page end--> 
		</section> 
  </section> 
  <!--main content end--> 
  <!--footer start--> 
  	<%@ include file="../shared/pageFooter.jsp"%> 
  <!--footer end-->
	</section>

	<!-- js placed at the end of the document so the pages load faster -->
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>

	<!--script for this page-->
	<script	type="text/javascript"  
		src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>
		
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js'/>"></script>	
		
	<!-- 时间戳类库 -->
	<script type="text/javascript" src="<c:url value='/js/moment/moment-with-locales.min.js'/>"></script>
	
    <script type="text/javascript"
    	src="<c:url value='/js/jhj/msg/msgForm.js'/>"></script>
</body>
</html>
