<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

<!-- taglib for this page -->
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
<%@ taglib prefix="orgSelectTag" uri="/WEB-INF/tags/OrgSelect.tld"%>
<%@ taglib prefix="cloudOrgSelectTag" uri="/WEB-INF/tags/CloudOrgSelect.tld" %>
<html>
<head>

<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>

<!--css for this page-->
</head>

<body>

	<section id="container"> <!--header start--> <%@ include
		file="../shared/pageHeader.jsp"%> <!--header end-->

	<!--sidebar start--> <%@ include file="../shared/sidebarMenu.jsp"%>
	<!--sidebar end--> <!--main content start--> <section id="main-content">
	<section class="wrapper"> <!-- page start-->

	<div class="row">
		<div class="col-lg-12">
			<section class="panel"> 
			
            <header class="panel-heading">
				<h4>服务人员总欠款表</h4>
				
				<form:form class="form-inline" modelAttribute="orgStaffDetailPaySearchVoModel" action="staffPayDept-list" method="GET">
                    
                    <td>选择门店:</td>
						<td><orgSelectTag:select selectId="${orgStaffDetailPaySearchVoModel.parentId }" sessionOrgId="${loginOrgId }"/></td>
						<td>选择云店:</td>
						<td>
						<select name="orgId" id="orgId" class="form-control">
							<option value="0">全部</option>
						</select>
					</td>
                    
                    
                    <%-- <div class="form-group">	
						选择云店: <form:select path="orgId">
									<form:option value="">请选择云店</form:option>
									<form:options items="${orgList}" itemValue="orgId" itemLabel="orgName" />
								</form:select>
					</div> --%>
						手机号码：<form:input path="mobile"/>
					<button type="submit" class="btn btn-primary" >搜索</button>						
                </form:form> 
			</header>
			
			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />

			<table class="table table-striped table-advance table-hover"
				id="table2excel">
				<thead>
					<tr>
						<th>服务人员姓名</th>
						<th>服务人员手机号</th>
						<th>总收入</th>
						<th>总欠款</th>
						<th>总提现</th>
						<th>可提现</th>
						<th>剩余金额</th>
						<th>添加时间</th>
						<th>更新时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${contentModel.list}" var="item">


						<tr>
							<td>${ item.name }</td>
							<td>${ item.mobile }</td>
							<td>${ item.totalIncoming }</td>
							<td>${ item.totalDept }</td>
							<td>${ item.totalCash }</td>
							<td>${ item.totalCashValid }</td>
						    <td>${ item.restMoney }</td>
							<td><timestampTag:timestamp patten="yyyy-MM-dd"
									t="${item.addTime * 1000}" /></td>
							<td><timestampTag:timestamp patten="yyyy-MM-dd"
									t="${item.updateTime * 1000}" /></td>
							<td>
							<button id="btn_view" onClick="btn_update('staff/staffPay-list?mobile=${ item.mobile }')" class="btn btn-danger btn-xs"  title="查看交易明细"><i class="icon-search "></i></button>
							</td>		
						</tr>
					</c:forEach>
				</tbody>
			</table>


			</section>
			<!--common script for all pages-->
			<%@ include file="../shared/importJs.jsp"%>
			<c:import url="../shared/paging.jsp">
				<c:param name="pageModelName" value="contentModel" />
				<c:param name="urlAddress" value="/staff/staffPayDept-list" />
			</c:import>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include
		file="../shared/pageFooter.jsp"%> <!--footer end-->
	</section>

	<!-- js placed at the end of the document so the pages load faster -->
	

	<%-- <script type="text/javascript" src="<c:url value='/assets/jquery.table2excel.js'/>"></script> --%>
	<!--script for this page-->
	<script type="text/javascript"
		src="<c:url value='/assets/jquery-validation/dist/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jhj/select-org-cloud.js'/>"></script>

</body>
</html>
