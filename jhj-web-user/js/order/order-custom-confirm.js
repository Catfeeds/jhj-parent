myApp.onPageInit('order-custom-confirm', function(page) {
	
	// payOrderType 订单支付类型 0 = 订单支付 1= 充值支付 2 = 手机话费类充值 3 = 订单补差价
	sessionStorage.setItem("pay_order_type", 0);
	
	var orderType = sessionStorage.getItem("order_type")
	$$("#orderType").val(orderType);
	
	//获取服务类别基本信息
	var serviceTypeId = sessionStorage.getItem("service_type_id");
	$$("#serviceType").val(serviceTypeId);
	
	var serviceTypeName = "家电清洗"
	$$("#serviceTypeName").html(serviceTypeName);
	$$("#serviceContent").val(serviceTypeName);
		
	var orderMoney = sessionStorage.getItem("order_money");
	
	console.log("orderMoney =" + orderMoney);
	$$("#priceStr").html(orderMoney);
	$$("#orderMoneyStr").html(orderMoney+ "元");
	$$("#orderMoney").val(orderMoney);
	
	var orderOriginMoney = sessionStorage.getItem("order_origin_money");
	$$("#orderOriginMoney").val(orderOriginMoney);
	
	$$("#serviceHourStr").html(sessionStorage.getItem("total_service_hour") + "小时");
	
	var userId = localStorage['user_id'];
	$$("#userId").val(userId);
	
	// 地址选择处理，1. 是否有默认地址， 2. 是否有选择的地址（最优先）
	var addrId = getItemAddrId();
	var addrName = getItemAddrName();
	
	if (addrId != undefined && addrId != "" && addrId != 0) {
		$$("#orderCustomAddrId").val(addrId);
	}
	if (addrName != undefined || addrName != "") {
		$$("#orderCustomAddrName").html(addrName);
	}
	
	//设置服务时间.
	if (sessionStorage.getItem('service_date') != null) {
		$$("#serviceDate").val(sessionStorage.getItem('service_date'));
	}
	
	if (sessionStorage.getItem('service_date_str') != null) {
		$$("#serviceDateStr").html(sessionStorage.getItem('service_date_str'));
	}
	
	$$("#orderPayStr").html(sessionStorage.getItem("order_pay") + "元");
	/*
	 * 提交订单
	 */
	$$("#orderCustomSubmit").click(function() {
		
		
		// 表单校验不通过，不能提交
		if (formValidation() == false) {
			return false;
		}
		
		var params = {};
		params.user_id = $$("#userId").val();
		params.mobile =  localStorage['user_mobile'];
		params.service_type = $$("#serviceType").val();
		params.service_date = $$("#serviceDate").val();
		params.serviceHour = sessionStorage.getItem("total_service_hour");
		params.addr_id = $$("#orderCustomAddrId").val();
		params.service_addons_datas = sessionStorage.getItem("service_addons_json");
		params.remarks = $$("#remarks").val();
		params.order_from = $$("#orderFrom").val();
		
		var staffId = sessionStorage.getItem("staff_id");
		if (staffId == undefined || staffId == "" || staffId == null) {
			staffId = 0;
		}
		params.staff_id = staffId;
		
		var orderOpFrom = sessionStorage.getItem("order_op_from");
		
		if (orderOpFrom != undefined && orderOpFrom != '') {
			params.order_op_from = orderOpFrom;
		}
		
		console.log(params);

		$$.ajax({
			type : "post",
			url : siteAPIPath + "order/post_exp.json",
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
				
				myApp.formDeleteData("orderCustom-Form");
				
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
	var formDatas = myApp.formToJSON('#orderCustom-Form');
	
	//校验服务时间是否正确
	var serviceDate = formDatas.serviceDate;
	var now = moment().unix();
	if (serviceDate == "" || serviceDate == 0 || serviceDate <= now) {
		myApp.alert("现在时间：" + moment().format("YYYY-MM-DD HH:MM ") + "\r\n" + "请选择合适的服务时间");
		return false;
	}
	
	//校验服务地址是否为空
	var addrId = $$("#orderCustomAddrId").val;
	if (addrId == 0 || addrId == "" || addrId == undefined) {
		alert("请选择服务地址");
		return false;
	}
	
	return true;
}