myApp.onPageInit('order-hour-confirm', function(page) {
	
	// payOrderType 订单支付类型 0 = 订单支付 1= 充值支付 2 = 手机话费类充值 3 = 订单补差价
	sessionStorage.setItem("pay_order_type", 0);
	
	var orderType = sessionStorage.getItem("order_type")
	$$("#orderType").val(orderType);
	
	//获取服务类别基本信息
	var serviceTypeId = sessionStorage.getItem("service_type_id");
	$$("#serviceType").val(serviceTypeId);
	$$.ajax({
	      type : "GET",
	      url: siteAPIPath+"dict/get_service_type.json?service_type_id="+serviceTypeId,
	      dataType: "json",
	      cache : true,
	      async : false,
	      success: function(data) {
	    	var serviceType = data.data;
	    	console.log(serviceType);
	    	if (serviceType == undefined || serviceType == "") {
	    		return false;
	    	}
	    	console.log(serviceType.name);
	    	$$("#serviceTypeName").html(serviceType.name);
	    	$$("#serviceContent").val(serviceType.name);
	      }
	});
	
	
	var userId = localStorage['user_id'];
	$$("#userId").val(userId);
	
	// 地址选择处理，1. 是否有默认地址， 2. 是否有选择的地址（最优先）
	var addrId = "";
	var addrName = ""
	if (localStorage['default_addr_id'] != null && localStorage['default_addr_id'] != undefined)  {
		addrId = localStorage['default_addr_id'];
	}
	if (localStorage['default_addr_name'] != null && localStorage['default_addr_name'] != undefined)  {
		addrName = localStorage['default_addr_name'];
	}
	
	//优先为刚选择的地址
	if (sessionStorage.getItem('addr_id') != null && sessionStorage.getItem('addr_id') != undefined)  {
		addrId = sessionStorage.getItem('addr_id');
	}
	
	if (sessionStorage.getItem('addr_name') != null && sessionStorage.getItem('addr_name') != undefined)  {
		addrName = sessionStorage.getItem('addr_name');
	}
	
	if (addrId != undefined && addrId != "" && addrId == 0) {
		$$("#orderHourAddrId").val(addrId);
	}
	if (addrName != undefined && addrName != "") {
		$$("#orderHourAddrName").html(addrName);
	}
	
	//设置服务时间.
	if (sessionStorage.getItem('service_date') != null) {
		$$("#serviceDate").val(sessionStorage.getItem('service_date'));
	}
	
	if (sessionStorage.getItem('service_date_str') != null) {
		$$("#serviceDateStr").html(sessionStorage.getItem('service_date_str'));
	}
	
	//设置服务人数和服务小时数
	var staffNums = sessionStorage.getItem('total_staff_nums');
	var serviceHours = sessionStorage.getItem('total_service_hour');
	var orderHourPay = sessionStorage.getItem('order_pay');
	var orderHourMoney = sessionStorage.getItem('order_money');
	$$("#staffNums").val(staffNums);
	$$("#serviceHour").val(serviceHours);
	$$("#serviceHourStr").html(serviceHours + "小时");
	$$("#orderMoney").val(orderHourMoney);
	$$("#priceStr").html(orderHourMoney);
	$$("#orderMoneyStr").html(orderHourMoney + "元");
	
	$$("#isMultiStr").html(staffNums + "位服务人员")
	
	//设置优惠劵
	if (sessionStorage.getItem('user_coupon_id') != null) {
		$$("#userCouponId").val(sessionStorage.getItem('user_coupon_id'));
		
		if (sessionStorage.getItem('user_coupon_name') != null) {
			$$("#userCouponName").html(sessionStorage.getItem('user_coupon_name'));
		}

		if (sessionStorage.getItem('user_coupon_value') != null) {
			var userCouponValue = sessionStorage.getItem('user_coupon_value');
			if(userCouponValue==undefined || userCouponValue=="" ||userCouponValue==null){
				userCouponValue=0;
			}
			$$("#userCouponValue").val(userCouponValue);
			console.log("userCouponValue = " + $$("#userCouponValue").val())
			var orderPayStr = $$("#orderMoney").val() - userCouponValue;
			if (orderPayStr < 0) orderPayStr = 0;
			sessionStorage.setItem("order_pay", orderPayStr);
		}
	} else {
		//读取用户可用的优惠劵
		var params = {};
		params.user_id = $$("#userId").val();
		params.service_type = $$("#serviceType").val();
		params.service_date = sessionStorage.getItem('service_date');
		params.order_money = $$("#orderMoney").val();
		
		console.log(params);
		$$.ajax({
		      type : "GET",
		      url: siteAPIPath+"user/get_validate_coupons.json",
		      dataType: "json",
		      cache : true,
		      data : params,
		      async : true,
		      success: function(data) {
		    	  var couponList = data.data;
		    	  var nums = 0;
		    	  if (couponList == undefined || couponList == "") {
		    		  nums = 0;
		    	  } else {
		    		  nums = couponList.length;
		    	  }
		    	  
		    	  //如果有优惠劵，则默认选择最大面值的优惠劵.
		    	  var userCouponValue = 0;
	    		  var userCouponId = 0;
	    		  var userCouponName = "";
	    		  
		    	  if (nums > 0) {
		    		 
		    		  $$.each(couponList, function(i, item) {
		    			  if (item.value > userCouponValue) {
		    				  userCouponValue = item.value;
		    				  userCouponId = item.id;
		    				  userCouponName = "￥" + userCouponValue;
		    			  }
		    		  });
		    		  
		    		  sessionStorage.setItem("user_coupon_id", userCouponId);
	    			  sessionStorage.setItem("user_coupon_name", userCouponName);
	    			  sessionStorage.setItem("user_coupon_value", userCouponValue);
		    	  }
		    	  
		    	  var userCouponNameStr = nums + "张可用";
		    	  if (userCouponId != 0 && userCouponValue != 0) {
		    		  var orderPayStr = $$("#orderMoney").val() - userCouponValue;
					  if (orderPayStr < 0) orderPayStr = 0;
					  sessionStorage.setItem("order_pay", orderPayStr);
					  userCouponNameStr = userCouponName;
		    	  }
		    	  
		    	  console.log("order_pay = " + sessionStorage.getItem("order_pay"));
		    	  $$("#userCouponName").html(userCouponNameStr);
		    	  $$("#orderHourPayStr").html(sessionStorage.getItem("order_pay") + "元");
		      }
		});
	}
	$$("#orderHourPayStr").html(sessionStorage.getItem("order_pay") + "元");
	/*
	 * 提交订单
	 */
	$$("#orderHourSubmit").click(function() {
		
		
		// 表单校验不通过，不能提交
		if (formValidation() == false) {
			return false;
		}
		
		var addrId = $$("#orderHourAddrId").val();
		if (addrId == undefined || addrId == "" || addrId == 0) {
			alert("请选择地址.");
			return false;
		}
			
		
		var params = {};
		params.userId = $$("#userId").val();
		params.serviceType = $$("#serviceType").val();
		params.serviceContent = $$("#serviceContent").val();
		params.serviceDate = $$("#serviceDate").val();
		params.addrId = addrId;
		params.staffNums = $$("#staffNums").val();
		params.serviceHour = $$("#serviceHour").val();
		params.remarks = $$("#remarks").val();
		params.orderFrom = $$("#orderFrom").val();
		
		var staffId = sessionStorage.getItem("staff_id");
		if (staffId == undefined || staffId == "" || staffId == null) {
			staffId = 0;
		}
		params.staff_id = staffId;
		
		console.log(params);

		$$.ajax({
			type : "post",
			url : siteAPIPath + "order/post_hour.json",
			data : params,
			success : function(data, status, xhr) {
				
				var result = JSON.parse(data);
				
				if (result.status == "999") {
					myApp.alert(result.msg);
					return;
				}

				sessionStorage.setItem('order_no', result.data.order_no);
				sessionStorage.setItem('order_id', result.data.id);
				/*
				 * 提交 校验通过后，，清空当前页面回显的数据
				 */
				sessionStorage.removeItem("service_date");
				sessionStorage.removeItem("service_dateStr");
				sessionStorage.removeItem("addr_id");
				sessionStorage.removeItem("addr_name");
				
				myApp.formDeleteData("orderHour-Form");
				
				/*
				 * 此处 逻辑： 如果 响应 成功 。 不管 是否 成功分配阿姨，都跳转到 支付 页面。 在 进行 支付
				 * 操作的时候，再给出提示，是否有可以服务的阿姨
				 */
				var successUrl = "order/order-pay.html";
				
				mainView.router.loadPage(successUrl);
				
			},
			error : function(status, xhr) {
				myApp.alert("网络异常,请稍后再试.");
			}
		});
	});
});

// 表单校验
function formValidation() {
	var formDatas = myApp.formToJSON('#orderHour-Form');
	
	//校验服务时间是否正确
	var serviceDate = formDatas.serviceDate;
	var now = moment().unix();
	if (serviceDate == "" || serviceDate == 0 || serviceDate <= now) {
		myApp.alert("现在时间：" + moment().format("YYYY-MM-DD HH:MM ") + "\r\n" + "请选择合适的服务时间");
		return false;
	}
	
	//校验服务地址是否为空
	var addrId = $$("#orderHourAddrId").val;
	if (addrId == 0 || addrId == "") {
		alert("请选择服务地址");
		return false;
	}
	
	return true;
}