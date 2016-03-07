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
                             用户管理
                          </header>
                          
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                          
                          <div class="panel-body">
                              <form:form modelAttribute="channelModel" class="form-horizontal"
								method="POST" action="channelForm" id="channel-form" >

								<form:hidden path="id" />
								<div class="form-body">

									<div class="form-group required">

										<label class="col-md-2 control-label">名称</label>
										<div class="col-md-5">
											<form:input path="name" class="form-control" placeholder="名称"
												maxLength="32" />
											<form:errors path="name" class="field-has-error"></form:errors>
										</div>
									</div>
                              </div>

                                       <div class="form-group required">
										<label class="col-md-2 control-label">唯一标识</label>
										<div class="col-md-5">
											<form:input path="token" class="form-control"
										     placeholder="唯一标识" maxLength="32" />
											<form:errors path="token" class="field-has-error"></form:errors>
										</div>
									</div>


									<div class="form-group required">
										<!-- Text input-->
										<label class="col-md-2 control-label">类型</label>
										<div class="col-md-10" id="channelTypeGroup">

											<div class="row">
												<div class="col-md-2" align="right">

													<label class="radio">
														<form:radiobutton path="channelType" value="0"/>App市场
													</label>
												</div>

												<div class="col-md-2" align="left">
													<label class="radio">
													<form:radiobutton path="channelType" value="1"/>线下二维码
													</label>
												</div>

												<div class="col-md-2" align="left">
													<label class="radio">
													<form:radiobutton path="channelType" value="2"/>导流网站
													</label>
												</div>
											</div>
                                      </div>
                                    </div>
                                       <div class="form-group required">
										<label class="col-md-2 control-label">下载地址</label>
										<div class="col-md-5">
											<form:input path="downloadUrl" class="form-control"
												placeholder="下载地址"  maxLength="128"
												readonly="${channelModel.channelType == 1}"
												/>
											<form:errors path="downloadUrl" class="field-has-error"></form:errors>
											<input type="hidden" id="downloadUrlDefault" value="${channelModel.downloadUrl}"/>
										</div>
									</div>

									<div class="form-group required">
										<!-- Text input-->
										<label class="col-md-2 control-label">是否上线</label>
										<div class="col-md-10">

											<div class="row">
												<div class="col-md-2" align="right">
													<label class="radio"> <input value="0" name="isOnline"
														type="radio"> 否
													</label>
												</div>
												<div class="col-md-2" align="left">
													<label class="radio"> <input checked="checked"
														value="1" name="isOnline" type="radio"> 是
													</label>
												</div>
											</div>
                                        </div>
                                      </div>

									<div class="form-group required">

										<!-- Text input-->
										<label class="col-md-2 control-label">是否生成二维码</label>
										<div class="col-md-10">

											<div class="row">
												<div class="col-md-2" align="right">
													<label class="radio"> <input value="0" name="isQrcode"
														type="radio"> 否
													</label>
												</div>
												<div class="col-md-2" align="left">
													<label class="radio"> <input checked="checked"
														value="1" name="isQrcode" type="radio"> 是
													</label>
												</div>
											</div>
                                   </div>
									</div>

									 <c:if test="${channelModel.token != null && channelModel.token  != '' }">
										<div class="form-group ">

											<label class="col-md-2 control-label">二维码图片</label>
											<div class="col-md-5">
												<img src="${ channelModel.qrcodeUrl }"/>
                                       </div>
                                       </div>
                                        <div class="form-group ">
                                           <label class="col-md-2 control-label">短地址</label>
										   <div class="col-md-5">

											<form:input path="shortUrl" class="form-control"
												placeholder="短地址" maxLength="32" />
											<form:errors path="downloadUrl" class="field-has-error"></form:errors>
										  </div>
                                          </div>
									</c:if>

									<div class="form-actions fluid">
										<div class="col-md-offset-6 col-md-6">
										 <c:if test="${channelModel.id == 0 }">
											<button type="button" id="channelForm_btn" class="btn btn-success">保存</button>
										 </c:if>
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
    <script
		src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"
		type="text/javascript"></script>
      <script
	   src= "<c:url value='/js/jhj/chan/channelForm.js'/>"
	   type="text/javascript"></script>
    
	<script src="<c:url value='/js/jhj/demo.js'/>"></script>

  </body>
</html>
