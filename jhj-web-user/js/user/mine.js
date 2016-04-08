myApp.onPageBeforeInit('mine', function (page) {
	var userId = localStorage.getItem("user_id");;
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
//	getUserInfos(userId);
	
	$$("#mine-order-lists").on("click",function(){
		mainView.router.loadPage("order/order-cal.html?user_id="+userId);
	});
	
	$$("#mine-addr-list").on("click",function(){
		mainView.router.loadPage("user/mine-addr-list.html?user_id="+userId);
	});
	
	$$("#mine-coupon-lists").on("click",function(){
		mainView.router.loadPage("user/coupon/mine-coupon-list.html?user_id="+userId);
	});
	$$("#mine-charge-list").on("click",function(){
		mainView.router.loadPage("user/charge/mine-charge-list.html?user_id="+userId);
	});
	$$("#mine-feedback-info").on("click",function(){
		mainView.router.loadPage("user/mine-feedback-info.html?user_id="+userId);
	});
	$$("#mine-more").on("click",function(){
		mainView.router.loadPage("user/more.html");
	});
	$$("#mine-info").on("click",function(){
		mainView.router.loadPage("user/user-wancheng.html");
	});

	// 点击 余额 --消费明细
   $$("#restMoneyDiv").on('click',function(){
	   mainView.router.loadPage("user/mine-rest-money-detail.html");
   });
	
	
	
	$$('.user-logout').on('click', function() {
		  localStorage.removeItem("mobile");
		  localStorage.removeItem('user_id');
		  localStorage.removeItem('im_username');
		  localStorage.removeItem('im_password');
		  
		 /*
		  *  此处是 用户退出登录操作, 斟酌之后, 结合 require-data.js的改动
		  *  
		  *  用户还在微网站内！！ 决定保留 这两个 “用户无关性” 数据 	
		  */
//		  localStorage.removeItem('service_type_addons_list');
//		  localStorage.removeItem('service_type_list');
		  
		  localStorage.removeItem("am_id");
		  localStorage.removeItem("am_mobile");
		  mainView.router.loadPage("index.html");
	});
	
});

//列表显示
myApp.template7Data['page:mine'] = function() {
	var result;
	var userId = localStorage.getItem("user_id");;
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
			// console.log(data);
			result = data.data;
		}
	})

	return result;
}

