$(document).ready(function() {
	// 根据 url 参数 ，控制 div 是否显示
	var url = window.location.search;
	// url 参数 最后 一位 即为 派工状态。
	var disStatu = url.charAt(url.length - 1);

	// 只有 订单为已支付状态=4 ，显示确认派工按钮+可用服务人员列表
	if (disStatu == 4) {
		$("#nowStaff").hide();
	} else if (disStatu == 5 || disStatu == 7 || disStatu == 9) {
		/**
		 * 当订单状态为开始服务=5 完成服务=7，已关闭=9显示当前服务人员
		 */
		$("#allStaff").hide();
		$("#viewForm").hide();
	} else {
		$("#allStaff").hide();
		$("#viewForm").hide();
		$("#nowStaff").hide();
	}

});

$("#viewForm").click(function(form) {

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
});