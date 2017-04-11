<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

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
	  <%@ include file="../shared/pageHeader.jsp"%>
	  <%@ include file="../shared/sidebarMenu.jsp"%>
      <!--main content start-->
      <section id="main-content">
          <section class="wrapper">
              <div class="row">
                  <div class="col-lg-12">
                      <section class="panel">
                          <header class="panel-heading">
                          	<h4>修改用户余额</h4>
                          	
                          	<form:form class="form-inline" 
                          		modelAttribute="user" action="updateUserRestMoney" method="post">
                          		<form:hidden path="id" />
	                         	<div class="form-group">	
									用户余额:<form:input path="restMoney" class="form-control" />
								</div>
								<input type="submit" class="btn btn-primary" value="修改余额" />						
	                 		</form:form> 
                          </header>
                      </section>
                  </div>
              </div>
              <!-- page end-->
          </section> 
      </section>
      <!--main content end-->
      
      <!--footer start-->
      <%@ include file="../shared/pageFooter.jsp"%>
      <!--footer end-->
      <%@ include file="../shared/importJs.jsp"%>
  </section>
    
  </body>
  
</html>