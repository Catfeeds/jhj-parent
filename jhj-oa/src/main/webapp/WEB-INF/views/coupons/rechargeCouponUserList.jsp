<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<!--taglib for this page  -->
<%@ taglib prefix="orderFromTag" uri="/WEB-INF/tags/orderFromName.tld" %>
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld" %>
<%@ taglib prefix="payTypeNameTag" uri="/WEB-INF/tags/payTypeName.tld" %>
<%@ taglib prefix="serviceTypeTag" uri="/WEB-INF/tags/serviceTypeName.tld" %>
<%@ taglib prefix="rangTypeNameTag" uri="/WEB-INF/tags/rangTypeName.tld" %>
<%@ taglib prefix="couponTypeNameTag" uri="/WEB-INF/tags/couponTypeName.tld" %>

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
                          	优惠券对应的用户列表
                          </header>
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                          <table class="table table-striped table-advance table-hover">
                              <thead>
	                              <tr>
	                                 <th >姓名</th>
	                         		  <th >用户手机号</th>
	                              </tr>
                              </thead>
                              <tbody>
                              <c:if test="${contentModel.list!=null}">
	                              <c:forEach items="${contentModel.list}" var="item">
		                              <tr>
		                                 <td>${ item.name }</td>
		                       			 <td>${ item.mobile }</td>
		                              </tr>
	                              </c:forEach>
                              </c:if>
                              </tbody>
                          </table>
                          
                      </section>
                      
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="contentModel"/>
	        				<c:param name="urlAddress" value="/bs/toRechargeCouponUserList"/>
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

    <!--script for this page-->	
<%-- 	<script src="<c:url value='/js/jhj/account/list.js'/>"></script> --%>

  </body>
</html>