$(document).ready(function() {
	// 根据 url 参数 ，控制 div 是否显示
	var url = window.location.search;
	// url 参数 最后 一位 即为 派工状态。
	var disStatu = url.charAt(url.length - 1);
	if(disStatu>=3){//订单已经派工,显示派工信息，or 隐藏派工信息
		$("#allStaff").show();
		$("#viewForm").hide();
		$("#nowStaff").hide();
	}else{
		$("#viewForm").show();
		$("#allStaff").hide();
		$("#nowStaff").hide();
	}
});

$('.form_datetime').datetimepicker({
	format: "yyyy-mm-dd hh:ii",
	language: "zh-CN",
	autoclose: true,
	todayBtn:true,
	minuteStep: 30
});



$("#submitForm").on('click',function(){
	
	
	var orderStatus =  $("#orderStatus").val();
	
	//只有 已支付 和  已派工  的订单，可以有  调整派工操作
	
	if(orderStatus != 2 && orderStatus != 3){
		
		alert("只有 已支付 或 已派工状态的 订单 ,可以进行调整 派工操作");
		
		return false;
	}
	
	
	
	/*  对于 基础保洁类派工
	 * 
	 * 	 可能 更改的 就两个条件, 	 
	 * 		
	 * 		1. 服务时间    2. 派工人员
	 */
	
	var orderDate =  $("#serviceDateStr").val();
	var orderDateTimeStamp = new Date(orderDate).getTime();
	//服务时间戳
	var paramStamp = orderDateTimeStamp/1000;
	
	//页面加载时, 已经选定的 派工人员, 不再 判断是否修改了。。直接到后台更新
//	var staffId = $("#staffId").val();
	
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
	
	var orderId = $("#id").val();
	
	$.ajax({
		type:'post',
		url:'/jhj-oa/new_dispatch/submit_manu_base_order.json',
		data:{
		  "selectStaffId":selectStaffId,	
		  		"orderId":orderId,
		 "newServiceDate":paramStamp,
		 "distanceValue" :distanceValue
		},
		dataType:'json',
		success:function(data,status,xhr){
			
			alert("保存成功");
			
			var rootPath = getRootPath();
			window.location.replace(rootPath+"/order/order-hour-list");
		},error:function(){
			
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


//员工 按钮 处理,单选效果， 点击当前，其余变灰
$(document).on("click",".popovers",function(k,v){
	
  $(this).parent().find("button").attr("class","btn btn-default popovers");
  $(this).attr("class","btn btn-success popovers");
  
});



//改变 服务时间时,动态获取派工
$('.form_datetime').datetimepicker().on('changeDate', function(ev){
    
   //时间戳 ms值	
   var date = ev.date.valueOf();
   //东八区 减去 8小时
   var newServiceDate = date/1000-8*3600;
   
   //根据 服务 时间, 动态获取 有无 可用派工 	
   var orderId = $("#id").val();	 
	
	$.ajax({
		type:"get",
		 url:"/jhj-oa/new_dispatch/load_staff_by_change_service_date.json",
		data:{
			 "orderId":orderId,
	   "newServiceDate":newServiceDate
		},
		dataType:"json",
		success:function(data, status, xhr){
			
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
		},
		error:function(){
			alert("网络错误");
			return false;
		}
	});
	
});


//页面加载时， 回显 已选中的 派工结果
var selectStaff = function(){
	
	$(".popovers").each(function(k,v){
			
		var selectStaffId = $(this).find("#selectStaffId").val();
		
		var staffId = $("#staffId").val();
		if(selectStaffId == staffId){
			
			$(this).attr("class","btn btn-success popovers");
		}
	});
}



window.onload = selectStaff;


