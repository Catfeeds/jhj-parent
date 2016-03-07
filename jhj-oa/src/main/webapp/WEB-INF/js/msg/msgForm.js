$('#msg-form').validate({
	errorElement: 'span', //default input error message container
	errorClass: 'help-block', // default input error message class
	focusInvalid: false, // do not focus the last invalid input
	rules: {
		title : {
			required: true
		},
		summary: {
			required: true
		},
		gotoUrl: {
			required: true
		}
	},
	messages: {
		title : {
			required: "标题标题标题.重要的事情说三遍!"
		},
		summary: {
			required: "请输入摘要。"
		},
		gotoUrl: {
			required: "请输入跳转路径。"
		}
	},
	invalidHandler: function (event, validator) { //display error alert on form submit
		$('.alert-error', $('#msg-form')).show();
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

$('.msg-form input').keypress(function (e) {
	if (e.which == 13) {
		$("#editMsg_btn").click();
		return false;
	}
});
$('.form_datetime').datepicker({
	format: "yyyy-mm-dd",
	language: "zh-CN",
	autoclose: true,
	startView: 1,
	todayBtn:true
});

$("#editMsg_btn").click(function() {
	if (confirm("确认要保存吗?")) {
		/*if ($('#msg-form').validate().form()) {*/
		
			$('#msg-form').submit();
		/*}*/
	}
});


/*$('.input-group.date').datepicker({
	format : "yyyy-mm-dd",
	language : "zh-CN",
	autoclose : true,
	startView : 1,
	defaultViewDate : {
		year : 1980,
		month : 0,
		day : 1
	}
});*/
/*function checkEndTime(){  
    var startTime=$("#sendTime").val();  
    var start=new Date(startTime.replace("-", "/").replace("-", "/"));  
    var endTime=$("#endTimeStr").val();  
    var end=new Date(endTime.replace("-", "/").replace("-", "/"));  
    if(end<start){ 
    	
    	alert('结束日期必须大于开始时间');
//    	 BootstrapDialog.alert({
//    			 title:'提示语',
//    			 message:'结束日期必须大于开始时间!'
//    			
//    	 });
    	 return false;  
    }  
    return true;  
} */
