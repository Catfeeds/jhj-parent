$('#charge-form').validate({ 
			errorElement: 'span', //default input error message container
			errorClass: 'help-block', // default input error message class
			focusInvalid: false, // do not focus the last invalid input
			rules: {
				chargeMoney: {
					required: true,
					number:true,
					maxlength:9,
					min:0
				},
			},

			messages: {
				chargeMoney	: {
					required: "请输入充值金额",
					number:"请输入数字",
					maxlength:"最多输入9位数字",
					min:"最小金额是0"
				},
			},

			invalidHandler: function (event, validator) { //display error alert on form submit
				$('.alert-error', $('#charge-form')).show();
			},

			highlight: function (element) { // hightlight error inputs
				$(element)
					.closest('.form-group').addClass('has-error'); // set error class to the control group
			},

			success: function (label) {
				label.closest('.form-group').removeClass('has-error');
				label.remove();
			},

			errorPlacement: function (error, element) {
				error.insertAfter(element.parents(".col-md-5"));
			}

});

//页面加载时。

var onloadChargeWay = function(){
	
   var id = $("#chargeWay").find("option:selected").val();	
   
   if(id==0){
   	$("#fixed").show();
   	$("#any").hide();
   }else{
   	$("#any").show();
   	$("#fixed").hide();
   }
}

window.onload = onloadChargeWay;


// 选择 充值方式
$("#chargeWay").on('change',function(){
    
    var id = $(this).find("option:selected").val();
    
    if(id==0){
    	$("#fixed").show();
    	$("#any").hide();
    }else{
    	$("#any").show();
    	$("#fixed").hide();
    }
});



/*
 *  获取验证码
 */
$("#getUserCode").on("click",function(){
		
//	 var mobile = $("#userMobile").val(); 	
	
	var count = 60;
	
	// window 对象, 计时器函数  setInterval()  。。js自己支持
	var countdown = setInterval(CountDown, 1000);
	function CountDown(){
		$("#getUserCode").attr("disabled", true);
		$("#getUserCode").text(count + "秒");
		if (count == 0) {
			$("#getUserCode").removeAttr("disabled");
			$("#getUserCode").text("获取验证码");
			clearInterval(countdown);
		}
		count--;
	}
	
	 var mobile = 13521193653;
	
	 //此处 新增一种  验证码 类型。 表示会员充值时需要的验证码
	 var smsType = 3;
	
	 var userId = $("#userId").val();
	 
	 $.ajax({
		type: "GET",
     	url:"get_user_sms_token.json",
 	    dataType:"json",
 	    data: {
 	    		"userId":userId,
 	    	"userMobile":mobile,
 	          "smsType":smsType 
 	    },
 	    success:function(data,textStatus,jqXHR){
 	    	
 	    	alert(data.msg);
 	    	return false;
 	    },
 	    error:function(){
 	    	alert("网络错误");
 	    }
 	});
     
});

/*
 * 提交 结果
 */

$("#addCoupon_btn").on("click",function(){
	
	var chargeWay = $("#chargeWay").find("option:selected").val();
	
	var chargeMoney = 0;
	
	if(chargeWay == 0){
		//固定金额充值
		chargeMoney = $("#dictCardId").find("option:selected").val();
	}else{
		//任意金额充值
		var money = $("#chargeMoney").val();
		
		if(money.length != 0 ){
			chargeMoney = money;	
		}else{
			
			alert("您还未输入 充值金额");
			return false;
		}
	}
	
	var userId = $("#userId").val();
	
	var userCode = $("#userCode").val();
	
	if(userCode.length == 0){
		alert("请输入验证码");
		return false;
	}
	
	var userMobile = $("#userMobile").val();
	
	 $.ajax({
			type: "post",
	     	url: "charge-form.json",
	 	    dataType:"json",
	 	    data: {
	 	    		"userId":userId,
	 	    	  "userCode":userCode,
	 	         "chargeWay":chargeWay,
	 	       "chargeMoney":chargeMoney,
	 	       "userMobile" :userMobile
	 	    },
	 	    success:function(data,textStatus,jqXHR){
	 	    	
	 	    	alert(data.msg);
	 	    	if(data.status == "999"){
	 	    		
	 	    		return false;
	 	    	}
	 	    	
	 	    	var rootPath = getRootPath();
				window.location.replace(rootPath+"/user/finace_recharge_list");
	 	    	return false;
	 	    },
	 	    error:function(){
	 	    	alert("网络错误");
	 	    }
	 	});
	
});


function getRootPath() {
    var strFullPath = window.document.location.href;
    var strPath = window.document.location.pathname;
    var pos = strFullPath.indexOf(strPath);
    var prePath = strFullPath.substring(0, pos);
    var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
    return (prePath + postPath);
}