myApp.onPageBeforeInit('order-list-shendubaojie-tijiao', function (page) {

	var orderNo = page.query.order_no;
	var userId = localStorage['user_id'];
	var orderId = 0;
	var orderPayType = 0;
	var postData = {};
	postData.order_no = orderNo;
	
	
	
	$$("#selectCoupon").click(function() {
		var orderType =$$("#order_type").val();
		var orderNo = $$("#order_no").val();
		var orderMoney = $$("#order_money").val();
		var linkUrl = "user/coupon/mine-coupon-list.html";
		linkUrl+= "?order_type="+orderType;
		linkUrl+= "&order_money="+orderMoney;
		mainView.router.loadPage(linkUrl);
	});
	
	  var orderStatus = $$("#order_status_span").val();
	  if(orderStatus==4){
	   		$$("#user_coupon_view1_li").show();  
			$$("#order_pay_view1_li").show();
	  }else{
		 $$("#user_coupon_view1_li").hide();  
		 $$("#order_pay_view1_li").hide(); 
	  }
	
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
		if(realMoney <0){
			realMoney = 0;
		}
		$$("#real-order-money-label-exp-clean").html("￥"+realMoney);
	}
	
	 //评价订单
	$$("#exp-clean-order-pingjia").on("click",function(){
 		mainView.router.loadPage("order/order-rate.html?order_id="+orderId);
 	});
	//查看订单平价
	$$("#exp-clean-pingjiaxiangqing").on("click",function(){
 		mainView.router.loadPage("order/order-get-rate.html?order_id="+orderId);
 	});
	
	$$('label[name=myPayTypeSelects]').on('click',function(){
		
		var nowValue = $$(this).find("input").val();
		//单选
		$$('img[name="order_pay_types"]').each(function(index,value){
			if($$(this).prev().val() == nowValue){
				$$(this).attr("src","img/icons/duigou-green.png");
			}else{
				$$(this).attr("src","img/icons/duigou-grey.png");
			}
			
		});

		
	});
	
	//取消订单操作
	$$("#cancle_exp_clean_order_button").on('click',function(){

		myApp.confirm('您真的要取消订单么?',function(){
			$$.ajax({
				type:"post",
				url: siteAPIPath+"order/cancle_am_order.json",
				data : postData,
				cache: true, 
				success: function(datas,status,xhr){
					 
					 var result = JSON.parse(datas);
					 if(result.status == 0){
						 myApp.alert("订单取消成功！");
						 mainView.router.loadPage("order/order-hour-now-list.html");
						
						 setTimeout(function () {
							 myApp.hidePreloader();
						 }, 1000);
					 }
					 if(result.status == 999){
						 myApp.showPreloader("订单不存在！");
						 return false;
					 }
				}
			});
		});
	});	

	var postCleanOrderPaySuccess =function(data, textStatus, jqXHR) {
		$$("#shendu-submit").removeAttr('disabled');
	 	var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}
		var order = result.data;
		var orderId = order.id;
		//orderPayType = result.data.pay_type;
		//如果为余额支付，则直接跳到完成页面
		if (orderPayType == 0) {
			mainView.router.loadPage("order/order-pay-success.html?order_no="+orderNo+"&order_type=1");
		}
		
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
	};	
	
	//点击支付的处理
	$$(".shendu-submit").click(function(){
		
		
		$$("#shendu-submit").attr("disabled", true);
		var userCouponId = $$("#user_coupon_id").val();
		if (userCouponId == undefined) userCouponId = 0;
		
		var postdata = {};
	
		postdata.user_id = userId;
		postdata.order_no = orderNo;
		postdata.user_coupon_id = userCouponId;
		
		//获得选中的支付方式
		$$('img[name="order_pay_types"]').each(function(index,value){
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
	         	200: postCleanOrderPaySuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});		
		 
	});		
});


//列表显示深度保洁项
myApp.template7Data['page:order-list-shendubaojie-tijiao'] = function(){
  var result; 
  var userId = localStorage['user_id'];
  var orderNo = localStorage['u_order_no_param'];
  
   $$.ajax({
          type : "GET",
          url :siteAPIPath+"order/get_exp_clean_order_detail.json?order_no="+orderNo+"&user_id="+userId,
          dataType: "json",
          cache : true,
          async : false,
          success: function(data){
        	  console.log(data);
              result = data.data;
          }
  })
  
  return result;
}
