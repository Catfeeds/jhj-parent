$(document).ready(function() {
	// 根据 url 参数 ，控制 div 是否显示
	var url = window.location.search;
	// url 参数 最后 一位 即为 派工状态。
	var disStatu = url.charAt(url.length - 1);
	$("#pickAddr").val("");
	
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
	
	
	
	/*
	 * 选择 常用地址 后, 也 发送请求 ,加载 最新可用的 员工列表
	 */
	
	loadProperStaffForAmOrder();
	

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

var gc = new BMap.Geocoder();// 地址解析类

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

/*
 *  智能 提示搜索 后 , 点击选中 某一个 目标结果的   事件
 */
ac.addEventListener("onconfirm", function(e) { 
	var _value = e.item.value;

	myValue = _value.province + _value.city + _value.district + _value.street
			+ _value.business;
	G("searchResultPanel").innerHTML = "onconfirm<br />index = " + e.item.index
			+ "<br />myValue = " + myValue;

	setPlace();
	
});

/*
 * 	在 搜索结果 选中 某一地址时,  获得 改地址 的 经纬度,保存在 隐藏域
*/
function setPlace() {
	function myFun() {
		var pp = local.getResults().getPoi(0).point; // 获取第一个智能搜索的结果
		if (pp) {
			
			$("#poiLongitude").val(pp.lng);
			$("#poiLatitude").val(pp.lat);
			
			/*
			 * jhj2.1  在设置完 新的  坐标位置后, 再 发起请求
			 */ 
			loadProperStaffForAmOrder();
			
		} else {
			alert("请您输入正确可识别的地址！");
		}
	}
	var local = new BMap.LocalSearch(map, { // 智能搜索
		onSearchComplete : myFun
	});
	local.search(myValue);
}

/*
 *  在 选择地址后 ,ajax 加载 可用的 服务人员
 *  
 *  分析:
 *    	
 *    助理类 订单。由于 服务时间 并不影响 派工结果, 而 多次修改 地址 又不符合 正常逻辑。
 *    	
 *    故, 大多改变 只是在  第一次 选择地址时 发生(刚跟用户确认完地址)
 */

function loadProperStaffForAmOrder(){
	
	//当前 经纬度
	var poiLat = $("#poiLatitude").val();
	
	var poiLng = $("#poiLongitude").val();
	
	var orderId = $("#id").val();
	
	$.ajax({
		type:'post',
		url:'/jhj-oa/new_dispatch/load_staff_for_am_order.json',
		data:{
		  		"orderId":orderId,
		  		"fromLat":poiLat,
		  		"fromLng":poiLng
		},
		dataType:'json',
		success:function(data,status,xhr){
			
			var result = data;
			
			//如果 调整时间后,没有可用派工
			if(result.length  == 0){
				
				$("#displayProperStaff").html("");
				
				var button =  document.createElement("button");
				
				button.setAttribute("type","button");
				button.setAttribute("class","btn btn-default");
				button.setAttribute("disabled","disabled");
				
				$(button).text("暂无可用派工");
				
				$("#displayProperStaff").append(button);
			}
			
			//如果有 可用的 服务人员
			if(result.length > 0){
				
				//先清空
				$("#displayProperStaff").text("");
				
				for(var i= 0, reLength = result.length; i< reLength; i++){
					
					var staff =  result[i];
					
					var button =  document.createElement("button");
					
					var tipContent = "预计到达用时:"+staff.duration_text+"\n" 
									+"今日派单数:"+ staff.today_order_num+"\n"
									+"距用户地址距离:"+ staff.distance_text;
					
					button.setAttribute("type","button");
					button.setAttribute("style","margin-top:10px;margin-left:5px");
					button.setAttribute("class","btn btn-default popovers");
					
					$(button).text(staff.name);
					
					//重新绑定  bootStrap 悬浮事件
					$(button).popover({
						trigger:"hover",
						placement:"top",
						title:"参考信息",
						html:'true',	
						content:tipContent,
					})					
					
					var input1 =  document.createElement("input");
					
					input1.setAttribute("type","hidden");
					input1.setAttribute("id","selectStaffId");
					input1.setAttribute("value",staff.staff_id);
					
					button.appendChild(input1);
					
					var input2 = document.createElement("input");
					
					input2.setAttribute("type","hidden");
					input2.setAttribute("id","distanceValue");
					input2.setAttribute("value",staff.distance_value);
					
					button.appendChild(input2);
					
					$("#displayProperStaff").append(button);	
					
				}
			}
			return false;
			
		},error:function(){
			alert("网络错误");
		}
	})
	
}

