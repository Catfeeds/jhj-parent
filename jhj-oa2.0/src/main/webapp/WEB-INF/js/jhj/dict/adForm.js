$('#dict-form').validate({
	errorElement: 'span', //default input error message container
	errorClass: 'help-block', // default input error message class
	focusInvalid: false, // do not focus the last invalid input
	rules: {
		No: {
			required: true,
     		digits:true,
			range:[0,255]

		},
		img_url_new: {
			required: true
		},
		gotoUrl: {
			required: true
		},
		serviceType: {
			required: true
		}



	},

	messages: {
		No: {
			required: "请输入编号。",
			digits:"请输入整数",
			range:"输入值必须介于 0 和 255之间"

		},
		img_url_new: {
			required: "请选择上传路径。"
		
		},
		gotoUrl: {
			required: "请输入跳转路径。"
		},
		serviceType: {
			required: "请选择服务类型。"
		}

	},

	invalidHandler: function (event, validator) { //display error alert on form submit
		$('.alert-error', $('#dict-form')).show();
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

$('.dict-form input').keypress(function (e) {
	if (e.which == 13) {
		$("#addstaff_btn").click();
		return false;
	}
});

$("#imgUrl").fileinput({
	previewFileType: "image",
	browseClass: "btn btn-success",
	browseLabel: "上传图片",
	browseIcon: '<i class="glyphicon glyphicon-picture"></i>',
	removeClass: "btn btn-danger",
	removeLabel: "删除",
	removeIcon: '<i class="glyphicon glyphicon-trash"></i>',
	allowedFileExtensions: ["jpg", "gif", "jpeg","png",],
	maxFileSize: 8192,
	msgSizeTooLarge: "上传文件大小超过8mb"
});

$('#imgUrl').change(function(){
	console.log($("#imgUrl").val()+"0000000000");
	 $("#img_url_new").text($("#imgUrl").val());
});
/*$("#adForm_btn").click(function() {
	
	console.log($("#imgUrl").val()+"0000000000");
	$("#img_url_new").text($("#imgUrl").val());
})*/

/*$('#adForm_btn').change(function(){
	console.log($("#imgUrl").val()+"0000000000");
	// $("#img_url_new").text($("#imgUrl").val());
});*/