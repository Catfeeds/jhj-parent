//参数设置
var host = window.location.host;
var appName = "jhj-app";
var localUrl = "http://" + host;
var siteAPIPath = localUrl + "/" + appName + "/app/";
var siteApp = "u";
var sitePath = localUrl + "/" + siteApp;
var userLoggedIn = false;

// 初始化应用
var myApp = new Framework7({
	precompileTemplates: true,
    template7Pages: true,
    pushState: true,
    cache: false,
    modalTitle: "提示",
    modalButtonOk:'确定',
    modalButtonCancel:'返回',
    template7Data: {},
    material: true,
    allowDuplicateUrls:true,
    domCache: true,
    // Hide and show indicator during ajax requests
    onAjaxStart: function (xhr) {
            myApp.showIndicator();
    },
    onAjaxComplete: function (xhr) {
            myApp.hideIndicator();
    },    
    preroute: function (view, options) {
    	
			/*    	 if(!isLogin() && options.url!='login.html'){
			//           console.log('must login');
			           view.router.loadPage('login.html');
			           return false;
			   }*/
    		//反向判断不好使！！！
             if(!isLogin() && options.url =='order/order-form-zhongdiangong.html'){
                     view.router.loadPage('login.html');
                     return false;
             }else if(!isLogin() && options.url =='order/order-list-shendubaojiezl.html'){
            	 view.router.loadPage('login.html');
            	 return false;
             }else if(!isLogin() && options.url =='order/order-am-faqiyuyue.html?service_type=3'){
			    	view.router.loadPage('login.html');
			    	return false;
			 }else if(!isLogin() && options.url =='user/mine.html'){
			    	view.router.loadPage('login.html');
			    	return false;
			 }else if(!isLogin() && options.url =='user/user-am-detail.html'){
		    	view.router.loadPage('login.html');
		    	return false;
		    }else if(!isLogin() && options.url =='order/order-hour-now-list.html'){
		    	view.router.loadPage('login.html');
		    	return false;
		    }else if(!isLogin() && options.url =='order/order-am-faqiyuyue.html?service_type=4'){
		    	view.router.loadPage('login.html');
		    	return false;
			 }else if(!isLogin() && options.url =='order/order-am-faqiyuyue.html?service_type=5'){
			    	view.router.loadPage('login.html');
			    	return false;
			 }else if(!isLogin() && options.url =='order/order-am-faqiyuyue.html?service_type=6'){
			    	view.router.loadPage('login.html');
			    	return false;
			 }else if(!isLogin() && options.url =='order/order-am-faqiyuyue.html?service_type=7'){
			    	view.router.loadPage('login.html');
			    	return false;
			 }else if(!isLogin() && options.url =='user/charge/mine-charge-list.html'){
			    	view.router.loadPage('login.html');
			    	return false;
			 }else if(!isLogin() && options.url =='huodong-detail.html'){
			    	view.router.loadPage('login.html');
			    	return false;
			 }else if(!isLogin() && options.url =='huodong-list.html'){
			    	view.router.loadPage('login.html');
			    	return false;
			 }else if(!isLogin()&& options.url == 'order/remind/order-remind-tixing.html'){
				 	view.router.loadPage('login.html');
			    	return false;
			 }else if(!isLogin() && options.url == 'order/order-am.html'){
				 	view.router.loadPage('login.html');
			    	return false;
			 }else if(!isLogin() && options.url == 'user/serviceCharge/order-service-charge.html'){
				 	view.router.loadPage('login.html');
			    	return false;
			 }else if(!isLogin() && options.url == 'xianshi-huodong.html'){
				 	view.router.loadPage('login.html');
			    	return false;
			 }else if(!isLogin() && options.url == 'order/order-am-faqiyuyue.html?service_type=10'){
				 	view.router.loadPage('login.html');
			    	return false;
			 }else if(!isLogin() && options.url == 'order/order-am-faqiyuyue.html?service_type=11'){
				 	view.router.loadPage('login.html');
			    	return false;
			 }
             
    }
        
        
});

// 定义Dom7框架工具
var $$ = jQuery = Dom7;

// 初始化主视图
var mainView = myApp.addView('.view-main', {
        dynamicNavbar: true,
        // domCache: true
});

function isLogin(){
        return typeof localStorage['user_id']!='undefined' && localStorage['user_id']!='';
}

var ajaxError = function(data, textStatus, jqXHR) {	
    	// We have received response and can hide activity indicator
        myApp.hideIndicator();		
    	myApp.alert('网络繁忙,请稍后再试.');
};



// 网上商城弹出框
/*$$(".index-shangcheng").click(function(){
	mainView.router.loadPage("http://kdt.im/OZjLSAKoJ");
    //alert("敬请期待");
})
*/
function toolBarHref(url, toolbarName) {
	var toolBarIndex = $$('#toolbar-index');
	var toolBarOrder = $$('#toolbar-order');
	var toolBarAm = $$('#toolbar-am');
	var toolBarMine = $$('#toolbar-mine');
	
	var toolBarRemind = $$('#toolbar-remind');
	
	if (toolbarName == 'toolbar-index') {
		toolBarIndex.addClass("active");
		toolBarIndex.css("color", "#FB571E");
		
		toolBarOrder.removeClass("active");
		toolBarOrder.css("color", "#FFF");
		
		toolBarAm.removeClass("active");
		toolBarAm.css("color", "#FFF");
		
		toolBarMine.removeClass("active");
		toolBarMine.css("color", "#FFF");	
		
		toolBarRemind.removeClass("active");
		toolBarRemind.css("color","#FFF");
	}
	
	if (toolbarName == 'toolbar-order') {
		toolBarOrder.addClass("active");
		toolBarOrder.css("color", "#FB571E");
		
		toolBarIndex.removeClass("active");
		toolBarIndex.css("color", "#FFF");
		
		toolBarAm.removeClass("active");
		toolBarAm.css("color", "#FFF");
		
		toolBarMine.removeClass("active");
		toolBarMine.css("color", "#FFF");
		
		toolBarRemind.removeClass("active");
		toolBarRemind.css("color","#FFF");
		
		if (localStorage.getItem('user_id') == null) {
			mainView.router.loadPage("login.html");
			return;
		}
	}
	
	if (toolbarName == 'toolbar-am') {
		
		toolBarAm.addClass("active");
		toolBarAm.css("color", "#FB571E");

		toolBarIndex.removeClass("active");
		toolBarIndex.css("color", "#FFF");
		
		toolBarOrder.removeClass("active");
		toolBarOrder.css("color", "#FFF");

		toolBarMine.removeClass("active");
		toolBarMine.css("color", "#FFF");		
		
		toolBarRemind.removeClass("active");
		toolBarRemind.css("color","#FFF");
		
		
		if (localStorage.getItem('user_id') == null) {
//			console.log(localStorage.getItem('mobile') + "----");
			mainView.router.loadPage("login.html");
			return;
		}
		
		if (localStorage.getItem('am_id') == 'null' || localStorage.getItem('am_mobile') == 'null') {
			
			myApp.alert('您还没有添加地址，点击确定前往添加地址立刻获得家庭助理', "", function () {
				mainView.router.loadPage("user/mine-add-addr.html?addr_id=0&return_url=user/user-am-detail.html");
		    });
			return;
		}			
	}
	
	if (toolbarName == 'toolbar-mine') {
				
		toolBarMine.addClass("active");
		toolBarMine.css("color", "#FB571E");
		
		toolBarOrder.removeClass("active");
		toolBarOrder.css("color", "#FFF");
		
		toolBarAm.removeClass("active");
		toolBarAm.css("color", "#FFF");
		
		toolBarIndex.removeClass("active");
		toolBarIndex.css("color", "#FFF");	
		
		toolBarRemind.removeClass("active");
		toolBarRemind.css("color","#FFF");
		
		if (localStorage.getItem('user_id') == null) {
			mainView.router.loadPage("login.html");
			return;
		}				
	}
	
	if(toolbarName == 'toolbar-remind'){
		
		toolBarRemind.addClass("active");
		toolBarRemind.css("color", "#FB571E");
		
		toolBarOrder.removeClass("active");
		toolBarOrder.css("color", "#FFF");
		
		toolBarAm.removeClass("active");
		toolBarAm.css("color", "#FFF");
		
		toolBarIndex.removeClass("active");
		toolBarIndex.css("color", "#FFF");	
		
		toolBarMine.removeClass("active");
		toolBarMine.css("color", "#FFF");	
		
		if (localStorage.getItem('user_id') == null) {
			console.log(localStorage.getItem('mobile') + "----");
			mainView.router.loadPage("login.html");
			return;
		}
		
	}
	
	mainView.router.loadPage(url);

}

var getParameterByName = function(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(window.location.href);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}