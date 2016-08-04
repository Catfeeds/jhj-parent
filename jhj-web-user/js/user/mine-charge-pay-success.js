myApp.onPageBeforeInit('charge-pay-success-page', function(page) {
	
	var userId = localStorage.getItem("user_id");
	var cardPay=localStorage.getItem("card_pay");
	var sendMoney=localStorage.getItem("send_money")

	var userInfoSuccess = function(data, textStatus, jqXHR) {
	  	myApp.hideIndicator();
		var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(data.msg);
			return;
		}
		var userInfo = result.data; 
		$$("#rest_money").text(userInfo.rest_money);
	};
	
	//获取用户充值信息接口
    $$.ajax({
        type:"get",
        url:siteAPIPath+"user/getUserRestMoneyInfo.json?user_id="+userId+"&card_pay="+cardPay+"&send_money="+sendMoney,
        dataType:"json",
        cache:false,
        statusCode : {
			200 : userInfoSuccess,
			400 : ajaxError,
			500 : ajaxError
		}
    });
    
    $$('#charge-pay-success-btn').click(function(){
    	mainView.router.loadPage("user/mine.html");
    });
	
});