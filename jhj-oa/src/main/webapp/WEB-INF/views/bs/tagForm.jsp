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
	<!-- 引入百度地图API,其中   申请的密钥   ak 和主机 ip绑定， -->
	<script type="text/javascript" 
		src="http://api.map.baidu.com/api?v=1.5&ak=tiwR2mNsj7ecMtu87jd0aXuc">
	</script>
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
                                门店管理
                          </header>
                          
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                          
                          <div class="panel-body">
                              <form:form modelAttribute="tagVoFormModel" class="form-horizontal"
								method="POST" action="doTagForm?tagId=${tagVoFormModel.tagId }" id="tag-form"
								enctype="multipart/form-data">
								
								<form:hidden path="tagId"/>
								<div class="form-body">

									<div class="form-group required">

										<label class="col-md-2 control-label">标签名称*</label>
										<div class="col-md-5">
											<form:input path="tagName" class="form-control" placeholder="标签名"
												maxLength="32" onchange="valid()" />
											<form:errors path="tagName" class="field-has-error"></form:errors>
											<div id="showResult" style="float:left"></div>
										</div>
									</div>
									
									
									<div class="form-group required">
										<label class="col-md-2 control-label">标签类型</label>
										<div class="col-md-5">
											 <form:radiobutton path="tagType" value="0" label="阿姨" />
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<form:radiobutton path="tagType" value="1" label="助理" />
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<form:radiobutton path="tagType" value="2" label="用户" />
										</div>
									</div>
									
									<div class="form-group required">
										<label class="col-md-2 control-label">标签状态</label>
										<div class="col-md-5">
											<%-- <form:checkbox path="isEnable"/> --%>
											<form:radiobutton path="isEnable" value="1" label="可用" />
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<form:radiobutton path="isEnable" value="0" label="不可用" />
										</div>
									</div>
									
									
									<div class="form-actions fluid">
										<div class="col-md-offset-6 col-md-6" style="margin-left:315px">
											<button type="submit" id="tagForm_btn"
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
        
    <script type="text/javascript"	src="<c:url value='/assets/bootstrap-fileupload/fileinput.min.js'/>"></script>
	<script type="text/javascript"  src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>
	<script type="text/javascript"  src="<c:url value='/js/jhj/bs/tagForm.js'/>"></script>
  </body>
</html>

