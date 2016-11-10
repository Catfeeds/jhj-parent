myApp.onPageInit('order-hour-choose', function(page) {

	var serviceTypeId = page.query.service_type_id;
	sessionStorage.setItem("service_type_id", serviceTypeId);
	
	sessionStorage.setItem("order_type", 0);
	
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
		$$("#orderChooseAddrName").html(addrName);
	}
	
	initOrderHour();
	
	 $$("#chooseServiceTime").on("click",function(){
		 
		 var checkOrderHour = setOrderHourTotal();
		 
		 if (checkOrderHour == false) return false;
		 
		 if ($$("#addrId").val() == "") {
			 myApp.alert("请选择地址.");
			 return false;
		 } 
		 var url = "order/order-lib-cal.html?next_url=order/order-hour-confirm.html"
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
		$$("#staffNums").focus();
		return false;
	}
	
	if (serviceHours == undefined || serviceHours == "" || serviceHours < 3) {
		alert("预约服务时间最少为3小时.");
		$$("#serviceHours").focus();
		return false;
	}
	
	var orderHourPay = "149";
	
	if (staffNums > 1 || serviceHours > 3) {
		orderHourPay = 50 * serviceHours * staffNums;
	}
	
	$$("#orderHourPayStr").html(orderHourPay + "元");
	
	sessionStorage.setItem("order_money", orderHourPay);
	sessionStorage.setItem("order_pay", orderHourPay);
	sessionStorage.setItem("total_staff_nums", staffNums);
	sessionStorage.setItem("total_service_hour", serviceHours);
	
	return true;
}


function initOrderHour() {
	var staffNums = sessionStorage.getItem("total_staff_nums");
	if (staffNums == undefined || staffNums == "" || staffNums <= 0) return false;
	$$("#staffNums").val(staffNums);
	
	var serviceHours = sessionStorage.getItem("total_service_hour");
	if (serviceHours == undefined || serviceHours == "" || serviceHours < 3) return false;
	$$("#serviceHours").val(serviceHours);
	
	var orderHourPay = sessionStorage.getItem("order_pay");
	if (orderHourPay == undefined || orderHourPay == "") return false;
	$$("#orderHourPayStr").html(orderHourPay + "元");
}