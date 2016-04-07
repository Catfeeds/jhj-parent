$('#cooBusForm').validate({
	errorElement : 'span', // default input error message container
	errorClass : 'help-block', // default input error message class
	focusInvalid : false, // do not focus the last invalid input
	rules : {
		loginName : {
			required : true
		},
		passWord : {
			required : true
		},
	confirmPassWord:{
			required : true
		},
		businessName:{
			required : true
		}
	},

	messages : {
		loginName : {
			required : "请输入登录名"
		},
		passWord: {
			required : "请输入登录密码"
		},
		confirmPassWord:{
			required : "请输入确认密码"
		},
		businessName:{
			required : "请输入商户名称"
		}
	},

	invalidHandler : function(event, validator) { // display error alert on
		// form submit
		$('.alert-error', $('#newStaff-form')).show();
	},

	highlight : function(element) { // hightlight error inputs
		$(element).closest('.form-group').addClass('has-error'); // set error
	},

	success : function(label) {
		label.closest('.form-group').removeClass('has-error');
		label.remove();
	},

	errorPlacement : function(error, element) {
		error.insertAfter(element.parents(".col-md-5"));
	}

});


$("#coopBusForm_btn").click(function(form) {
	
	if($("#roleId").find("option:selected").val() == 0){
		
		alert("请选择用户角色");
		
		return false;
	}
	
	if (confirm("确认要保存吗?")) {
		if($('#newStaff-form').validate().form()){
			$('#newStaff-form').submit();
		}
	}else{
		return false;
	}
});