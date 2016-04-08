//获取用户地址列表
myApp.template7Data['page:mine-addr-list'] = function(){
  var result; 
  var userId = localStorage['user_id'];
  
  $$.ajax({
      type : "GET",
      url: siteAPIPath+"user/get_user_addrs.json?user_id="+userId,
      dataType: "json",
      cache : true,
      async : false,
      success: function(data){
    	  
    	  if (localStorage['default_addr_id'] == null) {
    		  $$.each(data, function(i,item) {
    			  if (item.is_default == 1) {
    				  localStorage['default_addr_id'] = item.id;
    				  localStorage['default_addr_name'] = item.name + " " + item.addr;
    				  return false;
    			  }
    		  })
    		  
    		  
    	  }
    	  console.log("default_addr_id = " + localStorage['default_addr_id']);
    	  console.log("default_addr_name = " + localStorage['default_addr_name']);
    	  
    	  
          result = data;
      }
  })
  
  return result;
}

myApp.onPageInit('mine-addr-list', function (page) {
	
	var userId = localStorage['user_id'];

    $$(".mine-add-addr-link").on("click",function(){
 		mainView.router.loadPage("user/mine-add-addr.html?addr_id=0");
 	});

//    $$(document).on('click', '#selectAddrLink', function (e){
//    	
//    	e.stopImmediatePropagation(); //防止重复点击
//    	
//    	var addrId = $$(this).find("input[name=addr_id]").val();
//    	var addrName = $$(this).find(".item-title").html();
//
//    	
//    	addrName = addrName.replace("[默认] ", "");
//    	
//    	var returnPage = "";
//    	for (var i =1; i < 5; i++) {
//    		var historyPage = mainView.history[mainView.history.length-i];
//    		
//    		if (historyPage == undefined) continue;
//    		
//    		if (historyPage.indexOf("order-form-zhongdiangong") >= 0 ||
//    			historyPage.indexOf("order-list-shendubaojie-yuyue") >= 0
//    			) {
//    			returnPage = historyPage;
//    			break;
//    		}
//
//    	}
//    	
//    	if (returnPage == "") return;
//    	
//    	returnPage = replaceParamVal(returnPage, "addr_id", addrId);
//    	returnPage = replaceParamVal(returnPage, "addr_name", addrName);
//    	
//    	
//    	if (returnPage.indexOf("?") <=0 && returnPage.indexOf("addr") <=0) {
//    		returnPage = returnPage + "?addr_id="+addrId + "&addr_name="+ addrName;
//    	} 
//    	
//    	if (returnPage.indexOf("&") <=0 && returnPage.indexOf("addr") <=0) {
//    		returnPage = returnPage + "&addr_id="+addrId + "&addr_name="+ addrName;
//    	}
//    		
//
//    	mainView.router.loadPage(returnPage);
// 	});    
    
    
});


//function setAddrDefault(addrId) {
//
//	var userId = localStorage['user_id'];
//
//	var paramData = {};
//	paramData.user_id = userId;
//	paramData.addr_id = addrId;
//	$$.ajax({
//		type : "POST",
//		url : siteAPIPath + "user/post_set_addr_default.json",
//        data:{"user_id":userId, "addr_id":addrId},
//		success : function() {
//			
//			//设置用户默认地址全局变量
//			
//			//设置当前的为默认的地址.
//			$$(".swipeout").each(function(key, index) {
//
//				var addrIdObj = $$(this).find('input[type=hidden]');
//
//				var addrNameObj = $$(this).find('.item-title');
//				
//				var addrNameHtml = addrNameObj.html();
//				if (addrId == addrIdObj.val()) {
//					if (addrNameHtml.indexOf("默认") < 0) {
//						
//						localStorage['default_addr_id'] = addrId;
//						localStorage['default_addr_name'] = addrNameHtml.trim();
//						addrNameObj.html("[默认] " + addrNameHtml);
//						
//					} 
//				} else {
//					addrNameObj.html(addrNameHtml.replace("[默认]", ""));
//				}
//				myApp.swipeoutClose($$(this));
//			});
//			
//		}
//	});	
//}

function clickToSetDefault(obj,addrId){
	
	//当前被点击的地址
	var addrNameClick= $$(obj).find("#addrName").text();
	//当前被点击的地址  id
	var addrIdClick = $$(obj).find("#addr_id").val();
	
	if(addrNameClick.indexOf("默认")>0){
		
		localStorage.setItem("default_addr_name",addrNameClick.replace("[默认]", "").trim());
		localStorage.setItem("default_addr_id",addrIdClick);
		
		goBackToZhongDianGongPage();
		
		return false;
	}
	
	
	var userId = localStorage['user_id'];

	var paramData = {};
	paramData.user_id = userId;
	paramData.addr_id = addrId;
	
	myApp.confirm('设置为默认地址?',
			function(){
		
				$$.ajax({
					type : "POST",
					url : siteAPIPath + "user/post_set_addr_default.json",
			        data:{"user_id":userId, "addr_id":addrId},
			        async:false,
					success : function() {
						
						//设置当前的为默认的地址.
						$$(".swipeout").each(function(key, index) {
			
							var addrIdObj = $$(this).find('input[type=hidden]');
			
							var addrNameObj = $$(this).find('.item-title');
							
							var addrNameHtml = addrNameObj.html();
							if (addrId == addrIdObj.val()) {
								if (addrNameHtml.indexOf("默认") < 0) {
									
									localStorage['default_addr_id'] = addrId;
									localStorage['default_addr_name'] = addrNameHtml.trim();
									addrNameObj.html("[默认] " + addrNameHtml);
									
								} 
							} else {
								addrNameObj.html(addrNameHtml.replace("[默认]", ""));
							}
							myApp.swipeoutClose($$(this));
						});
						
						goBackToZhongDianGongPage();
						
					}
			        
				});	
		 },
		 //  点击 "取消/返回",不设置默认地址,但是 把该点击地址 作为 选中项，传回上一页
		 function (){
			 goBackToZhongDianGongPage1(addrIdClick,addrNameClick);
		 }
	);
}

//点选 ‘取消/返回’  1.不修改默认地址  2.把该地址 传回下单页
function goBackToZhongDianGongPage1(id,name){
	
	var addrId = id;
	var addrName = name;
	
	var returnPage = "";
	for (var i =1; i < 5; i++) {	// 判断前一页是不是 下单页,如果是,则作为 返回页
		var historyPage = mainView.history[mainView.history.length-i];
		
		if (historyPage == undefined) continue;
		
		if (historyPage.indexOf("order-form-zhongdiangong") >= 0 ||
			historyPage.indexOf("order-list-shendubaojie-yuyue") >= 0 ||
			
			//2015-11-6 15:17:49    返回 历史订单 添加页面
			historyPage.indexOf("history/history-zhongdiangong") >=0 ||	
			
			historyPage.indexOf("history/history-am") >=0 
			) {
			returnPage = historyPage;
			break;
		}

	}
	
	if (returnPage == "") return;
	
	returnPage = replaceParamVal(returnPage, "addr_id", addrId);
	returnPage = replaceParamVal(returnPage, "addr_name", addrName);
	
	
	if (returnPage.indexOf("?") <=0 && returnPage.indexOf("addr") <=0) {
		returnPage = returnPage + "?addr_id="+addrId + "&addr_name="+ addrName;
	} else{
		returnPage = returnPage + "&addr_id="+addrId + "&addr_name="+ addrName;
	}
	
//	if (returnPage.indexOf("&") >=0 && returnPage.indexOf("addr") <=0) {
//	}
		

	mainView.router.loadPage(returnPage);
	
	
	
}

/*
 * 点击"确定",返回之前的页面
 * 		1. 修改默认地址  2.把 该点选地址 传回 下单页
 */
function goBackToZhongDianGongPage(){
	
	
	var addrId = localStorage['default_addr_id'];
	var addrName = localStorage['default_addr_name'];
	
	var returnPage = "";
	for (var i =1; i < 5; i++) {	// 判断前一页是不是 下单页,如果是,则作为 返回页
		var historyPage = mainView.history[mainView.history.length-i];
		
		if (historyPage == undefined) continue;
		
		if (historyPage.indexOf("order-form-zhongdiangong") >= 0 ||
			historyPage.indexOf("order-list-shendubaojie-yuyue") >= 0 ||
			
			//2015-11-6 15:17:49    返回 历史订单 添加页面
			historyPage.indexOf("history/history-zhongdiangong") >=0  ||
			
			historyPage.indexOf("history/history-am") >=0 
			) {
			returnPage = historyPage;
			break;
		}

	}
	
	if (returnPage == "") return;
	
	returnPage = replaceParamVal(returnPage, "addr_id", addrId);
	returnPage = replaceParamVal(returnPage, "addr_name", addrName);
	
	
//	if (returnPage.indexOf("?") >=0 && returnPage.indexOf("addr") <=0) {
//		returnPage = returnPage + "?addr_id="+addrId + "&addr_name="+ addrName;
//	} 
	
	if (returnPage.indexOf("?") <=0 && returnPage.indexOf("addr") <=0) {
		returnPage = returnPage + "?addr_id="+addrId + "&addr_name="+ addrName;
	} else{
		returnPage = returnPage + "&addr_id="+addrId + "&addr_name="+ addrName;
	}
	
//	if (returnPage.indexOf("&") <=0 && returnPage.indexOf("addr") <=0) {
//		returnPage = returnPage + "&addr_id="+addrId + "&addr_name="+ addrName;
//	}

	mainView.router.loadPage(returnPage);
	
}

