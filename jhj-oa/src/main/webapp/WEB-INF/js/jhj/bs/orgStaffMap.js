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
	
	var points = [];//原始点信息数组  
	var bPoints = [];//百度化坐标数组。用于更新显示范围。  
	
	var makerPoints = []; 
	
	
	$.each(trailDatas, function(i, obj) {
		var name = obj.name;
		var lng = obj.lng;
		var lat = obj.lat;
		var id = obj.user_id;
		var addTimeStr = obj.add_time_str;
		var point = {"lng":lng,"lat":lat,"status":1,"id":id, "addTimeStr" : addTimeStr} ;
		points.push(point);
		makerPoints.push(point);   
		
		bPoints.push(new BMap.Point(lng,lat));  
	});
	
	var len = points.length;  
//    newLinePoints = points.slice(len-2, len);//最后两个点用来画线。  
	
    addLine(points);//增加轨迹线  
    setZoom(bPoints);  
	
}


//添加线  
function addLine(points){  
  
    var linePoints = [],pointsLen = points.length,i,polyline;  
    if(pointsLen == 0){  
        return;  
    }  
    // 创建标注对象并添加到地图     
    for(i = 0;i <pointsLen;i++){  
        linePoints.push(new BMap.Point(points[i].lng,points[i].lat));  
    }  
  
    polyline = new BMap.Polyline(linePoints, {strokeColor:"red", strokeWeight:2, strokeOpacity:0.5});   //创建折线  
    map.addOverlay(polyline);   //增加折线  
}  

function setZoom(bPoints){  
    var view = map.getViewport(eval(bPoints));  
    var mapZoom = view.zoom;   
    var centerPoint = view.center;   
    map.centerAndZoom(centerPoint,mapZoom);  
}

function addMarker(point){
	  var marker = new BMap.Marker(point);
	  map.addOverlay(marker);
}
