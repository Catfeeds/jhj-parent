<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!--header start-->
<header class="header white-bg">
	<div class="sidebar-toggle-box">
		<div data-original-title="Toggle Navigation" data-placement="right"
			class="icon-reorder tooltips"></div>
	</div>
	<!--logo start-->
	<a href="/index" class="logo">运营平台</a>
	<!--logo end-->
	<div class="nav notify-row" id="top_menu">

	</div>
	<div class="top-nav ">
		<!--search & user info start-->
		<ul class="nav pull-right top-menu">
			<!-- <li><input type="text" class="form-control search"
				placeholder="Search"></li> -->
			<!-- user login dropdown start-->
			<li class="dropdown"><a data-toggle="dropdown"
				class="dropdown-toggle" href="#"> <img alt=""
					src="<c:url value='/img/avatar1_small.jpg'/>"> <span
					class="username">${accountAuth.name}</span> <b class="caret"></b>
			</a>
				<ul class="dropdown-menu extended logout">
					<div class="log-arrow-up"></div>
					<li><a href="/jhj-oa/account/logout"><i class="icon-key"></i> 退出</a></li>
				</ul></li>
			<!-- user login dropdown end -->
		</ul>
		<!--search & user info end-->
	</div>
</header>
