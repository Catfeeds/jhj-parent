
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
				$(this).attr("style", "color:red");
			} else {
				
				var staffAry = staffName.split(",");
				
				if (staffAry.length != staffNums) {
					$(this).attr("style", "color:red");
				}
			}
		}
		
	}
	
	
	if (payType == 6) {
		$(this).attr("style", "color:red");
		// 如果 钟点工订单的 订单 状态 为已支付, 支付方式为 现金支付。。则将 状态 显示 为 上门收款
		if ($(this).find("#itemOrderStatus").val() == 2) {

			$(this).find("#payTypeStatus").text("上门收款");
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

function orderByTime(){
	var fromdata = $("#oaSearchForm").serialize();
	if($("button[name='orderByProperty']").val()==""){
		fromdata+="&orderByProperty=service_date desc";
	}
	
	$.ajax({
		type:"get",
		url:$("#btnSearch").val(),
		data:fromdata,
		success:function(data){
			var s=$("button[name='orderByProperty']").val();
			if(s==""){
				$("button[name='orderByProperty']").val("service_date");
				$("button[name='orderByProperty']").text("下单时间排序");
			}else{
				$("button[name='orderByProperty']").val("");
				$("button[name='orderByProperty']").text("服务时间排序");
			}
		}
	});
	
}


