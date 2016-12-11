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


//地址添加
myApp.onPageInit('mine-addr-list', function (page) {
	var userId = localStorage['user_id'];
	$$(".all-button9").on("click",function(){
		mainView.router.loadPage("user/mine-add-addr.html?addr_id=0");
	});
});


function clickToSetDefault(obj,addrId){
	//当前被点击的地址
	var addrNameClick= $$(obj).find("#mine-add-addr-link").text();

	if(addrNameClick.indexOf("默认")>0){
		localStorage.setItem("default_addr_name",addrNameClick.replace("[默认]", "").trim());
		localStorage.setItem("default_addr_id",addrId);
		goBackToOrder(localStorage['default_addr_id'], localStorage['default_addr_name']);
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
					$$(".addr-dizhi").each(function(key, index) {
						var addrIdObj = $$(this).find("#addr_id").val();
						var addrNameObj = $$(this).find("#mine-add-addr-link");
						var addrNameHtml = addrNameObj.html();
						if (addrId == addrIdObj) {
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
					goBackToOrder(localStorage['default_addr_id'], localStorage['default_addr_name']);
				}
			});
		},
		//  点击 "取消/返回",不设置默认地址,但是 把该点击地址 作为 选中项，传回上一页
		function (){
			goBackToOrder(addrId,addrNameClick);
		}
	);
}

//点选 ‘取消/返回’  1.不修改默认地址  2.把该地址 传回下单页
function goBackToOrder(addrId, addrName){
	console.log("goBackToOrder addr_id = " + addrId);
	console.log(" goBackToOrder addr_name = " + addrName);
	var returnPage = "";
	for (var i =1; i < 5; i++) {	// 判断前一页是不是 下单页,如果是,则作为 返回页
		var historyPage = mainView.history[mainView.history.length-i];

		if (historyPage == undefined) continue;
		console.log("historyPage = " + historyPage);
		if (historyPage.indexOf("order-hour-choose") >= 0 ||
			historyPage.indexOf("order-deep-choose") >= 0 
		) {
			returnPage = historyPage;
			break;
		}
	}

	if (returnPage == "") return;

	sessionStorage.setItem('addr_id', addrId);
	sessionStorage.setItem('addr_name', addrName);
	console.log("addr_id = " + sessionStorage.getItem("addr_id"));
	console.log("addr_name = " + sessionStorage.getItem("addr_name"));
	mainView.router.loadPage(returnPage);

}

//删除地址
function delAddr(obj){
	var id=$$(obj).next().val();
	if(id==null || id=='' || id==undefined) return false;
	$$.ajax({
		type:"get",
		url:siteAPIPath + "user/delete_addr.json",
		data:{"addr_id":id},
		success:function(){
			
		}
	});
}
