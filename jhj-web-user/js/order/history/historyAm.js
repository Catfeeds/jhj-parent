myApp.onPageInit('history-am-page', function(page) {
	
	
	//页面加载,获得所有阿姨名单,
	
	 $$.ajax({
	      type : "GET",
	      url:siteAPIPath + "order/history_am_before.json",
		data: {"userId":localStorage['history_user_id']},
		success: function (data, status, xhr){
     		  var result = JSON.parse(data);
	  		  var staList = result.data;
			
	  		  if(staList.length === undefined){
	  			  
	  			$$("#amSelect").append("<option value=''>请选择助理 </option>").append(
	  					"<option value="+staList.staff_id+">"+staList.name+"</option>");
	  			  
	  		  }else{
	  			  for (var i = 0; i < staList.length; i++) {
	  				  var staffId =  staList[i].staff_id;
	  				  
	  				  var name = staList[i].name;
	  				  
	  				  if(i == 0){
	  					  $$("#amSelect").append("<option value=''>请选择助理 </option>");
	  				  }
	  				  
	  				  $$("#amSelect").append("<option value="+staffId+">"+name+"</option>");
	  			  }
	  		  }
	      }
	   });
	   
	/*
	 * 第一步录入的 用户 id
	 */
	var userId = localStorage['history_user_id'];
	$$("#userId").val(userId);
	
	
	var logInUserId =  localStorage['user_id'];
	$$("#logInUserId").val(logInUserId);
	
	
	//如果是选择地址后的返回方法
	var addrId = page.query.addr_id;
	var addrName = page.query.addr_name;
	
	
	if (addrId != undefined) {
		$$("#addrId").val(addrId);
	}
	
	if (addrName != undefined) {
		
		$$("#defaultAddrName").html(addrName);
	}
	
	
//	//设置默认地址
	if (addrId == undefined) {
		 if (localStorage['default_addr_id'] != null) {
			 $$("#addrId").val(localStorage['default_addr_id']);
		 }

		 if (localStorage['default_addr_name'] != null) {
			 $$("#addrName").html(localStorage['default_addr_name']);
		 }		 
		 
	}
	
	
	
	remindDateSelct();
	
	
	/*
	 * 提交订单
	 */
	
	$$("#historyAmOrderSubmit").click(function(){
		
		// 文档要求： post请求，必须是 formData类型
		var formData = myApp.formToJSON('#history-am-form');
		
		
		formData.serviceDate =  moment(formData.amServiceDateStart + ":00", "yyyy-MM-DD HH:mm:ss").unix();
		
		
		$$.ajax({
			type: "post",
			 url: siteAPIPath + "order/history_am_order.json",
			data: formData,
			success: function (data, status, xhr){
			
			myApp.formDeleteData("history-am-Form");
			
			mainView.router.loadPage("index.html");
			
		 },
		  error: function(status,xhr){
			  	myApp.alert("网络异常,请稍后再试.");
		 }
		});
	});
	
});



