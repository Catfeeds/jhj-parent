<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
	<%@ include file="../shared/importCss.jsp"%>
	
	<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld" %>
<html>
  <head>
 	<title>门店信息列表</title>
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
                          <header class="panel-heading">
                          	<h4>门店管理</h4>
                          	
	                          	<div class="pull-right" >
	                          		<button onClick="btn_add('bs/orgForm?orgId=0')" class="btn btn-primary" type="button"><i class="icon-expand-alt"></i>新增</button>
	                    		</div>      
                          </header>
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                          <table class="table table-striped table-advance table-hover">
                              <thead>
                              <tr>
		                              <th>城市</th>
		                              <th>门店名称</th>
		                              <th>门店地址</th>
		                              <th>电话</th>
		                              <th>店长</th>
                                  	  <th>创建时间</th>
                                  	  <th>状态</th>
                                  	  <th>操作</th>
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${orgsModel.list}" var="org">
                              <tr>
									<td>${ org.poiCity }</td>
									<td>${ org.orgName }</td>
									<td>${ org.poiCity}${ org.poiAddress }${ org.orgAddr }</td>
									<td>${ org.orgTel }</td>
									<td>${ org.orgOwner }</td>
									<td><timestampTag:timestamp patten="yyyy-MM-dd" t="${org.addTime * 1000}"/></td>
									<td>
										<c:choose>
											<c:when test="${org.orgStatus == 0 }">
												不可用
											</c:when>
											<c:otherwise>
												服务中
											</c:otherwise>
										</c:choose>
									</td>
							<td>
									<button id="btn_update"
										onClick="btn_update('bs/orgForm?orgId=${org.orgId}')"
										class="btn btn-primary btn-xs" title="修改">
										<i class="icon-pencil"></i>
									</button>
									
									<button id="btn_update"
										onClick="btn_update('group/group_list?orgId=${org.orgId}')"
										class="btn btn-primary btn-xs" title="查看云店">
										<i class="icon-search"></i>
									</button>
							</td>
						</tr>
                              </c:forEach>
                              </tbody>
                          </table>
                      </section>
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="orgsModel"/>
	        				<c:param name="urlAddress" value="/bs/group-list"/>
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
    <%@ include file="../shared/importJs.jsp"%>
  </body>
</html>