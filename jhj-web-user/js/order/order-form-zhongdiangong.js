myApp.onPageInit('order-form-zhongdiangong-page', function(page) {
	
	
	var userId = localStorage['user_id'];
	$$("#userId").val(userId);
	
	//如果是选择地址后的返回方法
	var addrId = page.query.addr_id;
	var addrName = page.query.addr_name;
	
	if (addrId != undefined) {
		$$("#addrId").val(addrId);
	}
	
	if (addrName != undefined) {
//		$$("#addrName").html(addrName);
		
		$$("#defaultAddrName").html(addrName);
//		$$("#addrName").text(addrName);
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
	 * 回显   日期时间、选择的价格
	 */
	var dateTwo = sessionStorage.getItem("serviceDate");
	if(dateTwo != null){
		$$("#serviceDate").html(dateTwo);
	}
	
	var priceTwo = sessionStorage.getItem('sumPrice');
	if(!isNaN(priceTwo)){
		if(priceTwo == '' || priceTwo == null){
			priceTwo = 30;
		}
		$$("#price").html(priceTwo+"元/小时,两小时起");
	}
	
		
	/*
	 * 动态选择附加服务，以及 单价变化
	 */	
		
	// 附加服务 Id 的 字符串
	var tagIds = "";
	// 已选附加服务的 总价/小时
	var price = "";
	//点击图片变色效果。 有一定的局限性（需要是 .png 格式  、选中后的图片名需要是    xxx-2x.png 、 未选中的图片名需要是  xxx.png ）
	$$("img[src$='.png']").on('click',function(e) {
		
		var arr =  $$(this).attr("src").split(".");
		 
		if(arr[0].endWith("-2x")){
			$$(this).attr("src",arr[0].replace("-2x","")+".png");
		}else{
			$$(this).attr("src",arr[0]+"-2x"+".png");
		}
		
		getTagAndPriceSelect();
		
	});
	
	// 回显后 提交订单 也需要 遍历 所有 选中的 图片（代码块位置 必须在 提交前，否则tagIds，会被覆盖）
	function getTagAndPriceSelect(){
		price = $$("#price").attr("value");
		tagIds = $$("#serviceAddons").attr("value");
		
		$$("img[name = tag]").each(function(key, index) {
			
			//被选中的图片
			if ($$(this).attr("src").indexOf("-2x")>0) {
				tagIds = tagIds + $$(this).prev().val() + ",";
				/*
				 *数字 求和,动态 改变价格
				 */
				price = Number(price) + Number($$(this).next().attr("value"));
			}
		});	
		if (tagIds != "") {
			tagIds = tagIds.substring(0, tagIds.length - 1);
		}
		$$("#price").html(price+"元/小时,两小时起");
		
	}

	
	/*
	 * 提交订单
	 */
	
	$$("#submitOrder").click(function(){
		
		//表单校验不通过，不能提交
		if (formValidation() == false) {
			return false;
		}
		
		
		//提交时，再次 遍历 （回显时，没有触发click）
		getTagAndPriceSelect();
		
		// 文档要求： post请求，必须是 formData类型
		var formData = myApp.formToJSON('#orderHour-Form');
		formData.serviceAddons = tagIds;
		
		//处理 日期。。传给后台 string 类型 的 秒值，时间戳
		//去除警告：moment construction falls back to js Date
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
			
			sessionStorage.removeItem("sumPrice");
			
			sessionStorage.removeItem("serviceAddons");
			
			myApp.formDeleteData("orderHour-Form");
			
			/*
			 * 此处 逻辑：
			 * 		如果 响应 成功 。
			 * 		不管 是否 成功分配阿姨，都跳转到 支付 页面。
			 * 		在 进行 支付 操作的时候，再给出提示，是否有可以服务的阿姨
			 */
			var successUrl = "order/order-form-zhongdiangong-pay.html";
//			successUrl +="?order_no="+resul9
			mainView.router.loadPage(successUrl);
			
		 },
		  error: function(status,xhr){
			  	myApp.alert("网络异常,请稍后再试.");
		 }
		});
	});

	var serTypeList = JSON.parse(localStorage.getItem("service_type_addons_list"));
	
	for (var i = 0; i < serTypeList.length; i++) {
		var item =  serTypeList[i];
		
		if(item.name == "做饭"){
			$$("#zuofanPrice").val(item.price);
			$$("#zuofanId").val(item.service_addon_id);
		}
		if(item.name == "洗衣"){
			$$("#xiyiPrice").val(item.price);
			$$("#xiyiId").val(item.service_addon_id);
		}
		if(item.name == "清洁用品"){
			$$("#qingjiePrice").val(item.price);
			$$("#qingjieId").val(item.service_addon_id);
		}
	}
	
	/*
	 * 只有在  页面第一次加载 完成之后，才会有  回显 图片 的要求
	 */
	var addonIds = sessionStorage.getItem('serviceAddons');
	if(addonIds != null){
		$$("img[name = tag]").each(function(key, index) {
			
			var addonId =  $$(this).prev().val();
			
			var arr =  $$(this).attr("src").split(".");
			
			if(addonIds.indexOf(addonId) >= 0){
				$$(this).attr("src",arr[0]+"-2x"+".png");
			}
		});	
	}
	
	
	// 点击 服务地址 按钮时，将 页面 变化过的值，保存在本地
	$$("#addrSelect").on('click',function(){
		
		//保存已选择的   服务时间
		sessionStorage.setItem('serviceDate',$$("#serviceDate").val());
		//保存已选择 的 附加服务Id及 价格
		
		var tagIdss = "";
		var pricess = "";
		
		var aPrice = $$("#price").attr("value");
		
		$$("img[name = tag]").each(function(key, index) {
			
			//被选中的图片
			if ($$(this).attr("src").indexOf("-2x")>0) {
				tagIdss = tagIds + $$(this).prev().val() + ",";
				/*
				 *数字 求和,动态 改变价格
				 */
				aPrice = Number(aPrice) + Number($$(this).next().attr("value"));
			}
		});	
		
		sessionStorage.setItem('serviceAddons',tagIdss);
		sessionStorage.setItem('sumPrice',aPrice);
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
	
	//服务时长
	var serviceHourSelect = formDatas.serviceHour;
	var reg =/[\u4E00-\u9FA5]/g;
	var hour = serviceHourSelect.replace(reg,"");
	
	//当前整点小时数
	var nowHour = moment().hour();
	
	//选择的时间 小于 当前时间
	if(Number(hourSelect) <= nowHour && daySelect == realDate){
		alert("现在时间："+ moment().format("YYYY-MM-DD HH:MM ")+"\r\n"+"请选择合适的服务时间");
		return false;
	}
	
	
	if(Number(hourSelect)+Number(hour) > 21){
		alert("服务结束时间超过21点,无法提供服务");
		return false;
	}
	
	var name =  $$("#addrName").html();
	
	if(name == '请选择服务地址'){
		alert("请选择服务地址");
		return false;
	}
	
	if(formDatas.serviceHour == ""){
		alert("请选择服务时长");
		return false;
	}
	return true;
}


//判断字符串 以  "xxx" 结尾
String.prototype.endWith=function(endStr){
	  var d=this.length-endStr.length;
	  return (d>=0&&this.lastIndexOf(endStr)==d);
}


