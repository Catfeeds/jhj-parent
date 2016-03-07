$(function() {
			addCouponFormValidate.handleAddCoupon();
		});
var addCouponFormValidate = function () {

	var handleAddCoupon = function() {
		$('#coupon-form').validate({
			errorElement: 'span', //default input error message container
			errorClass: 'help-block', // default input error message class
			focusInvalid: false, // do not focus the last invalid input
			rules: {
				id: {
					required: true
				},
				value: {
					required: true
				},
				introduction: {
					required: true
				},
				description: {
					required: true
				},
				expTime :{
					required:true
				},
				serviceType:{
					required:true
				}
			},

			messages: {
				id: {
					required: "请输入优惠券数量"
				},
				value: {
					required: "请输入优惠券金额"
				},
				introduction: {
					required: "请输入描述信息"
				},
				description: {
					required: "请输入详细信息"
				},
				expTime:{
					required:"请选择过期日期"
				},
				serviceType:{
					required:"请选择服务类型"
				}
			},

			invalidHandler: function (event, validator) { //display error alert on form submit
				$('.alert-error', $('#coupon-form')).show();
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

		$('.coupon-form input').keypress(function (e) {
			if (e.which == 13) {
				$("#addCoupon_btn").click();
				return false;
			}
		});

		$("#addCoupon_btn").click(function(){
			if (confirm("确认要新增吗?")){
				if ($('#coupon-form').validate().form()) {
					$('#coupon-form').submit();
				}
		    }
		});
	}

    return {
        //main function to initiate the module
    	handleAddCoupon: function () {
    		handleAddCoupon();
        }
    };

}();
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


