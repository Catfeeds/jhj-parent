myApp.onPageInit('order-am-faqiyuyue-page', function(page) {
	
	// 根服务类型Id
	var parentServiceTypeId =  page.query.serviceType;
	
	if(parentServiceTypeId != undefined){
		$$("#serviceType").val(parentServiceTypeId);
	}
	
	
	$$.ajax({
		type: "get",
		 url: siteAPIPath + "newPartServiceType/service_type_detail.json",
		data: {
			"service_type_id":parentServiceTypeId
		},
		success: function (data, status, xhr){
			
			var result = JSON.parse(data);
			
			var service = result.data;
			
			$$("#servce_type_name").text(service.name);
			
			$$("#servicePrice").text(service.price+"/"+service.unit);
			
			$$("#serviceIntro").text(service.remarks);
			
		},
		error: function(status,xhr){
		  	myApp.alert("网络异常,请稍后再试.");
		}
	});

	
	$$("#order-am-faqiyuyue").on("click",function(){
		
		var content =  $$("#serviceContent").val();
		
		if(content == undefined || content == ""){
			myApp.alert("请您简单描述一下服务要求");
			return false;
		}
		
		var userId = localStorage['user_id'];
		
		var serviceType = $$("#serviceType").val();
		
	    $$.ajax({
	        type:"POST",
	        url:siteAPIPath+"order/post_user.json",
	        dataType:"json",
	        cache:false,
	        data:{"user_id":userId,
	        	  "service_type":serviceType,
	              "service_content":content,
	              "order_from":1
	      },
	      success: function (data, status, xhr){
				
//				var result = JSON.parse(data);
//				var service = result.data;
				myApp.alert("预约成功");
				mainView.router.loadPage("index.html")
			},
			error: function(status,xhr){
			  	myApp.alert("网络异常,请稍后再试.");
			}
	       
	    });
		
	});
});

