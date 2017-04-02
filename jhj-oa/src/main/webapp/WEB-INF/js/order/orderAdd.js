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

// ===============地址相关======================================================
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
					if (isVip == 0) $("#userTypeStr").html("普通会员");
					if (isVip == 1) $("#userTypeStr").html("金牌会员");
					serviceTypeChange();
					// changePrice();
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

function addrChange() {
	var addrName = $("#addrId").find("option:selected").text();
	$("#selectedAddrName").html(addrName);
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

// ============服务类别相关=========================================================
$("#parentServiceType").on('change', function() {
	var parentServiceTypeId = $(this).val();
	if (0 == parentServiceTypeId) {
		return false;
	}
	
	if (parentServiceTypeId == 23 || parentServiceTypeId == 24) {
		$("#orderType").val(0);
		$("#divServiceAddons").css("display", "none");
	} else {
		$("#orderType").val(1);
		$("#divServiceAddons").css("display", "block");
	}
});

function serviceTypeChange() {
	var serviceType = $("select[name='serviceType']").val();
	
	if (serviceType == "" || serviceType == undefined) {
		return false;
	}
	
	var parentServiceType = $("#parentServiceType").val();
	
	if (parentServiceType == 23 || parentServiceType == 24) {
		$("#divServiceAddons").css("display", "none");
		serviceTypeChangeHour();
	} else {
		serviceTypeChangeExp();
	}
}

// 金牌保洁服务类别
function serviceTypeChangeHour() {
	var id = $("select[name='serviceType'] option:selected").val();
	
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
			changePriceHour();
		}
	});
}

// 深度养护服务类别
function serviceTypeChangeExp() {
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
				
				serviceContentHtml += "<tr>";
				serviceContentHtml += '<td><input type="hidden" id="serviceAddonId" value="'
						+ serviceType[i].service_addon_id + '"/>';
				serviceContentHtml += '<input type="hidden" name="defaultNum" value="'
						+ serviceType[i].default_num + '"/>';
				serviceContentHtml += '<input type="hidden" name="serviceAddonServiceHour" value="'
						+ serviceType[i].service_hour + '"/>';
				serviceContentHtml += '<input type="hidden" name="serviceAddonPrice" value="'
						+ serviceType[i].price + '"/>';
				serviceContentHtml += '<input type="hidden" name="serviceAddonDisPrice" value="'
						+ serviceType[i].dis_price + '"/>';
				
				serviceContentHtml += '<input type="hidden" name="serviceAddonId" value="'
						+ serviceType[i].service_addon_id + '"/>';
				serviceContentHtml += serviceType[i].name + "</td>";
				if (isVip == 1) {
					serviceContentHtml += "<td>" + serviceType[i].dis_price + "&nbsp;<span>"
							+ serviceType[i].item_unit + "</span></td>";
				} else {
					serviceContentHtml += "<td>" + serviceType[i].price + "&nbsp;<span>"
							+ serviceType[i].item_unit + "</span></td>";
				}
				
				serviceContentHtml += "<td>" + serviceType[i].service_hour + "小时</td>";
				serviceContentHtml += "<td><input type='text' name='itemNum' value='"
						+ serviceType[i].default_num
						+ "' onkeyup='changePriceExp()'  onafterpaste='changePriceExp()' ></td>";
				serviceContentHtml += "</tr>";
			}
			
			$("#service-content").append(serviceContentHtml);
			
			sessionStorage.removeItem("totalOrderPay");
			$("#divServiceAddons").css("display", "block");
			changePriceExp();
		}
	});
}

serviceTypeChange();

// =====================价格相关====================================================
function changePrice(courponsValue) {
	var serviceType = $("select[name='serviceType']").val();
	
	if (serviceType == "" || serviceType == undefined) {
		return false;
	}
	
	var parentServiceType = $("#parentServiceType").val();
	
	if (parentServiceType == 23 || parentServiceType == 24) {
		$("#divServiceAddons").css("display", "none");
		changePriceHour();
	} else {
		changePriceExp();
	}
}

function changePriceHourCheck(courponsValue) {
	var serviceType = $("select[name='serviceType']").val();
	
	if (serviceType == "" || serviceType == undefined) {
		return false;
	}
	
	var parentServiceType = $("#parentServiceType").val();
	
	if (parentServiceType == 23 || parentServiceType == 24) {
		$("#divServiceAddons").css("display", "none");
		changePriceHour();
	}
}

// 金牌保洁价格计算
function changePriceHour(couponsValue) {
	var serviceHours = $("#serviceHour").val();
	serviceHours = serviceHours.replace(/\D|^0/g, '');
	var staffNums = $("#staffNums").val();
	staffNums = staffNums.replace(/\D|^0/g, '');
	
	var minServiceHour = $("#minServiceHour").val();
	var serviceType = $("select[name='serviceType'] option:selected").val();
	
	var price = $("#price").val();
	var mprice = $("#mprice").val();
	var pprice = $("#pprice").val();
	var mpprice = $("#mpprice").val();
	
	var isVip = $("#isVip").val();
	if (isVip == undefined || isVip == "") isVip = 0;
	// console.log(" changePrice price ==" + price);
	// console.log(" changePrice mprice ==" + mprice);
	// console.log(" changePrice pprice ==" + pprice);
	// console.log(" changePrice mpprice ==" + mpprice);
	// console.log(" changePrice is_vip ==" + isVip);
	var orderHourPay = pprice;
	var orderHourPrice = price;
	if (isVip == 1) {
		orderHourPay = mpprice;
		orderHourPrice = mprice;
	}
	
	if (couponsValue == undefined || couponsValue == null || couponsValue == '') {
		var val = $("#couponsId").find(":selected").text();
		couponsValue = parseInt(val);
	}
	
	if (staffNums == 1 && serviceHours == minServiceHour) {
		var val = sessionStorage.getItem("totalOrderPay");
		if (parseFloat(val) > 0) {
			orderHourPay = val;
		}
		$("#orderPay").val(orderHourPay - couponsValue);
	}
	if (staffNums > 1 || serviceHours > minServiceHour) {
		orderPay = parseFloat(orderHourPay) + parseFloat(serviceHours - minServiceHour)
				* parseFloat(orderHourPrice);
		orderPay = orderPay * staffNums - couponsValue;
		// orderPay = orderHourPrice * serviceHours * staffNums-couponsValue;
		var val = sessionStorage.getItem("totalOrderPay");
		if (parseFloat(val) > 0) {
			orderPay = val;
		}
		$("#orderPay").val(orderPay - couponsValue);
	}
}

// 深度养护类型价格计算
function changePriceExp(couponsValue) {
	$("#orderPay").val(0);
	$("#serviceHour").val(0);
	$("#serviceAddonDatas").val("");
	var totalOrderPay = 0;
	var totalServiceHour = 0;
	var serviceAddonsJson = [];
	$("#serviceAddonTable")
			.find("tr")
			.each(
					function() {
						
						var serviceAddonDisPrice = "";
						var serviceAddonPrice = "";
						var defaultNum = "";
						var itemNum = "";
						var serviceAddonServiceHour = "";
						var serviceAddonId = "";
						
						$(this)
								.find("td input")
								.each(
										function() {
											// console.log("attr = " +
											// $(this).attr("name"));
											if ($(this).attr("name") == "serviceAddonId") serviceAddonId = $(
													this).val();
											if ($(this).attr("name") == "serviceAddonPrice") serviceAddonPrice = $(
													this).val();
											if ($(this).attr("name") == "serviceAddonDisPrice") serviceAddonDisPrice = $(
													this).val();
											if ($(this).attr("name") == "defaultNum") defaultNum = $(
													this).val();
											if ($(this).attr("name") == "serviceAddonServiceHour") serviceAddonServiceHour = $(
													this).val();
											if ($(this).attr("name") == "itemNum") {
												var tmptxt = $(this).val();
												$(this).val(tmptxt.replace(/\D|^0/g, ''));
												itemNum = $(this).val();
											}
										});
						
						// console.log("serviceAddonDisPrice = " +
						// serviceAddonDisPrice);
						// console.log("defaultNum = " + defaultNum);
						// console.log("serviceAddonServiceHour = " +
						// serviceAddonServiceHour);
						// console.log("itemNum = " + itemNum);
						
						var isVip = $("#isVip").val();
						var servicePrice = serviceAddonPrice;
						if (isVip == 1) servicePrice = serviceAddonDisPrice
						if (itemNum != "" && itemNum != 0) {
							var orderPay = servicePrice * itemNum;
							totalOrderPay += orderPay;
							
							var serviceHour = serviceAddonServiceHour * itemNum;
							
							if (defaultNum != "" && defaultNum != 0) {
								serviceHour = (serviceAddonServiceHour / defaultNum) * itemNum;
							}
							
							totalServiceHour += serviceHour;
							
							serviceAddonsJson.push(jQuery.parseJSON('{"serviceAddonId":'
									+ serviceAddonId + ',"itemNum":' + itemNum + '}'));
							
						}
					});
	
	if (couponsValue == undefined || couponsValue == null || couponsValue == '') {
		var val = $("#couponsId").find(":selected").text();
		couponsValue = parseInt(val);
	}
	
	if (totalOrderPay != undefined && totalOrderPay != "" && totalOrderPay != 0) {
		totalOrderPay = totalOrderPay.toFixed(2);
		var val = sessionStorage.getItem("totalOrderPay");
		if (parseFloat(val) > 0) {
			totalOrderPay = val;
		}
		$("#orderPay").val(totalOrderPay - couponsValue);
	}
	
	if (totalServiceHour != undefined && totalServiceHour != "" && totalServiceHour != 0) {
		totalServiceHour = Math.round(totalServiceHour)
		$("#serviceHour").val(totalServiceHour);
	}
	
	// console.log(JSON.stringify(serviceAddonsJson));
	// json赋值.
	if (serviceAddonsJson.length > 0) {
		$("#serviceAddonDatas").val(JSON.stringify(serviceAddonsJson));
	}
	
}

// ==============优惠券相关===================================================
function selectCoupons() {
	var couponsValue = $("#couponsId").find(":selected").text();
	var value = parseInt(couponsValue);
	changePrice(value);
}

function setValue() {
	var orderPay = $("#orderPay").val();
	sessionStorage.setItem("totalOrderPay", orderPay);
}

/*
 * 提交订单
 */
function saveForm() {
	// $("#modalDispatch").modal("show");
	// return false;
	if ($('#orderForm').validate().form()) {
		
		var parentServiceType = $("#parentServiceType").val();
		
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
		var serviceDateUnix = moment(serviceDate).unix();
		loadStaffsByServiceDate(serviceDateUnix);
		$("input[name='disWay']").eq(0).attr("checked", true);
		$("input[name='disWay']").trigger("change");
		
	}
}

// =====================派工相关==========================================================

$('#selectedStaffs').tagsinput(
		{
			itemValue : 'id',
			itemText : 'label',
			tagClass : function(item) {
				var labelArray = [ 'label-primary', 'label-danger', 'label-success',
						'label-default', 'label-warning' ];
				var index = Math.floor((Math.random() * labelArray.length));
				var v = labelArray[index];
				return 'label ' + v;
			},
		});

$('#selectedStaffs').on('itemRemoved', function(event) {
	// event.item: contains the item
	if (event.item == undefined) return false;
	var staffId = event.item.id;
	$("input[name='select-staff']").each(function(k, v) {
		
		var selectStaffId = $(this).parent().find("#selectStaffId").val();
		
		var selectStaffName = $(this).parent().find("#selectStaffName").val();
		
		var distanceValue = $(this).parent().find("#distanceValue").val();
		
		if (selectStaffId == staffId) {
			// 如果该行被选中
			
			$(this).removeAttr("checked");
			
		}
	});
});

var loadStaffDynamic = function(data, status, xhr) {
	
	var result = data;
	
	// 清空原有数据
	$("#allStaff").html("");
	
	var tdHtml = "";
	
	if (result.length == 0) {
		alert("暂无可用派工");
	}
	
	var isMulti = $("#isMulti").val();
	
	for (var i = 0, j = result.length; i < j; i++) {
		
		var item = data[i];
		var selectInput = "";
		if (isMulti == 0) {
			selectInput = "<input name='select-staff' type='radio' onclick='doSelectStaff("
					+ item.staff_id + ")' value=" + item.staff_id + ">";
		} else {
			selectInput = "<input name='select-staff' type='checkbox' onclick='doSelectStaff("
					+ item.staff_id + ")' value=" + item.staff_id + ">";
		}
		
		var htmlStr = "<tr>";
		htmlStr += "<td>";
		if (item.dispath_sta_flag == 1) {
			htmlStr += selectInput;
		}
		
		htmlStr += "<input  type='hidden' id='selectStaffId' name='selectStaffId' value="
				+ item.staff_id + ">" + "<input type='hidden' id='distanceValue' value="
				+ item.distance_value + ">" + "<input type='hidden' id='selectStaffName' value="
				+ item.name + ">" + "</td>" + "<td>" + item.staff_org_name + "</td>" + "<td>"
				+ item.staff_cloud_org_name + "</td>" + "<td>" + item.org_distance_text + "</td>"
				+ "<td>" + item.name + "</td>" + "<td>" + item.mobile + "</td>" + "<td>"
				+ item.distance_text + "</td>" + "<td>" + item.today_order_num + "</td>" + "<td>"
				+ item.dispath_sta_str + "</td>" + "<td>" + item.reason + "</td>";
		htmlStr += "</tr>";
		tdHtml += htmlStr;
		
	}
	
	$("#allStaff").append(tdHtml);
	
	var selectedStaffs = $("#selectedStaffs").val();
	// console.log("selectedStaffs = " + selectedStaffs );
	if (selectedStaffs == undefined || selectedStaffs == "") {
		var serviceDate = $("#serviceDate").val()
		var serviceDateUnix = moment(serviceDate).unix();
		loadAutoDispatch(serviceDateUnix);
	}
	
	return false;
}

function doSelectStaff(staffId) {
	
	var selectStaffId = "";
	var selectStaffName = "";
	var distanceValue = "";
	$("input[name='select-staff']").each(function(k, v) {
		
		var selectStaffId = $(this).parent().find("#selectStaffId").val();
		
		var selectStaffName = $(this).parent().find("#selectStaffName").val();
		
		var distanceValue = $(this).parent().find("#distanceValue").val();
		
		if (selectStaffId == staffId) {
			// 如果该行被选中
			if (this.checked) {
				addSelectedStaffs(selectStaffId, selectStaffName, distanceValue);
			} else {
				$('#selectedStaffs').tagsinput('remove', {
					id : selectStaffId,
					label : selectStaffName,
					distanceValue : distanceValue
				});
			}
		}
		
	});
	console.log($('#selectedStaffs').val());
}

function doSelectStaffCheck(staffId) {
	
	var selectStaffId = "";
	var selectStaffName = "";
	var distanceValue = "";
	$("input[name='select-staff']").each(function(k, v) {
		
		var selectStaffId = $(this).parent().find("#selectStaffId").val();
		
		var selectStaffName = $(this).parent().find("#selectStaffName").val();
		
		var distanceValue = $(this).parent().find("#distanceValue").val();
		
		if (selectStaffId == staffId) {
			// 如果该行被选中
			$(this).attr("checked", "true");
			addSelectedStaffs(selectStaffId, selectStaffName, distanceValue);
		}
	});
	
}

function addSelectedStaffs(selectStaffId, selectStaffName, distanceValue) {
	var selectStaffIds = $("#selectedStaffs").val();
	
	if (selectStaffIds.indexOf(selectStaffId) >= 0) return false;
	
	$('#selectedStaffs').tagsinput('add', {
		id : selectStaffId,
		label : selectStaffName,
		distanceValue : distanceValue
	});
}

function remove(selectStaffId, selectStaffName, distanceValue) {
	var selectStaffIds = $("#selectedStaffs").val();
	
	if (selectStaffIds.indexOf(selectStaffId) >= 0) return false;
	
	$('#selectedStaffs').tagsinput('add', {
		id : selectStaffId,
		label : selectStaffName,
		distanceValue : distanceValue
	});
}

function loadAutoDispatch(serviceDate) {
	var addrId = $("#addrId").val();
	if (addrId == undefined || addrId == "") {
		return false;
	}
	
	var serviceType = $("#serviceType").val();
	if (serviceType == undefined || serviceType == "") {
		return false;
	}
	
	var params = {};
	
	params.addrId = $("#addrId").val();
	params.serviceHour = $("#serviceHour").val();
	params.serviceTypeId = $("#serviceType").val();
	params.serviceDate = serviceDate;
	params.staffNums = $("#staffNums").val();
	
	$.ajax({
		type : "get",
		url : "/jhj-oa/new_dispatch/load_auto_dispatch.json",
		data : params,
		dataType : "json",
		success : function(data) {
			
			if (data == undefined || data == '') return false;
			
			$('#selectedStaffs').tagsinput('removeAll');
			
			$('#selectedStaffs').val('');
			$.each(data, function(i, obj) {
				
				var staffId = obj.staff_id;
				var staffName = obj.name;
				doSelectStaffCheck(staffId);
			});
		}
	});
}

function loadStaffsByServiceDate(serviceDate) {
	// 根据 服务 时间, 动态获取 有无 可用派工
	
	var addrId = $("#addrId").val();
	if (addrId == undefined || addrId == "") {
		return false;
	}
	
	var serviceType = $("#serviceType").val();
	if (serviceType == undefined || serviceType == "") {
		return false;
	}
	
	var params = {};
	
	params.orderStatus = 2;
	params.addrId = $("#addrId").val();
	params.serviceHour = $("#serviceHour").val();
	params.serviceTypeId = $("#serviceType").val();
	params.serviceDate = serviceDate;
	
	$.ajax({
		type : "get",
		url : "/jhj-oa/new_dispatch/load_staff_by_change_service_date.json",
		data : params,
		dataType : "json",
		success : loadStaffDynamic
	});
}

/*
 * 选择 云店时，动态 展示 对应云店的 阿姨 派工状态
 */
// $("#parentId").on('change', function() {
// loadStaffs();
// });
$("#orgId").on('change', function() {
	loadStaffs();
});

function parentIdExtendChange() {
	loadStaffs();
}

function loadStaffs() {
	
	var orderStatus = $("#orderStatus").val();
	if (orderStatus == 1) return false;
	
	// 当前选中的 云店id
	var parentId = $("#parentId").val();
	
	// 根据 服务 时间, 动态获取 有无 可用派工
	var orderId = $("#id").val();
	
	// 如果未选择门店，直接返回
	if (parentId == 0) {
		return false;
	}
	
	// 获取当前选择的订单时间
	var serviceDateStr = $("#serviceDate").val();
	if (serviceDateStr == undefined || serviceDateStr == "") {
		return false;
	}
	var serviceDate = moment(serviceDateStr).unix();
	
	var addrId = $("#addrId").val();
	if (addrId == undefined || addrId == "") {
		return false;
	}
	
	var serviceType = $("#serviceType").val();
	if (serviceType == undefined || serviceType == "") {
		return false;
	}
	
	var params = {};
	params.parentId = parentId;
	params.orgId = $("#orgId").val();
	params.orderStatus = 2;
	params.addrId = $("#addrId").val();
	params.serviceHour = $("#serviceHour").val();
	params.serviceTypeId = $("#serviceType").val();
	params.serviceDate = serviceDate;
	$.ajax({
		type : "get",
		url : "/jhj-oa/new_dispatch/load_staff_by_change_cloud_org.json",
		data : params,
		dataType : "json",
		success : loadStaffDynamic
	});
}

// 点击选择 调整派工方案
$("input[name='disWay']").on("change", function() {
	// console.log("disWay change");
	var thisVal = $("input[name='disWay']:checked").val();
	if (thisVal == 0) {
		$("#div-org-id").hide();
		$("#div-cloud-id").hide();
		$("#modalDispatch").modal("show");
		
		var serviceDateUnix = moment(serviceDate).unix();
		loadStaffsByServiceDate(serviceDateUnix);
	}
	
	if (thisVal == 1) {
		$("#div-org-id").show();
		$("#div-cloud-id").show();
		
		loadStaffs();
	}
})

$("#orderSubmit").on("click", function() {
	
	$("#modalDispatch").modal("hide");
	
	var selectStaffIds = $("#selectedStaffs").val();
	if (selectStaffIds == undefined || selectStaffIds == "") {
		
		alert("没有选择派工人员.");
		return false;
	}
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
	
	// $("#orderForm").submit();
	
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
	params.orderPayType = $("#orderPayType").val();
	params.adminId = $("#adminId").val();
	params.adminName = $("#adminName").val();
	params.orderFrom = 2;
	params.orderOpFrom = $("#orderOpFrom").val();
	params.couponsId = $("#couponsId").val();
	params.remarks = $("#remarks").val();
	
//	console.log(params);
//	return false;
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
			location.href="order-list";
		}
	});
})