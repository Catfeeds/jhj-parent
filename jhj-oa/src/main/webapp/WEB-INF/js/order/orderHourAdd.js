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
			min : 2,
			
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


// 输入完手机号获取用户信息，根据用户的id获取用户的服务地址
function getAddrByMobile(addrId) {
	var mobile = $("#mobile").val();
	
	if (mobile == "" || mobile == undefined) return false;
	
	
	$("#addrId").find(":nth-child(2)").remove();
	var reg = /^1[3,4,5,6,7,8,9]\d{9}$/;
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
					$("#userId").val(userId);
					
					var isVip = data.data.isVip;
					$("#isVip").val(isVip);
					console.log(data.data);
					console.log("is_vip ==" + isVip);
					if (isVip == 0) $("#userTypeStr").html("普通会员");
					if (isVip == 1) $("#userTypeStr").html("金牌会员");
					chageServiceType();
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
	params.userId = $("#userId").val();
	params.mobile = $("#mobile").val();
	params.addrId = $("#addrId").val();
	params.serviceType = $("#serviceType").val();
	params.orderPay = $("#orderPay").val();
	params.orderFrom = $("#orderForm").val();
	var serviceDate = $("#serviceDate").val();
	
	params.serviceDate = moment(serviceDate + ":00", "YYYY-MM-DD HH:mm:ss").unix();
	params.serviceHour = $("input[name='serviceHour']").val();
	params.staffNums = $("#staffNums").val();
	params.orderOpFrom = $("select[name='orderOpFrom']").val();
	params.remarks = $("#remarks").val();
	var order_pay_type = $("#orderPayType").val();
	
	var couponsId = $("input[name='couponsId']:selected").val();
	params.coupons_id = couponsId;
	
	var user_id = $("#user_id").val();
	var user_name = $("#username").val();
	
	params.user_id = user_id;
	params.user_name = user_name;
	
	if ($('#orderHourForm').validate().form()) {
		$('#submitForm').attr('disabled', "true");
		$.ajax({
			type : "post",
			url : "/jhj-app/app/order/post_hour.json",
			data : params,
			dataType : "json",
			async:false,
			success : function(data) {
				var orderNo = data.data.order_no;
				var userId = data.data.user_id;
				if (data.status == 0) {
					savePay(order_pay_type, orderNo, userId,couponsId);
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
function savePay(orderPayType, orderNo, userId,couponsId) {
	var data = {};
	data.order_pay_type = orderPayType;
	data.order_no = orderNo;
	data.user_id = userId;
	data.coupon_id = couponsId;
	if (orderNo != null && userId != null) {
		$.ajax({
			type : "post",
			url : "/jhj-app/app/order/post_pay.json",
			data : data,
			dataType : "json",
			async:false,
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
				$("#userId").val(data.data);
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
function chageServiceType(){
	var id=$("select[name='serviceType'] option:selected").val();
	
	if (id == "") return false;
	
	$.ajax({
		type : "get",
		dataType : "json",
		url : "/jhj-app/app/dict/get_service_type.json?service_type_id=" + id,
		success : function(data) {
			var serviceType = data.data;
			
			$("#price").val(serviceType.price);
	    	$("#mprice").val(serviceType.mprice);
	    	$("#pprice").val(serviceType.pprice);
	    	$("#mpprice").val(serviceType.mpprice);
	    	
	    	$("#serviceHour").val(serviceType.service_hour);
			$("#minServiceHour").val(serviceType.service_hour);
			$("#staffNums").val(1);
	    	
			sessionStorage.removeItem("totalOrderPay");
			changePrice();
			
			
			
		}
	});
	
	
	
	
	
}

function changePrice(couponsValue) {
	var serviceHours = $("#serviceHour").val();
	serviceHours = serviceHours.replace(/\D|^0/g,'');
	var staffNums = $("#staffNums").val();
	staffNums = staffNums.replace(/\D|^0/g,'');
	
	var minServiceHour = $("#minServiceHour").val();
	console.log("minServiceHour = " + minServiceHour);
	var serviceType=$("select[name='serviceType'] option:selected").val();
	
	
	var price = $("#price").val();
	var mprice = $("#mprice").val();
	var pprice = $("#pprice").val();
	var mpprice = $("#mpprice").val();
	
	var isVip = $("#isVip").val();
	if (isVip == undefined || isVip == "") isVip = 0;
	console.log(" changePrice price ==" + price);
	console.log(" changePrice mprice ==" + mprice);
	console.log(" changePrice pprice ==" + pprice);
	console.log(" changePrice mpprice ==" + mpprice);
	console.log(" changePrice is_vip ==" + isVip);
	var orderHourPay = pprice;
	var orderHourPrice = price;
	if (isVip == 1) {
		orderHourPay = mpprice;
		orderHourPrice = mprice;
	}
	
	if(couponsValue==undefined || couponsValue==null || couponsValue==''){
		var val = $("#couponsId").find(":selected").text();
		couponsValue = parseInt(val);
	}
	
	
	
	if (staffNums == 1 && serviceHours == minServiceHour) {
		var val = sessionStorage.getItem("totalOrderPay");
		if(parseFloat(val)>0){
			orderHourPay = val;
		}
		$("#orderPay").val(orderHourPay-couponsValue);
	}
	if (staffNums > 1 || serviceHours > minServiceHour) {
		orderPay = parseFloat(orderHourPay) + parseFloat(serviceHours - minServiceHour) * parseFloat(orderHourPrice);
		orderPay = orderPay * staffNums-couponsValue;
//		orderPay = orderHourPrice * serviceHours * staffNums-couponsValue;
		var val = sessionStorage.getItem("totalOrderPay");
		if(parseFloat(val)>0){
			orderPay = val;
		}
		$("#orderPay").val(orderPay-couponsValue);
	}
}

chageServiceType();

//选择优惠券
function selectCoupons(){
	var couponsValue = $("#couponsId").find(":selected").text();
	var value = parseInt(couponsValue);
	changePrice(value);
}

function setValue(){
	var orderPay = $("#orderPay").val();
	sessionStorage.setItem("totalOrderPay",orderPay);
}
