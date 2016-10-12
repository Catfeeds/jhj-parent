myApp.onPageInit('mine-coupons-list', function (page) {
		
	var userId = localStorage.getItem("user_id");
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	localStorage['u_order_money_param'] = page.query.order_money;
	localStorage['u_order_type_param'] = page.query.order_type;
	//处理订单调整到当前页面选择优惠劵的
	$$(document).on('click', '#mine-coupons-use', function (e) {

		e.stopImmediatePropagation(); //防止重复点击

		var returnPage = ""
		for (var i =1; i < 5; i++) {
			var historyPage = mainView.history[mainView.history.length-i];
			console.log(historyPage);
			if (historyPage == undefined) continue;
			
			if (historyPage.indexOf("order-hour-confirm") >= 0 || 
				historyPage.indexOf("order-view-1") >= 0 ||
				historyPage.indexOf("order-view-2") >= 0 ||
				historyPage.indexOf("order-form-zhongdiangong-pay") >= 0 ||
				historyPage.indexOf("order-service-charge") >= 0) {
				
				returnPage = historyPage;
				
				break;
			}
		}
		
		if (returnPage == "") return;
		
		//		page.query ,有时不好用
		var orderTypeParam = localStorage['u_order_type_param'];

		var orderMoney = localStorage['u_order_money_param'];
		//做优惠劵的判断.
		var serviceType = $$(this).find("input[name=service_type]").val();
		var maxValue = $$(this).find("input[name=max_value]").val();
		var userCouponId = $$(this).find("#user_coupon_id").val();
		var userCouponName = $$(this).find("#introduction").html();
		var userCouponValue = $$(this).find("#user_coupon_value").val();
		var useCondition=$$(this).find("#coupons_type_id").val();
		
		var service_date = localStorage.getItem("service_date");
		var _week=moment(service_date).format('d');
		if(useCondition==2){
			if(_week>3 || _week<0){
				myApp.alert("限周一到周三使用！");
				return false;
			}
		}
		//判断当前选择优惠劵是否适用
		if (serviceType.indexOf(orderTypeParam) < 0) {
			myApp.alert("当前优惠劵不适用!");
			return false;
		}
		
		if (orderMoney != undefined && useCondition==3) {
			if (parseFloat(orderMoney) < parseFloat(maxValue)) {
				myApp.alert("消费满"+ maxValue + "可用!");
				return false;				
			}
		}
		
		
		sessionStorage.setItem("user_coupon_id", userCouponId);
		sessionStorage.setItem("user_coupon_name", userCouponName);
		sessionStorage.setItem("user_coupon_value", userCouponValue);
    	
    	mainView.router.loadPage(returnPage);		
		
	});
});

//列获取优惠券列表
myApp.template7Data['page:mine-coupons-list'] = function(){
  var result="";
  var userId = localStorage.getItem("user_id");
  $$.ajax({
          type : "GET",
          url  : siteAPIPath+"user/get_coupon.json?user_id="+userId,
          dataType: "json",
          cache : true,
          async : false,
          success: function(data){
              result = data;
          }
  })
  return result;
}