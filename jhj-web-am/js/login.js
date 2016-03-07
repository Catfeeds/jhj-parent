myApp.onPageInit('login', function (page) {
		
	$$('#get_code').on('click',function(e) {
		
		var mobile = $$("#am_mobile").val();		
        if(mobile == undefined || mobile == '') {
        	myApp.alert("请填写手机号。");
            return false;
        }
        var mobileStr = mobile.trim();
        if(mobileStr.length != 11) {
        	myApp.alert("请填写正确的手机号码");
        	return false;
        }

        var count = 60;
        var countdown = setInterval(CountDown, 1000);
        function CountDown(){
        	$$("#get_code").attr("disabled", true);
            $$("#get_code").css("background","#999");
            $$("#get_code").text(count + "秒后重新获取");
            if (count == 0) {
                $$("#get_code").removeAttr("disabled");
                $$("#get_code").css("background","#f37b1d");
                $$("#get_code").text("获取验证码");
                clearInterval(countdown);
            }
            count--;
        }

        var postdata = {};
        postdata.mobile = mobileStr;
        postdata.sms_type = 0;

        $$.ajax({
        	url:siteAPIPath+"user/get_sms_token.json",
//    		headers: {"X-Parse-Application-Id":applicationId,"X-Parse-REST-API-Key":restApiKey},
        	contentType:"application/x-www-form-urlencoded; charset=utf-8",
        	type: "GET",
    	    dataType:"json",
    	    cache:true,
    	    data: postdata,

    	    statusCode: {
    	    	201: smsTokenSuccess,
    	    	400: ajaxError,
    	    	500: ajaxError
    	    }
    	});
        
    	var smsTokenSuccess = function(data, textStatus, jqXHR) {
    		
    		// We have received response and can hide activity indicator
    	   	myApp.hideIndicator();    			
    	   	myApp.alert("验证码已发送到您的手机，请注意查收。");
    	};
        
        return false;
	});
	
	
	 //登录
    $$('#login_btn').on('click', function(e){
        //var formData = $('#loginform').serialize();
    	var mobile = $$("#am_mobile").val();
    	var verifyCode = $$("#verify_code").val();
        
        if(mobile == '' || verifyCode == '') {
          myApp.alert("请填写手机号或验证码。");
          return false;
        }
        
        if(mobile.length != 11 ) {
          myApp.alert("请填写正确的手机号码");
          return false;
        }

        var loginSuccess = function(data, textStatus, jqXHR) {
    		// We have received response and can hide activity indicator
    	   	myApp.hideIndicator();
    	   	
    	   	var result = JSON.parse(data.response);
//    	   	console.log(result);
//    	   	console.log(result.status);
//    	   	console.log(result.data.id);
//    	   	console.log(result.data.mobile);
    	   	if (result.status == "999") {
    	   		myApp.alert(result.msg);
    	   		return;
    	   	}
    	   	
    	   	if (result.status == "0") {
    	   	  //登录成功后记录用户有关信息
    	   		console.log(result.data+"---"+result.data.mobile);
    	   	  localStorage['am_mobile'] = result.data.mobile;
    	   	  localStorage['am_id']= result.data.staff_id;
    	   	}
    	   	
    	   	mainView.router.loadPage("order/order-list.html");
    	};                
        
        
        var postdata = {};
        postdata.mobile = mobile;
        postdata.sms_token = verifyCode;        
        postdata.login_from = 1;
        postdata.user_type = 2;

        $$.ajax({
            type : "POST",
            // type : "GET",
            url  : siteAPIPath+"am/login.json",
            dataType: "json",
//            contentType:"application/x-www-form-urlencoded; charset=utf-8",
            cache : true,
            data : postdata,
            
            statusCode: {
            	200: loginSuccess,
    	    	201: loginSuccess,
    	    	400: ajaxError,
    	    	500: ajaxError
    	    }
        });


        
        return false;
    });	
	
	
});







    
