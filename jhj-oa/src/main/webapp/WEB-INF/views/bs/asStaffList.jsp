<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ include file="../shared/importCss.jsp"%>

<!-- s -->
<%@taglib prefix="astroName" uri="/WEB-INF/tags/astroName.tld" %>

<%@taglib prefix="bloodTypeName" uri="/WEB-INF/tags/bloodTypeName.tld" %>
<html>
  <head>
  	<title>助理信息列表</title>
  
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
                      	
                      	<form:form modelAttribute="orgStaffAsSearchVoModel" action="am-list" method="GET">
                          <header class="panel-heading">
                          	<h4>数据搜索</h4>
                          		<div>
                          					姓名：<form:input path="name" />
                          					手机号：<form:input path="mobile"/>
									<input type="submit"  value="搜索"  >
								</div>   
                          </header>
                           </form:form>   
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                          <header class="panel-heading">  
                          	<h4> 助理列表</h4>
                          	<div class="pull-right">
                          		<button onClick="btn_add('bs/staffAsForm?orgStaffId=0')" class="btn btn-primary" type="button"><i class="icon-expand-alt"></i>新增</button>
                    		</div>      
                          </header>
							 <script type="text/javascript"> 
							function head_img_not_found1() {
								alert(1);
							}
						 </script>
                          <table class="table table-striped table-advance table-hover">
                              <thead>
	                              <tr>	  
	                              		<th>助理头像</th>                              		
	                              		<th>姓名</th>
	                              		<th>性别</th>
	                              		<th>年龄</th>
	                              		<th>籍贯</th>
	                              		<th>电话号码</th>
	                              		<th>星座</th>
	                              		<th>血型</th>
	                              		<th>技能</th>
	                              		<th>助理收入</th>
	                              		<th>操作</th>
	                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${orgStaffAsVoModel.list}" var="orgStaffAs">
	                              <tr>	
	                              		<td ><img id="head_img" src="${ orgStaffAs.headImg }" width="60px" height="60px" onerror="this.onerror=null;this.src='/jhj-oa/upload/headImg/default-head-img.png'"></td>
	                              		<td>${ orgStaffAs.name }</td>
	                              		<td>
	                              			<c:choose>
												<c:when test="${ orgStaffAs.sex == 0}">
														男
												</c:when>
												<c:when test="${ orgStaffAs.sex == 1}">
														女
												</c:when>
											</c:choose>	
	                              		</td>
										<td><fmt:formatDate  value="${ orgStaffAs.birth}" pattern="yyyy-MM-dd"/></td>
										<td>${ orgStaffAs.hukou }</td>
										<td>${ orgStaffAs.mobile }</td>
										<td>
											<astroName:astroId astroId="${ orgStaffAs.astro }"/>
										</td>
										<td>
											<bloodTypeName:bloodTypeId bloodTypeId="${ orgStaffAs.bloodType }"/>
										</td>
										<td>${ orgStaffAs.tagNames}</td>
										<td>${ orgStaffAs.amSumMoney }</td>
								<td>
										<button id="btn_update"
											onClick="btn_update('bs/staffAsForm?orgStaffId=${orgStaffAs.staffId}')"
											class="btn btn-primary btn-xs" title="修改">
											<i class="icon-pencil"></i>
										</button>
										<button id="btn_select" 
											onClick="btn_select('bs/getOwnUser?orgStaffId=${orgStaffAs.staffId}')" 
											class="btn btn-primary btn-xs"  title="查看管辖阿姨">
											<i class="icon-search "></i>
										</button> 
								</td>
							</tr>
                              </c:forEach>
                              </tbody>
                          </table>
                      
                      </section>
                      
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="orgStaffAsVoModel"/>
	        				<c:param name="urlAddress" value="/bs/am-list"/>
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
