// ============服务类别相关=========================================================

function serviceTypeChange(orderTypeId,serviceTypeId,orderTypeName) {

	if (serviceTypeId == "" || serviceTypeId == undefined) {
		return false;
	}
	
	if (orderTypeId == 23 || orderTypeId == 24) {
		$("#divServiceAddons").css("display", "none");
		$("#orderType").val(0);
		serviceTypeChangeHour(serviceTypeId);
		$("#divServiceAddons1").css({"display":"block"});
		$("#divServiceAddons2").val(orderTypeName);
	} else {
		$("#orderType").val(1);
		serviceTypeChangeExp(serviceTypeId);
		$("#divServiceAddons1").css({"display":"none"});
	}
	console.log("orderTYpe ==" + $("#orderType").val());
}

// 金牌保洁服务类别
function serviceTypeChangeHour(serviceTypeId) {
	var id = serviceTypeId;
	
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
function serviceTypeChangeExp(serviceTypeId) {
//	var serviceType = $("select[name='serviceType']").val();
	var serviceType = serviceTypeId;
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

$("#order-type").on('mouseover','li span',function(){
	var that = $(this).parent();
	var serviceTypeId = $(this).parent().attr("data-order-type");
	$("#order-type").find('div .chilrdMenu').css({"display":"none"});
	$("#serviceTypeId").val(serviceTypeId);
	$.ajax({
		type: 'GET',
		url: '/jhj-oa/newbs/get-service-types.json',
		dataType: 'json',
		cache: false,
		data:{parentId:serviceTypeId},
		success:function($result){
			if(0 == $result.status){
				var serviceTypeHtml = '<ul class="chilrdMenu" >';
				$.each($result.data, function(i, obj) {
					serviceTypeHtml += '<li class="service-type-li" data-service-type="'+obj.service_type_id+'">' + obj.name + "</li>";
				});
				serviceTypeHtml += "</ul>"
				$(that).find("div").html(serviceTypeHtml);
			}
		}
	});
});

$("#order-type li div").on("mouseleave",function(){
	$(this).find(".chilrdMenu").remove();
});

$(document).on('click','.service-type-li',function(){
	var serviceTypeId = $(this).attr("data-service-type");
	var orderTypeId = $(this).parents(".order-type-li").attr("data-order-type");
	var orderTypeName = $(this).text();
	$(this).css({"background":"red"});
	$("#serviceType").val(serviceTypeId);
	$("#parentServiceType").val(orderTypeId);
	$(this).parent().remove();
	serviceTypeChange(orderTypeId,serviceTypeId,orderTypeName);
});