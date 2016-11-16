myApp.onPageBeforeInit('order-appoint', function(page) {
	
	var staffId = sessionStorage.getItem("staff_id");
	
	var staffName = sessionStorage.getItem("staff_names");
	
	$$("#staffId").val(staffId);
	$$("#staffName").val(staffName);
	$$("#spanStaffName").html(staffName);
});


function appoinSerivceType(obj, serviceTypeId) {
	

	$$('#serviceTypeList').find('li').each(function(i,item) {
		$$(this).removeClass("special-color2");
	});
	
	obj.addClass("special-color2");
	
	var serviceTypeName = obj.html();
	$$("#spanServiceType").html(serviceTypeName);
	$$("#serviceTypeId").val(serviceTypeId);
}

function doAppointOrder() {
	var serviceTypeId = $$("#serviceTypeId").val();
	
	var url = "";
	console.log("serviceTypeId = " + serviceTypeId);

	if (serviceTypeId == 28 || serviceTypeId == 29) {
		url = "order/order-hour-choose.html?service_type_id="+serviceTypeId;
	} else {
		url = "order/order-deep-intro.html?service_type_id="+serviceTypeId;
	}
	
	mainView.router.loadPage(url);
	
}




