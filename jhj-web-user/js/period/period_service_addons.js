myApp.onPageBeforeInit("period_service_addons", function (page) {

    var serviceTypeId = page.query.service_type_id;

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
    				var htmlPart = temp;
    				htmlPart = htmlPart.replace(new RegExp('{serviceTypeAddonsId}', "gm"), serviceTypeAddons.service_addon_id);
    				htmlPart = htmlPart.replace(new RegExp('{name}', "gm"), serviceTypeAddons.name);
    				htmlPart = htmlPart.replace(new RegExp('{price}', "gm"), serviceTypeAddons.price);
    				htmlPart = htmlPart.replace(new RegExp('{pprice}', "gm"), serviceTypeAddons.dis_price);
    				htmlPart = htmlPart.replace(new RegExp('{totalPrice}', "gm"), serviceTypeAddons.price);
    				htmlPart = htmlPart.replace(new RegExp('{vipTotalPrice}', "gm"), serviceTypeAddons.dis_price);
    				html += htmlPart;
    			}
    			$$("#service-type-addons-ul").html(html);
    		}
        }
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
    
    $$(document).on("click",".add-num",function(){
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
    
//    $$(document).on('click','',function(){
//    	
//    });
    
    
});

