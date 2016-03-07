myApp.onPageBeforeInit('order-list-page', function (page) {
	
	var amId = localStorage['am_id'];
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
		var html = $$('#order-am-list-part').html();
		var resultHtml = '';
		
		console.log("order.legnth = " + orders.length);
		for(var i = 0; i< orders.length; i++) {
			var order = orders[i];
			var htmlPart = html;
			var img_tag = '<img alt="" src="img/icons/order_type_img_'+order.order_type+'.png"></p>';
			htmlPart = htmlPart.replace(new RegExp('{img_tag}',"gm"), img_tag);
			
			
			htmlPart = htmlPart.replace(new RegExp('{order_type}',"gm"), order.order_type);
			htmlPart = htmlPart.replace(new RegExp('{am_id}',"gm"), order.am_id);
			htmlPart = htmlPart.replace(new RegExp('{order_no}',"gm"), order.order_no);

			//助理预约单，展示带上 service_type，方便调整
			if(order.order_type ==2){
				htmlPart = htmlPart.replace(new RegExp('{order_type_name}',"gm"), order.order_type_name+' ('+order.service_type_name+')');
			}else{
				htmlPart = htmlPart.replace(new RegExp('{order_type_name}',"gm"), order.order_type_name);
			}
			
			htmlPart = htmlPart.replace(new RegExp('{order_status_name}',"gm"), order.order_status_name);
			htmlPart = htmlPart.replace(new RegExp('{mobile}',"gm"), order.mobile);
			resultHtml += htmlPart;
		}
		$$('.card-am-list ul').append(resultHtml);
		
		page = page + 1;
		loading = false;
		
		if (orders.length < 10) {
			$$('#order-am-list-more').css("display", "none");
			return;			
		}
	};	
	
	postdata.am_id = amId;
	postdata.page = page;
	//页面加载时获得订单列表
	 $$.ajax({
         type : "GET",
         url  : siteAPIPath+"order/get_am_order_list.json",
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

	 $$('#order-am-list-more').on('click', function () {

		if (loading) return;//如果正在加载，则退出
		loading = true;// 设置flag
		postdata.am_id = amId;
		postdata.page = page;
		$$.ajax({
			type : "GET",
			url  : siteAPIPath+"order/get_am_order_list.json",
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


function hrefToOrderView(order_type, order_no) {
	var am_id = localStorage['am_id'];
	localStorage.setItem('order_no_param', order_no);
	mainView.router.loadPage("order/order-view-"+order_type+".html?am_id="+am_id+"&order_no="+order_no);
}