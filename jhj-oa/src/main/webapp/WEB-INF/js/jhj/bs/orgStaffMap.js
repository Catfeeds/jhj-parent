// 百度地图API功能
// 百度地图API功能
var map = new BMap.Map("allmap"); // 创建Map实例
map.centerAndZoom(new BMap.Point(116.404, 39.915), 12); // 初始化地图,设置中心点坐标和地图级别
map.addControl(new BMap.MapTypeControl()); // 添加地图类型控件
map.setCurrentCity("北京"); // 设置地图显示的城市 此项是必须设置的
map.enableScrollWheelZoom(false); // 开启鼠标滚轮缩放

// map.setMapStyle({style:"grayscale"});

var top_left_control = new BMap.ScaleControl({
	anchor : BMAP_ANCHOR_TOP_LEFT
});// 左上角，添加比例尺
var top_left_navigation = new BMap.NavigationControl(); // 左上角，添加默认缩放平移控件
var top_right_navigation = new BMap.NavigationControl({
	anchor : BMAP_ANCHOR_TOP_RIGHT,
	type : BMAP_NAVIGATION_CONTROL_SMALL
}); // 右上角，仅包含平移和缩放按钮
/*
 * 缩放控件type有四种类型:
 * BMAP_NAVIGATION_CONTROL_SMALL：仅包含平移和缩放按钮；BMAP_NAVIGATION_CONTROL_PAN:仅包含平移按钮；BMAP_NAVIGATION_
 */

map.addControl(top_left_control);
map.addControl(top_left_navigation);
map.addControl(top_right_navigation);

function loadStaffMapDatas() {
	// 清空
	map.clearOverlays();
	$("#offline-div").css('display', 'block');
	$("#order-list-table").css('display', 'none');
	// 发送ajax请求根据省ID获取城市ID
	
	var parentId = $("#parentId").val();
	var orgId = $("#orgId").val();
	var name = "";
	var status = $("#status").val();
	
	var params = {};
	params.parentId = parentId;
	params.orgId = orgId;
	params.name = name;
	params.status = status;
	
	$.ajax({
		type : 'GET',
		url : '/jhj-oa/bs/get_staff_map.json',
		dataType : 'json',
		cache : false,
		data : params,
		success : function(datas) {
			console.log(datas.data);
			var onlineDatas = datas.data.online;
			var offlineDatas = datas.data.offline;
			
			if (onlineDatas != undefined || onlineDatas != "") {
				setOnlines(onlineDatas);
			}
			
			if (offlineDatas != undefined || offlineDatas != "") {
				setOfflines(offlineDatas);
			}
			
		},
		error : function() {
			
		}
	});
}

function setOnlines(datas) {
	// 设置门店地图标注
	$.each(datas, function(i, obj) {
		var name = obj.name;
		var lng = obj.lng;
		var lat = obj.lat;
		
		if (lng == undefined || lng == "") return false;
		if (lat == undefined || lat == "") return false;
		// console.log(name + "----" + lng + "-----" + lat);
		var point = new BMap.Point(lng, lat);
		
		var label = new BMap.Label(name, {
			offset : new BMap.Size(-10, -30),
			position : point
		});
		
		label.setStyle({
			color : "black",
			fontSize : "12px",
			height : "30px",
			lineHeight : "20px",
			lineWeight : "50px",
			fontFamily : "微软雅黑",
			background : "url('../img/baidumap/staffImg.png') no-repeat center",
			border : '0',
			backgroundColor : "0"
		});
		
		var iconUrl = "";
		var poiStatus = obj.poi_status;
		if (poiStatus == 1) iconUrl = "/jhj-oa/img/circle-red.png";
		if (poiStatus == 2) iconUrl = "/jhj-oa/img/circle-blue.png";
		if (poiStatus == 3) iconUrl = "/jhj-oa/img/circle-green.png";
		
		var myIcon = new BMap.Icon(iconUrl, new BMap.Size(30, 20));
		var marker = new BMap.Marker(point, {
			icon : myIcon
		});
		
		marker.setLabel(label);
		
		map.addOverlay(marker);
		
		var poiName = obj.poi_name;
		if (poiName == undefined) poiName = "";
		var msgContent = "时间：" + obj.poi_time_str + "<br>位置：" + poiName;
		addClickHandler(msgContent, marker);
	});
}

function setOfflines(datas) {
	
	$("#offline-list").html("");
	
	var offlinelistHtml = "";
	$.each(datas, function(i, obj) {
		var name = obj.name;
		offlinelistHtml += "<li>" + name + "</li>"
	});
	$("#offline-list").html(offlinelistHtml);
}

loadStaffMapDatas();

$('.form_datetime').datepicker({
	format : 'yyyy-mm-dd',
	language : "zh-CN",
	autoclose : true,
	startView : 0,
	todayBtn : true
});

function loadStaffRoute() {
	
	var serviceDateStr = $("#serviceDateStr").val();
	if (serviceDateStr == undefined || serviceDateStr == "") {
		alert("请指定日期.");
		return false;
	}
	
	var staffId = $("#selectStaff").val();
	
	if (staffId == undefined || staffId == "") {
		alert("请指定一位服务人员进行轨迹追踪.");
		return false;
	}
	
	var mergeDistance = $("#mergeDistance").val();
	map.clearOverlays();
	$("#offline-div").css('display', 'none');
	$("#order-list-table").css('display', 'none');
	var params = {};
	params.service_date = serviceDateStr;
	params.staff_id = staffId;
	params.merge_distance = mergeDistance;
	
	// 读取地图信息
	$.ajax({
		type : 'GET',
		url : '/jhj-oa/bs/get_staff_trail.json',
		dataType : 'json',
		cache : false,
		data : params,
		success : function(datas) {
			console.log(datas.data);
			var trailDatas = datas.data;
			
			if (trailDatas != undefined || trailDatas != "" || trailDatas.length > 0) {
				setStaffRoute(trailDatas);
			}
		},
		error : function() {
			
		}
	});
	
	// 读取订单信息.
	$.ajax({
		type : 'GET',
		url : '/jhj-oa/order/get_list_by_date.json',
		dataType : 'json',
		cache : false,
		data : params,
		success : function(datas) {
			console.log(datas.data);
			
			var tableDatas = datas.data;
			if (tableDatas == undefined || tableDatas == "") return false;
			
			var htmlStr = "";
			$.each(tableDatas, function(i, obj) {
				htmlStr += "<tr>";
				htmlStr += "<td>" + obj.staff_name + "</td>";
				htmlStr += "<td>" + obj.staff_nums + "</td>";
				htmlStr += "<td>" + obj.order_type_name + "</td>";
				htmlStr += "<td>" + obj.service_date_str + "</td>";
				htmlStr += "<td>" + obj.service_hour + "</td>";
				htmlStr += "<td>" + obj.order_address + "</td>";
				htmlStr += "<td>" + obj.order_op_from_name + "</td>";
				htmlStr += "<td>" + obj.order_status_name + "</td>";
				htmlStr += "<td>" + obj.pay_type_name + "</td>";
				htmlStr += "<td>" + obj.order_pay + "</td>";
				htmlStr += "<tr>";
			});
			
			if (htmlStr != "") {
				$("#order-list-tbody").html(htmlStr);
				$("#order-list-table").css('display', 'block');
			}
			
		},
		error : function() {
			
		}
	});
}

function setStaffRoute(trailDatas) {
	
	// 如果只有一个点，则不需要折线，地图标一个点即可.
	if (trailDatas.length == 1) {
		var o = trailDatas[0];
		addMakerPoint(o.lng, o.lat, o.add_time_str);
		return false;
	}
	
	showPoly(trailDatas);
	
}

function showPoly(trailDatas) {
	
	var pointList = [];
	$.each(trailDatas, function(i, obj) {
		
		var p = new BMap.Point(obj.lng, obj.lat, obj.poi_name);
		pointList.push(p);
		
		addMakerPoint(obj.lng, obj.lat, obj.add_time_str, obj.poi_name);
		
	});
	
	var group = Math.floor(pointList.length / 11);
	var mode = pointList.length % 11;
	
	var driving = new BMap.DrivingRoute(map, {
		// renderOptions:{map: map, autoViewport: true},
		policy : BMAP_DRIVING_POLICY_AVOID_HIGHWAYS,
		onSearchComplete : function(results) {
			if (driving.getStatus() == BMAP_STATUS_SUCCESS) {
				var plan = driving.getResults().getPlan(0);
				var num = plan.getNumRoutes();
				for (var j = 0; j < num; j++) {
					var pts = plan.getRoute(j).getPath(); // 通过驾车实例，获得一系列点的数组
					var polyline = new BMap.Polyline(pts);
					map.addOverlay(polyline);
				}
			}
		}
	});
	for (var i = 0; i < group; i++) {
		var waypoints = pointList.slice(i * 11 + 1, (i + 1) * 11);
		driving.search(pointList[i * 11], pointList[(i + 1) * 11], {
			waypoints : waypoints
		});// waypoints表示途经点
	}
	if (mode != 0) {
		var waypoints = pointList.slice(group * 11, pointList.length - 1);// 多出的一段单独进行search
		driving.search(pointList[group * 11], pointList[pointList.length - 1], {
			waypoints : waypoints
		});
	}
	
}

// 信息窗口配置
var opts = {
	width : 250, // 信息窗口宽度
	height : 80, // 信息窗口高度
	title : "", // 信息窗口标题
	enableMessage : true
// 设置允许信息窗发送短息
};

function addMakerPoint(lng, lat, title, content) {
	
	var iconUrl = "/jhj-oa/img/circle-green.png";
	var myIcon = new BMap.Icon(iconUrl, new BMap.Size(30, 20));
	var point = new BMap.Point(lng, lat);
	var label = new BMap.Label(title, {
		offset : new BMap.Size(10, -20),
		position : point
	});
	label.setStyle({
		color : "black",
		fontSize : "12px",
		height : "25px",
		lineHeight : "20px",
		lineWeight : "50px",
		fontFamily : "微软雅黑",
		background : "url('../img/baidumap/staffImg.png') no-repeat center",
		border : '0',
		backgroundColor : "0"
	});
	
	var marker = new BMap.Marker(point, {
		icon : myIcon
	});
	
	marker.setLabel(label);
	
	map.addOverlay(marker);
	
	var msgContent = "时间：" + title + "<br>位置：" + content;
	addClickHandler(msgContent, marker);
}

function addClickHandler(content, marker) {
	marker.addEventListener("click", function(e) {
		openInfo(content, e)
	});
}

function openInfo(content, e) {
	var p = e.target;
	var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
	var infoWindow = new BMap.InfoWindow(content, opts); // 创建信息窗口对象
	map.openInfoWindow(infoWindow, point); // 开启信息窗口
}

$("#btn-fullscreen").click(function() {
	screenfull.toggle($('#allmap')[0]);
});


