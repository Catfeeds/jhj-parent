$('#recharge-coupon-form').validate({ 
			errorElement: 'span', //default input error message container
			errorClass: 'help-block', // default input error message class
			focusInvalid: false, // do not focus the last invalid input
			rules: {
				 value: {
					required: true,
					number:true,
					maxlength:9
				},
				maxValue: {
					required: true,
					number:true,
					maxlength:6 
				},
				introduction: {
					required: true
				},
				description: {
					required: true
				},
				rangMonth :{
					required:true
				},
				serviceType:{
					required:true
				}
			},

			messages: {
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
				rangMonth:{
					required:"请选择日期范围"
				},
				serviceType:{
					required:"请选择服务类型"
				}
			},

			invalidHandler: function (event, validator) { //display error alert on form submit
				$('.alert-error', $('#recharge-coupon-form')).show();
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

		$('.recharge-coupon-form input').keypress(function (e) {
			if (e.which == 13) {
				$("#addCoupon_btn").click();
				return false;
			}
		});

		$("#addCoupon_btn").click(function(){
			if (confirm("确认要保存吗?")){
				if ($('#recharge-coupon-form').validate().form()) {
					$('#recharge-coupon-form').submit();
				}
		    }
		});