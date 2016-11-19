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
		console.log(name + "----" + lng + "-----" + lat);
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
