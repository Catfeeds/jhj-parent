myApp.onPageBeforeInit("period-order", function (page) {

    var packageTypeId = page.query.package_type_id;
    packageTypeId=1;

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
    				htmlPart = htmlPart.replace(new RegExp('{serviceTypeId}', "gm"), periodOrder.service_type_id);
    				htmlPart = htmlPart.replace(new RegExp('{price}', "gm"), periodOrder.price);
    				htmlPart = htmlPart.replace(new RegExp('{vipPrice}', "gm"), periodOrder.vip_price);
    				htmlPart = htmlPart.replace(new RegExp('{totalPrice}', "gm"), periodOrder.price*periodOrder.num);
    				htmlPart = htmlPart.replace(new RegExp('{vipTotalPrice}', "gm"), periodOrder.vip_price*periodOrder.num);
    				html += htmlPart;
    			}
    			$$("#period-order-div").html(html);
    		}
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
       	for(var i=0;i<serviceTypeList.length;i++){
       		var serviceParam = {};
       		var serviceTypeId = $$(serviceTypeList[i]).val();
       		serviceParam.serviceTypeId = serviceTypeId;
           	serviceParam.serviceTypeAddonsId = "";
           	serviceParam.serviceNum = 1;
           	serviceParam.price = 0;
           	serviceParam.pprice = 0;
           	serviceParam.name = "";
           	serviceArray.push(serviceParam);
       	}
       	var jsonServiceArray=JSON.stringify(serviceArray);
        sessionStorage.setItem("serviceTypeArray",jsonServiceArray);
        
        var serviceList = $$("input[name='serviceTypeId']:checked");
        var serviceTypeArray = sessionStorage.getItem("serviceTypeArray");
    	if(serviceList.length==0) return false;
    	for(var i=0;i<serviceList.length;i++){
    		var service = serviceList[i];
    		
    		for(var j=0;j<serviceTypeArray.length;j++){
        		var serviceType = serviceTypeArray[j];
        		if($$(service).val()==serviceType.serviceTypeId){
        			$$(service).nextAll(".item-inner").find(".housework-3 .item-title").text(serviceType.name);
        			$$(service).nextAll(".item-inner").find(".item-subtitle .housework-5 .price").text(serviceType.price);
        			$$(service).nextAll(".item-inner").find(".item-subtitle .housework-5 .vip-price").text(serviceType.vipPrice);
        			$$(service).nextAll(".item-inner").find(".item-subtitle .housework-6 .total-price").text(serviceType.totalPrice);
        			$$(service).nextAll(".item-inner").find(".item-subtitle .housework-6 .vip-total-price").text(serviceType.vipTotalPrice);
        		}
        	}
    	}
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
        				htmlPart = htmlPart.replace(new RegExp('{price}', "gm"), serviceTypeAddons.price);
        				htmlPart = htmlPart.replace(new RegExp('{pprice}', "gm"), serviceTypeAddons.dis_price);
        				htmlPart = htmlPart.replace(new RegExp('{totalPrice}', "gm"), serviceTypeAddons.price);
        				htmlPart = htmlPart.replace(new RegExp('{vipTotalPrice}', "gm"), serviceTypeAddons.dis_price);
        				html += htmlPart;
        			}
        			html += '<div><button type="button" id="btn-ensure" class="all-button9 close-popup">确定</button></div></div>';
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
    	var serviceTypeArray = JSON.parse(sessionStorage.getItem("serviceTypeArray"));
    	var serviceTypeAddonsId = $$("input[type='radio']:checked").val();
    	var serviceTypeId = $$(this).parent().prev().find(".list-block .label-checkbox input[name='serviceTypeId']").val();
    	var serviceNum = $$("input[type='radio']:checked").nextAll(".item-inner").find(".housework-3 .housework-2 #service-num").val();
    	var price = $$("input[type='radio']:checked").prevAll("#price1").val();
    	var pprice = $$("input[type='radio']:checked").prevAll("#pprice").val();
    	var name = $$("input[type='radio']:checked").nextAll(".item-inner").find(".housework-3 .item-title").text();
    	if(serviceTypeArray.length>0){
    		for(var i=0;i<serviceTypeArray.length;i++){
    			var serviceType = serviceTypeArray[i];
    			if(serviceTypeId == serviceType.serviceTypeId){
    				serviceType.serviceTypeAddonsId = serviceTypeAddonsId;
    				serviceType.serviceNum = serviceNum;
    				serviceType.price = price;
    				serviceType.pprice = pprice;
    				serviceType.name = name;
    				serviceTypeArray.splice(i,i+1,serviceType);
    				
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
    				break;
    			}
    		}
			sessionStorage.setItem("serviceTypeArray",JSON.stringify(serviceTypeArray));
			
			var period_price = 0;
			var total_price = 0;
			var serviceTypeList = $$("input[name='serviceTypeId']:checked");
	       	for(var k=0;k<serviceTypeList.length;k++){
	       		var type = serviceTypeList[k];
	       		var p = $$(type).nextAll(".item-inner").find(".item-subtitle .housework-6 .total-price").text();
				var pp = $$(type).nextAll(".item-inner").find(".item-subtitle .housework-6 .vip-total-price").text();
				period_price += parseFloat(pp);
				total_price += parseFloat(p);
	       	}
	       	$$(".housework1 .housework1-1 #period-price span").text(period_price);
	       	$$(".housework1 .housework1-1 .housework1-1-two #total-price span").text(total_price);
	       	$$(".housework1 .housework1-1 .housework1-1-two #total-pprice span").text(total_price-period_price);
	        
    	}
    });
    
});
