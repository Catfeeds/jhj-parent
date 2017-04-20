myApp.onPageBeforeInit("period_service_addons", function (page) {

    var serviceTypeId = page.query.service_type_id;

    $$.ajax({
        type:"get",
        url:siteAPIPath+"dict/get_service_type_addons.json?service_type_id="+serviceTypeId,
        dataType:"json",
        success:function(data){
    		var serviceTypeAddonsList = data.data;
    		
    		var temp = $$("#service-type-addons-temp").html();
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
    
    $$(document).on("click",".add-num",function(){
    	
    })
    
});

