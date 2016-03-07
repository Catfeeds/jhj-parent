<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<html>
<head>

<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>

<!--css for this page-->
<link rel="stylesheet"
	href="<c:url value='/assets/data-tables/DT_bootstrap.css'/>"
		<%@ include file="../shared/importJs.jsp"%>

<script type="text/javascript"
	src="<c:url value='/js/jquery.treeLite.js?ver=10' /> "
	type="text/javascript"></script>
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
			用户管理 </header>

			<hr
				style="width: 100%; color: black; height: 1px; background-color: black;" />
 				<form:form modelAttribute="contentModel" class="form-horizontal" method="POST" id="authority-form">
                        <div class="form-body">
                           <div class="form-group">
                              <label  class="col-md-2 control-label">名称&nbsp;*</label>
                              <div class="col-md-5">
                                 <form:input path="name" class="form-control" placeholder="名称"/><br/>
                                 <form:errors path="name" class="field-has-error"></form:errors>
                              </div>
                           </div>
                           <div class="form-group">
                              <label  class="col-md-2 control-label">位置</label>
                              <div class="col-md-5">
                                 <form:input path="position" class="form-control" placeholder="位置"/><br/>
                                 <form:errors path="position" class="field-has-error"></form:errors>
                              </div>
                           </div>
                           <div class="form-group">
                              <label  class="col-md-2 control-label">值</label>
                              <div class="col-md-5">
                                 <form:input path="theValue" class="form-control" placeholder="值"/>
                              </div>
                           </div>
                           <div class="form-group">
                              <label  class="col-md-2 control-label">Url&nbsp;*</label>
                              <div class="col-md-5">
                                 <form:input path="url" class="form-control" placeholder="Url"/><br/>
                                 <form:errors path="url" class="field-has-error"></form:errors>
                              </div>
                           </div>
                           <div class="form-group">
                              <label  class="col-md-2 control-label">MatchUrl&nbsp;&nbsp;*</label>
                              <div class="col-md-5">
                                 <form:input path="matchUrl" class="form-control" placeholder="MatchUrl"/><br/>
                                 <form:errors path="matchUrl" class="field-has-error"></form:errors>
                              </div>
                           </div>
                           <div class="form-group">
                              <label  class="col-md-2 control-label">图标</label>
                              <div class="col-md-5">
                                 <form:input path="itemIcon" class="form-control" placeholder="图标"/>
                              </div>
                           </div>
                           <div class="form-group">
                              <label  class="col-md-2 control-label">隶属于&nbsp;*</label>
                              <div class="col-md-5">
                                 <div class="portlet" id="parentId">
					                  <div class="portlet-body">
				                     	   <c:import url = "../shared/treeSelector.jsp">
												<c:param name="propertyName" value="parentId"/>
												<c:param name="propertyValue" value="${contentModel.parentId}"/>
												<c:param name="treeDataSourceName" value="treeDataSource"/>
										   </c:import>
					                  </div>
					             </div>
                              </div>
                           </div>
                        </div>
                        <div class="form-actions fluid">
                           <div class="col-md-offset-4">
                              <button type="button" id ="btn_submit" class="btn btn-success">保存</button>
                           </div>
                        </div>
                     </form:form>
			</section>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include
		file="../shared/pageFooter.jsp"%> <!--footer end-->
	</section>
	<!--script for this page-->
    <script src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>
	<script src="<c:url value='/js/jhj/admin/authorityForm.js'/>" type="text/javascript"></script>
  
	
</body>
</html>