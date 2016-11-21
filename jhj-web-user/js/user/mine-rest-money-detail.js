myApp.onPageInit('mine-rest-money-detail-page', function (page) {
	
	var userId = localStorage['user_id'];
	var loading = false;// 加载flag
	var page = $$("#page").val();
	
	var restDetailListSuccess = function(data, textStatus, jqXHR) {

		var result = JSON.parse(data.response);
		var datas = result.data;

		var htmlTemplate = $$('#rest-detail-list-part').html();

		var html = ''; // 当前订单

		for (var i = 0; i < datas.length; i++) {
			var item = datas[i];
			var htmlPart = htmlTemplate;
			
			
			var orderType = item.order_type;
			var detailImg = "";
			if (orderType == 0  || orderType == 2 || orderType == 3) {
				detailImg = '<img src="img/laonian/jianhao.png" alt="" class="all-img7">';
			} else if (orderType == 1) {
				detailImg = '<img src="img/laonian/jiahao.png" alt="" class="all-img7">';
			}
			htmlPart = htmlPart.replace(new RegExp('{detailImg}', "gm"), detailImg);
			
			htmlPart = htmlPart.replace(new RegExp('{orderTypeName}', "gm"), item.order_type_name);
			htmlPart = htmlPart.replace(new RegExp('{orderPay}', "gm"), item.order_pay);
			htmlPart = htmlPart.replace(new RegExp('{addTimeStr}', "gm"), item.add_time_str);
			
			html+= htmlPart;
		}
		// 当前订单
		if (page == 1) {
			$$("#rest-detail-list").html(html);
		} else {
			$$("#rest-detail-list").append(html);
		}

		loading = false;
		
		$$("#page").val(page);
		if (datas.length >= 10) {
			$$('#pay-detail-list-more').css("display", "block");
		} else {
			$$('#pay-detail-list-more').css("display", "none");
		}
	};
	
	
	function loadRestDetailList(userId, page) {
		console.log("page = " + page);
		var postdata = {};
		
		var apiUrl = "user/pay_detail_list.json";
		postdata.user_id = userId;
		postdata.page = page;

		$$.ajax({
			type : "GET",
			url : siteAPIPath + apiUrl,
			dataType : "json",
			cache : true,
			data : postdata,
			statusCode : {
				200 : restDetailListSuccess,
				400 : ajaxError,
				500 : ajaxError
			},

		});
	}	
		
	// 注册'infinite'事件处理函数
	$$('#pay-detail-list-more').on('click', function() {
		var cpage = ++page;
		loadRestDetailList(userId, cpage);
	});
	
	loadRestDetailList(userId, page);
});
