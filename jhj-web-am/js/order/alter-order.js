myApp.onPageInit('alter-order-page', function (page) {
	
	var orderNo = page.query.order_no;
	var amId =  page.query.am_id;
//	console.log("alter-order-page");
	//获取订单详情
	
	var orderAmInfoSuccess =function(data, textStatus, jqXHR) {
//		console.log("orderAmInfoSuccess");
	 	var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}
		var order = result.data;

		var formData = {
			'order_no' : order.order_no,
			'service_content' : order.service_content,
			'order_money' : order.order_money,
		}
		myApp.formFromJSON('#order-alert-form', formData);
		
		$$("#order_type_name_for_am").html(order.order_type_name);
		$$("#order_status_name_for_am").html(order.order_status_name);		
	}	
	
	$$.ajax({
		type : "GET",
		url :siteAPIPath+"order/get_am_order_detail.json?am_id="+amId+"&order_no="+orderNo,
		dataType : "json",
		cache : true,
		statusCode: {
         	200: orderAmInfoSuccess,
 	    	400: ajaxError,
 	    	500: ajaxError
 	    },
	});
	
	
	var  saveOrderSuccess = function(data, textStatus, jqXHR) {
		 $$("#mine-addr-save").removeAttr('disabled');  
		myApp.hideIndicator();
		var result = JSON.parse(data.response);
		var results = result.appResultData;
		if (results.status == "999") {
			myApp.alert(results.msg);
			return;
		}
		if (results.status == "0") {
			myApp.alert("订单调整完成");
			mainView.router.loadPage("order/order-list.html");
		}
	} 
	//订单调整保存
	$$("#order-adjust-submit").on("click", function() {
		$$("#mine-addr-save").attr("disabled", true);
		if (alterOrderPageValidation() == false) {
			return false;
		}
		
		var formData = myApp.formToJSON('#order-alert-form');
		$$.ajax({
			type : "POST",
			url : siteAPIPath+ "order/post_user_am.json",
			dataType : "json",
			cache : false,
			data : formData,
			statusCode : {
				200 : saveOrderSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
		});
	})
	
});


// 获取订单信息
function getOrderAmInfo(amId,orderNo) {
//	console.log("getOrderAmInfo");
	
}

function alterOrderPageValidation() {
	var formData = myApp.formToJSON('#order-alert-form');

	
	if (formData.service_content == "") {
		myApp.alert("请输入服务要求");
		return false;
	}
	var order_monye = formData.order_money;
	if (formData.order_money == "" || formData.order_money < 0 ) {
		myApp.alert("订单金额不合法");
		return false;
	}
	
	//util.js 新增校验方法
	if(!isMoneyNum(formData.order_money) || formData.order_money < 0){
		myApp.alert("请输入合法的数字");
		return false;
	}
/*	if (!isUnsignedNumeric(order_monye) ) {
		myApp.alert("订单金额必须正数");
		return false;
	}*/
	return true;
}
