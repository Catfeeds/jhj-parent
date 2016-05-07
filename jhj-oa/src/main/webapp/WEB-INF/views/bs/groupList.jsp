<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ include file="../shared/importCss.jsp"%>

<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld" %>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld" %>
<html>
  <head>
 	<title>小组信息列表</title>
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
                      	   <form:form class="form-inline" modelAttribute="groupSearchVoModel" action="group_list" method="GET">
	                         <header class="panel-heading">
	                         	<h4>数据搜索</h4>
									<div class="form-group">
										上级门店:
										<orgSelectTag:select selectId="${groupSearchVoModel.parentId}"/>
                          			</div>	
									<button type="submit" class="btn btn-primary" >搜索</button>															                         	
	                         </header>
                           </form:form>   
                           
                          	<h4>云店信息</h4>
                          	
	                          	<div class="pull-right" >
	                          		<button onClick="btn_add('group/groupForm?orgId=0')" class="btn btn-primary" type="button"><i class="icon-expand-alt"></i>新增</button>
	                    		</div>      
                          </header>
                          
                          <table class="table table-striped table-advance table-hover">
                              <thead>
                              <tr>
		                              <th>城市</th>
		                              <th>小组名称</th>
		                              <th>小组地址</th>
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
										onClick="btn_update('group/groupForm?orgId=${org.orgId}')"
										class="btn btn-primary btn-xs" title="修改">
										<i class="icon-pencil"></i>
									</button>
									
									<button id="btn_update"
										onClick="btn_update('newbs/new_staff_list?orgId=${org.orgId}')"
										class="btn btn-primary btn-xs" title="查看云店成员">
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
	        				<c:param name="urlAddress" value="/group/group_list"/>
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