
$('.form_datetime').datepicker({
	format: 'yyyy-mm-dd',
	language : "zh-CN",
	autoclose : true,
	startView : 0,
	todayBtn : true
});

$('.form-datetime').datetimepicker({
	format: "yyyy-mm-dd hh:ii",
	language: "zh-CN",
	autoclose: true,
	todayBtn:true,
	minuteStep: 30
});


function checkEndTime() {
	var startTime = $("#startTimeStr").val();
	var start = new Date(startTime.replace("-", "/").replace("-", "/"));
	var endTime = $("#endTimeStr").val();
	var end = new Date(endTime.replace("-", "/").replace("-", "/"));
	if (end < start) {

		alert('结束日期必须大于开始时间');
		return false;
	}
	
	return true;
}

//新增 钟点工订单的   线下支付方式， 红色显示
$("tbody").find("tr").each(function(k, v) {

	var payType = $(this).find("#itemPayType").val();
	var orderStatus = $(this).find("#itemOrderStatus").val();
	
	if (orderStatus == 0 || orderStatus == 1) return false;
	
	if (orderStatus == 2) {
		$(this).attr("style", "color:red");
	} else {
		var staffNums = $(this).find("#staffNums").val();
		var staffName = $(this).find("#staffName").val();
		
		if (staffNums > 0 ) {
			if (staffName == "" || staffName == undefined) {
//				console.log("staffNames = " + staffName);
				$(this).attr("style", "color:red");
			} else {
				
				var staffAry = staffName.split(",");
				
				if (staffAry.length != staffNums) {
//					console.log("staffNums = " + staffNums + " ----- l =" + staffAry.length);
					$(this).attr("style", "color:red");
				}
			}
		}
	}

});



$("#btnSearch").on("click",function() {
	$("#oaSearchForm").attr("action",$("#btnSearch").val());
	$("#oaSearchForm").submit();
});

$("#btnExport").on("click",function() {
	$("#oaSearchForm").attr("action","export_base_order");
	$("#oaSearchForm").submit();
});

function btnDetail(orderNo, orderType, disStatus) {
	if (orderType == 0) {
		location.href = "/" + appName +"/order/order-hour-view?orderNo="+orderNo+"&disStatus="+disStatus;
	}
	
	if (orderType == 1) {
		location.href = "/" + appName + "/order/order-exp-view?orderNo="+orderNo+"&disStatus="+disStatus;
	}
}

//清空查询条件
function cleanForm(){
	$("form :input").not(":button, :submit, :reset, :hidden, :checkbox").val("").remove("selected");
}

$("#submit-remarks").on('click',function(){
	var orderNo = $("#orderNo").val();
	var remarks = $("#remarks").val();
	
	if(remarks==undefined || remarks ==null || remarks=='') return false;
	
	$.ajax({
		type:"POST",
		url:"update-remarks",
		data:{
			"order_no":orderNo,
			"remarks":remarks
		},
		dataType:"json",
		success:function(data){
			console.log("----------------")
		}
	});
	
});

