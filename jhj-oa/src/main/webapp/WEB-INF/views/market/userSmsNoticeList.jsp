<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../shared/taglib.jsp"%>
<!--taglib for this page-->
<%@ taglib prefix="timestampTag" uri="/WEB-INF/tags/timestamp.tld"%>
<html>
<head>
<!--common css for all pages-->
<%@ include file="../shared/importCss.jsp"%>
<!--css for this page-->
</head>
<body>
	<section id="container"> <!--header start--> <%@ include file="../shared/pageHeader.jsp"%>
	<!--header end--> <!--sidebar start--> <%@ include file="../shared/sidebarMenu.jsp"%> <!--sidebar end-->
	<!--main content start--> <section id="main-content"> <section class="wrapper">
	<div class="row">
		<div class="col-lg-12">
			<section class="panel"> <header class="panel-heading">
			<h4>定期发送优惠劵列表</h4>
			<form:form class="form-inline"  modelAttribute="searchModel" method="GET">
				<div class="form-group">
					手机号:
					<form:input path="mobile" class="form-control" autocomplete="off" maxlengt="11" />
				</div>
				<div class="form-group">
				<form:select path="lastMonth" class="form-control">
					<form:option value="">全部</form:option>
					<form:option value="1">超过一个月未下单</form:option>
					<form:option value="2">超过2个月未下单</form:option>
					<form:option value="3">超过3个月未下单</form:option>
				</form:select>
				</div>
				<button type="submit" class="btn btn-primary">搜索</button>
			</form:form> </header>
			<hr style="width: 100%; color: black; height: 1px; background-color: black;" />
			<table class="table table-striped table-advance table-hover">
				<thead>
					<tr>
						<th>时间</th>
						<th>会员手机号</th>
						<th>短信模板</th>
						<th>备注</th>
						<th>发送状态</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${contentModel.list}" var="item">
						<tr>
							<td><timestampTag:timestamp patten="yyyy-MM-dd HH:mm" t="${item.addTime * 1000}" /></td>
							<td><a href="#" onclick="btn_add_blank('/order/order-list?mobile=${item.mobile}')">${ item.mobile }</a></td>
							<td>${ item.smsTemplateId }</td>
							<td>${ item.remarks }</td>
							
							<td>
								<c:if test="${item.smsReturn == '000000'}">
									发送成功
								</c:if> 
								<c:if test="${item.isSuceess != '000000'}">
									发送失败
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</section>
			<c:import url="../shared/paging.jsp">
				<c:param name="pageModelName" value="contentModel" />
				<c:param name="urlAddress" value="/market/sms-notice-list" />
			</c:import>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include file="../shared/pageFooter.jsp"%>
	<!--footer end--> </section>
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>
	<!--script for this page-->
</body>
</html>