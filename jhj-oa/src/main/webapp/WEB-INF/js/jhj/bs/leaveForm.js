$('#leaveForm').validate({
	errorElement: 'span', //default input error message container
	errorClass: 'help-block', // default input error message class
	focusInvalid: false, // do not focus the last invalid input
	rules: {
		parentId:{
			required: true,
			parentId:"parentId"
		},
		orgId: {
			required: true,
			orgId:"orgId"
		},
		staffId:{
			required: true,
			staffId:"staffId"
		},
		leaveDate:{
			required: true,
		},
		leaveDateEnd:{
			required: true,
		}
	},

	messages: {
		parentId:{
			required: "请选择门店",
		},
		leaveDate: {
			required: "请输入假期开始时间",
		},
		leaveDateEnd: {
			required: "请输入假期结束时间",
		}
	},

	invalidHandler: function (event, validator) { //display error alert on form submit
		$('.alert-error', $('#leaveForm')).show();
	},

	highlight: function (element) { // hightlight error inputs
		$(element)
			.closest('.form-group').addClass('has-error'); // set error class to the control group
	},

	success: function (label) {
		label.closest('.form-group').removeClass('has-error');
		label.remove();
	},

	errorPlacement: function (error, element) {
		error.insertAfter(element.parents(".col-md-5"));
	}

});

$.validator.addMethod("orgId",function(value,elements){
	
	// value 值是  option 选项 在 所有 option 中的 下标，从 0开始
	if(value != 0){
		return true;
	}
},"请选择云店");


$.validator.addMethod("staffId",function(value,elements){
	
	if(value != 0){
		return true;
	}
	
},"请选择服务人员");



$('.form-control.form_datetime').datepicker({
	format : "yyyy-mm-dd",
	language : "zh-CN",
	autoclose : true,
	startView : 0,
	todayBtn:true,
	startDate:new Date()
});

