<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<html>
  <head>
	
	<!--common css for all pages-->
	<%@ include file="../shared/importCss.jsp"%>
	
	<!--css for this page-->
	 <%@ include file="../shared/importJs.jsp"%>
	<script type="text/javascript" src="<c:url value='/js/jquery.treeLite.js?ver=10' /> " type="text/javascript"></script>
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
                              <form:form modelAttribute="adminRole" action="roleForm" id="role-form" class="form-horizontal tasi-form" method="POST">
                             	  <form:hidden path="id" />
                                  <div class="form-group">
                                      <label class="col-sm-2 col-sm-2 control-label">角色名称</label>
                                      <div class="col-sm-5">
                                      		<form:input path="name" class="form-control placeholder-no-fix" autocomplete="off" placeholder="姓名"/><br/>
											<form:errors path="name" class="field-has-error"></form:errors>
                                      </div>
                                  </div>
                                  <div class="form-group">
                                      <label class="col-sm-2 col-sm-2 control-label">绑定权限</label>
                                      <div class="col-sm-5">
                                      <div class="portlet-body" id="authorityId" name="authorityId">
					                  	   <c:import url = "../shared/treeSelector.jsp">
											 <c:param name="propertyName" value="authorityIds"/>
											 <c:param name="propertyValue" value="${adminRole.getAuthorityIdsString()}"/>
											 <c:param name="checkbox" value="true"/>
											 <c:param name="treeDataSourceName" value="treeDataSource"/>
										   </c:import>
					                  </div>
                                      </div>
                                  </div>     
                                  
                                  
                                  <div class="form-actions fluid">
                       
										<div class="col-md-offset-3">
											<button type="button" id="btn_submit" class="btn btn-success">保存</button>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<button class="btn btn-success" type="reset">重置</button>	
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
<%--     <%@ include file="../shared/importJs.jsp"%> --%>


    <!--script for this page-->	
    <script src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>
	<script src="<c:url value='/js/jhj/admin/roleForm.js'/>" type="text/javascript"></script>
  </body>
</html>
