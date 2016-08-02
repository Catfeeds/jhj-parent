myApp.onPageInit('order-list-shendubaojie-yuyue', function(page) {

	var addrId = page.query.addr_id;
	var addrName = page.query.addr_name;
	var userId = localStorage.getItem("user_id");
	if (addrId != undefined) {
		$$("#addr_ids").val(addrId);
		
//		 $$("#addr_ids").val(localStorage['default_addr_id']);
	}

	if (addrName != undefined) {
		
		//！！！！ 此处替换了 原来的 id= addrName 因为 这个框架，对同名id 分不清，id尽量独立！！  ！！
		$$("#shendubaojiAddrName").html(addrName);
		
	}
	
	//设置默认地址
	if (addrId == undefined) {
		 if (localStorage['default_addr_id'] != null) {
//			 $$("#addrId").val(localStorage['default_addr_id']);
			 
			 $$("#addr_ids").val(localStorage['default_addr_id']);
		 }

		 if (localStorage['default_addr_name'] != null) {
			 $$("#shendubaojiAddrName").html(localStorage['default_addr_name']);
		 }		 
		 
	}	
	
	var temp = sessionStorage.getItem('exp_clean_remarks');
	if (temp != null && temp != undefined) {
		$$("#exp_clean_remarks")
				.text(sessionStorage.getItem('exp_clean_remarks'));
	} else {
		$$("#exp_clean_remarks")
				.text(sessionStorage.getItem('exp_clean_remarks'));
	}
	/*
	 * 时间选择插件, 已包含 加载 时，赋给一个 时间 默认值
	 */
	serviceDateSelect();

	/*
	 * 回显 日期时间、选择的价格
	 */
	var dateTwo = sessionStorage.getItem("service_date");
	if (dateTwo != null) {
		$$("#serviceDate").val(dateTwo);
	}
	
	var tagIds = localStorage.getItem('tagIds');
	var tagIdsArr = tagIds.split(",");
	var orderPrice = $$("#order_price").val();
	orderPrice = Number(0);
	var itemNum = Number(0);
	for(var i = 0; i< tagIdsArr.length; i++) {
		 itemPrice = getServiceAddonPrice(tagIdsArr[i]);
		 itemNum =  getServiceAddonDefaultNum(tagIdsArr[i]);
		 if (itemNum != undefined && itemPrice != undefined ) {
			 orderPrice = Number(orderPrice) + Number(itemNum) * Number(itemPrice);
		 }
	}
	$$("#order_price").val(orderPrice);
	/**
	 * 深度保洁订单提交预约
	 */
	$$("#order-exp-clean-submit").on(
			"click",
			function() {
				$$("#order-exp-clean-submit").attr("disabled", true);
				sessionStorage.setItem('exp_clean_remarks', '')
				var tagIds = localStorage.getItem('tagIds');
				var tagIdsArr = tagIds.split(",");
				// 表单验证
				if (orderFormValidation() == false) {
					return false;
				}
				// 拼接json字符串
				var jsonData = "";
				for (var i = 0; i < tagIdsArr.length; i++) {
					var serviceAddonId = tagIdsArr[i];
					var itemNum = getServiceAddonDefaultNum(tagIdsArr[i]);
					;
					jsonData += "{";
					jsonData += "serviceAddonId" + ":" + serviceAddonId + ","
							+ "itemNum" + ":" + itemNum + ",";
					jsonData = jsonData.substring(0, jsonData.length - 1)
							+ "},";
				}
				;
				var index = jsonData.lastIndexOf(",");
				jsonData = "[" + jsonData.substring(0, index) + "]";
				var formData = myApp.formToJSON('#order-exp-clean-form');
				
//				alert(formData.serviceDate);
//				return false;
				formData.service_date = moment(formData.serviceDate + ":00",
						"yyyy-MM-DD HH:mm:ss").unix();
				formData.service_addons_datas = jsonData;// "[{'serviceAddonId':'3','itemNum':'2'}]"
				formData.user_id = userId;
				formData.service_type = "2";
				$$.ajax({
					type : "POST",
					url : siteAPIPath + "order/post_exp_clean_order.json",
					dataType : "json",
					async : false,
					cache : false,
					data : formData,
					statusCode : {
						200 : saveExpCleanOrderSuccess,
						400 : ajaxError,
						500 : ajaxError
					}
				});
			});

	$$("#addrSelect").on('click', function() {
		// 保存已选择的 服务时间
		sessionStorage.setItem('service_date', $$("#serviceDate").val());
		// 保存备注
		var remarks = $$("#exp_clean_remarks").val();
		var formData = myApp.formToJSON('#order-exp-clean-form');
		if (remarks != '' && remarks != undefined) {
			formData.remarks = remarks;
			sessionStorage.setItem('exp_clean_remarks', remarks);
		} else {
			formData.remarks = '';
			sessionStorage.setItem('exp_clean_remarks', '');
		}
	});

});
// 列表显示深度保洁项
myApp.template7Data['page:order-list-shendubaojie-yuyue'] = function() {
	var result = {};
	// 获取参数（附加服务service_addon_id）
	var tagIds = localStorage.getItem('tagIds');
	var tagIdsArr = tagIds.split(",");
	// 获取附加服务列表
	var servcieTypeAddonsList = JSON.parse(localStorage
			.getItem("service_type_addons_list"));
	var userId = localStorage.getItem("user_id");

	/**
	 * 根据选中的附加服务，显示对应的列表信息
	 */
	var list = [];
	for (var i = 0; i < tagIdsArr.length; i++) {
		var tagId = tagIdsArr[i];
		var name = "";
		var itemUnit = "";
		var price = ''
		var serviceAddons = {};
		// 获取对应的附加服务单价和量词
		$$.each(servcieTypeAddonsList, function(i, item) {
			if (item.service_addon_id == tagId) {
				name = item.name;
				itemUnit = item.item_unit;
				defaultNum = item.default_num;
				price = item.price;
			}
		});
		serviceAddons.name = name;
		serviceAddons.item_unit = itemUnit;
		serviceAddons.default_num = defaultNum;
		serviceAddons.price = price;
		list.push(serviceAddons);
	}
	result.list = list;
	return result;
}
// 提交深度保洁预约单后跳转
function saveExpCleanOrderSuccess(data, textStatus, jqXHR) {
	$$("#order-exp-clean-submit").removeAttr('disabled');
	myApp.hideIndicator();
	var results = JSON.parse(data.response);
	if (results.status == "999") {
		myApp.alert(results.msg);
		return;
	}
	if (results.status == "0") {
		var orderNo = results.data.order_no;
		var userId = localStorage.getItem("user_id");
		mainView.router
				.loadPage("order/order-list-shendubaojie-result.html?order_no="
						+ orderNo);
//		localStorage.setItem('u_order_no_param', orderNo);
		sessionStorage.removeItem("service_date");
	}
}
// 表单验证
function orderFormValidation() {
	var formData = myApp.formToJSON('#order-exp-clean-form');

	if (formData.service_date == "") {
		myApp.alert("请输入服务时间");
	}
	// 当前选择 的 日期
	var dateSelect = formData.service_date;

	// 当前 选择的 日期 中 的 '整点小时' 数，如：08,10,19...
	var hourSelect = moment(dateSelect).format('HH');

	/*
	 * 如果 是 默认 的时间。则需要判断 6点之前，不行
	 */

	if (Number(hourSelect) < 6) {
		alert("现在无法下单，点击选择合适 的时间");
		$$("#order-exp-clean-submit").removeAttr('disabled');
		return false;
	}

	if (formData.addr_id == "") {
		myApp.alert("请输入服务地址");
		$$("#order-exp-clean-submit").removeAttr('disabled');
		return false;
	}
	return true;
}
