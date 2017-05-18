// ============服务类别相关=========================================================
$(".parentServiceType").on('change', function() {
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
	var id = $("select[name='serviceType']").val();
	
	if (id == "") return false;
	
	$.ajax({
		type : "get",
		dataType : "json",
		async : false,
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
		async : false,
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