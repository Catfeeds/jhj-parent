myApp.onPageBeforeInit('order-list', function(page) {

	var loading = false;// 加载flag
	var page = $$("#page").val();
	function loadOrderList(userId, page) {
		console.log("page = " + page);
		var postdata = {};
		
		var apiUrl = "order/user_order_list.json";
		postdata.user_id = userId;
		postdata.page = page;
		postdata.day = cal;

		var orderListSuccess = function(data, textStatus, jqXHR) {

			var result = JSON.parse(data.response);
			var orders = result.data;

			var htmlTemplate = $$('#order-list-part').html();

			var html = ''; // 当前订单

			for (var i = 0; i < orders.length; i++) {
				var order = orders[i];
				var htmlPart = htmlTemplate;
				htmlPart = htmlPart.replace(new RegExp('{orderType}', "gm"), order.order_type);
				htmlPart = htmlPart.replace(new RegExp('{orderNo}', "gm"), order.order_no);
				htmlPart = htmlPart.replace(new RegExp('{serivceTypeName}', "gm"), order.order_hour_type_name);
				htmlPart = htmlPart.replace(new RegExp('{orderStatusStr}', "gm"), order.order_hour_status_name);
				htmlPart = htmlPart.replace(new RegExp('{addrName}', "gm"), order.address);
				htmlPart = htmlPart.replace(new RegExp('{orderPayStr}', "gm"), order.address);
				html+= htmlPart;
			}
			// 当前订单
			if (page == 1) {
				$$("#order-list-div").html(resultHtmlNow);
			} else {
				$$("#order-list-div").append(resultHtmlNow);
			}

			loading = false;
			
			page = page + 1;
			$$("#page").val(page);
			console.log("page = " + page);
			console.log("len = " + orders.length);
			if (orders.length < 10) {
				$$('#order-list-more').css("display", "none");
			}
		};

		$$.ajax({
			type : "GET",
			url : siteAPIPath + apiUrl,
			dataType : "json",
			cache : true,
			data : postdata,
			statusCode : {
				200 : orderListSuccess,
				400 : ajaxError,
				500 : ajaxError
			},

		});
	}	
		
	// 注册'infinite'事件处理函数
	$$('#order-list-more').on('click', function() {
		var cpage = ++page;
		loadOrderList(userId, cpage);
	});
	
});




