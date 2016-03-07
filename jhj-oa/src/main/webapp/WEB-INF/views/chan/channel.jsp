<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../shared/taglib.jsp"%>

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
			<section class="panel"> <header class="panel-heading">
			渠道管理

			<div class="pull-right">
				<button onClick="btn_add('/chan/channelForm?id=0')"
					class="btn btn-primary" type="button">
					<i class="icon-expand-alt"></i>新增
				</button>
			</div>
			</header>



			<table class="table table-striped table-advance table-hover">
				<thead>
					<tr>
						<th>名称</th>
						<th>唯一标识</th>
						<th>渠道类型</th>
						<th>是否发布</th>
						<th>下载次数</th>
						<th>添加时间</th>
						<th>最后更新时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${channelModel.list}" var="item">
						<tr>
						   <td>${ item.name}</a></td>
							<td>${ item.token }</td>
							<td>${ item.channelType }</td>
							<td>${ item.isOnline }</td>
							<td>${ item.totalDownloads }</td>
							<td>${ item.addTime }</td>
							<td>${ item.updateTime }</td>
							<td>
	                            <button id="btn_update" onClick="btn_update('/chan/channelForm?id=${item.id}')" class="btn btn-primary btn-xs" title="修改"><i class="icon-pencil"></i></button>                                    
                            </td>
						</tr>
					</c:forEach>
				</tbody>
			</table>


			</section>

			<c:import url="../shared/paging.jsp">
				<c:param name="pageModelName" value="channelModel" />
				<c:param name="urlAddress" value="/chan/channel" />
			</c:import>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include
		file="../shared/pageFooter.jsp"%> <!--footer end-->
	</section>

	<!-- js placed at the end of the document so the pages load faster -->
	<!--common script for all pages-->
	<%@ include file="../shared/importJs.jsp"%>

	<!--script for this page-->
	<script src="<c:url value='/js/jhj/account/list.js'/>"></script>

</body>
</html>