myApp.onPageBeforeInit('mine-addr-list', function (page) {
		
	var userId = localStorage['user_id'];

	var onAddrListSuccess = function(data, textStatus, jqXHR){
		myApp.hideIndicator();
	   	var result = JSON.parse(data.response);

		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}
		var userAddrList = result.data;

		var html = $$('#addr-list-part-li').html();
		var resultHtml = '';
		$$.each(userAddrList, function(i, item) {
			var htmlPart = html;
			htmlPart = htmlPart.replace(new RegExp('{addr_name}', "gm"),item.name + item.addr);
			htmlPart = htmlPart.replace(new RegExp('{addr_id}', "gm"), item.id);
			var is_default_name = "";
			if (item.is_default == 1) {
				is_default_name = "默认";
			}
			htmlPart = htmlPart.replace(new RegExp('{is_default_name}', "gm"), is_default_name);
			resultHtml += htmlPart;
		});
		
		if (resultHtml == '') {
			resultHtml = '<li style="color: #aaa">您还没有服务地址，现在就添加一个吧！</li>';
		}
		
		$$('.list-block ul').append(resultHtml);
	}	
	
	  //获取用户地址列表
	$$.ajax({
	        type:"GET",
	        url: siteAPIPath+"user/get_user_addrs.json?user_id="+userId,
	        dataType:"json",
	        cache:false,
	        statusCode: {
	         	200: onAddrListSuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    },
	});
	
    $$(".mine-add-addr-link").on("click",function(){
 		mainView.router.loadPage("user/mine-add-addr.html?addr_id=0");
 	});

});


function setAddrDefault(addrId) {
	console.log(addrId);
	var userId = localStorage['user_id'];

	var paramData = {};
	paramData.user_id = userId;
	paramData.addr_id = addrId;
	$$.ajax({
		type : "POST",
		url : siteAPIPath + "user/post_set_addr_default.json",
        data:{"user_id":userId, "addr_id":addrId},
		success : function() {
			
			//设置当前的为默认的地址.
			$$("input[class=swipeout]").each(function(key, index) {
				var addrIdObj = $$(this).find('input[name=addr_id]');
				var spanObj = $$(this).find('input[class=am-badge]');
				if (addrId == addrIdObj.val()) {
					spanObj.text("默认");
				} else {
					spanObj.text("");
				}
			});
			myApp.swipeoutClose($$(this));
			
			myApp.addNotification({
		        message: '操作成功',
		        button: {
		            text: '关闭',
		        },
		        hold:2000
		    });
		}
	});	
	
}