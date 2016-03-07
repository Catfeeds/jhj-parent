myApp.onPageInit('history-user-page',function(){
	
//	var user_id = localStorage['user_id'];
//	
//	$$("#userId").val(user_id);
	
	$$("#remindServiceDate").val(moment().format('YYYY-MM-DD  HH:mm'));
	
	remindDateSelct();
	
	$$("#historyUserNext").click(function(){
		
		
		var formData = myApp.formToJSON('#history-user-form');
		
		formData.serviceDate =  moment(formData.serviceDate + ":00", "yyyy-MM-DD HH:mm:ss").unix();
		
		
		$$.ajax({
			type: "get",
			 url: siteAPIPath + "order/history_user.json",
			data: formData,
			success: function (data, status, xhr){
				
				var result = JSON.parse(data);												
				
				localStorage.setItem("history_user_id", result.data.id);
				
				localStorage.setItem("history_am_id",result.data.am_id);
				
				
				
				var successUrl = "order/history/history-order-type.html";
				mainView.router.loadPage(successUrl);
			},
			error:function(){
				myApp.alert("网络异常,请稍后重试");
			}
		});
	});
	
});

