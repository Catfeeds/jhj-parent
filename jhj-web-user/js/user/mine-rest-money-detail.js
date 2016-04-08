myApp.onPageInit('mine-rest-money-detail-page', function (page) {
	
	var postdata = {};
	var page = 1;
	var userId = localStorage['user_id'];
	var loading = false;// 加载flag
	var apiUrl = "user/pay_detail_list.json";
	postdata.user_id = userId;
	postdata.page = page;
	
	
	var displayItem;
	
	
	var orderListSuccess= function(data, textStatus, jqXHR) {
		
		var result = JSON.parse(data.response);
		
		displayItem = result.data;
		
		if(page >=2){
			myList.appendItems(displayItem);
		}
		
		page = page+1;
		loading = false;
		
		if(displayItem.length == 0){
			loading = true;
			$$("#pay-detail-list-more").text("已无更多记录(*^__^*)");
		}
		
		return false;
		
	}	
	
	
	postdata.user_id = userId;
	postdata.page = page;
	 $$.ajax({
         type : "GET",
         url  : siteAPIPath + apiUrl,
         dataType: "json",
         cache : true,
         data : postdata,
         async:false,	//设置为 同步。取到数据后。再 构造
 		statusCode : {
 			200 : orderListSuccess,
 			400 : ajaxError,
 			500 : ajaxError
 		}
 		
	 });
	

 var myList = myApp.virtualList('.list-block.virtual-list',{
		
		   items:  displayItem,
		   height: 100,
		template:  "<div class='pay-list-rv' >"
						+ "<div class='pay-list-rv-left'>"
						+  		"<img src='{{img_url}}' alt=''>"
						+ "</div>"
						+ "<div class='pay-list-rv-right'>"
						+	"<div class='pay-list-rv-right1'>"
						+		"<div class='pay-list-rv-right1-1'>{{order_type_name}}</div>"
						+			"<div class='pay-list-rv-right1-2'>支付金额:{{order_money}}</div>"
						+		"</div>"
						+		"<div class='pay-list-rv-right2'>时间:{{add_time_str}}</div>"
						+	"</div>"
						+ "</div>"
					+"</div>"
	});
	

	 
	 
	// 注册'infinite'事件处理函数
	$$('#pay-detail-list-more').on('click', function () {
		if (loading) return;//如果正在加载，则退出
		loading = true;// 设置flag
		postdata.user_id = userId;
		
		postdata.page = page;
		
		$$.ajax({
			type : "GET",
			url  : siteAPIPath + apiUrl,
			dataType : "json",
			cache : true,
			data : postdata,
			timeout: 10000, //超时时间：10秒
			async:false,
			statusCode : {
				200 : orderListSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
		});
	}); 
	
});
