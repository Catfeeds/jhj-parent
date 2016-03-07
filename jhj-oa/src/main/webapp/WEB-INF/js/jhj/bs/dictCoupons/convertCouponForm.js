$('#convert-coupon-form').validate({ 
			errorElement: 'span', //default input error message container
			errorClass: 'help-block', // default input error message class
			focusInvalid: false, // do not focus the last invalid input
			rules: {
				count: {
					required: true,
					digits:true,
					max:1000,
					min:0
				},
				value: {
					required: true,
					number:true,
					maxlength:9
				},
				maxValue: {
					required: true,
					number:true,
					maxlength:5 
				},
				introduction: {
					required: true
				},
				description: {
					required: true
				},
				serviceType:{
					required:true
				}
			},

			messages: {
				count: {
					required: "请输入卡数量",
					digits:"请输入整数",
					min:"最小输入0",
					max:"最大输入1000"
				},
				value: {
					required: "请输入优惠券金额",
					number:"请输入数字",
					maxlength:"最多输入9位数字"
				},
				maxValue: {
					required: "请输入订单满金额",
					number:"请输入数字",
					maxlength:"最多输入5位数字"
				},
				introduction: {
					required: "请输入描述信息"
				},
				description: {
					required: "请输入详细信息"
				},
				serviceType:{
					required:"请选择服务类型"
				}
			},

			invalidHandler: function (event, validator) { //display error alert on form submit
				$('.alert-error', $('#convert-coupon-form')).show();
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

		$('.convert-coupon-form input').keypress(function (e) {
			if (e.which == 13) {
				$("#addCoupon_btn").click();
				return false;
			}
		});

		$("#addCoupon_btn").click(function(){
			if (confirm("确认要保存吗?")){
				if ($('#convert-coupon-form').validate().form()) {
					var startTime=$("#fromDate").val();  
				    var start=new Date(startTime.replace("-", "/").replace("-", "/"));  
				    var endTime=$("#toDate").val();  
				    var end=new Date(endTime.replace("-", "/").replace("-", "/"));  
					    if(end<start){ 
					    	 BootstrapDialog.alert({
					    			 title:'提示语',
					    			 message:'结束日期必须大于开始时间!'
					    	 });
					        return false;  
					    }else{
					    	$('#convert-coupon-form').submit();
					    }
					
				}
		    }
		});

$('.input-group.date').datepicker({
	format: "yyyy-mm-dd",
	language: "zh-CN",
	autoclose: true,
	startView: 1,
	defaultViewDate : {
		year:1980,
		month:0,
		day:1
	}
});
/*
function checkEndTime(){  
    var startTime=$("#fromDate").val();  
    var start=new Date(startTime.replace("-", "/").replace("-", "/"));  
    var endTime=$("#toDate").val();  
    var end=new Date(endTime.replace("-", "/").replace("-", "/"));  
    if(end<start){ 
    	 BootstrapDialog.alert({
    			 title:'提示语',
    			 message:'结束日期必须大于开始时间!'
    			
    	 });
        return false;  
    }  
    return true;  
} 
$("#fromDate").bind('change',function(){
	console.log('dd');
	checkEndTime();
});
$("#toDate").bind("change",function(){
	checkEndTime();
});*/