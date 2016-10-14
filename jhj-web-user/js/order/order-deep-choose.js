myApp.onPageBeforeInit('order-deep-choose', function(page) {
	
	removeSessionData();
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
				html+= '<li><span onclick="onDeepAddItemNum($$(this).parent())">+</span>';
								
				html+= '<input name="itemNum" value="'+item.default_num+'" onkeyup="inputNum($$(this))"  onafterpaste="inputNum($$(this))"  maxLength="3" autocomplete="off">';
				html+= '<input type="hidden" name="serviceAddonName" value="'+name+'" autocomplete="off">';
				
				html+= '<input type="hidden" name="defaultNum" value="'+item.default_num+'" autocomplete="off">';
				html+= '<input type="hidden" name="serviceAddonServiceHour" value="'+item.service_hour+'" autocomplete="off">';
				
				html+= '<input type="hidden" name="serviceAddonItemUnit" value="'+item.item_unit+'" autocomplete="off">';
				html+= '<input type="hidden" name="serviceAddonDisPrice" value="'+item.dis_price+'" autocomplete="off">';
				html+= '<input type="hidden" name="serviceAddonId" value="'+item.service_addon_id+'" autocomplete="off">';
				html+= '<span onclick="onDeepSubItemNum($$(this).parent())">－</span></li>';
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
		 
		 setDeepTotal();
		 
		 var orderMoney = sessionStorage.getItem("order_money");
		 
		 if (orderMoney == undefined || orderMoney == "" || orderMoney == 0) {
			 myApp.alert("请选择服务数量.");
			 return false;
		 }
		 
		 if ($$("#addrId").val() == "") {
			 myApp.alert("请选择地址.");
			 return false;
		 }
		 var url = "order/order-lib-cal.html?next_url=order/order-deep-confirm.html"
		 mainView.router.loadPage(url);
	 });
	
});


//加号处理
function onDeepAddItemNum(obj) {
	console.log("onDeepAddItemNum");
	var itemNumObj = obj.find('input[name=itemNum]'); 
	var v = itemNumObj.val();
	if (v == undefined || !isNum(v)) {
		v = 0;
	} else {
		v = parseInt(v) + 1;
	}
	itemNumObj.val(v);
}

//减号处理
function onDeepSubItemNum(obj) {
	console.log("onDeepSubItemNum");
	var itemNumObj = obj.find('input[name=itemNum]'); 
	console.log(itemNumObj);
	var v = itemNumObj.val();
	if (v == undefined || !isNum(v) || v == 0) {
		v = 0;
	} else {
		v = parseInt(v) - 1;
	}
	
	//如果数量小于起步数，则只能等于起步数量
	var defaultNum = obj.find('input[name=defaultNum]').val(); 
	if (defaultNum > 0 && v < defaultNum) {
		myApp.alert("最低数量为"+ defaultNum);
		v = defaultNum;
	}
	
	itemNumObj.val(v);
}

//计算价格总和
function setDeepTotal() {

	var orderMoney = itemPrice = Number(0);
	var totalServiceHour = 0;
	var serviceAddonId = 0;
	var serviceAddonIdObj;
	var serviceAddonsJson = [];
	var serviceAddons = [];
	$$("input[name = itemNum]").each(function(key, index) {
		 
		 itemNum = $$(this).val();
		 
		 serviceAddonIdObj = $$(this).parent().find('input[name=serviceAddonId]');
		 serviceAddonId = serviceAddonIdObj.val();
		 
		 console.log("serviceAddonId = " + serviceAddonId);
		 if (itemNum == undefined || 
			 itemNum == "" || 
			 itemNum == 0 || 
			 serviceAddonId == undefined || 
			 serviceAddonId == 0) {
			 return false;
		 }
		 
		 var serviceAddonServiceHour = $$(this).parent().find('input[name=serviceAddonServiceHour]').val();
		 var defaultNum = $$(this).parent().find('input[name=defaultNum]').val();
		 var disPrice = $$(this).parent().find('input[name=serviceAddonDisPrice]').val();
		
		 
		 var reg = /[1-9][0-9]*/g;
		 
		 itemPrice = disPrice.match(reg);

		 orderMoney = Number(orderMoney) + Number(itemNum) * Number(itemPrice);
		 
		 var serviceHour = serviceAddonServiceHour * itemNum;
			
		 if(defaultNum != "" && defaultNum != 0) {
			serviceHour = (serviceAddonServiceHour / defaultNum) * itemNum;
		 }
		 
		 totalServiceHour+= serviceHour;
		 
		 var serviceAddonName = $$(this).parent().find('input[name=serviceAddonName]').val();
		 var serviceAddonItemUnit = $$(this).parent().find('input[name=serviceAddonItemUnit]').val();
		 
		 console.log("serviceAddonName = " + serviceAddonName);
		 console.log("serviceAddonItemUnit = " + serviceAddonItemUnit);
		 console.log("serviceAddonServiceHour = " + serviceAddonServiceHour);
		 console.log("defaultNum = " + defaultNum);
		 console.log("disPrice = " + disPrice);
		 console.log("itemNum = " + itemNum);
		
		
		 
		 
		 
		 var serviceAddonItem = {};
		 serviceAddonItem.serviceAddonName = serviceAddonName;
		 serviceAddonItem.serviceAddonId = serviceAddonId;
		 serviceAddonItem.itemNum = itemNum;
		 serviceAddonItem.itemUnit = serviceAddonItemUnit;
		 console.log(serviceAddonItem);
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
	
	console.log("order_money = " + sessionStorage.getItem("order_money"));
	console.log("total_service_hour = " + sessionStorage.getItem("total_service_hour"));
	console.log("service_addons = " + sessionStorage.getItem("service_addons"));
	console.log("service_addons_json = " + sessionStorage.getItem("service_addons_json"));
		
}
