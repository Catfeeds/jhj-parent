myApp.onPageInit('order-am-faqiyuyue-page', function(page) {
	
	
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



////列表显示
myApp.template7Data['page:order-am-faqiyuyue-page'] = function() {
	var result;
//	var parentServiceTypeId = 24;
	
	// 根服务类型Id
	var serviceTypeId =  localStorage['am_faqiyuyue_service_type_id'];
	
	if(serviceTypeId != undefined){
		$$("#serviceType").val(serviceTypeId);
	}
	
	var parentServiceTypeId = localStorage['am_faqiyuyue_parent_service_type_id'];
	
	if(parentServiceTypeId != undefined){
		$$("#parentServiceTypeId").val(parentServiceTypeId);
	}
	
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "newPartServiceType/service_type_detail.json",
		data: {
			"service_type_id":serviceTypeId
		},
		dataType : "json",
		cache : true,
		async : false,
		success : function(data) {
			result = data;
		}
	})

	return result;
}





