//检测必备的数据是否正确。

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
}