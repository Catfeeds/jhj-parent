myApp.onPageBeforeInit('order-coupons-use', function (page) {
	
	//优惠券
	$$("#mine-coupons").on("click",function(){
		mainView.router.loadPage("user/user-coupons.html?user_id="+userId);
	});

	//关于我们
	$$("#mine-about-us").on("click",function(){
		mainView.router.loadPage("user/aboutus.html");
	});
	
	
});

//列表显示，获取用户的信息
myApp.template7Data['page:mine']=function(){
	var result="";
	var userId = localStorage.getItem("user_id");
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}

	$$.ajax({
		type : "GET",
		url : siteAPIPath + "user/get_userinfo.json?user_id="+userId,
		dataType : "json",
		cache : true,
		async : false,
		success : function(data) {
			result = data.data;

			localStorage.setItem("is_vip",result.is_vip);
			
			
		}
	})
	return result;
}


