//表单验证规则===================================================
var formVal = $('#form').validate({
	errorElement : 'span', // default input error message container
	errorClass : 'help-block', // default input error message class
	focusInvalid : false, // do not focus the last invalid input
	rules : {
		name : {
			required : true,
		},
		serviceTypeId : {
			required : true
		},
		serviceAddonId : {
			required : true,
		},
		price : {
			required : true,
			digits : true,
		},
		vipPrice : {
			required : true,
			digits : true,
		},
		num : {
			required : true,
			digits : true,
		},
		punit : {
			required : true,
		},
		total : {
			required : true,
			digits : true,
		},
		enbale : {
			required : true,
		}
	},
	messages : {
		name : {
			required : "名称不能为空",
		},
		serviceTypeId : {
			required : "服务类型不能为空"
		},
		serviceAddonId : {
			required : "服务子类型不能为空",
		},
		price : {
			required : "原价不能为空",
			digits : "总次数必须为数字",
		},
		vipPrice : {
			required : "会员价不能为空",
			digits : "总次数必须为数字",
		},
		num : {
			required : "次数不能为空",
			digits : "总次数必须为数字",
		},
		punit : {
			required : "频率不能为空",
		},
		total : {
			required : "总次数不能为空",
			digits : "总次数必须为数字",
		},
		enbale : {
			required : "请选择支付方式",
		},
	
	},
	
	highlight : function(element) {
		$(element).closest('.form-group').addClass('has-error'); 
	},
	success : function(label) {
		label.closest('.form-group').removeClass('has-error');
		label.remove();
	},
	errorPlacement : function(error, element) {
		error.insertAfter(element.parents(".col-md-5"));
	}
});

$(function(){
	$("#btn-save").on('click',function(){
		if($('#form').validate().form()){
			$("form").action="save";
			$("form").submit();
		}
	});
});