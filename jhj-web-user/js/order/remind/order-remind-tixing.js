myApp.onPageInit('order-remind-tixing-page',function(){
	
	//新用户，不添加地址，引导其添加地址获得助理
	var nowAmId = localStorage['am_id'];
	
	if(nowAmId == 'null'){
		myApp.alert('点击确定前往添加地址立刻获得家庭助理', "", function () {
			mainView.router.loadPage("user/mine-add-addr.html?addr_id=0&return_url=user/user-am-detail.html");
	    });
		
		return;
	}
	
	
	var user_id = localStorage['user_id'];
	
	$$("#userId").val(user_id);
	
	$$("#remindServiceDate").val(moment().format('YYYY-MM-DD  HH:mm'));
	
	// 提醒类业务 日期选择插件
	remindDateSelct();
	
	$$("#startRemind").click(function(){
		
		if (tixingFormVaidation() == false) {
			return false;
		}
		
		var formData = myApp.formToJSON('#order-remind-tixing-form');
		
		formData.serviceDate =  moment(formData.serviceDate + ":00", "yyyy-MM-DD HH:mm:ss").unix();
		
		//处理 特殊字符
		formData.title = encodeURIComponent(formData.title);
		formData.remarks = encodeURIComponent(formData.remarks);
		
		$$.ajax({
			type: "post",
			 url: siteAPIPath + "remind/post_remind.json",
			data: formData,
			success: function (data, status, xhr){
				
				var result = JSON.parse(data);
				
				var order_no = result.data.order_no;
				
				var successUrl = "order/remind/order-remind-tixing-success.html?order_no="+order_no;
				
				mainView.router.loadPage(successUrl);
			},
			error:function(){
				myApp.alert("网络异常,请稍后重试");
			}
		});
	});
	
});



function tixingFormVaidation(){
	
	var serviceDate = $$("#remindServiceDate").val();
	//选择的时间戳
	var selectTime =   moment(serviceDate + ":00", "yyyy-MM-DD HH:mm:ss").unix();
	
	//现在的时间戳
	var nowTime  =  Math.round(new Date() / 1000);
	
	if(nowTime > selectTime){
		myApp.alert("请您提前至少1小时预约");
		return false;
	}
	if(nowTime +3600 > selectTime){
		myApp.alert("请您提前至少1小时预约");
		return false;
	}
	
	var title = $$("#title").val();
	
	if(title.length == 0){
		myApp.alert("请您输入提醒标题");
		return false;
	}
	
	var remarks = $$("#remarks").val();

	if(remarks.length == 0){
		myApp.alert("请您输入提醒内容");
		return false;
	}
	
}