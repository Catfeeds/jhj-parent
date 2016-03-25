<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.jhj.oa.common.UrlHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../../shared/taglib.jsp"%>

<html>
<head>

<!--common css for all pages-->
<%@ include file="../../shared/importCss.jsp"%>

<!--css for this page-->
<link rel="stylesheet"
	href="<c:url value='/assets/data-tables/DT_bootstrap.css'/>"
</head>

<body>

	<section id="container"> <!--header start--> 
	<%@ include file="../../shared/pageHeader.jsp"%> <!--header end-->

	<!--sidebar start-->
	 <%@ include file="../../shared/sidebarMenu.jsp"%>
	<!--sidebar end--> <!--main content start--> <section id="main-content">
	<section class="wrapper"> <!-- page start-->
	<div class="row">
		<div class="col-lg-12">
			<section class="panel"> 
				<header class="panel-heading">
			 			服务类别管理
			 	</header>

			<hr	style="width: 100%; color: black; height: 1px; background-color: black;" />

			<div class="panel-body">
				<div class="portlet">
					<div class="portlet-title">
						<div class="caption">
							<i class="icon-sitemap"></i>${requestScope.permissionMenu.curName}</div>
					</div>
					<div class="portlet-body">
						<div id="treeData-list"></div>
					</div>
					<div class="portlet-title tool-bottom"></div>
				</div>
 
			</div>
			</section>
		</div>
	</div>
	<!-- page end--> </section> </section> <!--main content end--> <!--footer start--> <%@ include
		file="../../shared/pageFooter.jsp"%> <!--footer end-->
	</section>

	<!-- js placed at the end of the document so the pages load faster -->
	<!--common script for all pages-->
	<%@ include file="../../shared/importJs.jsp"%>

	<script type='text/javascript' src="<c:url value='/js/jquery.treeLite.js?ver=10' /> "></script>
	<script type='text/javascript' src="<c:url value='/js/jquery.toolbarlite.js?ver=10' />"></script>

	<!--script for this page-->
		<script  >
		$(function() {
			   $("#treeData-list").treeLite({
		    	   items: <c:out value="${contentModel}" escapeXml="false"></c:out>
		    });
		
			      $(".tool-bottom").toolbarLite({
		        items: [
		            { link: true, display: "创建同级节点", css: "icon-plus", showIcon: true, url: "javascript:;",
		                click: function() {
		                    var parentId = $('#treeData-list').treeLite('parentId');
		                    var expanded = $('#treeData-list').treeLite('expandedIds');
		                    if(parentId != undefined)
		                        location.href = "<%=UrlHelper.resolveWithReturnUrl("/newbs/add/{0}",
						request.getAttribute("requestUrl"),
						request.getAttribute("requestQuery"), "expanded={1}",
						pageContext)%>".replace("{0}", parentId).replace(escape("{1}"), expanded);
		                    else
		                        alert("必须选择一个节点！");
		                    return false;
		               }
		            },
		            { splitter: true },
		            { link: true, display: "创建子级节点", css: "icon-plus", showIcon: true, url: "javascript:;",
		                click: function() {
		                    var selectedId = $('#treeData-list').treeLite('selectedId');
		                    var expanded = $('#treeData-list').treeLite('expandedIds');
		                    if(selectedId != undefined)
		                    	location.href = "<%=UrlHelper.resolveWithReturnUrl("/newbs/add/{0}",
						request.getAttribute("requestUrl"),
						request.getAttribute("requestQuery"), "expanded={1}",
						pageContext)%>".replace("{0}", selectedId).replace(escape("{1}"), expanded);
		                    else
		                        alert("必须选择一个节点！");
		                    return false;
		                }
		             },
		             { splitter: true },
		             { link: true, display: "编辑", css: "icon-edit", showIcon: true, url: "javascript:;",
		                 click: function() {
		                    var selectedId = $('#treeData-list').treeLite('selectedId');
		                    var expanded = $('#treeData-list').treeLite('expandedIds');
		                    if(selectedId != undefined)
								location.href = "<%=UrlHelper.resolveWithReturnUrl("/newbs/edit/{0}",request.getAttribute("requestUrl"),request.getAttribute("requestQuery"), "expanded={1}",pageContext)%>".replace("{0}", selectedId).replace(escape("{1}"), expanded);
		                    else
		                        alert("必须选择一个节点！");
		                    return false;
		                }
		             },
		             
		             { splitter: true },
		             { link: true, display: "删除", css: "icon-trash", showIcon: true, url: "javascript:;",
		                 click: function() {
		                    var selectedId = $('#treeData-list').treeLite('selectedId');
		                    var expanded = $('#treeData-list').treeLite('expandedIds');
		                    if(selectedId && selectedId != undefined)
		                    {
		                        if(confirm("确认删除所选节点？"))
		                        	location.href = "<%= UrlHelper.resolveWithReturnUrl("/newbs/delete/{0}", request.getAttribute("requestUrl"), request.getAttribute("requestQuery"), "expanded={1}", pageContext)%>".replace("{0}", selectedId).replace(escape("{1}"), expanded);
		                    }
		                    else
		                        alert("必须选择一个节点！");
		                    return false;
		                }
		             }
		             ]
					}); 
			});
		</script>
</body>
</html>
