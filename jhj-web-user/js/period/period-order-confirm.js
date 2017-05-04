myApp.onPageInit("period-order-confirm", function (page) {
	
	var packageTypeId = page.query.package_type_id;
    
	var periodOrderStr = sessionStorage.getItem("periodOrder");
	var periodOrderJson = JSON.parse(periodOrderStr);
	if(periodOrderJson.length>0){
    	var temp = $$(".period-order-temp1").html();
    	var html = "";
    	
    	for(var i=0;i<periodOrderJson.length;i++){
			var periodOrder = periodOrderJson[i];
			var htmlPart = temp;
			htmlPart = htmlPart.replace(new RegExp('{name}', "gm"), periodOrder.name);
			htmlPart = htmlPart.replace(new RegExp('{id}', "gm"), periodOrder.id);
			htmlPart = htmlPart.replace(new RegExp('{serviceTypeId}', "gm"), periodOrder.service_type_id);
			htmlPart = htmlPart.replace(new RegExp('{serviceTypeAddonsId}', "gm"), periodOrder.service_addon_id);
			htmlPart = htmlPart.replace(new RegExp('{total}', "gm"), periodOrder.num);
			htmlPart = htmlPart.replace(new RegExp('{num}', "gm"), periodOrder.num);
			htmlPart = htmlPart.replace(new RegExp('{punit}', "gm"), periodOrder.punit);
			htmlPart = htmlPart.replace(new RegExp('{price}', "gm"), periodOrder.price);
			htmlPart = htmlPart.replace(new RegExp('{vipPrice}', "gm"), periodOrder.vipPrice);
			htmlPart = htmlPart.replace(new RegExp('{totalPrice}', "gm"), periodOrder.price*periodOrder.num);
			htmlPart = htmlPart.replace(new RegExp('{vipTotalPrice}', "gm"), periodOrder.vipPrice*periodOrder.num);
			html += htmlPart;
		}
		$$("#period-order-list").append(html);
    }
    
	// 地址选择处理，1. 是否有默认地址， 2. 是否有选择的地址（最优先）
	var addrId = getItemAddrId();
	var addrName = getItemAddrName();
	
	if (addrName != undefined || addrName != "") {
		$$("#orderChooseAddrName").html(addrName);
	}	

    var totalMoney = sessionStorage.getItem("periodPayMoney");
    if(totalMoney==undefined ||totalMoney==null ||totalMoney=='') totalMoney=0;
    $$("#totalMoney").text(totalMoney);
    
    $$("#save-period-order").on('click',function(){
       	var param = {};
       	param.user_id = localStorage.getItem("user_id");
       	param.mobile = localStorage.getItem("user_mobile");
       	param.addr_id = addrId;
       	param.order_type = 1;
       	param.order_status = 1;
       	param.order_money = sessionStorage.getItem("periodOrderMoney");
       	param.order_price = sessionStorage.getItem("periodPayMoney");
       	param.user_coupons_id = 0;
       	param.period_service_type_id = packageTypeId;
       	param.order_from = 1;
       	param.remarks = "";
       	param.period_service_addons_json = periodOrderStr;
       	$$.ajax({
       		type:"post",
       		url:siteAPIPath+"period/save-period-order.json",
       		data:param,
       		dataType:"json",
       		success:function(data){
       			if(data.status == '999'){
       				myApp.alert(data.msg);
       			}
       			if(data.status == '0'){
       				mainView.router.loadPage("order/period/period-order-pay.html?order_no="+data.data.order_no);
       			}
       		}
       	});
    });
    
});
