myApp.onPageInit('twoyears-active-coupons', function (page) {
	
	$$("#get-active-coupons").on('click',function(){
		var mobile = $$("#mobile").val();
		
		var couponsIdList = '4171,4169,4168,4167,4170';
		var host = window.location.host;
		var appName = "jhj-app";
		var localUrl = "http://" + host;
		var siteAPIPath = localUrl + "/" + appName + "/app/";
		
		$$.ajax({
			type:"post",
			url: siteAPIPath + "/user/receive_coupon.json",
			data:{"mobile":mobile,"coupons_id":couponsIdList},
			success:function(data){
				
				var status = data.status;
				if (status == "999") {
					alert(data.msg);
					return false;
				}
				
				mainView.router.loadPage("active/twoyears-active-getCoupons.html");
			}
		});
		
		
	});

});

