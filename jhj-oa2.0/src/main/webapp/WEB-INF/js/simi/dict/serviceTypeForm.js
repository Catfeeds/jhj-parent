$.validator.addMethod("uniqueName", function(value, element) {
  var response;
  $.ajax({
      type: "POST",
      url:"/simi-oa/interface-dict/check-name-dumplicate.json", //发送给服务器的url
      data: "name="+value + "&serviceType="+$('#id').val(),
      dataType:"json",
      async: false,
	  success: function(msg) {
		  response = msg.data;
	  }
  });

  //不存在，则返回true
  if(response == false) return true;

  //如果存在，则返回false;
  return false;

}, "");

var formVal = $('#dict-form').validate({
	errorElement : 'span', // default input error message container
	errorClass : 'help-block', // default input error message class
	focusInvalid : false, // do not focus the last invalid input
	rules : {
		name : {
			required : true,
			uniqueName : true
		},
		keyword : {
			required : true
		},
		tips:{
			required:true,
		},
		price : {
			required : true,

			number:true
		},
		disPrice : {

			required : true,
			range:[ 0.1, 1 ]
		},

	},

	messages : {
		name : {
			required : "请输入名称。",
			uniqueName : "名称已经存在"
		},
		keyword : {
			required : "请输入关键词。"
		},
		tips : {
			required : "请输入提示语。"
		},
		price:{
			required: "请输入价格(单位：元)",
			number: "请输入数字"
		},
		disPrice : {
			required : "请输入折扣",
			range : "请输入0.1-1之间的折扣"

		},

	},

	invalidHandler : function(event, validator) { // display error alert on
													// form submit
		$('.alert-error', $('#dict-form')).show();
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

$("#editServiceType_btn").click(function() {
	alert("1111111111");
	if (confirm("确认要保存吗?")) {
		if ($('#dict-form').validate().form()) {
			$('#dict-form').submit();
		}
	}
})

