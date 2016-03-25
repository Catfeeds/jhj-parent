myApp.onPageBeforeInit('order-am-faqiyuyue-page', function (page) {
	
//	console.log("serviceTypeList: "+localStorage.getItem("service_type_list"));	
	
//首页贴心家事。如果新用户未添加地址，不让预约，引导其添加地址
var nowAmId = localStorage['am_id'];

if(nowAmId == 'null'){
	myApp.alert('您还没有添加地址,点击确定前往添加地址立刻获得家庭助理', "", function () {
		mainView.router.loadPage("user/mine-add-addr.html?addr_id=0&return_url=user/user-am-detail.html");
    });
	return;
}	
	
//临时修改，代购和 衣橱整理单价 为 1元
var orderAmPriceServiceType = page.query.service_type;

if(orderAmPriceServiceType == 8 || orderAmPriceServiceType == 9){
	$$("#order-am-faqiyuyue-page-price").html("参考报价：首单惊喜价:<font color='red' class='tehui'>60</font>"+"元,第二单起100元/次");
	
	$$("#priceIntroComplex").hide();
	
	$$("#service_around_li").hide();
	
}else if(orderAmPriceServiceType == 10){
	
	$$("#order-am-faqiyuyue-page-price").html("参考报价：首张床四件套特惠价<font color='red' class='tehui'>19.9</font>"+"元,第二单起100元/次");
	
	$$("#priceIntroComplex").hide();
	
	$$("#service_around_li").hide();
}else if(orderAmPriceServiceType == 11){
	$$("#order-am-faqiyuyue-page-price").html("参考报价：原价15/双,首单特惠<font color='red' class='tehui'>10</font>"+"元/双,5双起");
	
	$$("#priceIntroComplex").hide();
	
	$$("#service_around_li").hide();
	
}else if(orderAmPriceServiceType == 12){
	
	$$("#priceIntroSimple").hide();
	
	$$("#order-am-faqiyuyue-page-price-complex").html
								("<span><font color='red'>特惠</font>：<font color='red'>全城最低！</font>50平米仅需<font color='red' class='tehui'>300</font>元" +
										",(若大于50㎡,每平米加收8元)</span>" 
							+"<br><span><font color='green'>原价</font>：<font color='green' style='text-decoration:line-through'>750</font></span>");
	
	$$("#service_around").html("地板打蜡养护:	实木地板、复合地板" +
							"<p>家具保养:	实木门、实木家具</p>");
	
}else if(orderAmPriceServiceType == 13){
	
	$$("#priceIntroSimple").hide();
	
	$$("#order-am-faqiyuyue-page-price-complex").html
							("<span><font color='red'>特惠</font>：一居<font color='red' >299</font>元,&nbsp;&nbsp;" +
										"两居<font color='red' >349</font>元,&nbsp;&nbsp;"+
										"三居<font color='red' >399</font>元"+
							 "</span>" 
							+"<br><span><font color='green'>原价</font>：一居<font color='green' style='text-decoration:line-through'>360</font>元,&nbsp;&nbsp;" +
									    "两居<font color='green' style='text-decoration:line-through'>460</font>元,&nbsp;&nbsp;"+
										"三居<font color='green' style='text-decoration:line-through'>560</font>元"+
			                "</span>");
	
	$$("#service_around").html("床品除螨:	枕头、被子、褥子、床垫" +
					        "<p>窗帘除螨:	窗帘正反面除螨杀菌吸尘 </p>" +
					        "<p>沙发除菌:	沙发靠背、坐垫、两边扶手</p>");
	
}else if(orderAmPriceServiceType == 14){
	
	$$("#priceIntroSimple").hide();
	
	$$("#order-am-faqiyuyue-page-price-complex").html
							("<span><font color='red'>特惠</font>：" +
										"80~120平米,2人服务仅需<font color='red' >400</font>元" +
										"<p style='text-align:center'>130~150平米,3人服务仅需<font color='red' >600</font>元</p>"+
										"<p style='text-align:center'>160~200平米,4人服务仅需<font color='red' >800</font>元</p>"+
							 "</span>" 
							+"<span><font color='green'>原价</font>：" +
										"80~120平米,2人服务仅需<font color='green' style='text-decoration:line-through' >660</font>元" +
										"<p style='text-align:center'>130~150平米,3人服务仅需<font color='green' style='text-decoration:line-through'>840</font>元</p>"+
										"<p style='text-align:center'>160~200平米,4人服务仅需<font color='green' style='text-decoration:line-through'>1080</font>元</p>"+
			                "</span>");
	
	$$("#service_around").html("玻璃正反面清洗、厨房、卫生间、客厅、卧室、家具、墙面、家庭死角清理");
	
}else if(orderAmPriceServiceType == 15){
	
	$$("#priceIntroSimple").hide();
	
	$$("#order-am-faqiyuyue-page-price-complex").html
							("<span><font color='red'>特惠</font>：一厨一卫</button><font color='red'>329</font>元&nbsp;&nbsp;" +
										"一厨两卫<font color='red' >429</font>元&nbsp;&nbsp;&nbsp;&nbsp;"+
										"一厨三卫<font color='red' >529</font>元"+
							 "</span>" 
							+"<br><span><font color='green'>原价</font>：一厨一卫<font color='green' style='text-decoration:line-through'>690</font>元,&nbsp;&nbsp;" +
									    "一厨两卫<font color='green' style='text-decoration:line-through'>930</font>元,&nbsp;&nbsp;"+
										"一厨三卫<font color='green' style='text-decoration:line-through'>1170</font>元"+
			                "</span>");
	
	$$("#service_around").html("厨房高温杀菌:	厨房全面清洁，除污、除油、蒸汽无缝杀菌、橱柜及瓷砖面抑菌处理" +
			"				 <p>卫生间高温杀菌:	洁具、瓷砖除污垢、水纹、蒸汽无缝杀菌卫浴设施全面清洁、消毒、抑菌处理</p>");
	
}else if(orderAmPriceServiceType == 16){
	
	$$("#priceIntroSimple").hide();
	$$("#order-am-faqiyuyue-page-price-complex").html
							("<span><font color='red'>特惠</font>：<font color='red'>260</font>元" +
							 "</span>" 
							+"<br><span><font color='green'>原价</font>：<font color='green' style='text-decoration:line-through'>320</font>元"+
			                "</span>");
	
	$$("#service_around").html("油烟机服务：清洗机身内外表面、油网、风轮、油盒、涡轮壳、高温蒸汽消毒杀菌" +
							"<p>冰箱清洗:	冰箱内部除冰、零部件清洗、冰箱内外擦拭清洗、高温消毒杀菌</p>" +
							"<p>洗衣机清洗:	洗衣机内筒、外壁、排水口、密封圈、接水管、排水管、显示屏干净光亮</p>");
	
}else{
	
	$$("#priceIntroComplex").hide();
	
	$$("#order-am-faqiyuyue-page-price").html("参考报价：100元/小时(以助理和您电话确认的实际价格为准)");
	
	$$("#service_around_li").hide();
}


var userId = localStorage.getItem("user_id");
var serviceType = page.query.service_type;

var servcieTypeList = JSON.parse(localStorage.getItem("service_type_list"));

$$.each(servcieTypeList, function(i,item){
	if (item.id == serviceType) {
		$$('#servce_type_name').append(item.name);
		$$('#service_type_tips').append(item.tips);
	}
});

//获取用户地址列表
$$('#order-am-faqiyuyue').on('click',function(){
	

	if (orderAmFormValidation() == false) {
		console.log(orderFormValidation);
	
		return false;
	}
	
	var serviceContent = $$("#service_content").val();
    $$.ajax({
        type:"POST",
        url:siteAPIPath+"order/post_user.json",
        dataType:"json",
        cache:false,
        data:{"user_id":userId,
        	  "service_type":serviceType,
              "service_content":serviceContent,
              "order_from":1
      },
        statusCode: {
         	200: onListSuccess,
 	    	400: ajaxError,
 	    	500: ajaxError
 	    },
    });

    
   
});


$$("#service_content").click(function(){
	$$("#myContent").css('padding','10px');
	
});

function onListSuccess(data, textStatus, jqXHR){
	$$("#order-am-faqiyuyue").attr("disabled", false);
   	var result = JSON.parse(data.response);
	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
	var order = result.data;
	var orderNo = order.order_no;
	mainView.router.loadPage("order/order-am-result.html?user_id="+userId+"&order_no="+orderNo);
}
});

function orderAmFormValidation() {
	$$("#order-am-faqiyuyue").removeAttr('disabled');
	var formData = myApp.formToJSON('#order-am-faqiyuyue-form');

	if (formData.service_content == undefined || formData.service_content == "") {
		myApp.alert("请输入服务内容");
		$$("#order-am-faqiyuyue").removeAttr('disabled');
		return false;
	}
	
	return true;
}