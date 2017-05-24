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
	    	$$("#ppriceStr").html(serviceType.pprice + "元/" + serviceType.service_hour + "小时");
	    	$$("#mppriceStr").html(serviceType.mpprice + "元/" + serviceType.service_hour + "小时");
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
			}
		}
	});
	
	
	$$("#order-hour-click").on("click", function() {
		var url = "order/order-hour-choose.html?service_type_id=" + serviceTypeId;
		mainView.router.loadPage(url);
	});
	
});
