var formVal = $('#msg-form').validate({
	errorElement : 'span', // default input error message container
	errorClass : 'help-block', // default input error message class
	focusInvalid : false, // do not focus the last invalid input
	rules : {
		title : {
			required : true
		},
		summary : {
			required : true
		},
		content:{
			required: true
		},
	},

	messages : {
		title : {
			required : "请输入消息标题。"
		},
		summary : {
			required : "请输入消息摘要。"
		},
		content : {
			required : "请输入消息内容。"
		},
	},

	invalidHandler : function(event, validator) { // display error alert on
													// form submit
		$('.alert-error', $('#msg-form')).show();
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

$('.msg-form input').keypress(function(e) {
	if (e.which == 13) {
		$("#addMsg_btn").click();
		return false;
	}
});

$("#editMsg_btn").click(function() {
	if (confirm("确认要保存吗?")) {
		if ($('#msg-form').validate().form()) {
			$('#msg-form').submit();
		}
	}
});

$("#previewMsg_btn").click(function() {
	
	var title = $("#title").val();
	var content = $("#some-textarea").val();
	var summary = $("#summary").val();
	$.ajax({
        type:"post",
        url:"/"+appName+"/interface-promotion/preview-msg.json",
        dataType:"json",
        cache:false,
        data:"title="+title+"&summary="+summary+"&content="+content,
        success :function(data){
        	var data = data.data;
        	window.open(appRootUrl+data);
        },
	    error : function(){
	    }
    });
});
