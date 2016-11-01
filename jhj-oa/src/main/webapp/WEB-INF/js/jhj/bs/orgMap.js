// 百度地图API功能
// 百度地图API功能
var map = new BMap.Map("allmap"); // 创建Map实例
map.centerAndZoom(new BMap.Point(116.404, 39.915), 11); // 初始化地图,设置中心点坐标和地图级别
map.addControl(new BMap.MapTypeControl()); // 添加地图类型控件
map.setCurrentCity("北京"); // 设置地图显示的城市 此项是必须设置的
map.enableScrollWheelZoom(true); // 开启鼠标滚轮缩放

// 编写自定义函数,创建标注
function addMarker(point, label) {
	var marker = new BMap.Marker(point);
	map.addOverlay(marker);
	marker.setLabel(label);
}

function loadOrgs() {
	// 发送ajax请求根据省ID获取城市ID
	$.ajax({
		type : 'GET',
		url : '/jhj-oa/bs/get-all-orgs.json',
		dataType : 'json',
		cache : false,
		success : function(datas) {
			setOrgMapPoi(datas);
		},
		error : function() {
			
		}
	});
}

function setOrgMapPoi(datas) {
	var parentOrgs = datas.data.parent_orgs;
	var orgs = datas.data.orgs;
	
	// 设置门店地图标注
	$.each(parentOrgs, function(i, obj) {
		var name = obj.org_name;
		var lng = obj.poi_longitude;
		var lat = obj.poi_latitude;
		
		var point = new BMap.Point(lng, ne.lat - lat);
		
		var label = new BMap.Label(name, {
			offset : new BMap.Size(20, -10)
		});
		
		addMarker(point, label);
	});
	
}

loadOrgs();
