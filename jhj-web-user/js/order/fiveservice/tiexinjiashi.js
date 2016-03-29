myApp.onPageInit('tiexinjiashi-page', function(page) {
	
	// 根服务类型Id
	var parentServiceTypeId =  page.query.firstServiceType;
	
	$$.ajax({
		type: "get",
		 url: siteAPIPath + "newPartServiceType/second_service_type.json",
		data: {
			"service_type_id":parentServiceTypeId
		},
		success: function (data, status, xhr){
			
			var result = JSON.parse(data);
			
			var list = result.data;
			
			var tempHtml = $$("#serviceTemplate").html();
			
			var resultHtm = "";
			
			for (var i = 0, j = list.length ; i < j; i ++) {
				
				var service = list[i];
				
				var htmlPart = tempHtml;
				
				htmlPart = htmlPart.replace(new RegExp('{imgUrl}',"gm"), service.service_img_url);
				htmlPart = htmlPart.replace(new RegExp('{name}',"gm"), service.name);
				htmlPart = htmlPart.replace(new RegExp('{serviceTypeId}',"gm"), service.service_type_id);
				
				htmlPart = htmlPart.replace(new RegExp('{enableNum}',"gm"), service.enable);
				var buttonWord = ""
				
				if(service.enable == 1){
					buttonWord = "立即预定";
				}else{
					buttonWord = "敬请期待";
				}
				
				htmlPart = htmlPart.replace(new RegExp('{enable}',"gm"), buttonWord);
				
				resultHtm += htmlPart;
				
			}
			
			$$("#serviceContainer").append(resultHtm);
			
	 },
	  error: function(status,xhr){
		  	myApp.alert("网络异常,请稍后再试.");
	 }
	});

	
	$$(document).on("click","#tiexinjiashidiv",function(){
		
		
		var userId =  localStorage['user_id'];
		
		if(userId == undefined || userId.length == 0){
			
			myApp.alert("您还没有登录");
			
			mainView.router.loadPage("login.html");
			return false;
		}
		
		var enable =  $$(this).next().val();
		
		if(enable == 0){
			return false;
		}
		
		var serviceTypeId =  $$(this).prev().val();
		
		mainView.router.loadPage("order/order-am-faqiyuyue.html?serviceType="+serviceTypeId);
	});
});


