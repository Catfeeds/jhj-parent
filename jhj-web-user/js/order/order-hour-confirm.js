myApp.onPageInit('order-hour-confirm', function(page) {
	
	var orderType = $$("#orderType").val();
	sessionStorage.setItem("order_type", orderType);
	
	//获取服务类别基本信息
	var serviceTypeId = sessionStorage.getItem("service_type_id");
	$("#serviceTypeId").val(serviceTypeId);
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
	    	$$("#serviceHourStr").html(serviceType.service_hour + "小时");
	    	$$("#priceStr").html(serviceType.price);
	    	$$("#orderMoneyStr").html(serviceType.price + "元");
	    	
	    	$$("#serviceContent").val(serviceType.name);
	    	$$("#orderMoney").val(serviceType.price);
	    	
	    	sessionStorage.setItem("order_money", serviceType.price);
	    	sessionStorage.setItem("order_pay", serviceType.price);
	      }
	});
	
	
	var userId = localStorage['user_id'];
	$$("#userId").val(userId);
	
	// 地址选择处理，1. 是否有默认地址， 2. 是否有选择的地址（最优先）
	var addrId = "";
	var addrName = ""
	if (localStorage['default_addr_id'] != null)  addrId = localStorage['default_addr_id'];
	if (localStorage['default_addr_name'] != null)  addrName = localStorage['default_addr_name'];
	
	//优先为刚选择的地址
	if (sessionStorage.getItem('addr_id') != null)  addrId = sessionStorage.getItem('addr_id');
	if (sessionStorage.getItem('addr_name') != null)  addrName = sessionStorage.getItem('addr_name');
	
	if (addrId != undefined || addrId != "") $$("#addrId").val(addrId);
	if (addrName != undefined || addrName != "") {
		$$("#orderHourAddrName").html(addrName);
	}
	
	//设置服务时间.
	if (sessionStorage.getItem('service_date') != null) {
		$$("#serviceDate").val(sessionStorage.getItem('service_date'));
	}
	
	if (sessionStorage.getItem('service_date_str') != null) {
		$$("#serviceDateStr").html(sessionStorage.getItem('service_date_str'));
	}
	
	//设置优惠劵
	if (sessionStorage.getItem('user_coupon_id') != null) {
		$$("#userCouponId").val(sessionStorage.getItem('user_coupon_id'));
		
		if (sessionStorage.getItem('user_coupon_name') != null) {
			$$("#userCouponName").html(sessionStorage.getItem('user_coupon_name'));
		}

		if (sessionStorage.getItem('user_coupon_value') != null) {
			var userCouponValue = sessionStorage.getItem('user_coupon_value');
			$$("#userCouponValue").val(userCouponValue);
			console.log("userCouponValue = " + $$("#userCouponValue").val())
			var orderPayStr = $$("#orderMoney").val() - userCouponValue;
			if (orderPayStr < 0) orderPayStr = 0;
			sessionStorage.setItem("order_pay", orderPayStr);
			$$("#orderPayStr").html(orderPayStr + "元");
		}
	} else {
		//读取用户可用的优惠劵
		var params = {};
		params.user_id = $$("#userId").val();
		params.order_type = $$("#orderType").val();
		params.service_date = $$("#serviceDate").val();
		params.order_money = $$("#orderMoney").val();
		
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
		    	  $$("#userCouponName").html(nums + "张可用")
		      }
		});
	}

	/*
	 * 提交订单
	 */
	$$("#orderHourSubmit").click(function() {
		
		
		// 表单校验不通过，不能提交
		if (formValidation() == false) {
			return false;
		}

		var params = {};
		params.userId = $$("#userId").val();
		params.serviceType = $$("#serviceType").val();
		params.serviceContent = $$("#serviceContent").val();
		params.serviceDate = $$("#serviceDate").val();
		params.addrId = $$("#addrId").val();
		params.serviceHour = $$("#serviceHour").val();
		params.remarks = $$("#remarks").val();
		params.orderFrom = $$("#orderFrom").val();
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
	var addrId = $$("#addrId").val;
	if (addrId == 0 || addrId == "") {
		alert("请选择服务地址");
		return false;
	}
	
	return true;
}