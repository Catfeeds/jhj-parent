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
      
      <!--main content start-->
      <section id="main-content">
          <section class="wrapper">
              <!-- page start-->

              <div class="row">
                  <div class="col-lg-12">
                      <section class="panel">
                          <header class="panel-heading">
                          	服务类型
                          	
                          	<div class="pull-right">
                          		<button onClick="btn_add('/base/serviceTypeForm?id=0')" class="btn btn-primary" type="button"><i class="icon-expand-alt"></i>新增</button>
                    		</div>      
                          </header>
                          
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                          

                          <table class="table table-striped table-advance table-hover">
                              <thead>
                              <tr>
                                      <th >名称</th>
		                             <!--  <th >关键词</th>
		                              <th >提示语</th> -->
		                              <th >单价</th>
		                              <!-- <th >折扣</th> -->
		                              <th>服务类型介绍</th>
                                  	  <th>操作</th>
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${contentModel.list}" var="item">
                              <tr>
									<td>${ item.name}</td>
									<%-- <td>${ item.keyword }</td>
									<td>${ item.tips }</td> --%>
									<td>${ item.price }</td>
									<%-- <td>${ item.disPrice }</td> --%>
									<td>${ item.descUrl }</td>
							<td>
								<button id="btn_update" onClick="btn_update('/base/serviceTypeForm?id=${item.id}')" class="btn btn-primary btn-xs" title="修改">
										<i class="icon-pencil"></i>
								</button>
								<button id="btn_add" onClick="btn_add('/base/serviceAdd-list?id=${item.id}')" class="btn btn-primary btn-xs" title="服务类型附加  ">
										<i class="icon-plus"></i>
								</button>
							</td>
						</tr>
                              </c:forEach>
                              </tbody>
                          </table>

                          
                      </section>
                      
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="contentModel"/>
	        				<c:param name="urlAddress" value="/base/service-list"/>
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
	
	

  </body>
</html>