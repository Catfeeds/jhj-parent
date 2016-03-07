<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ include file="../shared/importCss.jsp"%>
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  	<title>服务人员列表</title>
	  
	<!-- common css for all pages -->
	<%@ include file="../shared/importCss.jsp"%>
	<!-- css for this page -->
	<link rel="stylesheet" href="<c:url value='/css/fileinput.css'/>" type="text/css" />
	
	<%request.setCharacterEncoding("UTF-8");%>
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
                      	<form:form modelAttribute="orgStaffSearchVoModel" action="staff-list" method="get">
                          		<div>
                          					服务人员姓名：<form:input path="name" />
                          					手机号：<form:input path="mobile"  type="number"/>
											性别：<form:select path="sex">
													<option value=""/> 请选择性别
													<form:option value="0">男</form:option>
													<form:option value="1">女</form:option>
												</form:select> 
									<input type="submit"  value="搜索"  >
								</div>   
                           </form:form>   
                          </header>
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                          <header class="panel-heading">  
                          	<h4> 服务人员列表</h4>
                          	<div class="pull-right">
                          		<button onClick="btn_add('bs/orgStaffForm?orgStaffId=0')" class="btn btn-primary" type="button"><i class="icon-expand-alt"></i>新增</button>
                    		</div>      
                          </header>

                          <table class="table table-striped table-advance table-hover">
                              <thead>
                              <tr>	  
                              		<th>门店</th>
                              		<th>助理</th>
                              		<th>服务人员姓名</th>
                              		<th>性别</th>
                              		<th>出生日期</th>
                              		<th>籍贯</th>
                              		<th>身份证号</th>
                              		<th>电话号码</th>
                              		<th>标签</th>
                              		<th>操作</th>
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${orgStaffVoModel.list}" var="orgStaff">
	                              <tr>	
	                              		<td>${ orgStaff.orgName }</td>
	                              		<td>${ orgStaff.amName }</td>
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
										<td>${ orgStaff.hukou }</td>
										<td>${ orgStaff.cardId }</td>
										<td>${ orgStaff.mobile }</td>
										<td>${ orgStaff.tagNames }</td>			
								<td>
										<button id="btn_update"
											onClick="btn_update('bs/orgStaffForm?orgStaffId=${orgStaff.staffId}')"
											class="btn btn-primary btn-xs" title="修改">
											<i class="icon-pencil"></i>
										</button>
										<button 
											onClick="btn_update('order/order-scheduling?org_staff_id=${orgStaff.staffId}')"
											class="btn btn-info" >排班</button>
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
	        				<c:param name="pageModelName" value="orgStaffVoModel"/>
	        				<c:param name="urlAddress" value="/bs/staff-list"/>
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
	<script type="text/javascript">
	/* 	function to_arrange(a){
	console.log(a);
		window.location.href="/jhj-oa/order/orderCalendarList.jsp";
		} */
	</script>
  </body>
</html>
