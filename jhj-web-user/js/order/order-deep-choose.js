myApp.onPageBeforeInit('order-deep-choose', function(page) {
	
	var userId = localStorage['user_id'];
	getUserInfo(userId);
	
	var serviceTypeId = page.query.service_type_id;
	sessionStorage.setItem("service_type_id", serviceTypeId);
	
	sessionStorage.setItem("order_type", 1);
		
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "dict/get_service_type_addons.json?service_type_id=" + serviceTypeId,
		dataType : "json",
		async : false,
		success : function(data) {
			var serviceAddons = data.data;
			
			$$.each(serviceAddons, function(i, item) {
				var html = "";
				var name = item.name;
				if (name == "金牌保洁") return false;
				html = "<ul class='order-rili'>";
				html+= "<li>" + name + "</li>"
				html+= '<li><span onclick="onDeepSubItemNum($$(this).parent())">-</span>';
				
				var n = setItemNum(item.service_addon_id, item.default_num);
				html+= '<input name="itemNum" value="'+n+'" onkeyup="onItemNumKeyUp($$(this).parent())"  onafterpaste="onItemNumKeyUp($$(this).parent())"  maxLength="3" autocomplete="off">';
				html+= '<input type="hidden" name="serviceAddonName" value="'+name+'" autocomplete="off">';
				
				html+= '<input type="hidden" name="defaultNum" value="'+item.default_num+'" autocomplete="off">';
				html+= '<input type="hidden" name="serviceAddonServiceHour" value="'+item.service_hour+'" autocomplete="off">';
				
				html+= '<input type="hidden" name="serviceAddonItemUnit" value="'+item.item_unit+'" autocomplete="off">';
				html+= '<input type="hidden" name="serviceAddonPrice" value="'+item.price+'" autocomplete="off">';
				html+= '<input type="hidden" name="serviceAddonDisPrice" value="'+item.dis_price+'" autocomplete="off">';
				html+= '<input type="hidden" name="serviceAddonId" value="'+item.service_addon_id+'" autocomplete="off">';
				html+= '<span onclick="onDeepAddItemNum($$(this).parent())">+</span></li>';
				html+= "</ul>";
					
				$$("#order-deep-content").append(html);
			});
			
		}
	});
	
	// 地址选择处理，1. 是否有默认地址， 2. 是否有选择的地址（最优先）
	var addrId = "";
	var addrName = ""
	if (localStorage['default_addr_id'] != null)  addrId = localStorage['default_addr_id'];
	if (localStorage['default_addr_name'] != null)  addrName = localStorage['default_addr_name'];
	
	//优先为刚选择的地址
	if (sessionStorage.getItem('addr_id') != null)  addrId = sessionStorage.getItem('addr_id');
	if (sessionStorage.getItem('addr_name') != null)  addrName = sessionStorage.getItem('addr_name');
	
	if (addrId != undefined || addrId != "") $$("#addrId").val(addrId);
	if (addrName != undefined || addrName != "") {
		$$("#orderChooseAddrName").html(addrName);
	}
	
	 $$("#chooseServiceTime").on("click",function() {
		 
		 var validateMsg = setDeepTotal();
		 if (validateMsg != undefined && validateMsg != "") {
			 myApp.alert(validateMsg);
			 return false;
		 }
		 
		 var orderMoney = sessionStorage.getItem("order_money");
		 
		 if (orderMoney == undefined || orderMoney == "" || orderMoney == 0) {
			 myApp.alert("请选择服务数量.");
			 return false;
		 }
		 
		 var addrId = $$("#addrId").val();
		 if (addrId == undefined || addrId == "" || addrId == 0) {
				myApp.alert("请选择地址.");
				return false;	
		 }
		 var url = "order/order-lib-cal.html?next_url=order/order-deep-confirm.html"
			 
	     var staffId = sessionStorage.getItem("staff_id");
		 if (staffId != undefined && staffId != "" && staffId != null) {
			 url+="?staff_id="+staffId;
		 }	 
		
		 mainView.router.loadPage(url);
	 });
	
});


function onItemNumKeyUp(obj) {
	var itemNumObj = obj.find('input[name=itemNum]'); 
	
	var tmptxt=itemNumObj.val();
	tmptxt = tmptxt.replace(/\D/g,'');
	itemNumObj.val(tmptxt);
	
	setDeepTotal();
}

//加号处理
function onDeepAddItemNum(obj) {
	var itemNumObj = obj.find('input[name=itemNum]'); 
	var v = itemNumObj.val();
	if (v == undefined || !isNum(v)) {
		v = 0;
	} else {
		v = parseInt(v) + 1;
	}
	itemNumObj.val(v);
	
	var validateMsg = setDeepTotal();
//	console.log("validateMsg == " + validateMsg);
//	if (validateMsg != undefined && validateMsg != "") {
//		myApp.alert(validateMsg);
//		return false;
//	}
}

//减号处理
function onDeepSubItemNum(obj) {
	console.log("onDeepSubItemNum");
	var itemNumObj = obj.find('input[name=itemNum]'); 
	var v = itemNumObj.val();
	if (v == undefined || !isNum(v) || v == 0) {
		v = 0;
	} else {
		v = parseInt(v) - 1;
	}
	
	itemNumObj.val(v);
	
	var validateMsg = setDeepTotal();
//	console.log("validateMsg == " + validateMsg);
//	if (validateMsg != undefined && validateMsg != "") {
//		myApp.alert(validateMsg);
//		return false;
//	}
	
	
}

function checkDefaultNum() {
	//判断如果有起步数量 ，并且有多个，则只要有一个不超过起步数量即可.
	console.log("checkDefaultNum");
	var hasDefaultNum = false;
	
	var inputItemNum = 0;
	var minDefaultNum = 0;
	
	
	$$("input[name = itemNum]").each(function(key, index) {
		itemNum = $$(this).val();
		 
		 if (itemNum == undefined || itemNum == "") {
			 itemNum = 0;
		 } 
		 
		 inputItemNum = Number(inputItemNum) + Number(itemNum);
		 
		 
		 
		 var serviceAddonName = $$(this).parent().find('input[name=serviceAddonName]').val();
		 var defaultNum = $$(this).parent().find('input[name=defaultNum]').val();
		 
		 if (minDefaultNum == 0 ) {
			 minDefaultNum = defaultNum;
		 } else {
			 if (minDefaultNum > defaultNum)   minDefaultNum = defaultNum;
		 }
	});
	console.log("minDefaultNum==" + minDefaultNum );
	console.log("inputItemNum==" + inputItemNum);
	var validateMsg = ""
	if (Number(minDefaultNum) > 0 && Number(inputItemNum) < Number(minDefaultNum) ) {
		validateMsg = "最低数量为" +minDefaultNum;
	}
	
	console.log("validateMsg == " + validateMsg);
	
	
	
	
	return validateMsg;
}

function setItemNum(serviceAddonId, defaultNum) {

	var itemNum = defaultNum;
	var serviceAddonsJson = sessionStorage.getItem("service_addons_json");
	
	console.log("serviceAddonsJson = " + serviceAddonsJson);
	if (serviceAddonsJson == undefined || serviceAddonsJson == "") {
		return itemNum;
	}
	
	var serviceAddons = JSON.parse(serviceAddonsJson);
	$$.each(serviceAddons, function(i, item) {
		var itemServiceAddonId = item.serviceAddonId;
		var tmpItemNum = item.itemNum;
		
		if (itemServiceAddonId == serviceAddonId) {
			itemNum = tmpItemNum;
			return false;
		}
	});
	
	return itemNum;
}

//计算价格总和
function setDeepTotal() {

	var orderMoney = itemPrice = Number(0);
	var totalServiceHour = 0;
	var serviceAddonId = 0;
	var serviceAddonIdObj;
	var serviceAddonsJson = [];
	var serviceAddons = [];
	
	var isVip = localStorage['is_vip'];
	if (isVip == undefined || isVip == "") isVip = 0;

	$$("input[name = itemNum]").each(function(key, index) {
		 
		 itemNum = $$(this).val();
		 
		 if (itemNum == undefined || itemNum == "") {
			 itemNum = 0;
		 } 
		 
		 serviceAddonIdObj = $$(this).parent().find('input[name=serviceAddonId]');
		 serviceAddonId = serviceAddonIdObj.val();
		 var serviceAddonName = $$(this).parent().find('input[name=serviceAddonName]').val();
		 var defaultNum = $$(this).parent().find('input[name=defaultNum]').val();
		
		 
		 if (itemNum == 0 || 
			 serviceAddonId == undefined || 
			 serviceAddonId == 0) {
			 return false;
		 }
		 
		 var serviceAddonServiceHour = $$(this).parent().find('input[name=serviceAddonServiceHour]').val();
		 
		 var price = $$(this).parent().find('input[name=serviceAddonPrice]').val();
		 var disPrice = $$(this).parent().find('input[name=serviceAddonDisPrice]').val();
		
		 
		 var reg = /[1-9][0-9]*/g;
		 if (isVip == 0) itemPrice = price.match(reg);
		 if (isVip == 1) itemPrice = disPrice.match(reg);

		 orderMoney = Number(orderMoney) + Number(itemNum) * Number(itemPrice);
		 
		 var serviceHour = serviceAddonServiceHour * itemNum;
			
		 if(defaultNum != "" && defaultNum != 0) {
			serviceHour = (serviceAddonServiceHour / defaultNum) * itemNum;
		 }
		 
		 totalServiceHour+= serviceHour;
		 
		 
		 var serviceAddonItemUnit = $$(this).parent().find('input[name=serviceAddonItemUnit]').val();
		 
		 
		 var serviceAddonItem = {};
		 serviceAddonItem.serviceAddonName = serviceAddonName;
		 serviceAddonItem.serviceAddonId = serviceAddonId;
		 serviceAddonItem.itemNum = itemNum;
		 serviceAddonItem.itemUnit = serviceAddonItemUnit;
		 serviceAddons.push(serviceAddonItem); 
		 
		 var serviceAddonJson = {}
		 serviceAddonJson.serviceAddonId = serviceAddonId;
		 serviceAddonJson.itemNum = itemNum;
		 
		 
		 serviceAddonsJson.push(serviceAddonJson); 
	});
		
	totalServiceHour = totalServiceHour.toFixed(0);
	sessionStorage.setItem("order_money", orderMoney);
	sessionStorage.setItem("order_pay", orderMoney);
	sessionStorage.setItem("total_service_hour", totalServiceHour);
	sessionStorage.setItem("service_addons", JSON.stringify(serviceAddons));
	sessionStorage.setItem("service_addons_json", JSON.stringify(serviceAddonsJson));
	
	var validateMsg = checkDefaultNum();
	if (validateMsg != undefined && validateMsg != "") {
		return validateMsg;
	}
	
	return validateMsg;
}
