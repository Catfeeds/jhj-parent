myApp.onPageInit('order-hour-choose', function(page) {
	
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
	
	 $$("#chooseServiceTime").on("click",function(){
		 if ($$("#addrId").val() == "") {
			 myApp.alert("请选择地址.");
			 return false;
		 } 
		 var url = "order/order-lib-cal.html?next_url=order/order-hour-confirm.html"
		 mainView.router.loadPage(url);
	 });
});