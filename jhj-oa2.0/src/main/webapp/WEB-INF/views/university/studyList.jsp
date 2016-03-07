<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld" %>
<%@ taglib prefix="partServiceType" uri="/WEB-INF/tags/partnerServiceTypeName.tld" %>


<html>
  <head>
	<!--common css for all pages-->
	<%@ include file="../shared/importCss.jsp"%>
	
	<!--css for this page-->
	
	<link rel="stylesheet" href="<c:url value='/css/jquery.treetable.css'/>" type="text/css" />
	
  </head>

  <body>

  <section id="container" >
	  
	  <!--header start-->
	  <%@ include file="../shared/pageHeader.jsp"%>
	  <!--header end-->
	  
      <!--sidebar start-->
	  <%@ include file="../shared/sidebarMenu.jsp"%>
      <!--sidebar end-->
      
      <section id="main-content">
          <section class="wrapper">

            <div class="row">
                  <div class="col-lg-12">
                      <section class="panel">
                          <header class="panel-heading">
                          	 <h4>培训学习</h4>
                          	<div class="pull-right">
                          		<button onClick="btn_add('university/study_form?id=0')" class="btn btn-primary" type="button"><i class="icon-expand-alt"></i>新增</button>
                    		</div>      
                          </header>
                         
                          
                          <table class="table table-striped table-advance table-hover" id="myTable">
                              <thead>
                              <tr>
                                      <th>服务类别</th>
		                              <th>添加时间</th>
                                  	  <th>操作</th>
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${studyVoListModel.list}" var="item">
	                              <tr data-tt-id="${item.id }">
										<td>
											<partServiceType:typeId typeId="${item.serviceTypeId }"/>
										</td>
										<td>
							            	<timestampTag:timestamp patten="yyyy-MM-dd HH:mm" t="${item.addTime * 1000}"/>
							            </td>
										
										<td>
											<button id="btn_update" onClick="btn_update('university/study_form?id=${item.id}')" class="btn btn-primary btn-xs" title="修改">
													<i class="icon-pencil"></i>
											</button>
										</td>
								 </tr>
								 <%-- <tr  data-tt-id="treeChild" data-tt-parent-id="${item.id }">
								 		<td>${item.content }</td>
								 </tr> --%>
                              </c:forEach>
                              </tbody>
                          </table>
					</section>		
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="studyVoListModel"/>
	        				<c:param name="urlAddress" value="/university/study_list"/>
	       			  </c:import>
               	</div>
              </div>
          </section> 
      </section>
      
      <%@ include file="../shared/pageFooter.jsp"%>
  </section>

   <%@ include file="../shared/importJs.jsp"%> 
	  <script type="text/javascript" src="<c:url value='/js/jhj/university/questionList.js' />"></script>

	
	<!-- <script type="text/javascript">
		
		$("#myTable").treetable({ 
			
			expandable: true ,
			initialState:"collapsed",
			clickableNodeNames:true
		});
	</script> -->
	
  </body>
</html>