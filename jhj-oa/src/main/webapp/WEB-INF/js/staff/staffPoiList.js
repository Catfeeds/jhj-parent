$("tbody").find("tr").each(function(k, v) {
	var todayPoiStatus = $(this).find("#todayPoiStatus").val();
	if (todayPoiStatus == 0) {
		$(this).attr("style", "color:red");
	}
});