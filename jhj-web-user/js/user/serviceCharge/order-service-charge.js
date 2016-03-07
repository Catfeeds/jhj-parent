myApp.onPageBeforeInit('order-service-charge-page', function(page) {
	
	var userId = localStorage.getItem("user_id");

	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	$$("#userId").val(userId);
	
	//加载页面时的默认值  
	
	
	//选择优惠券后的回显 以及加载时的 回显
	var oldChargeMoney = sessionStorage['oldSelectChargeMoney'];
	if(oldChargeMoney != undefined){
		
		var thisCouponValue =  page.query.user_coupon_value;
		
		if(thisCouponValue == undefined){
			thisCouponValue = 0;
		}
		
		$$("#chargeMoney").val(oldChargeMoney);
		
		if(oldChargeMoney === "20" || oldChargeMoney === "30"){
			
			$$("#realMoney").html("");
			$$("#realMoney").html('￥'+"<font color='red'>"+(Number(oldChargeMoney)-Number(thisCouponValue)).toFixed(2)+"</font>");
			$$("#paramRealMoney").val(oldChargeMoney);
			
		}
		
		if(oldChargeMoney === "50"){
			$$("#realMoney").html('￥'+"<font color='red'>"+(Number($$("#selectFivty").val())-Number(thisCouponValue)).toFixed(2)+"</font>");
			
			$$("#paramRealMoney").val(Number($$("#selectFivty").val())-Number(thisCouponValue).toFixed(2));
			
		}
		
		if(oldChargeMoney === "100"){
			$$("#realMoney").html('￥'+"<font color='red'>"+(Number($$("#selectOneHundred").val())-Number(thisCouponValue)).toFixed(2)+"</font>");
			
			$$("#paramRealMoney").val((Number($$("#selectOneHundred").val())-Number(thisCouponValue)).toFixed(2));
			
		}
		
		if(oldChargeMoney === "200"){
			$$("#realMoney").html('￥'+"<font color='red'>"+(Number($$("#selectTwoHundred").val())-Number(thisCouponValue)).toFixed(2)+"</font>");
			
			$$("#paramRealMoney").val((Number($$("#selectTwoHundred").val())-Number(thisCouponValue)).toFixed(2));
			
		}
		
		
	}else{
		$$("#selectMoney").text(20);
		
		$$("#chargeMoney").val(20);
		$$("#realMoney").html('￥'+"<font color='red'>"+Number(20).toFixed(2)+"</font>");
		$$("#paramRealMoney").val(20);
	}
	
	var oldChargeMobile = sessionStorage['oldChargeMobile'];
	if(oldChargeMobile !=''){
		$$("#chargeMobile").val(oldChargeMobile);
	}
	
	var oldReChargeMobile = sessionStorage['oldReChargeMobile'];
	if(oldReChargeMobile !=''){
		$$("#reChargeMobile").val(oldReChargeMobile);
	}
	
	//充值面值 选择
	$$("#chargeMoney").on('change',function(){
		
		var thisMoney = $$(this).val();
		
		var thisCouponValue =  page.query.user_coupon_value;
		
		if(thisCouponValue == undefined){
			thisCouponValue = 0;
		}
		
		/*
		 *	2015-11-18 13:51:47 由于生成的优惠券有问题。会出现 负值，特此更改
		 *
		 * 
		 */
		
		//同步显示当前的实际支付金额
		
		if(thisMoney == ""){
			$$("#realMoney").html("");
			$$("#paramRealMoney").val("");
			sessionStorage.setItem('oldChargeMoney',thisMoney);
		}
		
		if(thisMoney === "20" || thisMoney === "30"){
			
			/*
			 * 2015-11-18 13:53:17  解决 负值 问题
			 */
			var myParamRealMoney = Number(thisMoney)-Number(thisCouponValue);
			
			if(myParamRealMoney <= 0){
				myParamRealMoney = 0;
			}
			
			
			//当前实际支付金额    toFixed(2) js自带方法 保留两位小数
			$$("#realMoney").html('￥'+"<font color='red'>"+myParamRealMoney.toFixed(2)+"</font>");
			//当前作为参数的实际金额
			$$("#paramRealMoney").val(myParamRealMoney.toFixed(2));
			//当前作为跳转到 优惠券列表时的  参数
			sessionStorage.setItem('oldChargeMoney',thisMoney);
		}
		
		
		
		if(thisMoney === "50"){
			
			var myParamRealMoney = Number($$("#selectFivty").val())-Number(thisCouponValue);
			
			if(myParamRealMoney <= 0){
				myParamRealMoney = 0;
			}
			
			
			
			$$("#realMoney").html('￥'+"<font color='red'>"+myParamRealMoney.toFixed(2)+"</font>");
			
			$$("#paramRealMoney").val(myParamRealMoney.toFixed(2));
			
			
			sessionStorage.setItem('oldChargeMoney',$$("#selectFivty").val());
		}
		
		if(thisMoney === "100"){
			
			var myParamRealMoney = Number($$("#selectOneHundred").val())-Number(thisCouponValue);
			
			if(myParamRealMoney <= 0){
				myParamRealMoney = 0;
			}
			
			
			
			$$("#realMoney").html('￥'+"<font color='red'>"+myParamRealMoney.toFixed(2)+"</font>");
			
			$$("#paramRealMoney").val(myParamRealMoney.toFixed(2));
			
			sessionStorage.setItem('oldChargeMoney',$$("#selectOneHundred").val());
		}
		
		if(thisMoney === "200"){
			
			var myParamRealMoney = Number($$("#selectTwoHundred").val())-Number(thisCouponValue);
			
			if(myParamRealMoney <= 0){
				myParamRealMoney = 0;
			}
			
			
			$$("#realMoney").html('￥'+"<font color='red'>"+	myParamRealMoney.toFixed(2)+"</font>");
			
			$$("#paramRealMoney").val(myParamRealMoney.toFixed(2));
			
			sessionStorage.setItem('oldChargeMoney',$$("#selectTwoHundred").val());
		}
		
		//存储当前选择的优惠券，用于回显
		sessionStorage.setItem('oldSelectChargeMoney',thisMoney);
		
	});
	
	/*	
	 * 点击选择优惠券
	 * 	  sessionStorage 存储充值页面变化过的 变量值, 便于回显
	 * 
	 */
	$$("#selectCoupon").click(function() {
		
//		var oldMoney =  sessionStorage['oldChargeMoney']
		
		var oldMoney = $$("#selectMoney").text();
		
		if(oldMoney == undefined || oldMoney == ""){
			myApp.alert("请您先选择充值面值");
			return ;
		}
		
		//存储 已经 输入或选择过的 值 
		sessionStorage.setItem('oldChargeMobile',$$("#chargeMobile").val());
		sessionStorage.setItem('oldReChargeMobile',$$("#reChargeMobile").val());
		
		var order_type = $$("#orderType").val();
		
		//util.js  处理  ￥符号
//		var order_money = delSomeString(money);
		
		localStorage.setItem('u_order_money_param', sessionStorage['oldChargeMoney']);
		
		
		var linkUrl = "user/coupon/mine-coupon-list.html";
		linkUrl+= "?order_type="+order_type;
//		linkUrl+= "&order_money="+order_money;
		linkUrl+= "&order_money="+oldMoney;
		mainView.router.loadPage(linkUrl);
	});
	
	//用户返回优惠劵之后的处理
	var userCouponId = page.query.user_coupon_id;
	var userCouponName = page.query.user_coupon_name;
	var userCouponValue = page.query.user_coupon_value;
	if (userCouponId != undefined && userCouponName != undefined) {
		$$("#user_coupon_id").val(userCouponId);
		$$("#couponSelect").html("为您节省了"+userCouponValue+"元");
		if (userCouponValue == undefined) userCouponValue = 0;
		
		/*
		 * 2015-11-2 20:24:42 选完优惠券应该是   初始 实际金额的基础上 再减去 优惠券面值！！
		 */
		
//		alert(sessionStorage['oldChargeMoney']);
		
		
		/*
		 * 2015-11-16 14:53:45
		 * 		
		 * 	当用户进入页面，无任何操作,
		 */
		
		
		var realMoney = 0;
		
		if(sessionStorage['oldChargeMoney'] == undefined){
			realMoney = $$("#paramRealMoney").val() - userCouponValue;
		}else{
			realMoney = sessionStorage['oldChargeMoney'] - userCouponValue;
		}
		
		if(realMoney <= 0 || isNaN(realMoney)){
			realMoney = 0;
		}
		$$("#realMoney").html('');
		$$("#realMoney").html('￥'+"<font color='red'>"+realMoney.toFixed(2)+"</font>");
		
		//作为参数的 实际支付金额, toFixed(2) js自带方法 保留两位小数
		$$("#paramRealMoney").val(realMoney.toFixed(2));
	}
	
	
	
	var rechargeSubmitSuccess =function(data, textStatus, jqXHR) {
		
		//ajax 请求返回时，清除session中的数据
		
		sessionStorage.clear();
		
		
	 	var result = JSON.parse(data.response);
	 	
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}
		var orderId = result.data.id;		
		 //只支持微信支付，则需要跳转到微信支付页面.
		
		var userCouponId = $$("#user_coupon_id").val();
		if (userCouponId == "") userCouponId = 0;
//		alert(userCouponId);
		var wxPayUrl = localUrl + "/" + appName + "/wx-pay-pre.jsp";
		
		 wxPayUrl +="?orderId="+orderId;
		 wxPayUrl +="&userCouponId="+userCouponId;	// 0,表示 为不使用 优惠券  ； 大于0 表示使用的优惠券 面值
		 wxPayUrl +="&orderType=6";		// 6 表示  话费、水电煤气类订单类型
		 wxPayUrl +="&payOrderType=2";	// 0=订单支付   1=充值卡支付  2=充话费支付
		 location.href = wxPayUrl;
	};		

	
	//立即充值
	$$("#submitAtOnce").on('click',function(){
		
		//校验
		if(!orderServiceChargePageValidate()){
			return false;
		};
		
		var formData = myApp.formToJSON('#order-service-charge-form');
		
		formData.realMoney = $$("#paramRealMoney").val();
		
		var userCouponId = $$("#user_coupon_id").val();
		if(userCouponId == ""){
			formData.couponId = 0;
		}else{
			formData.couponId = userCouponId;
		}
		
		$$.ajax({
			type: "post",
			 url: siteAPIPath + "order/recharge_submit.json",
			data: formData,
			statusCode: {
	         	200: rechargeSubmitSuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});		
		 
	});	

});

function orderServiceChargePageValidate(){
	
	var phone = $$("#chargeMobile").val();
	
	var rePhone = $$("#reChargeMobile").val();
	
	if(Trim(phone).length != 11 || !IsNum(phone)){
		myApp.alert("请输入11位合法的手机号");
		return false;
	}
	
	if(Trim(phone).length == 0){
		myApp.alert("请再次确认充值号码");
		return false;
	}
	
	if(phone !== rePhone){
		myApp.alert("两次输入手机号码不一致");
		return false;
	}
	
	var money = $$("#paramRealMoney").val();
	
	if(money == undefined || money == ""){
		myApp.alert("请您选择充值面值");
		return false;
	}
	
	return true;
}

//去除空格，避免输入空格字符
function Trim(str){
	str  =   str.replace(/\s+/g,"");    
    return str;
}

//判断是数字
function IsNum(num){
	  var reNum=/^\d*$/;
	  return(reNum.test(num));
}

//强制保留两位小数  ，用于显示实际支付金额
function toDecimal2(x) {    
    var f = parseFloat(x);    
    if (isNaN(f)) {    
        return false;    
    }    
    var f = Math.round(x*100)/100;    
    var s = f.toString();    
    var rs = s.indexOf('.');    
    if (rs < 0) {    
        rs = s.length;    
        s += '.';    
    }    
    while (s.length <= rs + 2) {    
        s += '0';    
    }    
    return s;    
}    


	

