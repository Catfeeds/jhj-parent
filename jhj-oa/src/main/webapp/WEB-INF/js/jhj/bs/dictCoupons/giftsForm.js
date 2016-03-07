var formval = $('#gifts-form').validate({
	errorElement : 'span', // default input error message container
	errorClass : 'help-block', // default input error message class
	focusInvalid : false, // do not focus the last invalid input
	rules : {
		name : {
			required : true
		},
		rangeMonth : {
			required : true
		},
	},

	messages : {

		name : {
			required : "请输入礼包名称"
		},
		rangeMonth : {
			required : "请选择有效期限"
		},
	},

	invalidHandler : function(event, validator) { // display error alert on
													// form submit
		$('.alert-error', $('#gifts-form')).show();
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

$('.gifts-form input').keypress(function(e) {
	if (e.which == 13) {
		$("#addCoupon_btn").click();
		return false;
	}
});

$("#addCoupon_btn").click(function() {
	if (confirm("确认要保存吗?")) {
		if ($('#gifts-form').validate().form()) {

			if(!checkGiftNameExist()) return false;
			
			if(!validationItemNum()) return false;

			$('#gifts-form').submit();
			
		}
	}
});
//礼包名称排重 
function checkGiftNameExist() {
	var giftName = $.trim($("#giftName").val());
	var giftId = $.trim($("#giftId").val());
	var flag = true;
    $.ajax({
    	type:"POST",   //http请求方式
    	url:"/jhj-oa/interface-dict/check-giftName-dumplicate.json", //发送给服务器的url
    	data:"giftName="+giftName+"&giftId="+giftId, //发送给服务器的参数
    	dataType:"json",
    	complete : function(msg) {
    		var result = eval("(" + msg.responseText + ")");
    		$("#giftNameResult").html(result.msg);
    		if(result.data){
    			flag = false;
    		}
    	}
    });
    return flag;
}
function clearGiftNameCss() {
	$("#giftNameResult").html("");
}
// 联系人添加多个脚本
$(document).on(
		'click',
		'.btn-add',
		function(e) {
			e.preventDefault();

			var controlForm = $('#giftCouponsTable');

			var currentEntry = controlForm.find('tr').eq(1);
			var newEntry = $(currentEntry.clone()).appendTo(controlForm);

			newEntry.find('input').val('');

			controlForm.find('tr:not(:last-child) .btn-add').removeClass(
					'btn-add').addClass('btn-remove')
					.removeClass('btn-success').addClass('btn-danger').html(
							'<span class="glyphicon glyphicon-minus"></span>');

			controlForm.find('tr:last-child .btn-remove').removeClass(
					'btn-remove').addClass('btn-add').removeClass('btn-danger')
					.addClass('btn-success').html(
							'<span class="glyphicon glyphicon-plus"></span>');
		}).on('click', '.btn-remove', function(e) {

	e.preventDefault();
	$(this).parents("tr").first().remove();
	return false;
});


//校验数量
function validationItemNum() {

	var errors = {};
			
	$("input[name*=num]").each(function(pIndex, obj){

		if ($(this).val() == null ||  $(this).val() == "") {
			errors.num = "请输入优惠劵数量";
			return false;
		}
	});
	
	if (errors.num != null) {
		formval.showErrors(errors);
		return false;
	}
		
	return true;
}