myApp.onPageBeforeInit('mine-charge-pay-success', function(page) {
	
	var userId = localStorage.getItem("user_id");
//	var paycardId=localStorage.getItem("pay_card_id");
	var userInfoSuccess = function(data, textStatus, jqXHR) {
	  	myApp.hideIndicator();
		var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(data.msg);
			return;
		}
		var userInfo = result.data; 
//		$$("#rest_money").text(userInfo.rest_money);

		localStorage.setItem("is_vip",userInfo.is_vip);
		
	};
	
	//获取用户充值信息接口
    $$.ajax({
        type:"get",
        url:siteAPIPath+"user/getUserRestMoneyInfo.json?user_id="+userId,
        dataType:"json",
        cache:false,
        statusCode : {
			200 : userInfoSuccess,
			400 : ajaxError,
			500 : ajaxError
		}
    });
//    
//    $$('#charge-pay-success-btn').click(function(){
//    	mainView.router.loadPage("user/mine.html");
//    });
//	
});