$("#sraffCashForm_btn").click(function() {
	if (confirm("确认要保存吗?")) {
			$('#staffCash-form').submit();
	}
});
/*("#sraffCashForm_btn").click(function() {
	if (confirm("确认要审批吗?")) {
		$('#staffCash-form').submit();
		if ($('#staffCash-form').validate().form()) {
			$('#staffCash-form').submit();
		}
	}
});*/
/*$("#orderStatus").change(function(){ 
	var orderStatus = $(this).children('input:radio[name=orderStatus]:checked').val();
	if (orderStatus == "0" || orderStatus == "1") {
		$("#remarks").css('display', 'none');
	}
	//$('input:radio[name=sex]:checked').val();
	
	if (orderStatus == "2" || orderStatus == "3") {
		$("#remarks").css('display', 'block');
	}
});*/