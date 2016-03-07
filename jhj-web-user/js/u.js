
initHomePage();

function initHomePage() {
	console.log("initHomePage");
	var result;
	console.log("111111111");
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "indexAd/get_ad_list.json",
		dataType : "json",
		cache : true,
		async : false,
		success : function(data) {

			result = data;
			console.log(result);
			var dictAdList = result.data;
//			var html = $$('#index-imgUrl-list').html();
			var resultHtmlNow = '';
			for (var i = 0; i < dictAdList.length; i++) {
				var imgUrl = dictAdList[i];
				if (imgUrl.img_url == "") {
					//resultHtmlNow += "<div class=\"swiper-slide\"style=\"height:150px;\"><img src=\"img/tmp/13.png\"></div>";
					continue;
				} else {
					imgUrl = localUrl + imgUrl.img_url;
					console.log(imgUrl);
					resultHtmlNow += "<div class=\"swiper-slide\"style=\"height:150px;\"><img src=\""
							+ imgUrl + "\"></div>";
				}
			}
			
			
			if (resultHtmlNow == "") {

				resultHtmlNow = "<div class=\"swiper-slide\"style=\"height:150px;\"><img src=\"img/tmp/13.png\"></div>";
			}
			
			var html = "<div class=\"swiper-wrapper\" id=\"swiper-list\">";
			
			html += resultHtmlNow;
			html += "</div><div class=\"swiper-pagination\"></div>"
			
			$$(".swiper-container").html(html);
//			console.log($$(".swiper-container").html());
			
			var mySwiper = myApp.swiper('.swiper-container', {
				pagination:'.swiper-pagination',
				autoplay: 2000
			});				
	
		}

	})
	return result;
}

$$(document).on('pageBeforeInit', function(e) {
	var page = e.detail.page;

	if (page.name === 'index') {
		initHomePage();
		
		//首页滚动广告
	}
})


;myApp.onPageInit('login', function (page) {
	$$('#get_code').on('click',function(e) {
		var mobile = $$("#user_mobile").val();		
        if(mobile == undefined || mobile == '') {
        	myApp.alert("请填写手机号。");
            return false;
        }
        var moblieStr = mobile.trim();
        if(moblieStr.length != 11) {
        	myApp.alert("请填写正确的手机号码");
        	return false;
        }

        var count = 60;
        var countdown = setInterval(CountDown, 1000);
        function CountDown(){
        	$$("#get_code").attr("disabled", true);
            $$("#get_code").css("background","#999");
            $$("#get_code").text(count + "秒");
            if (count == 0) {
                $$("#get_code").removeAttr("disabled");
                $$("#get_code").css("background","#f37b1d");
                $$("#get_code").text("获取验证码");
                clearInterval(countdown);
            }
            count--;
        }

        var postdata = {};
        postdata.mobile = moblieStr;
        postdata.sms_type = 0;

        $$.ajax({
        	url:siteAPIPath+"user/get_sms_token.json",
//    		headers: {"X-Parse-Application-Id":applicationId,"X-Parse-REST-API-Key":restApiKey},
        	contentType:"application/x-www-form-urlencoded; charset=utf-8",
        	type: "GET",
    	    dataType:"json",
    	    cache:true,
    	    data: postdata,

    	    statusCode: {
    	    	201: smsTokenSuccess,
    	    	400: ajaxError,
    	    	500: ajaxError
    	    }
    	});
        
    	var smsTokenSuccess = function(data, textStatus, jqXHR) {
    		
    		// We have received response and can hide activity indicator
    	   	myApp.hideIndicator();    			
    	   	myApp.alert("验证码已发送到您的手机，请注意查收。");
    	};
        
        return false;
	});
	
	
	 //登录
    $$('#login_btn').on('click', function(e){
        //var formData = $('#loginform').serialize();
    	var mobile = $$("#user_mobile").val();
    	var verifyCode = $$("#verify_code").val();
        
        if(mobile == '' || verifyCode == '') {
          myApp.alert("请填写手机号或验证码。");
          return false;
        }
        if(mobile.length != 11 ) {
          myApp.alert("请填写正确的手机号码");
          return false;
        }

        var loginSuccess = function(data, textStatus, jqXHR) {
    		// We have received response and can hide activity indicator
    	   	myApp.hideIndicator();
    	   	
    	   	var result = JSON.parse(data.response);
    	   	if (result.status == "999") {
    	   		myApp.alert(result.msg);
    	   		return;
    	   	}
    	   	
    	   	if (result.status == "0") {
    	   	  //登录成功后记录用户有关信息
    	   	  localStorage['user_mobile'] = result.data.mobile;
    	   	  localStorage['user_id']= result.data.id;
    	   	  localStorage['im_username'] = result.data.im_user_name;
    	   	  localStorage['im_password'] = result.data.im_password;
    	   	}
    	   	var flag =result.data.has_user_addr;
    	   	if(flag ==true){
    	   	//返回用户浏览的上一页
	   			var target = mainView.history[mainView.history.length-2];
	   			
	   			mainView.router.loadPage(target);
    	   //mainView.router.loadPage("index.html");
    	   	}else{
    	   		mainView.router.loadPage("user/mine-add-addr.html");
    	   	}
    	   
    	};                
        
        
        var postdata = {};
        postdata.mobile = mobile;
        postdata.sms_token = verifyCode;        
        postdata.login_from = 1;
        postdata.user_type = 2;

        $$.ajax({
            type : "POST",
            // type : "GET",
            url  : siteAPIPath+"user/login.json",
            dataType: "json",
//            contentType:"application/x-www-form-urlencoded; charset=utf-8",
            cache : true,
            data : postdata,
            
            statusCode: {
            	200: loginSuccess,
    	    	201: loginSuccess,
    	    	400: ajaxError,
    	    	500: ajaxError
    	    }
        });


        
        return false;
    });	
	
	$$(".yanzheng1").click(function(){
        $$(".login-tishi1").css("display","block");
    })
});
    
;myApp.onPageBeforeInit('order-am-faqiyuyue-page', function (page) {

var userId = localStorage.getItem("user_id");
var serviceType = page.query.service_type;

var servcieTypeList = JSON.parse(localStorage.getItem("service_type_list"));

$$.each(servcieTypeList, function(i,item){
	if (item.id == serviceType) {
		$$('#servce_type_name').append(item.name);
		$$('#service_type_tips').append(item.tips);
	}
});

//获取用户地址列表
$$('#order-am-faqiyuyue').on('click',function(){
	

	if (orderAmFormValidation() == false) {
		console.log(orderFormValidation);
	
		return false;
	}
	
	var serviceContent = $$("#service_content").val();
    $$.ajax({
        type:"POST",
        url:siteAPIPath+"order/post_user.json",
        dataType:"json",
        cache:false,
        data:{"user_id":userId,
        	  "service_type":serviceType,
              "service_content":serviceContent,
              "order_from":1
      },
        statusCode: {
         	200: onListSuccess,
 	    	400: ajaxError,
 	    	500: ajaxError
 	    },
    });

    
   
});


$$("#service_content").click(function(){
	$$("#myContent").css('padding','10px');
	
});

function onListSuccess(data, textStatus, jqXHR){
	$$("#order-am-faqiyuyue").attr("disabled", false);
   	var result = JSON.parse(data.response);
	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
	var order = result.data;
	var orderNo = order.order_no;
	mainView.router.loadPage("order/order-am-result.html?user_id="+userId+"&order_no="+orderNo);
}
});

function orderAmFormValidation() {
	$$("#order-am-faqiyuyue").removeAttr('disabled');
	var formData = myApp.formToJSON('#order-am-faqiyuyue-form');

	if (formData.service_content == undefined || formData.service_content == "") {
		myApp.alert("请输入服务内容");
		$$("#order-am-faqiyuyue").removeAttr('disabled');
		return false;
	}
	
	return true;
};myApp.onPageInit('order-am-page', function(page) {
	
	var servcieTypeList = JSON.parse(localStorage.getItem("service_type_list"));

	var html = $$('#order-am-part').html();

	var resultHtml = '';
	$$.each(servcieTypeList, function(i,item){
		
		if (item.id >= 3) {
			var htmlPart = html;
			var imgTag = '<img src="img/icons/service_type_img_' + item.id + '.png" alt=""> ';
			htmlPart = htmlPart.replace(new RegExp('{img_tag}',"gm"), imgTag);
			htmlPart = htmlPart.replace(new RegExp('{id}',"gm"), item.id);
			//htmlPart = htmlPart.replace(new RegExp('{img_id}',"gm"), item.id);
			htmlPart = htmlPart.replace(new RegExp('{name}',"gm"), item.name);
			htmlPart = htmlPart.replace(new RegExp('{tips}',"gm"), item.tips);
	
			resultHtml += htmlPart;		
		}
		
	});

	$$('.order-am-list ul').append(resultHtml);
});;myApp.onPageInit('orderHour-pay-page', function(page) {
	
	var userId = localStorage['user_id'];
	var orderNo = localStorage['order_no'];
	var orderId = localStorage['order_id'];
	
	//默认支付类型
	var orderPayType = 0;
	
	
	/*
	 * 优惠券
	 */
	//订单状态
	var orderStatus = $$("#orderStatus").val();
	
	//总金额
	var money  = $$("#orderMoney").text();
	//util.js  处理  ￥符号
	var order_money = delSomeString(money);
	
	localStorage.setItem('u_order_money_param', order_money);
	
	var order_type = $$("#orderType").val();
	
	if(orderStatus == 3) {
		//点击选择优惠劵
		$$("#selectCoupon").click(function() {
			
			var linkUrl = "user/coupon/mine-coupon-list.html";
			linkUrl+= "?order_type="+order_type;
			linkUrl+= "&order_money="+order_money;
			mainView.router.loadPage(linkUrl);
		});
		
		//用户返回优惠劵之后的处理
		var userCouponId = page.query.user_coupon_id;
		var userCouponName = page.query.user_coupon_name;
		var userCouponValue = page.query.user_coupon_value;
		console.log(userCouponName);
		if (userCouponId != undefined && userCouponName != undefined) {
			$$("#user_coupon_id").val(userCouponId);
			$$("#couponSelect").html("为您节省了"+userCouponValue+"元");
			if (userCouponValue == undefined) userCouponValue = 0;
			
			var realMoney = parseFloat(order_money) - parseFloat(userCouponValue);
			if(realMoney <= 0){
				realMoney = 0;
			}
			$$("#real-order-money-label").html('￥'+realMoney);
		}
	}
	
	
	//选择支付类型的处理
//	$$('input[type="checkbox"]').on('change', function() {
//		  this.checked = true;
//		  var v = this.value;
//		  $$('input[type="checkbox"]').each(function(key,index) {
//			 if (this.value != v) this.checked= false;
//		  });
//	});
	
	$$('label[name=myPayTypeSelect]').on('click',function(){
		
		var nowValue = $$(this).find("input").val();
		
		//单选
		$$('img[name="order_pay_type"]').each(function(index,value){
			if($$(this).prev().val() == nowValue){
				$$(this).attr("src","img/icons/duigou-green.png");
			}else{
				$$(this).attr("src","img/icons/duigou-grey.png");
			}
			
		});

		
	});
	
	
	
	
	var postOrderPaySuccess =function(data, textStatus, jqXHR) {
		$$(".zf-hour-submit").removeAttr('disabled');  
	 	var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			
			// 重复支付或者订单不存在等情况，禁用支付按钮
			$$(".zf-hour-submit").attr("disabled", true);
			return;
		}
		//orderPayType = result.data.pay_type;
		//如果为余额支付，则直接跳到完成页面
		if (orderPayType == 0) {
			mainView.router.loadPage("order/order-pay-success.html?order_no="+orderNo+"&order_type=0");
		}
		
		//如果为微信支付，则需要跳转到微信支付页面.
		if (orderPayType == 2) {
			 var userCouponId = $$("#user_coupon_id").val();
			 if (userCouponId == undefined) userCouponId = 0;
			 var wxPayUrl = localUrl + "/" + appName + "/wx-pay-pre.jsp";
			 wxPayUrl +="?orderId="+orderId;
			 wxPayUrl +="&userCouponId="+userCouponId;
			 wxPayUrl +="&orderType=0";
			 wxPayUrl +="&payOrderType=0";
			 location.href = wxPayUrl;
		}
	};
	
	//点击支付的处理
	$$(".zf-hour-submit").click(function(){
		$$(".zf-hour-submit").attr("disabled", true);
		var postdata = {};
		postdata.user_id = userId;
		postdata.order_no = orderNo;
		
		var userCouponId = $$("#user_coupon_id").val();
		if (userCouponId == undefined) userCouponId = 0;
		postdata.user_coupon_id = userCouponId;
		
//		$$('input[type="checkbox"]').each(function(key,index) {
//			 if(this.checked) {
//				 orderPayType = this.value;
//			 }
//		 });
		
		
		$$('img[name="order_pay_type"]').each(function(index,value){
			if($$(this).attr("src").indexOf("green")>0){
				orderPayType = $$(this).prev().val();
			}
			
		});
		
		
		
		postdata.order_pay_type = orderPayType;
		
		$$.ajax({
			type: "post",
			 url: siteAPIPath + "order/post_pay.json",
			data: postdata,
			statusCode: {
	         	200: postOrderPaySuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});		
		 
	});

});	


myApp.template7Data['page:orderHour-pay-page'] = function(){
	    var result; 
	    var postdata = {};
		var order_no = localStorage['order_no'];
		
		postdata.order_no = order_no;

	  
	   $$.ajax({
          type : "GET",
          url:siteAPIPath + "order/order_hour_detail.json",
          dataType: "json",
          data : postdata,
          cache : true,
          async : false,	// 不能是异步
          success: function(data){
//        	  console.log(data);
              result = data.data;
              
              //设置时间显示格式
              var timestamp = moment.unix(result.service_date);
  			  var startTime = timestamp.format('YYYY-MM-DD HH:mm');
  			  result.service_date = startTime;
  			  
          }
	   })
	  
	  return result;
}


;myApp.onPageInit('order-form-zhongdiangong-page', function(page) {
	
	
	var userId = localStorage['user_id'];
	$$("#userId").val(userId);
	
	//如果是选择地址后的返回方法
	var addrId = page.query.addr_id;
	var addrName = page.query.addr_name;
	
	if (addrId != undefined) {
		$$("#addrId").val(addrId);
	}
	
	if (addrName != undefined) {
		$$("#addrName").html(addrName);
	}	
	
	//时间 选择 插件。  包含了 赋值 默认值 ，所以要放在 回显 代码 的前面
	dateSelect();
	
	/*
	 * 回显   日期时间、选择的价格
	 */
	var dateTwo = sessionStorage.getItem("serviceDate");
	if(dateTwo != null){
		$$("#serviceDate").val(dateTwo);
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
//			successUrl +="?order_no="+result.data.order_no;
//			successUrl +="&order_id="+result.data.id;
			mainView.router.loadPage(successUrl);
			
		 },
		  error: function(status,xhr){
			  		alert("网络异常,请稍后再试.");
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
		alert("超过21点，无法提供服务");
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


;myApp.onPageBeforeInit('order-get-rate-page', function(page) {
	
	console.log("111111111111");
	var orderId = page.query.order_id;
	//var result = JSON.parse(data.response);
	//var order = result.data;
	//var servcieTypeAddonsList = JSON.parse(localStorage.getItem("service_type_addons_list"));
	
	
	//获取订单详情
	$$.ajax({
		type : "GET",
		url :siteAPIPath+"order/get_rate.json?order_id="+orderId,
		dataType : "json",
		cache : true,
		success: function(datas,status,xhr){
			console.log(datas);
			var result = datas.data;
			
			/* $$(orderRateVo).each(function(key,index) {
				 console.log(key);
			  });*/
			var content = '';
			var html = $$('#order-am-clean-part').html();
			var list = result.list;
			for(var i=0;i<list.length;i++){
				
			var orderRateVo = list[i];
		    console.log(orderRateVo);
		    
		    content = orderRateVo.rate_content;
		    console.log(content+"------------content-------");
		    
		    rateType = orderRateVo.rate_type;
		    console.log(rateType+"------------rateType-------");
		    
		    rateValue = orderRateVo.rate_value;
		    console.log(rateValue+"------------rateValue-------");
		    if(rateType == 0 ){
				$$("img[name=order_type_0_img]").each(function(key, index) {
					if ($$(this).attr("value") == rateValue) {
						$$(this).attr("src", "img/icons/shi-x.png");
					} else {
						$$(this).attr("src", "img/icons/shi.png");
					}
				});
		    }
		    if(rateType == 1 ){
				$$("img[name=order_type_1_img]").each(function(key, index) {
					if ($$(this).attr("value") == rateValue) {
						$$(this).attr("src", "img/icons/shi-x.png");
					} else {
						$$(this).attr("src", "img/icons/shi.png");
					}
				});
		    }
		    if(rateType == 2 ){
				$$("img[name=order_type_2_img]").each(function(key, index) {
					if ($$(this).attr("value") == rateValue) {
						$$(this).attr("src", "img/icons/shi-x.png");
					} else {
						$$(this).attr("src", "img/icons/shi.png");
					}
				});
		    }
		    if(rateType == 3 ){
		    	
				$$("img[name=order_type_3_img]").each(function(key, index) {
					var thisValue = $$(this).attr("value");
					if (thisValue == rateValue) {
						if(rateValue == 0 )
							$$(this).attr("src", "img/icons/henbang-x.png");
						if(rateValue == 1 )
							$$(this).attr("src", "img/icons/yiban-x.png");
						if(rateValue == 2 )
							$$(this).attr("src", "img/icons/jiaocha-x.png");				
					} 
					else {
						if(thisValue == 0 )
							$$(this).attr("src", "img/icons/henbang.png");
						if(thisValue == 1 )
							$$(this).attr("src", "img/icons/yiban.png");
						if(thisValue == 2 )
							$$(this).attr("src", "img/icons/jiaocha.png");
					}
				});
				}
		    if(rateType == 4 ){
		    	
				$$("img[name=order_type_4_img]").each(function(key, index) {
					var thisValue = $$(this).attr("value");
					if (thisValue == rateValue) {
						if(rateValue == 0 )
							$$(this).attr("src", "img/icons/henbang-x.png");
						if(rateValue == 1 )
							$$(this).attr("src", "img/icons/yiban-x.png");
						if(rateValue == 2 )
							$$(this).attr("src", "img/icons/jiaocha-x.png");				
					} 
					else {
						if(thisValue == 0 )
							$$(this).attr("src", "img/icons/henbang.png");
						if(thisValue == 1 )
							$$(this).attr("src", "img/icons/yiban.png");
						if(thisValue == 2 )
							$$(this).attr("src", "img/icons/jiaocha.png");
					}
				});
				}
		    
			}
			$$("#rateContent").text(content);
			$$("#rateType").text(rateType);
			$$("#rateValue").text(rateValue);

 	    },
	});
/*	var order = result.data;
	var list = order.list;
	var html = $$('#order-am-clean-part').html();
	var resultHtml = '';
		for(var i = 0; i< servcieTypeAddonsList.length; i++) {
	//	 var dictServiceAddon= servcieTypeAddonsList[i];
		 
			var htmlPart = html;
			htmlPart = htmlPart.replace(new RegExp('{service_addon_id}',"gm"), service_addon_id);
			htmlPart = htmlPart.replace(new RegExp('{service_addon_name}',"gm"), serviceAddonName);
			htmlPart = htmlPart.replace(new RegExp('{item_price}',"gm"), itemPrice);
			htmlPart = htmlPart.replace(new RegExp('{item_unit}',"gm"), dictServiceAddon.item_unit);
			htmlPart = htmlPart.replace(new RegExp('{item_num}',"gm"), dictServiceAddon.default_num);
			resultHtml += htmlPart;
		
	}
*/


});





;myApp.onPageBeforeInit('order-hour-now-list-page', function (page) {
	
	var postdata = {};
	var page = 1;
	var userId = localStorage['user_id'];
	var loading = false;// 加载flag
	var apiUrl = "order/orderHourNowList.json";
	postdata.user_id = userId;
	postdata.page = page;
	
	var orderListSuccess= function(data, textStatus, jqXHR) {

		var result = JSON.parse(data.response);
		var orders = result.data;
		
		var html = $$('#order-hour-list').html();
		
		var resultHtmlNow = '';	//当前订单
		
		for(var i = 0; i< orders.length; i++) {
			var order = orders[i];
			
			var htmlPart = html;
			//var img_tag = '<img alt="" src="img/icons/order_type_img_'+ order.order_type +'.png ">';
			//htmlPart = htmlPart.replace(new RegExp('{img_tab}',"gm"), img_tag);
			htmlPart = htmlPart.replace(new RegExp('{order_type}',"gm"), order.order_type);
			htmlPart = htmlPart.replace(new RegExp('{order_no}',"gm"), order.order_no);
			htmlPart = htmlPart.replace(new RegExp('{order_hour_type_name}',"gm"), order.order_hour_type_name);
			htmlPart = htmlPart.replace(new RegExp('{order_hour_status_name}',"gm"), order.order_hour_status_name);
			
			if(order.order_type == 2){
				htmlPart = htmlPart.replace(new RegExp('{service_date}',"gm"), moment.unix(order.add_time).format("YYYY-MM-DD HH:mm"));
			}else{
				htmlPart = htmlPart.replace(new RegExp('{service_date}',"gm"), moment.unix(order.service_date).format("YYYY-MM-DD HH:mm"));
			}
			
			htmlPart = htmlPart.replace(new RegExp('{address}',"gm"), order.address);
			
			resultHtmlNow += htmlPart;
		}
		//当前订单
		$$("#card-hour-now-list ul").append(resultHtmlNow);
		
		page = page + 1;
		loading = false;
			
		if (orders.length < 10) {
			
			$$('#order-list-more').css("display", "none");
			return;			
		}

	};	
	
	postdata.user_id = userId;
	postdata.page = page;
	 $$.ajax({
         type : "GET",
         url  : siteAPIPath + apiUrl,
         dataType: "json",
         cache : true,
         data : postdata,
 		statusCode : {
 			200 : orderListSuccess,
 			400 : ajaxError,
 			500 : ajaxError
 		},
 		
	 });
	// 注册'infinite'事件处理函数
	$$('#order-list-more').on('click', function () {
		if (loading) return;//如果正在加载，则退出
		loading = true;// 设置flag
		postdata.user_id = userId;
		postdata.page = page;
		
		$$.ajax({
			type : "GET",
			url  : siteAPIPath + apiUrl,
			dataType : "json",
			cache : true,
			data : postdata,
			timeout: 10000, //超时时间：10秒
			statusCode : {
				200 : orderListSuccess,
				400 : ajaxError,
				500 : ajaxError
			},
		});
	}); 
	
	$$('#btn-order-now').on('click', function() {

		if ($$(this).attr("class") != "dangqian-box active-state") {
			$$(this).attr("class", "dangqian-box");
			$$('#btn-order-history').attr("class", "history-box");		
			
			apiUrl = "order/orderHourNowList.json";
			$$('#order-list-more').css("display", "block");
			$$("#card-hour-now-list ul").html("");
			page = 1;
			var postdata = {};
			postdata.user_id = userId;
			postdata.page = page;
			 $$.ajax({
		         type : "GET",
		         url  : siteAPIPath+"order/orderHourNowList.json",
		         dataType: "json",
		         cache : true,
		         data : postdata,
		 		statusCode : {
		 			200 : orderListSuccess,
		 			400 : ajaxError,
		 			500 : ajaxError
		 		},
		 		
			 });			
			
		}
	});
	
	$$('#btn-order-history').on('click', function() {

		if ($$(this).attr("class") != "history-box1 active-state") {
			$$(this).attr("class", "history-box1");
			$$('#btn-order-now').attr("class", "dangqian-box1");
			
			apiUrl = "order/orderHourOldList.json";
			$$('#order-list-more').css("display", "block");
			$$("#card-hour-now-list ul").html("");
			page = 1;
			var postdata = {};
			postdata.user_id = userId;
			postdata.page = page;
			 $$.ajax({
		         type : "GET",
		         url  : siteAPIPath+"order/orderHourOldList.json",
		         dataType: "json",
		         cache : true,
		         data : postdata,
		 		statusCode : {
		 			200 : orderListSuccess,
		 			400 : ajaxError,
		 			500 : ajaxError
		 		},
		 		
			 });			
			
		}
	});	
	
	
});


function hrefToUorderView(order_type, order_no) {
	localStorage.setItem('u_order_no_param', order_no);
	mainView.router.loadPage("order/order-view-"+order_type+".html?order_no="+order_no);
};myApp.onPageInit('order-pay-success-page', function(page) {

	var orderNo = page.query.order_no;
	
	var orderType = page.query.order_type;
	
	
	if (orderType == 0) {
		$$("#blackContent").text("订单支付成功!")
		$$("#grayContent").text("我们正在为您安排服务人员");
	}
	
	if(orderType == 1){
		$$("#blackContent").text("订单支付成功!")
		$$("#grayContent").text("服务人员即将为您服务");
	}
	
	if(orderType == 2){
		$$("#blackContent").text("订单支付成功!")
		$$("#grayContent").text("您的助理会按约定时间为您服务");
	}
	
	$$('#order-pay-success-btn').click(function(){

		var fromUrl = "";
    	if (orderType == 0) {
    		fromUrl =  "order/order-view-0.html";
    	}
    	
    	if (orderType == 1) {
    		fromUrl = "order/order-view-1.html";
    	}
    	
    	if (orderType == 2) {
    		fromUrl = "order/order-view-2.html";                                		
    	}
    	
		
		localStorage.setItem('u_order_no_param', orderNo);
		
		fromUrl+= "?order_no="+orderNo;
		
		mainView.router.loadPage(fromUrl);
	});
	
});


//TODO  不生效
// myApp.onPageBack('order-pay-success-page', function(page) {
//	
//	var orderNo = page.query.order_no;
//	
//	var orderType = page.query.order_type;
//	
//	
//	
//	localStorage.setItem('u_order_no_param', orderNo);
//	fromUrl+= "?order_no="+orderNo;
//	
//	mainView.router.back({url:fromUrl,ignoreCache:true,force:true});
//	
//	mainView.router.refreshPreviousPage();
//});

;myApp.onPageBeforeInit('order-rate-page', function(page) {
	
	var userId = localStorage.getItem("user_id");
	var orderId = page.query.order_id;
	// 附加服务 Id 的 字符串
	var tagIds = "";
	//点击图片变色效果。 有一定的局限性（需要是 .png 格式  、选中后的图片名需要是    xxx-2x.png 、 未选中的图片名需要是  xxx.png ）
	//准时到达
	$$("img[name=order_type_0_img]").on('click',function(e) {

		var selectValue = $$(this).attr("value");
		$$("img[name=order_type_0_img]").each(function(key, index) {
			if ($$(this).attr("value") == selectValue) {
				$$(this).attr("src", "img/icons/shi-x.png");
			} else {
				$$(this).attr("src", "img/icons/shi.png");
			}
		});
		$$("#rate_type_0").val(selectValue);
//		console.log($$("#rate_type_0").val())
	});
	//易容仪表整洁
	$$("img[name=order_type_1_img]").on('click',function(e) {

		var selectValue = $$(this).attr("value");
		$$("img[name=order_type_1_img]").each(function(key, index) {
			if ($$(this).attr("value") == selectValue) {
				$$(this).attr("src", "img/icons/shi-x.png");
			} else {
				$$(this).attr("src", "img/icons/shi.png");
			}
		});
		$$("#rate_type_1").val(selectValue);
//		console.log($$("#rate_type_1").val())
	});
	//细节到位
	$$("img[name=order_type_2_img]").on('click',function(e) {

		var selectValue = $$(this).attr("value");
		$$("img[name=order_type_2_img]").each(function(key, index) {
			if ($$(this).attr("value") == selectValue) {
				$$(this).attr("src", "img/icons/shi-x.png");
			} else {
				$$(this).attr("src", "img/icons/shi.png");
			}
		});
		$$("#rate_type_2").val(selectValue);
//		console.log($$("#rate_type_2").val())
	});
	
	//服务整体感观
	$$("img[name=order_type_3_img]").on('click',function(e) {

		var selectValue = $$(this).attr("value");
		console.log("selecrValue 的值"+selectValue);
		$$("img[name=order_type_3_img]").each(function(key, index) {
			var thisValue = $$(this).attr("value");
			if (thisValue == selectValue) {
				if(selectValue == 0 )
					$$(this).attr("src", "img/icons/henbang-x.png");
				if(selectValue == 1 )
					$$(this).attr("src", "img/icons/yiban-x.png");
				if(selectValue == 2 )
					$$(this).attr("src", "img/icons/jiaocha-x.png");				
			} 
			else {
				if(thisValue == 0 )
					$$(this).attr("src", "img/icons/henbang.png");
				if(thisValue == 1 )
					$$(this).attr("src", "img/icons/yiban.png");
				if(thisValue == 2 )
					$$(this).attr("src", "img/icons/jiaocha.png");
			}
		});
		$$("#rate_type_3").val(selectValue);
//		console.log($$("#rate_type_3").val())
		
	});

	
	//整体服务
	
	$$("img[name=order_type_4_img]").on('click',function(e) {

		var selectValue = $$(this).attr("value");
		console.log("selecrValue 的值"+selectValue);
		$$("img[name=order_type_4_img]").each(function(key, index) {
			var thisValue = $$(this).attr("value");
			if (thisValue == selectValue) {
				if(selectValue == 0 )
					$$(this).attr("src", "img/icons/henbang-x.png");
				if(selectValue == 1 )
					$$(this).attr("src", "img/icons/yiban-x.png");
				if(selectValue == 2 )
					$$(this).attr("src", "img/icons/jiaocha-x.png");				
			} 
			else {
				if(thisValue == 0 )
					$$(this).attr("src", "img/icons/henbang.png");
				if(thisValue == 1 )
					$$(this).attr("src", "img/icons/yiban.png");
				if(thisValue == 2 )
					$$(this).attr("src", "img/icons/jiaocha.png");
			}
		});
		$$("#rate_type_4").val(selectValue);
//		console.log($$("#rate_type_4").val())
		
	});
	
	var  saveOrderSuccess = function(data, textStatus, jqXHR) {
		myApp.hideIndicator();
		var results = JSON.parse(data.response);
		//var results = result.appResultData;
		if (results.status == "999") {
			myApp.alert(results.msg);
			$$("#pingjia-submit").attr("disabled",true);
			return;
		}
		if (results.status == "0") {
			myApp.alert("订单评价完成");
			mainView.router.loadPage("order/order-hour-now-list.html");
		}
	} 
	//提交平价
	$$("#pingjia-submit").on("click", function() {
		
		var postdata = {};
		var rate_type_0 = $$("#rate_type_0").val();
		var rate_type_1 = $$("#rate_type_1").val();
		var rate_type_2 = $$("#rate_type_2").val();
		var rate_type_3 = $$("#rate_type_3").val();
		var rate_type_4 = $$("#rate_type_4").val();
		var rateJson = [{"rateType":0, "rateValue": rate_type_0},
		                {"rateType":1, "rateValue": rate_type_1},
		                {"rateType":2, "rateValue": rate_type_2},
		                {"rateType":3, "rateValue": rate_type_3},
		                {"rateType":4, "rateValue": rate_type_4}]
		
		postdata.user_id = userId;
		postdata.order_id = orderId;
		
		postdata.rate_content = $$("#rateContents").val();
		postdata.rate_datas = JSON.stringify(rateJson);
		console.log(postdata);
		//return;
		
		$$.ajax({
			type : "POST",
			url : siteAPIPath+ "order/post_rate.json",
			dataType : "json",
			cache : false,
			data : postdata,
			statusCode : {
				200 : saveOrderSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
		});
	})
	

});





;myApp.onPageInit('order-hour-view-0-page', function (page) {
	
	var userId = localStorage['user_id'];
	
	var orderNo = page.query.order_no;
		
//	var orderId = 0; // 定义全局变量 orderId
	
	
	
	// 订单状态 
    var orderStatus = $$("#orderStatus").val();
	
	if(orderStatus == 3){
		$$("#u1  li:nth-child(odd)").css("background-color","#EF7C00");
	}
	if(orderStatus == 4){
		$$("#u1  li:nth-child(odd)").css("background-color","#EF7C00");
		$$("#u2  li:nth-child(odd)").css("background-color","#EF7C00");
	}
	if(orderStatus == 5){
		$$("#u1  li:nth-child(odd)").css("background-color","#EF7C00");
		$$("#u2  li:nth-child(odd)").css("background-color","#EF7C00");
		$$("#u3  li:nth-child(odd)").css("background-color","#EF7C00");
	}
	if(orderStatus == 6){
		$$("#u1  li:nth-child(odd)").css("background-color","#EF7C00");	
		$$("#u2  li:nth-child(odd)").css("background-color","#EF7C00");
		$$("#u3  li:nth-child(odd)").css("background-color","#EF7C00");
	}
	if(orderStatus == 7){
		$$("#u1  li:nth-child(odd)").css("background-color","#EF7C00");
		$$("#u2  li:nth-child(odd)").css("background-color","#EF7C00");
		$$("#u3  li:nth-child(odd)").css("background-color","#EF7C00");
		$$("#u4  li:nth-child(odd)").css("background-color","#EF7C00");
		$$("#u5  li:nth-child(odd)").css("background-color","#EF7C00");
	}
	
	// 有 支付选项时，所需字段
	var order_type = $$("#orderType").val();
	var order_money = delSomeString($$("#orderMoney").text());
	
	
	//根据状态是否显示支付. (只有待支付 状态的 订单。显示 支付 选项)
	if(orderStatus == 3){
		$$("#user_coupon_view_li").hide();  // 优惠券选项
		$$("#order_pay_view_li").hide();	// 实际金额
		
		$$("#hour-pay-submit").show();		//支付按钮
		
		//支付信息展现
		$$(".hour-pay-coupon-select").show();		//优惠券选择
		$$(".hour-pay-type-choose").show();			//支付类型选择
		
		//点击选择优惠劵
		$$("#selectCoupon").click(function() {
			var linkUrl = "user/coupon/mine-coupon-list.html";
			linkUrl+= "?order_type="+order_type;
			linkUrl+= "&order_money="+order_money;
			mainView.router.loadPage(linkUrl);
		});
		
		//用户返回优惠劵之后的处理
		var userCouponId = page.query.user_coupon_id;
		var userCouponName = page.query.user_coupon_name;
		var userCouponValue = page.query.user_coupon_value;
		
		if (userCouponId != undefined && userCouponName != undefined) {
			$$("#user_coupon_id").val(userCouponId);
			$$("#couponSelect").html("为您节省了"+userCouponValue+"元");
			if (userCouponValue == undefined) userCouponValue = 0;
			
			var realMoney = parseFloat(order_money) - parseFloat(userCouponValue);
			if(realMoney < 0){
				realMoney = 0;
			}
			$$("#real-order-money-label").html('￥'+realMoney);
		}

		}else{
			
			// 除 待支付状态之外，只展示  优惠券、实际金额 相关数值
			$$("#hour-pay-submit").hide();			//支付 按钮
			$$(".hour-pay-coupon-select").hide();   // 优惠券选择  列表
			$$(".hour-pay-type-choose").hide();		// 支付类型选择  
			$$("#user_coupon_view_li").show();	   // 优惠券
			$$("#order_pay_view_li").show();       // 实际支付金额
		}

		// 对于钟点工 订单，只有  待支付，已支付 两种 状态   ，，显示取消订单  按钮 
		if(orderStatus == 3 ||  orderStatus == 4){
			$$("#cancleOrder").show();
		}else{
			$$("#cancleOrder").hide();
		}
		
		//待评价订单，显示 评价按钮
		if(orderStatus == 6){
			$$("#rate").show();
		}else{
			$$("#rate").hide();
		}
		
		//已评价 ，显示 查看评价 按钮
		if(orderStatus == 7){
			$$("#getRate").show();
		}else{
			$$("#getRate").hide();
		}
	
	
	
	var postHourOrderPaySuccess =function(data, textStatus, jqXHR) {
		
		$$("#hour-pay-submit").removeAttr('disabled');
		
	 	var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			
			// 重复支付或者订单不存在等情况，禁用支付按钮
			$$(".zf-hour-submit").attr("disabled", true);
			
			return;
		}
		//orderPayType = result.data.pay_type;
		//如果为余额支付，则直接跳到完成页面
		if (orderPayType == 0) {
			mainView.router.loadPage("order/order-pay-success.html?order_no="+orderNo+"&order_type=0");
		}
		
		
		var orderId = $$("#orderId").val();
		
		//如果为微信支付，则需要跳转到微信支付页面.
		if (orderPayType == 2) {
			 var userCouponId = $$("#user_coupon_id").val();
			 if (userCouponId == undefined) userCouponId = 0;
			 var wxPayUrl = localUrl + "/" + appName + "/wx-pay-pre.jsp";
			 wxPayUrl +="?orderId="+orderId;
			 wxPayUrl +="&userCouponId="+userCouponId;
			 wxPayUrl +="&orderType=1";
			 wxPayUrl +="&payOrderType=0";
			 location.href = wxPayUrl;
		}
		
	};	
	
	//选择支付类型
	$$('label[name=myPayTypeSelect]').on('click',function(){
		
		var nowValue = $$(this).find("input").val();
		
		//单选
		$$('img[name="order_pay_type"]').each(function(index,value){
			if($$(this).prev().val() == nowValue){
				$$(this).attr("src","img/icons/duigou-green.png");
			}else{
				$$(this).attr("src","img/icons/duigou-grey.png");
			}
			
		});

		
	});
	
	
	
	//点击支付的处理
	$$("#hour-pay-submit").click(function(){
		
		$$("#hour-pay-submit").attr("disabled", true);

		var userCouponId = $$("#user_coupon_id").val();
		if (userCouponId == undefined) userCouponId = 0;
		var postdata = {};
		postdata.user_id = userId;
		postdata.order_no = orderNo;
		postdata.user_coupon_id = userCouponId;
				
//		$$('input[type="radio"]').each(function(key,index) {
//			 if(this.checked) {
//				 orderPayType = this.value;
//			 }
//		 });
		
		$$('img[name="order_pay_type"]').each(function(index,value){
			if($$(this).attr("src").indexOf("green")>0){
				orderPayType = $$(this).prev().val();
			}
			
		});
		
		postdata.order_pay_type = orderPayType;
		
		$$.ajax({
			type: "post",
			 url: siteAPIPath + "order/post_pay.json",
			data: postdata,
			statusCode: {
	         	200: postHourOrderPaySuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});		
		 
	});			
	
	// 点击 评价 操作
	$$("#rate").on('click',function(){
		
		if(orderStatus == 7){
			myApp.alert("您已经评价过啦");
			return false;
		}
		
		var orderId = $$("#orderId").val();
		mainView.router.loadPage("order/order-rate.html?order_id="+orderId);
		
		
	});
	
	//点击 查看评价 操作
	$$("#getRate").on('click',function(){
		var orderId = $$("#orderId").val();
		mainView.router.loadPage("order/order-get-rate.html?order_id="+orderId);
	});
	
	
	//取消订单操作
	$$("#cancleOrder").on('click',function(){
		
//		var orderNo = localStorage['u_order_no_param'];
		
		var dataCancle = {};
		dataCancle.order_no = orderNo;
		
		
		//根据服务时间，设置取消提示
		var text = "";
		
		// 服务时间  unix时间戳
		var aaa =   $$("#serviceDate").text();
		var bb =  (new Date(aaa).getTime()/1000).toString();
		
		
		//当前时间 unix时间戳
		var now = Math.round(new Date().getTime()/1000);
		
		//时间差
		var diffValue = Number(bb) - Number(now);
		
		//服务开始前两小时之内
		if(diffValue <= 7200 && diffValue >0){
			text = "服务已经快开始了，现在取消订单会扣除全部费用哦，确定取消吗?";
		}else{
			//逻辑上，服务时间内，还未支付的订单，已经变为 已关闭了
			text = "现在取消订单，订单金额会全部退到您的账号余额，确定取消订单吗?";
		}
		
		myApp.confirm(text,function(){
			$$.ajax({
				type:"post",
				url: siteAPIPath+"order/cancle_am_order.json",
				data : dataCancle,
				cache: true, 
				success: function(datas,status,xhr){
					 
					 var result = JSON.parse(datas);
					 if(result.status == 0){
						 myApp.alert("订单取消成功！");
						 mainView.router.loadPage("order/order-hour-now-list.html");
					 }
					 if(result.status == 999){
						 myApp.alert(result.msg);
						 
						 $$("#cancleOrder").attr("disabled",true);
						 return false;
					 }
				}
			});
		});
	});
});


myApp.template7Data['page:order-hour-view-0-page'] = function(){
    var result; 
    var postdata = {};
	var order_no = localStorage['u_order_no_param'];
//    var order_no = page.query.order_no;
	postdata.order_no = order_no;

   $$.ajax({
      type : "GET",
      url:siteAPIPath + "order/order_hour_detail.json",
      dataType: "json",
      data : postdata,
      cache : true,
      async : false,	// 不能是异步
      success: function(data){
		  console.log(data);
	      result = data.data;
          
          //设置时间显示格式
          var timestamp = moment.unix(result.service_date);
		  var startTime = timestamp.format('YYYY-MM-DD HH:mm');
		  result.service_date = startTime;
		  
//		  var orderStatus = result.order_status;
		  
		 
      }	
			  
   })
  
  return result;
}


;myApp.onPageBeforeInit('order-hour-view-2-page', function (page) {
	

	var userId = localStorage['user_id'];
	var order_no = page.query.order_no;
	var orderNo = page.query.order_no;
	var orderId = 0;
	
	
	$$("#selectCoupon").click(function() {
		var orderType =$$("#order_type").val();
		var orderNo = $$("#order_no").val();
		var orderMoney = $$("#order_money").val();
		var linkUrl = "user/coupon/mine-coupon-list.html";
		linkUrl+= "?order_type="+orderType;
		linkUrl+= "&order_money="+orderMoney;
		mainView.router.loadPage(linkUrl);
	});	
	
	//用户返回优惠劵之后的处理
	var userCouponId = page.query.user_coupon_id;
	//var userCouponName = page.query.user_coupon_name;
	var userCouponValue = page.query.user_coupon_value;
	
	if (userCouponId != undefined && userCouponValue != undefined) {
		$$("#user_coupon_id").val(userCouponId);
		$$("#user_coupon_name").html("为您节省了"+userCouponValue+"元");
		if (userCouponValue == undefined) userCouponValue = 0;
		var orderMoney = $$("#order_money").val();
		$$("#real-am-order-money-label").html(parseFloat(orderMoney) - parseFloat(userCouponValue) + "元");
	}
		
	var postAmOrderPaySuccess =function(data, textStatus, jqXHR) {
		$$("#am-pay-submit").removeAttr('disabled');
	 	var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}
		
		var order = result.data;
		console.log(order+"~~~~~~~~~~~~~");
		var orderId = order.id;
		//orderPayType = result.data.pay_type;
		//如果为余额支付，则直接跳到完成页面
		if (orderPayType == 0) {
			mainView.router.loadPage("order/order-pay-success.html?order_no="+orderNo+"&order_type=2");
		}
		
		//如果为微信支付，则需要跳转到微信支付页面.
		if (orderPayType == 2) {
			var userCouponId = $$("#user_coupon_id").val();
			if (userCouponId == undefined) userCouponId = 0;
			
			 var wxPayUrl = localUrl + "/" + appName + "/wx-pay-pre.jsp";
			 wxPayUrl +="?orderId="+orderId;
			 wxPayUrl +="&userCouponId="+userCouponId;
			 wxPayUrl +="&orderType=2";
			 wxPayUrl +="&payOrderType=0";
			 location.href = wxPayUrl;
		}
	};	
	
	//选择支付类型
$$('label[name=myPayTypeSelect]').on('click',function(){
		
		var nowValue = $$(this).find("input").val();
		
		//单选
		$$('img[name="order_pay_type"]').each(function(index,value){
			if($$(this).prev().val() == nowValue){
				$$(this).attr("src","img/icons/duigou-green.png");
			}else{
				$$(this).attr("src","img/icons/duigou-grey.png");
			}
			
		});

		
	});
	
	
	//点击支付的处理
	$$(".am-pay-submit").click(function(){
		
		
		$$("#am-pay-submit").attr("disabled", true);
		var userCouponId = $$("#user_coupon_id").val();
		if (userCouponId == undefined) userCouponId = 0;
		
		var postdata = {};
		postdata.user_id = userId;
		postdata.order_no = orderNo;
		postdata.user_coupon_id = userCouponId;
		
		
		//获得选中的支付方式
		$$('img[name="order_pay_type"]').each(function(index,value){
			if($$(this).attr("src").indexOf("green")>0){
				orderPayType = $$(this).prev().val();
			}
			
		});
		
		postdata.order_pay_type = orderPayType;
		
		$$.ajax({
			type: "post",
			 url: siteAPIPath + "order/post_pay.json",
			data: postdata,
			statusCode: {
	         	200: postAmOrderPaySuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});		
		 
	});		
	
    //评价订单
	$$("#yonghu-pingjiadingdan").on("click",function(){
		/*var orderHourViewVo = result.data;
		orderId = orderHourViewVo.id;*/
 		mainView.router.loadPage("order/order-rate.html?order_id="+orderId);

 		
 	});
	
	//查看订单平价
	$$("#yonghu-pingjiaxiangqing").on("click",function(){
	
 		mainView.router.loadPage("order/order-get-rate.html?order_id="+orderId);

 		
 	});
});

//列表显示
myApp.template7Data['page:order-hour-view-2-page'] = function(){
  var result; 
  var userId = localStorage['user_id'];
  var orderNo = localStorage['u_order_no_param'];
  var postData = {};
  postData.order_no = orderNo;
   $$.ajax({
          type : "GET",
          url  : siteAPIPath+"order/order_hour_paotui_detail.json",
          dataType: "json",
          data : postData,
          cache : true,
          async : false,
          success: function(data){
        	  console.log(data);
              result = data.data;
          }
  })
  
  return result;
};myApp.onPageBeforeInit('order-view-page', function(page) {

	var userId = page.query.user_id;
	
	var orderNo = page.query.order_no;
	
	var orderId = 0;
	
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	
	var secId = localStorage['sec_id'];
		
	var orderInfoSuccess = function(data, textStatus, jqXHR) {
		
		// We have received response and can hide activity indicator
		myApp.hideIndicator();
		var result = JSON.parse(data.response);
		var order = result.data;
//		console.log(order);
		
		var timestamp = moment.unix(order.add_time);
		var addTimeStr = timestamp.format('YYYY-MM-DD HH:mm:ss');		
		$$("#add_time").text(addTimeStr);
		$$("#order_status").text(order.order_status_name);
		if (order.order_pay_type == 0) {
			$$("#order_pay_type").text("无需支付");
						
		} else {
			$$("#order_pay_type").text("在线支付");
		}
		
		$$("#order_id").val(order.id);
		$$("#order_no").text(order.order_no);
		$$("#name-span").text(order.name);
		$$("#mobile").text(order.mobile);
		$$("#service_type").text(order.service_type_name);
		$$("#service_content").text(order.service_content);

		
		$$("#order_money").text(order.order_money);
		
		if (order.order_status == 1 || order.order_status == 3) {
			$$("#order-modify").css('display','block'); 
			$$("#order-push").css('display','block'); 
		}

	};
	
	var postdata = {};

	postdata.user_id = userId;
	postdata.order_no = orderNo;
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "order/get_detail.json",
		dataType : "json",
		cache : true,
		data : postdata,
		statusCode : {
			200 : orderInfoSuccess,
			400 : ajaxError,
			500 : ajaxError
		},
		success : function() {

		}
	});		

	$$('#order-modify').on('click', function() {
		mainView.router.loadPage("order/order-form.html?user_id="+userId+"&order_no="+ orderNo);
	});
	
	$$('#order-push').on('click', function() {
		
		var orderPushSuccess = function(data, textStatus, jqXHR) {
			// We have received response and can hide activity indicator
			myApp.hideIndicator();
//			console.log("push success");
			var result = JSON.parse(data.response);

//			console.log(result);
			if (result.status == "999") {
				myApp.alert(result.msg);
				return;
			}
			
			if (result.status == "0") {
				myApp.alert("订单推送已完成");
				
			}

		};				
		
		var postdata = {};
		var orderId = $$("#order_id").val();
		postdata.user_id = userId;
		postdata.order_id = orderId;
		
		$$.ajax({
			cache : true,
			type : "POST",
			url : siteAPIPath + "order/push_order.json",
			data : postdata,
			statusCode : {
				200 : orderPushSuccess,
				400 : ajaxError,
				500 : ajaxError
			},
		});		
	});	

});;myApp.onPageBeforeInit('order-am-result-page', function (page) {

	var orderNo =  page.query.order_no;
	//查看订单
	$$("#order-am-result").on("click", function() {
	
		localStorage.setItem('u_order_no_param', orderNo);
		//调整订单跳转
		mainView.router.loadPage("order/order-view-2.html?order_no="+orderNo);

	})
	
});

;myApp.onPageBeforeInit('order-list-shendubaojie-result', function (page) {

	//获取深度保洁订单号
	var orderNo = page.query.order_no;
	//to深度保洁详情页面
	$$("#to-shendu-order-detail").on("click", function() {
		mainView.router.loadPage("order/order-view-1.html?order_no="+orderNo);
		localStorage.setItem('u_order_no_param',orderNo);
	});
});
	



;myApp.onPageInit('order-list-shendubaojie-yuyue', function(page) {

	var addrId = page.query.addr_id;
	var addrName = page.query.addr_name;
	var userId = localStorage.getItem("user_id");
	if (addrId != undefined) {
		$$("#addr_ids").val(addrId);
	}

	if (addrName != undefined) {
		$$("#addrName").html(addrName);
	}
	var temp = sessionStorage.getItem('exp_clean_remarks');
	if (temp != null && temp != undefined) {
		$$("#exp_clean_remarks")
				.text(sessionStorage.getItem('exp_clean_remarks'));
	} else {
		$$("#exp_clean_remarks")
				.text(sessionStorage.getItem('exp_clean_remarks'));
	}
	/*
	 * 时间选择插件, 已包含 加载 时，赋给一个 时间 默认值
	 */
	dateSelect();

	/*
	 * 回显 日期时间、选择的价格
	 */
	var dateTwo = sessionStorage.getItem("service_date");
	if (dateTwo != null) {
		$$("#serviceDate").val(dateTwo);
	}
	
	var tagIds = localStorage.getItem('tagIds');
	var tagIdsArr = tagIds.split(",");
	var orderPrice = $$("#order_price").val();
	orderPrice = Number(0);
	var itemNum = Number(0);
	for(var i = 0; i< tagIdsArr.length; i++) {
		 itemPrice = getServiceAddonPrice(tagIdsArr[i]);
		 itemNum =  getServiceAddonDefaultNum(tagIdsArr[i]);
		 if (itemNum != undefined && itemPrice != undefined ) {
			 orderPrice = Number(orderPrice) + Number(itemNum) * Number(itemPrice);
		 }
	}
	$$("#order_price").val(orderPrice);
	/**
	 * 深度保洁订单提交预约
	 */
	$$("#order-exp-clean-submit").on(
			"click",
			function() {
				$$("#order-exp-clean-submit").attr("disabled", true);
				sessionStorage.setItem('exp_clean_remarks', '')
				var tagIds = localStorage.getItem('tagIds');
				var tagIdsArr = tagIds.split(",");
				// 表单验证
				if (orderFormValidation() == false) {
					return false;
				}
				// 拼接json字符串
				var jsonData = "";
				for (var i = 0; i < tagIdsArr.length; i++) {
					var serviceAddonId = tagIdsArr[i];
					var itemNum = getServiceAddonDefaultNum(tagIdsArr[i]);
					;
					jsonData += "{";
					jsonData += "serviceAddonId" + ":" + serviceAddonId + ","
							+ "itemNum" + ":" + itemNum + ",";
					jsonData = jsonData.substring(0, jsonData.length - 1)
							+ "},";
				}
				;
				var index = jsonData.lastIndexOf(",");
				jsonData = "[" + jsonData.substring(0, index) + "]";
				var formData = myApp.formToJSON('#order-exp-clean-form');
				formData.service_date = moment(formData.service_date + ":00",
						"yyyy-MM-DD HH:mm:ss").unix();
				formData.service_addons_datas = jsonData;// "[{'serviceAddonId':'3','itemNum':'2'}]"
				formData.user_id = userId;
				formData.service_type = "2";
				$$.ajax({
					type : "POST",
					url : siteAPIPath + "order/post_exp_clean_order.json",
					dataType : "json",
					async : false,
					cache : false,
					data : formData,
					statusCode : {
						200 : saveExpCleanOrderSuccess,
						400 : ajaxError,
						500 : ajaxError
					}
				});
			});

	$$("#addrSelect").on('click', function() {
		// 保存已选择的 服务时间
		sessionStorage.setItem('service_date', $$("#serviceDate").val());
		// 保存备注
		var remarks = $$("#exp_clean_remarks").val();
		var formData = myApp.formToJSON('#order-exp-clean-form');
		if (remarks != '' && remarks != undefined) {
			formData.remarks = remarks;
			sessionStorage.setItem('exp_clean_remarks', remarks);
		} else {
			formData.remarks = '';
			sessionStorage.setItem('exp_clean_remarks', '');
		}
	});

});
// 列表显示深度保洁项
myApp.template7Data['page:order-list-shendubaojie-yuyue'] = function() {
	var result = {};
	// 获取参数（附加服务service_addon_id）
	var tagIds = localStorage.getItem('tagIds');
	var tagIdsArr = tagIds.split(",");
	// 获取附加服务列表
	var servcieTypeAddonsList = JSON.parse(localStorage
			.getItem("service_type_addons_list"));
	var userId = localStorage.getItem("user_id");

	/**
	 * 根据选中的附加服务，显示对应的列表信息
	 */
	var list = [];
	for (var i = 0; i < tagIdsArr.length; i++) {
		var tagId = tagIdsArr[i];
		var name = "";
		var itemUnit = "";
		var price = ''
		var serviceAddons = {};
		// 获取对应的附加服务单价和量词
		$$.each(servcieTypeAddonsList, function(i, item) {
			if (item.service_addon_id == tagId) {
				name = item.name;
				itemUnit = item.item_unit;
				defaultNum = item.default_num;
				price = item.price;
			}
		});
		serviceAddons.name = name;
		serviceAddons.item_unit = itemUnit;
		serviceAddons.default_num = defaultNum;
		serviceAddons.price = price;
		list.push(serviceAddons);
	}
	result.list = list;
	return result;
}
// 提交深度保洁预约单后跳转
function saveExpCleanOrderSuccess(data, textStatus, jqXHR) {
	$$("#order-exp-clean-submit").removeAttr('disabled');
	myApp.hideIndicator();
	var results = JSON.parse(data.response);
	if (results.status == "999") {
		myApp.alert(results.msg);
		return;
	}
	if (results.status == "0") {
		var orderNo = results.data.order_no;
		var userId = localStorage.getItem("user_id");
		mainView.router
				.loadPage("order/order-list-shendubaojie-result.html?order_no="
						+ orderNo);
		localStorage.setItem('u_order_no_param', orderNo);
		sessionStorage.removeItem("service_date");
	}
}
// 表单验证
function orderFormValidation() {
	var formData = myApp.formToJSON('#order-exp-clean-form');

	if (formData.service_date == "") {
		myApp.alert("请输入服务时间");
	}
	// 当前选择 的 日期
	var dateSelect = formData.service_date;

	// 当前 选择的 日期 中 的 '整点小时' 数，如：08,10,19...
	var hourSelect = moment(dateSelect).format('HH');

	/*
	 * 如果 是 默认 的时间。则需要判断 6点之前，不行
	 */

	if (Number(hourSelect) < 6) {
		alert("现在无法下单，点击选择合适 的时间");
		$$("#order-exp-clean-submit").removeAttr('disabled');
		return false;
	}

	if (formData.addr_id == "") {
		myApp.alert("请输入服务地址");
		$$("#order-exp-clean-submit").removeAttr('disabled');
		return false;
	}
	return true;
}
;myApp.onPageBeforeInit('order-list-shendubaojiezl-page', function(page) {
	
	// 附加服务 Id 的 字符串
	var tagIds = "";
	//点击图片变色效果。 有一定的局限性（需要是 .png 格式  、选中后的图片名需要是    xxx-2x.png 、 未选中的图片名需要是  xxx.png ）
	$$(".shendu-fl-lb-every").on('click',function(e) {
		
		var arr =  $$(this).children().children().attr("src").split(".");
		//实现图片的切换
		if(arr[0].endWith("-x")){
			//这里貌似 不能 直接调  subString() [[ substring()!!!!..    ]]   ,RTrim()等方法。但是可以用 replace()
			$$(this).children().children().attr("src",arr[0].replace("-x","")+".png");
			$$(this).css("background","#F7F7F7");
			$$($$(this).children()).next().css("color","#999");
		}else{
			$$(this).children().children().attr("src",arr[0]+"-x"+".png");
			// $$(this).attr("background","#CACCFD");
			$$(this).css("background","#CACCFD");
			$$($$(this).children()).next().css("color","#EF7C00");
		}
		tagIds = $$("#serviceAddons").attr("value");
		
		//图片以'-x'结尾的就是被选中的，统计所用被选中的附加服务，获得其相应的Id
		$$("img[name = tag]").each(function(key, index) {
			if ($$(this).attr("src").indexOf("-x")>0) {
				tagIds = tagIds + $$(this).attr("id") + ",";
			}
		});	
		if (tagIds != "") {
			tagIds = tagIds.substring(0, tagIds.length - 1);
		}
	});
	
	$$("#shendu-to-form").click(function(){
		if(tagIds !='' && tagIds.length>0){
			mainView.router.loadPage("order/order-list-shendubaojie-yuyue.html");
			localStorage.setItem("tagIds",tagIds);
		}else{
			myApp.alert("请选择服务种类");
			return ;
		}
	});
});
;myApp.onPageBeforeInit('order-list-shendubaojie-tijiao', function (page) {

	var orderNo = page.query.order_no;
	var userId = localStorage['user_id'];
	var orderId = 0;
	var orderPayType = 0;
	var postData = {};
	postData.order_no = orderNo;
	
	
	
	$$("#selectCoupon").click(function() {
		var orderType =$$("#order_type").val();
		var orderNo = $$("#order_no").val();
		var orderMoney = $$("#order_money").val();
		var linkUrl = "user/coupon/mine-coupon-list.html";
		linkUrl+= "?order_type="+orderType;
		linkUrl+= "&order_money="+orderMoney;
		mainView.router.loadPage(linkUrl);
	});
	
	  var orderStatus = $$("#order_status_span").val();
	  if(orderStatus==4){
	   		$$("#user_coupon_view1_li").show();  
			$$("#order_pay_view1_li").show();
	  }else{
		 $$("#user_coupon_view1_li").hide();  
		 $$("#order_pay_view1_li").hide(); 
	  }
	
	//用户返回优惠劵之后的处理
	var userCouponId = page.query.user_coupon_id;
	//var userCouponName = page.query.user_coupon_name;
	var userCouponValue = page.query.user_coupon_value;
	
	if (userCouponId != undefined && userCouponValue != undefined) {
		$$("#user_coupon_id").val(userCouponId);
		$$("#user_coupon_name").html("为您节省了"+userCouponValue+"元");
		if (userCouponValue == undefined) userCouponValue = 0;
		var orderMoney = $$("#order_money").val();
		var realMoney = parseFloat(orderMoney) - parseFloat(userCouponValue);
		if(realMoney <0){
			realMoney = 0;
		}
		$$("#real-order-money-label-exp-clean").html("￥"+realMoney);
	}
	
	 //评价订单
	$$("#exp-clean-order-pingjia").on("click",function(){
 		mainView.router.loadPage("order/order-rate.html?order_id="+orderId);
 	});
	//查看订单平价
	$$("#exp-clean-pingjiaxiangqing").on("click",function(){
 		mainView.router.loadPage("order/order-get-rate.html?order_id="+orderId);
 	});
	
	$$('label[name=myPayTypeSelects]').on('click',function(){
		
		var nowValue = $$(this).find("input").val();
		//单选
		$$('img[name="order_pay_types"]').each(function(index,value){
			if($$(this).prev().val() == nowValue){
				$$(this).attr("src","img/icons/duigou-green.png");
			}else{
				$$(this).attr("src","img/icons/duigou-grey.png");
			}
			
		});

		
	});
	
	//取消订单操作
	$$("#cancle_exp_clean_order_button").on('click',function(){

		myApp.confirm('您真的要取消订单么?',function(){
			$$.ajax({
				type:"post",
				url: siteAPIPath+"order/cancle_am_order.json",
				data : postData,
				cache: true, 
				success: function(datas,status,xhr){
					 
					 var result = JSON.parse(datas);
					 if(result.status == 0){
						 myApp.alert("订单取消成功！");
						 mainView.router.loadPage("order/order-hour-now-list.html");
						
						 setTimeout(function () {
							 myApp.hidePreloader();
						 }, 1000);
					 }
					 if(result.status == 999){
						 myApp.showPreloader("订单不存在！");
						 return false;
					 }
				}
			});
		});
	});	

	var postCleanOrderPaySuccess =function(data, textStatus, jqXHR) {
		$$("#shendu-submit").removeAttr('disabled');
	 	var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}
		var order = result.data;
		var orderId = order.id;
		//orderPayType = result.data.pay_type;
		//如果为余额支付，则直接跳到完成页面
		if (orderPayType == 0) {
			mainView.router.loadPage("order/order-pay-success.html?order_no="+orderNo+"&order_type=1");
		}
		
		//如果为微信支付，则需要跳转到微信支付页面.
		if (orderPayType == 2) {
			 var userCouponId = $$("#user_coupon_id").val();
			 if (userCouponId == undefined) userCouponId = 0;
			 
			
			 var wxPayUrl = localUrl + "/" + appName + "/wx-pay-pre.jsp";
			 wxPayUrl +="?orderId="+orderId;
			 wxPayUrl +="&userCouponId="+userCouponId;
			 wxPayUrl +="&orderType=1";
			 wxPayUrl +="&payOrderType=0";
			 location.href = wxPayUrl;
		}
	};	
	
	//点击支付的处理
	$$(".shendu-submit").click(function(){
		
		
		$$("#shendu-submit").attr("disabled", true);
		var userCouponId = $$("#user_coupon_id").val();
		if (userCouponId == undefined) userCouponId = 0;
		
		var postdata = {};
	
		postdata.user_id = userId;
		postdata.order_no = orderNo;
		postdata.user_coupon_id = userCouponId;
		
		//获得选中的支付方式
		$$('img[name="order_pay_types"]').each(function(index,value){
			if($$(this).attr("src").indexOf("green")>0){
				orderPayType = $$(this).prev().val();
			}
			
		});
		postdata.order_pay_type = orderPayType;
		
		$$.ajax({
			type: "post",
			 url: siteAPIPath + "order/post_pay.json",
			data: postdata,
			statusCode: {
	         	200: postCleanOrderPaySuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});		
		 
	});		
});


//列表显示深度保洁项
myApp.template7Data['page:order-list-shendubaojie-tijiao'] = function(){
  var result; 
  var userId = localStorage['user_id'];
  var orderNo = localStorage['u_order_no_param'];
  
   $$.ajax({
          type : "GET",
          url :siteAPIPath+"order/get_exp_clean_order_detail.json?order_no="+orderNo+"&user_id="+userId,
          dataType: "json",
          cache : true,
          async : false,
          success: function(data){
        	  console.log(data);
              result = data.data;
          }
  })
  
  return result;
}
;//获取活动的详情
myApp.template7Data['page:huodong-detail'] = function(){
  var result;
  var userId = localStorage.getItem("user_id");
  if(userId ==null || userId== undefined){
 	 	mainView.router.loadPage("login.html");
 	 	return;
	}
  	var socialsId =localStorage.getItem("socials_id");
  	if(socialsId ==null || socialsId== undefined){
	 	socialsId =0;
	}
	var formData={};
	formData.id=socialsId;
	formData.user_id=userId;
$$.ajax({
          type : "GET",
          url  : siteAPIPath+"dict/get_socials_detail.json",
          data : formData,
          dataType: "json",
          cache : true,
          async : false,
          success: function(data){
              result = data.data;
          }
  })
  return result;
}
myApp.onPageInit('huodong-detail', function (page) {
	var userId = localStorage.getItem("user_id");;
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	var socialsId =localStorage.getItem("socials_id");
  	if(socialsId ==null || socialsId== undefined){
	 	socialsId =0;
	}
  	var saveSubmitSuccess = function(data, textStatus, jqXHR){
  		myApp.hideIndicator();
  	   	var result = JSON.parse(data.response);
  		}
  	var formData ={};
  	formData.user_id=userId;
  	formData.socials_id = socialsId;
     $$(".save_submit").on("click",function(){
    	 $$.ajax({
             type : "POST",
             url  : siteAPIPath+"dict/post_socials_call.json",
             data : formData,
             dataType: "json",
             cache : true,
             async : false,
             statusCode: {
              	200: saveSubmitSuccess,
      	    	400: ajaxError,
      	    	500: ajaxError
      	    },
    	 })
     });
});;//列表显示用户充值卡
myApp.template7Data['page:huodong-list'] = function(){
  var result;
  var userId = localStorage.getItem("user_id");
  if(userId ==null || userId== undefined){
 	 	mainView.router.loadPage("login.html");
 	 	return;
	}
  $$.ajax({
          type : "GET",
          url  : siteAPIPath+"dict/get_socilas.json",
          dataType: "json",
          cache : true,
          async : false,
          success: function(data){
              result = data;
          }
  })
  return result;
}
function jumpDetail(id){
	localStorage.setItem("socials_id",id);
	mainView.router.loadPage("huodong-detail.html");
}
;
myApp.onPageBeforeInit('mine-add-addrs', function(page) {

	var userId = localStorage['user_id'];
	
	addrId = page.query.addr_id;
	
	var returnUrl = page.query.return_url;
	$$("#mine-addr-save").removeAttr('disabled');

	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}

	if (addrId == undefined || addrId == '') {
		addrId = 0;
	}
	
	if (returnUrl == undefined || returnUrl == '') {
		returnUrl = "user/mine-addr-list.html";
	}

	$$("#user_id").val(userId);
	$$("#addr_id").val(addrId);

	var addrSuccess = function(data, textStatus, jqXHR) {
		myApp.hideIndicator();
		$$("#mine-addr-save").removeAttr('disabled');
		var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}
		
		getAm();
		
		localStorage['am_mobile'] = result.data.amMobile;
		

		
		mainView.router.loadPage(returnUrl);
	}

	$$('#mine-addr-save').on('click', function() {
		console.log("mine-addr-save click");
		$$("#mine-addr-save").attr("disabled", true);
		if (addrFormValidation() == false) {
			$$("#mine-addr-save").removeAttr('disabled');
			return false;
		}

		var formData = myApp.formToJSON('#addr-form');

		$$.ajax({
			type : "POST",
			url : siteAPIPath + "user/post_user_addrs.json",
			dataType : "json",
			cache : false,
			data : formData,
			statusCode : {
				200 : addrSuccess,
				400 : ajaxError,
				500 : ajaxError
			},
		});
		

	});

});

//百度调用接口返回处理
function baiduAutoCompleteSuccess(data, textStatus, jqXHR) {
	var result = JSON.parse(data.response);
	var list = result.data;
	if (list.length > 0)
		$$('#addr-auto-list ul').html("");
	var html = $$('#autocomplete-list-part-li').html();
	var resultHtml = '';
	$$.each(list, function(i, item) {
		if (item.location != undefined) {
			var htmlPart = html;
			htmlPart = htmlPart.replace(new RegExp('{item_name}', "gm"),
					item.name);
			htmlPart = htmlPart.replace(new RegExp('{item_lat}', "gm"),
					item.location.lat);
			htmlPart = htmlPart.replace(new RegExp('{item_lng}', "gm"),
					item.location.lng);
			htmlPart = htmlPart.replace(new RegExp('{item_city}', "gm"),
					item.city);
			htmlPart = htmlPart.replace(new RegExp('{item_uid}', "gm"),
					item.uid);
			resultHtml += htmlPart;
		}
	});
	$$('#addr-auto-list ul').append(resultHtml);

}

//发起请求调用百度关键字提示接口
function getBaiduAutoComplete() {

	var paramData = {};
	paramData.query = $$("#name").val();
	paramData.region = "131";
	paramData.output = "json";
	paramData.ak = "2sshjv8D4AOoOzozoutVb6WT";

	$$.ajax({
		type : "GET",
		url : siteAPIPath + "map/autocomplete.json",
		dataType : 'json',
		cache : true,
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		data : paramData,
		statusCode : {
			200 : baiduAutoCompleteSuccess,
			400 : ajaxError,
			500 : ajaxError
		},
		success : function() {

		}
	});
}

//下拉列表选择后处理.
function selectAddr(objDiv) {
	var obj = objDiv.find('li');
	var latObj = obj.find('input[name=item_lat]');
	var lngObj = obj.find('input[name=item_lng]');
	var cityObj = obj.find('input[name=item_city]');
	var uidObj = obj.find('input[name=item_uid]');
	var nameObj = obj.find('div[class*=item-title]');
	var lng = lngObj.val();
	var lat = latObj.val();
	var city = cityObj.val();
	var name = nameObj.html();
	var uid = uidObj.val();

//		console.log("lng = " + lng);
//		console.log("lat = " + lat);
//		console.log("city = " + lng);
//		console.log("name = " + name);
//		console.log("uid = " + uid);

	$$("#name").val(name);
	$$("#longitude").val(lng);
	$$("#latitude").val(lat);
	$$("#city").val(city);
	$$("#uid").val(uid);

	$$('#addr-auto-list ul').html("");

}

function addrFormValidation() {
	var formData = myApp.formToJSON('#addr-form');
	if (formData.name == "") {
		myApp.alert("请输入小区名或大厦名，并在下拉列表中选择。");
		return false;
	}

	if (formData.longitude == "" || formData.latitude == "") {
		myApp.alert("请输入小区名或大厦名，并在下拉列表中选择。");
		return false;
	}

	if (formData.addr == "") {
		myApp.alert("请输入详细门牌号");
		return false;
	}

	return true;
}
;//获取用户地址列表
myApp.template7Data['page:mine-addr-list'] = function(){
  var result; 
  var userId = localStorage['user_id'];
  
  $$.ajax({
      type : "GET",
      url: siteAPIPath+"user/get_user_addrs.json?user_id="+userId,
      dataType: "json",
      cache : true,
      async : false,
      success: function(data){
    	  
          result = data;
      }
  })  
  
  return result;
}

myApp.onPageInit('mine-addr-list', function (page) {
	
	var userId = localStorage['user_id'];

    $$(".mine-add-addr-link").on("click",function(){
 		mainView.router.loadPage("user/mine-add-addr.html?addr_id=0");
 	});

    $$(document).on('click', '#selectAddrLink', function (e){
    	
    	e.stopImmediatePropagation(); //防止重复点击
    	
    	var addrId = $$(this).find("input[name=addr_id]").val();
    	var addrName = $$(this).find(".item-title").html();

    	
    	addrName = addrName.replace("[默认] ", "");
    	
    	var returnPage = "";
    	for (var i =1; i < 5; i++) {
    		var historyPage = mainView.history[mainView.history.length-i];
    		
    		if (historyPage == undefined) continue;
    		
    		if (historyPage.indexOf("order-form-zhongdiangong") >= 0 ||
    			historyPage.indexOf("order-list-shendubaojie-yuyue") >= 0
    			) {
    			returnPage = historyPage;
    			break;
    		}

    	}
    	
    	if (returnPage == "") return;
    	
    	returnPage = replaceParamVal(returnPage, "addr_id", addrId);
    	returnPage = replaceParamVal(returnPage, "addr_name", addrName);
    	
    	
    	if (returnPage.indexOf("?") <=0 && returnPage.indexOf("addr") <=0) {
    		returnPage = returnPage + "?addr_id="+addrId + "&addr_name="+ addrName;
    	} 
    	
    	if (returnPage.indexOf("&") <=0 && returnPage.indexOf("addr") <=0) {
    		returnPage = returnPage + "&addr_id="+addrId + "&addr_name="+ addrName;
    	}
    		

    	mainView.router.loadPage(returnPage);
 	});    
});


function setAddrDefault(addrId) {

	var userId = localStorage['user_id'];

	var paramData = {};
	paramData.user_id = userId;
	paramData.addr_id = addrId;
	$$.ajax({
		type : "POST",
		url : siteAPIPath + "user/post_set_addr_default.json",
        data:{"user_id":userId, "addr_id":addrId},
		success : function() {

			//设置当前的为默认的地址.
			$$(".swipeout").each(function(key, index) {

				var addrIdObj = $$(this).find('input[type=radio]');

				var addrNameObj = $$(this).find('.item-title');
				var addrNameHtml = addrNameObj.html();
				if (addrId == addrIdObj.val()) {
					if (addrNameHtml.indexOf("默认") < 0) {
						addrNameObj.html("[默认] " + addrNameHtml);
					} 
					
				} else {
					addrNameObj.html(addrNameHtml.replace("[默认] ", ""));
				}
				myApp.swipeoutClose($$(this));
			});
			
		}
	});	
	
};//列表显示用户充值卡
myApp.template7Data['page:mine-charge-list'] = function(){
  var result;
  var userId = localStorage.getItem("user_id");
  if(userId==null || userId == undefined){
	  userId = 0;
  }
  $$.ajax({
          type : "GET",
          url  : siteAPIPath+"/dict/get_cards.json?user_id="+userId,
          dataType: "json",
          cache : true,
          async : false,
          success: function(data){
              result = data;
          }
  })
  return result;
}
myApp.onPageInit('mine-charge-list', function (page) {
	var userId = localStorage.getItem("user_id");;
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
     $$(".charge-way").on("click",function(){
    	 var cardId = $$(this).prev().val();
    	 var cardPay = $$(this).next().val();
    	 if(cardPay < 10000){
        	 mainView.router.loadPage("user/charge/mine-charge-way.html?card_id="+cardId+"&card_pay="+cardPay);
    	 }
 	});
     $$(".feed_back_money").on("click",function(){
    	 mainView.router.loadPage("user/charge/mine-charge-fanquan.html");
     });
});

function chargeLargeValue(obj) {
	var amId = localStorage.getItem('am_id');
	var amMobile = localStorage.getItem('am_mobile');
	
	if (localStorage.getItem('am_id') == null || localStorage.getItem('am_mobile') == null) {
		
		myApp.confirm('您还没有添加地址，点击确定前往添加地址立刻获得家庭助理，点击返回留在此页', "", function () {
			mainView.router.loadPage("user/mine-add-addr.html?addr_id=0&return_url=user/charge/mine-charge-list.html");
	    });
	} else {
	
		myApp.confirm('土豪,大额充值可联系您的助理为您上门办理。确定拨打助理电话:'+amMobile, "", function () {
			$$("#cal_am_mobile_real").attr("href", "tel:"+amMobile);
			$$("#cal_am_mobile_real").click();
	    });
	}
};myApp.onPageBeforeInit('charge-pay-success-page', function(page) {
	
	var userId = localStorage.getItem("user_id");

	var userInfoSuccess = function(data, textStatus, jqXHR) {
	  	myApp.hideIndicator();
		var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(data.msg);
			return;
		}
		var userInfo = result.data; 
		$$("#rest_money").text(userInfo.rest_money);
	};
	
	//获取用户充值信息接口
    $$.ajax({
        type:"GET",
        url:siteAPIPath+"user/get_userinfo.json?user_id="+userId,
        dataType:"json",
        cache:false,
        statusCode : {
			200 : userInfoSuccess,
			400 : ajaxError,
			500 : ajaxError
		}
    });
    
    $$('#charge-pay-success-btn').click(function(){
    	mainView.router.loadPage("user/mine.html");
    });
	
});;myApp.onPageBeforeInit('mine-charge-way', function(page) {
	
	var userId = localStorage.getItem("user_id");

	var cardId = page.query.card_id;
	var cardPay = page.query.card_pay;
	var orderPayType = 2;
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	
	$$("#mobile").text(localStorage.getItem("user_mobile"));
	
    $$("#card_pay_view").text(cardPay);
	if (cardPay == undefined || cardPay == '' || cardPay == 0) {
		var cardInfoSuccess = function(data, textStatus, jqXHR) {
		  	myApp.hideIndicator();
			var result = JSON.parse(data.response);
			if (result.status == "999") {
				myApp.alert(data.msg);
				return;
			}
			var cardInfo = result.data; 
			$$("#card_pay_view").text(cardInfo.card_value);
		};
	

		$$.ajax({
	        type:"GET",
	        url:siteAPIPath+"dict/get_card_detail.json?card_id="+cardId,
	        dataType:"json",
	        cache:false,
	        statusCode : {
				200 : cardInfoSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
		}); 
	}
	
	
	var postCardBuySuccess =function(data, textStatus, jqXHR) {

	 	var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}
		console.log(orderPayType)
		var orderId = result.data.id;
		//如果为微信支付，则需要跳转到微信支付页面.
//		if (orderPayType == 2) {
			 var wxPayUrl = localUrl + "/" + appName + "/wx-pay-pre.jsp";
			 wxPayUrl +="?orderId="+orderId;
			 wxPayUrl +="&userCouponId=0";
			 wxPayUrl +="&orderType=4";
			 wxPayUrl +="&payOrderType=1";
			 location.href = wxPayUrl;
//		}
	};		
	
	//点击支付的处理
	$$(".chongzhi-submit").click(function(){

		
		var postdata = {};
		postdata.user_id = userId;
		postdata.card_type = cardId;
		postdata.pay_type = 2;
						
		$$.ajax({
			type: "post",
			 url: siteAPIPath + "user/card_buy.json",
			data: postdata,
			statusCode: {
	         	200: postCardBuySuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});		
		 
	});	
    
    
    
    
});;myApp.onPageInit('mine-coupon-list-page', function (page) {
		
	var userId = localStorage.getItem("user_id");
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	localStorage['u_order_money_param'] = page.query.order_money;
	localStorage['u_order_type_param'] = page.query.order_type;
	//处理订单调整到当前页面选择优惠劵的
	$$(document).on('click', '#selectCouponLink', function (e) {

		e.stopImmediatePropagation(); //防止重复点击

		var returnPage = ""
		for (var i =1; i < 5; i++) {
			var historyPage = mainView.history[mainView.history.length-i];
			console.log(historyPage);
			if (historyPage == undefined) continue;
			
			if (historyPage.indexOf("order-view-0") >= 0 || 
				historyPage.indexOf("order-view-1") >= 0 ||
				historyPage.indexOf("order-view-2") >= 0 ||
				historyPage.indexOf("order-form-zhongdiangong-pay") >= 0) {
				returnPage = historyPage;
				break;
			}
		}
		
		if (returnPage == "") return;
		
		//		page.query ,有时不好用
		var orderTypeParam = localStorage['u_order_type_param'];

		var orderMoney = localStorage['u_order_money_param'];
		//做优惠劵的判断.
		var serviceType = $$(this).find("input[name=service_type]").val();
		var maxValue = $$(this).find("input[name=max_value]").val();
		var userCouponId = $$(this).find("#user_coupon_id").val();
		var userCouponName = $$(this).find("#introduction").html();
		var userCouponValue = $$(this).find("#user_coupon_value").val();

		//判断当前选择优惠劵是否适用
		if (serviceType.indexOf(orderTypeParam) < 0) {
			myApp.alert("当前优惠劵不适用!");
			return false;
		}
		
		if (orderMoney != undefined) {
			if (parseFloat(orderMoney) < parseFloat(maxValue)) {
				myApp.alert("消费满"+ maxValue + "可用!");
				return false;				
			}
		}
		
    	returnPage = replaceParamVal(returnPage, "user_coupon_id", userCouponId);
    	returnPage = replaceParamVal(returnPage, "user_coupon_name", userCouponName);
    	returnPage = replaceParamVal(returnPage, "user_coupon_value", userCouponValue);
    	
    	if(returnPage.indexOf('?')>0){
    		returnPage+= "&user_coupon_id="+userCouponId;
    	}else{
    		returnPage+= "?user_coupon_id="+userCouponId;
    	}
    	returnPage+= "&user_coupon_name="+ userCouponName;
    	returnPage+= "&user_coupon_value="+ userCouponValue;
    	
    	mainView.router.loadPage(returnPage);		
		
	});
	
	
	
     $$("#user-coupon-exchange-button").on("click",function(){
    	 exchangeUserCoupon(userId);
 	});
});

//列表显示用户兑换码
myApp.template7Data['page:mine-coupon-list-page'] = function(){
  var result;
  var userId = localStorage.getItem("user_id");
  $$.ajax({
          type : "GET",
          url  : siteAPIPath+"user/get_coupon.json?user_id="+userId,
          dataType: "json",
          cache : true,
          async : false,
          success: function(data){
              result = data;
          }
  })
  return result;
}
var onExchangeSuccess = function(data, textStatus, jqXHR){
	myApp.hideIndicator();
   	var result = JSON.parse(data.response);
	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
	  var userAddr = result.data;
	  mainView.router.loadPage("user/coupon/mine-coupon-list.html?user_id="+1);
	}
//兑换优惠券function
function exchangeUserCoupon(userId){
	
	var cardPasswd = $$("#cardPasswdItem").val();
	if(cardPasswd =='' || cardPasswd.length <0){
		myApp.alert("兑换码不能为空");
		return;
	}
    $$.ajax({
        type:"POST",
        url:siteAPIPath+"user/post_coupon.json",
        data:{"user_id":userId,
        	  "card_passwd":cardPasswd
        	  },
        dataType:"json",
        cache:false,
        statusCode: {
         	200: onExchangeSuccess,
 	    	400: ajaxError,
 	    	500: ajaxError
 	    },
    });
};myApp.onPageInit("mine-feed-back-info", function (page) {
	var userId = localStorage.getItem("user_id");
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	$$(".feed-back-info-submit").on("click",function(){
		var content = $$("#service_content").val();
		if(content !='' && content != undefined){
			if(content.length >100){
				myApp.alert("只能输入100个字");
				return ;
			}
			saveUserFeedBack(userId);
		}else{
			myApp.alert("请输入您的意见详情！");    
			return;
		}
	});
});
var saveUserFeedBackSuccess =function(data, textStatus, jqXHR) {
   	var result = JSON.parse(data.response);
	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
/*    myApp.modal({
        text: '您的意见我们已经收到，感谢您的反馈!',
        buttons: [
          {
            text: '好的',
            bold: true,
          },
        ]
      })*/
	myApp.alert("您的意见我们已经收到，感谢您的反馈!");
	mainView.router.loadPage("user/mine.html");
}
//保存用户意见接口
function saveUserFeedBack(userId) {
	var postdata = {};
    postdata.user_id = userId; 
    postdata.content = $$("#service_content").val();
	$$.ajax({
		type : "POST",
		url : siteAPIPath + "user/post_feed_back.json",
		dataType : "json",
		cache : true,
		data :postdata,
		statusCode: {
         	200: saveUserFeedBackSuccess,
 	    	400: ajaxError,
 	    	500: ajaxError
 	    },
	});
}
;myApp.onPageBeforeInit('mine', function (page) {
	var userId = localStorage.getItem("user_id");;
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	getUserInfos(userId);
	
	$$("#mine-addr-list").on("click",function(){
			mainView.router.loadPage("user/mine-addr-list.html?user_id="+0);
		});
	$$("#mine-coupon-lists").on("click",function(){
		mainView.router.loadPage("user/coupon/mine-coupon-list.html?user_id="+userId);
	});
	$$("#mine-charge-list").on("click",function(){
		mainView.router.loadPage("user/charge/mine-charge-list.html?user_id="+userId);
	});
	$$("#mine-feedback-info").on("click",function(){
		mainView.router.loadPage("user/mine-feedback-info.html?user_id="+userId);
	});
	$$("#mine-more").on("click",function(){
		mainView.router.loadPage("user/more.html");
	});
	$$("#mine-info").on("click",function(){
		mainView.router.loadPage("user/user-wancheng.html");
	});

	$$('.user-logout').on('click', function() {
		  localStorage.removeItem("mobile");
		  localStorage.removeItem('user_id');
		  localStorage.removeItem('im_username');
		  localStorage.removeItem('im_password');
		  localStorage.removeItem('service_type_addons_list');
		  localStorage.removeItem('service_type_list');
		  localStorage.removeItem("am_id");
		  localStorage.removeItem("am_mobile");
		  mainView.router.loadPage("index.html");
	});
	
});


var onUserInfoSuccess =function(data, textStatus, jqXHR) {
   	var result = JSON.parse(data.response);
	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
	var user = result.data;
	var headImg = user.head_img;
	if(headImg !='' && headImg != undefined){
		$$("#head_img-span").attr("src",user.head_img);
	}
	 $$("#mobile").text(user.mobile);
	 $$("#total_coupon").text(user.total_coupon+" 张");
	 $$("#user-id").text(user.id);
	 $$("#mine-rest-money").text(user.rest_money+" 元");
	 $$("#score").text(user.score);
	 var temp = Number(0);
     var restMoney = user.rest_money;
     temp = restMoney/100;
     if(temp>=100){
    	 temp = 100;
     }
	 $$("#am-time-span").text("约"+temp.toFixed(1)+"小时");
	 
	  // 路径配置
	    require.config({
	        paths: {
	            	echarts: 'http://echarts.baidu.com/build/dist'
	        }
	    });
	    // 使用
	    require(
	        [
	            'echarts',
	            'echarts/chart/gauge' // 使用柱状图就加载bar模块，按需加载
	        ],
	        function (ec) {
	            // 基于准备好的dom，初始化echarts图表
	            var myChart = ec.init(document.getElementById('main')); 
	            option = {
	            	    tooltip : {
	            	        formatter: "{a} <br/>{b} : {c}%"
	            	    },
	            	    series : [
	            	        {
	            	           /* name:'个性化仪表盘',*/
	            	            type:'gauge',
	            	            center : ['50%', '50%'],    // 默认全局居中
	            	            radius : [0, '75%'],
	            	            startAngle: 140,
	            	            endAngle : -140,
	            	            min: 0,                     // 最小值
	            	            max: 100,                   // 最大值
	            	            precision: 0,               // 小数精度，默认为0，无小数点
	            	            splitNumber: 10,             // 分割段数，默认为5
	            	            axisLine: {            // 坐标轴线
	            	                show: true,        // 默认显示，属性show控制显示与否
	            	                lineStyle: {       // 属性lineStyle控制线条样式
	            	                    color: [[0.2, 'lightgreen'],[0.4, 'orange'],[0.8, 'skyblue'],[1, '#ff4500']], 
	            	                    width: 30
	            	                }
	            	            },
	            	            axisTick: {            // 坐标轴小标记
	            	                show: true,        // 属性show控制显示与否，默认不显示
	            	                splitNumber: 5,    // 每份split细分多少段
	            	                length :8,         // 属性length控制线长
	            	                lineStyle: {       // 属性lineStyle控制线条样式
	            	                    color: '#eee',
	            	                    width: 1,
	            	                    type: 'solid'
	            	                }
	            	            },
	            	            axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
	            	                show: true,
	            	                rotate:-90,
	            	                formatter: function(v){
	            	                    switch (v+''){
	            	                        case '0': return '100';
	            	                        case '20': return '80';
	            	                        case '40': return '60';
	            	                        case '60': return '40';
	            	                        case '80': return '20';
	            	                        case '100': return '0';
	            	                        default: return '';
	            	                    }
	            	                },
	            	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	            	                    color: '#333'
	            	                }
	            	            },
	            	            splitLine: {           // 分隔线
	            	                show: true,        // 默认显示，属性show控制显示与否
	            	                length :30,         // 属性length控制线长
	            	                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
	            	                    color: '#eee',
	            	                    width: 2,
	            	                    type: 'solid'
	            	                }
	            	            },
	            	            pointer : {
	            	                length : '80%',
	            	                width : 8,
	            	                color : 'auto'
	            	            },
	            	            tooltip :{
	            	            	show:false
	            	            },
	            	            title : {
	            	                show : true,
	            	                offsetCenter: ['-65%', -10],       // x, y，单位px
	            	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	            	                    color: '#333',
	            	                    fontSize : 15
	            	                }
	            	            },
	            	            detail : {
	            	                show : false,
	            	                backgroundColor: 'rgba(0,0,0,0)',
	            	                borderWidth: 0,
	            	                borderColor: '#ccc',
	            	                width: 100,
	            	                height: 40,
	            	                offsetCenter: ['-60%', 10],       // x, y，单位px
	            	                formatter:'{value}%',
	            	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	            	                    color: 'auto',
	            	                    fontSize : 30
	            	                }
	            	            },
	            	            data:[{value: 30}],
	            	        }
	            	    ]
	            	};
	    
	            // 为echarts对象加载数据 
	            var temp = Number(0);
	           var restMoney = user.rest_money;
	           temp = restMoney/100;
	           if(temp>=100){
	        	 temp = 100;
	           }
	       	 option.series[0].data[0].value = 100-temp.toFixed(1);
	         myChart.setOption(option, true);
	        }
	    );
}

//获取用户信息接口
function getUserInfos(userId) {
	var postdata = {};
    postdata.user_id = userId;    
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "user/get_userinfo.json",
		dataType : "json",
		cache : true,
		data :postdata,
		statusCode: {
         	200: onUserInfoSuccess,
 	    	400: ajaxError,
 	    	500: ajaxError
 	    },
	});
}
/*function goToOrderForm() {
	var userId = $$.urlParam('user_id');
	location.href = "wx-order-form.html?user_id=" + userId;
}

function goToOrderList() {
	var userId = $$.urlParam('user_id');
	location.href = "wx-order-list.html?user_id=" + userId;
}*/

;myApp.onPageBeforeInit('mine-more', function (page) {
	var userId = localStorage.getItem("user_id");
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	$$("#mine-faq").on("click",function(){
			mainView.router.loadPage("user/faq.html");
		});
	$$("#mine-agreement").on("click",function(){
		mainView.router.loadPage("user/agreement.html");
	});
	$$("#mine-aboutus").on("click",function(){
		mainView.router.loadPage("user/aboutus.html");
	});
});;myApp.onPageBeforeInit('user-am-detail-page', function(page) {

	var userId = localStorage['user_id'];
	var postData = {};
		
	postData.user_id = userId;
	
	$$.ajax({
		type:"get",
		url  : siteAPIPath+"user/user_get_am.json",
        data : postData,
        cache: true,
		success: function(datas,status,xhr){
			var result = JSON.parse(datas);
			
			var formResult = result.data;
			
			if (formResult != "") {
				localStorage.setItem('am_id', formResult.staff_id);
				localStorage.setItem('am_mobile', formResult.mobile);
			}
			
			
			if(formResult.head_img != ""){
				$$("#staffImg").attr("src",formResult.head_img);
			}else{
				$$("#staffImg").attr("src","./img/i-f7.png");
			}
			
			$$("#staffName").text("姓名:  "+formResult.name);
			$$("#cardNo").text("身份证号:  "+formResult.card_id);
			
			//根据出生日期，得出年龄
			var reg =/[\u4E00-\u9FA5]/g;	//去除中文
			
			var bbb =   moment(formResult.birth);
			var aaa =  moment(bbb).fromNow();
			
			$$("#age").text("年龄:  "+aaa.replace(reg,'')+"岁");
			
			//呼叫助理
			$$("#callStaff").attr("href","tel:"+formResult.mobile);
			
			
//			$$("#astro").text(formResult.astro);
			
			var astroName = getAstroName(formResult.astro);
			
			$$("#astro").text(astroName);
			
			$$("#bloodType").text(formResult.blood_type);
			
			for(var i = 0; i<formResult.tag_list.length;i++){
				$$("#tagName").append("<a href='#' >"+formResult.tag_list[i].tag_name+"</a>&nbsp&nbsp&nbsp");
			}
			
			$$("#orderNum").text("助理"+formResult.name+"已经为您服务"+formResult.order_num+"次啦");
			
			$$("#intro").text(formResult.intro);
			
		}
	});

	
});



;myApp.onPageBeforeInit('user-wancheng', function (page) {
	var userId = localStorage.getItem("user_id");;
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	getMineInfos(userId);
	$$("#user_mine_submit").on("click",function(){
		$$("#user_mine_submit").attr("disabled", true);
		var formData = myApp.formToJSON('#user_wancheng');
		$$.ajax({
			type : "POST",
			url : siteAPIPath+ "user/post_user_info.json?user_id="+userId,
			dataType : "json",
			cache : false,
			data : formData,
			statusCode : {
				200 : saveUserSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
		});
		});
});
function saveUserSuccess(data, textStatus, jqXHR) {
	 $$("#user_mine_submit").removeAttr('disabled');
	myApp.hideIndicator();
	var results = JSON.parse(data.response);
	if (results.status == "999") {
		myApp.alert(results.msg);
		return;
	}
	if (results.status == "0") {
		mainView.router.loadPage("user/mine.html");
	}
} 
var onMineInfoSuccess =function(data, textStatus, jqXHR) {
   	var result = JSON.parse(data.response);
	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
	var user = result.data;
	 $$("#user_mobile_span").text(user.mobile);
	 var formData = {
		'name' : user.name,
	}
	 myApp.formFromJSON('#user_wancheng', formData);
}
//获取用户信息接口
function getMineInfos(userId) {
	var postdata = {};
    postdata.user_id = userId;    
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "user/get_userinfo.json",
		dataType : "json",
		cache : true,
		data :postdata,
		statusCode: {
         	200: onMineInfoSuccess,
 	    	400: ajaxError,
 	    	500: ajaxError
 	    },
	});
}