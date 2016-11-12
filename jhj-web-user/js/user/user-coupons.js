myApp.onPageInit('mine-coupons-list', function (page) {

	var userId = localStorage.getItem("user_id");
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}

	var backUrl = page.query.back_url;
	var serviceTypeId = sessionStorage.getItem('service_type_id');
	
	//var orderMoney = sessionStorage.getItem('order_money');

	//处理订单调整到当前页面选择优惠劵的
	if(serviceTypeId!=null && serviceTypeId!='' && serviceTypeId!=undefined){
		$$(document).on('click', '#mine-coupons-use', function (e) {

			e.stopImmediatePropagation(); //防止重复点击

			if (backUrl == "") return;

			var couponOrderType =  $$(this).find(".service_type").val();
	//		var maxValue = $$(this).find("input[name=max_value]").val();
			var userCouponId = $$(this).find("#user_coupon_id").val();
			var userCouponValue = $$(this).find("#user_coupon_value").val();
	//		var useCondition=$$(this).find("#coupons_type_id").val();
			var userCouponName = "￥" + userCouponValue;

	//		var serviceDate = sessionStorage.getItem("service_date")*1000;
	//		var week=moment(serviceDate).format('d');
	//		if(useCondition==2){
	//			if(week>3 || week<0){
	//				myApp.alert("限周一到周三使用！");
	//				return false;
	//			}
	//		}
			//判断当前选择优惠劵是否适用

			if (couponOrderType.indexOf(serviceTypeId) < 0) {
				myApp.alert("当前优惠劵不适用!");
				return false;
			}

	//		if (orderMoney != undefined && useCondition==3) {
	//			if (parseFloat(orderMoney) < parseFloat(maxValue)) {
	//				myApp.alert("消费满"+ maxValue + "可用!");
	//				return false;
	//			}
	//		}


			sessionStorage.setItem("user_coupon_id", userCouponId);
			sessionStorage.setItem("user_coupon_name", userCouponName);
			sessionStorage.setItem("user_coupon_value", userCouponValue);

			mainView.router.loadPage(backUrl);

		});
	}
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