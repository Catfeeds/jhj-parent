myApp.onPageBeforeInit('user-list-page', function (page) {
	var amId = localStorage['am_id'];
	var userId = localStorage['user_id'];
	var orderStatus = localStorage['order_status'];
	var postdata = {};
	var page = 1;
	var loading = false;// 加载flag
	
	postdata.am_id = amId;
	postdata.page = page;
	//追加页面，替换相应的值
	var orderListSuccess= function(data, textStatus, jqXHR) {
		myApp.hideIndicator();
		var result = JSON.parse(data.response);
		var orders = result.data;
		var html = $$('#user-list-part').html();
		var resultHtml = '';
		
		
		for(var i = 0; i< orders.length; i++) {
			var order = orders[i];
			var htmlPart = html;
			
			htmlPart = htmlPart.replace(new RegExp('{mobile}',"gm"), order.mobile);
			htmlPart = htmlPart.replace(new RegExp('{service_addr}',"gm"), order.service_addr);
			htmlPart = htmlPart.replace(new RegExp('{am_id}',"gm"), order.am_id);
			htmlPart = htmlPart.replace(new RegExp('{service_times}',"gm"), order.service_times);
			htmlPart = htmlPart.replace(new RegExp('{user_id}',"gm"), order.user_id);
			htmlPart = htmlPart.replace(new RegExp('{order_status_name}',"gm"), order.order_status_name);
			
			resultHtml += htmlPart;
		}
		$$('.card-user-list ul').append(resultHtml);
		
		page = page + 1;
		loading = false;
		
		if (orders.length < 10) {
			$$('#am-user-list-more').css("display", "none");
			return;			
		}
	};	
	//页面加载时获得订单列表
	 $$.ajax({
         type : "GET",
         url  : siteAPIPath+"user/get_user_list.json",
         dataType: "json",
         cache : true,
         data : postdata,
         async : false,
 		statusCode : {
 			200 : orderListSuccess,
 			400 : ajaxError,
 			500 : ajaxError
 		},
 		
 });
	// 注册'infinite'事件处理函数,下拉滚动时触发
	 $$('#am-user-list-more').on('click', function () {
		if (loading) return;//如果正在加载，则退出
		loading = true;// 设置flag
		postdata.am_id = amId;
		postdata.page = page;
		$$.ajax({
			type : "GET",
			url  : siteAPIPath+"user/get_user_list.json",
			dataType : "json",
			cache : true,
			data : postdata,
			timeout: 10000, //超时时间：10秒
			statusCode : {
				200 : orderListSuccess,
				400 : ajaxError,
				500 : ajaxError
			},
		});
	}); 
});



function hrefToUserView(userId) {
	localStorage.setItem('user_id_param', userId);
	mainView.router.loadPage("user/user-form-xiangqing.html?user_id="+userId);
}