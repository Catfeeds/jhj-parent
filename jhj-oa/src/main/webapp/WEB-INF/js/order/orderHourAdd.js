var formVal = $('#orderHourForm').validate({
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
			min : 3,
			
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
			min : "服务时长必须超过3小时",
			
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


// 设置日历控件
$('#serviceDate').datetimepicker({
	format : "yyyy-mm-dd hh:ii",
	language : "zh-CN",
	autoclose : true,
	todayBtn : true,
	minuteStep : 30
});
$('#serviceDate').datetimepicker('setStartDate', new Date());

// 输入完手机号获取用户信息，根据用户的id获取用户的服务地址
function getAddrByMobile(addrId) {
	var mobile = $("#mobile").val();
	
	if (mobile == "" || mobile == undefined) return false;
	
	$("#addrId").find(":nth-child(2)").remove();
	var reg = /^1[3,5,7,8]\d{9}$/;
	if (reg.test(mobile)) {
		$.ajax({
			type : "post",
			url : "../user/getUser",
			data : {
				"mobile" : mobile
			},
			dataType : "json",
			success : function(data) {
				if (data.data != false) {
					var userId = data.data.id;
					$("#userId").data("userId", userId)
					$("#mobile").data("mobile", mobile);
					$("#userId").text(userId);
					$.ajax({
						type : "get",
						dataType : "json",
						url : "/jhj-app/app/user/get_user_addrs.json?user_id=" + userId,
						success : function(result) {
							var userAddr = result.data;
							var selectid = document.getElementById("addrId");
							for (var i = 0; i < userAddr.length; i++) {
								selectid[i + 1] = new Option(userAddr[i].name, userAddr[i].id,
										false, false);
							}
							
							if (addrId > 0) {
								$("#addrId").val(addrId);
							}
							
						}
					});
				} else {
					var isResult = confirm("是否添加该用户？");
					if (isResult) {
						regUser(mobile);
					}
					
				}
			}
		});
	}
}

// 页面第一次加载
getAddrByMobile(0);

function saveFrom() {
	
	var params = {};
	params.userId = $("#userId").text();
	params.mobile = $("#mobile").val();
	params.addrId = $("#addrId").val();
	params.serviceType = $("input[name='serviceType']").val();
	params.orderPay = $("#orderPay").val();
	params.orderFrom = $("#orderForm").val();
	var serviceDate = $("#serviceDate").val();
	params.serviceDate = moment(serviceDate + ":00", "yyyy-MM-DD HH:mm:ss").unix();
	params.serviceHour = $("input[name='serviceHour']").val();
	params.orderOpFrom = $("select[name='orderOpFrom']").val();
	;
	params.remarks = $("#remarks").val();
	var order_pay_type = $("#orderPayType").val();
	if ($('#orderHourForm').validate().form()) {
		$('#submitForm').attr('disabled', "true");
		$.ajax({
			type : "post",
			url : "/jhj-app/app/order/post_hour.json",
			data : params,
			dataType : "json",
			success : function(data) {
				var orderNo = data.data.order_no;
				var userId = data.data.user_id;
				if (data.status == 0) {
					savePay(order_pay_type, orderNo, userId);
				}
				if (data.status == 999) {
					alert(data.msg);
					$('#submitForm').removeAttr("disabled");
				}
			}
		});
	}
}

// 订单的支付方式
function savePay(orderPayType, orderNo, userId) {
	var data = {};
	data.order_pay_type = orderPayType;
	data.order_no = orderNo;
	data.user_id = userId;
	if (orderNo != null && userId != null) {
		$.ajax({
			type : "post",
			url : "/jhj-app/app/order/post_pay.json",
			data : data,
			dataType : "json",
			success : function(data) {
				alert("订单添加成功！");
				location.href = "order-hour-list";
			}
		});
	}
}

/*
 * 如果用户在系统中不存，注册新用户到系统中
 * 
 */
function regUser(mobile) {
	var mobile = mobile;
	if (mobile != null && mobile.length == 11) {
		$.ajax({
			type : "post",
			url : "/jhj-app/app/user/reg.json",
			data : {
				"mobile" : mobile
			},
			dataType : "json",
			success : function(data) {
				$("#userId").text(data.data);
				$("#userId").data("userId", data.data)
				$("#mobile").data("mobile", $("#mobile").val());
			}
		});
	}
}

// 为新增用户添加注册地址
function address() {
	var mobile = $("#mobile").val();
	if (mobile == "" || mobile == undefined) {
		alert("请先输入手机号码！");
		return;
	}
	$("#from-add-addr").show();
	// $('#exampleModal').on('show.bs.modal');
	
}

function saveAddress() {
	var form = {}
	form.user_id = $("#userId").data("userId");
	form.phone = $("#mobile").data("mobile");
	
	var lng = $("#poiLongitude").val();
	
	var lat = $("#poiLatitude").val();
	
	if (lng == undefined || lng == "" || lat == undefined || lat == "") {
		alert("请在服务地址下拉中选择!");
		return false;
	}
	
	
	form.longitude = $("#poiLongitude").val();
	form.latitude = $("#poiLatitude").val();
	
	
	
	
	form.name = $("#suggestId").val();
	form.addr = $("#recipient-addr").val();
	form.addr_id = 0;
	form.is_default = 1;
	form.city = "北京市";
	$.ajax({
		type : "post",
		url : "/jhj-app/app/user/post_user_addrs.json",
		data : form,
		dataType : "json",
		success : function(data) {
			$("#from-add-addr").hide();
			alert("地址添加成功");
			var addrId = data.data.id;
			getAddrByMobile(addrId);
		}
	});
}
