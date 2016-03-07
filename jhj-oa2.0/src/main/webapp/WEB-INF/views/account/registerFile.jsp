<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!-- BEGIN BODY -->
<body class="login" >
	<!-- BEGIN LOGIN -->
            <div class="col-md-2"></div>
            <div class="col-md-5">
		<!-- BEGIN REGISTRATION FORM -->
		<form:form modelAttribute="contentModel" class="register-form" method="POST">
			<h3 align="center">新增用户</h3>
			<p>请输入个人信息：</p>
			<div class="form-group">
				<label class="control-label visible-ie8 visible-ie9">姓名</label>
				<div class="input-icon">
					<i class="icon-font"></i>
					<form:input path="name" class="form-control placeholder-no-fix" autocomplete="off" placeholder="姓名"/><br/>
					<form:errors path="name" class="field-has-error"></form:errors>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label visible-ie8 visible-ie9">昵称</label>
				<div class="input-icon">
					<i class="icon-font"></i>
					<form:input path="nickname" class="form-control placeholder-no-fix" autocomplete="off" placeholder="昵称"/><br/>
					<form:errors path="nickname" class="field-has-error"></form:errors>
				</div>
			</div>
			<div class="form-group">
				<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
				<label class="control-label visible-ie8 visible-ie9">邮箱</label>
				<div class="input-icon">
					<i class="icon-envelope"></i>
					<form:input path="email" class="form-control placeholder-no-fix" autocomplete="off" placeholder="邮箱"/><br/>
					<form:errors path="email" class="field-has-error"></form:errors>
				</div>
			</div>
			<p>请输入账户信息：</p>
			<div class="form-group">
				<label class="control-label visible-ie8 visible-ie9">用户名</label>
				<div class="input-icon">
					<i class="icon-user"></i>
					<form:input path="username" class="form-control placeholder-no-fix" autocomplete="off" placeholder="用户名"/><br/>
					<form:errors path="username" class="field-has-error"></form:errors>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label visible-ie8 visible-ie9">密码</label>
				<div class="input-icon">
					<i class="icon-lock"></i>
					<form:password path="password" class="form-control placeholder-no-fix" autocomplete="off" placeholder="密码"/><br/>
					<form:errors path="password" class="field-has-error"></form:errors>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label visible-ie8 visible-ie9">确认密码</label>
				<div class="controls">
					<div class="input-icon">
						<i class="icon-ok"></i>
						<form:password path="confirmPassword" class="form-control placeholder-no-fix" autocomplete="off" placeholder="确认密码"/><br/>
						<form:errors path="confirmPassword" class="field-has-error"></form:errors>
					</div>
				</div>
			</div>
			<p>请选择账户绑定角色：</p>
			<div class="form-group">
				<!-- <label class="col-md-4 control-label">绑定角色</label> -->
				<div class="col-md-5">
					<form:select path="roleId" class="form-control">
						<option value="">请选择</option>
						<!-- <option value="1">系统管理员</option>
						<option value="2">管家部</option>
						<option value="3">运营部</option>
						<option value="4">研发部</option> -->
						<form:options items="${selectDataSource}" /> 
					</form:select>
				</div>
			</div>
			<div class="form-group" id="divIM">
				<label class='col-md-4 control-label'>环&nbsp;&nbsp;信&nbsp;&nbsp;IM</label>
				<div class='col-md-5'>
					<form:input path='imUsername' class='form-control'
						placeholder='环信IM' />
					<br />
					<form:errors path='imUsername' class='field-has-error'></form:errors>
				</div>
			</div> 

			<!-- <div class="form-group">
				<label>
					<form:checkbox path="agreement"/> 我同意 <a href="#">服务条款</a> 和 <a href="#">隐私政策</a>
				</label>
				<div id="register_agreement_error"></div>
				<form:errors path="agreement" class="field-has-error"></form:errors>
			</div> -->
			<div class="form-actions">
				<button id="register-back-btn" type="button" class="btn btn-default" onclick="javascript:history.back(-1);">
				<i class="m-icon-swapleft">  返回</i>
				</button> 
				<button type="submit" id="register-submit-btn" class="btn btn-info pull-right">
				注册 <i class="m-icon-swapright m-icon-white"></i>
				</button>
			</div>			
			<div class="col-md-5"></div>
		</form:form>
		</div>
		<!-- END REGISTRATION FORM -->
		   <script type="text/javascript">
   	  $(function() {
   		 $("#divIM").hide();
         App.init();
      });
   		$("select option").click(function(){
   	    var id = $(this).attr("value");
   	    if(id==2){
   	    	$("#divIM").show();
   	    }else{
   	    	$("#divIM").hide();
   	    }
   	});
   </script>
</body>