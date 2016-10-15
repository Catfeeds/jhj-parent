
$('.form_datetime').datetimepicker({
	format : "yyyy-mm-dd hh:ii",
	language : "zh-CN",
	autoclose : true,
	todayBtn : true,
	minuteStep : 30
});


$('#selectedStaffs').tagsinput({
	  itemValue: 'id',
	  itemText: 'label',
	  tagClass: function(item) {
		  var labelArray = ['label-primary', 'label-danger', 'label-success', 'label-default', 'label-warning'];
		  var index = Math.floor((Math.random()*labelArray.length)); 
		  var v = labelArray[index];
		  return 'label ' + v;
	  },
});

// 提交派工修改
$("#submitForm").on('click', function() {
	$('#submitForm').attr('disabled',"true");
	var orderStatus = $("#orderStatus").val();
	
	// 只有 已支付 和 已派工 的订单，可以有 调整派工操作
	
	if (orderStatus != 2 && orderStatus != 3) {
		
		alert("只有 已支付 或 已派工状态的 订单 ,可以进行调整 派工操作");
		$('#submitForm').removeAttr("disabled"); 
		return false;
	}
	
	/*
	 * 对于 基础保洁类派工
	 * 
	 * 可能 更改的 就两个条件,
	 * 
	 * 1. 服务时间 2. 派工人员
	 */

	var orderDate = $("#serviceDateStr").val();
	var orderDateTimeStamp = new Date(orderDate).getTime();
	// 服务时间戳
	var paramStamp = orderDateTimeStamp / 1000;
	
	// 人工 选择的 派工 人员.如果没选。默认为0
	var selectStaffIds = $("#selectedStaffs").val();
	console.log("selectStaffIds = " + selectStaffIds);
	var orderId = $("#id").val();
	
	// 如果 未选择派工 。直接 返回列表页
	if (selectStaffIds == undefined || selectStaffIds == "") {
		
		alert("没有可用派工,返回列表页");
		$('#submitForm').removeAttr("disabled"); 
		
		return false;
	}
	
	$.ajax({
		type : 'post',
		url : '/jhj-oa/new_dispatch/save_order_exp.json',
		data : {
			"selectStaffIds" : selectStaffIds,
			"orderId" : orderId,
			"newServiceDate" : orderDate
		},
		dataType : 'json',
		success : function(data, status, xhr) {
			
			alert("保存成功");
			$('#submitForm').removeAttr("disabled"); 
			var rootPath = getRootPath();
			window.location.replace(rootPath + "/order/order-exp-list");
		},
		error : function() {
			$('#submitForm').removeAttr("disabled"); 
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

var loadStaffDynamic = function(data, status, xhr) {
	
	var result = data;
	
	// 清空原有数据
	$("#allStaff").html("");
	
	var tdHtml = "";
	
	if (result.length == 0) {
		alert("暂无可用派工");
	}
	
	var isMulti = $("#isMulti").val();
	
	for (var i = 0, j = result.length; i < j; i++) {
		
		var item = data[i];
		var selectInput = "";
		if (isMulti == 0) {
			selectInput = "<input name='select-staff' type='radio' onclick='doSelectStaff()' value=" + item.staff_id + ">";
		} else {
			selectInput = "<input name='select-staff' type='checkbox' onclick='doSelectStaff()' value=" + item.staff_id + ">";
		}
		
		var htmlStr = "<tr>";
			htmlStr+= "<td>";
		if (item.dispath_sta_flag == 1) {
			htmlStr+= selectInput;
		}
		
		htmlStr+= "<input  type='hidden' id='selectStaffId' name='selectStaffId' value=" + item.staff_id+ ">"
		+ "<input type='hidden' id='distanceValue' value=" + item.distance_value + ">" 
		+ "<input type='hidden' id='selectStaffName' value=" + item.name + ">" 
		+ "</td>" 
		+ "<td>" + item.staff_org_name + "</td>" 
		+ "<td>" + item.staff_cloud_org_name + "</td>" 
		+ "<td>" + item.org_distance_text + "</td>" 
		+ "<td>" + item.name + "</td>" 
		+ "<td>" + item.mobile + "</td>" 
		+ "<td>" + item.distance_text + "</td>" 
		+ "<td>" + item.today_order_num + "</td>"
		+ "<td>" + item.dispath_sta_str + "</td>"
		+ "<td>" + item.reason + "</td>";
		htmlStr+="</tr>";
		tdHtml += htmlStr;
		
	}
	
	$("#allStaff").append(tdHtml);
	
	return false;
}



function doSelectStaff() {

	
	$("input[name='select-staff']").each(function(k, v) {
		
		var selectStaffId = $(this).parent().find("#selectStaffId").val();
		
		var selectStaffName = $(this).parent().find("#selectStaffName").val();
		
		var distanceValue = $(this).parent().find("#distanceValue").val();
		
		// 如果该行被选中
		if (this.checked) {
			
			$('#selectedStaffs').tagsinput('add', { id: selectStaffId, label: selectStaffName, distanceValue :  distanceValue});
		} else {
			$('#selectedStaffs').tagsinput('remove', { id: selectStaffId, label: selectStaffName, distanceValue :  distanceValue});
		}
	});

	console.log($('#selectedStaffs').val());
}

// 改变 服务时间时,动态获取派工
$('.form_datetime').datetimepicker().on('changeDate', function(ev) {
	
	// 时间戳 ms值
	var date = ev.date.valueOf();
	// 东八区 减去 8小时
	var newServiceDate = date / 1000 - 8 * 3600;
	
	loadStaffsByServiceDate(newServiceDate);
	
});

function loadStaffsByServiceDate(serviceDate) {
	// 根据 服务 时间, 动态获取 有无 可用派工
	var orderId = $("#id").val();
	
	$.ajax({
		type : "get",
		url : "/jhj-oa/new_dispatch/load_staff_by_change_service_date.json",
		data : {
			"orderId" : orderId,
			"newServiceDate" : serviceDate
		},
		dataType : "json",
		success : loadStaffDynamic
	});
}

/*
 * 选择 云店时，动态 展示 对应云店的 阿姨 派工状态
 */
$("#parentId").on('change', function() {
	loadStaffs();
});

$("#orgId").on('change', function() {
	loadStaffs();
});

function loadStaffs() {
	
	var orderStatus = $("#orderStatus").val();
	if (orderStatus == 1) return false;
	
	// 当前选中的 云店id
	var parentId = $("#parentId").val();
	
	// 根据 服务 时间, 动态获取 有无 可用派工
	var orderId = $("#id").val();
	
	// 如果未选择门店，直接返回
	if (parentId == 0) {
		return false;
	}
	
	var orgId = $("#orgId").val();
	
	$.ajax({
		type : "get",
		url : "/jhj-oa/new_dispatch/load_staff_by_change_cloud_org.json",
		data : {
			"orderId" : orderId,
			"parentId" : parentId,
			"orgId" : orgId
		},
		dataType : "json",
		success : loadStaffDynamic
	});
}





// 点击选择 调整派工方案
$("input[name='disWay']").on("change", function() {

	var thisVal = $("input[name='disWay']:checked").val();
	if (thisVal == 0) {
		$("#div-org-id").hide();
		$("#div-cloud-id").hide();
	}
	
	if (thisVal == 1) {
		$("#div-org-id").show();
		$("#div-cloud-id").show();
	}
})

// 页面加载时， 回显 已选中的 派工结果
var selectStaff = function() {
	
	var orderStatus = $("#orderStatus").val();
	if (orderStatus == 1) {
		$("#dispatchSection").css("display", "none");
		$("#doDispatchSection").css("display", "none");
		
		return false;
	}
	
	
	var staffId = $("#staffId").val();
	$("input[name='select-staff']").each(function(k, v) {
		
		var selectStaffId = $(this).val();
		
		if (selectStaffId == staffId) {
			
			$(this).attr("checked", true);
		}

	});
		
	$("input[name='disWay']").trigger("change");
	
	var disWay = $("input[name='disWay']:checked").val();
	
	if (disWay == 0) {
		var serviceDateStr = $("#serviceDateStr").val();
		
		if (serviceDateStr.length == 16) serviceDateStr+=":00";
		
		var newServiceDate = moment(serviceDateStr).unix();
				
		loadStaffsByServiceDate(newServiceDate);
	}
	
	if (disWay == 1) {
		$("#parentId").trigger("change");
	}
	
	
	
}

window.onload = selectStaff;
