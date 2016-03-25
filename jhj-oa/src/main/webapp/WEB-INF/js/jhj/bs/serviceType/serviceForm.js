$('#service-form').validate({ 
			errorElement: 'span', //default input error message container
			errorClass: 'help-block', // default input error message class
			focusInvalid: false, // do not focus the last invalid input
			rules: {
				name: {
					required: true
				},
				unit: {
					required: true
				},
				defaultNum: {
					required: true,
					number:true
				},
				price: {
					required: true,
					number:true
				},
			},

			messages: {
				name: {
					required: "请输入权限名称"
				},
				unit: {
					required: "请输入计量单位"
				},
				defaultNum: {
					required: "请输入默认数量",
					number:"请输入合法的数量数字"
				},
				price: {
					required: "请输入单价",
					number:"请输入合法的单价数字"
				},
			},

			invalidHandler: function (event, validator) { //display error alert on form submit
				$('.alert-error', $('#service-form')).show();
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

		$('.service-form input').keypress(function (e) {
			if (e.which == 13) {
				$("#btn_submit").click();
				return false;
			}
		});

		$("#btn_submit").click(function(){
			if (confirm("确认要保存吗?")){
				if ($('#service-form').validate().form()) {
					$('#service-form').submit();
				}
		    }
});
		

//$("#headImg").fileinput({
//	previewFileType: "image",
//	browseClass: "btn btn-success",
//	browseLabel: "上传图片",
//	browseIcon: '<i class="glyphicon glyphicon-picture"></i>',
//	removeClass: "btn btn-danger",
//	removeLabel: "删除",
//	removeIcon: '<i class="glyphicon glyphicon-trash"></i>',
//	allowedFileExtensions: ["jpg", "gif", "jpeg","png",],
//	maxFileSize: 8192,
//	msgSizeTooLarge: "上传文件大小超过8mb"
//});		
		