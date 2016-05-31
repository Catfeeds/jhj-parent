<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="cloudOrgSelectTag" uri="/WEB-INF/tags/CloudOrgSelect.tld"%>
<html>
  <head>
	
	<!--common css for all pages-->
	<%@ include file="../shared/importCss.jsp"%>
	
	<!--css for this page-->
	<link href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>"
	     rel="stylesheet" type="text/css" />
  </head>

  <body>

  <section id="container" >
	  
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
                         	<h4>数据搜索</h4>
	                      	  <form:form modelAttribute="disAndLeaveSearchVoModel"
	                      	  class="form-inline" action="calendar_list" method="GET" id="oaSearchForm">
		                     			
										<div class="form-group">		
											选择云店:<cloudOrgSelectTag:select 
													selectId="${disAndLeaveSearchVoModel.orgId }"
													logInParentOrgId="${loginOrgId }"/>
										</div>	
										<div class="form-group">
			                          		开始时间(可选)：
											<form:input path="startTimeStr" class="form-control form_datetime"
											 style="width:110px; margin-bottom:0" readonly="true" />
										</div>
										<div class="form-group">
											结束时间：
											<form:input path="endTimeStr" class="form-control " 
											style="width:110px; margin-bottom:0" readonly="true" />
										</div> 
										
										<button type="submit" id="btnSearch" class="btn btn-primary" >搜索</button>
	                           </form:form>   
	                         </header>
                           
                      	<hr style="width: 100%; color: black; height: 1px; background-color:black;" />  
                      	
                          <header class="panel-heading">
                          	<h4>服务人员状态跟踪</h4>
                          </header>
                          
                          <table class="table table-bordered" >
                          
                          <!-- 返回json数据 ,,注意 要用 单引号！！-->
                          <input type="hidden" id="returnListData" value='${listVoModel }'>
                          
                        		<thead id="resultThead">
                        			<th>姓名</th>
	                         		<c:forEach items="${weekDateModel }" var="weekDateItem">
										  <th>${weekDateItem }</th>                     				
	                         		</c:forEach>	
                        		</thead>
                         		
								<tbody id="resultDisplay">
									
									
								</tbody >                 
								
								<div id="resultDiv">
									<tr id="resultTr" style="display:none">
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
								</div>
								
								
                          </table>
                      </section>
                  </div>
              </div>
              <!-- page end-->
          </section> 
      </section>
      <!--main content end-->
      
  </section>

    <!--common script for all pages-->
    <%@ include file="../shared/importJs.jsp"%>

    
	 <script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
	
	<script src="<c:url value='/js/staffDisAndLeave/staffDisAndLeaveList.js'/>"></script>
	
  </body>
</html>
