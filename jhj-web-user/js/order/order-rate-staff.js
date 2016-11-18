myApp.onPageBeforeInit('order-rate-staff', function(page) {
	
	var userId = localStorage['user_id'];
	
	var staffId = page.query.staff_id;
	

	
	var orderStaffRateSuccess = function(data, textStatus, jqXHR) {
		
		var result = JSON.parse(data.response);
		var orderRates = result.data;
		
		if (orderRates == undefined || orderRates == "") return false;
		
		var htmlTemplate = $$('#staffTotalRateTemplate').html();
		
		var html = ''; // 当前订单
		
		for (var i = 0; i < orderRates.length; i++) {
			var rate = orderRates[i];
			var htmlPart = htmlTemplate;
			htmlPart = htmlPart.replace(new RegExp('{staffName}', "gm"), rate.name);
			htmlPart = htmlPart.replace(new RegExp('{age}', "gm"), rate.age);
			htmlPart = htmlPart.replace(new RegExp('{skill}', "gm"), rate.skill);
			htmlPart = htmlPart.replace(new RegExp('{hukou}', "gm"), rate.hukou);
			htmlPart = htmlPart.replace(new RegExp('{totalArrival}', "gm"), rate.total_arrival);
			htmlPart = htmlPart.replace(new RegExp('{intro}', "gm"), rate.intro);
			htmlPart = htmlPart.replace(new RegExp('{staffId}', "gm"), rate.staff_id);
			//头像
			var headImg = rate.head_img;
			var headImgHtml = '<img src="'+headImg+'" alt="">';
			htmlPart = htmlPart.replace(new RegExp('{headImg}', "gm"), headImgHtml);
			
			//客户好评度
			var totalRateStarHtml = "";
			var totalRateStar = rate.total_rate_star;
			
			if (totalRateStar > 0) {
				for (var i = 1 ; i <= 5; i++) {
					if (i <= totalRateStar) {
						totalRateStarHtml+='<img src="img/yudingayi/xx.png" alt="">';
					} else {
						totalRateStarHtml+='<img src="img/yudingayi/xx1.png" alt="">';
					}
					
				}
			}
			htmlPart = htmlPart.replace(new RegExp('{totalRateStar}', "gm"), totalRateStarHtml);
			html += htmlPart;
		}
		
		$$("#staffTotalRate").html(html);
		
	};
	
	function loadOrderStaffRate(staffId) {
		
		var postdata = {};
		
		var apiUrl = "order/get_staff_total_rate.json";
		postdata.staff_id = staffId;
		
		$$.ajax({
			type : "GET",
			url : siteAPIPath + apiUrl,
			dataType : "json",
			cache : true,
			data : postdata,
			statusCode : {
				200 : orderStaffRateSuccess,
				400 : ajaxError,
				500 : ajaxError
			},
		
		});
	}
	
	loadOrderStaffRate(staffId);
	
});

function reStaffOrder(staffId, staffName) {
	if (staffId == undefined || staffId == "" || staffId == 0) return false;
		
	sessionStorage.setItem("staff_id", staffId);
	sessionStorage.setItem("staff_names", staffName);
	mainView.router.loadPage("order/order-appoint.html");
}
