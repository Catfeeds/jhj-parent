<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ include file="../shared/taglib.jsp"%>
<%@ include file="../shared/importCss.jsp"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld" %>
<%@ taglib prefix="cloudOrgSelectTag" uri="/WEB-INF/tags/CloudOrgSelect.tld" %>
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  	<title>工作人员地理位置</title>
	  
	<%@ include file="../shared/importCss.jsp"%>
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
                          	<h4>数据搜索</h4>
                       	  <form class="form-inline" action="staffRegion" method="post" >
                       	  
                          		<div class="form-group">
                          			手机号:<input name="mobile" class="form-control"  type="number"/>
                          		</div>	
								<button type="submit" class="btn btn-primary" >搜索</button>
                           </form> 
                          </header>
                          
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />

                          <table class="table table-striped table-advance table-hover">
                              <thead>
                              <tr>	  
                              		<th>门店名称</th>
                              		<th>云店名称</th>
                              		<th>服务人员姓名</th>
                              		<th>电话号码</th>
                              		<th>地理位置</th>
                              		<th>添加时间</th>
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${pageInfo.list}" var="staff">
	                              <tr>	
                              		<td>
                              			<c:forEach items="${orgsList }" var="org">
                              				${staff.parent_id }
                              				<c:if test="${org.orgId eq staff.parent_id && org.parentId eq 0 }">
                              					<span>${org.orgName }</span>
                              				</c:if>
                              			</c:forEach>
                              		</td>
                              		<td>
                              			<c:forEach items="${cloudOrgsList }" var="cloud">
                              				<c:if test="${cloud.orgId gt 0 && cloud.orgId eq staff.org_id }">
                              					<span>${cloud.orgName }</span>
                              				</c:if>
                              			</c:forEach>
                              		</td>
									<td>${ staff.name }</td>
									<td>${ staff.mobile }</td>
                              		<td>${ staff.poi_name }</td>
									<td>${staff.add_time}</td>
								</tr>
                              </c:forEach>
                              </tbody>
                          </table>
                      
                      </section>
                      
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="pageInfo"/>
	        				<c:param name="urlAddress" value="/newbs/staffRegion"/>
	       			  </c:import>
                  </div>
              </div>
              
              
          </section> 
      </section>
      <%@ include file="../shared/pageFooter.jsp"%>	
  </section>
    <%@ include file="../shared/importJs.jsp"%>
    
  </body>
</html>
