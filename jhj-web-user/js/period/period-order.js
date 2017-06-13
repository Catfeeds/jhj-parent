myApp.onPageBeforeInit("period-order", function (page) {
    var pageContainer=$$(page.container);

    /*//兼容ios position:fixed
   pageContainer.find('.page-content').on('scroll',function(){
// 重点就是下面这一条语句的实现var housework1Top=$$(window).height()-110;
		var scrollTop=pageContainer.find('.page-content').scrollTop();
        var housework1Top=$$(window).height()+scrollTop-110;
        //$$('.housework1').css('transform',translateY(housework1Top));
       $$('.housework1').css({"top":housework1Top+"px"});
		//({"top":housework1Top+"px"});
    });*/

    var packageTypeId = page.query.package_type_id;

    $$.ajax({
        type:"get",
        url:siteAPIPath+"period/get-periodServiceType-list.json?packageTypeId="+packageTypeId,
        dataType:"json",
        success:function(data){
    		var periodOrderList = data.data;
    		
    		var temp = $$(".period-order-temp").html();
    		var html = "";
    		var total = 0;
    		var ptotal = 0;
    		
    		if(periodOrderList.length>0){
    			for(var i=0;i<periodOrderList.length;i++){
    				var order = {};
    				var periodOrder = periodOrderList[i];
    				var htmlPart = temp;
    				
    				htmlPart = htmlPart.replace(new RegExp('{name}', "gm"), periodOrder.name);
    				htmlPart = htmlPart.replace(new RegExp('{id}', "gm"), periodOrder.id);
    				htmlPart = htmlPart.replace(new RegExp('{serviceTypeId}', "gm"), periodOrder.service_type_id);
    				htmlPart = htmlPart.replace(new RegExp('{serviceTypeAddonsId}', "gm"), periodOrder.service_addon_id);
    				if(periodOrder.service_type_id == 28 || periodOrder.service_type_id == 80 || periodOrder.service_type_id == 81){
    					htmlPart = htmlPart.replace(new RegExp('{adjust}', "gm"), '');
    					htmlPart = htmlPart.replace(new RegExp('{checked}', "gm"), 'checked');
    					htmlPart = htmlPart.replace(new RegExp('{disabled}', "gm"), 'disabled');
    					total = parseFloat(periodOrder.price) * parseFloat(periodOrder.total);
    					ptotal = parseFloat(periodOrder.vip_price) * parseFloat(periodOrder.total);
    				}else{
    					htmlPart = htmlPart.replace(new RegExp('{adjust}', "gm"), '调整');
    					htmlPart = htmlPart.replace(new RegExp('{checked}', "gm"), 'checked');
    					htmlPart = htmlPart.replace(new RegExp('{disabled}', "gm"), '');
						total = parseFloat(periodOrder.price) * parseFloat(periodOrder.total);
						ptotal = parseFloat(periodOrder.vip_price) * parseFloat(periodOrder.total);
    				}
    				if(periodOrder.name=='擦玻璃'){
    					htmlPart = htmlPart.replace(new RegExp('{describle}', "gm"), '总面积');
    					htmlPart = htmlPart.replace(new RegExp('{total}', "gm"), 10);
    				}else{
    					htmlPart = htmlPart.replace(new RegExp('{describle}', "gm"), '总次数');
    				}
    				htmlPart = htmlPart.replace(new RegExp('{total}', "gm"), periodOrder.total);
    				htmlPart = htmlPart.replace(new RegExp('{num-punit}', "gm"), "频次:"+periodOrder.num+""+periodOrder.punit);
    				htmlPart = htmlPart.replace(new RegExp('{price}', "gm"), periodOrder.price);
    				htmlPart = htmlPart.replace(new RegExp('{vipPrice}', "gm"), periodOrder.vip_price);
    				htmlPart = htmlPart.replace(new RegExp('{totalPrice}', "gm"), periodOrder.price*periodOrder.total);
    				htmlPart = htmlPart.replace(new RegExp('{vipTotalPrice}', "gm"), periodOrder.vip_price*periodOrder.total);

					order.periodServiceAddonId = periodOrder.id;
					order.name = periodOrder.name;
    				order.serviceTypeId = periodOrder.service_type_id;
    				order.serviceAddonId = periodOrder.service_addon_id;
    				order.price = periodOrder.price;
    				order.vipPrice = periodOrder.vip_price;
    				order.num = periodOrder.total;
					var ss = JSON.stringify(order).replace(/"/g, "\'");
    				htmlPart = htmlPart.replace(new RegExp('{data-addons-json}', "gm"), ss);
					html += htmlPart;
    			}
    			
    			$$("#period-order-div").html(html);
    		}
    		$$(".housework1 .housework1-1 #period-price span").text(ptotal);
	       	$$(".housework1 .housework1-1 .housework1-1-two #total-price span").text(total);
	       	$$(".housework1 .housework1-1 .housework1-1-two #total-pprice span").text(total-ptotal);
	       	setPeriodPrice();
        }

    });
    
   
    
    //动态计算选中的服务总价格
    $$(document).on('change','.label-checkbox',function(){
    	setPeriodPrice();
    });
    
    $$("#balance-account").on('click',function(){
       	var serviceTypeList = $$("input[name='serviceTypeId']:checked");
       	if(serviceTypeList.length==0){
       		myApp.alert("请至少选择一种服务！");
       		return false;
       	}
       	
       	var periodServiceAddonsArray = [];
       	
       	for(var i=0;i<serviceTypeList.length;i++){
       		var serviceType = serviceTypeList[i];
       		
       		var periodOrder = $$(serviceType).prev().attr("data-addons-json");
			periodOrder = periodOrder.replace(/'/g, "\"");
			var periodOrderObj = JSON.parse(periodOrder);
			if(periodOrderObj instanceof Array){
				Array.prototype.push.apply(periodServiceAddonsArray, periodOrderObj);
			}else{
				periodServiceAddonsArray.push(periodOrderObj);
			}

			setPeriodPrice();
//       		var serviceTypeObject = {};
//       		serviceTypeObject.name = $$(serviceType).nextAll(".item-inner").find(".housework-3 .item-title").text();
//       		serviceTypeObject.periodServiceAddonId = $$(serviceType).prevAll("input[name='id']").val();
//       		serviceTypeObject.serviceTypeId = $$(serviceType).val();
//       		serviceTypeObject.serviceAddonId = $$(serviceType).prev().prev().val();
//       		serviceTypeObject.price = $$(serviceType).nextAll(".item-inner").find(".item-subtitle .housework-5 .price").text();
//       		serviceTypeObject.vipPrice = $$(serviceType).nextAll(".item-inner").find(".item-subtitle .housework-5 .vip-price").text();
//       		serviceTypeObject.num = $$(serviceType).nextAll(".item-inner").find(".item-subtitle .housework-4 .total").text();
       		
//       		periodServiceAddonsArray.push(serviceTypeObject);
       	}
       	
       	sessionStorage.setItem("periodOrder",JSON.stringify(periodServiceAddonsArray));
       	
       	mainView.router.loadPage("order/period/period-order-confirm.html?package_type_id="+packageTypeId);
    });
    
    
    /**---------------------------服务子类--------------------------------*/
    
    $$(document).on('click','.adjust',function(){
    	$$(".popup-overlay").removeClass("modal-overlay-visible");
    	var serviceTypeId = $$(this).prev("input[type='hidden']").val();
		var periodServiceTypeId = $$(this).parents(".item-inner").prevAll("input[name='id']").val();
    	$$(this).parents(".item-inner").prevAll("input[name='serviceTypeId']").attr("checked",true);
    	
    	var dataAddonsJsonString = $$(this).parents(".item-inner").prevAll("input[name='serviceTypeAddonsJson']").attr("data-addons-json");
    	var dataAddonsJson = '';
    	if(dataAddonsJsonString!=null &&dataAddonsJsonString!=''&&dataAddonsJsonString!=undefined){
			dataAddonsJsonString = dataAddonsJsonString.replace(/'/g, "\"");
    		dataAddonsJson = JSON.parse(dataAddonsJsonString);
    	}
    	
    	$$.ajax({
            type:"get",
            url:siteAPIPath+"dict/get_service_type_addons.json?service_type_id="+serviceTypeId,
            dataType:"json",
            success:function(data){
        		var serviceTypeAddonsList = data.data;
        		var temp = $$(".service-type-addons-temp").html();
        		var html = "";
        		if(serviceTypeAddonsList.length>0){
        			for(var i=0;i<serviceTypeAddonsList.length;i++){
        				var serviceTypeAddons = serviceTypeAddonsList[i];
        				if(serviceTypeAddons.name=='金牌保洁' || serviceTypeAddons.name=='基础保洁'){//金牌保洁 30
        					continue;
        				}
        				var htmlPart = temp;
        				
        				htmlPart = htmlPart.replace(new RegExp('{serviceTypeId}', "gm"), serviceTypeId);
        				htmlPart = htmlPart.replace(new RegExp('{serviceTypeAddonsId}', "gm"), serviceTypeAddons.service_addon_id);
        				htmlPart = htmlPart.replace(new RegExp('{name}', "gm"), serviceTypeAddons.name);
        				htmlPart = htmlPart.replace(new RegExp('{price}', "gm"), serviceTypeAddons.price);
        				htmlPart = htmlPart.replace(new RegExp('{pprice}', "gm"), serviceTypeAddons.dis_price);
						htmlPart = htmlPart.replace(new RegExp('{periodServiceTypeId}', "gm"), periodServiceTypeId);
        				/**
        				 * 回显数据
        				 * */
        				for(var j=0;j<dataAddonsJson.length;j++){
        					var addons = dataAddonsJson[j];
        					if(serviceTypeAddons.service_addon_id==addons.serviceAddonId){
        						htmlPart = htmlPart.replace(new RegExp('{checked}', "gm"), "checked");
        						htmlPart = htmlPart.replace(new RegExp('{number}', "gm"), addons.num);
        						htmlPart = htmlPart.replace(new RegExp('{totalPrice}', "gm"), parseFloat(serviceTypeAddons.price)*parseFloat(addons.num));
                				htmlPart = htmlPart.replace(new RegExp('{vipTotalPrice}', "gm"), parseFloat(serviceTypeAddons.dis_price)*parseFloat(addons.num));
        					}
        				}
        				
        				if(serviceTypeAddons.name=='擦玻璃'){
        					htmlPart = htmlPart.replace(new RegExp('{number}', "gm"), 10);
        				}else{
        					htmlPart = htmlPart.replace(new RegExp('{number}', "gm"), 1);
        				}
        				htmlPart = htmlPart.replace(new RegExp('{totalPrice}', "gm"), serviceTypeAddons.price);
        				htmlPart = htmlPart.replace(new RegExp('{vipTotalPrice}', "gm"), serviceTypeAddons.dis_price);
        				
        				html += htmlPart;
        			}
        			$$("#popup-content").html(html,true);
        		}
            }
        });
    });
    
    $$(".page").on("click","#service-type-addons-ul .label-checkbox",function(){
    	$$(this).children(".item-inner").prevAll("input[name='serviceTypeAddons']").removeAttr("disabled");
    })
    
    //计算总价和优惠价
    function calc_price(obj,serviceNum){
    	var temp = $$(".service-type-addons-temp").find(".item-subtitle .housework-6").html();
    	var price = parseFloat($$(obj).parents(".item-inner").find(".item-subtitle .housework-5 .price").text());
    	var pprice = parseFloat($$(obj).parents(".item-inner").find(".item-subtitle .housework-5 .pprice").text());
    	temp = temp.replace(new RegExp('{totalPrice}','gm'),price*serviceNum);
    	temp = temp.replace(new RegExp('{vipTotalPrice}','gm'),pprice*serviceNum);
    	$$(obj).parents(".item-inner").find(".item-subtitle .housework-6").html(temp);
    }
    
    
    //数量加
    $$(".page").on("click",".add-num",function(){
    	$$(this).parents(".item-inner").prevAll("input[name='serviceTypeAddons']").attr("disabled","disabled");
    	var serviceNum = parseInt($$(this).prev(".service-num").val());
    	serviceNum = serviceNum + 1;
    	$$(this).prev(".service-num").val(serviceNum);
    	calc_price(this,serviceNum);
    });
    
    //数量减
    $$(".page").on("click",".sub-num",function(){
    	$$(this).parents(".item-inner").prevAll("input[name='serviceTypeAddons']").attr("disabled","disabled");
    	var serviceNum = parseInt($$(this).next(".service-num").val());
    	var name = $$(this).parent().prev().text();
    	if(name=='擦玻璃'){
    		if(serviceNum<=10){
        		myApp.alert("服务数量不能小于10");
        		return false;
        	}
    	}else{
    		if(serviceNum<=1){
        		myApp.alert("服务数量不能小于1");
        		return false;
        	}
    	}
    	
    	serviceNum = serviceNum - 1;
    	$$(this).next(".service-num").val(serviceNum);
    	calc_price(this,serviceNum);
    });
    
    $$(".page").on("change",".service-num",function(){
    	var serviceNum = $$(this).val();
    	if(serviceNum<=1){
    		myApp.alert("服务数量不能小于1");
    		return false;
    	}
    	calc_price(this,serviceNum);
    })
    
    //调整服务类别
    $$(document).on('click','#btn-ensure',function(){
    	var serviceTypeAddonsList = $$("input[name='serviceTypeAddons']:checked");
    	if(serviceTypeAddonsList==undefined || serviceTypeAddonsList==null || serviceTypeAddonsList=='') return false;
    	
    	var serviceTypeId = $$(this).parent().prev().find(".list-block .label-checkbox input[name='serviceTypeId']").val();
    	//var serviceNum = $$("input[name='serviceTypeAddons']:checked").nextAll(".item-inner").find(".housework-3 .housework-2 .service-num").val();
    	//var price = $$("input[name='serviceTypeAddons']:checked").prevAll("#price1").val();
    	//var pprice = $$("input[name='serviceTypeAddons']:checked").prevAll("#pprice").val();
    	//var name = $$("input[name='serviceTypeAddons']:checked").nextAll(".item-inner").find(".housework-3 .item-title").text();
		var input = $$("input[name='serviceTypeId']:checked");
		
		var serviceTypeAddonsArray = [];
		
		var totalPrice = 0;
		var totalVipPrice = 0;
		var count=0;
		
		for(var i=0;i<serviceTypeAddonsList.length;i++){
			var serviceTypeAddons = serviceTypeAddonsList[i];
			
			var price = $$(serviceTypeAddons).prevAll("#price1").val();
			var pprice = $$(serviceTypeAddons).prevAll("#pprice").val();
			var name = $$(serviceTypeAddons).nextAll(".item-inner").find(".housework-3 .item-title").text();
			var serviceNum = $$(serviceTypeAddons).nextAll(".item-inner").find(".housework-3 .housework-2 .service-num").val();
			var serviceTypeAddonsId = $$(serviceTypeAddons).val();
			var periodServiceTypeId = $$(serviceTypeAddons).prevAll("input[name='periodServiceTypeId']").val();
			
			var serviceTypeAddonsObject = {};
			serviceTypeAddonsObject.serviceTypeId = serviceTypeId;
			serviceTypeAddonsObject.serviceAddonId = serviceTypeAddonsId;
			serviceTypeAddonsObject.price = price;
			serviceTypeAddonsObject.vipPrice = pprice;
			serviceTypeAddonsObject.name = name;
			serviceTypeAddonsObject.num = serviceNum;
			serviceTypeAddonsObject.periodServiceAddonId = periodServiceTypeId;
			serviceTypeAddonsArray.push(serviceTypeAddonsObject);
			
			count += parseFloat(serviceNum);
			totalPrice += parseFloat(price)*parseFloat(serviceNum);
			totalVipPrice += parseFloat(pprice)*parseFloat(serviceNum);
		}
		
		var serviceTypeAddonsJson = JSON.stringify(serviceTypeAddonsArray);
		
		if(input.length>0){
			for(var j=0;j<input.length;j++){
				var inp = input[j];
				if(serviceTypeId == $$(inp).val()){
					$$(inp).nextAll(".item-inner").find(".item-subtitle .housework-4 .total").text(count);
					$$(inp).nextAll(".item-inner").find(".item-subtitle .housework-6 .total-price").text(totalPrice);
					$$(inp).nextAll(".item-inner").find(".item-subtitle .housework-6 .vip-total-price").text(totalVipPrice);
					
					$$(inp).prev().attr('data-addons-json',serviceTypeAddonsJson);
				}
			}
		}
		
		//重新计算选中的金额
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

//计算金额
function setPeriodPrice() {
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
}


