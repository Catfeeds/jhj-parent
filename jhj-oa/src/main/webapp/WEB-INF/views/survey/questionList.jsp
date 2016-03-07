<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld" %>


<html>
  <head>
	<!--common css for all pages-->
	<%@ include file="../shared/importCss.jsp"%>
	
	<!-- 树形结构css -->
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
                          	 <h4>题库列表</h4>
                         	<div class="pull-right">
                          		<button onClick="btn_add('survey/question_form?id=0')" class="btn btn-primary" type="button"><i class="icon-expand-alt"></i>新增</button>
                    		</div>
                          </header>
                         
                          
                          <table class="table table-striped table-advance table-hover"  id="questionTableList">
                              <c:forEach items="${questionListModel.list}" var="item" varStatus="status">
                              	
	                              <tr data-tt-id="${item.qId }">	
										<td>
											${status.index +1 }&nbsp;&nbsp;&nbsp; 
											${item.questionTypeName }&nbsp;&nbsp;&nbsp;&nbsp; 
											${item.bankName }&nbsp;&nbsp;&nbsp;&nbsp; 
											
											<b>题目:</b>${item.title }&nbsp;&nbsp;&nbsp;&nbsp; 
											
											<button id="btn_add" onClick="btn_add('survey/question_form?id=0')" class="btn btn-success btn-xs" title="添加">
															<i class="icon-ok">添加</i>
											</button>
											
											<button id="btn_update" onClick="btn_update('survey/question_form?id=${item.qId}')" class="btn btn-primary btn-xs" title="修改">
															<i class="icon-pencil">修改</i>
											</button>
											 
											<button type="button" onclick="optionRelatQuestion(0,${item.qId })"  class="btn btn-info btn-xs" title="设置关联题目">
															<i class="icon-wrench">新增关联题目</i>
											</button>
											<button type="button" onclick="optionRelatQuestion(1,${item.qId })"  class="btn btn-info btn-xs" title="设置关联题目">
															<i class="icon-wrench">修改关联题目</i>
											</button>
										</td>
	                              </tr>
	                              <tr  data-tt-id="treeChild" data-tt-parent-id="${item.qId }">
								 		<td>
									 		<li><b>题目:</b>&nbsp;&nbsp;&nbsp;${item.title }</li>
								 			<c:forEach items="${item.optionList }" var="allOption" >
								 				<li  style="list-style-type:none;">
								 				
								 					${allOption.no}  ${allOption.title }  
								 					
								 					<button type="button" onclick="optionRelatContent(0,'${allOption.no }',${item.qId })"  class="btn btn-info btn-xs" title="设置关联题目">
															<i class="icon-wrench">新增关联内容</i>
													</button>
								 					
								 					<button type="button" onclick="optionRelatContent(1,'${allOption.no }',${item.qId })"  class="btn btn-info btn-xs" title="设置关联题目">
															<i class="icon-wrench">修改关联内容</i>
													</button>
								 						
								 			    </li>
								 			</c:forEach>
								 		</td>
								  </tr>
                              </c:forEach> 
                          </table>
					</section>		
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="questionListModel"/>
	        				<c:param name="urlAddress" value="/survey/question_list"/>
	       			  </c:import>
               	</div>
              </div>
          </section> 
      </section>
      
      <%@ include file="../shared/pageFooter.jsp"%>
  </section>

   <%@ include file="../shared/importJs.jsp"%> 
	
	<!--树形结构js ,还需要css-->
	<script type="text/javascript" src="<c:url value='/js/treeTable/jquery.treetable.js'/>"></script>
	
	<script type="text/javascript" src="<c:url value='/js/jhj/survey/questionList.js'/>"></script>
  </body>
</html>
<script type="text/javascript">
	/*  $('#serviceTypeId').trigger('change');  */
	
	$("#questionTableList").treetable({ 
		expandable: true ,
		initialState:"collapsed",
		clickableNodeNames:true
	});
	
</script>
