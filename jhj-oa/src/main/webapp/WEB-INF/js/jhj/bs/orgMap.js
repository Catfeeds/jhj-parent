// 百度地图API功能
// 百度地图API功能
var map = new BMap.Map("allmap"); // 创建Map实例
map.centerAndZoom(new BMap.Point(116.404, 39.915), 13); // 初始化地图,设置中心点坐标和地图级别
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



function loadOrgs() {
	// 发送ajax请求根据省ID获取城市ID
	$.ajax({
		type : 'GET',
		url : '/jhj-oa/bs/get-all-orgs.json',
		dataType : 'json',
		cache : false,
		success : function(datas) {
			console.log(datas);
			setOrgMapPoi(datas.data);
		},
		error : function() {
			
		}
	});
}

function setOrgMapPoi(datas) {
	console.log("setOrgMapPoi");
	var parentOrgs = datas.parentOrgs;
	var orgs = datas.orgs;

	// 设置门店地图标注
	$.each(parentOrgs, function(i, obj) {
		var name = obj.org_name;
		var lng = obj.poi_longitude;
		var lat = obj.poi_latitude;

		var point = new BMap.Point(lng, lat);
		
		var label = new BMap.Label(name, {
			offset : new BMap.Size(20, -10)
			
		});
		
		var myIcon = new BMap.Icon("http://developer.baidu.com/map/jsdemo/img/fox.gif", new BMap.Size(300,157));
		var marker = new BMap.Marker(point);
		map.addOverlay(marker);
		
		label.setStyle({
			 color : "red",
			 fontSize : "12px",
			 height : "20px",
			 lineHeight : "20px",
			 fontFamily:"微软雅黑"
		 });
		marker.setLabel(label);
		
		
//		addMarker(point, label);
	});
	
	// 设置云店地图标注
	$.each(orgs, function(i, obj) {
		var name = obj.org_name;
		var lng = obj.poi_longitude;
		var lat = obj.poi_latitude;
//		console.log("name =" + name + "lng = " + lng + "lat = " + lat);

		var point = new BMap.Point(lng, lat);
		
		var label = new BMap.Label(name, {
			offset : new BMap.Size(20, -10)
		});
		
		addMarker(point, label);
	});
	
}

//编写自定义函数,创建标注
function addMarker(point, label) {
	var marker = new BMap.Marker(point);
	map.addOverlay(marker);
	
	label.setStyle({
		 color : "red",
		 fontSize : "12px",
		 height : "20px",
		 lineHeight : "20px",
		 fontFamily:"微软雅黑"
	 });
	marker.setLabel(label);
}

loadOrgs();
