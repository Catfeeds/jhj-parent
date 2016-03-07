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

/*$("#viewForm").click(function(form) {
	// 单选按钮，不用处理 ,框架根据 name自动提交
	if (confirm("确认要派工吗?")) {
		if ($('#viewForm').validate().form()) {
			var val = $('input:radio[name="staffId"]:checked').val();
			if (val == null) {
				alert("请选择派工人员");
				return false;
			} else {
				$('#viewForm').submit();
			}
		}
	} else {
		return false;
	}
});*/