myApp.onPageInit('login', function (page) {
	$$('#get_code').on('click',function(e) {
		var mobile = $$("#user_mobile").val();
        if(mobile == undefined || mobile == '') {
        	myApp.alert("请填写手机号。");
            return false;
        }
        var moblieStr = mobile.trim();
        if(moblieStr.length != 11) {
        	myApp.alert("请填写正确的手机号码");
        	return false;
        }
        
        $$("#get_code").css("background","#999");

        var count = 60;
        var countdown = setInterval(CountDown, 1000);
        function CountDown(){
            $$("#get_code").css("background","#999");
            $$("#get_code").text(count + "秒");
            if (count == 0) {
                $$("#get_code").removeAttr("disabled");
                $$("#get_code").css("background","#f37b1d");
                $$("#get_code").text("获取验证码");
                clearInterval(countdown);
            }
            count--;
        }
        
        $$(this).attr("disabled", true);
//        $$("#get_code").attr("disabled", true);
        
        var smsTokenSuccess = function(data, textStatus, jqXHR) {
    		
    		// We have received response and can hide activity indicator
    	   	myApp.hideIndicator();    			
    	   	myApp.alert("验证码已发送到您的手机，请注意查收。");
    	};
        
        var postdata = {};
        postdata.mobile = moblieStr;
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
        
    	
        
        return false;
	});
	
	
	/**
	 *  获取语音验证码	
	 */
	
	$$('#get_code_yuyin').on('click',function(e) {
		var mobile = $$("#user_mobile").val();		
        if(mobile == undefined || mobile == '') {
        	myApp.alert("请填写手机号。");
            return false;
        }
        var moblieStr = mobile.trim();
        if(moblieStr.length != 11) {
        	myApp.alert("请填写正确的手机号码");
        	return false;
        }

        var count = 60;
        var countdown = setInterval(CountDown, 1000);
        function CountDown(){
        	$$("#get_code").attr("disabled", true);
            $$("#get_code").css("background","#999");
            $$("#get_code").text(count + "秒");
            if (count == 0) {
                $$("#get_code").removeAttr("disabled");
                $$("#get_code").css("background","#f37b1d");
                $$("#get_code").text("获取验证码");
                clearInterval(countdown);
            }
            count--;
        }

        var postdata = {};
        postdata.mobile = moblieStr;
        postdata.sms_type = 0;

        $$.ajax({
        	url:siteAPIPath+"user/get_yu_yin_token.json",
//    		headers: {"X-Parse-Application-Id":applicationId,"X-Parse-REST-API-Key":restApiKey},
        	contentType:"application/x-www-form-urlencoded; charset=utf-8",
        	type: "GET",
    	    dataType:"json",
    	    cache:true,
    	    data: postdata,

    	    statusCode: {
    	    	201: voiceTokenSuccess,
    	    	400: ajaxError,
    	    	500: ajaxError
    	    }
    	});
        
    	var voiceTokenSuccess = function(data, textStatus, jqXHR) {
    		
    		// We have received response and can hide activity indicator
    	   	myApp.hideIndicator();    			
    	   	myApp.alert("验证码已发送到您的手机，请注意查收。");
    	};
        
        return false;
	});
	
	 //登录
    $$('#login_btn').on('click', function(e){
    	
    	
    	
        //var formData = $('#loginform').serialize();
    	var mobile = $$("#user_mobile").val();
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
    	   	
    	  //2015-11-5 16:01:40 防止重复点击，造成多条重复用户
    		$$("#login_btn").removeAttr('disabled');  
    	   	
    	   	
    	   	
    	   	var result = JSON.parse(data.response);
    	   	if (result.status == "999") {
    	   		myApp.alert(result.msg);
    	   		return;
    	   	}
    	   	
    	   	if (result.status == "0") {
    	   	  //登录成功后记录用户有关信息

    	   	  localStorage.setItem("user_mobile",result.data.mobile);
    	   	  
    	   	  localStorage.setItem("user_id",result.data.id);
    	   	  
    	   	  localStorage.setItem("is_vip",result.data.is_vip);
    	   	  	    	  
	    	  //如果有默认地址则设置为默认地址
	    	  var userAddr = result.default_user_addr;

	    	  if (userAddr != undefined && userAddr != null) {
	    		  if (userAddr.is_default == 1) {
	    			  
	    			  localStorage.setItem('default_addr_id', userAddr.id);
	    			  localStorage.setItem('default_addr_name', userAddr.name + " " + userAddr.addr);	
	    		  }
	    	  }
    	   	  
    	   	}
    	   	var flag =result.data.has_user_addr;
    	   	if(flag ==true){
    	   	//返回用户浏览的上一页
	   			var target = mainView.history[mainView.history.length-2];
	   			
	   			mainView.router.loadPage(target);
    	   //mainView.router.loadPage("index.html");
    	   	}else{
    	   		mainView.router.loadPage("user/mine-add-addr.html");
    	   	}
    	   
    	};                
        
        
    	
    	
    	
    	//2015-11-5 16:01:40    防止重复点击，造成多条重复用户
    	$$(this).attr("disabled", true);
    	
    	
    	
        var postdata = {};
        postdata.mobile = mobile;
        postdata.sms_token = verifyCode;        
        postdata.login_from = 1;
        postdata.user_type = 0;

        $$.ajax({
            type : "POST",
            // type : "GET",
            url  : siteAPIPath+"user/login.json",
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
	
	$$(".yanzheng1").click(function(){
        $$(".login-tishi1").css("display","block");
    })
});
    
