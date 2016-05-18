<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

	<!--taglib for this page-->
<%@ taglib prefix="orderFromTag" uri="/WEB-INF/tags/orderFromName.tld" %>
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld" %>
<%@ taglib prefix="payTypeNameTag" uri="/WEB-INF/tags/payTypeName.tld" %>
<%@ taglib prefix="partnerPayTypeNameTag" uri="/WEB-INF/tags/partnerPayTypeName.tld" %>
<%@ taglib prefix="orderFromTag" uri="/WEB-INF/tags/orderFromName.tld" %>
<%@ taglib prefix="userTypeTag" uri="/WEB-INF/tags/userTypeName.tld" %>

<html>
  <head>
	
	<!--common css for all pages-->
	<%@ include file="../shared/importCss.jsp"%>
	
	<!--css for this page-->
	<link href="<c:url value='/assets/bootstrap-datepicker/css/bootstrap-datepicker3.min.css'/>"
	     rel="stylesheet" type="text/css" />
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
              <div class="row">
                  <div class="col-lg-12">
                      <section class="panel">
                          <header class="panel-heading">
                          	<h4>会员列表</h4>
                          	
                          	<form:form class="form-inline" onsubmit="return checkEndTime()"
                          		modelAttribute="userListSearchVoModel" action="user-list" method="GET">
	                         	<div class="form-group">	
									手机号:<form:input path="mobile" class="form-control" autocomplete="off" maxlengt="11"/>
								</div>
								
								<div class="form-group">
	                          		开始时间：
									<form:input path="startTimeStr" class="form-control form_datetime"
									 style="width:110px; margin-bottom:0" readonly="true" />
								</div>
								<div class="form-group">
									结束时间：
									<form:input path="endTimeStr" class="form-control form_datetime" 
									style="width:110px; margin-bottom:0" readonly="true" />
								</div> 
								
								<button type="submit" class="btn btn-primary" >搜索</button>						
	                 		</form:form> 
                          	
                          </header>
                          
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />

                          <table class="table table-striped table-advance table-hover">
                              <thead>
                              <tr>
                                  	  <th >会员手机号</th>
		                              <th >手机号归属地</th>
		                              <th >会员姓名</th>
		                              <th >会员余额</th>
		                              <th >会员积分</th>
		                              <th >会员类型</th>
		                              <th >会员来源</th>
		                              <th >添加时间</th>
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${userList.list}" var="item">
                              <tr>
							            <td>${ item.mobile }</td>
							            <td>${ item.provinceName }</td>
							            <td>${ item.name }</td>

							            <td>${ item.restMoney }</td>
							            <td>
											${ item.score }
							            </td>
							          
							            <td>
							            	<userTypeTag:userTypeId userTypeId="${ item.userType }"/>
							            </td>
							          
							            <td>
							            	<orderFromTag:orderfrom orderFrom="${item.addFrom }"/>
							            </td>
							            <td>
							            	<timestampTag:timestamp patten="yyyy-MM-dd" t="${item.addTime * 1000}"/>
							            </td>

                              </tr>
                              </c:forEach>
                              </tbody>
                          </table>

                          
                      </section>
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="userList"/>
	        				<c:param name="urlAddress" value="/user/user-list"/>
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

    <!--script for this page-->	
    
    <script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/js/bootstrap-datepicker.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/assets/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'/>"></script>
    
	<script src="<c:url value='/js/jhj/user/userList.js'/>"></script>
	
  </body>
  
</html>