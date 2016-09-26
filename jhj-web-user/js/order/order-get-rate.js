myApp.onPageBeforeInit('order-get-rate-page', function(page) {
	
	console.log("111111111111");
	var orderId = page.query.order_id;
	//var result = JSON.parse(data.response);
	//var order = result.data;
	
	
	//获取订单详情
	$$.ajax({
		type : "GET",
		url :siteAPIPath+"order/get_rate.json?order_id="+orderId,
		dataType : "json",
		cache : true,
		success: function(datas,status,xhr){
			console.log(datas);
			var result = datas.data;
			
			/* $$(orderRateVo).each(function(key,index) {
				 console.log(key);
			  });*/
			var content = '';
			var html = $$('#order-am-clean-part').html();
			var list = result.list;
			for(var i=0;i<list.length;i++){
				
			var orderRateVo = list[i];
		    console.log(orderRateVo);
		    
		    content = orderRateVo.rate_content;
		    console.log(content+"------------content-------");
		    
		    rateType = orderRateVo.rate_type;
		    console.log(rateType+"------------rateType-------");
		    
		    rateValue = orderRateVo.rate_value;
		    console.log(rateValue+"------------rateValue-------");
		    if(rateType == 0 ){
				$$("img[name=order_type_0_img]").each(function(key, index) {
					if ($$(this).attr("value") == rateValue) {
						$$(this).attr("src", "img/icons/shi-x.png");
					} else {
						$$(this).attr("src", "img/icons/shi.png");
					}
				});
		    }
		    if(rateType == 1 ){
				$$("img[name=order_type_1_img]").each(function(key, index) {
					if ($$(this).attr("value") == rateValue) {
						$$(this).attr("src", "img/icons/shi-x.png");
					} else {
						$$(this).attr("src", "img/icons/shi.png");
					}
				});
		    }
		    if(rateType == 2 ){
				$$("img[name=order_type_2_img]").each(function(key, index) {
					if ($$(this).attr("value") == rateValue) {
						$$(this).attr("src", "img/icons/shi-x.png");
					} else {
						$$(this).attr("src", "img/icons/shi.png");
					}
				});
		    }
		    if(rateType == 3 ){
		    	
				$$("img[name=order_type_3_img]").each(function(key, index) {
					var thisValue = $$(this).attr("value");
					if (thisValue == rateValue) {
						if(rateValue == 0 )
							$$(this).attr("src", "img/icons/henbang-x.png");
						if(rateValue == 1 )
							$$(this).attr("src", "img/icons/yiban-x.png");
						if(rateValue == 2 )
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
				}
		    if(rateType == 4 ){
		    	
				$$("img[name=order_type_4_img]").each(function(key, index) {
					var thisValue = $$(this).attr("value");
					if (thisValue == rateValue) {
						if(rateValue == 0 )
							$$(this).attr("src", "img/icons/henbang-x.png");
						if(rateValue == 1 )
							$$(this).attr("src", "img/icons/yiban-x.png");
						if(rateValue == 2 )
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
				}
		    
			}
			$$("#rateContent").text(content);
			$$("#rateType").text(rateType);
			$$("#rateValue").text(rateValue);

 	    },
	});
/*	var order = result.data;
	var list = order.list;
	var html = $$('#order-am-clean-part').html();
	var resultHtml = '';
		for(var i = 0; i< servcieTypeAddonsList.length; i++) {
	//	 var dictServiceAddon= servcieTypeAddonsList[i];
		 
			var htmlPart = html;
			htmlPart = htmlPart.replace(new RegExp('{service_addon_id}',"gm"), service_addon_id);
			htmlPart = htmlPart.replace(new RegExp('{service_addon_name}',"gm"), serviceAddonName);
			htmlPart = htmlPart.replace(new RegExp('{item_price}',"gm"), itemPrice);
			htmlPart = htmlPart.replace(new RegExp('{item_unit}',"gm"), dictServiceAddon.item_unit);
			htmlPart = htmlPart.replace(new RegExp('{item_num}',"gm"), dictServiceAddon.default_num);
			resultHtml += htmlPart;
		
	}
*/


});





