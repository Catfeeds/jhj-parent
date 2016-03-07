$('#org-form').validate({
	errorElement: 'span', //default input error message container
	errorClass: 'help-block', // default input error message class
	focusInvalid: false, // do not focus the last invalid input
	rules: {
		tagName: {
			required: true
		}

	},

	messages: {
		orgName: {
			required: "请输入标签名称"
		}
	
	},

	invalidHandler: function (event, validator) { //display error alert on form submit
		$('.alert-error', $('#tag-form')).show();
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

//校验标签名称是否重复
function valid(){
	var inputName = encodeURIComponent(document.getElementById("tagName").value);

	var option ={
		url:"/jhj-oa/bs/validTag",	
		type:"get",
		dataType:"text",
		data:{
			name:inputName
		},
		success:function(responseText){
			if(responseText == "no"){
				 $("#showResult").html("标签已存在");
                 $("#showResult").css("color","red");
			}else{
				$("#showResult").html("");
				return true;
			}
		},
		error:function(){
			return false;
		}
	} ;
	$.ajax(option);
}

$("#tagForm_btn").click(function(form) {
	if (confirm("确认要保存吗?")) {
		if ($('#tag-form').validate().form()) {
			form.submit();
		}
	}else{
		return false;
	}
});

 
 
