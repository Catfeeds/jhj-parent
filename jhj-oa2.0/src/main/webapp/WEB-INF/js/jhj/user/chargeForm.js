$('#charge-form').validate({ 
			errorElement: 'span', //default input error message container
			errorClass: 'help-block', // default input error message class
			focusInvalid: false, // do not focus the last invalid input
			rules: {
				chargeMoney: {
					required: true,
					number:true,
					maxlength:9,
					min:0
				},
			},

			messages: {
				chargeMoney	: {
					required: "请输入充值金额",
					number:"请输入数字",
					maxlength:"最多输入9位数字",
					min:"最小金额是0"
				},
			},

			invalidHandler: function (event, validator) { //display error alert on form submit
				$('.alert-error', $('#charge-form')).show();
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

		$('.charge-form input').keypress(function (e) {
			if (e.which == 13) {
				$("#addCoupon_btn").click();
				return false;
			}
		});
		$("#addCoupon_btn").click(function(){
			if (confirm("确认要进行充值吗?")){
				var chargeWay =	$(".select1").find('option:selected').val();
				if(chargeWay==1){
					if ($('#charge-form').validate().form()) {
						$('#charge-form').submit();
					}
				}else{
					$('#charge-form').submit();
				}
		    }
		});
$(function(){
	var chargeWay =	$(".select1").find('option:selected').val();
	if(chargeWay==1){//1=任意金额充值
		$("#any").show();
    	$("#fixed").hide();
	}else{
		$("#fixed").show();
		$("#any").hide();
	}
});
$(".select1 option").click(function(){
    var id = $(this).attr("value");
    if(id==0){
    	$("#fixed").show();
    	$("#any").hide();
    }else{
    	$("#any").show();
    	$("#fixed").hide();
 }});
	