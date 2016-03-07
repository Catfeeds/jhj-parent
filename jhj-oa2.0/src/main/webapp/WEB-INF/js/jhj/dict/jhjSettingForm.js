$.validator.addMethod("uniqueName", function(value, element) {
  var response;
  $.ajax({
      type: "POST",
      url:"/jhj-oa/interface-dict/check-cardName-dumplicate.json", //发送给服务器的url
      data: "name="+value + "&dictCardType="+$('#id').val(),
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
var formVal = $('#jhjSettingForm').validate({
	errorElement : 'span', // default input error message container
	errorClass : 'help-block', // default input error message class
	focusInvalid : false, // do not focus the last invalid input
	rules : {

		name : {
			required : true,
			uniqueName : true
		},
		settingValue : {
			required : true,
			number:true
		},
		

	},

	messages : {
		name : {
			required : "请输入名称。",
			uniqueName : "名称已经存在"
		},
		settingValue : {
			required: "请输入需要配置的值",
			number: "请输入数字"
		},
		
	},

//提示语颜色与样式
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

$("#jhjSettingForm_btn").click(function() {
	if (confirm("确认要保存吗?")) {
		if ($('#jhjSettingForm').validate().form()) {
			$('#jhjSettingForm').submit();
		}
	}
});


