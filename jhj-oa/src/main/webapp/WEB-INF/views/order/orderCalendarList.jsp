<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../shared/taglib.jsp"%>
<!--taglib for this page  -->
<html>
  <head>
	<!--common css for all pages-->
	<%@ include file="../shared/importCss.jsp"%>
	<!--css for this page-->
  <link rel="stylesheet" href="<c:url value='/css/calendar/fullcalendar.css'/>" type="text/css"/>
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
                    <input type="hidden" value="${orgStaffId}" id="org_staff_id">
				   <!-- <div class="portlet-title">
                     <div class="caption"><i class="icon-table"></i>订单日历</div>
                   </div> -->
                  
                  <tr>
                  
                   <td>
                   		<p></p>
                   		<p></p>
                   		<p></p>
                   		<p></p>
                   </td>
                   <td>
	                  <div class="portlet-body" style="width: 1000px;margin-left: 200px;">
	                     <div class="dataTables_wrapper form-inline" role="grid">
		                     <div class="table-scrollable">
		                     	<div id="calendar" class="has-toolbar"></div>
		                     </div>
	       				 </div>
	                  </div>
                   </td>
                  </tr>
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
    <script type="text/javascript" src="<c:url value='/js/calendar/moment.min.js'/>"></script>
   <script type="text/javascript" src="<c:url value='/js/calendar/fullcalendar.min.js'/>"></script>
   <script type="text/javascript" src="<c:url value='/js/lang/zh-cn.js'/>"></script>
    <!--script for this page-->	

     <script type="text/javascript" src="<c:url value='/js/jhj/order/order-calendar.js'/>"></script>
 

  </body>
</html>