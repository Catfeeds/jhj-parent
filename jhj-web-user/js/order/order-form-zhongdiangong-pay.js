myApp.onPageInit('orderHour-pay-page', function(page) {
	
	var userId = localStorage['user_id'];
	var orderNo = localStorage['order_no'];
	var orderId = localStorage['order_id'];
	
	//默认支付类型
	var orderPayType = 0;
	
	
	/*
	 * 优惠券
	 */
	//订单状态
	var orderStatus = $$("#orderStatus").val();
	console.log("orderStatus = " + orderStatus);
	//总金额
	var money  = $$("#orderMoney").text();
	//util.js  处理  ￥符号
	var order_money = delSomeString(money);
	
	localStorage.setItem('u_order_money_param', order_money);
	
	var order_type = $$("#orderType").val();
	
	if(orderStatus == 1) {
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
		console.log(userCouponName);
		if (userCouponId != undefined && userCouponName != undefined) {
			$$("#user_coupon_id").val(userCouponId);
			$$("#couponSelect").html("为您节省了"+userCouponValue+"元");
			if (userCouponValue == undefined) userCouponValue = 0;
			
			var realMoney = parseFloat(order_money) - parseFloat(userCouponValue);
			if(realMoney <= 0){
				realMoney = 0;
			}
			$$("#real-order-money-label").html('￥'+realMoney);
		}
	}
	
	
	//选择支付类型的处理
//	$$('input[type="checkbox"]').on('change', function() {
//		  this.checked = true;
//		  var v = this.value;
//		  $$('input[type="checkbox"]').each(function(key,index) {
//			 if (this.value != v) this.checked= false;
//		  });
//	});
	
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
	
	
	
	
	var postOrderPaySuccess =function(data, textStatus, jqXHR) {
		$$(".zf-hour-submit").removeAttr('disabled');  
	 	var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			
			// 重复支付或者订单不存在等情况，禁用支付按钮
			$$(".zf-hour-submit").attr("disabled", true);
			return;
		}
		
		//服务地址超出派工范围的情况
		if(result.status == "101"){
			//先禁用 支付按钮
			$$(".zf-hour-submit").attr("disabled", true);
			//前往  呼叫助理
			myApp.alert("对不起,当前地址不在我们的服务范围之内");
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
		
		
		//orderPayType = result.data.pay_type;
		//如果为余额支付，则直接跳到完成页面
		if (orderPayType == 0) {
			mainView.router.loadPage("order/order-pay-success.html?order_no="+orderNo+"&order_type=0");
		}
		
		//如果为微信支付，则需要跳转到微信支付页面.
		if (orderPayType == 2) {
			 var userCouponId = $$("#user_coupon_id").val();
			 if (userCouponId == undefined) userCouponId = 0;
			 var wxPayUrl = localUrl + "/" + appName + "/wx-pay-pre.jsp";
			 wxPayUrl +="?orderId="+orderId;
			 wxPayUrl +="&userCouponId="+userCouponId;
			 wxPayUrl +="&orderType=0";
			 wxPayUrl +="&payOrderType=0";
			 location.href = wxPayUrl;
		}
	};
	
	//点击支付的处理
	$$(".zf-hour-submit").click(function(){
		$$(".zf-hour-submit").attr("disabled", true);
		var postdata = {};
		postdata.user_id = userId;
		postdata.order_no = orderNo;
		
		var userCouponId = $$("#user_coupon_id").val();
		if (userCouponId == undefined) userCouponId = 0;
		postdata.user_coupon_id = userCouponId;
		
//		$$('input[type="checkbox"]').each(function(key,index) {
//			 if(this.checked) {
//				 orderPayType = this.value;
//			 }
//		 });
		
		
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
			statusCode: {
	         	200: postOrderPaySuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});		
		 
	});

});	


myApp.template7Data['page:orderHour-pay-page'] = function(){
	    var result; 
	    var postdata = {};
		var order_no = localStorage['order_no'];
		
		postdata.order_no = order_no;

	  
	   $$.ajax({
          type : "GET",
          url:siteAPIPath + "order/order_hour_detail.json",
          dataType: "json",
          data : postdata,
          cache : true,
          async : false,	// 不能是异步
          success: function(data){
//        	  console.log(data);
              result = data.data;
              
              //设置时间显示格式
              var timestamp = moment.unix(result.service_date);
  			  var startTime = timestamp.format('YYYY-MM-DD HH:mm');
  			  result.service_date = startTime;
  			  
          }
	   })
	  
	  return result;
}


