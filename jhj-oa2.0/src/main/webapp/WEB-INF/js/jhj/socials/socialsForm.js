$('#socials-form').validate({
	errorElement : 'span', // default input error message container
	errorClass : 'help-block', // default input error message class
	focusInvalid : false, // do not focus the last invalid input
	rules : {
		title : {
			required : true
		},
		titleImgs : {
			required : true
		}
	},

	messages : {
		title : {
			required : "请输入活动标题"
		},
		titleImgs : {
			required : "请选择上传的题图"
		}
	},

	invalidHandler : function(event, validator) { // display error alert on
													// form submit
		$('.alert-error', $('#socials-form')).show();
	},

	highlight : function(element) { // hightlight error inputs
		$(element).closest('.form-group').addClass('has-error'); // set error
	},

	success : function(label) {
		label.closest('.form-group').removeClass('has-error');
		label.remove();
	},

	errorPlacement : function(error, element) {
		error.insertAfter(element.parents(".col-md-5"));
	}

});

$('.socials-form input').keypress(function(e) {
	if (e.which == 13) {
		$("#addSocials_btn").click();
		return false;
	}
});

$("#addSocials_btn").click(function() {
	if (confirm("确认要保存吗?")) {
		if ( $('#socials-form').validate().form()) {
				$('#socials-form').submit();
		}
	}
});
$('.input-group.date').datepicker({
	format : "yyyy-mm-dd",
	language : "zh-CN",
	autoclose : true,
	startView : 1,
	defaultViewDate : {
		year : 1980,
		month : 0,
		day : 1
	}
});
/*function validateLength(arg){
	  var intro = arg.innerHTML.replace(/^\s+|\s+$/g,"");
	  if(intro.length>2){
	   arg.innerHTML=intro.substr(0,2);
	  }
	 }*/
$("#headImg").fileinput({
	previewFileType: "image",
	browseClass: "btn btn-success",
	browseLabel: "上传图片",
	browseIcon: '<i class="glyphicon glyphicon-picture"></i>',
	removeClass: "btn btn-danger",
	removeLabel: "删除",
	removeIcon: '<i class="glyphicon glyphicon-trash"></i>',
	allowedFileExtensions: ["jpg", "gif", "jpeg","png",],
	maxFileSize: 8388608,//单位字节B
	msgSizeTooLarge: "上传文件大小超过8mb"
});
