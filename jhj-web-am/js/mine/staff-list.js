myApp.onPageInit('staff-list-page', function (page) {
	
	var amId = localStorage['am_id'];
	var postdata = {};
	var page = 1;
	var loading = false;// 加载flag
	
	
	postdata.amId = amId;
	postdata.page = page;
	
	//追加页面，替换相应的值
	var orderListSuccess= function(data, textStatus, jqXHR) {
		myApp.hideIndicator();
		var result = JSON.parse(data.response);
		var staffs = result.data;
		var html = $$('#staffList').html();
		var resultHtml = '';
		
		
		for(var i = 0; i< staffs.length; i++) {
			var staff = staffs[i];
			var htmlPart = html;
			//replace,
			htmlPart = htmlPart.replace(new RegExp('{name}',"gm"), staff.name);
			htmlPart = htmlPart.replace(new RegExp('{mobile}',"gm"), staff.mobile);
			
			htmlPart = htmlPart.replace(new RegExp('{tel}',"gm"),staff.mobile);
			resultHtml += htmlPart;
		}
		$$('.staff-list ul').append(resultHtml);
		
		page = page + 1;
		loading = false;
		
		if (staffs.length < 3) {
		      myApp.detachInfiniteScroll($$('.infinite-scroll'));
		      $$('.infinite-scroll-preloader').remove();// 删除加载提示符
		      return;			
		}
	};	
	
	postdata.amId = amId;
	postdata.page = page;
	//页面加载时获得阿姨列表
	 $$.ajax({
         type : "GET",
         url  : siteAPIPath+"user/get_staff_by_amId.json",
//         dataType: "json",
//         cache : true,
         data : postdata,
//         async : false,
 		statusCode : {
 			200 : orderListSuccess,
 			400 : ajaxError,
 			500 : ajaxError
 		},
 		
 });
	// 注册'infinite'事件处理函数,下拉滚动时触发
	$$('.infinite-scroll').on('infinite', function () {
		if (loading) return;//如果正在加载，则退出
		loading = true;// 设置flag
		postdata.amId = amId;
		postdata.page = page;
		$$.ajax({
			type : "GET",
			url  : siteAPIPath+"user/get_staff_by_amId.json",
//			dataType : "json",
//			cache : true,
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