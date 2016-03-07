myApp.onPageInit('order-am-page', function(page) {
	
	var nowAmId = localStorage['am_id'];
	
	if(nowAmId == 'null'){
		myApp.alert('您还没有添加地址,点击确定前往添加地址立刻获得家庭助理', "", function () {
			mainView.router.loadPage("user/mine-add-addr.html?addr_id=0&return_url=user/user-am-detail.html");
	    });
		return;
	}
	
	
	var servcieTypeList = JSON.parse(localStorage.getItem("service_type_list"));
	
	
	var html = $$('#order-am-part').html();

	var resultHtml = '';
	$$.each(servcieTypeList, function(i,item){
		
		// 2015-10-22 16:44:41   下架 代购业务
		if(item.id == 8){
			$$("#order-am-part").hide();
		}
		
		if (item.id >= 3 && item.id != 7 && item.id !=8) {
			var htmlPart = html;
			var imgTag = '<img src="img/icons/service_type_img_' + item.id + '.png" alt=""> ';
			htmlPart = htmlPart.replace(new RegExp('{img_tag}',"gm"), imgTag);
			htmlPart = htmlPart.replace(new RegExp('{id}',"gm"), item.id);
			//htmlPart = htmlPart.replace(new RegExp('{img_id}',"gm"), item.id);
			
			
////			//衣橱整理
//			if(item.id == 50){
//				htmlPart = htmlPart.replace(new RegExp('{name}',"gm"), "thisIsTheOnly");
//			}
			
			//2015-11-24 18:19:53 新增5个 活动
			if(item.id == 12 || item.id == 13 || item.id == 14 || item.id == 15 || item.id == 16 ){
				htmlPart = htmlPart.replace(new RegExp('{name}',"gm"), item.name+"<img class='experience' src='img/gif/new.gif'>");
			}
			
			//其他   ，手动设置id 为50，预留新业务 id 的位置
			
			htmlPart = htmlPart.replace(new RegExp('{name}',"gm"), item.name);
			
			
			htmlPart = htmlPart.replace(new RegExp('{tips}',"gm"), item.tips);
			
			
			
			resultHtml += htmlPart;		
		}
		
	});

	$$('.order-am-list ul li').append(resultHtml);
});