myApp.onPageInit("period-order-detail", function (page) {
    
	var periodOrderId = page.query.period_order_id;
	
	function periodOrderAddonsList(data, textStatus, jqXHR){
		
		var result = JSON.parse(data.response);
		var periodOrderList = result.data;
		
		var temp = $$(".period-order-temp2").html();
		var html = "";
	    	
		for(var i=0;i<periodOrderList.length;i++){
			var periodOrder = periodOrderList[i];
			var htmlPart = temp;
			htmlPart = htmlPart.replace(new RegExp('{name}', "gm"), periodOrder.service_addons_name);
			htmlPart = htmlPart.replace(new RegExp('{id}', "gm"), periodOrder.id);
			htmlPart = htmlPart.replace(new RegExp('{serviceTypeId}', "gm"), periodOrder.service_type_id);
			htmlPart = htmlPart.replace(new RegExp('{serviceTypeAddonsId}', "gm"), periodOrder.service_addon_id);
			htmlPart = htmlPart.replace(new RegExp('{total}', "gm"), periodOrder.num);
			htmlPart = htmlPart.replace(new RegExp('{num}', "gm"), periodOrder.num);
			htmlPart = htmlPart.replace(new RegExp('{punit}', "gm"), periodOrder.punit);
			htmlPart = htmlPart.replace(new RegExp('{price}', "gm"), periodOrder.price);
			htmlPart = htmlPart.replace(new RegExp('{vipPrice}', "gm"), periodOrder.vip_price);
			htmlPart = htmlPart.replace(new RegExp('{totalPrice}', "gm"), periodOrder.price*periodOrder.num);
			htmlPart = htmlPart.replace(new RegExp('{vipTotalPrice}', "gm"), periodOrder.vip_price*periodOrder.num);
			html += htmlPart;
		}
		$$("#period-order-detail").append(html);
	}
	
	$$.ajax({
   		type:"get",
   		url:siteAPIPath+"period/get_period_order_addons_list.json",
   		data:{"periodOrderId":periodOrderId},
   		dataType:"json",
   		statusCode : {
			200 : periodOrderAddonsList,
			400 : ajaxError,
			500 : ajaxError
		}
   	});
});
