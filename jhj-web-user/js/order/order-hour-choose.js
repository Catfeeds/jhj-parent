myApp.onPageInit('order-hour-choose', function(page) {
	
	
	var userId = localStorage['user_id'];
	getUserInfo();
	
	var serviceTypeId = page.query.service_type_id;
	sessionStorage.setItem("service_type_id", serviceTypeId);
	
	//包月初体验隐藏页面
	function hideContent(serviceTypeId){
		if(serviceTypeId==61){
			$$("#minServiceHour").val('4.5');
			$$("#isShow").css("display","none");
		}
	}
	hideContent(serviceTypeId);
	
		
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
	    	
	    	$$("#price").val(serviceType.price);
	    	$$("#mprice").val(serviceType.mprice);
	    	$$("#pprice").val(serviceType.pprice);
	    	$$("#mpprice").val(serviceType.mpprice);
	    	
	    	var isVip = localStorage['is_vip'];
	    	if (isVip == undefined || isVip == "") isVip = 0;
	    	
	    	if (isVip == 0) {
	    		$$("#orderHourPayStr").html(serviceType.pprice + "元");
	    	}
	    	
	    	if (isVip == 1) {
	    		$$("#orderHourPayStr").html(serviceType.mpprice + "元");
	    	}
	    	
	    	var minServiceHour = 3;
	    	
	    	minServiceHour = serviceType.service_hour;
	    	$$("#minServiceHour").val(minServiceHour);
	    	$$("#serviceHours").val(minServiceHour);
	      }
	});
	
	var maxServiceHour = 6;
	$$("#maxServiceHour").val(maxServiceHour);
	
	
	sessionStorage.setItem("order_type", 0);
	
	// 地址选择处理，1. 是否有默认地址， 2. 是否有选择的地址（最优先）
	var addrId = getItemAddrId();
	var addrName = getItemAddrName();
	
	if (addrId != undefined && addrId != "") {
		$$("#addrId").val(addrId);
	}
	
	if (userId == 4953) {
		myApp.alert("addrId = " + addrId);
		myApp.alert("addrID val = " + $$("#addrId").val() );
	}
	
	if (addrName != undefined && addrName != "") {
		$$("#orderChooseAddrName").html(addrName);
	}
	
	initOrderHour();
	
	 $$("#chooseServiceTime").on("click",function(){
		 
		 var checkOrderHour = setOrderHourTotal();
		 
		 if (checkOrderHour == false) return false;
		 
		var addrId = $$("#addrId").val();
		if (addrId == undefined || addrId == "" || addrId == 0) {
			myApp.alert("请选择地址.");
			return false;	
		}

		 var url = "order/order-lib-cal.html?next_url=order/order-hour-confirm.html"
		 
		 var staffId = sessionStorage.getItem("staff_id");
		 if (staffId != undefined && staffId != "" && staffId != null) {
			 url+="?staff_id="+staffId;
		 }
		 
		 mainView.router.loadPage(url);
	 });
});


function onStaffNumsAdd() {
	var staffNums = $$("#staffNums").val();
	console.log("staffNums = " + staffNums);
	staffNums = staffNums.replace(/\D|^0/g,'');
	staffNums++;
	if (staffNums < 0 ) staffsNums = 0;
	$$("#staffNums").val(staffNums);
	setOrderHourTotal();
}

function onStaffNumsSub() {
	var staffNums = $$("#staffNums").val();
	staffNums = staffNums.replace(/\D|^0/g,'');
	staffNums--;
	if (staffNums < 0 ) staffsNums = 0;
	$$("#staffNums").val(staffNums);
	setOrderHourTotal();
}

function onServiceHoursAdd() {
	var serviceHours = $$("#serviceHours").val();
	serviceHours = serviceHours.replace(/\D|^0/g,'');
	serviceHours++;
	if (serviceHours < 0 ) serviceHours = 0;
	$$("#serviceHours").val(serviceHours);
	setOrderHourTotal();
}

function onServiceHoursSub() {
	var serviceHours = $$("#serviceHours").val();
	serviceHours = serviceHours.replace(/\D|^0/g,'');
	serviceHours--;
	if (serviceHours < 0 ) serviceHours = 0;
	$$("#serviceHours").val(serviceHours);
	setOrderHourTotal();
}

function setOrderHourTotal() {
	console.log("setOrderHourTotal");
	var staffNums = $$("#staffNums").val();
	staffNums = staffNums.replace(/\D|^0/g,'');
	$$("#staffNums").val(staffNums);
	var serviceHours = $$("#serviceHours").val();
	serviceHours = serviceHours.replace(/\D|^0/g,'');
	$$("#serviceHours").val(serviceHours);
	
	if (staffNums == undefined || staffNums == "" || staffNums <= 0) {
		alert("预约服务人员数量最少为1个人.");
		$$("#staffNums").val(1);
		return false;
	}
	
	if (staffNums > 5) {
		alert("预约服务人员数量最多可以指定5人.");
		$$("#staffNums").val(5);
		return false;
	}
	
	var minServiceHour = $$("#minServiceHour").val();
	var maxServiceHour = $$("#maxServiceHour").val();
	if (serviceHours == undefined || serviceHours == "" || serviceHours < minServiceHour) {
		alert("预约服务时间最少为"+ minServiceHour +"小时.");
		
		$$("#serviceHours").val(minServiceHour);
		return false;
	}
	
	if (serviceHours > maxServiceHour) {
		alert("预约服务时间最多为"+maxServiceHour+"小时.");
		$$("#serviceHours").val(maxServiceHour);
		return false;
	}
	
	var price = $$("#price").val();
	var mprice = $$("#mprice").val();
	var pprice = $$("#pprice").val();
	var mpprice = $$("#mpprice").val();
	var isVip = localStorage['is_vip'];
	if (isVip == undefined || isVip == "") isVip = 0;
	console.log("is_vip ==" + isVip);
	
	var orderOriginalPay = pprice;
	var orderOriginalPrice = price;
	
	var orderHourPay = pprice;
	var orderHourPrice = price;
	if (isVip == 1) {
		orderHourPay = mpprice;
		orderHourPrice = mprice;
	}
	
	if (staffNums > 1 || serviceHours > minServiceHour) {
		
		orderHourPay = parseFloat(orderHourPay) + parseFloat(serviceHours - minServiceHour) * parseFloat(orderHourPrice);
		orderHourPay = orderHourPay * staffNums;

		orderOriginalPay = parseFloat(orderOriginalPay) + parseFloat(serviceHours - minServiceHour) * parseFloat(orderOriginalPrice);
		orderOriginalPay = orderOriginalPay * staffNums;
//		orderHourPay = orderHourPrice * serviceHours * staffNums;
		
//		orderOriginalPay = orderOriginalPrice * serviceHours * staffNums;
	}
	
	$$("#orderHourPayStr").html(orderHourPay + "元");
	
	sessionStorage.setItem("order_money", orderHourPay);
	sessionStorage.setItem("order_pay", orderHourPay);
	
	//再存储非会员价的价格，满足需求，只有余额支付才能使用会员价.
	sessionStorage.setItem("order_origin_money", orderOriginalPay);
	sessionStorage.setItem("order_origin_pay", orderOriginalPay);
	
	sessionStorage.setItem("total_staff_nums", staffNums);
	sessionStorage.setItem("total_service_hour", serviceHours);
	
	console.log("orderHourPay = " + orderHourPay);
	return true;
}


function initOrderHour() {
	var staffNums = sessionStorage.getItem("total_staff_nums");
	if (staffNums == undefined || staffNums == "" || staffNums <= 0) return false;
	$$("#staffNums").val(staffNums);
	
	var minServiceHour = $$("#minServiceHour").val();
	var maxServiceHour = $$("#maxServiceHour").val();
	
	var serviceHours = sessionStorage.getItem("total_service_hour");
	if (serviceHours == undefined || serviceHours == "" || serviceHours < minServiceHour) return false;
	$$("#serviceHours").val(serviceHours);
	
	var orderHourPay = sessionStorage.getItem("order_pay");
	if (orderHourPay == undefined || orderHourPay == "") return false;
	$$("#orderHourPayStr").html(orderHourPay + "元");
}

