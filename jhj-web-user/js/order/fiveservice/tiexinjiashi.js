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
		mainView.router.loadPage("order/order-am-faqiyuyue.html?serviceType="+serviceTypeId
				+ "&parentServiceTypeId=" + parentServiceTypeId);
				
	});
});



