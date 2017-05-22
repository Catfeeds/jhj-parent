//表单验证规则===================================================
var formVal = $('#orderForm').validate({
	errorElement : 'span', // default input error message container
	errorClass : 'help-block', // default input error message class
	focusInvalid : false, // do not focus the last invalid input
	rules : {
		mobile : {
			required : true,
			minlength : 11,
			maxlength : 11,
			isMobile : true
		},
		
		addrId : {
			required : true
		},
		
		serviceType : {
			required : true,
		},
		
		orderPay : {
			required : true,
			isFloat : true,
		},
		
		orderOpFrom : {
			required : true,
		},
		
		serviceDate : {
			required : true,
		},
		
		serviceHour : {
			required : true,
			digits : true,
			min : 1,
		
		},
		
		staffNums : {
			required : true,
			digits : true,
			min : 1,
		
		},
		
		orderPayType : {
			required : true,
		},
	
	},
	
	messages : {
		
		mobile : {
			required : "输入用户手机号",
			minlength : "手机号不正确",
			maxlength : "手机号不正确",
			isMobile : "手机号不正确"
		},
		
		addrId : {
			required : "请选择用户地址"
		},
		
		serviceType : {
			required : "请选择服务类别",
		},
		
		// ItemNum : {
		// required : "数量不能为空",
		// digits : "请输入有效的数字"
		// },
		
		orderPay : {
			required : "价格不能为空",
			isFloat : "价格必须为数字",
		},
		
		orderOpFrom : {
			required : "请选择订单来源",
		},
		
		serviceDate : {
			required : "请选择服务时间",
		},
		
		serviceHour : {
			required : "服务时长不能为空",
			digits : "服务时长必须为数字",
			min : "服务时长必须超过1小时",
		
		},
		
		staffNums : {
			required : "服务人员数量不能为空",
			digits : "服务人员数量必须为数字",
			min : "服务人员数量必须大于等于1个人",
		
		},
		
		orderPayType : {
			required : "请选择支付方式",
		},
	
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

/*
 * 提交订单
 */
function saveForm() {
	// $("#modalDispatch").modal("show");
	// return false;
	if ($('#orderForm').validate().form()) {
		
		var parentServiceType = $("#serviceTypeId").val();
		
		if (parentServiceType != 23 && parentServiceType != 24) {
			var serviceAddonDatas = $("#serviceAddonDatas").val();
			// console.log(serviceAddonDatas);
			// console.log(serviceAddonDatas.length);
			if (serviceAddonDatas == undefined || serviceAddonDatas == ""
					|| serviceAddonDatas == []) {
				alert("请输入服务子项的数量");
				return false;
			}
		}
		
		$('#selectedStaffs').tagsinput('removeAll');
		$('#selectedStaffs').val('');
		var serviceDate = $("#serviceDate").val()
		$("#divSerivceDate").val(serviceDate);
		
		$("#modalDispatch").modal("show");
		var serviceDate = $("#serviceDate").val();
		var serviceDateUnix = moment(serviceDate).unix();
		loadStaffsByServiceDate(serviceDateUnix);
		$("input[name='disWay']").eq(0).attr("checked", true);
		$("input[name='disWay']").trigger("change");
		
	}
}

$("#orderSubmit").on("click", function() {
	
	$("#modalDispatch").modal("hide");
	
	var selectStaffIds = $("#selectedStaffs").val();
	if (selectStaffIds == undefined || selectStaffIds == "") {
		
		if (confirm("未选择派工人员，提交订单会如果是自动派工服务品类则会自动派工?")) {
			$("#selectStaffIds").val("");
			orderFormSubmit();
		}
	} else {
		
		$("#selectStaffIds").val(selectStaffIds);
		
		var selectStaffCount = 0;
		if (selectStaffIds.indexOf(",") >= 0) {
			var ary = selectStaffIds.split(",");
			var selectStaffCount = ary.length;
		} else {
			selectStaffCount = 1;
		}
		
		var staffNums = $("#staffNums").val();
		if (staffNums != selectStaffCount) {
			alert("当前订单录入服务人数为" + staffNums + "人, 派工人数选择了" + selectStaffCount + "人,不一致.")
			return false;
		}
		
		orderFormSubmit();
	}
	
})

function orderFormSubmit() {
	// 提交订单
	var params = {};
	params.userId = $("#userId").val();
	params.orderType = $("#orderType").val();
	params.serviceType = $("#serviceType").val();
	params.serviceDate = $("#serviceDate").val();
	params.addrId = $("#addrId").val();
	params.serviceHour = $("#serviceHour").val();
	params.staffNums = $("#staffNums").val();
	params.orderPay = $("#orderPay").val();
	params.serviceAddonDatas = $("#serviceAddonDatas").val();
	params.selectStaffIds = $("#selectStaffIds").val();
	params.orderPayType = $("input[name='orderPayType']").val();
	params.adminId = $("#adminId").val();
	params.adminName = $("#adminName").val();
	params.orderFrom = 2;
	params.orderOpFrom = $("#orderOpFrom").val();
	params.couponsId = $("#couponsId").val();
	params.remarks = $("#remarks").val();
	params.periodOrderId = $("#periodOrderId").val();
	
	if ($("#sendSmsToUser").is(":checked")) {
		params.sendSmsToUser = 1;
	} else {
		params.sendSmsToUser = 0;
	}
	
	$.ajax({
		type : "post",
		url : "/jhj-app/app/order/post_order_add.json",
		data : params,
		dataType : "json",
		async : false,
		success : function(data) {
			console.log(data);
			if (data.status == 999) {
				alert(data.msg);
				return false;
			}
			alert("订单添加成功！");
			location.href = "order-list";
		}
	});
}