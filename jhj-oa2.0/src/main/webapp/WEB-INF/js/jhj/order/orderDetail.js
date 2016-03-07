function doReset() {
	$("#orderNo1").attr("value", "");
	$("#mobile1").attr("value", "");
	$("#st").attr("disabled", true);

}
$(function() {

	if ($.trim($("#servicedate").val()).length == 0) {
		$("#st").attr("disabled", true);
	}
})
function checkSt() {
	if ($.trim($("#servicedate").val()).length > 0) {
		$("#st").attr("disabled", false);
	}
}