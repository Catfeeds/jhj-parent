myApp.onPageInit('mine-coupon-list-page', function (page) {
		
	var userId = localStorage.getItem("user_id");
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	localStorage['u_order_money_param'] = page.query.order_money;
	localStorage['u_order_type_param'] = page.query.order_type;
	//处理订单调整到当前页面选择优惠劵的
	$$(document).on('click', '#selectCouponLink', function (e) {

		e.stopImmediatePropagation(); //防止重复点击

		var returnPage = ""
		for (var i =1; i < 5; i++) {
			var historyPage = mainView.history[mainView.history.length-i];
			console.log(historyPage);
			if (historyPage == undefined) continue;
			
			if (historyPage.indexOf("order-view-0") >= 0 || 
				historyPage.indexOf("order-view-1") >= 0 ||
				historyPage.indexOf("order-view-2") >= 0 ||
				historyPage.indexOf("order-form-zhongdiangong-pay") >= 0 ||
				historyPage.indexOf("order-service-charge") >= 0) {
				
				//解决连续多次选择优惠券页面。。url参数重复累积的问题
				if(historyPage.indexOf('?')>0){
					returnPage = historyPage.substring(0,historyPage.indexOf('?'));
				}else{
					returnPage = historyPage;
				}
				
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
		
    	returnPage = replaceParamVal(returnPage, "user_coupon_id", userCouponId);
    	returnPage = replaceParamVal(returnPage, "user_coupon_name", userCouponName);
    	returnPage = replaceParamVal(returnPage, "user_coupon_value", userCouponValue);
    	
    	if(returnPage.indexOf('?')>0){
    		returnPage+= "&user_coupon_id="+userCouponId;
    	}else{
    		returnPage+= "?user_coupon_id="+userCouponId;
    	}
    	returnPage+= "&user_coupon_name="+ userCouponName;
    	returnPage+= "&user_coupon_value="+ userCouponValue;
    	
    	mainView.router.loadPage(returnPage);		
		
	});
	
	
	
     $$("#user-coupon-exchange-button").on("click",function(){
    	 exchangeUserCoupon(userId);
 	});
});

//列表显示用户兑换码
myApp.template7Data['page:mine-coupon-list-page'] = function(){
  var result;
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
var onExchangeSuccess = function(data, textStatus, jqXHR){
	myApp.hideIndicator();
   	var result = JSON.parse(data.response);
	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
	  var userAddr = result.data;
	  mainView.router.loadPage("user/coupon/mine-coupon-list.html?user_id="+1);
	}
//兑换优惠券function
function exchangeUserCoupon(userId){
	
	var cardPasswd = $$("#cardPasswdItem").val();
	if(cardPasswd =='' || cardPasswd.length <0){
		myApp.alert("兑换码不能为空");
		return;
	}
    $$.ajax({
        type:"POST",
        url:siteAPIPath+"user/post_coupon.json",
        data:{"user_id":userId,
        	  "card_passwd":cardPasswd
        	  },
        dataType:"json",
        cache:false,
        statusCode: {
         	200: onExchangeSuccess,
 	    	400: ajaxError,
 	    	500: ajaxError
 	    },
    });
}