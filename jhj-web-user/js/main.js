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

		if (!isLogin() && options.url == 'order/order-hour-form.html') {
			view.router.loadPage('login.html');
			return false;
		} else if (!isLogin() && options.url == 'order/order-list-shendubaojiezl.html') {
			view.router.loadPage('login.html');
			return false;
		} else if (!isLogin() && options.url == 'user/mine.html') {
			view.router.loadPage('login.html');
			return false;
		} else if (!isLogin() && options.url == 'order/order-hour-now-list.html') {
			view.router.loadPage('login.html');
			return false;
		} else if (!isLogin() && options.url == 'user/charge/mine-charge-list.html') {
			view.router.loadPage('login.html');
			return false;
		} else if (!isLogin() && options.url == 'user/serviceCharge/order-service-charge.html') {
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

// 网上商城弹出框
/*
 * $$(".index-shangcheng").click(function(){
 * mainView.router.loadPage("http://kdt.im/OZjLSAKoJ"); //alert("敬请期待"); })
 */
function toolBarHref(url, toolbarName) {
	// 首页
	var toolBarIndex = $$('#toolbar-index');
	// 助理
	var toolBarAm = $$('#toolbar-am');
	// 活动
	var toolBarHuodong = $$('#toolbar-huodong');
	// 商城
	var toolBarYouzan = $$('#toolbar-youzan');
	// 我的
	var toolBarMine = $$('#toolbar-mine');

	if (toolbarName == 'toolbar-index') {
		toolBarIndex.addClass("active");
		toolBarIndex.css("color", "#FB571E");

		toolBarAm.removeClass("active");
		toolBarAm.css("color", "#FFF");

		toolBarHuodong.removeClass("active");
		toolBarHuodong.css("color", "#FFF");

		toolBarYouzan.removeClass("active");
		toolBarYouzan.css("color", "#FFF");

		toolBarMine.removeClass("active");
		toolBarMine.css("color", "#FFF");
	}

	if (toolbarName == 'toolbar-am') {
		toolBarIndex.removeClass("active");
		toolBarIndex.css("color", "#FFF");

		toolBarAm.addClass("active");
		toolBarAm.css("color", "#FB571E");

		toolBarHuodong.removeClass("active");
		toolBarHuodong.css("color", "#FFF");

		toolBarYouzan.removeClass("active");
		toolBarYouzan.css("color", "#FFF");

		toolBarMine.removeClass("active");
		toolBarMine.css("color", "#FFF");

		if (localStorage.getItem('user_id') == null) {
			mainView.router.loadPage("login.html");
			return;
		}

		// if (localStorage.getItem('am_id') == 'null' ||
		// localStorage.getItem('am_mobile') == 'null') {
		//			
		// myApp.alert('您还没有添加地址，点击确定前往添加地址立刻获得家庭助理', "", function () {
		// mainView.router.loadPage("user/mine-add-addr.html?addr_id=0&return_url=user/user-am-detail.html");
		// });
		// return;
		// }

	}

	if (toolbarName == 'toolbar-huodong') {

		toolBarIndex.removeClass("active");
		toolBarIndex.css("color", "#FFF");

		toolBarAm.removeClass("active");
		toolBarAm.css("color", "#FFF");

		toolBarHuodong.addClass("active");
		toolBarHuodong.css("color", "#FB571E");

		toolBarYouzan.removeClass("active");
		toolBarYouzan.css("color", "#FFF");

		toolBarMine.removeClass("active");
		toolBarMine.css("color", "#FFF");

		if (localStorage.getItem('user_id') == null) {
			mainView.router.loadPage("login.html");
			return;
		}

	}

	if (toolbarName == 'toolbar-youzan') {

		toolBarIndex.removeClass("active");
		toolBarIndex.css("color", "#FFF");

		toolBarAm.removeClass("active");
		toolBarAm.css("color", "#FFF");

		toolBarHuodong.removeClass("active");
		toolBarHuodong.css("color", "#FFF");

		toolBarYouzan.addClass("active");
		toolBarYouzan.css("color", "#FB571E");

		toolBarMine.removeClass("active");
		toolBarMine.css("color", "#FFF");

	}

	if (toolbarName == 'toolbar-mine') {

		toolBarIndex.removeClass("active");
		toolBarIndex.css("color", "#FFF");

		toolBarAm.removeClass("active");
		toolBarAm.css("color", "#FFF");

		toolBarHuodong.removeClass("active");
		toolBarHuodong.css("color", "#FFF");

		toolBarYouzan.removeClass("active");
		toolBarYouzan.css("color", "#FFF");

		toolBarMine.addClass("active");
		toolBarMine.css("color", "#FB571E");

		if (localStorage.getItem('user_id') == null) {
			mainView.router.loadPage("login.html");
			return;
		}
	}

	mainView.router.loadPage(url);
}

var getParameterByName = function(name) {
	name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
	var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex
			.exec(window.location.href);
	return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function isWeiXin() {
	var ua = window.navigator.userAgent.toLowerCase();
	if (ua.match(/MicroMessenger/i) == 'micromessenger') {
		return true;
	} else {
		return false;
	}
}