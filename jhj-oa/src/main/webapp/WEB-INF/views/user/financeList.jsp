<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld" %>

<html>
  <head>
	
	<!--common css for all pages-->
	<%@ include file="../shared/importCss.jsp"%>
	
	<!--css for this page-->

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
                          	财务充值记录列表
                          </header>
                          
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                          

                          <table class="table table-striped table-advance table-hover">
                              <thead>
                              <tr>
                                  	  <th >用户姓名</th>
		                              <th >用户手机号</th>
		                              <th >充值前金额</th>
		                              <th >充值金额</th>
		                              <th >充值后金额</th>
		                              <th >审批手机号</th>
		                              <th >审批验证码</th>
		                              <th >充值操作人员姓名</th>
		                              <th >充值时间</th>
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${financeListModel.list}" var="item">
                              <tr>
							            <td>${ item.userName }</td>
							            <td>${ item.userMobile }</td>
							            <td>${ item.restMoneyBefore }</td>
							            <td>${ item.rechargeValue}</td>
							            <td>${ item.restMoneyAfter }</td>
							            <td>${ item.approveMobile }</td>
							            <td>${ item.approveToken }</td>
							            <td>${ item.adminName }</td>
							            <td>
							            	<timestampTag:timestamp patten="yyyy-MM-dd HH:mm" t="${item.addTime * 1000}"/>
							            </td>
                              </tr>
                              </c:forEach>
                              </tbody>
                          </table>

                          
                      </section>
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="financeListModel"/>
	        				<c:param name="urlAddress" value="/user/finace_recharge_list"/>
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