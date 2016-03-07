myApp.onPageBeforeInit('order-rate-page', function(page) {
	
	var userId = localStorage.getItem("user_id");
	var orderId = page.query.order_id;
	// 附加服务 Id 的 字符串
	var tagIds = "";
	//点击图片变色效果。 有一定的局限性（需要是 .png 格式  、选中后的图片名需要是    xxx-2x.png 、 未选中的图片名需要是  xxx.png ）
	//准时到达
	$$("img[name=order_type_0_img]").on('click',function(e) {

		var selectValue = $$(this).attr("value");
		$$("img[name=order_type_0_img]").each(function(key, index) {
			if ($$(this).attr("value") == selectValue) {
				$$(this).attr("src", "img/icons/shi-x.png");
			} else {
				$$(this).attr("src", "img/icons/shi.png");
			}
		});
		$$("#rate_type_0").val(selectValue);
//		console.log($$("#rate_type_0").val())
	});
	//易容仪表整洁
	$$("img[name=order_type_1_img]").on('click',function(e) {

		var selectValue = $$(this).attr("value");
		$$("img[name=order_type_1_img]").each(function(key, index) {
			if ($$(this).attr("value") == selectValue) {
				$$(this).attr("src", "img/icons/shi-x.png");
			} else {
				$$(this).attr("src", "img/icons/shi.png");
			}
		});
		$$("#rate_type_1").val(selectValue);
//		console.log($$("#rate_type_1").val())
	});
	//细节到位
	$$("img[name=order_type_2_img]").on('click',function(e) {

		var selectValue = $$(this).attr("value");
		$$("img[name=order_type_2_img]").each(function(key, index) {
			if ($$(this).attr("value") == selectValue) {
				$$(this).attr("src", "img/icons/shi-x.png");
			} else {
				$$(this).attr("src", "img/icons/shi.png");
			}
		});
		$$("#rate_type_2").val(selectValue);
//		console.log($$("#rate_type_2").val())
	});
	
	//服务整体感观
	$$("img[name=order_type_3_img]").on('click',function(e) {

		var selectValue = $$(this).attr("value");
		console.log("selecrValue 的值"+selectValue);
		$$("img[name=order_type_3_img]").each(function(key, index) {
			var thisValue = $$(this).attr("value");
			if (thisValue == selectValue) {
				if(selectValue == 0 )
					$$(this).attr("src", "img/icons/henbang-x.png");
				if(selectValue == 1 )
					$$(this).attr("src", "img/icons/yiban-x.png");
				if(selectValue == 2 )
					$$(this).attr("src", "img/icons/jiaocha-x.png");				
			} 
			else {
				if(thisValue == 0 )
					$$(this).attr("src", "img/icons/henbang.png");
				if(thisValue == 1 )
					$$(this).attr("src", "img/icons/yiban.png");
				if(thisValue == 2 )
					$$(this).attr("src", "img/icons/jiaocha.png");
			}
		});
		$$("#rate_type_3").val(selectValue);
//		console.log($$("#rate_type_3").val())
		
	});

	
	//整体服务
	
	$$("img[name=order_type_4_img]").on('click',function(e) {

		var selectValue = $$(this).attr("value");
		console.log("selecrValue 的值"+selectValue);
		$$("img[name=order_type_4_img]").each(function(key, index) {
			var thisValue = $$(this).attr("value");
			if (thisValue == selectValue) {
				if(selectValue == 0 )
					$$(this).attr("src", "img/icons/henbang-x.png");
				if(selectValue == 1 )
					$$(this).attr("src", "img/icons/yiban-x.png");
				if(selectValue == 2 )
					$$(this).attr("src", "img/icons/jiaocha-x.png");				
			} 
			else {
				if(thisValue == 0 )
					$$(this).attr("src", "img/icons/henbang.png");
				if(thisValue == 1 )
					$$(this).attr("src", "img/icons/yiban.png");
				if(thisValue == 2 )
					$$(this).attr("src", "img/icons/jiaocha.png");
			}
		});
		$$("#rate_type_4").val(selectValue);
//		console.log($$("#rate_type_4").val())
		
	});
	
	var  saveOrderSuccess = function(data, textStatus, jqXHR) {
		myApp.hideIndicator();
		var results = JSON.parse(data.response);
		//var results = result.appResultData;
		if (results.status == "999") {
			myApp.alert(results.msg);
			$$("#pingjia-submit").attr("disabled",true);
			return;
		}
		if (results.status == "0") {
			myApp.alert("订单评价完成");
			mainView.router.loadPage("order/order-hour-now-list.html");
		}
	} 
	//提交平价
	$$("#pingjia-submit").on("click", function() {
		
		var postdata = {};
		var rate_type_0 = $$("#rate_type_0").val();
		var rate_type_1 = $$("#rate_type_1").val();
		var rate_type_2 = $$("#rate_type_2").val();
		var rate_type_3 = $$("#rate_type_3").val();
		var rate_type_4 = $$("#rate_type_4").val();
		var rateJson = [{"rateType":0, "rateValue": rate_type_0},
		                {"rateType":1, "rateValue": rate_type_1},
		                {"rateType":2, "rateValue": rate_type_2},
		                {"rateType":3, "rateValue": rate_type_3},
		                {"rateType":4, "rateValue": rate_type_4}]
		
		postdata.user_id = userId;
		postdata.order_id = orderId;
		
		postdata.rate_content = $$("#rateContents").val();
		postdata.rate_datas = JSON.stringify(rateJson);
		console.log(postdata);
		//return;
		
		$$.ajax({
			type : "POST",
			url : siteAPIPath+ "order/post_rate.json",
			dataType : "json",
			cache : false,
			data : postdata,
			statusCode : {
				200 : saveOrderSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
		});
	})
	

});





