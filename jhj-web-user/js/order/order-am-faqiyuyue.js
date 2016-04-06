myApp.onPageInit('order-am-faqiyuyue-page', function(page) {
	
	// 根服务类型Id
	var serviceTypeId =  page.query.serviceType;
	
	if(serviceTypeId != undefined){
		$$("#serviceType").val(serviceTypeId);
	}
	
	var parentServiceTypeId = page.query.parentServiceTypeId;
	
	if(parentServiceTypeId != undefined){
		$$("#parentServiceTypeId").val(parentServiceTypeId);
	}
	
	
	$$.ajax({
		type: "get",
		 url: siteAPIPath + "newPartServiceType/service_type_detail.json",
		data: {
			"service_type_id":serviceTypeId
		},
		success: function (data, status, xhr){
			
			var result = JSON.parse(data);
			
			var service = result.data;
			
			$$("#servceTypeName").text(service.name);
			
			$$("#servicePrice").text(service.remarks);
			
			$$("#serviceContents").text(service.service_content);
			
		},
		error: function(status,xhr){
		  	myApp.alert("网络异常,请稍后再试.");
		}
	});

	
	$$("#order-am-faqiyuyue-page-a-submit").on("click",function(){
		
		var serviceType = $$("#serviceType").val();
		
		var content =  $$("#serviceContentText").val();
		
		var parentServiceType = $$("#parentServiceTypeId").val();
		
		// 深度养护 不需要必填 服务要求
		if(parentServiceType != 26){
			
			if(content == undefined || content == ""){
				myApp.alert("请您简单描述一下服务要求");
				return false;
			}
		}
		
		var userId = localStorage['user_id'];
		
	    $$.ajax({
	        type:"POST",
	        url:siteAPIPath+"order/post_user.json",
	        data:{"user_id":userId,
	        	  "service_type":serviceType,
	              "service_content":content,
	              "order_from":1
	      },
	      success: function (data, status, xhr){
				
				myApp.alert("预约成功");
				mainView.router.loadPage("index.html");
			}
	       
	    });
		
	});
});

