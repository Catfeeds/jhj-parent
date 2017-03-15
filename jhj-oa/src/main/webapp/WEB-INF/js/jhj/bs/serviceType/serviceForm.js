$('#service-form').validate({
	errorElement : 'span', // default input error message container
	errorClass : 'help-block', // default input error message class
	focusInvalid : false, // do not focus the last invalid input
	rules : {
		name : {
			required : true
		},
		unit : {
			required : true
		},

		price : {
			required : true,
			number : true
		},
		
		mprice : {
			required : true,
			number : true
		},
		
		pprice : {
			required : true,
			number : true
		},
		
		mpprice : {
			required : true,
			number : true
		},
		
		serviceHour:{
			required : true,
			number : true
		},
		serviceTimes : {
			required : true,
			number : true
		},
		defaultHour : {
			required : true,
			number : true
		}
	
	},
	
	messages : {
		name : {
			required : "请输入权限名称"
		},
		unit : {
			required : "请输入计量单位"
		},

		price : {
			required : "请输入非会员价格",
			number : "请输入合法的单价数字"
		},
		pprice : {
			required : "请输入非会员套餐价格",
			number : "请输入合法的单价数字"
		},
		
		mprice : {
			required : "请输入会员价格",
			number : "请输入合法的单价数字"
		},
		
		mpprice : {
			required : "请输入会员套餐价格",
			number : "请输入合法的单价数字"
		},
		
		serviceHour:{
			required:"请输入服务时长",
			number:"请输入合法的时间"
		},
		serviceTimes : {
			required : "请输入每周服务次数",
			number : "请输入合法的周服务次数"
		},
		defaultHour : {
			required : "请输入默认服务时长",
			number : "服务时长必须为数字"
		}
	
	},
	
	invalidHandler : function(event, validator) { // display error alert on
													// form submit
		$('.alert-error', $('#service-form')).show();
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

$('.service-form input').keypress(function(e) {
	if (e.which == 13) {
		$("#btn_submit").click();
		return false;
	}
});

$("#btn_submit").click(function() {
	if (confirm("确认要保存吗?")) {
		
		
		// 检测服务子项输入是否完整.
		var v = validateServiceAddons();
		if (v == false) {
			alert("服务子项填写不完整.");
			return false;
		}
		
		if ($('#service-form').validate().form()) {
			$('#service-form').submit();
		}
	}
});

function validateServiceAddons() {
	
	var v = true;
	console.log("first v = " + v);
	$("#serviceAddonTable").find("tr").each(function() {
		var serviceAddonName = "";
		var itemUnit = "";
		var serviceAddonPrice = "";
		var serviceAddonStaffPrice = "";
		var serviceAddonDisPrice = "";
		var serviceAddonStaffDisPrice = ""
		var defaultNum = "";
		var serviceAddonServiceHour = "";
			
		$(this).find("td input").each(function(){
			console.log("attr = " + $(this).attr("name"));
			if($(this).attr("name") == "serviceAddonName") serviceAddonName = $(this).val();
			if($(this).attr("name") == "itemUnit") itemUnit = $(this).val();
			if($(this).attr("name") == "serviceAddonPrice") serviceAddonPrice = $(this).val();
			if($(this).attr("name") == "serviceAddonStaffPrice") serviceAddonStaffPrice = $(this).val();
			if($(this).attr("name") == "serviceAddonDisPrice") serviceAddonDisPrice = $(this).val();
			if($(this).attr("name") == "serviceAddonStaffDisPrice") serviceAddonStaffDisPrice = $(this).val();
			if($(this).attr("name") == "defaultNum") defaultNum = $(this).val();
			if($(this).attr("name") == "serviceAddonServiceHour") serviceAddonServiceHour = $(this).val();
		});
		
		console.log("name = " + serviceAddonName);
		console.log("itemUnit = " + itemUnit);
		console.log("price = " + serviceAddonPrice);
		console.log("disPrice = " + serviceAddonDisPrice);
		//如果都为空则不校验
		if (serviceAddonName == "" && 
			itemUnit == "" && 
			(serviceAddonPrice == "" || serviceAddonPrice == "0")  && 
			(serviceAddonStaffPrice == "" || serviceAddonStaffPrice == "0")  && 
			(serviceAddonDisPrice == "" || serviceAddonDisPrice == "0") &&
			(serviceAddonStaffDisPrice == "" || serviceAddonStaffDisPrice == "0") &&
			(defaultNum == "" || defaultNum == "0") &&
			(serviceAddonServiceHour == "" || serviceAddonServiceHour == "0.0")
			) {
			return;
		}
		
		//如果都不为空，则需要校验
		if (serviceAddonName == "" || 
			itemUnit == "" || 
			serviceAddonPrice == "" || 
			serviceAddonStaffPrice == "" || 
			serviceAddonDisPrice == "" ||
			serviceAddonStaffDisPrice == "" ||
			defaultNum == "" ||
			serviceAddonServiceHour == "") {
			v = false;
			return false;
		}
	});  
	console.log("end v = " + v);
	return v;
}

// $("#headImg").fileinput({
// previewFileType: "image",
// browseClass: "btn btn-success",
// browseLabel: "上传图片",
// browseIcon: '<i class="glyphicon glyphicon-picture"></i>',
// removeClass: "btn btn-danger",
// removeLabel: "删除",
// removeIcon: '<i class="glyphicon glyphicon-trash"></i>',
// allowedFileExtensions: ["jpg", "gif", "jpeg","png",],
// maxFileSize: 8192,
// msgSizeTooLarge: "上传文件大小超过8mb"
// });

$("input[name='isMulti']").on("change", function() {
	console.log("isMulti change");
	var isMulti = $("input[name='isMulti']:checked").val();
	var isAuto = $("input[name='isAuto']:checked").val();
	
	if (isMulti == 1 && isAuto == 1) {
		alert("一单多人不能自动派工.");
		$("input[name='isMulti'][value='0']").prop('checked', true);
	}
});

$("input[name='serviceProperty']").on("change", function() {
	
	if ($(this).val() == 0) {
		$("#weekTimes").hide();
		$("#timeDetail").hide();
		
		$("#serviceTimes").val(0);
	}
	
	if ($(this).val() == 1) {
		$("#weekTimes").show();
		
	}
	
});

$("#serviceTimes").on("change", function() {
	
	$("#timeDetail").show();
	
	var time = $(this).val();
	
	var price = $("#price").val();
	
	$("#yearTimes").text("全年次数:" + Number(time) * 52);
	
	$("#sumPrice").text("全年总价:" + price);
	
	$("#monthPrice").text("月付价格(95折):" + (Number(price) / 12 * 0.95).toFixed(2));
	
	$("#yearPrice").text("年付价格(85折):" + (Number(price) * 0.85).toFixed(2));
	
});

$(document).on('click','.btn-add', function(e) {
			e.preventDefault();
			
			var controlForm = $('#serviceAddonTable');
			
			var rowCount = $('#serviceAddonTable tr').length;
			
			var currentEntry = controlForm.find('tr').eq(1);
			var newEntry = $(currentEntry.clone()).appendTo(controlForm);
			
			newEntry.find('input').val('');
			
			controlForm.find('tr:not(:last-child) .btn-add').removeClass('btn-add').addClass(
					'btn-remove').removeClass('btn-success').addClass('btn-danger').html(
					'<span class="glyphicon glyphicon-minus"></span>');
			
			controlForm.find('tr:last-child .btn-remove').removeClass('btn-remove').addClass(
					'btn-add').removeClass('btn-danger').addClass('btn-success').html(
					'<span class="glyphicon glyphicon-plus"></span>');
		}).on('click', '.btn-remove', function(e) {
	e.preventDefault();
	$(this).parents("tr").first().remove();
	return false;
});

/*
 * 页面加载时。动态展示
 */

var loadTime = function() {
	
	if ($("input[name='serviceProperty']:checked").val() == 0) {
		$("#weekTimes").hide();
		$("#timeDetail").hide();
	} else {
		
		$("#timeDetail").show();
		
		var time = $("#serviceTimes").val();
		
		var price = $("#price").val();
		
		$("#yearTimes").text("全年次数:" + Number(time) * 52);
		
		$("#sumPrice").text("全年总价:" + price);
		
		$("#monthPrice").text("月付价格(95折):" + (Number(price) / 12 * 0.95).toFixed(2));
		
		$("#yearPrice").text("年付价格(85折):" + (Number(price) * 0.85).toFixed(2));
		
	}
}

window.onload = loadTime;