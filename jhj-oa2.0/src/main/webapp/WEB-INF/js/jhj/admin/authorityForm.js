$('#authority-form').validate({ 
			errorElement: 'span', //default input error message container
			errorClass: 'help-block', // default input error message class
			focusInvalid: false, // do not focus the last invalid input
			rules: {
				name: {
					required: true
				},
				position: {
					required: true
				},
				url: {
					required: true
				},
				matchUrl: {
					required: true
				},
				parentId :{
					required:true
				},
			},

			messages: {
				name: {
					required: "请输入权限名称"
				},
				position: {
					required: "请输入权限位置"
				},
				url: {
					required: "请输入权限url"
				},
				matchUrl: {
					required: "请输入权限matchUrl"
				},
				parentId:{
					required:"请选择权限位置"
				},
			},

			invalidHandler: function (event, validator) { //display error alert on form submit
				$('.alert-error', $('#authority-form')).show();
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

		$('.authority-form input').keypress(function (e) {
			if (e.which == 13) {
				$("#btn_submit").click();
				return false;
			}
		});

		$("#btn_submit").click(function(){
			if (confirm("确认要保存吗?")){
				if ($('#authority-form').validate().form()) {
					$('#authority-form').submit();
				}
		    }
		});