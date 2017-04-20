myApp.onPageBeforeInit("period-order", function (page) {

    var packageTypeId = page.query.package_type_id;
    packageTypeId=1;

    $$.ajax({
        type:"get",
        url:siteAPIPath+"period/get-periodServiceType-list.json?packageTypeId="+packageTypeId+"&pageNo=1",
        dataType:"json",
        success:function(data){
    		var periodOrderList = data.data;
    		
    		var temp = $$("#period-order-temp").html();
    		var html = "";
    		
    		if(periodOrderList.length>0){
    			for(var i=0;i<periodOrderList.length;i++){
    				var periodOrder = periodOrderList[i];
    				var htmlPart = temp;
    				htmlPart = htmlPart.replace(new RegExp('{name}', "gm"), periodOrder.name);
    				htmlPart = htmlPart.replace(new RegExp('{serviceTypeId}', "gm"), periodOrder.service_type_id);
    				htmlPart = htmlPart.replace(new RegExp('{price}', "gm"), periodOrder.price);
    				htmlPart = htmlPart.replace(new RegExp('{vipPrice}', "gm"), periodOrder.vip_price);
    				htmlPart = htmlPart.replace(new RegExp('{totalPrice}', "gm"), periodOrder.price*periodOrder.num);
    				htmlPart = htmlPart.replace(new RegExp('{vipTotalPrice}', "gm"), periodOrder.vip_price*periodOrder.num);
    				html += htmlPart;
    			}
    			$$("#period-order-ul").html(html);
    		}
        }
    });
});
