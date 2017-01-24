var formVal = $('#orderExpForm').validate({
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
		
		ItemNum : {
			required : true,
			digits : true
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
		
		ItemNum : {
			required : "数量不能为空",
			digits : "请输入有效的数字"
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
			min : "服务时长必须超过1小时",
			
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
					$("#userId").val(userId);
					var isVip = data.data.isVip;
					$("#isVip").val(isVip);
					console.log(data.data);
					console.log("is_vip ==" + isVip);
					if (isVip == 0) $("#userTypeStr").html("普通会员");
					if (isVip == 1) $("#userTypeStr").html("金牌会员");
					serviceTypeChange();
//					changePrice();
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

getAddrByMobile(0);

/*
 * 提交订单
 */
function saveFrom() {
	console.log("saveform");
	var serviceAddonDatas = $("#serviceAddonDatas").val();
	console.log(serviceAddonDatas);
	console.log(serviceAddonDatas.length);
	if (serviceAddonDatas == undefined || serviceAddonDatas == "" || serviceAddonDatas == []) {
		alert("请输入服务子项的数量");
		return false;
	}	
	
	var params = {};
	params.user_id = $("#userId").val();
	params.mobile = $("#mobile").val();
	params.addr_id = $("#addrId").val();
	params.service_type = $("#serviceType").val();
	params.order_pay = $("#orderPay").val();
	params.order_from = $("#orderFrom").val();
	params.order_op_from = $("#orderOpFrom").val();
	params.serviceHour = $("#serviceHour").val();
	var serviceDate = $("#serviceDate").val();
	params.service_date = moment(serviceDate + ":00", "YYYY-MM-DD HH:mm:ss").unix();
	params.remarks = $("#remarks").val();
	var orderPayType = $("#orderPayType").val();
	params.service_addons_datas = $("#serviceAddonDatas").val();
	
	var couponsId = $("#couponsId :selected").val();
	params.coupons_id = couponsId;
	
	params.userid = $("#userid").val();
	params.user_name = $("#username").val();

	if ($('#orderExpForm').validate().form()) {
		$('#submitForm').attr('disabled',"true");
		$.ajax({
			type : "post",
			url : "/jhj-app/app/order/post_exp.json",
			data : params,
			dataType : "json",
			async:false,
			success : function(data) {
				console.log(data);
				var orderNo = data.data.order_no;
				var userId = data.data.user_id;
				var service_type=data.data.service_type;
				
				if (data.status == 0) {
					savePay(orderPayType, orderNo, userId,service_type,couponsId);
				}
				if (data.status == 999) {
					alert(data.msg);
					$('#submitForm').removeAttr("disabled");
				}
			}
		});
	}
}

//订单的支付方式
function savePay(orderPayType, orderNo, userId,service_type,couponsId) {
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
				if(service_type==62 || service_type==63 ||service_type==64 || service_type==65){
					location.href="order-exp-baby-list";
				}else{
					location.href="order-exp-list";
				}
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
function address(){
	var mobile=$("#mobile").val();
	if (mobile == "" || mobile == undefined) {
		alert("请先输入手机号码！");
		return false;
	}
	$("#from-add-addr").show();
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
			$("#userId").removeData("userId");
			$("#mobile").removeData("mobile");
			getAddrByMobile(addrId);
		}
	});
}

function serviceTypeChange() {
	var serviceType = $("select[name='serviceType']").val();
	
	if (serviceType == "" || serviceType == undefined) {
		return false;
	}
	
	$.ajax({
		type : "get",
		url : "/jhj-app/app/dict/get_service_type_addons.json",
		data : {
			"service_type_id" : serviceType
		},
		dataType : "json",
		success : function(data) {
			$("#service-content").children().remove();
			var serviceType = data.data;
			var serviceContentHtml = "";
			var isVip = $("#isVip").val();
			for (var i = 0; i < serviceType.length; i++) {
				
				serviceContentHtml+="<tr>";
				serviceContentHtml+='<td><input type="hidden" id="serviceAddonId" value="' + serviceType[i].service_addon_id + '"/>';
				serviceContentHtml+='<input type="hidden" name="defaultNum" value="' + serviceType[i].default_num + '"/>';
				serviceContentHtml+='<input type="hidden" name="serviceAddonServiceHour" value="' + serviceType[i].service_hour + '"/>';
				serviceContentHtml+='<input type="hidden" name="serviceAddonPrice" value="' + serviceType[i].price + '"/>';
				serviceContentHtml+='<input type="hidden" name="serviceAddonDisPrice" value="' + serviceType[i].dis_price + '"/>';
				
				serviceContentHtml+='<input type="hidden" name="serviceAddonId" value="' + serviceType[i].service_addon_id + '"/>';
				serviceContentHtml+= serviceType[i].name + "</td>";
				if (isVip == 1) {
					serviceContentHtml+="<td>" + serviceType[i].dis_price + "&nbsp;<span>" + serviceType[i].item_unit + "</span></td>";
				} else {
					serviceContentHtml+="<td>" + serviceType[i].price + "&nbsp;<span>" + serviceType[i].item_unit + "</span></td>";
				}
				
				
				serviceContentHtml+="<td>" + serviceType[i].service_hour + "小时</td>";
				serviceContentHtml+="<td><input type='text' name='itemNum' value='" + serviceType[i].default_num + "' onkeyup='changePrice()'  onafterpaste='changePrice()' ></td>";
				serviceContentHtml+="</tr>";
			}
			
			$("#service-content").append(serviceContentHtml);
			
			sessionStorage.removeItem("totalOrderPay");
			changePrice();
		}
	});
}

serviceTypeChange();

function changePrice(couponsValue) {
	$("#orderPay").val(0);
	$("#serviceHour").val(0);
	$("#serviceAddonDatas").val("");
	var totalOrderPay = 0;
	var totalServiceHour = 0;
	var serviceAddonsJson = [];
	$("#serviceAddonTable").find("tr").each(function() {
		
		var serviceAddonDisPrice = "";
		var serviceAddonPrice = "";
		var defaultNum = "";
		var itemNum = "";
		var serviceAddonServiceHour = "";
		var serviceAddonId = "";
			
		$(this).find("td input").each(function(){
//			console.log("attr = " + $(this).attr("name"));
			if($(this).attr("name") == "serviceAddonId") serviceAddonId = $(this).val();
			if($(this).attr("name") == "serviceAddonPrice") serviceAddonPrice = $(this).val();
			if($(this).attr("name") == "serviceAddonDisPrice") serviceAddonDisPrice = $(this).val();
			if($(this).attr("name") == "defaultNum") defaultNum = $(this).val();
			if($(this).attr("name") == "serviceAddonServiceHour") serviceAddonServiceHour = $(this).val();
			if($(this).attr("name") == "itemNum") {
				var tmptxt=$(this).val();
				$(this).val(tmptxt.replace(/\D|^0/g,''));
				itemNum = $(this).val();
			}
		});
		
//		console.log("serviceAddonDisPrice = " + serviceAddonDisPrice);
//		console.log("defaultNum = " + defaultNum);
//		console.log("serviceAddonServiceHour = " + serviceAddonServiceHour);
//		console.log("itemNum = " + itemNum);
		
		var isVip = $("#isVip").val();
		var servicePrice = serviceAddonPrice;
		if (isVip == 1) servicePrice = serviceAddonDisPrice
		if(itemNum != "" && itemNum != 0) {
			var orderPay = servicePrice * itemNum;
			totalOrderPay+= orderPay;
			
			var serviceHour = serviceAddonServiceHour * itemNum;
			
			if(defaultNum != "" && defaultNum != 0) {
				serviceHour = (serviceAddonServiceHour / defaultNum) * itemNum;
			}
			
			
			totalServiceHour+= serviceHour;
			
			serviceAddonsJson.push(jQuery.parseJSON('{"serviceAddonId":' + serviceAddonId + ',"itemNum":' + itemNum + '}')); 
			
		}
	});
	
	if(couponsValue==undefined || couponsValue==null || couponsValue==''){
		var val = $("#couponsId").find(":selected").text();
		couponsValue = parseInt(val);
	}
	
	if (totalOrderPay != undefined && totalOrderPay != "" && totalOrderPay != 0) {
		totalOrderPay = totalOrderPay.toFixed(2);
		var val = sessionStorage.getItem("totalOrderPay");
		if(parseFloat(val)>0){
			totalOrderPay = val;
		}
		$("#orderPay").val(totalOrderPay-couponsValue);
	}
	
	if (totalServiceHour != undefined && totalServiceHour != "" && totalServiceHour != 0) {
		totalServiceHour = Math.round(totalServiceHour)
		$("#serviceHour").val(totalServiceHour);
	}
	
//	console.log(JSON.stringify(serviceAddonsJson));
	// json赋值.
	if (serviceAddonsJson.length > 0) {
		$("#serviceAddonDatas").val(JSON.stringify(serviceAddonsJson) );
	}
	
}

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
