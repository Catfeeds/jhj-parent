<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

	<!--taglib for this page  -->
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld" %>
<%@ taglib prefix="couponTypeNameTag" uri="/WEB-INF/tags/couponTypeName.tld" %>
<%@ taglib prefix="isUsedNameTag" uri="/WEB-INF/tags/isUsedName.tld" %>

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
                          	用户兑换优惠券列表
                          <!-- 	<div class="pull-right">'/account/register?id=0' 
                          		<button onClick="btn_add()" class="btn btn-primary" type="button"><i class="icon-expand-alt"></i>新增</button>
                    		</div>  -->     
                          </header>
                          
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                          

                          <table class="table table-striped table-advance table-hover">
                              <thead>
                              <tr>
                              	  <th >序号</th>
	                              <th >用户手机号</th>
	                              <th >用户Id</th>
	                              <th >优惠券密码</th>
	                              <th >优惠券金额</th>
	                              <th>过期时间</th>
	                              <th>是否使用</th>
	                              <th >使用时间</th>
	                              <th >对应订单号</th>
	                              <th >添加时间</th>
	                             <!--  <th>操作</th> -->
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${contentModel.list}" var="item">
                              <tr>
                                  	 <td>${ item.couponId }</td>
							            <td>${ item.mobile }</td>
							            <td>${ item.userId }</td>
							            <td>
											${ item.cardPasswd }
							            </td>
							            <td>
							            	${ item.value }
							            </td>
							            <td>
							            <timestampTag:timestamp patten="yyyy-MM-dd" t="${item.expTime * 1000}"/>

							            </td>
							            <td>
							            	<isUsedNameTag:isUsed isUsed="${item.isUsed}"/>

							            </td>
							            <td>
							            	<timestampTag:timestamp patten="yyyy-MM-dd" t="${item.usedTime * 1000}"/>
							            </td>
							            <td>${item.orderNo}  </td>

							            <td>
							            	<timestampTag:timestamp patten="yyyy-MM-dd" t="${item.addTime * 1000}"/>
							            </td>
										<%-- <td>
											<button id="btn_update" onClick="btn_update('msg/msgForm?id=${ item.id }')" class="btn btn-primary btn-xs" title="修改"><i class="icon-pencil"></i></button>
	                                  		<button id="btn_del" onClick="btn_del('/coupon/delete/${item.id}')" class="btn btn-danger btn-xs"  title="删除"><i class="icon-trash "></i></button>
										</td> --%>
							          
                              </tr>
                              </c:forEach>
                              </tbody>
                          </table>

                          
                      </section>
                      
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="contentModel"/>
	        				<c:param name="urlAddress" value="/coupon/used"/>
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
	<script src="<c:url value='/js/jhj/account/list.js'/>"></script>

  </body>
</html>