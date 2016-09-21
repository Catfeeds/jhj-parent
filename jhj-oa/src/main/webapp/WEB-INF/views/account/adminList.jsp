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
                          	用户管理
                          	
                          	<div class="pull-right">
                          		<button onClick="btn_add('account/register?id=0')" class="btn btn-primary" type="button"><i class="icon-expand-alt"></i>新增</button>
                    		</div>      
                          </header>
                          
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                          

                          <table class="table table-striped table-advance table-hover">
                              <thead>
                              <tr>
                                  <th>用户名</th>
                                  <th>姓名</th>
                                  <th>邮箱</th>
                                  <th>角色</th>
                                  <th>是否可用</th>
                                  <th>所属门店</th>
                                  <th>注册时间</th>
                                  <th>操作</th>
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${contentModel.list}" var="item">
                              <tr>
                                  	<td>${ item.username }</td>
						            <td>${ item.name }</td>
						            <td>${ item.email }</td>
						            <td>${ item.roleName }</td>
						            <td>${ item.enable }</td>
						            <td>
						            	<c:if test="${item.orgsId==0 }">
						            		<span>--</span>
						            	</c:if>
						            	<c:if test="${item.orgsId!=0 }">
							            	<c:forEach items="${org }" var="o">
							            		<c:if test="${item.orgsId==o.orgId }">
							            			<span>${o.orgName }</span>
							            		</c:if>
							            		
							            	</c:forEach>
						            	</c:if>
						            </td>
						            <td><fmt:formatDate value="${item.registerTime}" pattern="yyyy-MM-dd HH:mm:ss " /></td>
                                  	<td>
	                                    <button id="btn_update" onClick="btn_update('/account/register?id=${item.id}')" class="btn btn-primary btn-xs" title="修改"><i class="icon-pencil"></i></button>
	                                    <button id="btn_del" onClick="btn_del('/account/delete/${item.id}')" class="btn btn-danger btn-xs"  title="删除"><i class="icon-trash "></i></button>
                                  	</td>
                              </tr>
                              </c:forEach>
                              </tbody>
                          </table>

                          
                      </section>
                      
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="contentModel"/>
	        				<c:param name="urlAddress" value="/account/list"/>
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
	<%-- <script src="<c:url value='/js/jhj/account/list.js'/>"></script> --%>

  </body>
</html>