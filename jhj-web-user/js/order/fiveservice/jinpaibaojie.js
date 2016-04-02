myApp.onPageInit('jinpaibaojie-page', function(page) {
	
	// 根服务类型Id
	var parentServiceTypeId =  page.query.firstServiceType;
	
	if(parentServiceTypeId == 24){
		$("#topImg").attr("src","img/icon-fiveservice/chuniangshaofan-top.png");
	}
	
	if(parentServiceTypeId == 23){
		$("#topImg").attr("src","img/icon-fiveservice/jinpaibaojie-top.png");
	}
	
	
	$$("#parentServiceType").val(parentServiceTypeId);
	
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
				
				htmlPart = htmlPart.replace(new RegExp('{name}',"gm"), service.name);
				htmlPart = htmlPart.replace(new RegExp('{remarks}',"gm"), service.remarks);
				
				if(service.service_property == 0){
					//单品
					htmlPart = htmlPart.replace(new RegExp('{priceAndUnit}',"gm"), 
								"<font color='orange'>"+service.price+service.unit+"</font>");
				}else{
					
					var times =  service.service_times;
					
					var weekNum = "";
					
					if(times < 1){
						//取整，舍弃小数
						weekNum = parseInt(1/times);
					}
					// 全年订制
					htmlPart = htmlPart.replace(new RegExp('{priceAndUnit}',"gm"), "全年订制:每"+weekNum+"周"+times+"次");
				}
				
				htmlPart = htmlPart.replace(new RegExp('{serviceTypeId}',"gm"), service.service_type_id);
				
				htmlPart = htmlPart.replace(new RegExp('{enableNum}',"gm"), service.enable);
				
				htmlPart = htmlPart.replace(new RegExp('{serviceProperty}',"gm"), service.service_property);
				
				
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

	
	$$(document).on("click","#jinpaibaojiediv",function(){
		
		
		var userId =  localStorage['user_id'];
		
		if(userId == undefined || userId.length == 0){
			
			myApp.alert("您还没有登录");
			
			mainView.router.loadPage("login.html");
			return false;
		}
		
		var enable =  $$(this).find("input[name='enable']").val();
		
		if(enable == 0){
			return false;
		}
		
		var serviceTypeId =  $$(this).find("input[name='serviceTypeId']").val();
		
		
		var parentServiceTypeId =  $$("#parentServiceType").val();
		
		var serviceProperty = $$(this).find("input[name='serviceProperty']").val();
		
		
		var url = "order/order-form-zhongdiangong.html?serviceType="+serviceTypeId
						+"&parentServiceTypeId="+parentServiceTypeId;
		
		if(serviceProperty == 1){
			// 全年订制
			url = "order/fiveservice/QuanNianDingZhi.html?serviceType="+serviceTypeId;	
			
		}
		
		mainView.router.loadPage(url);
	});
});







