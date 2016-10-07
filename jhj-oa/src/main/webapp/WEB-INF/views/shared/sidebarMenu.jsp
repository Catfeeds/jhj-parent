<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<aside>
	<div id="sidebar" class="nav-collapse ">
		<!-- sidebar menu start-->
		<ul class="sidebar-menu" id="nav-accordion">
			
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

					<li id="menu-sub-id-${subItem.id}">
						<a href="<c:url value='${ subItem.url }'/>" onclick="setSubMenuId('menu-sub-id-${subItem.id}')">${ subItem.name }</a>
					</li>
					</c:forEach>
					</ul>
			</li>
		</c:forEach>

		</ul>
		<!-- sidebar menu end-->
	</div>
</aside>