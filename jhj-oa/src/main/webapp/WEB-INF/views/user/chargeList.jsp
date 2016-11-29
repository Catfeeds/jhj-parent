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
<%@ taglib prefix="cloudOrgSelectTag" uri="/WEB-INF/tags/CloudOrgSelect.tld"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>

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
                          	<h4>会员充值列表</h4>
                          	<form:form class="form-inline" 
                          		modelAttribute="orderCardsVo" action="user-charge-list" method="GET">
                          		
	                         	<div class="form-group">	
									门店:<orgSelectTag:select selectId="${orderCardsVo.parentId }" sessionOrgId="${loginOrgId }"/>
								</div>
								<div class="form-group">	
									云店:<select name="orgId" id="orgId" class="form-control">
											<option value="0">全部</option>
										</select>
								</div>
	                         	<div class="form-group">	
									手机号:<form:input path="mobile" class="form-control" autocomplete="off" maxlengt="11"/>
								</div>
								<div class="form-group">	
									推荐人:<form:input path="staffName" class="form-control" autocomplete="off" maxlengt="11"/>
								</div>
								<div class="form-group">
	                          		开始时间：
									<form:input path="addStartTimeStr" class="form-control form_datetime"
									 style="width:110px; margin-bottom:0" readonly="true" />
								</div>
								<div class="form-group">
									结束时间：
									<form:input path="addEndTimeStr" class="form-control form_datetime" 
									style="width:110px; margin-bottom:0" readonly="true" />
								</div> 
								<button type="submit" class="btn btn-primary" >搜索</button>						
	                 		</form:form> 
                          </header>
                          <div>
                          	<label>充值总金额：</label>
                          	<span>${chargeMoney }元</span>
                          </div>
                          <hr style="width: 100%; color: black; height: 1px; background-color:black;" />

                          <table class="table table-striped table-advance table-hover">
                              <thead>
                              <tr>
                                  <th >会员手机号</th>
	                              <th >会员姓名</th>
	                              <th>充值金额</th>
	                              <th >会员余额</th>
	                              <th >会员积分</th>
	                              <th>推荐人</th>
	                              <th >会员类型</th>
	                              <th >会员来源</th>
	                              <th >充值时间</th>
	                              <th>操作</th>
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${pageList.list}" var="item">
                              <tr>
					            <td>${ item.mobile }</td>
					            <td>${ item.userName }</td>
					            <td>${item.cardMoney }</td>
					            <td>${ item.userRestMoney }</td>
					            <td>${ item.score }</td>
					            <td>${item.staffName }</td>
					            <td>
					            	<c:if test="${item.isVip==0 }">普通用户</c:if>
					            	<c:if test="${item.isVip==1 }">金牌会员</c:if>
					            	<%-- <userTypeTag:userTypeId userTypeId="${ item.userType }"/> --%>
					            </td>
					          
					            <td>
					            	<orderFromTag:orderfrom orderFrom="${item.addFrom }"/>
					            </td>
					            <td>
					            	<timestampTag:timestamp patten="yyyy-MM-dd" t="${item.addTime * 1000}"/>
					            </td>
					            <td><a href="user-pay-detail?mobile=${item.mobile }">消费明细</a></td>
                              </tr>
                              </c:forEach>
                              </tbody>
                          </table>

                          
                      </section>
                      <c:import url = "../shared/paging.jsp">
	        				<c:param name="pageModelName" value="pageList"/>
	        				<c:param name="urlAddress" value="/user/user-charge-list"/>
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
    
	<script src="<c:url value='/js/jhj/user/chargeList.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jhj/select-org-cloud.js'/>"></script>
	
  </body>
  
</html>