myApp.onPageBeforeInit('mine', function (page) {
	var userId = localStorage.getItem("user_id");;
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	
	//优惠券
	$$("#mine-coupons").on("click",function(){
		mainView.router.loadPage("user/user-coupons.html?user_id="+userId);
	});

	//关于我们
	$$("#mine-about-us").on("click",function(){
		mainView.router.loadPage("user/aboutus.html");
	});
	
	//常见问题
	$$("#mine-issue").on("click",function(){
		mainView.router.loadPage("user/faq.html");
	});
	
	//地址管理
	$$("#mine-addr-manager").on("click",function(){
		mainView.router.loadPage("user/mine-addr-list.html?user_id="+userId);
	});
	
	//用户协议
	$$("#mine-agreement").on("click",function(){
		mainView.router.loadPage("user/agreement.html");
	});
	
	// 点击 余额 --消费明细
//	$$("#restMoneyDiv").on('click',function(){
//		mainView.router.loadPage("user/mine-rest-money-detail.html");
//	});

	//退出登录
	$$('#user-logout').on('click', function() {
		localStorage.removeItem("mobile");
		localStorage.removeItem('user_id');
		localStorage.removeItem('is_vip');
		localStorage.removeItem('im_username');
		localStorage.removeItem('im_password');

		localStorage.removeItem("am_id");
		localStorage.removeItem("am_mobile");
		
		localStorage.removeItem("default_addr_id");
		localStorage.removeItem("default_addr_name");
		
		removeSessionData();
		mainView.router.loadPage("index.html");
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
			
			if (result != undefined ||result != "") {
				localStorage.setItem("is_vip",result.data.is_vip);
			}
			
		}
	})
	return result;
}


