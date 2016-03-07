$(document).ready(function(){
	//根据 url 参数 ，控制  div 是否显示
	var url = window.location.search;
	// url 参数 最后 一位 即为  派工状态。
	var disStatu =  url.charAt(url.length - 1);
	
	//只有 派工状态 为 1 ，即 当前 派工 成功时。才显示  当前阿姨、可用阿姨 两个 div
	if(disStatu == 0 || disStatu == 3){   // 0：无效派工     3：不是状态为 已支付 的 订单， 不能派工
		$("#nowStaff").hide();
		$("#allStaff").hide();
		
		$("#viewForm").hide();
	}
	
	
	
});



$("#viewForm").click(function(form) {

	//单选按钮，不用处理 ,框架根据 name自动提交
	
	if (confirm("确认要换人吗?")) {
		if ($('#viewForm').validate().form()) {
			$('#viewForm').submit();
		}
	}else{
		return false;
	}
});