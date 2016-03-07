$('.form-signin').validate({
	errorElement : 'span', //default input error message container
	errorClass : 'help-block', // default input error message class
	focusInvalid : false, // do not focus the last invalid input
	rules : {
		username : {
			required : true
		},
		password : {
			required : true
		},
		remember : {
			required : false
		}
	},

	messages : {
		username : {
			required : "请输入用户名。"
		},
		password : {
			required : "请输入密码。"
		}
	},

	invalidHandler : function(event, validator) { //display error alert on form submit   
		$('.alert-error', $('.form-signin')).show();
	},

	highlight : function(element) { // hightlight error inputs
		$(element).closest('.form-group').addClass('has-error'); // set error class to the control group
	},

	success : function(label) {
		label.closest('.form-group').removeClass('has-error');
		label.remove();
	},

	errorPlacement : function(error, element) {
		error.insertAfter(element.closest('.input-icon'));
	},

	submitHandler : function(form) {
		form.submit();
	}
});

$('.form-signin input').keypress(function(e) {
	if (e.which == 13) {
		if ($('.form-signin').validate().form()) {
			$('.form-signin').submit();
		}
		return false;
	}
});