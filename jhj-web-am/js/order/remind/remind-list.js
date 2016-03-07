myApp.onPageBeforeInit('am-remind-list-page', function (page) {
	
	var amId = localStorage['am_id'];
	var postdata = {};
	var page = 1;
	var loading = false;// 加载flag
	
	postdata.am_id = amId;
	postdata.page = page;
	
	//追加页面，替换相应的值
	var orderRemindListSuccess= function(data, textStatus, jqXHR) {
		myApp.hideIndicator();
		var result = JSON.parse(data.response);
		var orders = result.data;
		var html = $$('#remind-am-list-part').html();
		var resultHtml = '';
		
//		console.log("order.legnth = " + orders.length);
		for(var i = 0; i< orders.length; i++) {
			var order = orders[i];
			var htmlPart = html;
			
			htmlPart = htmlPart.replace(new RegExp('{order_type_name}',"gm"), order.order_type_name);
			htmlPart = htmlPart.replace(new RegExp('{order_status_name}',"gm"), order.order_status_name);
			
			htmlPart = htmlPart.replace(new RegExp('{service_content}',"gm"), order.service_content);
			htmlPart = htmlPart.replace(new RegExp('{service_date}',"gm"), moment.unix(order.service_date).format("YYYY-MM-DD HH:mm"));
			
			htmlPart = htmlPart.replace(new RegExp('{order_no}',"gm"), order.order_no);
			
			resultHtml += htmlPart;
		}
		$$('.card-remind-list ul').append(resultHtml);
		
		page = page + 1;
		loading = false;
		
		if (orders.length < 10) {
			$$('#remind-am-list-more').css("display", "none");
			return;			
		}
	};	
	
	postdata.am_id = amId;
	postdata.page = page;
	//页面加载时获得订单列表
	 $$.ajax({
         type : "GET",
         url  : siteAPIPath+"remind/am_remind_list.json",
         dataType: "json",
         cache : true,
         data : postdata,
         async : false,
 		statusCode : {
 			200 : orderRemindListSuccess,
 			400 : ajaxError,
 			500 : ajaxError
 		},
 		
	 });

	 $$('#remind-am-list-more').on('click', function () {

		if (loading) return;//如果正在加载，则退出
		loading = true;// 设置flag
		postdata.am_id = amId;
		postdata.page = page;
		$$.ajax({
			type : "GET",
			url  : siteAPIPath+"remind/am_remind_list.json",
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


function hrefToAmRemindView(order_no) {
	var am_id = localStorage['am_id'];
	localStorage.setItem('am_remind_order_no_param', order_no);
	mainView.router.loadPage("order/remind/remind-detail.html?order_no="+order_no);
}