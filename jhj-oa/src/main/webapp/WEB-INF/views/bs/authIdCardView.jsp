<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.jhj.oa.common.UrlHelper"%>
	
<%@ include file="../shared/taglib.jsp"%>	
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	<h4 class="modal-title" id="myModalLabel">服务人员认证信息</h4>
</div>
<div class="modal-body">
	<div class="row">
		<!--widget start-->
		<section class="panel">
			<div class="twt-feed blue-bg">
				<h1>${orgStaff.name }</h1>
				<p>
					<c:if test="${isAuthIdCard == 0 }">													
						<font color="red">未认证</font>
					</c:if>
					
					<c:if test="${isAuthIdCard == 1 }">													
						<font color="green">已认证</font>
					</c:if>
					
					<c:if test="${isAuthIdCard == 2 }">													
						<font color="red">认证失败</font>
					</c:if>
				
				</p>
				<a href="#">
					<img id="head_img" src="${orgStaff.headImg }"
						onerror="this.onerror=null;this.src='/jhj-oa/upload/headImg/default-head-img.png'" width="60px" height="60px">
				</a>
			</div>
			<ul class="nav nav-pills nav-stacked">
				<li>
					<a href="javascript:;">
						<i class="icon-time"></i>
						Mail Inbox
						<span class="label label-primary pull-right r-activity">19</span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<i class="icon-calendar"></i>
						Recent Activity
						<span class="label label-info pull-right r-activity">11</span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<i class="icon-bell-alt"></i>
						Notification
						<span class="label label-warning pull-right r-activity">03</span>
					</a>
				</li>
				<li>
					<a href="javascript:;">
						<i class="icon-envelope-alt"></i>
						Message
						<span class="label label-success pull-right r-activity">10</span>
					</a>
				</li>
			</ul>
		</section>
		<!--widget end-->
	</div>
</div>
<div class="modal-footer">
	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	<button type="button" class="btn btn-primary serviceType-confirm-btn">认证</button>
</div>

