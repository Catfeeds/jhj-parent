//参数设置
var host = window.location.host;
var appName = "jhj-app/app/";
var localUrl = "http://" + host;
var siteAPIPath = localUrl + "/" + appName; //正式

var siteApp = "am";
var userLoggedIn = false;

// 初始化应用
var myApp = new Framework7({
	precompileTemplates: true,
    template7Pages: true,
    pushState: true,
    cache: false,
    modalTitle: "提示",
    template7Data: {},
    // Hide and show indicator during ajax requests
    onAjaxStart: function (xhr) {
            myApp.showIndicator();
    },
    onAjaxComplete: function (xhr) {
            myApp.hideIndicator();
    },    
    preroute: function (view, options) {

             if(!isLogin() && options.url!='login.html'){
//                     console.log('must login');
                     view.router.loadPage('login.html');
                     return false;
             }
             
             if (options != undefined && options.url == "") {
            	 view.router.loadPage('order/order-list.html');
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
        return typeof localStorage['am_id']!='undefined' && localStorage['am_id']!='';
}

var ajaxError = function(data, textStatus, jqXHR) {	
    	// We have received response and can hide activity indicator
        myApp.hideIndicator();		
    	myApp.alert('网络繁忙,请稍后再试.');
};


var getParameterByName = function(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(window.location.href);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function toolBarHref(url, toolbarName) {
	console.log(toolbarName);
	var toolBarOrder = $$('#toolbar-order');
	var toolBarUser = $$('#toolbar-user');
	var toolBarMine = $$('#toolbar-mine');
	
	var toolBarRemind = $$('#toolbar-remind');
	
	
	if (toolbarName == 'toolbar-order') {
		toolBarOrder.addClass("active");
		toolBarOrder.css("color", "#EF7B00");
		
		toolBarUser.removeClass("active");	
		toolBarUser.css("color", "#FFF");
		
		toolBarMine.removeClass("active");
		toolBarMine.css("color", "#FFF");
		
		toolBarRemind.removeClass("active");
		toolBarRemind.css("color","#FFF");
	}
	
	if (toolbarName == 'toolbar-user') {

		toolBarUser.addClass("active");
		toolBarUser.css("color", "#EF7B00");
		
		
		toolBarOrder.removeClass("active");
		toolBarOrder.css("color", "#FFF");
		
		toolBarMine.removeClass("active");
		toolBarMine.css("color", "#FFF");

		toolBarRemind.removeClass("active");
		toolBarRemind.css("color","#FFF");
		
		
	}
	
	if (toolbarName == 'toolbar-mine') {
		toolBarMine.addClass("active");		
		toolBarMine.css("color", "#EF7B00");
		
		toolBarOrder.removeClass("active");
		toolBarOrder.css("color", "#FFF");
		
		toolBarUser.removeClass("active");
		toolBarUser.css("color", "#FFF");
		

		toolBarRemind.removeClass("active");
		toolBarRemind.css("color","#FFF");
	}
	
	if(toolbarName == 'toolbar-remind'){
		toolBarRemind.addClass("active");
		toolBarRemind.css("color", "#EF7B00");
		
		toolBarOrder.removeClass("active");
		toolBarOrder.css("color", "#FFF");
		
		toolBarUser.removeClass("active");
		toolBarUser.css("color", "#FFF");
		
		toolBarMine.removeClass("active");
		toolBarMine.css("color", "#FFF");
		
	}
	
	mainView.router.loadPage(url);

}


myApp.onPageInit('index', function (page) {
	mainView.router.loadPage("order/order-list.html");
}).trigger(); //And trigger it right away