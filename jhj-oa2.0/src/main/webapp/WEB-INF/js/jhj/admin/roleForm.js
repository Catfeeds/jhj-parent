$('#role-form').validate({ 
			errorElement: 'span', //default input error message container
			errorClass: 'help-block', // default input error message class
			focusInvalid: false, // do not focus the last invalid input
			rules: {
				name: {
					required: true
				},
				authorityId: {
					required: true
				},
			},

			messages: {
				name: {
					required: "请输入角色名称"
				},
				authorityId: {
					required: "请选择绑定的权限"
				},
			},

			invalidHandler: function (event, validator) { //display error alert on form submit
				$('.alert-error', $('#role-form')).show();
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
				error.insertAfter(element.parents(".col-sm-5"));
			}

		});

		$('.role-form input').keypress(function (e) {
			if (e.which == 13) {
				$("#btn_submit").click();
				return false;
			}
		});

		$("#btn_submit").click(function(){
			if (confirm("确认要保存吗?")){
				if ($('#role-form').validate().form()) {
					$('#role-form').submit();
				}
		    }
		});