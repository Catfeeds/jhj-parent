myApp.onPageInit('order-jingpai-page',function(){
	
	$$("#historyOrderPut").on('click',function(){
		
		var userMobile = localStorage['user_mobile'];

		if(userMobile === '13521193653'){
			mainView.router.loadPage("order/history/history-user.html");
		}else{
			return false;
		}
		
		
	});
});

