myApp.onPageBeforeInit('order-custom-choose', function(page) {
	
	sessionStorage.removeItem("logined_next_url");
	var userId = localStorage['user_id'];
	getUserInfo();
	
	var serviceTypeId = page.query.service_type_id;
	sessionStorage.setItem("service_type_id", serviceTypeId);
	console.log("serviceTypeId = " + serviceTypeId);
	sessionStorage.setItem("order_type", 1);
		
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "dict/get_service_type_addons.json?service_type_id=" + serviceTypeId,
		dataType : "json",
		async : false,
		success : function(data) {
			var serviceAddons = data.data;
			
			var serviceAddonContentBx = "";
			var serviceAddonContentKt = "";
			var serviceAddonContentYyj = "";
			var serviceAddonContentXyj = "";
			var serviceAddonContentJzbj = "";
			$$.each(serviceAddons, function(i, item) {
				var html = "";
				var name = item.name;
				var serviceAddonId = item.service_addon_id;
				var html = "";
				
				//只能写死，针对不同的服务大类
				//冰箱
				if (serviceAddonId == 82 ||  serviceAddonId == 83 || serviceAddonId == 84 || serviceAddonId == 85 ) {
					serviceAddonContentBx+= getSerivceAddonContentHtml(item);
				}
				
				//空调
				if (serviceAddonId == 79 ||  serviceAddonId == 80 || serviceAddonId == 81) {
					serviceAddonContentKt+= getSerivceAddonContentHtml(item);
				}
				
				//油烟机
				if (serviceAddonId == 75 ||  serviceAddonId == 76 || serviceAddonId == 77 || serviceAddonId == 78) {
					serviceAddonContentYyj+= getSerivceAddonContentHtml(item);
				}
				
				//洗衣机
				if (serviceAddonId == 72 ||  serviceAddonId == 73 || serviceAddonId == 74) {
					serviceAddonContentXyj+= getSerivceAddonContentHtml(item);
				}
				
				//家政保洁
				if (serviceAddonId == 69 ||  serviceAddonId == 70 || serviceAddonId == 71) {
					serviceAddonContentJzbj+= getSerivceAddonContentHtml(item);
				}
				
				if(serviceTypeId==84){
					$$("#appliance4").css({"display":"none"});
					
					switch(serviceAddonId){
						case 88:
						case 89:serviceAddonContentXyj+= getSerivceAddonContentHtml(item);break;
						case 90:
						case 91:
						case 92:
						case 93:serviceAddonContentYyj+= getSerivceAddonContentHtml(item);break;
						case 94:
						case 95:
						case 96:serviceAddonContentKt+= getSerivceAddonContentHtml(item);break;
						case 97:
						case 98:
						case 99:
						case 100:serviceAddonContentBx+= getSerivceAddonContentHtml(item);break;
					}
				}
				
			});
			
			$$("#service-addon-content-bx").append(serviceAddonContentBx);
			$$("#service-addon-content-kt").append(serviceAddonContentKt);
			$$("#service-addon-content-yyj").append(serviceAddonContentYyj);
			$$("#service-addon-content-xyj").append(serviceAddonContentXyj);
			$$("#service-addon-content-jzbj").append(serviceAddonContentJzbj);
			
		}
	});
	
	// 地址选择处理，1. 是否有默认地址， 2. 是否有选择的地址（最优先）
	var addrId = getItemAddrId();
	var addrName = getItemAddrName();
	
	if (addrId != undefined && addrId != "") {
		$$("#addrId").val(addrId);
	}
		
	if (addrName != undefined || addrName != "") {
		$$("#orderChooseAddrName").html(addrName);
	}
	
	 $$("#chooseServiceTime").on("click",function() {
		 
		

			var hasDefaultNum = false;
			 //如果为家政保洁类， 擦玻璃 = 69 ， 金牌保洁 = 70， 基础保洁 = 71 则需要一项服务接口
			var validateSelectTwoItemNum = 0;
			//如果不为家政保洁，则必须为数值为2以上才能验证通过
			var validateSelectOneItemNum = 0;
			var validateMsg = ""
			var validateSuccess = true;
			$$("input[name = itemNum]").each(function(key, index) {
				itemNum = $$(this).val();
				var serviceAddonId = $$(this).parent().find('input[name=serviceAddonId]').val();
				
				console.log("serviceAddonId == " + serviceAddonId);
				 if (itemNum == undefined || itemNum == "") {
					 itemNum = 0;
				 }
				 
				 if (Number(itemNum) > 0) {
					 
					 if (serviceAddonId == 69 ||  serviceAddonId == 70 || serviceAddonId == 71) {
						 validateSelectOneItemNum = parseFloat(validateSelectOneItemNum) + parseFloat(itemNum);
					 } else {
						 validateSelectTwoItemNum = parseFloat(validateSelectTwoItemNum) + parseFloat(itemNum);
					 }
					 
//					 selectedItemNum = parseFloat(selectedItemNum) + parseFloat(itemNum);
					 
					 var serviceAddonName = $$(this).parent().find('input[name=serviceAddonName]').val();
					 var defaultNum = $$(this).parent().find('input[name=defaultNum]').val();
					 
					 if (Number(defaultNum) > 0) {
						 if (Number(defaultNum) > Number(itemNum)) {
							 validateMsg = serviceAddonName + "需" + defaultNum + "平米起订.";
							 validateSuccess = false;
							 myApp.alert(validateMsg);
							 return false;
						 }
					 }
					 
				 }
			});
			
			if (validateSuccess == false) return false;
			console.log("validateSelectOneItemNum = " + validateSelectOneItemNum);
			console.log("validateSelectTwoItemNum = " + validateSelectTwoItemNum);
			if (validateSelectOneItemNum == 0 && validateSelectTwoItemNum == 0) {
				validateMsg = "需两项服务起订";
				myApp.alert(validateMsg);
				return false;
			} else if (validateSelectOneItemNum == 0 && validateSelectTwoItemNum > 0) {
				if (parseFloat(validateSelectTwoItemNum) < parseFloat(2) ) {
					validateMsg = "需两项服务起订";
					myApp.alert(validateMsg);
					return false;
				}
			}
		 var validateMsg = setCustomTotal();
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
		
		 var url = "order/order-lib-cal.html?next_url=order/order-custom-confirm.html"
			 
	     var staffId = sessionStorage.getItem("staff_id");
		 if (staffId != undefined && staffId != "" && staffId != null) {
			 url+="?staff_id="+staffId;
		 }	 
		
		 mainView.router.loadPage(url);
	 });
	
});


function getSerivceAddonContentHtml(item) {
	var html = "<ul class=\"appliance3\">";
	
	html+= "<li>" + item.name + "</li>"
	html+= "<li>" + item.aprice + "</li>"
	
	html+= "<li>";
	html+= '<input type="hidden" name="serviceAddonName" value="'+name+'" autocomplete="off">';
	
	html+= '<input type="hidden" name="defaultNum" value="'+item.default_num+'" autocomplete="off">';
	html+= '<input type="hidden" name="serviceAddonServiceHour" value="'+item.service_hour+'" autocomplete="off">';
	html+= '<input type="hidden" name="serviceAddonItemUnit" value="'+item.item_unit+'" autocomplete="off">';
	html+= '<input type="hidden" name="serviceAddonAprice" value="'+item.aprice+'" autocomplete="off">';
	html+= '<input type="hidden" name="serviceAddonId" value="'+item.service_addon_id+'" autocomplete="off">';
	
	html+= '<div class="appliance3-1" onclick="onCustomAddItemNum($$(this).parent())">＋</div>';
	html+= '<input name="itemNum" value="0" onkeyup="onItemNumKeyUp($$(this).parent())"  onafterpaste="onItemNumKeyUp($$(this).parent())"  maxLength="3" autocomplete="off">';
	html+= '<div class="appliance3-1" onclick="onCustomSubItemNum($$(this).parent())">一</div>';
	
	html+= '</li>';
	html+= '</ul>';
	return html;
}

function onItemNumKeyUp(obj) {
	var itemNumObj = obj.find('input[name=itemNum]'); 
	
	var tmptxt=itemNumObj.val();
	tmptxt = tmptxt.replace(/\D/g,'');
	itemNumObj.val(tmptxt);
	
	setCustomTotal();
}

//加号处理
function onCustomAddItemNum(obj) {
	var itemNumObj = obj.find('input[name=itemNum]'); 
	var v = itemNumObj.val();
	if (v == undefined || !isNum(v)) {
		v = 0;
	} else {
		v = parseInt(v) + 1;
	}

	itemNumObj.val(v);
	
	var validateMsg = setCustomTotal();

}

//减号处理
function onCustomSubItemNum(obj) {
	console.log("onCustomSubItemNum");
	var itemNumObj = obj.find('input[name=itemNum]'); 
	var v = itemNumObj.val();
	if (v == undefined || !isNum(v) || v == 0) {
		v = 0;
	} else {
		v = parseInt(v) - 1;
	}
	
	itemNumObj.val(v);
	
	var validateMsg = setCustomTotal();

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
function setCustomTotal() {

	var orderMoney = itemPrice = Number(0);
	var orderOriginMoney = Number(0);
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
		 
		 var aprice = $$(this).parent().find('input[name=serviceAddonAprice]').val();
		 if (aprice == undefined || aprice == "") return false;
		 console.log("aprice ==" + aprice);
		 var reg = /[1-9][0-9]*/g;
		 itemPrice = aprice.match(reg);

		 orderMoney = Number(orderMoney) + Number(itemNum) * Number(itemPrice);
		 orderOriginMoney = Number(orderOriginMoney) + Number(itemNum) * Number(itemPrice);
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
	sessionStorage.setItem("order_origin_money", orderOriginMoney);
	sessionStorage.setItem("order_origin_pay", orderOriginMoney);
	sessionStorage.setItem("total_service_hour", totalServiceHour);
	sessionStorage.setItem("service_addons", JSON.stringify(serviceAddons));
	sessionStorage.setItem("service_addons_json", JSON.stringify(serviceAddonsJson));
	
	return "";
}
