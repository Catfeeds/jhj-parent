<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<aside>
	<div id="sidebar" class="nav-collapse ">
		<!-- sidebar menu start-->
		<ul class="sidebar-menu" id="nav-accordion">
			<!-- 			
			菜单样本
			<li class="sub-menu">
				<a href="javascript:;"> <i class="icon-laptop"></i> <span>Layouts</span></a>

				<ul class="sub">
					<li><a href="boxed_page.html">Boxed Page</a></li>
					<li><a href="horizontal_menu.html">Horizontal Menu</a></li>
					<li><a href="language_switch_bar.html">Language Switch Bar</a></li>
				</ul>
			</li> 
-->

			<c:forEach items="${sessionScope.accountAuth.accountRole.authorityMenus}" var="item" varStatus="status">

				<c:choose>
					<c:when test="${item.id eq requestScope.permissionMenu.rootId}">
						<li class="sub-menu"><a href="javascript:;" class="active">
					</c:when>
					<c:otherwise>
						<li class="sub-menu"><a href="javascript:;">
					</c:otherwise>
				</c:choose>

				<i class='${ empty item.itemIcon?"icon-list": item.itemIcon}'></i>

				<span>${ item.name }</span>
				</a>
				<ul class="sub">
				<c:forEach items="${item.childrens}" var="subItem" varStatus="subStatus">

					<c:choose>
						<c:when test="${subItem.id eq requestScope.permissionMenu.subId}">
							<li class="active">
						</c:when>
						<c:otherwise>
							<li>
						</c:otherwise>
					</c:choose>
					<a href="<c:url value='${ subItem.url }'/>">${ subItem.name }</a>
					</li>
					</c:forEach>
					</ul>
			</li>
		</c:forEach>

		</ul>
		<!-- sidebar menu end-->
	</div>
</aside>