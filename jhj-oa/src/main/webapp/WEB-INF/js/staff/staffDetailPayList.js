
$('.form_datetime').datepicker({
	format: "yyyy-mm-dd",
	language: "zh-CN",
	autoclose: true,
	startView: 0,
	todayBtn:true
});

function checkEndTime(){  
    var startTime=$("#startTimeStr").val();  
    var start=new Date(startTime.replace("-", "/").replace("-", "/"));  
    var endTime=$("#endTimeStr").val();  
    var end=new Date(endTime.replace("-", "/").replace("-", "/"));  
    if(end<start){ 
    	
    	alert('结束日期必须大于开始时间');
    	 BootstrapDialog.alert({
    			 title:'提示语',
    			 message:'结束日期必须大于开始时间!'
    	 });
    	 return false;  
    }  
    return true;  
} 

function searchSubmit() {
	var actionUrl = "/jhj-oa/staff/staffPay-list";
	$("#searchForm").attr("action", actionUrl);
	$("#searchForm").submit();
}

function exportStaffOrder() {
	var selectStaff = $("#selectStaff").val();
	console.log("selectStaff = "+ selectStaff); 
	if (selectStaff == undefined || selectStaff == "" || selectStaff == 0) {
		alert("请选择服务人员.");
		return false;
	}

	var startTimeStr = $("#startTimeStr").val();
	if (startTimeStr == undefined || startTimeStr == "") {
		alert("请选择开始时间.");
		return false;
	}
	
	var endTimeStr = $("#endTimeStr").val();
	if (endTimeStr == undefined || endTimeStr == "") {
		alert("请选择结束时间.");
		return false;
	}
	
	var exportUrl = "/jhj-oa/staff/export-order";
	$("#searchForm").attr("action", exportUrl);
	$("#searchForm").submit();
}


function exportStaffPayDept() {
	var selectStaff = $("#selectStaff").val();
	console.log("selectStaff = "+ selectStaff); 
	if (selectStaff == undefined || selectStaff == "" || selectStaff == 0) {
		alert("请选择服务人员.");
		return false;
	}
	
	var exportUrl = "/jhj-oa/staff/export-pay-dept";
	$("#searchForm").attr("action", exportUrl);
	$("#searchForm").submit();
}

$("#cleanBtn").on('click',function(){
	$("form :input").not(":button, :submit, :reset, :hidden,:selected").val("");
});