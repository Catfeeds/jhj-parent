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
    
   /* $$(document).on('click','adjust',function(){
    	var serviceTypeId = $$(this).parents(".item-inner").prevAll("input[name='serviceTypeId']").val();
    	$$(this).attr("href","");
    });*/
    
    
    var serviceArray = [];
    $$("#balance-account").on('click',function(){
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
    
    
    var serviceTypeArray = JSON.parse(sessionStorage.getItem("serviceTypeArray"));
    if(serviceTypeArray==null || serviceTypeArray=='' || serviceTypeArray==undefined){
    	return false;
    }else{
    	var serviceList = $$("input[name='serviceTypeId']:checked");
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
    	
    	
    	
    }
    
    
    
});
