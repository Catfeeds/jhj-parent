
// =====================派工相关==========================================================

$('#selectedStaffs').tagsinput(
		{
			itemValue : 'id',
			itemText : 'label',
			tagClass : function(item) {
				var labelArray = [ 'label-primary', 'label-danger', 'label-success',
						'label-default', 'label-warning' ];
				var index = Math.floor((Math.random() * labelArray.length));
				var v = labelArray[index];
				return 'label ' + v;
			},
		});

$('#selectedStaffs').on('itemRemoved', function(event) {
	// event.item: contains the item
	if (event.item == undefined) return false;
	var staffId = event.item.id;
	$("input[name='select-staff']").each(function(k, v) {
		
		var selectStaffId = $(this).parent().find("#selectStaffId").val();
		
		var selectStaffName = $(this).parent().find("#selectStaffName").val();
		
		var distanceValue = $(this).parent().find("#distanceValue").val();
		
		if (selectStaffId == staffId) {
			// 如果该行被选中
			
			$(this).removeAttr("checked");
			
		}
	});
});

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
			selectInput = "<input name='select-staff' type='radio' onclick='doSelectStaff("
					+ item.staff_id + ")' value=" + item.staff_id + ">";
		} else {
			selectInput = "<input name='select-staff' type='checkbox' onclick='doSelectStaff("
					+ item.staff_id + ")' value=" + item.staff_id + ">";
		}
		
		var htmlStr = "<tr>";
		htmlStr += "<td>";
		if (item.dispath_sta_flag == 1) {
			htmlStr += selectInput;
		}
		
		var sexName = "男";
		if (item.sex == 1) sexName = "女";
		
		htmlStr+= "<input  type='hidden' id='selectStaffId' name='selectStaffId' value="+ item.staff_id + ">";
		htmlStr+= "<input type='hidden' id='distanceValue' value="+ item.distance_value + ">";
		htmlStr+= "<input type='hidden' id='selectStaffName' value="+ item.name + ">";
		htmlStr+= "</td>";
		htmlStr+= "<td>" + item.parent_org_name + "</td>";
		htmlStr+= "<td>" + item.org_name + "</td>";
		htmlStr+= "<td>" + item.org_distance_text + "</td>";
		htmlStr+= "<td>" + item.name + "</td>";
		htmlStr+= "<td>" + item.mobile + "</td>";
		htmlStr+= "<td>" + sexName + "</td>";
		htmlStr+= "<td>" + item.distance_text + "</td>";
		htmlStr+= "<td>" + item.today_order_num + "</td>"
		htmlStr+= "<td>" + item.pre_day_order_num + "</td>"
		htmlStr+= "<td>" + item.dispath_sta_str + "</td>";
//		htmlStr+= "<td>" + item.reason + "</td>";
		htmlStr+= "<td>" + item.allocate_reason + "</td>";
		htmlStr += "</tr>";
		tdHtml += htmlStr;
		
	}
	
	$("#allStaff").append(tdHtml);
	
	var selectedStaffs = $("#selectedStaffs").val();
	// console.log("selectedStaffs = " + selectedStaffs );
	if (selectedStaffs == undefined || selectedStaffs == "") {
		var serviceDate = $("#serviceDate").val()
		var serviceDateUnix = moment(serviceDate).unix();
		loadAutoDispatch(serviceDateUnix);
	}
	
	return false;
}

function doSelectStaff(staffId) {
	
	var selectStaffId = "";
	var selectStaffName = "";
	var distanceValue = "";
	$("input[name='select-staff']").each(function(k, v) {
		
		var selectStaffId = $(this).parent().find("#selectStaffId").val();
		
		var selectStaffName = $(this).parent().find("#selectStaffName").val();
		
		var distanceValue = $(this).parent().find("#distanceValue").val();
		
		if (selectStaffId == staffId) {
			// 如果该行被选中
			if (this.checked) {
				addSelectedStaffs(selectStaffId, selectStaffName, distanceValue);
			} else {
				$('#selectedStaffs').tagsinput('remove', {
					id : selectStaffId,
					label : selectStaffName,
					distanceValue : distanceValue
				});
			}
		}
		
	});
	console.log($('#selectedStaffs').val());
}

function doSelectStaffCheck(staffId) {
	
	var selectStaffId = "";
	var selectStaffName = "";
	var distanceValue = "";
	$("input[name='select-staff']").each(function(k, v) {
		
		var selectStaffId = $(this).parent().find("#selectStaffId").val();
		
		var selectStaffName = $(this).parent().find("#selectStaffName").val();
		
		var distanceValue = $(this).parent().find("#distanceValue").val();
		
		if (selectStaffId == staffId) {
			// 如果该行被选中
			$(this).attr("checked", "true");
			addSelectedStaffs(selectStaffId, selectStaffName, distanceValue);
		}
	});
	
}

function addSelectedStaffs(selectStaffId, selectStaffName, distanceValue) {
	var selectStaffIds = $("#selectedStaffs").val();
	
	if (selectStaffIds.indexOf(selectStaffId) >= 0) return false;
	
	$('#selectedStaffs').tagsinput('add', {
		id : selectStaffId,
		label : selectStaffName,
		distanceValue : distanceValue
	});
}

function remove(selectStaffId, selectStaffName, distanceValue) {
	var selectStaffIds = $("#selectedStaffs").val();
	
	if (selectStaffIds.indexOf(selectStaffId) >= 0) return false;
	
	$('#selectedStaffs').tagsinput('add', {
		id : selectStaffId,
		label : selectStaffName,
		distanceValue : distanceValue
	});
}

function loadAutoDispatch(serviceDate) {
	var addrId = $("#addrId").val();
	if (addrId == undefined || addrId == "") {
		return false;
	}
	
	var serviceType = $("#serviceType").val();
	if (serviceType == undefined || serviceType == "") {
		return false;
	}
	
	var params = {};
	
	params.addrId = $("#addrId").val();
	params.serviceHour = $("#serviceHour").val();
	params.serviceTypeId = $("#serviceType").val();
	params.serviceDate = serviceDate;
	params.staffNums = $("#staffNums").val();
	
	$.ajax({
		type : "get",
		url : "/jhj-oa/new_dispatch/load_auto_dispatch.json",
		data : params,
		dataType : "json",
		async : false,
		success : function(data) {
			
			if (data == undefined || data == '') return false;
			
			$('#selectedStaffs').tagsinput('removeAll');
			
			$('#selectedStaffs').val('');
			$.each(data, function(i, obj) {
				
				var staffId = obj.staff_id;
				var staffName = obj.name;
				doSelectStaffCheck(staffId);
			});
		}
	});
}

function loadStaffsByServiceDate(serviceDate) {
	// 根据 服务 时间, 动态获取 有无 可用派工
	console.log("loadStaffsByServiceDate = " + serviceDate);
	var addrId = $("#addrId").val();
	if (addrId == undefined || addrId == "") {
		return false;
	}
	
	var serviceType = $("#serviceType").val();
	if (serviceType == undefined || serviceType == "") {
		return false;
	}
	
	var params = {};
	
	params.orderStatus = 2;
	params.addrId = $("#addrId").val();
	params.serviceHour = $("#serviceHour").val();
	params.serviceTypeId = $("#serviceType").val();
	params.serviceDate = serviceDate;
	
	$.ajax({
		type : "get",
		url : "/jhj-oa/new_dispatch/load_staff_by_change_service_date.json",
		async : false,
		data : params,
		dataType : "json",
		success : loadStaffDynamic
	});
}

/*
 * 选择 云店时，动态 展示 对应云店的 阿姨 派工状态
 */
// $("#parentId").on('change', function() {
// loadStaffs();
// });
$("#orgId").on('change', function() {
	loadStaffs();
});

function parentIdExtendChange() {
	loadStaffs();
}

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
	
	// 获取当前选择的订单时间
	var serviceDateStr = $("#serviceDate").val();
	if (serviceDateStr == undefined || serviceDateStr == "") {
		return false;
	}
	var serviceDate = moment(serviceDateStr).unix();
	
	var addrId = $("#addrId").val();
	if (addrId == undefined || addrId == "") {
		return false;
	}
	
	var serviceType = $("#serviceType").val();
	if (serviceType == undefined || serviceType == "") {
		return false;
	}
	
	var params = {};
	params.parentId = parentId;
	params.orgId = $("#orgId").val();
	params.orderStatus = 2;
	params.addrId = $("#addrId").val();
	params.serviceHour = $("#serviceHour").val();
	params.serviceTypeId = $("#serviceType").val();
	params.serviceDate = serviceDate;
	$.ajax({
		type : "get",
		url : "/jhj-oa/new_dispatch/load_staff_by_change_cloud_org.json",
		data : params,
		async : false,
		dataType : "json",
		success : loadStaffDynamic
	});
}

// 点击选择 调整派工方案
$("input[name='disWay']").on("change", function() {
	// console.log("disWay change");
	var thisVal = $("input[name='disWay']:checked").val();
	if (thisVal == 0) {
		$("#div-org-id").hide();
		$("#div-cloud-id").hide();
		$("#modalDispatch").modal("show");
		
		var serviceDate = $("#serviceDate").val();
		var serviceDateUnix = moment(serviceDate).unix();
		loadStaffsByServiceDate(serviceDateUnix);
	}
	
	if (thisVal == 1) {
		$("#div-org-id").show();
		$("#div-cloud-id").show();
		
		loadStaffs();
	}
})