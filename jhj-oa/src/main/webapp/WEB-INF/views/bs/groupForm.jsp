<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ include file="../shared/importCss.jsp"%>
<html>
  <head>
	
	<!--common css for all pages-->
	<%@ include file="../shared/importCss.jsp"%>
	
	<!--css for this page-->
	<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>"
	type="text/css" />

  </head>

  <body>

  <section id="container" >
	  
	  <!--header start-->
	  <%@ include file="../shared/pageHeader.jsp"%>
	  <!--header end-->
	  
      <!--sidebar start-->
	  <%@ include file="../shared/sidebarMenu.jsp"%>
      <!--sidebar end-->
      
<!--main content start-->
      <section id="main-content">
          <section class="wrapper">
              <!-- page start-->
              <div class="row">
                  <div class="col-lg-12">
                      <section class="panel">
                          <header class="panel-heading">
                                	小组管理
                          </header>
                          
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                          
                          <div class="panel-body">
                              <form:form modelAttribute="orgsModel" class="form-horizontal"
								method="POST" action="doGroupForm" id="org-form"
								enctype="multipart/form-data">

								<form:hidden path="orgId" />
								<form:hidden path="addTime"/>	
								<form:hidden path="poiLatitude" id="poiLatitude"/>
								<form:hidden path="poiLongitude" id="poiLongitude"/>
								
								<form:hidden path="poiAddress" id="poiAddress"/>
								<form:hidden path="poiCity" id="poiCity"/>
								
								<div class="form-body">

									<div class="form-group required">

										<label class="col-md-2 control-label">小组名称*</label>
										<div class="col-md-5">
											<form:input path="orgName" class="form-control" placeholder="小组名称"
												maxLength="32" onchange="valid()"/>
											<form:errors path="orgName" class="field-has-error"></form:errors>
											 <div id="showResult" style="float:left"></div>
										</div>
									</div>
									
									<div class="form-group required">
										<label class="col-md-2 control-label">小组电话*</label>
										<div class="col-md-5">
											<form:input path="orgTel" class="form-control" placeholder="小组电话"
												maxLength="32" />
											<form:errors path="orgTel" class="field-has-error"></form:errors>
										</div>
									</div>
									
									<%-- <div class="form-group required">
										<label class="col-md-2 control-label">店长*</label>
										<div class="col-md-5">
											<form:input path="orgOwner" class="form-control" placeholder="店长"
												maxLength="32" />
											<form:errors path="orgOwner" class="field-has-error"></form:errors>
										</div>
									</div>
									
									<div class="form-group required">

										<label class="col-md-2 control-label">店长电话*</label>
										<div class="col-md-5">
											<form:input path="orgOwnerTel" class="form-control" placeholder="店长电话"
												maxLength="32" />
											<form:errors path="orgOwnerTel" class="field-has-error"></form:errors>
										</div>
									</div> --%>
									
									<div class="form-group required">
												<label class="col-md-2 control-label">小组地址*</label>
												<!-- <input type="text"  id="location" style="margin-left:15px;border:thin solid gray"/>
												<input value="搜索" id="search" name="search" type="button" onclick="searchs();"/> -->
												
												请输入:<input type="text" id="suggestId" size="20" value="请输入位置" style="width:150px;" />
   											<div id="searchResultPanel" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>
												
										<br/><br/>
										<div style="margin-left:220px; width:520px;height:340px;border:1px solid gray" 
											id="containers"></div>
									</div>
									
									<div class="form-group required">
										<label class="col-md-2 control-label">门牌号*</label>
										<div class="col-md-5">
											<form:input path="orgAddr" class="form-control" placeholder="门牌号"
												maxLength="32" />
											<form:errors path="orgAddr" class="field-has-error"></form:errors>
										</div>
									</div>
									
									<div class="form-group required">
										<label class="col-md-2 control-label">小组状态</label>
										<div class="col-md-5">
											<c:choose>
												<c:when test="${orgsModel.orgStatus == 0 }">
														<div class="col-md-2" align="right">
															<label class="radio"> <input value="0" name="orgStatus"
																	checked type="radio"> 不可用
															</label>
														</div>
														<div class="col-md-2" align="left">
															<label class="radio"> <input value="1" name="orgStatus"
																	  type="radio"> 服务中
															</label>
														</div>
														<!-- 动态获取orgStatus的值 -->
														<form:hidden path="orgStatus"/>
												</c:when>
												<c:when test="${orgsModel.orgStatus == 1 }">	
														<div class="col-md-2" align="right">
															<label class="radio"> <input value="0" name="orgStatus"
																	 type="radio"> 不可用
															</label>
														</div>
														<div class="col-md-2" align="left">
															<label class="radio"> <input value="1" name="orgStatus"
																	 checked type="radio"> 服务中
															</label>
														</div>
														<form:hidden path="orgStatus"/>
												</c:when>
											</c:choose>
										</div>
									</div>
									<br/>								
									<div class="form-actions fluid">
										<div class="col-md-offset-6 col-md-6" style="margin-left:315px">
											<button type="submit" id="orgForm_btn"
												class="btn btn-success">保存</button>
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
    <%@ include file="../shared/importJs.jsp"%>


    <!--script for this page-->	
    
    	<!-- 引入百度地图API,其中   申请的密钥   ak 和主机 ip绑定， -->
	<script type="text/javascript" 
		src="http://api.map.baidu.com/api?v=1.5&ak=2sshjv8D4AOoOzozoutVb6WT">
	</script>
    <script type="text/javascript"	src="<c:url value='/assets/bootstrap-fileupload/fileinput.min.js'/>"></script>
	<script type="text/javascript"  src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>
	<script type="text/javascript"  src="<c:url value='/js/jhj/bs/orgForm.js'/>"></script>
  </body>
</html>

