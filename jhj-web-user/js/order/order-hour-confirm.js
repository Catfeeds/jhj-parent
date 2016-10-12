myApp.onPageInit('order-hour-confirm', function(page) {
	
	//获取服务类别基本信息
	var serviceTypeId = $$("#serviceType").val();
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
	      }
	})
	
	
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
	if (sessionStorage.getItem('serviceDate') != null) {
		$$("#serviceDate").val(sessionStorage.getItem('serviceDate'));
	}
	
	if (sessionStorage.getItem('serviceDateStr') != null) {
		$$("#serviceDateStr").html(sessionStorage.getItem('serviceDateStr'));
	}
	
	
	/*
	 * 提交订单
	 */
	$$("#submitOrder").click(function() {
		
		// 表单校验不通过，不能提交
		if (formValidation() == false) {
			return false;
		}
		
		// 文档要求： post请求，必须是 formData类型
		var formData = myApp.formToJSON('#orderHour-Form');
		
		// 处理 日期。。传给后台 string 类型 的 秒值，时间戳
		formData.serviceDate = moment(formData.serviceDate + ":00", "yyyy-MM-DD HH:mm:ss").unix();
		
		$$.ajax({
			type : "post",
			url : siteAPIPath + "order/post_hour.json",
			data : formData,
			success : function(data, status, xhr) {
				
				var result = JSON.parse(data);
				
				if (result.status == "999") {
					myApp.alert(result.msg);
					
					return;
				}
				
				localStorage.setItem('user_id', userId);
				
				localStorage.setItem('order_no', result.data.order_no);
				
				localStorage.setItem('order_id', result.data.id);
				
				/*
				 * 提交 校验通过后，，清空当前页面回显的数据
				 */

				sessionStorage.removeItem("serviceDate");
				
				myApp.formDeleteData("orderHour-Form");
				
				/*
				 * 此处 逻辑： 如果 响应 成功 。 不管 是否 成功分配阿姨，都跳转到 支付 页面。 在 进行 支付
				 * 操作的时候，再给出提示，是否有可以服务的阿姨
				 */
				var successUrl = "order/order-form-zhongdiangong-pay.html";
				
				mainView.router.loadPage(successUrl);
				
			},
			error : function(status, xhr) {
				myApp.alert("网络异常,请稍后再试.");
			}
		});
	});
	
	// 点击 服务地址 按钮时，将 页面 变化过的值，保存在本地
	$$("#addrSelect").on('click', function() {
		// 保存已选择的 服务时间
		sessionStorage.setItem('serviceDate', $$("#serviceDate").val());
	});
});

// 表单校验
function formValidation() {
	var formDatas = myApp.formToJSON('#orderHour-Form');
	
	// 当前选择 的 日期
	var dateSelect = formDatas.serviceDate;
	// 当前 选择的 日期 中 的 '整点小时' 数，如：08,10,19...
	var hourSelect = moment(dateSelect).format('HH');
	
	// 选择日期的 年月日
	var daySelect = moment(dateSelect).format('YYYYMMDD');
	
	// 现实日期的 年月日
	var realDate = moment().format("YYYYMMDD");
	
	// 当前整点小时数
	var nowHour = moment().hour();
	
	// 选择的时间 小于 当前时间
	if (Number(hourSelect) <= nowHour && daySelect == realDate) {
		myApp.alert("现在时间：" + moment().format("YYYY-MM-DD HH:MM ") + "\r\n" + "请选择合适的服务时间");
		return false;
	}
	
	var addrId = $$("#addrId").val;
	
	if (addrId == 0 || addrId == "") {
		alert("请选择服务地址");
		return false;
	}
	
	return true;
}