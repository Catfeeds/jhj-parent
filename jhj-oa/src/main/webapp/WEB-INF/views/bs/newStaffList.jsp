<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ include file="../shared/importCss.jsp"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld" %>
<%@ taglib prefix="cloudOrgSelectTag" uri="/WEB-INF/tags/CloudOrgSelect.tld" %>
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  	<title>工作人员列表</title>
	  
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
                       	  <form:form class="form-inline" modelAttribute="staffSearchVoModel" action="new_staff_list" method="get" >
                       	  
                          		<div class="form-group">
                          				手机号:<form:input path="mobile" class="form-control"  type="number"/>
                          		</div>	
                          		
                          		<div class="form-group">
										性别:<form:select path="sex" class="form-control">
												<option value=""/> 请选择性别
												<form:option value="0">男</form:option>
												<form:option value="1">女</form:option>
											</form:select> 
								 </div>				
								 
								 <div class="form-group">				
										选择门店: <orgSelectTag:select  selectId="${staffSearchVoModel.parentId }"/>								
								 </div>		
								 
								 <div class="form-group">	
										选择云店: <cloudOrgSelectTag:select selectId="${staffSearchVoModel.orgId }"/>
								 </div>   
									<button type="submit" class="btn btn-primary" >搜索</button>
                           </form:form> 
                          </header>
                          
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                          <header class="panel-heading">  
                          	<h4>工作人员列表</h4>
                          	<div class="pull-right">
                          		<button onClick="btn_add('newbs/new_staff_form?orgStaffId=0')" class="btn btn-primary" type="button"><i class="icon-expand-alt"></i>新增</button>
                    		</div>      
                          </header>

                          <table class="table table-striped table-advance table-hover">
                              <thead>
                              <tr>	  
                                    <th>员工头像</th>
                              		<th>门店名称</th>
                              		<th>云店名称</th>
                              		<th>服务人员姓名</th>
                              		<th>性别</th>
                              		<th>出生日期</th>
                              		<th>籍贯</th>
                              		<th>身份证号</th>
                              		<th>电话号码</th>
                              		<th>操作</th>
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${staffModel.list}" var="orgStaff">
	                              <tr>	
	                              		<td>
	                              			<img id="head_img" src="${ orgStaff.headImg }" 
	                              				width="60px" height="60px" 
	                              				onerror="this.onerror=null;this.src='/jhj-oa/upload/headImg/default-head-img.png'">
	                              		</td>
	                              		
	                              		<td>${ orgStaff.parentOrgName }</td>
	                              		<td>${ orgStaff.orgName }</td>
										<td>${ orgStaff.name }</td>
										<td>
											<c:choose>
												<c:when test="${ orgStaff.sex == 0}">
														男
												</c:when>
												<c:when test="${ orgStaff.sex == 1}">
														女
												</c:when>
											</c:choose>	
										</td>
										<td><fmt:formatDate  value="${ orgStaff.birth}" pattern="yyyy-MM-dd"/></td>
										<td>${ orgStaff.nativePlace }</td>
										<td>${ orgStaff.cardId }</td>
										<td>${ orgStaff.mobile }</td>
								<td>
										<button id="btn_update"
											onClick="btn_update('newbs/new_staff_form?orgStaffId=${orgStaff.staffId}')"
											class="btn btn-primary btn-xs" title="修改">
											<i class="icon-pencil"></i>
										</button>
										
										
										<button 
											onClick="btn_update('order/order-scheduling?org_staff_id=${orgStaff.staffId}')"
											class="btn btn-info" >排班</button>
										</button>
								</td>
							</tr>
                              </c:forEach>
                              </tbody>
                          </table>
                      
                      </section>
                      
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="staffModel"/>
	        				<c:param name="urlAddress" value="/newbs/new_staff_list"/>
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
