myApp.onPageInit('order-form-zhongdiangong-page', function(page) {
	
	
	var serviceTypeId = $$("#serviceType").val();
	
	var paramServiceTypeId =  page.query.serviceType;
	
	if(paramServiceTypeId != undefined){
		$$("#serviceType").val(paramServiceTypeId);
	}
	
	
	var userId = localStorage['user_id'];
	$$("#userId").val(userId);
	
	//如果是选择地址后的返回方法
	var addrId = page.query.addr_id;
	var addrName = page.query.addr_name;
	
	if (addrId != undefined) {
		$$("#addrId").val(addrId);
	}
	
	if (addrName != undefined) {
		
		$$("#defaultAddrName").html(addrName);
	}
	
	//设置默认地址
	if (addrId == undefined) {
		 if (localStorage['default_addr_id'] != null) {
			 $$("#addrId").val(localStorage['default_addr_id']);
		 }

		 if (localStorage['default_addr_name'] != null) {
			 $$("#addrName").html(localStorage['default_addr_name']);
		 }		 
		 
	}
	
	serviceDateSelect();
	
	/*
	 * 回显   日期时间
	 */
	var dateTwo = sessionStorage.getItem("serviceDate");
	if(dateTwo != null){
		$$("#serviceDateSelect").html(dateTwo);
	}
	
	/*
	 * 提交订单
	 */
	$$("#submitOrder").click(function(){
		
		//表单校验不通过，不能提交
		if (formValidation() == false) {
			return false;
		}
		
		// 文档要求： post请求，必须是 formData类型
		var formData = myApp.formToJSON('#orderHour-Form');
		
		//处理 日期。。传给后台 string 类型 的 秒值，时间戳
		formData.serviceDate =  moment(formData.serviceDate + ":00", "yyyy-MM-DD HH:mm:ss").unix();
		
		
		$$.ajax({
			type: "post",
			 url: siteAPIPath + "order/post_hour.json",
			data: formData,
			success: function (data, status, xhr){
			
			var result = JSON.parse(data);
			
			if (result.status == "999") {
				myApp.alert(result.msg);
				
				return;
			}	
			
			localStorage.setItem('user_id', userId);
			
			localStorage.setItem('order_no',result.data.order_no);
			
			localStorage.setItem('order_id',result.data.id);
			
			
			/*
			 * 提交 校验通过后，，清空当前页面回显的数据
			 */
			
			sessionStorage.removeItem("serviceDate");
			
			myApp.formDeleteData("orderHour-Form");
			
			/*
			 * 此处 逻辑：
			 * 		如果 响应 成功 。
			 * 		不管 是否 成功分配阿姨，都跳转到 支付 页面。
			 * 		在 进行 支付 操作的时候，再给出提示，是否有可以服务的阿姨
			 */
			var successUrl = "order/order-form-zhongdiangong-pay.html";
			
			mainView.router.loadPage(successUrl);
			
		 },
		  error: function(status,xhr){
			  	myApp.alert("网络异常,请稍后再试.");
		 }
		});
	});

	
	// 点击 服务地址 按钮时，将 页面 变化过的值，保存在本地
	$$("#addrSelect").on('click',function(){
		//保存已选择的   服务时间
		sessionStorage.setItem('serviceDate',$$("#serviceDate").val());
	});
	
});

//表单校验
function formValidation(){
	var formDatas = myApp.formToJSON('#orderHour-Form');
	
	//当前选择 的 日期
	var  dateSelect =  formDatas.serviceDate;
	// 当前 选择的 日期 中  的   '整点小时' 数，如：08,10,19...
	var hourSelect = moment(dateSelect).format('HH');
	
	//选择日期的 年月日
	var daySelect = moment(dateSelect).format('YYYYMMDD');
	
	//现实日期的 年月日
	var realDate = moment().format("YYYYMMDD");
	
//	//服务时长
//	var serviceHourSelect = formDatas.serviceHour;
//	var reg =/[\u4E00-\u9FA5]/g;
//	var hour = serviceHourSelect.replace(reg,"");
	
	//当前整点小时数
	var nowHour = moment().hour();
	
	//选择的时间 小于 当前时间
	if(Number(hourSelect) <= nowHour && daySelect == realDate){
		myApp.alert("现在时间："+ moment().format("YYYY-MM-DD HH:MM ")+"\r\n"+"请选择合适的服务时间");
		return false;
	}
	
	//服务时间 固定为了 3小时
	if(Number(hourSelect)+3 > 21){
		myApp.alert("服务结束时间超过21点,无法提供服务");
		return false;
	}
	
	var name =  $$("#addrName").html();
	
	if(name == '请选择服务地址'){
		alert("请选择服务地址");
		return false;
	}
	
	return true;
}


//判断字符串 以  "xxx" 结尾
String.prototype.endWith=function(endStr){
	  var d=this.length-endStr.length;
	  return (d>=0&&this.lastIndexOf(endStr)==d);
}


