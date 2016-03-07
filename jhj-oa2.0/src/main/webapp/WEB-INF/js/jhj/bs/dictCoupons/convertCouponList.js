function doSearch() {
	var startTime = $("#fromDate").val();
	var start = new Date(startTime.replace("-", "/").replace("-", "/"));
	var endTime = $("#toDate").val();
	var end = new Date(endTime.replace("-", "/").replace("-", "/"));
	if (end < start) {
		BootstrapDialog.alert({
			title : '提示语',
			message : '结束日期必须大于开始时间!'
		});
		return false;
	} else {
		$("#convertForm").submit();
	}
}
function doReset(){
	$("#fromDate").val("");
	$("#toDate").val("");
}
$('.form_datetime').datepicker({
	format : "yyyy-mm-dd",
	language : "zh-CN",
	autoclose : true,
	startView : 1,
	todayBtn : true,
	pickerPosition : "bottom-left"

});
function download() {
	var startTime = $("#fromDate").val();
	var endTime = $("#toDate").val();
	if (startTime == undefined || startTime == null) {
		startTime = "";
	}
	if (endTime == undefined || endTime == null) {
		endTime = "";
	}
	window.location.href = "/" + appName + "/bs/download_project?startDate="
			+ startTime + "&endDate=" + endTime;
}