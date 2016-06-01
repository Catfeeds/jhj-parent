myApp.onPageInit('order-hour-view-0-page', function (page) {
	
	var userId = localStorage['user_id'];
	
	var orderNo = page.query.order_no;
	
	var isWx = isWeiXin();
	
//	console.log("isWx == " + isWx);
	if (isWx) {
		$$("#select-wxpay").css("display", "block");
		$$("#select-alipay").css("display", "none");
	} else  {
		$$("#select-wxpay").css("display", "none");
		$$("#select-alipay").css("display", "block");
	}
		
	// 有 支付选项时，所需字段
	var order_type = $$("#orderType").val();
	var order_money = delSomeString($$("#orderMoney").text());
	
	var orderStatus = $("#orderStatus").val();
	
	//根据状态是否显示支付. (只有待支付 状态的 订单。显示 支付 选项)
	if(orderStatus == 1){
		$$("#user_coupon_view_li").hide();  // 优惠券选项
		$$("#order_pay_view_li").hide();	// 实际金额
		
		$$("#hour-pay-submit").show();		//支付按钮
		
		//支付信息展现
		$$(".hour-pay-coupon-select").show();		//优惠券选择
		$$(".hour-pay-type-choose").show();			//支付类型选择
		
		//点击选择优惠劵
		$$("#selectCoupon").click(function() {
			var linkUrl = "user/coupon/mine-coupon-list.html";
			linkUrl+= "?order_type="+order_type;
			linkUrl+= "&order_money="+order_money;
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
			
			var realMoney = parseFloat(order_money) - parseFloat(userCouponValue);
			if(realMoney < 0){
				realMoney = 0;
			}
			$$("#real-order-money-label").html('￥'+realMoney);
		}

		}else{
			
			// 除 待支付状态之外，只展示  优惠券、实际金额 相关数值
			$$("#hour-pay-submit").hide();			//支付 按钮
			$$(".hour-pay-coupon-select").hide();   // 优惠券选择  列表
			$$(".hour-pay-type-choose").hide();		// 支付类型选择  
			$$("#user_coupon_view_li").show();	   // 优惠券
			$$("#order_pay_view_li").show();       // 实际支付金额
		}

		/*
		 * 	jhj2.1   对于钟点工 订单， 已支付=2 ，已派工=3 ，可以取消订单
		 */
	
		if(orderStatus == 2 ||  orderStatus == 3){
			$$("#cancleOrder").show();
		}else{
			$$("#cancleOrder").hide();
		}
		
//		//待评价订单，显示 评价按钮
//		if(orderStatus == 6){
//			$$("#rate").show();
//		}else{
//			$$("#rate").hide();
//		}
//		
//		//已评价 ，显示 查看评价 按钮
//		if(orderStatus == 7){
//			$$("#getRate").show();
//		}else{
//			$$("#getRate").hide();
//		}
	
	
	
	var postHourOrderPaySuccess =function(data, textStatus, jqXHR) {
		
		
		
	};	
	
	//选择支付类型
	$$('label[name=myPayTypeSelect]').on('click',function(){
		
		var nowValue = $$(this).find("input").val();
		
		//单选
		$$('img[name="order_pay_type"]').each(function(index,value){
			if($$(this).prev().val() == nowValue){
				$$(this).attr("src","img/icons/duigou-green.png");
			}else{
				$$(this).attr("src","img/icons/duigou-grey.png");
			}
			
		});
		
	});
	
	var payAjax = function(){
			
		

		var userCouponId = $$("#user_coupon_id").val();
		if (userCouponId == undefined) userCouponId = 0;
		var postdata = {};
		postdata.user_id = userId;
		postdata.order_no = orderNo;
		postdata.user_coupon_id = userCouponId;
				
		
		$$('img[name="order_pay_type"]').each(function(index,value){
			if($$(this).attr("src").indexOf("green")>0){
				orderPayType = $$(this).prev().val();
			}
			
		});
		
		postdata.order_pay_type = orderPayType;
		
		
		$$.ajax({
			type: "post",
			 url: siteAPIPath + "order/post_pay.json",
			data: postdata,
			timeout : 20000, //超时时间设置，单位毫秒
			success:function(xhr,data,status) {
	        	
				$$("#hour-pay-submit").removeAttr('disabled');
				
			 	var result = JSON.parse(data.response);
				if (result.status == "999") {
					myApp.alert(result.msg);
					
					// 重复支付或者订单不存在等情况，禁用支付按钮
					$$(".zf-hour-submit").attr("disabled", true);
					
					return;
				}
				
				//无可用派工的情况
				if(result.status == "102"){
					
					//先禁用 支付按钮
					$$(".zf-hour-submit").attr("disabled", true);
					
					//前往  呼叫助理
					
					myApp.alert("请联系助理,调整服务时间");
					mainView.router.loadPage("user/user-am-detail.html");
					return;
				}
				
				orderPayType = result.data.pay_type;
				orderType = result.data.order_type;
				console.log("orderPayType = " + orderPayType);
				//orderPayType = result.data.pay_type;
				//如果为余额支付，则直接跳到完成页面
				if (orderPayType == 0 || orderPayType == 6) {
					mainView.router.loadPage("order/order-pay-success.html?order_no="+orderNo+"&order_type=0");
				}
				
				//如果为支付宝支付，则跳转到支付宝手机网页支付页面
				if (orderPayType == 1) {
					var orderPay = result.data.order_pay;
					var alipayUrl = localUrl + "/" + appName + "/pay/alipay_order_api.jsp";
					alipayUrl +="?orderNo="+orderNo;
					alipayUrl +="&orderPay="+orderPay;
					alipayUrl +="&orderType="+orderType;
					location.href = alipayUrl;
				}
				
				var orderId = $$("#orderId").val();
				
				//如果为微信支付，则需要跳转到微信支付页面.
				if (orderPayType == 2) {
					 var userCouponId = $$("#user_coupon_id").val();
					 if (userCouponId == undefined) userCouponId = 0;
					 var wxPayUrl = localUrl + "/" + appName + "/wx-pay-pre.jsp";
					 wxPayUrl +="?orderId="+orderId;
					 wxPayUrl +="&userCouponId="+userCouponId;
					 wxPayUrl +="&orderType=1";
					 wxPayUrl +="&payOrderType=0";
					 location.href = wxPayUrl;
				}
		    },
		    error:function(xhr,status){
		    	myApp.alert("网络环境较差,请稍候重试");
		    },
		    complete:function(xhr,status){
		    	
		    	if(status == 'timeout'){
		    		
		    		payAjax.abort();
		    		
		    		myApp.alert("网络环境较差,请稍后重试");
		    	}
		    }
		});	
	}
	
	
	//点击支付的处理
	$$("#hour-pay-submit").click(function(){
		
		$$("#hour-pay-submit").attr("disabled", true);
		
		payAjax();
	});			
	
	// 点击 评价 操作（2016年5月4日10:16:01 已无用）
	$$("#rate").on('click',function(){
		
		if(orderStatus == 7){
			myApp.alert("您已经评价过啦");
			return false;
		}
		
		var orderId = $$("#orderId").val();
		mainView.router.loadPage("order/order-rate.html?order_id="+orderId);
		
		
	});
	
	//点击 查看评价 操作（2016年5月4日10:16:06 已无用）
	$$("#getRate").on('click',function(){
		var orderId = $$("#orderId").val();
		mainView.router.loadPage("order/order-get-rate.html?order_id="+orderId);
	});
	
	
	//取消订单操作
	$$("#cancleOrder").on('click',function(){
		
//		var orderNo = localStorage['u_order_no_param'];
		
		var dataCancle = {};
		dataCancle.order_no = orderNo;
		
		//根据服务时间，设置取消提示
		var text = "";
		
		// 服务时间  unix时间戳
		var aaa =   $$("#serviceDate").text();
		var bb =  (new Date(aaa).getTime()/1000).toString();
		
		
		//当前时间 unix时间戳
		var now = Math.round(new Date().getTime()/1000);
		
		//时间差
		var diffValue = Number(bb) - Number(now);
		
		//服务开始前两小时之内
		if(diffValue <= 7200 && diffValue >0){
			text = "服务已经快开始了，现在取消订单会扣除全部费用哦，确定取消吗?如有问题请联系客服";
		}else{
			//逻辑上，服务时间内，还未支付的订单，已经变为 已关闭了
			text = "现在取消订单，订单金额会全部退到您的账号余额，确定取消订单吗?";
		}
		
		myApp.confirm(text,function(){
			$$.ajax({
				type:"post",
				url: siteAPIPath+"order/cancle_am_order.json",
				data : dataCancle,
				cache: true, 
				success: function(datas,status,xhr){
					 
					 var result = JSON.parse(datas);
					 if(result.status == 0){
						 myApp.alert(result.msg);
						 mainView.router.loadPage("order/order-cal.html");
					 }
					 if(result.status == 999){
						 myApp.alert(result.msg);
						 
						 $$("#cancleOrder").attr("disabled",true);
						 return false;
					 }
				}
			});
		});
	});
});


myApp.template7Data['page:order-hour-view-0-page'] = function(){
    var result; 
    var postdata = {};
	var order_no = localStorage['u_order_no_param'];
//    var order_no = page.query.order_no;
	postdata.order_no = order_no;

   $$.ajax({
      type : "GET",
      url:siteAPIPath + "order/order_hour_detail.json",
      dataType: "json",
      data : postdata,
      cache : true,
      async : false,	// 不能是异步
      success: function(data){
		  console.log(data);
	      result = data.data;
          
          //设置时间显示格式
          var timestamp = moment.unix(result.service_date);
		  var startTime = timestamp.format('YYYY-MM-DD HH:mm');
		  result.service_date = startTime;
		 
      }	
   })
  
  return result;
}


