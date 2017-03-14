myApp.onPageInit('firstOrderHalf', function (page) {
	
	$$("#get-active-coupons").on('click',function(){
		var mobile = $$("#mobile").val();
		if(mobile == undefined || mobile == '') {
        	myApp.alert("请填写手机号。");
            return false;
        }
        var moblieStr = mobile.trim();
        if(moblieStr.length != 11) {
        	myApp.alert("请填写正确的手机号码");
        	return false;
        }
		var couponsIdList = '4191';
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
				mainView.router.loadPage("active/half-success.html");
			}
		});
	});

});

