myApp.onPageBeforeInit('order-deep-intro', function(page) {
	
	removeSessionData();
	
	var serviceTypeId = page.query.service_type_id;

	sessionStorage.setItem("service_type_id",serviceTypeId);

	function getData(serviceTypeId){
		$$.get("order/deepIntro/order-deep-intro-"+serviceTypeId+".html",function(data){
			$$("#serviceTypeIntroDiv").html(data);
		});
	}
	
	switch (parseInt(serviceTypeId)){
		case 34: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/chuangpuchuman/banner-34.png"); break;
		case 35: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/chuweixiaodu/banner-35.png"); break;
		case 36: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/youyanji/banner-36.png"); break;
		case 50: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/bingxiang/banner-50.png"); break;
		case 51: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/kongtiaoqingxi/banner-51.jpg"); break;
		case 52: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/dibandala/banner-52.png"); break;
		case 53: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/fangwu/banner-53.png"); break;
		case 54: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/caboli/banner-54.png"); break;
		case 56: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/kaihuang/banner-56.png"); break;
		case 60: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/xiyiji/banner-60.png"); break;
		case 78: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/xiyiji/banner-78.png"); break;
		case 79: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/xiyiji/banner-79.png"); break;
		case 83: getData(serviceTypeId);$$("#order-deep-img").attr("src", "img/jinpaibaojie/order-jinpai-83.jpg"); break;
	}


	var result="";

	if(serviceTypeId==undefined || serviceTypeId=="" || serviceTypeId==null) return ;
	//获取服务子类信息
	$$.ajax({
		type:"GET",
		url:siteAPIPath+"dict/get_service_type_addons.json?service_type_id="+serviceTypeId,
		dataType:"json",
		async:false,
		success:function(data){
			var _data=data.data;
			for(var i=0;i<_data.length;i++){
				var name =_data[i].name;
				if(name!="金牌保洁" && name!="基础保洁"){
					result+="<ul class='shendu-bx-xd11'>" +
					"<li>"+_data[i].name+"</li>"+
					"<li>"+_data[i].price+"/"+_data[i].item_unit+"</li>"+
					"<li>"+_data[i].dis_price+"/"+_data[i].item_unit+"</li></ul>"
				}
			}
		}
	});

	$$("#serviceAddons").append(result);
	
	$$("#order-deep-click").on("click", function() {
		var url = "order/order-deep-choose.html?service_type_id="+serviceTypeId;
		mainView.router.loadPage(url);
	});
	

});

