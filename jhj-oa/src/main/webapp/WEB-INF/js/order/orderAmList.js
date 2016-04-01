
$("tbody").find("tr").each(function(k,v){
	
	var payType =  $(this).find("#itemPayType").val();
	
	if(payType == 6){
		$(this).attr("style","color:red");
		//如果  助理订单的 订单 状态 为已支付, 支付方式为  现金支付。。则将  状态 显示 为 上门收款
		if($(this).find("#itemOrderStatus").val() == 3){
			
			$(this).find("#payTypeStatus").text("上门收款");
		}
	}
	
});

