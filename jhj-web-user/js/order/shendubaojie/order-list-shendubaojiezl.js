myApp.onPageBeforeInit('order-list-shendubaojiezl-page', function(page) {
	
	// 附加服务 Id 的 字符串
	var tagIds = "";
	//点击图片变色效果。 有一定的局限性（需要是 .png 格式  、选中后的图片名需要是    xxx-2x.png 、 未选中的图片名需要是  xxx.png ）
	$$(".shendu-fl-lb-every").on('click',function(e) {
		
		var arr =  $$(this).children().children().attr("src").split(".");
		//实现图片的切换
		if(arr[0].endWith("-x")){
			//这里貌似 不能 直接调  subString() [[ substring()!!!!..    ]]   ,RTrim()等方法。但是可以用 replace()
			$$(this).children().children().attr("src",arr[0].replace("-x","")+".png");
			$$(this).css("background","#F7F7F7");
			$$($$(this).children()).next().css("color","#999");
		}else{
			$$(this).children().children().attr("src",arr[0]+"-x"+".png");
			// $$(this).attr("background","#CACCFD");
			$$(this).css("background","#CACCFD");
			$$($$(this).children()).next().css("color","#EF7C00");
		}
		tagIds = $$("#serviceAddons").attr("value");
		
		//图片以'-x'结尾的就是被选中的，统计所用被选中的附加服务，获得其相应的Id
		$$("img[name = tag]").each(function(key, index) {
			if ($$(this).attr("src").indexOf("-x")>0) {
				tagIds = tagIds + $$(this).attr("id") + ",";
			}
		});	
		if (tagIds != "") {
			tagIds = tagIds.substring(0, tagIds.length - 1);
		}
	});
	
	$$("#shendu-to-form").click(function(){
		if(tagIds !='' && tagIds.length>0){
			mainView.router.loadPage("order/order-list-shendubaojie-yuyue.html");
			localStorage.setItem("tagIds",tagIds);
		}else{
			myApp.alert("请选择服务种类");
			return ;
		}
	});
});
