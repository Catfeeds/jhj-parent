// =====================价格相关====================================================
function changePrice(courponsValue) {
	var serviceType = $("#serviceType").val();
	
	if (serviceType == "" || serviceType == undefined) {
		return false;
	}
	
	var parentServiceType = $("#parentServiceType").val();
	
	if (parentServiceType == 23 || parentServiceType == 24) {
		$("#divServiceAddons").css("display", "none");
		changePriceHour(courponsValue);
	} else {
		changePriceExp(courponsValue);
	}
}

function changePriceHourCheck(courponsValue) {
	var serviceType = $("#serviceType").val();
	
	if (serviceType == "" || serviceType == undefined) {
		return false;
	}
	
	var parentServiceType = $("#parentServiceType").val();
	
	if (parentServiceType == 23 || parentServiceType == 24) {
		$("#divServiceAddons").css("display", "none");
		changePriceHour(courponsValue);
	}
}

// 金牌保洁价格计算
function changePriceHour(couponsValue) {
	var serviceHours = $("#serviceHour").val();
	serviceHours = serviceHours.replace(/\D|^0/g, '');
	var staffNums = $("#staffNums").val();
	staffNums = staffNums.replace(/\D|^0/g, '');
	
	var minServiceHour = $("#minServiceHour").val();
	var serviceType = $("#serviceType").val();
	
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
	console.log("couponsValue ==" + couponsValue);
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
							
							changeStaffNums(serviceAddonId, itemNum);
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
	console.log("selectCoupons");
	console.log("couponsValue ==" + value);
	changePrice(value);
}

function setValue() {
	var orderPay = $("#orderPay").val();
	sessionStorage.setItem("totalOrderPay", orderPay);
}