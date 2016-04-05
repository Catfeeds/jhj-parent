
$$("#indexPageContentDiv").find("a").on("click",function(k,v){
		
	
});

$$(document).on('pageBeforeInit', function(e) {
	var page = e.detail.page;
	
	if (page.name === 'index') {
		
		$$(".mincircle").on("click",function(k,v){
			
			var content =  $$(this).find("p").text();
			
			//给一个默认的 跳转页
			var url = "index.html";
			
			var firstServiceType = "";
			
			if(content == '金牌保洁'){
				url = "order/fiveservice/jinpaibaojie.html?firstServiceType=23";
				firstServiceType = 23;
			}
			
			if(content == '厨娘烧饭'){
				url = "order/fiveservice/cook.html?firstServiceType=24";
				firstServiceType = 24;
			}
			
			if(content == '贴心家事'){
				url = "order/fiveservice/tiexinjiashi.html?firstServiceType=25";
				firstServiceType = 25;
			}
			
			if(content == '深度养护'){
				url = "order/fiveservice/tiexinjiashi.html?firstServiceType=26";
				firstServiceType = 26;
			}
			
			if(content == '企业服务'){
				url = "order/fiveservice/tiexinjiashi.html?firstServiceType=27";
				firstServiceType = 27;
			}
			
			localStorage.setItem("firstServiceType",firstServiceType);
			
			mainView.router.loadPage(url);			
		});
	}
})