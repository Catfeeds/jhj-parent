<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
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
      
      <section id="main-content">
          <section class="wrapper">

            <!-- <div class="row">
                  <div class="col-lg-12"> -->
                      <section class="panel">
                          <header class="panel-heading">
                          	 <h4>服务类别列表</h4>
                          	<div class="pull-right">
                          		<button onClick="btn_add('university/partner_type_form?serviceTypeId=0')" class="btn btn-primary" type="button"><i class="icon-expand-alt"></i>新增</button>
                    		</div>      
                          </header>
                         
                          

                          
                          <table  class="table table-striped table-advance table-hover">
                              <thead>
                              <tr>
                                      <th>名称</th>
                                      <th>父级服务</th>
		                              <th>类别</th>
                                  	  <th>操作</th>
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${partnerServiceTypeListModel.list}" var="item">
                              <tr >
									<td>${ item.name}</td>
									<td>${item.parentName }</td>
									<td>
										<c:if test="${item.viewType == 0 }">
											类别
										</c:if>
										<c:if test="${item.viewType == 1 }">
											商品
										</c:if>
									</td>
									
							<td>
								<button id="btn_update" onClick="btn_update('/university/partner_type_form?serviceTypeId=${item.serviceTypeId}')" class="btn btn-primary btn-xs" title="修改">
										<i class="icon-pencil"></i>
								</button>
							</td>
							</tr>
                              </c:forEach>
                              </tbody>
                          </table>
						 </section>		
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="partnerServiceTypeListModel"/>
	        				<c:param name="urlAddress" value="/university/partner_list"/>
	       			  </c:import>
               	<!-- </div> -->
                 
          </section> 
      </section>
      
      <%@ include file="../shared/pageFooter.jsp"%>
  </section>

    <%@ include file="../shared/importJs.jsp"%> 

  </body>
</html>