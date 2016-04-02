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
				serviceTimes:{
					required: true,
					number:true
				}
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
				serviceTimes:{
					required: "请输入每周服务次数",
					number:"请输入合法的周服务次数"
				}
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
		
$("input[name='serviceProperty']").on("change",function(){
	
	if($(this).val() == 0){
		$("#weekTimes").hide();
		$("#timeDetail").hide();
		
		$("#serviceTimes").val(0);
	}
	
	if($(this).val() == 1){
		$("#weekTimes").show();
		
	}
	
});		
		
$("#serviceTimes").on("change",function(){
	
	$("#timeDetail").show();
	
	var time =  $(this).val();
	
	var price = $("#price").val();
	
	$("#yearTimes").text("全年次数:"+Number(time)*52);
	
	$("#sumPrice").text("全年总价:"+price);
	
	$("#monthPrice").text("月付价格(95折):"+(Number(price)/12*0.95).toFixed(2));
	
	$("#yearPrice").text("年付价格(85折):"+(Number(price)*0.85).toFixed(2));
	
});

/*
 *  页面加载时。动态展示 
 */

var loadTime = function(){
	
	if($("input[name='serviceProperty']:checked").val() == 0){
		$("#weekTimes").hide();
		$("#timeDetail").hide();
	}else{
		
		$("#timeDetail").show();
		
		var time =  $("#serviceTimes").val();
		
		var price = $("#price").val();
		
		$("#yearTimes").text("全年次数:"+Number(time)*52);
		
		$("#sumPrice").text("全年总价:"+price);
		
		$("#monthPrice").text("月付价格(95折):"+(Number(price)/12*0.95).toFixed(2));
		
		$("#yearPrice").text("年付价格(85折):"+(Number(price)*0.85).toFixed(2));
		
	}
}

window.onload = loadTime;