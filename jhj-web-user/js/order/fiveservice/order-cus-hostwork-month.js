myApp.onPageBeforeInit('order-cus-hostwork-month', function(page) {
	
	removeSessionData();
	
	var serviceTypeId = page.query.service_type_id;
	
	sessionStorage.setItem("service_type_id", serviceTypeId);
	
	if (serviceTypeId == undefined || serviceTypeId == "" || serviceTypeId == null) return;
	
	// 获取服务子类信息
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "dict/get_service_type.json?service_type_id=" + serviceTypeId,
		dataType : "json",
		async : false,
		success : function(data) {
			var _data = data.data;
			var serviceType = data.data;
	    	console.log(serviceType);
	    	if (serviceType == undefined || serviceType == "") {
	    		return false;
	    	}
	    	$$("#serviceTypeNameStr").html(serviceType.name);
	    	$$("#ppriceStr").html(serviceType.pprice + "" + serviceType.unit);
	    	$$("#mppriceStr").html(serviceType.mpprice + "" + serviceType.unit);
	    	
	    	sessionStorage.setItem("periodOrderMoney",serviceType.pprice);
	    	sessionStorage.setItem("periodPayMoney",serviceType.mpprice);
	    	
	    	var periodOrder = {};
	    	periodOrder.periodServiceAddonId = 0;
	    	periodOrder.name = serviceType.name;
	    	periodOrder.serviceTypeId = serviceTypeId;
	    	periodOrder.serviceAddonId = 0;
	    	periodOrder.price = serviceType.pprice;
	    	periodOrder.vipPrice = serviceType.mpprice;
	    	periodOrder.num = 4;
	    	
	    	var periodOrderList = [];
	    	periodOrderList.push(periodOrder);
	    	
	    	sessionStorage.setItem("periodOrder",JSON.stringify(periodOrderList));
	    	
		}
	});
	
	
	function getIntroData(serviceTypeId) {
		$$.get("order/fiveservice/"+serviceTypeId + ".html", function(data) {
			$$("#hourServiceTypeIntroDiv").html(data);
		});
	}
	
	switch (parseInt(serviceTypeId)) {
		case 61:
			getIntroData(serviceTypeId);
			$$("#order-hour-banner-img").attr("src", "img/jiawubaoyue/baoyuebanner1.png");
			break;
	}
	
	var userId = localStorage.getItem("user_id");
	
	//验证用户是否可以下包月初体验
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "order/validate-pack-early-experience.json",
		data:{"user_id":userId,"service_type_id":serviceTypeId},
		dataType : "json",
		async : false,
		success : function(data) {
			var result = data.data;
			if(parseInt(result)>=1){
				$$("#order-hour-click").attr("disabled","disabled");
				myApp.alert("该服务只能体验一次");
			}
		}
	});
	
	
	$$("#order-hour-click").on("click", function() {
//		var url = "order/order-hour-choose.html?service_type_id=" + serviceTypeId;
		var url = "order/period/period-order-confirm.html?package_type_id=6";
		mainView.router.loadPage(url);
	});
	
});
