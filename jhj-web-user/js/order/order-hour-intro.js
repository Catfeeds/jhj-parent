myApp.onPageBeforeInit('order-hour-intro', function(page) {
	
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
		$$.get("order/hourIntro/order-hour-intro-" + serviceTypeId + ".html", function(data) {
			$$("#hourServiceTypeIntroDiv").html(data);
		});
	}
	
	switch (parseInt(serviceTypeId)) {
		case 28:
		case 70:
			getIntroData(serviceTypeId);
			//img/jinpaibaojie/order-jinpai1.png
			$$("#order-hour-banner-img").attr("src", "img/newYear/9.png");
			break;
		case 68:
			getIntroData(serviceTypeId);
			$$("#order-hour-banner-img").attr("src", "img/jinpaibaojie/order-jinpai2.png");
			break;
		case 69:
			getIntroData(serviceTypeId);
			$$("#order-hour-banner-img").attr("src", "img/jinpaibaojie/order-jinpai3.png");
			break;
	}
	
	$$("#order-hour-click").on("click", function() {
		var url = "order/order-hour-choose.html?service_type_id=" + serviceTypeId;
		mainView.router.loadPage(url);
	});
	
});
