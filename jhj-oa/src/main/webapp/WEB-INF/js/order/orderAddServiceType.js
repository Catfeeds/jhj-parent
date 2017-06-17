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

/**
 * 新居开荒和大扫除 调整到自动派工 
 * 		最低两人起（能派一人就派一人）
 * 		60平米两人 60-100 两人 
 * 		101-140 3人 
 * 		141-160 4人 
 * 		160以上4人 
 * 		200以上5人 （5人封顶） 自动派工后可以手动调整 
   擦玻璃改为自动派工，派单人.
 * @param serviceAddonId  = 29
 * @param itemNum
 * @returns {Boolean}
 */
function changeStaffNums(serviceAddonId, itemNum) {
	console.log("changeStaffNums");
	var orderTypeId = $("#orderType").val();
	if (orderTypeId == 0) return false;
	var serviceTypeId = $("#serviceType").val();	
	console.log("orderTypeId ==" + orderTypeId);
	console.log("serviceTypeId ==" + serviceTypeId);
	if (serviceTypeId != 56) return false;
	if (serviceAddonId != 29 ) return false;
	if (itemNum == undefined || itemNum == "" || itemNum == 0) return false;
	var staffNums = 1;
	if (itemNum < 60) staffNums = 1;
	if (itemNum >= 60 && itemNum <= 100) staffNums = 2;
	if (itemNum > 100 && itemNum <= 140) staffNums = 3;
	if (itemNum > 140 && itemNum <= 200) staffNums = 4;
	if (itemNum > 200) staffNums = 5;
	
	$("#staffNums").val(staffNums);
}