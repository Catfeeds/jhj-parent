myApp.onPageInit('jinpaibaojie-page', function(page) {

	// 根服务类型Id
	var parentServiceTypeId = page.query.firstServiceType;

	$$("#parentServiceType").val(parentServiceTypeId);

	$$(document).on(
			"click",
			"#jinpaibaojiediv",
			function() {

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

				var parentServiceTypeId = 23;

				var serviceProperty = $$(this).find("input[name='serviceProperty']").val();

				var url = "order/order-form-zhongdiangong.html?serviceType=" + serviceTypeId
						+ "&parentServiceTypeId=" + 23;

				if (serviceProperty == 1) {
					// 全年订制
					localStorage.setItem('service_type_id', serviceTypeId);
					url = "order/fiveservice/QuanNianDingZhi.html?serviceType=" + serviceTypeId;

				}

				mainView.router.loadPage(url);
			});
	
	// 2. 点击banner 图进入服务介绍
	$$("#jinpaibaojie-banner").on('click',function(){
		
		if(parentServiceTypeId == 23){
			mainView.router.loadPage("order/service-introduce/service-jinpai.html");
		}
		
	});
	
	
});

// 列表显示
myApp.template7Data['page:jinpaibaojie-page'] = function() {
	var result;
	var parentServiceTypeId = 23;

	$$.ajax({
		type : "GET",
		url : siteAPIPath + "newPartServiceType/second_service_type.json?service_type_id="
				+ parentServiceTypeId,
		dataType : "json",
		cache : true,
		async : false,
		success : function(data) {
			// console.log(data);
			result = data;

		}
	})

	return result;
}
