<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ include file="../shared/importCss.jsp"%>
<html>
  <head>
  	<title>标签信息列表</title>
  
	<!-- common css for all pages -->
	<%@ include file="../shared/importCss.jsp"%>
	<!-- css for this page -->
	<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>" type="text/css" />
	
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
                      	
                      	<form:form modelAttribute="tagSearchVoModel" action="tag-list" method="GET">
                          <header class="panel-heading">
                          	<h4>数据搜索</h4>
                          		<div>
                          					标签名：<form:input path="tagName" />
                          				      标签类型：<form:select path="tagType">
													<option value=""/> 请选择标签类型
													<form:option value="0">阿姨</form:option>
													<form:option value="1">助理</form:option>
													<form:option value="2">用户</form:option>
												   </form:select> 
									<input type="submit"  value="搜索"  >
								</div>   
                          </header>
                           </form:form>   
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                          <header class="panel-heading">  
                          	<h4> 标签列表</h4>
                          	<div class="pull-right">
                          		<button onClick="btn_add('bs/tagForm?tagId=0')" class="btn btn-primary" type="button"><i class="icon-expand-alt"></i>新增</button>
                    		</div>      
                          </header>

                          <table class="table table-striped table-advance table-hover">
                              <thead>
	                              <tr>	  
	                              		<th>标签名</th>                              		
	                              		<th>标签类型</th>
	                              		<th>是否可用</th>
	                              		<th>操作</th>
	                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${tagVoModel.list}" var="tag">
	                              <tr>	
	                              		<td>${ tag.tagName }</td>
	                              		<td>
	                              			<c:choose>
												<c:when test="${ tag.tagType == 0}">
														阿姨
												</c:when>
												<c:when test="${ tag.tagType == 1}">
														助理
												</c:when>
												<c:when test="${ tag.tagType == 2}">
														用户
												</c:when>
											</c:choose>	
	                              		</td>
										<td>
											<c:choose>
												<c:when test="${ tag.isEnable == 0}">
														不可用
												</c:when>
												<c:when test="${ tag.isEnable == 1}">
														可用
												</c:when>
											</c:choose>	
										</td>
								<td>
										<button id="btn_update"
											onClick="btn_update('bs/tagForm?tagId=${tag.tagId}')"
											class="btn btn-primary btn-xs" title="修改">
											<i class="icon-pencil"></i>
										</button>
										<!-- <button id="btn_del" 
											onClick="btn_del('bs/delete/?orgId=')" 
											class="btn btn-danger btn-xs"  title="删除">
											<i class="icon-trash "></i>
										</button> -->
								</td>
							</tr>
                              </c:forEach>
                              </tbody>
                          </table>
                      
                      </section>
                      
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="tagVoModel"/>
	        				<c:param name="urlAddress" value="/bs/tag-list"/>
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
    <!--common script for all pages-->
    <%@ include file="../shared/importJs.jsp"%>

  </body>
</html>
