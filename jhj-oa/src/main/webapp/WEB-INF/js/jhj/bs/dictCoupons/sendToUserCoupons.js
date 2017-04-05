$("#btn-send").on("click",function(){
		var mobiles = $("#mobiles").val();
		if(mobiles==undefined || mobiles==null || mobiles ==''){
			alert("手机号不能为空");
			return false;
		}
		
		var param = $("form").serialize();
		
		$.ajax({
			type:"post",
			url:"sendToUserCoupons",
			data:param,
			async:false,
			dataType:"json", 
			success:function(data){
				if(data>0){
					alert("发送成功");
					location.href = "recharge-coupon-list";
				}
			}
		});
		
	});