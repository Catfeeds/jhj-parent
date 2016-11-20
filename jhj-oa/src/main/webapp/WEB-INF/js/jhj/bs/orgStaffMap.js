// 百度地图API功能
// 百度地图API功能
var map = new BMap.Map("allmap"); // 创建Map实例
map.centerAndZoom(new BMap.Point(116.404, 39.915), 12); // 初始化地图,设置中心点坐标和地图级别
map.addControl(new BMap.MapTypeControl()); // 添加地图类型控件
map.setCurrentCity("北京"); // 设置地图显示的城市 此项是必须设置的
map.enableScrollWheelZoom(false); // 开启鼠标滚轮缩放

var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
var top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}); //右上角，仅包含平移和缩放按钮
/*缩放控件type有四种类型:
BMAP_NAVIGATION_CONTROL_SMALL：仅包含平移和缩放按钮；BMAP_NAVIGATION_CONTROL_PAN:仅包含平移按钮；BMAP_NAVIGATION_
*/
map.addControl(top_left_control);        
map.addControl(top_left_navigation);     
map.addControl(top_right_navigation);    

function loadStaffMapDatas() {
	//清空
	map.clearOverlays();
	
	// 发送ajax请求根据省ID获取城市ID
	
	var parentId = $("#parentId").val();
	var orgId = $("#orgId").val();
	var name = $("#name").val();
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
		data: params,
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
	console.log("setOrgMapPoi");

	// 设置门店地图标注
	$.each(datas, function(i, obj) {
		var name = obj.name;
		var lng = obj.lng;
		var lat = obj.lat;
		

		if (lng == undefined || lng == "") return false;
		if (lat == undefined || lat == "") return false;
//		console.log(name + "----" + lng + "-----" + lat);
		var point = new BMap.Point(lng, lat);
		
		var label = new BMap.Label(name, {
			offset : new BMap.Size(-10, -30), position:point
		});
		
		label.setStyle({
			 color : "black",
			 fontSize : "12px",
			 height : "25px",
			 lineHeight : "20px",
			 lineWeight : "50px", 
			 fontFamily:"微软雅黑"
		 });
		
		
		var iconUrl = "";
		var poiStatus = obj.poi_status;
		if (poiStatus == 1) iconUrl = "/jhj-oa/img/circle-red.png";
		if (poiStatus == 2) iconUrl = "/jhj-oa/img/circle-blue.png";
		if (poiStatus == 3) iconUrl = "/jhj-oa/img/circle-green.png";
		
		var myIcon = new BMap.Icon(iconUrl, new BMap.Size(30,20));
		var marker = new BMap.Marker(point, {icon:myIcon});
		
		
		marker.setLabel(label);
		
		map.addOverlay(marker);
		
		
		
		

	});	
}

function setOfflines(datas) {
	
	$("#offline-list").html("");
	
	var offlinelistHtml="";
	$.each(datas, function(i, obj) {
		var name = obj.name;
		offlinelistHtml+="<li>"+name+"</li>"
	});
	$("#offline-list").html(offlinelistHtml);
}

loadStaffMapDatas();



$('.form_datetime').datepicker({
	format: 'yyyy-mm-dd',
	language : "zh-CN",
	autoclose : true,
	startView : 0,
	todayBtn : true
});


function loadStaffTrail() {
	
	
	var serviceDateStr = $("#serviceDateStr").val();
	if (serviceDateStr == undefined || serviceDateStr == "") {
		alert("请指定日期.");
		return false;
	}
	
	var mobile = $("#mobile").val();
	if (mobile == undefined || mobile == "") {
		alert("请输入手机号，轨迹只能查询某一天，一个服务人员的轨迹.");
		return false;
	}
	
	map.clearOverlays();
	
	var params = {};
	params.service_date = serviceDateStr;
	params.mobile = mobile;

	$.ajax({
		type : 'GET',
		url : '/jhj-oa/bs/get_staff_trail.json',
		dataType : 'json',
		cache : false,
		data: params,
		success : function(datas) {
			console.log(datas.data);
			var trailDatas = datas.data;
			
			if (trailDatas != undefined || trailDatas != "") {
				setStaffTrail(trailDatas);
			}
		},
		error : function() {
			
		}
	});
}


function setStaffTrail(trailDatas) {
	
	//如果只有一个点，则不需要折线，地图标一个点即可.
	if (trailDatas.length == 1) {
		var o = trailDatas[0];
		var opoint = new BMap.Point(o.lng, o.lat);
		var o1 = new BMap.Marker(opoint);
		 map.addOverlay(o1);
		 var ol1 = new BMap.Label(o.add_time_str, {offset : new BMap.Size(-10, -30), position: opoint });
		 map.addOverlay(ol1);
		 return false;
	}
	
	
	
	var chartData = [];
	
	var walking = new BMap.WalkingRoute(map, { renderOptions: { map: map, autoViewport: true} });
	
	var startTrail = trailDatas[0];
	var endTrail = trailDatas[trailDatas.length-1];
	
	var startpoint = new BMap.Point(startTrail.lng, startTrail.lat);
    var endpoint = new BMap.Point(endTrail.lng, endTrail.lat);
    walking.search(startpoint, endpoint);
    
  //定义集合用来存放沿线的坐标值
    var chartData = [];
  //通过setSearchCompleteCallback回调事件可以把步行间的坐标信息获取
    walking.setSearchCompleteCallback(function (rs) {
        var pts = walking.getResults().getPlan(0).getRoute(0).getPath();
        for (var i = 0; i < pts.length; i++) {
            chartData.push(new BMap.Point(pts[i].lat, pts[i].lng));
        }
    });
    
  //把步行线路的坐标集合转化成折线
    var polyline = new BMap.Polyline(chartData, { strokeColor: "red", strokeWeight: 6, strokeOpacity: 0.5 });
    map.addOverlay(polyline);
    
  //对起点、终点、途经点做一个简单的处理，泡泡跟文字提示
    addMaker(startTrail.lng, startTrail.lat, startTrail.add_time_str);
    addMaker(endTrail.lng, endTrail.lat, endTrail.add_time_str);

	
	//处理途经点
    console.log("trailDatas.length == " + trailDatas.length);
    if (trailDatas.length == 2) return false;
    

    $.each(trailDatas, function(i, obj) {
    	
    	if (i == 0) return true;
    	if (i == (trailDatas.length - 1)  ) return true;
    	console.log("i ==" + i);
		var name = obj.name;
		var lng = obj.lng;
		var lat = obj.lat;
		
		addMakerPoint(lng, lat, obj.add_time_str);
	});

    
}


function addMaker(lng, lat, title) {
	
	var point = new BMap.Point(lng, lat);
	var label = new BMap.Label(title, { offset : new BMap.Size(-10, 10), position: point });
	label.setStyle({
		 color : "black",
		 fontSize : "12px",
		 height : "25px",
		 lineHeight : "20px",
		 lineWeight : "50px", 
		 fontFamily:"微软雅黑"
	 });
	map.addOverlay(point);
	map.addOverlay(label);
	
}



function addMakerPoint(lng, lat, title) {
	
	var iconUrl = "/jhj-oa/img/circle-green.png";
	var myIcon = new BMap.Icon(iconUrl, new BMap.Size(30,20));
	var point = new BMap.Point(lng, lat);
	var label = new BMap.Label(title, { offset : new BMap.Size(-40, -30), position: point });
	label.setStyle({
		 color : "black",
		 fontSize : "12px",
		 height : "25px",
		 lineHeight : "20px",
		 lineWeight : "50px", 
		 fontFamily:"微软雅黑"
	 });
	
	var marker = new BMap.Marker(point, {icon:myIcon});
	
	marker.setLabel(label);
	
	map.addOverlay(marker);
}



function setZoom(bPoints){  
    var view = map.getViewport(eval(bPoints));  
    var mapZoom = view.zoom;   
    var centerPoint = view.center;   
    map.centerAndZoom(centerPoint,mapZoom);  
}
