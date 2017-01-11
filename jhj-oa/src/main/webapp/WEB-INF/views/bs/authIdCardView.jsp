<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.jhj.oa.common.UrlHelper"%>
<%@ include file="../shared/taglib.jsp"%>

<div class="modal-body">
	<div class="row">
		<!--widget start-->
		<section class="panel">
			<div class="twt-feed blue-bg">
				<h1>${orgStaff.name }, 身份证号:${orgStaff.cardId }</h1>
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
			<div class="twt-category">

				<div class="col-lg-6">
					<ul class="list-group">
                     <li class="list-group-item" id="authTime">认证时间:${authData.authTime }</li>
                     <li class="list-group-item" id="msg">认证情况:${authData.msg }</li>
                     <li class="list-group-item" id="mobileProv">手机号归属地:${authData.mobileProv }</li>
                     
                 </ul>
				</div>
				
				<div class="col-lg-6">
					<ul class="list-group">
                     
                     <li class="list-group-item" id="sex">性别:${authData.sex }</li>
                     <li class="list-group-item" id="birthday">生日:${authData.birthday }</li>
                     <li class="list-group-item" id="address">地址:${authData.address }</li>
                 </ul>
				</div>
				
			</div>
		</section>
		<!--widget end-->
	</div>
</div>
<div class="modal-footer">
	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	<button type="button" class="btn btn-primary serviceType-confirm-btn">
	<c:if test="${isAuthIdCard != 1 }">
		认证
	</c:if>
	<c:if test="${isAuthIdCard == 1 }">
		再次认证
	</c:if>
	</button>
</div>
