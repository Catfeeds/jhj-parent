myApp.onPageInit('mine-coupons-list', function (page) {

	var userId = localStorage.getItem("user_id");
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	var backUrl = page.query.back_url;
	var serviceTypeId = sessionStorage.getItem('service_type_id');
	var orderMoney = sessionStorage.getItem('order_money');
	var serviceDate = sessionStorage.getItem("service_date")*1000;
	
	$$.ajax({
		type : "GET",
		url  : siteAPIPath+"user/get_coupon.json?user_id="+userId,
		dataType: "json",
		cache : true,
		async : false,
		success: function(data){
			var html="";
			var tempHtml=$$("#mine-coupons-use-temp").html();
			
			var result = data.data;
			for(var i=0;i<result.length;i++){
				var temp=tempHtml;
				var coupons = result[i];
				temp=temp.replace(new RegExp('{value}','gm'),coupons.value);
				if(backUrl!=null && backUrl!=''){
					if(coupons.service_type==serviceTypeId || coupons.service_type==0 && parseFloat(coupons.max_value) <= parseFloat(orderMoney) && serviceDate>=coupons.from_date && serviceDate<=coupons.to_date){
						temp=temp.replace(new RegExp('{status}','gm'),"可用");
					}else{
						temp=temp.replace(new RegExp('{status}','gm'),"不可用");
					}
				}else{
					temp=temp.replace(new RegExp('{status}','gm'),"");
				}
				if(coupons.service_type==0){
					temp=temp.replace(new RegExp('{service_type_name}','gm'),coupons.service_type_name);
				}else{
					temp=temp.replace(new RegExp('{service_type_name}','gm'),"仅限于："+coupons.service_type_name);
				}
				if(parseFloat(coupons.max_value)>0){
					temp=temp.replace(new RegExp('{maxvalue}','gm'),"满"+coupons.max_value+"元可用");
				}else{
					temp=temp.replace(new RegExp('{maxvalue}','gm'),"");
				}
				
				temp=temp.replace(new RegExp('{from_date_str}','gm'),coupons.from_date_str);
				temp=temp.replace(new RegExp('{to_date_str}','gm'),coupons.to_date_str);
				temp=temp.replace(new RegExp('{id}','gm'),coupons.id);
				temp=temp.replace(new RegExp('{service_type}','gm'),coupons.service_type);
				temp=temp.replace(new RegExp('{value}','gm'),coupons.value);
				temp=temp.replace(new RegExp('{from_date}','gm'),coupons.from_date);
				temp=temp.replace(new RegExp('{to_date}','gm'),coupons.to_date);
				temp=temp.replace(new RegExp('{max_value}','gm'),coupons.max_value);
				html+=temp;
			}
			$$("#coupons-list").html(html);
		}
	});
	
	


	//处理订单调整到当前页面选择优惠劵的
	if(serviceTypeId!=null && serviceTypeId!='' && serviceTypeId!=undefined){
		$$(document).on('click', '#mine-coupons-use', function (e) {
			
			e.stopImmediatePropagation(); //防止重复点击

			if (backUrl == "") return;
			
			var couponOrderType =  $$(this).find(".service_type").val();
			var userCouponId = $$(this).find("#user_coupon_id").val();
			var userCouponValue = $$(this).find("#user_coupon_value").val();
			var maxVlaue = $$(this).find("#max_value").val();
			console.log("maxVlaue="+maxVlaue);
			var userCouponName = "￥" + userCouponValue;
			
			var fromDate = $$(this).find("#from_date").val();
			var toDate = $$(this).find("#to_date").val();
//			var serviceDate = sessionStorage.getItem("service_date")*1000;
//			var couponsTypeId = $$(this).find("#coupons_type_id").val();
			
			//判断优惠券的使用类型
			if(couponOrderType>0){
				if (couponOrderType.indexOf(serviceTypeId) < 0) {
					myApp.alert("当前优惠劵不适用!");
					return false;
				}
			}
			
			
			if(parseFloat(sessionStorage.getItem('order_money')) < parseFloat(maxVlaue)){
				myApp.alert("当前优惠劵不适用!");
				return false;
			}

			
			//判断有效期
			if(serviceDate<fromDate || serviceDate>toDate){
				myApp.alert("当前优惠劵不适用!");
				return false;
			}

			sessionStorage.setItem("user_coupon_id", userCouponId);
			sessionStorage.setItem("user_coupon_name", userCouponName);
			sessionStorage.setItem("user_coupon_value", userCouponValue);
			
			mainView.router.loadPage(backUrl);

		});
	}
});

