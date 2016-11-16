myApp.onPageBeforeInit('order-rate', function(page) {
	
	var userId = localStorage['user_id'];
	
	var orderId = sessionStorage.getItem("order_id");
	
	var staffNames = sessionStorage.getItem("staff_names");
	
	
	$$("#orderId").val(orderId);
	$$("#staffNameStr").html(staffNames);
	
	$$("#rateAttitude").val(3);
	$$("#rateSkill").val(3);
	//最多只能输入254个字
	$$("#rateContent").keydown(function() {
		var curLength = $$("#rateContent").val().length;
		if (curLength >= 253) {
			var content = $$("#rateContent").val().substr(0, 254);
			$$("#rateContent").val(content);
		}
	});
	
	$$("#rateSubmit").on("click",function(){
		var params = {};
		params.user_id = userId;
		params.order_id = orderId;
		params.rate_arrival = $$("#rateArrival").val();
		params.rate_attitude = $$("#rateAttitude").val();
		params.rate_skill = $$("#rateSkill").val();
		
		var rateContent = $$("#rateContent").val();
		if (rateContent == undefined) rateContent = "";
		params.rate_content = rateContent;
		console.log(params);
		return false;
		$$.ajax({
			type : "POST",
			url : siteAPIPath + "order/post_rate.json",
			dataType : "json",
			cache : true,
			async : false,
			data : params,
			success : function(data) {
				
				var s = data.status;
				if (s == "999") {
					myApp.alert(data.msg);
					return false;
				}
				
				mainView.router.loadPage("order/order-rate-success.html");
			}
		})
	});
	
	
});

function doRateArrival(v) {
	if (v == 0) {
		$$("#rateArrival_0").removeClass("waiter1-2").addClass("waiter1-1");
		$$("#rateArrival_1").removeClass("waiter1-1").addClass("waiter1-2");
	}
	
	if (v == 1) {
		$$("#rateArrival_0").removeClass("waiter1-1").addClass("waiter1-2");
		$$("#rateArrival_1").removeClass("waiter1-2").addClass("waiter1-1");
	}
	
	$$("#rateArrival").val(v);
}

function doRateAttitude(v) {
	
	for (var i = 1; i <= 5; i++) {
		if (v >= i) {
			$$("#rateAttitude_" + i).attr("src", "img/yudingayi/xx.png");
		} else {
			$$("#rateAttitude_" + i).attr("src", "img/yudingayi/xx1.png");
		}
	}
	$$("#rateAttitude").val(v);
}

function doRateSkill(v) {
	
	for (var i = 1; i <= 5; i++) {
		if (v >= i) {
			$$("#rateSkill_" + i).attr("src", "img/yudingayi/xx.png");
		} else {
			$$("#rateSkill_" + i).attr("src", "img/yudingayi/xx1.png");
		}
	}
	$$("#rateSkill").val(v);
}
