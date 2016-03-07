$.validator.addMethod("uniqueToken", function(value, element) {
  var response;
  $.ajax({
      type: "POST",
      url:"/simi-oa/interface-promotion/check-token-dumplicate.json", //发送给服务器的url
      data: "token="+value + "&channel=",
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
var formVal = $('#channel-form').validate({
	errorElement : 'span', // default input error message container
	errorClass : 'help-block', // default input error message class
	focusInvalid : false, // do not focus the last invalid input
	rules : {

		name : {
			required : true

		},
		token: {
			required : true,
			uniqueToken : true
		},
		downloadUrl:{
			required:true
		},

	},

	messages : {
		name : {
			required : "请输入名称。"

		},
		token : {
			required : "请输入标识。",
		    uniqueToken : "标识已经存在"
		},

		downloadUrl : {
			required : "请输入下载地址。"
		},

	},

	invalidHandler : function(event, validator) { // display error alert on
													// form submit
		$('.alert-error', $('#channel-form')).show();
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

$('.channel-form input').keypress(function(e) {
	if (e.which == 13) {
		$("#channelForm_btn").click();
		return false;
	}
});

$("#channelForm_btn").click(function() {
	if (confirm("确认要保存吗?")) {
		if ($('#channel-form').validate().form()) {
			$('#channel-form').submit();
		}
	}
});


$("#channelTypeGroup").change(function() {

	var channelType = $('input[name="channelType"]:checked').val();
	console.log(channelType);
	if (channelType == "1") {

		var tokenValue = $("#token").val();
		var url = "http://www.yougeguanjia.com/simi-oa/d/" + tokenValue;
		$("#downloadUrl").val(url);

		$("#downloadUrl").attr("readOnly","readOnly");
		$("#downloadUrl").rules("remove", "required");
	} else {

		$("#downloadUrl").removeAttr("readOnly");
		var downloadUrlDefault = $("#downloadUrlDefault").val();
		$("#downloadUrl").val(downloadUrlDefault);

		$("#downloadUrl").rules("add", "required");
	}

});

