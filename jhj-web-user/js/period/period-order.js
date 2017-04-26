myApp.onPageBeforeInit("period-order", function (page) {

    var packageTypeId = page.query.package_type_id;

    $$.ajax({
        type:"get",
        url:siteAPIPath+"period/get-periodServiceType-list.json?packageTypeId="+packageTypeId,
        dataType:"json",
        success:function(data){
    		var periodOrderList = data.data;
    		
    		var temp = $$(".period-order-temp").html();
    		var html = "";
    		
    		if(periodOrderList.length>0){
    			for(var i=0;i<periodOrderList.length;i++){
    				var periodOrder = periodOrderList[i];
    				var htmlPart = temp;
    				htmlPart = htmlPart.replace(new RegExp('{name}', "gm"), periodOrder.name);
    				htmlPart = htmlPart.replace(new RegExp('{id}', "gm"), periodOrder.id);
    				htmlPart = htmlPart.replace(new RegExp('{serviceTypeId}', "gm"), periodOrder.service_type_id);
    				htmlPart = htmlPart.replace(new RegExp('{serviceTypeAddonsId}', "gm"), periodOrder.service_addon_id);
    				htmlPart = htmlPart.replace(new RegExp('{total}', "gm"), periodOrder.total);
    				htmlPart = htmlPart.replace(new RegExp('{num}', "gm"), periodOrder.num);
    				htmlPart = htmlPart.replace(new RegExp('{punit}', "gm"), periodOrder.punit);
    				htmlPart = htmlPart.replace(new RegExp('{price}', "gm"), periodOrder.price);
    				htmlPart = htmlPart.replace(new RegExp('{vipPrice}', "gm"), periodOrder.vip_price);
    				htmlPart = htmlPart.replace(new RegExp('{totalPrice}', "gm"), periodOrder.price*periodOrder.total);
    				htmlPart = htmlPart.replace(new RegExp('{vipTotalPrice}', "gm"), periodOrder.vip_price*periodOrder.total);
    				html += htmlPart;
    			}
    			$$("#period-order-div").html(html);
    		}
        }
    });
    
    //动态计算选中的服务总价格
    $$(document).on('change','.label-checkbox',function(){
    	var serviceTypeList = $$("input[name='serviceTypeId']:checked");
    	if(serviceTypeList.length>0){
    		var periodPrice = 0;
    		var orginPrice = 0;
    		for(var i=0;i<serviceTypeList.length;i++){
    			var serviceType = serviceTypeList[i];
    			
    			var p = $$(serviceType).nextAll(".item-inner").find(".item-subtitle .housework-6 .total-price").text();
				var pp = $$(serviceType).nextAll(".item-inner").find(".item-subtitle .housework-6 .vip-total-price").text();
				periodPrice += parseFloat(pp);
				orginPrice += parseFloat(p);
    		}
    		
    		sessionStorage.setItem("periodOrderMoney",orginPrice);
    		sessionStorage.setItem("periodPayMoney",periodPrice);
    		
    		$$(".housework1 .housework1-1 #period-price span").text(periodPrice);
	       	$$(".housework1 .housework1-1 .housework1-1-two #total-price span").text(orginPrice);
	       	$$(".housework1 .housework1-1 .housework1-1-two #total-pprice span").text(orginPrice-periodPrice);
    	}else{
    		$$(".housework1 .housework1-1 #period-price span").text(0);
	       	$$(".housework1 .housework1-1 .housework1-1-two #total-price span").text(0);
	       	$$(".housework1 .housework1-1 .housework1-1-two #total-pprice span").text(0);
    	}
    });
    
    $$("#balance-account").on('click',function(){
    	var serviceArray = [];
    	sessionStorage.removeItem("serviceArray");
       	var serviceTypeList = $$("input[name='serviceTypeId']:checked");
       	if(serviceTypeList.length==0){
       		myApp.alert("请至少选择一种服务！");
       		return false;
       	}
       	
       	var periodServiceAddonsArray = [];
       	for(var i=0;i<serviceTypeList.length;i++){
       		var serviceType = serviceTypeList[i];
       		var serviceTypeObject = {};
       		serviceTypeObject.periodServiceTypeId = $$(serviceType).prevAll("input[name='id']").val();
       		serviceTypeObject.serviceTypeId = $$(serviceType).val();
       		serviceTypeObject.serviceAddonId = $$(serviceType).prev().val();
       		serviceTypeObject.price = $$(serviceType).nextAll(".item-inner").find(".item-subtitle .housework-5 .price").text();
       		serviceTypeObject.vipPrice = $$(serviceType).nextAll(".item-inner").find(".item-subtitle .housework-5 .vip-price").text();
       		serviceTypeObject.num = $$(serviceType).nextAll(".item-inner").find(".item-subtitle .housework-4 .total").text();
       		
       		periodServiceAddonsArray.push(serviceTypeObject);
       	}
       	
       	
       	var param = {};
       	param.user_id = localStorage.getItem("user_id");
       	param.mobile = localStorage.getItem("user_mobile");
       	param.addr_id = 1;
       	param.order_type = 1;
       	param.order_status = 1;
       	param.order_money = sessionStorage.getItem("periodOrderMoney");
       	param.order_price = sessionStorage.getItem("periodPayMoney");
       	param.user_coupons_id = 0;
       	param.period_service_type_id = 0;
       	param.order_from = 2;
       	param.remarks = "";
       	param.period_service_addons_json = JSON.stringify(periodServiceAddonsArray);
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
       				mainView.router.loadPage("order/period/period-order-pay.html?period_order_id="+data.data.id);
       			}
       		}
       	});
       
    });
    
    
    /**---------------------------服务子类--------------------------------*/
    
    $$(document).on('click','.adjust',function(){
    	var serviceTypeId = $$(this).prev("input[type='hidden']").val();
    	$$(this).parents(".item-inner").prevAll("input[name='serviceTypeId']").attr("checked",true);
    	$$.ajax({
            type:"get",
            url:siteAPIPath+"dict/get_service_type_addons.json?service_type_id="+serviceTypeId,
            dataType:"json",
            success:function(data){
        		var serviceTypeAddonsList = data.data;
        		var temp = $$(".service-type-addons-temp").html();
        		var html = "";
        		if(serviceTypeAddonsList.length>0){
        			html = '<div class="popup popup-about">';
        			for(var i=0;i<serviceTypeAddonsList.length;i++){
        				var serviceTypeAddons = serviceTypeAddonsList[i];
        				var htmlPart = temp;
        				htmlPart = htmlPart.replace(new RegExp('{serviceTypeId}', "gm"), serviceTypeId);
        				htmlPart = htmlPart.replace(new RegExp('{serviceTypeAddonsId}', "gm"), serviceTypeAddons.service_addon_id);
        				htmlPart = htmlPart.replace(new RegExp('{name}', "gm"), serviceTypeAddons.name);
        				htmlPart = htmlPart.replace(new RegExp('{total}', "gm"), serviceTypeAddons.total);
        				htmlPart = htmlPart.replace(new RegExp('{num}', "gm"), serviceTypeAddons.num);
        				htmlPart = htmlPart.replace(new RegExp('{punit}', "gm"), serviceTypeAddons.punit);
        				htmlPart = htmlPart.replace(new RegExp('{price}', "gm"), serviceTypeAddons.price);
        				htmlPart = htmlPart.replace(new RegExp('{pprice}', "gm"), serviceTypeAddons.dis_price);
        				htmlPart = htmlPart.replace(new RegExp('{totalPrice}', "gm"), serviceTypeAddons.price);
        				htmlPart = htmlPart.replace(new RegExp('{vipTotalPrice}', "gm"), serviceTypeAddons.dis_price);
        				html += htmlPart;
        			}
        			html += '<div><button type="button" id="btn-ensure" class="all-button17 close-popup">确定</button></div></div>';
        			myApp.popup(html);
        		}
            }
        });
    });
    
    //计算总价和优惠价
    function calc_price(obj,serviceNum){
    	var temp = $$(".service-type-addons-temp").find(".item-subtitle .housework-6").html();
    	var price = parseFloat($$(obj).parents(".item-inner").find(".item-subtitle .housework-5 .price").text());
    	var pprice = parseFloat($$(obj).parents(".item-inner").find(".item-subtitle .housework-5 .pprice").text());
    	temp = temp.replace(new RegExp('{totalPrice}','gm'),price*serviceNum);
    	temp = temp.replace(new RegExp('{vipTotalPrice}','gm'),pprice*serviceNum);
    	$$(obj).parents(".item-inner").find(".item-subtitle .housework-6").html(temp);
    }
    
    $$(document).off("click",".add-num").on("click",".add-num",function(){
    	var serviceNum = parseInt($$(this).prev("#service-num").val());
    	serviceNum = serviceNum + 1;
    	$$(this).prev("#service-num").val(serviceNum);
    	calc_price(this,serviceNum);
    });
    
    $$(document).on("click",".sub-num",function(){
    	var serviceNum = parseInt($$(this).next("#service-num").val());
    	if(serviceNum<=1){
    		myApp.alert("服务数量不能小于1");
    		return false;
    	}
    	serviceNum = serviceNum - 1;
    	$$(this).next("#service-num").val(serviceNum);
    	calc_price(this,serviceNum);
    });
    
    //调整服务类别
    $$(document).on('click','#btn-ensure',function(){
    	var serviceTypeAddonsId = $$("input[type='radio']:checked").val();
    	var serviceTypeId = $$(this).parent().prev().find(".list-block .label-checkbox input[name='serviceTypeId']").val();
    	var serviceNum = $$("input[type='radio']:checked").nextAll(".item-inner").find(".housework-3 .housework-2 #service-num").val();
    	var price = $$("input[type='radio']:checked").prevAll("#price1").val();
    	var pprice = $$("input[type='radio']:checked").prevAll("#pprice").val();
    	var name = $$("input[type='radio']:checked").nextAll(".item-inner").find(".housework-3 .item-title").text();
		var input = $$("input[type='checkbox']:checked");
		if(input.length>0){
			for(var j=0;j<input.length;j++){
				var inp = input[j];
				if(serviceTypeId == $$(inp).val()){
					$$(inp).nextAll(".item-inner").find(".housework-3 .item-title").text(name);
					$$(inp).nextAll(".item-inner").find(".item-subtitle .housework-5 input[name='price']").val(price);
					$$(inp).nextAll(".item-inner").find(".item-subtitle .housework-5 input[name='pprice']").val(pprice);
					$$(inp).nextAll(".item-inner").find(".item-subtitle .housework-5 .housework-5-1 .price").text(price);
					$$(inp).nextAll(".item-inner").find(".item-subtitle .housework-5 #price .vip-price").text(pprice);
					$$(inp).nextAll(".item-inner").find(".item-subtitle .housework-6 .total-price").text(price*serviceNum);
					$$(inp).nextAll(".item-inner").find(".item-subtitle .housework-6 .vip-total-price").text(pprice*serviceNum);
				}
			}
		}
    	var serviceTypeList = $$("input[name='serviceTypeId']:checked");
    	if(serviceTypeList.length>0){
    		var periodPrice = 0;
    		var orginPrice = 0;
    		for(var i=0;i<serviceTypeList.length;i++){
    			var serviceType = serviceTypeList[i];
    			var p = $$(serviceType).nextAll(".item-inner").find(".item-subtitle .housework-6 .total-price").text();
				var pp = $$(serviceType).nextAll(".item-inner").find(".item-subtitle .housework-6 .vip-total-price").text();
				periodPrice += parseFloat(pp);
				orginPrice += parseFloat(p);
    		}
    		$$(".housework1 .housework1-1 #period-price span").text(periodPrice);
	       	$$(".housework1 .housework1-1 .housework1-1-two #total-price span").text(orginPrice);
	       	$$(".housework1 .housework1-1 .housework1-1-two #total-pprice span").text(orginPrice-periodPrice);
    	}
    });
    
});
