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
}).trigger(); //And trigger it right away;//检测必备的数据是否正确。

//检测
if (localStorage.getItem('service_type_list') == null) {
	serviceTypeList();
}
//检测附加服务列表是否加载
if (localStorage.getItem('service_type_addons_list') == null) {
	serviceTypeAddonsList();
}

if (localStorage.getItem('service_type_addons_list') == null) {
	serviceTypeAddonsList();
}

function serviceTypeList() {
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "dict/get_serviceType.json?",
		dataType : "json",
		async:false,
		cache : true,
		statusCode : {
				200 : ajaxServiceTypeListSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
	});
}

function serviceTypeAddonsList() {
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "dict/get_service_addons_type.json?",
		dataType : "json",
		async:false,
		cache : true,
		statusCode : {
			200 : ajaxServiceTypeAddonsListSuccess,
			400 : ajaxError,
			500 : ajaxError
		}
	});
}

function ajaxServiceTypeListSuccess(data, textStatus, jqXHR) {
//	console.log("cityListSuccess");
	var result = JSON.parse(data.response);
	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
	var serviceTypeList = JSON.stringify(result.data);
	localStorage.setItem('service_type_list', serviceTypeList);
//	console.log(localStorage.getItem("service_type_list"));
//	var cityList = JSON.parse(localStorage.getItem("city_list"));
//	
//	$$.each(cityList, function(i,item){
//		console.log(item.city_id + "---" + item.name);
//	});
}

function ajaxServiceTypeAddonsListSuccess(data, textStatus, jqXHR) {
	var result = JSON.parse(data.response);
	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
	var serviceTypeList = JSON.stringify(result.data);
	localStorage.setItem('service_type_addons_list', serviceTypeList);
	console.log(localStorage.getItem("service_type_addons_list"));
}


function getServiceAddonName(serviceAddonId) {
	var serviceTypeAddonsList = JSON.parse(localStorage.getItem("service_type_addons_list"));
	var serviceAddonName = "";
	$$.each(serviceTypeAddonsList, function(i,item){
//		console.log(item.city_id + "---" + item.name);
		if (item.service_addon_id == serviceAddonId) {
			serviceAddonName = item.name;

		}
	});
	return serviceAddonName;
}

function getServiceAddonPrice(serviceAddonId) {
	var serviceTypeAddonsList = JSON.parse(localStorage.getItem("service_type_addons_list"));
	var serviceAddonPrice = "";
	$$.each(serviceTypeAddonsList, function(i,item){
//		console.log(item.city_id + "---" + item.name);
		if (item.service_addon_id == serviceAddonId) {
			serviceAddonPrice = item.price;

		}
	});
	return serviceAddonPrice;
}


//显示星座名称
function getAstroName(astro){
	
	var astroName = "";
	
	switch(astro){
	case 0:
		astroName = "魔羯座";
		break;
	case 1:
		astroName = "水瓶座";
		break;
	case 2:
		astroName = "双鱼座";
		break;
	case 3:
		astroName = "白羊座";
		break;
	case 4:
		astroName = "金牛座";
		break;
	case 5:
		astroName = "双子座";
		break;
	case 6:
		astroName = "巨蟹座";
		break;
	case 7:
		astroName = "狮子座";
		break;
	case 8:
		astroName = "处女座";
		break;
	case 9:
		astroName = "天秤座";
		break;
	case 10:
		astroName = "天蝎座";
		break;
	case 11:
		astroName = "射手座";
		break;
	default:
		astroName = "";
			
	}
	
	return astroName;
};/**
 * 判断是否是数字
 * @param value
 * @returns {Boolean}
 */
function isNum(value){
    if( value != null && value.length>0 && isNaN(value) == false){
        return true;
    }else{
        return false;
    }
};/**
 * 检查字符串是否为合法手机号码
 * @param {String} 字符串
 * @return {bool} 是否为合法手机号码
 */
function isPhone(aPhone) {
	var bValidate = RegExp(
			/^(0|86|17951)?(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|19[0-9])[0-9]{8}$/)
			.test(aPhone);
	if (bValidate) {
		return true;
	} else
		return false;
}

/**
 * 检查字符串是否为合法email地址
 * @param {String} 字符串
 * @return {bool} 是否为合法email地址
 */
function isEmail(aEmail) {
	var bValidate = RegExp(
			/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/)
			.test(aEmail);
	if (bValidate) {
		return true;
	} else
		return false;
}