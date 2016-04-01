//检测必备的数据是否正确。

/*
 * 2015-11-26 15:41:16  
 * 		处理每次更新的缓存问题
 * 		 如：每次新增 新的 助理订单类型，有些客户端，由于下方的检测，而不能获得最新的  service_type_list
 * 	
 * 		决定: 去掉检测，每次用户点击进入微网站,都去获得最新的 资源~ 可能会增加 几十 Kb的 网络开销~	
 *    
 */
//serviceTypeList();
serviceTypeAddonsList();


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

