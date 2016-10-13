myApp.onPageBeforeInit('order-list', function(page) {

	var loading = false;// 加载flag
	var page = $$("#page").val();
	function loadOrderList(userId, page, cal) {
		console.log("page = " + page);
		curClickDay = cal;
		var postdata = {};
		
		var apiUrl = "order/user_order_list.json";
		postdata.user_id = userId;
		postdata.page = page;
		postdata.day = cal;

		var orderListSuccess = function(data, textStatus, jqXHR) {

			var result = JSON.parse(data.response);
			var orders = result.data;

			var html = $$('#order-hour-list').html();

			var resultHtmlNow = ''; // 当前订单

			for (var i = 0; i < orders.length; i++) {
				var order = orders[i];

				var htmlPart = html;
				
				htmlPart = htmlPart.replace(new RegExp('{order_type}', "gm"), order.order_type);
				htmlPart = htmlPart.replace(new RegExp('{order_no}', "gm"), order.order_no);
				htmlPart = htmlPart.replace(new RegExp('{order_hour_type_name}', "gm"),
						order.order_hour_type_name);
				htmlPart = htmlPart.replace(new RegExp('{order_hour_status_name}', "gm"),
						order.order_hour_status_name);
				if (order.order_type == 2) {
					htmlPart = htmlPart.replace(new RegExp('{service_date}', "gm"), "预约时间: "+moment.unix(
							order.add_time).format("YYYY-MM-DD HH:mm"));
				} else {
					htmlPart = htmlPart.replace(new RegExp('{service_date}', "gm"), "服务时间: "+moment.unix(
							order.service_date).format("YYYY-MM-DD HH:mm"));
				}

				htmlPart = htmlPart.replace(new RegExp('{address}', "gm"), order.address);

				resultHtmlNow += htmlPart;
			}
			// 当前订单
			if (page == 1) {
				$$("#card-hour-now-list ul").html(resultHtmlNow);
			} else {
				$$("#card-hour-now-list ul").append(resultHtmlNow);
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
		loadOrderList(userId, cpage, curClickDay);
	});
	
});




