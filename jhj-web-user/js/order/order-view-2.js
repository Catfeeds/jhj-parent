myApp.onPageBeforeInit('order-hour-view-2-page', function (page) {
	

	var userId = localStorage['user_id'];
	var order_no = page.query.order_no;
	var orderNo = page.query.order_no;
	var orderId = 0;
	
	
	$$("#selectCoupon").click(function() {
		var orderType =$$("#order_type").val();
		var orderNo = $$("#order_no").val();
		var orderMoney = $$("#order_money").val();
		var linkUrl = "user/coupon/mine-coupon-list.html";
		linkUrl+= "?order_type="+orderType;
		linkUrl+= "&order_money="+orderMoney;
		mainView.router.loadPage(linkUrl);
	});	
	
	//用户返回优惠劵之后的处理
	var userCouponId = page.query.user_coupon_id;
	//var userCouponName = page.query.user_coupon_name;
	var userCouponValue = page.query.user_coupon_value;
	
	if (userCouponId != undefined && userCouponValue != undefined) {
		$$("#user_coupon_id").val(userCouponId);
		$$("#user_coupon_name").html("为您节省了"+userCouponValue+"元");
		if (userCouponValue == undefined) userCouponValue = 0;
		var orderMoney = $$("#order_money").val();
		
		var realMoney = parseFloat(orderMoney) - parseFloat(userCouponValue);
		
		/*
		 * 2015-11-2 14:42:16 如果选完优惠券， 实际支付金额 为0 ，则不让使用 微信支付
		 */
		if(realMoney < 0 || realMoney == 0){
			realMoney = 0;
			$$("#wxzhifuDiv").hide();
			
		}
		
		$$("#real-am-order-money-label").html("￥"+realMoney);
	}
		
	var postAmOrderPaySuccess =function(data, textStatus, jqXHR) {
		$$("#am-pay-submit").removeAttr('disabled');
	 	var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}
		
		var order = result.data;
//		console.log(order+"~~~~~~~~~~~~~");
		var orderId = order.id;
		//orderPayType = result.data.pay_type;
		//如果为余额支付，则直接跳到完成页面
		if (orderPayType == 0 || orderPayType == 6) {
			
			var orderNo = order.order_no;
			
			mainView.router.loadPage("order/order-pay-success.html?order_no="+orderNo+"&order_type=2"
									+"&order_pay_type"+orderPayType);
		}
		
		//如果为微信支付，则需要跳转到微信支付页面.
		if (orderPayType == 2) {
			var userCouponId = $$("#user_coupon_id").val();
			if (userCouponId == undefined) userCouponId = 0;
			
			 var wxPayUrl = localUrl + "/" + appName + "/wx-pay-pre.jsp";
			 wxPayUrl +="?orderId="+orderId;
			 wxPayUrl +="&userCouponId="+userCouponId;
			 wxPayUrl +="&orderType=2";
			 wxPayUrl +="&payOrderType=0";
			 location.href = wxPayUrl;
		}
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
	
	
	//点击支付的处理
	$$(".am-pay-submit").click(function(){
		
		
		$$("#am-pay-submit").attr("disabled", true);
		var userCouponId = $$("#user_coupon_id").val();
		if (userCouponId == undefined) userCouponId = 0;
		
		var postdata = {};
		postdata.user_id = userId;
		
		
		var orderNo = $$("#order_no").val();
		
		postdata.order_no = orderNo;
		postdata.user_coupon_id = userCouponId;
		
		
		//获得选中的支付方式
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
	         	200: postAmOrderPaySuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});		
		 
	});		
	
    //评价订单
	$$("#yonghu-pingjiadingdan").on("click",function(){
		/*var orderHourViewVo = result.data;
		orderId = orderHourViewVo.id;*/
 		mainView.router.loadPage("order/order-rate.html?order_id="+orderId);

 		
 	});
	
	//查看订单平价
	$$("#yonghu-pingjiaxiangqing").on("click",function(){
	
 		mainView.router.loadPage("order/order-get-rate.html?order_id="+orderId);

 		
 	});
});

//列表显示
myApp.template7Data['page:order-hour-view-2-page'] = function(){
	
  var result; 
  var userId = localStorage['user_id'];
  var orderNo = localStorage['u_order_no_param'];
  var postData = {};
  postData.order_no = orderNo;
   $$.ajax({
          type : "GET",
          url  : siteAPIPath+"order/order_hour_paotui_detail.json",
          dataType: "json",
          data : postData,
          cache : true,
          async : false,
          success: function(data){
              result = data.data;
              
          }
  })
  return result;
  
}