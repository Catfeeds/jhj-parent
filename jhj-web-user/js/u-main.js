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
	

	
	if (toolbarName == 'toolbar-index') {
		toolBarIndex.addClass("active");
		toolBarIndex.css("color", "#FB571E");
		
		toolBarOrder.removeClass("active");
		toolBarOrder.css("color", "#FFF");
		
		toolBarAm.removeClass("active");
		toolBarAm.css("color", "#FFF");
		
		toolBarMine.removeClass("active");
		toolBarMine.css("color", "#FFF");		
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
		
		
		if (localStorage.getItem('user_id') == null) {
			console.log(localStorage.getItem('mobile') + "----");
			mainView.router.loadPage("login.html");
			return;
		}
		
		if (localStorage.getItem('am_id') == null || localStorage.getItem('am_mobile') == null) {
			
			myApp.confirm('您还没有添加地址，点击确定前往添加地址立刻获得家庭助理，点击返回留在此页', "", function () {
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
		
		if (localStorage.getItem('user_id') == null) {
			mainView.router.loadPage("login.html");
			return;
		}				
	}
	
	mainView.router.loadPage(url);

};//检测必备的数据是否正确。

//检测
if (localStorage.getItem('service_type_list') == null) {
	serviceTypeList();
}
//检测附加服务列表是否加载
if (localStorage.getItem('service_type_addons_list') == null) {
	serviceTypeAddonsList();
}

//检测用户是否已有相应的助理
console.log(localStorage.getItem('am_id') );
console.log(localStorage.getItem('am_mobile') );
if (localStorage.getItem('am_id') == null || localStorage.getItem('am_mobile') == null) {
	getAm();
}

function serviceTypeList() {
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "dict/get_serviceType.json?",
		dataType : "json",
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
//	console.log(localStorage.getItem("service_type_addons_list"));
}


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
	if (user_id == undefined || user_id ==  "") return false;
	

	$$.ajax({
		type:"get",
		url  : siteAPIPath+"user/user_get_am_id.json?user_id="+user_id,
        cache: true,
		success: function(datas,status,xhr){
			console.log(datas);
			var result = JSON.parse(datas);
			var amData = result.data;
			
			if (typeof amData == 'undefiend' ) return ;
			if (amData == "" || amData == null) return ;
			
			var amId = amData.am_id;
			var amMobile = amData.am_mobile;
			
			localStorage.setItem('am_id', amId);
			localStorage.setItem('am_mobile', amMobile);
		}
	});
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
};	 
/*********** 符合 下单 时间选择逻辑 的插件 (start) ***********/
	/*
	 *  使用 说明：
	 *     页面需要 提供   
	 *     
	 * 		<input type="text" id="serviceDate" name="serviceDate" >
	 *  
	 */

function dateSelect () {

		var serviceDateValues = [];
		
		var start =0;
		//当前整点小时数
		var nowHour = moment().hour();
		
//		var nowHour = 11;
		if (nowHour >= 16) start = 1;
		
		for (var i = start; i < 14; i++) {
			var tempDay = moment().add(i,'days').format('YYYY-MM-DD');
			serviceDateValues.push(tempDay);
		}
		
		var serviceHoursValues = [];
		// 可选 小时 的  最大范围 （考虑 时长）， 为  6 ~ 19
		for (var i =6; i < 20; i++) {
			var tempHour = moment({ hour:i}).format('HH');
			serviceHoursValues.push(tempHour);
		}
		
		/*
		 * 2. 今天下单，判断当前 的时间点  
		 * 
		 */

		// 当天  0~6点 （6点多）， 时间 起止范围    10~19 （不考虑服务时长）
		var dis1 = [10,11,12,13,14,15,16,17,18,19]; 
		
		// 当天  7 点 ~ 12点（12点多） ，时间起止范围  11~ 16 
		var dis2 = [11,12,13,14,15,16];
		
		//当天 13 ~ 15点（！！！不能是 15点多 ） ，时间 起止范围  17~19
		var dis3 = [17,18,19];
		
		// 当天 16~ 19点， 起止范围 第二天  6~ 19
		var dis4 = [6,7,8,9,10,11,12,13,14,15,16,17,18,19];
		
		//当天 20 ~23点 ，时间起止范围 为 第二天  10~ 19
		var dis5 = [10,11,12,13,14,15,16,17,18,19]; 
		
		if(nowHour < 7){
			var serviceDateVendors = { 
					serviceDates : serviceDateValues,
					serviceHours : dis1,
					serviceMins : ['00', '30']
			};
		}
		
		if(nowHour > 6 && nowHour <= 12){
			var serviceDateVendors = { 
					serviceDates : serviceDateValues,
					serviceHours : dis2,
					serviceMins : ['00', '30']
			};
		}
		
		if(nowHour >12 && nowHour <= 15){
			var serviceDateVendors = { 
					serviceDates : serviceDateValues,
					serviceHours : dis3,
					serviceMins : ['00', '30']
			}
		}
		
		if(nowHour >= 16 && nowHour <= 19){
			var serviceDateVendors = { 
					serviceDates : serviceDateValues,
					serviceHours : dis4,
					serviceMins : ['00', '30']
			}  
		}
		
		if(nowHour > 19 && nowHour <=23){
			var serviceDateVendors = { 
					serviceDates : serviceDateValues,
					serviceHours : dis5,
					serviceMins : ['00', '30']
			}  
		}
		
		//页面加载时。默认提供 一个 时间值
		$$("#serviceDate").val(serviceDateVendors.serviceDates[0] + "  " +serviceDateVendors.serviceHours[0] + ":"  + serviceDateVendors.serviceMins[0])

		
		
		var b =true;
		var a = true;
		
		var c = true;
		var d = true;
		
		var e = true;
		var f = true; 
		
	   var pickerInline = myApp.picker({
			input: '#serviceDate',		
			toolbarCloseText:'确认',
			onChange: function(p, values, displayValues){
				
				//时间为 19:00，则不能 选  19:30
				if(values[1] == 19){
					p.cols[3].setValue('00');
				}
				
				// 在今天  选  今天
				if(moment(values[0]).fromNow().indexOf("前")>0){
					//第一次加载，根据当前时间 加载 不同 的 时间段
//					console.log("e="+e);
//					console.log("f="+f);
					if(!f) return;
					
					if(e && f){
						// 0~6 点
						if(nowHour < 7){
							p.cols[1].replaceValues(dis1);
						}
						
						if(nowHour >=7 && nowHour <=12){
							
							if(nowHour < dis2[0]){
								var index = dis2.indexOf(nowHour+4);
								p.cols[1].replaceValues(dis2.slice(index));
							}else{
								var index=dis2.indexOf(nowHour);
								p.cols[1].replaceValues(dis2.slice(index+4));
							}
							
						}
						
						if(nowHour >12 && nowHour <= 15){
							
							var index=dis3.indexOf(nowHour+4);
							p.cols[1].replaceValues(dis3.slice(index));
						}
						
						if(nowHour >15 && nowHour <= 19){
							p.cols[1].replaceValues(dis4);
						}
						if(nowHour > 19 && nowHour <= 23){
							p.cols[1].replaceValues(dis5);
						}
							
						e = false;
						return;
					}
					
					// 经过 了    “今天 选明天 之后”，回滚时，需要重新加载  当前时间 对应 的 时间段
					while(f && !e && !d){
						if(nowHour < 7){
							p.cols[1].replaceValues(dis1);
						}
						
						if(nowHour >=7 && nowHour <=12){
							if(nowHour < dis2[0]){
								var index = dis2.indexOf(nowHour+4);
								p.cols[1].replaceValues(dis2.slice(index));
							}else{
								var index=dis2.indexOf(nowHour);
								p.cols[1].replaceValues(dis2.slice(index+4));
							}
						}
						
						if(nowHour >12 && nowHour <= 15){
							var index=dis3.indexOf(nowHour+4);
							p.cols[1].replaceValues(dis3.slice(index));
						}
						
						if(nowHour >15 && nowHour <= 19){
							p.cols[1].replaceValues(dis4);
						}
						
						if(nowHour > 19 && nowHour <= 23){
							p.cols[1].replaceValues(dis5);
						}
						
						d = true;
					}
					
				}
				
				// 今天 选 明天  
				var selectDay = moment(moment(values[0])).format("YYYY-MM-DD");
				var  inDay = moment(selectDay).toNow();
				
				if(inDay.indexOf("前")>0 && nowHour < 16){
					
//					console.log("c="+c);
//					console.log("d="+d);
					if(c && d){
						p.cols[1].replaceValues(dis4);
						c = true;
						d = false;
					}
					while(!c && !d){
						p.cols[1].replaceValues(dis4);
						c = false;
						d = true;
					}
				}
				
				//今天 20点  之后 选  明天 和 后天 	
				if(moment(values[0]).toNow().indexOf("天")>0){
					while(b && a){
						p.cols[1].replaceValues(dis4);
						b =false;
					}
					while(!b && !a){
						p.cols[1].replaceValues(dis4);
						b = true;
						a = false;
					}
					
				}else{
					if(nowHour < 7){
						while(a && !b && d){
							p.cols[1].replaceValues(dis1);
							a = false;
						}
						while(!a && b && d){
							p.cols[1].replaceValues(dis1);
							a = true;
						}
					}
					
					if(nowHour > 6 && nowHour <= 12){
						while(a && !b && d){
							if(nowHour < dis2[0]){
								var index = dis2.indexOf(nowHour+4);
								p.cols[1].replaceValues(dis2.slice(index));
							}else{
								var index=dis2.indexOf(nowHour);
								p.cols[1].replaceValues(dis2.slice(index+4));
							}
							a = false;
						}
						while(!a && b && d){
							if(nowHour < dis2[0]){
								var index = dis2.indexOf(nowHour+4);
								p.cols[1].replaceValues(dis2.slice(index));
							}else{
								var index=dis2.indexOf(nowHour);
								p.cols[1].replaceValues(dis2.slice(index+4));
							}
							a = true;
						}
					}
					
					if(nowHour >12 && nowHour <= 15){
						while(a && !b && d){
							var index=dis3.indexOf(nowHour+4);
							p.cols[1].replaceValues(dis3.slice(index));
							a = false;
						}
						while(!a && b && d){
							var index=dis3.indexOf(nowHour+4);
							p.cols[1].replaceValues(dis3.slice(index));
							a = true;
						}
					}
					
					if(nowHour >= 16 && nowHour <= 19){
						while(a && !b && d){
							p.cols[1].replaceValues(dis4);
							a = false;
						}
						while(!a && b && d){
							p.cols[1].replaceValues(dis4);
							a = true;
						}
					}
					
					if(nowHour > 19 && nowHour <=23){
						while(a && !b && d){
							p.cols[1].replaceValues(dis5);
							a = false;
						}
						while(!a && b && d){
							p.cols[1].replaceValues(dis5);
							a = true;
						}
					}
				}
			},
			formatValue: function (p, values, displayValues) {
				
				return values[0] + "  " + values[1] +':' +values[2] ;
		    },
			cols: [

			        {
			            values: serviceDateVendors.serviceDates,
			            width: 160,
			        },
			        
			        {
			            values: serviceDateVendors.serviceHours,
			        },			 
			        {
			            divider: true,
			            content: ':'
			        },
			        {
			            values: serviceDateVendors.serviceMins,
			        },			        
			 ]
		});
};		
/*********** 符合 下单 时间选择逻辑 的插件 (end) ***********/	

//当 用户 不在当前页面 时，关闭 时间 选择插件 。(解决：返回上一页面，时间选择不消失的问题)
window.onhashchange = function(){
	
	 $$(".close-picker").click();
};	


