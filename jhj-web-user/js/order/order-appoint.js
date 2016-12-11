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

	if (serviceTypeId == 28 || 
	    serviceTypeId == 29 || 
	    serviceTypeId == 68 || 
	    serviceTypeId == 69 ||
	    serviceTypeId == 70 ) {
		url = "order/order-hour-choose.html?service_type_id="+serviceTypeId;
	} else {
		url = "order/order-deep-choose.html?service_type_id="+serviceTypeId;
	}
	
	mainView.router.loadPage(url);
	
}

//列表显示，获取用户的信息
myApp.template7Data['page:order-appoint']=function(){
	var result="";
	var staffId = sessionStorage.getItem("staff_id");

	$$.ajax({
		type : "GET",
		url : siteAPIPath + "staff/get_skills.json?staff_id="+staffId,
		dataType : "json",
		cache : true,
		async : false,
		success : function(data) {
			result = data;
		}
	})
	return result;
}




