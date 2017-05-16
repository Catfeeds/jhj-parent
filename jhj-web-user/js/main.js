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
	precompileTemplates : true,
	template7Pages : true,
	pushState : true,
	cache : false,
	modalTitle : "提示",
	modalButtonOk : '确定',
	modalButtonCancel : '返回',
	template7Data : {},
	material : false,
	allowDuplicateUrls : true,
	domCache : true,
	// Hide and show indicator during ajax requests
	onAjaxStart : function(xhr) {
		myApp.hideIndicator();
	},
	onAjaxComplete : function(xhr) {
		myApp.hideIndicator();
	},
	preroute : function(view, options) {

		if (!isLogin() && options.url.indexOf('order/order-hour-choose.html') >= 0 ) {
			view.router.loadPage('login.html');
			return false;
		} else if (!isLogin() && options.url.indexOf('order/order-deep-choose.html') >= 0 ) {
			view.router.loadPage('login.html');
			return false;
		} else if (!isLogin() && options.url.indexOf('order/order-custom-choose.html') >= 0 ) {
			var serviceTypeId = GetUrlParamValue(options.url, 'service_type_id');
			var nextUrl = "order/order-custom-choose.html?service_type_id="+serviceTypeId;
			console.log("nextUrl = " + nextUrl);
			sessionStorage.setItem("logined_next_url", nextUrl);
			view.router.loadPage('login.html');
			return false;
		} else if (!isLogin() && options.url == 'order/order-list.html') {
			view.router.loadPage('login.html');
			return false;
		} else if (!isLogin() && options.url == 'user/mine.html') {
			view.router.loadPage('login.html');
			return false;
		} else if (!isLogin() && options.url == 'user/charge/mine-charge-list.html') {
			view.router.loadPage('login.html');
			return false;
		} else if (!isLogin() && options.url == 'user/serviceCharge/order-service-charge.html') {
			view.router.loadPage('login.html');
			return false;
		} else if(!isLogin() && options.url == 'order/period/period-order-confirm.html'){
			view.router.loadPage('login.html');
			return false;
		}else if (!isLogin() && options.url == 'order/order-list.html') {
			view.router.loadPage('login.html');
			return false;
		}else if (!isLogin() && options.url == 'user/mine-addr-list.html') {
			view.router.loadPage('login.html');
			return false;
		}
	}

});

// 定义Dom7框架工具
var $$ = jQuery = Dom7;

// 初始化主视图
var mainView = myApp.addView('.view-main', {
	dynamicNavbar : true,
// domCache: true
});

function isLogin() {
	return typeof localStorage['user_id'] != 'undefined' && localStorage['user_id'] != '';
}

var ajaxError = function(data, textStatus, jqXHR) {
	// We have received response and can hide activity indicator
	myApp.hideIndicator();
	myApp.alert('网络繁忙,请稍后再试.');
};

function toolBarHref(url, toolbarName) {
	
	var toolBars = ['toolbar-index', 'toolbar-order', 'toolbar-charge', 'toolbar-mine'];
	
	$$.each(toolBars,function(n,value) {  
		if (value == toolbarName) {
			$$('#' + value).addClass("active");
			$$('#' + value).css("color", "#FB571E");
		} else {
			$$('#' + value).removeClass("active");
			$$('#' + value).css("color", "#000");
		}
	});

	mainView.router.loadPage(url);
}



function GetUrlParamValue(url, name) {   
	console.log("GetUrlParamValue");
	console.log("url = " + url);
	var v  = "";
   if (url.indexOf("?") != -1) {   
      var str = url.substr(url.indexOf("?") + 1);   
      strs = str.split("&");
      
      for(var i = 0; i < strs.length; i ++) { 
    	  if (strs[i].split("=")[0] == name) {
    		  v = strs[i].split("=")[1];
    		  return v;
    	  }
      }   
      
   }   
   return v;   
}   

function isWeiXin() {
	var ua = window.navigator.userAgent.toLowerCase();
	if (ua.match(/MicroMessenger/i) == 'micromessenger') {
		return true;
	} else {
		return false;
	}
}