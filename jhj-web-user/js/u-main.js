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
};//检测必备的数据是否正确。

/*
 * 2015-11-26 15:41:16  
 * 		处理每次更新的缓存问题
 * 		 如：每次新增 新的 助理订单类型，有些客户端，由于下方的检测，而不能获得最新的  service_type_list
 * 	
 * 		决定: 去掉检测，每次用户点击进入微网站,都去获得最新的 资源~ 可能会增加 几十 Kb的 网络开销~	
 *    
 */
//serviceTypeList();
//serviceTypeAddonsList();


//partnerServiceTypeList(); 

//检测
//if (localStorage.getItem('service_type_list') == null) {
//}
//检测附加服务列表是否加载
//if (localStorage.getItem('service_type_addons_list') == null) {
//}

//检测用户是否已有相应的助理
//console.log(localStorage.getItem('am_id') );
//console.log(localStorage.getItem('am_mobile') );
//if (localStorage.getItem('am_id') == null || localStorage.getItem('am_mobile') == null) {
//	getAm();
//}

//function serviceTypeList() {
//	$$.ajax({
//		type : "GET",
//		url : siteAPIPath + "dict/get_serviceType.json?",
//		dataType : "json",
//		cache : true,
//		statusCode : {
//				200 : ajaxServiceTypeListSuccess,
//				400 : ajaxError,
//				500 : ajaxError
//			}
//	});
//}

//function serviceTypeAddonsList() {
//	$$.ajax({
//		type : "GET",
//		url : siteAPIPath + "dict/get_service_addons_type.json?",
//		dataType : "json",
//		cache : true,
//		statusCode : {
//			200 : ajaxServiceTypeAddonsListSuccess,
//			400 : ajaxError,
//			500 : ajaxError
//		}
//	});
//}

function ajaxServiceTypeListSuccess(data, textStatus, jqXHR) {
//	console.log("cityListSuccess");
	var result = JSON.parse(data.response);
	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
	
	var serviceTypeList = JSON.stringify(result.data);
	
	localStorage.setItem('service_type_list', serviceTypeList);
}

//function ajaxServiceTypeAddonsListSuccess(data, textStatus, jqXHR) {
//	var result = JSON.parse(data.response);
//	if (result.status == "999") {
//		myApp.alert(result.msg);
//		return;
//	}
//	var serviceTypeList = JSON.stringify(result.data);
//	localStorage.setItem('service_type_addons_list', serviceTypeList);
////	console.log(localStorage.getItem("service_type_addons_list"));
//}


function getServiceAddonName(serviceAddonId) {
	var serviceTypeAddonsList = JSON.parse(localStorage.getItem("service_type_addons_list"));
	var serviceAddonName = "";
	$$.each(serviceTypeAddonsList, function(i,item){
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
		if (item.service_addon_id == serviceAddonId) {
			serviceAddonPrice = item.price;

		}
	});
	return serviceAddonPrice;
}
function getServiceAddonDefaultNum(serviceAddonId) {
	var serviceTypeAddonsList = JSON.parse(localStorage.getItem("service_type_addons_list"));
	var serviceAddonDefaultNum = "";
	$$.each(serviceTypeAddonsList, function(i,item){
		if (item.service_addon_id == serviceAddonId) {
			serviceAddonDefaultNum = item.default_num;
		}
	});
	return serviceAddonDefaultNum;
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
}

//获取助理信息
function getAm() {

	var user_id = localStorage.getItem('user_id');
//	alert(user_id);
	if (user_id == undefined || user_id ==  "") return false;
	

	$$.ajax({
		type:"get",
		url  : siteAPIPath+"user/user_get_am_id.json?user_id="+user_id,
        cache: true,
        async:false,
		success: function(datas,status,xhr){
//			console.log(datas);
			var result = JSON.parse(datas);
			var amData = result.data;
			
//			alert(amData);
			if (typeof amData == 'undefiend' ) return ;
			if (amData == "" || amData == null) return ;
			
			var amId = amData.am_id;
			var amMobile = amData.am_mobile;
			
			localStorage.setItem('am_id', amId);
			localStorage.setItem('am_mobile', amMobile);
		}
	});
}


/**
 *  2016年3月14日16:53:31  新首页，加载 服务类别
 */
function partnerServiceTypeList() {
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "partService/part_service_tree_list.json?",
		dataType : "json",
		cache : true,
		statusCode : {
			200 : partServiceTreeSuccess,
			400 : ajaxError,
			500 : ajaxError
		}
	});
}

function partServiceTreeSuccess(data, textStatus, jqXHR) {
	var result = JSON.parse(data.response);
	
	var serviceTypeList = JSON.stringify(result.data);
	
	localStorage.setItem('part_service_type_tree_list', serviceTypeList);
}

;/**
 * 只允许输入数字
 */
function inputNum(obj) {
	var tmptxt=obj.val();
	tmptxt = tmptxt.replace(/[^\d\.]/g,'');
	obj.val(tmptxt);
}

/**
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
}

/**
 * 替换url中的参数
 * @param value
 * @returns {Boolean}
 */
function replaceParamVal(url, paramName, replaceWith) {
    var re = eval('/(' + paramName + '=)([^&]*)/gi');
    var nUrl = url.replace(re, paramName + '=' + replaceWith);
    return nUrl;
}

/*
 * 去除字符串 中的 中文
 * 
*/

function delChinese(someString){
	var reg=/[\u4E00-\u9FA5]/g;
		var result = someString.replace(reg,'');
	
	return result;
	
}

/*
 * 处理 人民币，数字   ￥123.322  -->  123.322
 */
function delSomeString(someString){
	
	var result = someString.substr(1,someString.length);
	
	return result;
}



;/**
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