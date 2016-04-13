<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<!-- taglib for this page -->
<%@ taglib prefix="partServiceType" uri="/WEB-INF/tags/partnerNoParentServiceTypeSelect.tld" %>


<html>
  <head>
	
	<!--common css for all pages-->
	<%@ include file="../shared/importCss.jsp"%>
	
	<!--css for this page-->
	<link rel="stylesheet" href="<c:url value='/assets/jquery-treetable/css/jquery.treetable.css'/>" type="text/css" />
	
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
	                         <h4>搜索</h4>
	                      	  <form:form class="form-inline" modelAttribute="questionSearchVoModel" action="question_list" method="GET">
		                         		<div class="form-group">
											服务类别:
												<partServiceType:select selectId="${questionSearchVoModel.serviceTypeId }"/>
										</div>
										<div class="form-group">		
											题库名称:
											<select id="bankId" name="bankId">
												<option value="0">请选择题库</option>
											<select>
										</div>
										<button type="submit" class="btn btn-primary" >搜索</button>
	                           </form:form>   
											
		                   </header>
                           
                      	<hr style="width: 100%; color: black; height: 1px; background-color:black;" />  
                      	
                          <header class="panel-heading">
                          	<h4>考题列表</h4>
                          	
                          	<div class="pull-right">
                          		<button onClick="btn_add('university/question_form?id=0')" class="btn btn-primary" type="button"><i class="icon-expand-alt"></i>新增</button>
                    		</div>      
                          </header>
                          
                          
                          <table class="table table-striped table-advance table-hover" id="questionTableList" >
                             <c:forEach items="${questionListVoModel.list}" var="item" varStatus="status">
                              	
	                              <tr data-tt-id="${item.qId }">	
										<td>
											${status.index +1 }&nbsp;&nbsp;&nbsp; 
											${item.questionTypeName }&nbsp;&nbsp;&nbsp;&nbsp; 
											${item.bankName }&nbsp;&nbsp;&nbsp;&nbsp; 
											${item.serviceTypeName }&nbsp;&nbsp;&nbsp;&nbsp; 
											${item.isNeedName }
										</td>
										<td>
											
											<button id="btn_update" onClick="btn_update('university/question_form?id=${item.qId}')" class="btn btn-primary btn-xs" title="修改">
															<i class="icon-pencil"></i>
											</button>
										</td>			                              
	                              </tr>
	                              <tr  data-tt-id="treeChild" data-tt-parent-id="${item.qId }">
								 		<td>
									 		<li>${item.title }</li>
								 			<c:forEach items="${item.optionList }" var="option">
								 				<li>${option.no}  ${option.title }</li>
								 			</c:forEach>
								 		</td>
								  </tr>
                              </c:forEach> 
                          </table>

                          
                      </section>
                      
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="questionListVoModel"/>
	        				<c:param name="urlAddress" value="/university/question_list"/>
	       			  </c:import>
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

    <script type="text/javascript" src="<c:url value='/assets/jquery-treetable/jquery.treetable.js'/>"></script>
    <!--script for this page-->	
    <script	type="text/javascript"  src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/js/jhj/university/questionList.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/js/jhj/university/universityServiceTypeSelect.js'/>"></script>
  </body>
</html>
