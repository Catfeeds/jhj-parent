<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
<html>
<head>
<title>全年订制订单</title>
<%@ include file="../shared/importCss.jsp"%>

<!--css for this page-->
</head>
<body>

	<section id="container"> <!--header start--> 
	
	<%@ include file="../shared/pageHeader.jsp"%> 

	<%@ include file="../shared/sidebarMenu.jsp"%>
	<section id="main-content">
	 <section class="wrapper"> 
	 

	<div class="row">
		<div class="col-lg-12">

			<h4>全年订制订单列表</h4>

			<table class="table table-striped table-advance table-hover"
				id="table2excel">
				<thead>
					<tr>
						<th>用户手机号</th>
						<th>订单服务类型</th>
						<th>下单时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${cusYearModel.list}" var="item">
						<tr>
							<td>${item.userMobile }</td>
							<td>${item.serviceName}</td>
							<td><timestampTag:timestamp patten="yyyy-MM-dd HH:mm"
									t="${item.addTime * 1000}" /></td>
							<td>
								<button id="btn_update"
									onClick="btn_update('order/order_cus_year_detail?id=${item.id }')"
									class="btn btn-primary btn-xs" title="订单详情">
									<i class="icon-ambulance"></i>
								</button>
							</td>		
						</tr>
					</c:forEach>
				</tbody>
			</table>


			</section>

			<c:import url="../shared/paging.jsp">
				<c:param name="pageModelName" value="cusYearModel" />
				<c:param name="urlAddress" value="/order/order_cus_year_list" />
			</c:import>
		</div>
	</div>
	<!-- page end--> </section> </section> 
	
	 <%@ include file="../shared/pageFooter.jsp"%> 
	</section>

	<%@ include file="../shared/importJs.jsp"%>

</body>
</html>
