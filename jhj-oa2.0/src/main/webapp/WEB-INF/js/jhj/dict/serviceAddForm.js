var formVal = $('#serviceAdd-form').validate({
	errorElement : 'span', // default input error message container
	errorClass : 'help-block', // default input error message class
	focusInvalid : false, // do not focus the last invalid input
	rules : {
		serviceType:{
			required : true,
		},
		
		name : {
			required : true
		},
		
		price : {
			required : true,

			number:true
		},

	},

	messages : {

		serviceType:{
			required : "请输入服务类型"
		},
		
		name : {
			required : "请输入名称"
		},
		
		price:{
			required: "请输入价格(单位：元)",
			number: "请输入数字"
		},

	},

	highlight : function(element) { // hightlight error inputs
		$(element).closest('.form-group').addClass('has-error'); // set error
																	// class to
																	// the
																	// control
																	// group
	},

	success : function(label) {
		label.closest('.form-group').removeClass('has-error');
		label.remove();
	},

	errorPlacement : function(error, element) {
		error.insertAfter(element.parents(".col-md-5"));
	}
});

$("#serviceAddForm_btn").click(function() {
	if (confirm("确认要保存吗?")) {
		if ($('#serviceAdd-form').validate().form()) {
			$('#serviceAdd-form').submit();
		}
	}
})