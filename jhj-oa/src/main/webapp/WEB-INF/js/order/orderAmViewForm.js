//$(document).ready(function() {
//	// 根据 url 参数 ，控制 div 是否显示
//	var url = window.location.search;
//	// url 参数 最后 一位 即为 派工状态。
//	var disStatu = url.charAt(url.length - 1);
//	$("#pickAddr").val("");
//	
//	// 当订单状态=1(已预约)，显示地图
//	if (disStatu == 1 || disStatu == null || disStatu =="" ) {
//		$("#addrMap").show();
//	} else {
//		$("#addrMap").hide();
//	}
//	$("#containers").hide();
//	var flag = $("#flag").val();
//	if (flag == 0) {
//		$("#staffList").hide();
//	} else {
//		$("#staffList").show();
//	}
//	var disStatus = $("#disStatus").val();
//	if (disStatu == 0 || disStatu == 1) {
//		$("#staffDispatch").hide();
//	} else {
//		$("#staffDispatch").show();
//	}
//
//});

$("select[name='userAddrKey']").on('change',function() {
	var userAddr = $('#userAddrKey option:selected').val();
	if (userAddr == undefined || userAddr == "") return false;
	var v = userAddr.split("=");
	var pickAddrName = v[0];
	var pickAddr = v[1];
	var longtitude = v[2];
	var latitude = v[3];
	$("#poiLongitude").val(longtitude);
	$("#poiLatitude").val(latitude);
//	$("#pickAddrName").val(pickAddrName);
//	$("#pickAddrName").attr("placeholder", pickAddrName);
//	$("#pickAddr").val(pickAddr);
//	$("#pickAddr").attr("placeholder", pickAddr);
	$("#addrNum").show();
	
	
	/*
	 * jhj2.1  存储 地址名称
	 */
	$("#dyanmicPickAddrName").val(pickAddrName);
	
	/*
	 * 选择 常用地址 后, 也 发送请求 ,加载 最新可用的 员工列表
	 */
	
//	setPlace();
	
	loadProperStaffForAmOrder();

});

//员工 按钮 处理,单选效果， 点击当前，其余变灰
$(document).on("click",".popovers",function(k,v){
	
  $(this).parent().find("button").attr("class","btn btn-default popovers");
  $(this).attr("class","btn btn-success popovers");
  
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


ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
	var str = "";
	var _value = e.fromitem.value;
	var value = "";
	if (e.fromitem.index > -1) {
		value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
	}    
	str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;
	
	value = "";
	if (e.toitem.index > -1) {
		_value = e.toitem.value;
		value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
		
	}     
	str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
	G("searchResultPanel").innerHTML = str;
});

var myValue;

/*
 *  智能 提示搜索 后 , 点击选中 某一个 目标结果的   事件
 */
ac.addEventListener("onconfirm", function(e) { 
	
	var _value = e.item.value;
	myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
	G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
	
	
	/*
	 * jhj2.1  点击确认后, 保存地址名称
	 */
	
	$("#dyanmicPickAddrName").val(myValue);
	
	setPlace();
});

/*
 * 	在 搜索结果 选中 某一地址时,  获得 改地址 的 经纬度,保存在 隐藏域
*/
function setPlace() {
	function myFuns() {
		
		var pp = local.getResults().getPoi(0).point; // 获取第一个智能搜索的结果
			
			$("#poiLongitude").val(pp.lng);
			$("#poiLatitude").val(pp.lat);
			
			/*
			 * jhj2.1  在设置完 新的  坐标位置后, 再 发起请求
			 */ 
			loadProperStaffForAmOrder();
			
	}
	var local = new BMap.LocalSearch(map, { //智能搜索
		  onSearchComplete: myFuns
	});
	local.search(myValue);
}

//function setPlace(){
//	map.clearOverlays();    //清除地图上所有覆盖物
//	function myFun(){
//		var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
//		map.centerAndZoom(pp, 18);
//		
//		
//		//jhj2.1 使用搜索后,设置 选中 目标地点的 坐标
//		$("#poiLongitude").val(pp.lng);
//		$("#poiLatitude").val(pp.lat);
//		
//		loadProperStaffForAmOrder();
//		
//		var marker = new BMap.Marker(pp);
//		map.addOverlay(new BMap.Marker(pp));    //添加标注
//		marker.enableDragging();
//		
//	}
//	var local = new BMap.LocalSearch(map, { //智能搜索
//	  onSearchComplete: myFun
//	});
//	local.search(myValue);
//}




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

/*
 *  提交 派工结果
 * 
 */

$('#viewForm').on('click',function(){
	
	var orderStatus =  $("#orderStatus").val();
	
	//只有 已支付 和  已派工  的订单，可以有  调整派工操作
	
	if(orderStatus != 3 && orderStatus != 4){
		
		alert("只有 已支付或 已派工状态的 订单 ,可以进行调整 派工操作");
		return false;
	}
	
	//门牌号
	var addrNum = $("#pickAddr").val();
	
	if(addrNum == ""){
		
		alert("您还没有输入具体门派号哦");
		
		return false;
	}
	
	
	var orderId = $("#id").val();
	var fromLat = $("#poiLatitude").val();
	var fromLng = $("#poiLongitude").val();
	var userAddrName = $("#dyanmicPickAddrName").val()+addrNum;
	
	// 人工  选择的 派工 人员.如果没选。默认为0
	var selectStaffId = 0;
	
	//距离 数字。。省去处理派工时，重新查表，计算距离
	var distanceValue = 0;
	
	$(".popovers").each(function(k,v){
		
		if($(this).attr("class").indexOf("btn-success") > 0){
			
			selectStaffId = $(this).find("#selectStaffId").val();
			
			distanceValue = $(this).find("#distanceValue").val();
		}
	});
	
	$.ajax({
		type:'post',
		url:'/jhj-oa/new_dispatch/submit_manu_am_order_result.json',
		data:{
		  		"orderId":orderId,
		  		"fromLat":fromLat,
		  		"fromLng":fromLng,
		  "selectStaffId":selectStaffId,	
		  	  "distance" :distanceValue,
		   "userAddrName":userAddrName,
		},
		dataType:'json',
		success:function(data,status,xhr){
			
			alert("保存成功");
			
			var rootPath = getRootPath();
			window.location.replace(rootPath+"/order/order-am-list");
		},error:function(){
			alert("网络错误");
		}
	})
	
});

function getRootPath() {
    var strFullPath = window.document.location.href;
    var strPath = window.document.location.pathname;
    var pos = strFullPath.indexOf(strPath);
    var prePath = strFullPath.substring(0, pos);
    var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
    return (prePath + postPath);
}



//页面加载时， 做一些 效果的处理
var selectStaff = function(){
	
	//回显 已选中的 派工结果
	$(".popovers").each(function(k,v){
			
		var selectStaffId = $(this).find("#selectStaffId").val();
		
		var staffId = $("#staffId").val();
		if(selectStaffId == staffId){
			
			$(this).attr("class","btn btn-success popovers");
		}
	});
	
	
	// 当订单状态 为   >2 已确认， 则 隐藏  '提交订单' 按钮
	
	var stat =  $("#orderStatus").val();
	
	if(stat >=2){
		$("#saveOrder").hide();
		//金额也不能修改
		$("#orderMoney").prop("readonly",true);
	}
}

window.onload = selectStaff;



/*
 *   已预约的订单, 手动填写 价格、沟通后的需求，提交
 */
$("#saveOrder").on('click',function(){
	
	var orderMoney =  $("#orderMoney").val();
	
	if(orderMoney.length == 0){
		
		alert("请输入订单价格");
		return false;
	}
	
	var remarksConfirm = encodeURIComponent($("#remarksConfirm").val());
	
	var orderId = $("#id").val();
	
	$.ajax({
		type:'get',
		 url:'oa_submit_am_order.json',
		 data:{
			 "orderMoney":orderMoney,
			 "orderId":orderId,
			 "remarksConfirm":remarksConfirm
		 },
		 dataType:'json',
		 success:function(data,sta,xhr){
			 alert(data.msg)
			 
			 var rootPath = getRootPath();
			 window.location.replace(rootPath+"/order/order-am-list");
			 return false;
		 },
		 error:function(){
			 alert("网络错误,请联系管理员");
			 return false;
		 }
		
	});
	
});





