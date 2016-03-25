<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../../shared/taglib.jsp"%>

<html>
<head>

<!--common css for all pages-->
<%@ include file="../../shared/importCss.jsp"%>
<!--css for this page-->
<link rel="stylesheet" href="<c:url value='/assets/data-tables/DT_bootstrap.css'/>"

<%@ include file="../../shared/importJs.jsp"%>
<script type="text/javascript" src="<c:url value='/js/jquery.treeLite.js' /> "></script>
</head>

<body>

	<section id="container">
	<%@ include file="../../shared/pageHeader.jsp"%>

	<%@ include file="../../shared/sidebarMenu.jsp"%>
	
	<section id="main-content">
	<section class="wrapper"> 
		
	<div class="row">
		<div class="col-lg-12">
			<section class="panel"> <header class="panel-heading">
			服务类别管理 </header>

			<hr
				style="width: 100%; color: black; height: 1px; background-color: black;" />
 				<form:form enctype="multipart/form-data"
 						 modelAttribute="contentModelForm" class="form-horizontal" method="POST" id="service-form" >
 						 
                        <div class="form-body">
                        
                           <div class="form-group">
                              <label  class="col-md-2 control-label">服务名称*</label>
                              <div class="col-md-5">
                                 <form:input path="name" class="form-control" placeholder="服务名称"/>
                                 <form:errors path="name" class="field-has-error"/>
                              </div>
                           </div>
                          
                           <div class="form-group required">
                              <label  class="col-md-2 control-label">单位*</label>
                              <div class="col-md-5">
                                 <form:input path="unit" class="form-control" placeholder="单位"/>
                                 <form:errors path="unit" class="field-has-error"/>
                              </div>
                           </div>
                           
                           <div class="form-group">
                              <label  class="col-md-2 control-label">默认数量*</label>
                              <div class="col-md-5">
                                 <form:input path="defaultNum" class="form-control" placeholder="默认数量"/>
                                 <form:errors path="defaultNum" class="field-has-error"/>
                              </div>
                           </div>
                           
                           <div class="form-group">
                              <label  class="col-md-2 control-label">单价*</label>
                              <div class="col-md-5">
                                 <form:input path="price" class="form-control" placeholder="单价"/>
                                 <form:errors path="price" class="field-has-error"/>
                              </div>
                           </div>
                           
                           <div class="form-group">
                              <label  class="col-md-2 control-label">备注</label>
                              <div class="col-md-5">
                                 <form:input path="remarks" class="form-control" placeholder="备注" maxlength="100"/>
                                 <form:errors path="remarks" class="field-has-error"/>
                              </div>
                           </div>
                           
                           <div class="form-group">
                              <label  class="col-md-2 control-label">服务类别</label>
                              <div class="col-md-5">
                              	 <form:radiobutton path="viewType" value="1" label="商品" />
								 <form:radiobutton path="viewType" value="0" label="类别" />
                              </div>
                           </div>
                           
                           
                           <div class="form-group">
                              <label  class="col-md-2 control-label">是否可用</label>
                              <div class="col-md-5">
                              	 <form:radiobutton path="enable" value="1" label="可用" />
								 <form:radiobutton path="enable" value="0" label="不可用" />
                              </div>
                           </div>
                           
                           
                           <div class="form-group">
                              <label  class="col-md-2 control-label">列表排序</label>
                              <div class="col-md-5">
                              	 <form:input path="no" class="form-control" placeholder="列表排序"/>
                                 <form:errors path="no" class="field-has-error"/>
                              </div>
                           </div>
                           
                           
	                       <div class="form-group">
								<label class="col-md-2 control-label">图片</label>
								<div class="col-md-5">
									<img src="${contentModelForm.serviceImgUrl }" style="display:block;width:100%;max-width:200px;max-height:200px;"/>
								</div>
							</div>
							
	
							<div class="form-group">
								<label class="col-md-2 control-label">服务类别图标</label>
								<div class="col-md-5">
									<input id="serviceImgUrl" type="file" name="serviceImgUrl" accept="image/*"
										data-show-upload="false">
								</div>
							</div> 
                           
                           
                           <div class="form-group">
                              <label  class="col-md-2 control-label">隶属于&nbsp;*</label>
                              <div class="col-md-5">
                                 <div class="portlet" id="parentId">
					                  <div class="portlet-body">
				                     	   <c:import url = "../../shared/treeSelector.jsp">
												<c:param name="propertyName" value="parentId"/>
												<c:param name="propertyValue" value="${contentModelForm.parentId}"/>
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
	<!-- page end--> 
	</section> 
	</section> 
	<!--main content end--> 
	<!--footer start--> 
	<%@ include file="../../shared/pageFooter.jsp"%> 
	</section>
	<!--script for this page-->
	
    <script src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>
	<script src="<c:url value='/js/jhj/bs/serviceType/serviceForm.js'/>" type="text/javascript"></script>
</body>
</html>