<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<!-- taglib for this page -->
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
           
			  <%@ include file="../common/cooperate/userOrderHeader.jsp"%>	
                         
                      	<hr style="width: 100%; color: black; height: 1px; background-color:black;" />  
                      	
                          <table class="table table-striped table-advance table-hover" id="table2excel">
                              <thead>
	                              <tr>
	                            	  <th >用户手机号</th>
	                               	  <th >订单数量</th>
	                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${userOrderModel.list}" var="item">
	                              <tr>	
	                              		<td>${item.userMobile }</td>
							            <td>${item.userOrderNum }</td>
	                              </tr>
                              	</c:forEach>
                              </tbody>
                          </table>
                          
                      </section>
                      
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="userOrderModel"/>
	        				<c:param name="urlAddress" value="/cooperate/cooperate_user_order"/>
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
