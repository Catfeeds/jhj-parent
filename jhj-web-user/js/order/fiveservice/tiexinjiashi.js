//列表显示
myApp.template7Data['page:tiexinjiashi-page'] = function() {
	var result;
	
	var parentServiceTypeId = localStorage['firstServiceType'];
	
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "newPartServiceType/second_service_type.json",
		data:{
			"service_type_id":parentServiceTypeId
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


myApp.onPageInit('tiexinjiashi-page', function(page) {

	// 根服务类型Id
	var parentServiceTypeId = localStorage['firstServiceType'];
	
	$$("#parentServiceTypeId").val(parentServiceTypeId);
	
	
	// 1.  点击 到 下单页面
	$$("div[name='tiexinjiashidiv']").on("click",function() {

		var userId = localStorage['user_id'];

		if (userId == undefined || userId.length == 0) {

			myApp.alert("您还没有登录");

			mainView.router.loadPage("login.html");
			return false;
		}

		var enable = $$(this).find("input[name='enable']").val();

		if (enable == 0) {
			return false;
		}

		var serviceTypeId = $$(this).find("input[name='serviceTypeId']").val();
		
		
		//在 助理 服务下单页面。使用模板。此处需要存储url 参数值
		localStorage.setItem("am_faqiyuyue_service_type_id",serviceTypeId);
		localStorage.setItem("am_faqiyuyue_parent_service_type_id",localStorage['firstServiceType']);
		
//		mainView.router.loadPage("order/order-am-faqiyuyue.html?serviceType="+serviceTypeId
//				+ "&parentServiceTypeId=" + localStorage['firstServiceType']);
				
		mainView.router.loadPage("order/order-am-faqiyuyue.html");
	});
	
	//2. 点击banner图进入 服务说明
	$$("#tiexinjiashi-banner").on('click',function(){
		
		if(parentServiceTypeId == 25){
			mainView.router.loadPage("order/service-introduce/service-tiexin.html");
		}
		
		if(parentServiceTypeId == 26){
			mainView.router.loadPage("order/service-introduce/service-shendu.html");
		}
		
	})
	
});



