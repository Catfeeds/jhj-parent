$(document).ready(function() {
	// 根据 url 参数 ，控制 div 是否显示
	var url = window.location.search;
	// url 参数 最后 一位 即为 派工状态。
	var disStatu = url.charAt(url.length - 1);
	$("#pickAddr").val("");
/*	var v = $("#pickAddrName").val();
	if (v != null && v != "" && v != "请输入位置") {
		$("#pickAddrs").show();
		$("#addrNum").show();
	} else {
		$("#pickAddrs").hide();
		$("#addrNum").hide();
	}*/
	// 当订单状态=1(已预约)，显示地图
	if (disStatu == 1 || disStatu == null || disStatu =="" ) {
		$("#addrMap").show();
	} else {
		$("#addrMap").hide();
	}
	$("#containers").hide();
	var flag = $("#flag").val();
	if (flag == 0) {
		$("#staffList").hide();
	} else {
		$("#staffList").show();
	}
	var disStatus = $("#disStatus").val();
	if (disStatu == 0 || disStatu == 1) {
		$("#staffDispatch").hide();
	} else {
		$("#staffDispatch").show();
	}

});

$("select[name='userAddrKey']").click(function() {
	var userAddr = $('#userAddrKey option:selected').val();
	if (userAddr == undefined || userAddr == "") return false;
	var v = userAddr.split("=");
	var pickAddrName = v[0];
	var pickAddr = v[1];
	var longtitude = v[2];
	var latitude = v[3];
	$("#poiLongitude").val(longtitude);
	$("#poiLatitude").val(latitude);
	$("#pickAddrName").val(pickAddrName);
	$("#pickAddrName").attr("placeholder", pickAddrName);
	$("#pickAddr").val(pickAddr);
	$("#pickAddr").attr("placeholder", pickAddr);
	$("#addrNum").show();

});

$("#viewForm").click(function(form) {
	// 单选按钮，不用处理 ,框架根据 name自动提交
	if (confirm("确认要派工吗?")) {
		if ($('#viewForm').validate().form()) {
			var val = $('input:radio[name="staffId"]:checked').val();
			if (val == null) {
				alert("请选择派工人员");
				return false;
			} else {
				$('#viewForm').submit();
			}
		}
	} else {
		return false;
	}
});

$("#chooseStaff").click(function() {
	var localName = $("#pickAddrName").val();
	var addrName = $("#pickAddr").val();
	var poiLatitude = $("#poiLatitude").val();
	var poiLongitude = $("#poiLatitude").val();
	console.log("lat = " + poiLatitude);
	console.log("lng = " + poiLongitude);
	if (localName == null || localName == "" || poiLatitude == "" || poiLongitude == "") {
		alert("请输入服务地址");
		return;
	}
	if (addrName == null || addrName == "") {
		alert("请输入服务地址门牌号");
	}
	$('#amView').attr("action", "order-am-staff");
	$("#amView").submit();
});

// baiduMap 相关
var map = new BMap.Map("containers");// 初始化地图
map.addControl(new BMap.NavigationControl()); // 初始化地图控件
map.addControl(new BMap.ScaleControl());
map.addControl(new BMap.OverviewMapControl());
map.enableScrollWheelZoom(); // 滚轮放大缩小控件
var point = new BMap.Point(116.404, 39.915);
map.centerAndZoom(point, 15);// 初始化地图中心点
var marker = '';

var gc = new BMap.Geocoder();// 地址解析类
map.addEventListener("click", function(e) {
	console.log(e.point + "--");
	marker = new BMap.Marker(new BMap.Point(e.point.lng, e.point.lat));
	map.clearOverlays();
	map.addOverlay(marker);
	marker.enableDragging();
	marker.addEventListener("dragstart", function(e) {
		document.getElementById("poiLongitude").value = e.point.lng;
		document.getElementById("poiLatitude").value = e.point.lat;
		gc.getLocation(e.point, function(rs) {
			showLocationInfo(e.point, rs);
		});
	});
});

// 信息窗口
function showLocationInfo(pt, rs) {
	var opts = {
		width : 250, // 信息窗口宽度
		height : 100, // 信息窗口高度
		title : "当前位置" // 信息窗口标题
	}

	var addComp = rs.addressComponents;
	var addr = addComp.province + ", " + addComp.city + ", " + addComp.district
			+ ", " + addComp.street + ", " + addComp.streetNumber + "<br/>";
	addr += "纬度: " + pt.lat + ", " + "经度：" + pt.lng;

	var searchTxt = document.getElementById("pickAddrName").value;
	/*
	 * document.getElementById("poiAddress").value = addComp.district +
	 * addComp.street + addComp.streetNumber;
	 * document.getElementById("poiCity").value = addComp.city;
	 */
	var infoWindow = new BMap.InfoWindow(addr, opts); // 创建信息窗口对象
	marker.openInfoWindow(infoWindow);
}

// 搜索提示
function G(id) {
	return document.getElementById(id);
}

var ac = new BMap.Autocomplete( // 建立一个自动完成的对象
{
	"input" : "pickAddrName",
	"location" : map
});

ac.addEventListener("onhighlight", function(e) { // 鼠标放在下拉列表上的事件
	var str = "";
	var _value = e.fromitem.value;
	var value = "";
	if (e.fromitem.index > -1) {
		value = _value.province + _value.city + _value.district + _value.street
				+ _value.business;
	}
	str = "FromItem<br />index = " + e.fromitem.index + "<br />value = "
			+ value;

	value = "";
	if (e.toitem.index > -1) {
		_value = e.toitem.value;
		value = _value.province + _value.city + _value.district + _value.street
				+ _value.business;
	}
	str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = "
			+ value;
	G("searchResultPanel").innerHTML = str;
});

var myValue;
ac.addEventListener("onconfirm", function(e) { // 鼠标点击下拉列表后的事件
	var _value = e.item.value;
/*	if (_value != null && _value != "" && _value != "请输入位置") {
		$("#pickAddrs").show();
		$("#addrNum").show();
	} else {
		$("#pickAddrs").hide();
		$("#addrNum").hide();
	}*/
	myValue = _value.province + _value.city + _value.district + _value.street
			+ _value.business;
	G("searchResultPanel").innerHTML = "onconfirm<br />index = " + e.item.index
			+ "<br />myValue = " + myValue;

	setPlace();
});

function setPlace() {
	map.clearOverlays(); // 清除地图上所有覆盖物
	function myFun() {
		var pp = local.getResults().getPoi(0).point; // 获取第一个智能搜索的结果
		if (pp) {
			console.log(document.getElementById("poiLongitude").value);
			document.getElementById("poiLongitude").value = pp.lng;
			console.log(document.getElementById("poiLongitude").valuess);

			document.getElementById("poiLatitude").value = pp.lat;
			map.centerAndZoom(pp, 18);
			var marker = new BMap.Marker(pp);
			map.addOverlay(new BMap.Marker(pp)); // 添加标注
			marker.enableDragging();
		} else {
			alert("请您输入正确可识别的地址！");
		}
	}
	var local = new BMap.LocalSearch(map, { // 智能搜索
		onSearchComplete : myFun
	});
	local.search(myValue);
}

// 页面加载时，判断是否回显地图信息
function hhh() {
	var b = document.getElementById("poiLatitude").value;
	var c = document.getElementById("poiLongitude").value;
	var pt = new BMap.Point(c, b);
	var marker = new BMap.Marker(pt); // 初始化地图标记，通过标记窗口，回显地图信息

	var myGeo = new BMap.Geocoder();
	// 解析记录中 经纬度 确定的 标记点，在信息窗中回显该地址信息
	myGeo.getLocation(pt, function(rs) {
		var opts = {
			width : 250, // 信息窗口宽度
			height : 50, // 信息窗口高度
			title : "当前位置:" // 信息窗口标题
		}
		var addComp = rs.addressComponents;

		var addr = document.getElementById("pickAddrName").value;
		// var addr = document.getElementById("poiAddress").value;
		if (addr == "") {
			// 若是新增页面。则定位在天安门，不显示信息窗
			map.centerAndZoom(point, 15);
		} else {
			// 若是修改页面。则定位在指定位置，并显示信息窗
			var infoWindow = new BMap.InfoWindow(addr, opts); // 创建信息窗口对象
			map.addOverlay(marker);
			marker.openInfoWindow(infoWindow);
		}
	});
}

// 修改页面一加载就回显地图位置
window.onload = hhh;