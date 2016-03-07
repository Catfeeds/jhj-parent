myApp.onPageInit('login', function (page) {
		
	$$('#get_code').on('click',function(e) {
		
		var mobile = $$("#am_mobile").val();		
        if(mobile == undefined || mobile == '') {
        	myApp.alert("请填写手机号。");
            return false;
        }
        var mobileStr = mobile.trim();
        if(mobileStr.length != 11) {
        	myApp.alert("请填写正确的手机号码");
        	return false;
        }

        var count = 60;
        var countdown = setInterval(CountDown, 1000);
        function CountDown(){
        	$$("#get_code").attr("disabled", true);
            $$("#get_code").css("background","#999");
            $$("#get_code").text(count + "秒后重新获取");
            if (count == 0) {
                $$("#get_code").removeAttr("disabled");
                $$("#get_code").css("background","#f37b1d");
                $$("#get_code").text("获取验证码");
                clearInterval(countdown);
            }
            count--;
        }

        var postdata = {};
        postdata.mobile = mobileStr;
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
    	var mobile = $$("#am_mobile").val();
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
//    	   	console.log(result);
//    	   	console.log(result.status);
//    	   	console.log(result.data.id);
//    	   	console.log(result.data.mobile);
    	   	if (result.status == "999") {
    	   		myApp.alert(result.msg);
    	   		return;
    	   	}
    	   	
    	   	if (result.status == "0") {
    	   	  //登录成功后记录用户有关信息
    	   		console.log(result.data+"---"+result.data.mobile);
    	   	  localStorage['am_mobile'] = result.data.mobile;
    	   	  localStorage['am_id']= result.data.staff_id;
    	   	}
    	   	
    	   	mainView.router.loadPage("order/order-list.html");
    	};                
        
        
        var postdata = {};
        postdata.mobile = mobile;
        postdata.sms_token = verifyCode;        
        postdata.login_from = 1;
        postdata.user_type = 2;

        $$.ajax({
            type : "POST",
            // type : "GET",
            url  : siteAPIPath+"am/login.json",
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
	
	
});







    
;myApp.onPageBeforeInit('am-mine-page', function (page) {
	
	var amId = localStorage['am_id'];
	
	$$('#sec-logout').on('click', function() {
		  localStorage.removeItem("am_id");
		  localStorage.removeItem('am_mobile');
		  localStorage.removeItem('service_type_addons_list');
		  localStorage.removeItem('service_type_list');
		  mainView.router.loadPage("index.html");
	});
	
	$$("#getStaffDetial").on('click',function(){
		mainView.router.loadPage("mine/staff-list.html?amId="+amId);
	});
});

myApp.template7Data['page:am-mine-page'] = function(){
    var result; 
    var postdata = {};
	var amId = localStorage['am_id'];
	
	postdata.amId = amId;

  
   $$.ajax({
	   type : "GET",
       url  : siteAPIPath+"user/get_am_by_amId.json",
       dataType: "json",
      cache : true,
       data : postdata,
      async : false,	
      success: function(datas){
    	  
    	  console.log(datas.data);
//          result = datas.data;
           result = datas.data;
           
           //根据生日，计算年龄
            var reg =/[\u4E00-\u9FA5]/g;	//去除中文
			var bbb =   moment(result.birth);
			var aaa =  moment(bbb).fromNow();
           
			result.real_age = aaa.replace(reg,'');
			
			//星座名称
			result.astro_name =  getAstroName(result.astro);
			  
      },
   	  error:ajaxErrorClean
   })
  
  return result;
}

var ajaxErrorClean = function(data, textStatus, jqXHR) {	
	// We have received response and can hide activity indicator
    myApp.hideIndicator();		
	myApp.alert('网络繁忙,请稍后再试.');
	
	localStorage.removeItem("am_id");
	localStorage.removeItem('am_mobile');
	localStorage.removeItem('service_type_addons_list');
	localStorage.removeItem('service_type_list');
	mainView.router.loadPage("login.html");
};

;myApp.onPageInit('staff-list-page', function (page) {
	
	var amId = localStorage['am_id'];
	var postdata = {};
	var page = 1;
	var loading = false;// 加载flag
	
	
	postdata.amId = amId;
	postdata.page = page;
	
	//追加页面，替换相应的值
	var orderListSuccess= function(data, textStatus, jqXHR) {
		myApp.hideIndicator();
		var result = JSON.parse(data.response);
		var staffs = result.data;
		var html = $$('#staffList').html();
		var resultHtml = '';
		
		
		for(var i = 0; i< staffs.length; i++) {
			var staff = staffs[i];
			var htmlPart = html;
			//replace,
			htmlPart = htmlPart.replace(new RegExp('{name}',"gm"), staff.name);
			htmlPart = htmlPart.replace(new RegExp('{mobile}',"gm"), staff.mobile);
			
			htmlPart = htmlPart.replace(new RegExp('{tel}',"gm"),staff.mobile);
			resultHtml += htmlPart;
		}
		$$('.staff-list ul').append(resultHtml);
		
		page = page + 1;
		loading = false;
		
		if (staffs.length < 3) {
		      myApp.detachInfiniteScroll($$('.infinite-scroll'));
		      $$('.infinite-scroll-preloader').remove();// 删除加载提示符
		      return;			
		}
	};	
	
	postdata.amId = amId;
	postdata.page = page;
	//页面加载时获得阿姨列表
	 $$.ajax({
         type : "GET",
         url  : siteAPIPath+"user/get_staff_by_amId.json",
//         dataType: "json",
//         cache : true,
         data : postdata,
//         async : false,
 		statusCode : {
 			200 : orderListSuccess,
 			400 : ajaxError,
 			500 : ajaxError
 		},
 		
 });
	// 注册'infinite'事件处理函数,下拉滚动时触发
	$$('.infinite-scroll').on('infinite', function () {
		if (loading) return;//如果正在加载，则退出
		loading = true;// 设置flag
		postdata.amId = amId;
		postdata.page = page;
		$$.ajax({
			type : "GET",
			url  : siteAPIPath+"user/get_staff_by_amId.json",
//			dataType : "json",
//			cache : true,
			data : postdata,
			timeout: 10000, //超时时间：10秒
			statusCode : {
				200 : orderListSuccess,
				400 : ajaxError,
				500 : ajaxError
			},
		});
	}); 
});;myApp.onPageBeforeInit('alter-order-1-page', function (page) {
	  
	var orderNo = page.query.order_no;
	var amId =  page.query.am_id;
	var servcieTypeAddonsList = JSON.parse(localStorage.getItem("service_type_addons_list"));
	
	var saveExpCleanOrderSuccess = function(data, textStatus, jqXHR) {
		$$("#order-clean-submit").removeAttr('disabled');
		myApp.hideIndicator();
		var results = JSON.parse(data.response);
		if (results.status == "999") {
			myApp.alert(results.msg);
			return;
		}
		if (results.status == "100") {
			myApp.alert(results.msg);
			mainView.router.loadPage("order/order-list.html");
		}
		if (results.status == "0") {
			myApp.alert("订单调整完成");
			mainView.router.loadPage("order/order-list.html");
		}
	} 
	//订单调整保存
	var itemNum = Number(0);
	$$("#order-clean-submit").on("click", function(e) {
		 $$("#order-clean-submit").attr("disabled", true);
		if (expCleanOorderFormValidation() == false) {
			$$("#order-clean-submit").removeAttr('disabled');
			return false;
		}
		var jsonData = "";
		$$("input[name = item_num]").each(function(key,index){
			 itemNum = $$(this).val();
			 var serviceAddonIdObj = $$(this).parent().find('input[name=service_addon_id]');
			 var serviceAddonId = serviceAddonIdObj.val();
			 if (itemNum != undefined  && serviceAddonId != undefined) {
				jsonData+="{";
				jsonData +="serviceAddonId"+":"+serviceAddonId +","+"itemNum"+":"+itemNum+",";
				jsonData = jsonData.substring(0,jsonData.length - 1)+"},";
			 }
		});
		var index = jsonData.lastIndexOf(",");
		jsonData = "[" + jsonData.substring(0,index)+"]";
		var formData = myApp.formToJSON('#alter-order-1-view');
		formData.service_addons_datas=jsonData;
	/*	formData.service_addr = $$("#addr_id").val();
		formData.service_date =  moment(formData.serviceDate + ":00", "yyyy-MM-DD HH:mm:ss").unix();
		*/
		$$.ajax({
			type : "POST",
			url : siteAPIPath+ "order/post_am_clean.json",
			dataType : "json",
			cache : false,
			data : formData,
			statusCode : {
				200 : saveExpCleanOrderSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
		});
	})
	
	
});


//列表显示深度保洁项
myApp.template7Data['page:alter-order-1-page'] = function(){
  var result;

  var amId = getParameterByName('am_id');
  var orderNo = getParameterByName('order_no');
  
   $$.ajax({
          type : "GET",
          url:siteAPIPath+"order/get_am_clean_detail.json?am_id="+amId+"&order_no="+orderNo,
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

function expCleanOorderFormValidation() {
	var formData = myApp.formToJSON('#alter-order-1-view');

	var flag = true;
	$$("input[name='item_num']").each(function(key,index){
		var item_num = $$(this).val();
			if(item_num ==''||item_num ==undefined ){
				myApp.alert("类别数量不能为空！");
				flag =  false;	
			}	
	});
	return flag;
}
function ValidateNumber(e, pnumber){
	if (!/^\d+$/.test(pnumber)){
	e.value = /^\d+/.exec(e.value);
	}
	return e.value;
}
//加号处理
function onAddItemNum(obj) {

	var itemNumObj = obj.find('input[name=item_num]'); 
	var v = itemNumObj.val();
	if (v == undefined || !isNum(v)) {
		v = 0;
	} else {
		v = parseInt(v) + 1;
	}
	itemNumObj.val(v);

	setTotal();
}

//减号处理
function onSubItemNum(obj) {
	var itemNumObj = obj.find('input[name=item_num]'); 
	console.log(itemNumObj);
	var v = itemNumObj.val();
	if (v == undefined || !isNum(v) || v == 0) {
		v = 0;
	} else {
		v = parseInt(v) - 1;
	}
	itemNumObj.val(v);
	setTotal();	
}


//计算价格总和
function setTotal() {

	var orderMoney =  itemPrice= Number(0);
	var serviceAddonId = 0;
	var serviceAddonIdObj;
	$$("input[name = item_num]").each(function(key, index) {
		 
		 itemNum = $$(this).val();
		 serviceAddonIdObj = $$(this).parent().find('input[name=service_addon_id]');

		 serviceAddonId = serviceAddonIdObj.val();
		 
		 if (itemNum != undefined && isNum(itemNum) && serviceAddonId != undefined) {

			 itemPrice = getServiceAddonPrice(serviceAddonId);
				 
			 orderMoney = Number(orderMoney) + Number(itemNum) * Number(itemPrice);
			 
		 }
	
	});	
	$$("#order_money_input").attr("value",orderMoney);		
	$$("#order_moneys_span").text("￥"+orderMoney);		
}
;myApp.onPageInit('alter-order-page', function (page) {

	var orderNo = page.query.order_no;
	var amId =  page.query.am_id;
	console.log("alter-order-page");
	//获取订单详情
	
	var orderAmInfoSuccess =function(data, textStatus, jqXHR) {
//		console.log("orderAmInfoSuccess");
	 	var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}
		var order = result.data;

		var formData = {
			'order_no' : order.order_no,
			'service_content' : order.service_content,
			'order_money' : order.order_money,
		}
		myApp.formFromJSON('#order-alert-form', formData);
		
		$$("#order_type_name_for_am").html(order.order_type_name);
		$$("#order_status_name_for_am").html(order.order_status_name);		
	}	
	
	$$.ajax({
		type : "GET",
		url :siteAPIPath+"order/get_am_order_detail.json?am_id="+amId+"&order_no="+orderNo,
		dataType : "json",
		cache : true,
		statusCode: {
         	200: orderAmInfoSuccess,
 	    	400: ajaxError,
 	    	500: ajaxError
 	    },
	});
	
	
	var  saveOrderSuccess = function(data, textStatus, jqXHR) {
		 $$("#mine-addr-save").removeAttr('disabled');  
		myApp.hideIndicator();
		var result = JSON.parse(data.response);
		var results = result.appResultData;
		if (results.status == "999") {
			myApp.alert(results.msg);
			return;
		}
		if (results.status == "0") {
			myApp.alert("订单调整完成");
			mainView.router.loadPage("order/order-list.html");
		}
	} 
	//订单调整保存
	$$("#order-adjust-submit").on("click", function() {
		$$("#mine-addr-save").attr("disabled", true);
		if (orderFormValidation() == false) {
			return false;
		}
		
		var formData = myApp.formToJSON('#order-alert-form');
		$$.ajax({
			type : "POST",
			url : siteAPIPath+ "order/post_user_am.json",
			dataType : "json",
			cache : false,
			data : formData,
			statusCode : {
				200 : saveOrderSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
		});
	})
	
});


// 获取订单信息
function getOrderAmInfo(amId,orderNo) {
	console.log("getOrderAmInfo");
	
}

function orderFormValidation() {
	var formData = myApp.formToJSON('#order-alert-form');

	
	if (formData.service_content == "") {
		myApp.alert("请输入服务要求");
		return false;
	}
	var order_monye = formData.order_money;
	if (formData.order_money == "" || formData.order_money == 0 ) {
		myApp.alert("订单金额必须大于0");
		return false;
	}
/*	if (!isUnsignedNumeric(order_monye) ) {
		myApp.alert("订单金额必须正数");
		return false;
	}*/
	return true;
}
;myApp.onPageBeforeInit('order-list-page', function (page) {
	
	var amId = localStorage['am_id'];
	var postdata = {};
	var page = 1;
	var loading = false;// 加载flag
	
	postdata.am_id = amId;
	postdata.page = page;
	
	//追加页面，替换相应的值
	var orderListSuccess= function(data, textStatus, jqXHR) {
		myApp.hideIndicator();
		var result = JSON.parse(data.response);
		var orders = result.data;
		var html = $$('#order-am-list-part').html();
		var resultHtml = '';
		
		console.log("order.legnth = " + orders.length);
		for(var i = 0; i< orders.length; i++) {
			var order = orders[i];
			var htmlPart = html;
			var img_tag = '<img alt="" src="img/icons/order_type_img_'+order.order_type+'.png"></p>';
			htmlPart = htmlPart.replace(new RegExp('{img_tag}',"gm"), img_tag);
			
			htmlPart = htmlPart.replace(new RegExp('{order_type}',"gm"), order.order_type);
			htmlPart = htmlPart.replace(new RegExp('{am_id}',"gm"), order.am_id);
			htmlPart = htmlPart.replace(new RegExp('{order_no}',"gm"), order.order_no);
			htmlPart = htmlPart.replace(new RegExp('{order_type_name}',"gm"), order.order_type_name);
			htmlPart = htmlPart.replace(new RegExp('{order_status_name}',"gm"), order.order_status_name);
			htmlPart = htmlPart.replace(new RegExp('{mobile}',"gm"), order.mobile);
			resultHtml += htmlPart;
		}
		$$('.card-am-list ul').append(resultHtml);
		
		page = page + 1;
		loading = false;
		
		if (orders.length < 10) {
			$$('#order-am-list-more').css("display", "none");
			return;			
		}
	};	
	
	postdata.am_id = amId;
	postdata.page = page;
	//页面加载时获得订单列表
	 $$.ajax({
         type : "GET",
         url  : siteAPIPath+"order/get_am_order_list.json",
         dataType: "json",
         cache : true,
         data : postdata,
         async : false,
 		statusCode : {
 			200 : orderListSuccess,
 			400 : ajaxError,
 			500 : ajaxError
 		},
 		
	 });

	 $$('#order-am-list-more').on('click', function () {

		if (loading) return;//如果正在加载，则退出
		loading = true;// 设置flag
		postdata.am_id = amId;
		postdata.page = page;
		$$.ajax({
			type : "GET",
			url  : siteAPIPath+"order/get_am_order_list.json",
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
});


function hrefToOrderView(order_type, order_no) {
	var am_id = localStorage['am_id'];
	localStorage.setItem('order_no_param', order_no);
	mainView.router.loadPage("order/order-view-"+order_type+".html?am_id="+am_id+"&order_no="+order_no);
};myApp.template7Data['page:am-order-hour-view-0-page'] = function(){
    var result; 
    var postdata = {};
	
	var am_id = localStorage['am_id'];
	var order_no = localStorage['order_no_param'];
	
	postdata.order_no = order_no;
	postdata.am_id = am_id;
  
   $$.ajax({
      type : "POST",
      url  : siteAPIPath+"order/am_Order_Hour_Detail.json",
      dataType: "json",
      data : postdata,
      cache : true,
      async : false,	
      success: function(data){
    	  console.log(data);
          result = data.data;
          
          //设置时间显示格式
          var timestamp = moment.unix(result.service_date);
		  var startTime = timestamp.format('YYYY-MM-DD HH:mm');
		  result.service_date = startTime;
		  
		  //动态显示阿姨名称
		  
		  if(result.staff_name == ''){
//			$$("#staffName-span").text('暂未分配服务人员');
			result.staff_name = '暂未分配服务人员';
		  }
		  
		  
		 
		  
      }
   })
  
  return result;
}

myApp.onPageInit('am-order-hour-view-0-page', function (page) {
	
	
	  var orderStatus = $$("#orderStatus").val();
	  if(orderStatus == 3){
		  $$("#couponLi").hide();
		  $$("#realMoneyLi").hide();
	  }else{
		  $$("#couponLi").show();
		  $$("#realMoneyLi").show(); 
	  }
	
	
});
;myApp.onPageInit('order-view-page1', function(page) {
	
	var amId = page.query.am_id;
	if (amId == undefined || amId == '' || amId == 0) {
		return;
	}
	var orderNo = page.query.order_no;
	var amId =  page.query.am_id;
		
    //调整订单跳转
	$$('.ddxq-tiiaozheng').on('click', function() {
		mainView.router.loadPage("order/alter-order-view-1.html?am_id="+amId+"&order_no="+orderNo);
	});
	
	
});

//列表显示深度保洁项
myApp.template7Data['page:order-view-page1'] = function(){
  var result; 
  var amId = localStorage['am_id'];
  console.log(amId);
  var orderNo = localStorage['order_no_param'];
  
   $$.ajax({
          type : "GET",
          url:siteAPIPath+"order/get_am_clean_detail.json?am_id="+amId+"&order_no="+orderNo,
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



;myApp.onPageInit('order-view-page2', function(page) {
	
	var amId = page.query.am_id;
	if (amId == undefined || amId == '' || amId == 0) {
		return;
	}
	var orderNo = page.query.order_no;
	var amId =  page.query.am_id;
	
	 //调整订单跳转
	$$('.ddxq-tiiaozheng').on('click', function() {
		mainView.router.loadPage("order/alter-order.html?am_id="+amId+"&order_no="+orderNo);
	});
	
});
//访问订单详情接口
myApp.template7Data['page:order-view-page2'] = function(){
  var result; 
  var amId = localStorage['am_id'];
  console.log(amId);
  var orderNo = localStorage['order_no_param'];
  
   $$.ajax({
          type : "GET",
          url:siteAPIPath+"order/get_am_order_detail.json?am_id="+amId+"&order_no="+orderNo,
          dataType: "json",
          cache : true,
          async : false,
          success: function(data){
        	  console.log(data);
              result = data.data;
          }
  })
  
  return result;
};
myApp.onPageBeforeInit('mine-add-addrs', function(page) {

	var userId = localStorage['user_id'];
	addrId = page.query.addr_id;
	resultUrl = page.query.resultUrl;

	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}

	if (addrId == undefined || addrId == '') {
		addrId = 0;
	}

	if (resultUrl == undefined || resultUrl == '') {
		resultUrl = "user/mine-addr-list.html";
	}

	$$("#user_id").val(userId);
	$$("#addr_id").val(addrId);

	var addrSuccess = function(data, textStatus, jqXHR) {
		$$("#mine-addr-save").removeAttr('disabled'); 
		myApp.hideIndicator();
		var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}
		mainView.router.loadPage(resultUrl + "?user_id=" + userId);
	}

	$$('#mine-addr-save').on('click', function() {
		$$("#mine-addr-save").attr("disabled", true);
		if (addrFormValidation() == false) {
			return false;
		}

		var formData = myApp.formToJSON('#addr-form');
		console.log(formData);

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
		$$('.list-block ul').html("");
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
	$$('.list-block ul').append(resultHtml);

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
function selectAddr(obj) {

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

	//	console.log("lng = " + lng);
	//	console.log("lat = " + lat);
	//	console.log("city = " + lng);
	//	console.log("name = " + name);
	//	console.log("uid = " + uid);

	$$("#name").val(name);
	$$("#longitude").val(lng);
	$$("#latitude").val(lat);
	$$("#city").val(city);
	$$("#uid").val(uid);

	console.log($$("#longitude").val());
	$$('.list-block ul').html("");

}

function addrFormValidation() {
	var formData = myApp.formToJSON('#addr-form');
	console.log(formData);
	if (formData.name == "") {
		myApp.alert("请输入小区名称.");
		return false;
	}

	if (formData.longitude == "" || formData.latitude == "") {
		myApp.alert("请选择你所在的小区");
		return false;
	}

	if (formData.addr == "") {
		myApp.alert("请输入详细门牌号");
		return false;
	}

	return true;
}
;myApp.onPageBeforeInit('mine-addr-list', function (page) {
		
	var userId = localStorage['user_id'];

	var onAddrListSuccess = function(data, textStatus, jqXHR){
		myApp.hideIndicator();
	   	var result = JSON.parse(data.response);

		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}
		var userAddrList = result.data;

		var html = $$('#addr-list-part-li').html();
		var resultHtml = '';
		$$.each(userAddrList, function(i, item) {
			var htmlPart = html;
			htmlPart = htmlPart.replace(new RegExp('{addr_name}', "gm"),item.name + item.addr);
			htmlPart = htmlPart.replace(new RegExp('{addr_id}', "gm"), item.id);
			var is_default_name = "";
			if (item.is_default == 1) {
				is_default_name = "默认";
			}
			htmlPart = htmlPart.replace(new RegExp('{is_default_name}', "gm"), is_default_name);
			resultHtml += htmlPart;
		});
		
		if (resultHtml == '') {
			resultHtml = '<li style="color: #aaa">您还没有服务地址，现在就添加一个吧！</li>';
		}
		
		$$('.list-block ul').append(resultHtml);
	}	
	
	  //获取用户地址列表
	$$.ajax({
	        type:"GET",
	        url: siteAPIPath+"user/get_user_addrs.json?user_id="+userId,
	        dataType:"json",
	        cache:false,
	        statusCode: {
	         	200: onAddrListSuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    },
	});
	
    $$(".mine-add-addr-link").on("click",function(){
 		mainView.router.loadPage("user/mine-add-addr.html?addr_id=0");
 	});

});


function setAddrDefault(addrId) {
	console.log(addrId);
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
			$$("input[class=swipeout]").each(function(key, index) {
				var addrIdObj = $$(this).find('input[name=addr_id]');
				var spanObj = $$(this).find('input[class=am-badge]');
				if (addrId == addrIdObj.val()) {
					spanObj.text("默认");
				} else {
					spanObj.text("");
				}
			});
			myApp.swipeoutClose($$(this));
			
			myApp.addNotification({
		        message: '操作成功',
		        button: {
		            text: '关闭',
		        },
		        hold:2000
		    });
		}
	});	
	
};myApp.onPageBeforeInit('user-form-bianji-page', function (page) {
	
	var userId =  page.query.user_id;
	//获取订单详情
	getOrderInfo(userId);	

	//客户资料调整保存
	$$("#user-kehuzl-submit").on("click", function() {
		$$("#mine-addr-save").attr("disabled", true);
		
		if (orderFormValidation() == false) {
			return false;
		}
		
		var formData = myApp.formToJSON('#user-form-bianji');
		
		$$.ajax({
			type : "POST",
			url : siteAPIPath+ "user/post_user_edit.json?user_id="+userId,
			dataType : "json",
			cache : false,
			data : formData,
			statusCode : {
				200 : saveOrderSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
		});
	})
	
});
//获取订单信息
function getOrderInfo(userId) {
	$$.ajax({
		type : "GET",
		url :siteAPIPath+"user/get_user_edit_detail.json?user_id="+userId,
		dataType : "json",
		cache : true,
		statusCode: {
         	200: orderInfoSuccess,
 	    	400: ajaxError,
 	    	500: ajaxError
 	    },
	});
}
var orderInfoSuccess =function(data, textStatus, jqXHR) {
 	var result = JSON.parse(data.response);
 	var orderHourViewVo = result.data; 

	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
	
	var aaa = orderHourViewVo.list;
	
	var bbb = orderHourViewVo.tag_list;
	
	var selectedTagIds = "";
	if(bbb !=null) {
		for(var j = 0; j< bbb.length;j++){
			selectedTagIds+= bbb[j] + ",";
		}	
		
		$$("#tag_ids").val(selectedTagIds);
	}
		
	
	var hasSelected = false;
	var tagHtml = "";
	if(aaa !=null){
	for(var i = 0; i<aaa.length;i++){
		hasSelected = false;
		if(bbb !=null){
		for(var j = 0; j< bbb.length;j++){
			
			if(aaa[i].tag_id == bbb[j]){
				
				hasSelected = true;
				
				break;
			}
		}
		}
		if(hasSelected){
			if (i % 2 == 0 ) {
				tagHtml +=  "<a href='#' id='tag_id_'"+orderHourViewVo.list[i].tag_id+" onclick='tagClick("+orderHourViewVo.list[i].tag_id+", $$(this))' class='button button-cancel button-round kehuxq-biaoqian'  style='margin-left: 2%;'>"+orderHourViewVo.list[i].tag_name+"</a>";
			} else {
				tagHtml +=  "<a href='#' id='tag_id_'"+orderHourViewVo.list[i].tag_id+" onclick='tagClick("+orderHourViewVo.list[i].tag_id+", $$(this))' class='button button-cancel button-round kehuxq-biaoqian'>"+orderHourViewVo.list[i].tag_name+"</a>";
			}
		}else{
			
			if (i % 2 == 0 ) {
				tagHtml +=  "<a href='#' id='tag_id_'"+orderHourViewVo.list[i].tag_id+" onclick='tagClick("+orderHourViewVo.list[i].tag_id+", $$(this))' class='button button-cancel button-round kehuxg-biaoqian'  style='margin-left: 2%;'>"+orderHourViewVo.list[i].tag_name+"</a>";
			} else {
				tagHtml +=  "<a href='#' id='tag_id_'"+orderHourViewVo.list[i].tag_id+" onclick='tagClick("+orderHourViewVo.list[i].tag_id+", $$(this))' class='button button-cancel button-round kehuxg-biaoqian'>"+orderHourViewVo.list[i].tag_name+"</a>";
			}
			
		}		
		
		
	}}
	$$("#tagNames").append(tagHtml);
	console.log(orderHourViewVo);
    
	  $$("#name").text(orderHourViewVo.name);
	  $$("#mobile").text(orderHourViewVo.mobile);

	  $$("#userAddr").text(orderHourViewVo.addr_name);
	  $$("#remarks").text(orderHourViewVo.remarks);
	  $$("#addrId").text(orderHourViewVo.addr_id);
	  /*var formData = {
		

		'name' : orderHourViewVo.name,
		'mobile' : orderHourViewVo.mobile,
		'sex' : orderHourViewVo.sex,
		'remarks' : orderHourViewVo.remarks,
		
		'userAddr' : orderHourViewVo.addr_name,

        'addrId' : orderHourViewVo.addrId,
       // 'tagName' : orderHourViewVo.tagName
       
		
	}
	myApp.formFromJSON('#user-form-bianji', formData);*/
}



function saveOrderSuccess(data, textStatus, jqXHR) {
	 $$("#mine-addr-save").removeAttr('disabled');
	myApp.hideIndicator();
	var results = JSON.parse(data.response);
	
	if (results.status == "999") {
		myApp.alert(results.msg);
		return;
	}
	if (results.status == "0") {
		myApp.alert("信息修改成功");
		mainView.router.loadPage("user/user-list.html");
	}
} 





function orderFormValidation() {
	var formData = myApp.formToJSON('#order-alert-form');

	if (formData.mobile == "") {
		myApp.alert("请输入手机号");
		return false;
	}
	if (formData.remarks == "") {
		myApp.alert("请输入服务要求");
		return false;
	}

	/*if(formData.sex !="男"&formData.sex !="女"){
		myApp.alert("请输入正常的性别");
		return false;
	}*/
	return true;
}

function tagClick(tagId, obj) {
	//如果未选中，则换class为选中   

	var tagIds = $$("#tag_ids").val();
	
	//kehuxq-biaoqian = 选中
	if (obj.is(".kehuxg-biaoqian")) {
		obj.addClass("kehuxq-biaoqian");
		obj.removeClass("kehuxg-biaoqian");
		
		if (tagIds.indexOf(tagId + ",") < 0) {
			tagIds += tagId + ",";
		}
	//kehuxg-biaoqian = 未选中	
	} else {
		obj.removeClass("kehuxq-biaoqian");
		obj.addClass("kehuxg-biaoqian");	
		if (tagIds.indexOf(tagId + ",") >= 0) {
			tagIds = tagIds.replace(tagId + ",", "");
		}		
	}
	
	 $$("#tag_ids").val(tagIds);
	 
	 console.log( $$("#tag_ids").val());
	
	
	
}
;myApp.onPageInit('user-xiangqing-page', function(page) {
	//var amId = page.query.am_id;
	var userId = page.query.user_id; 

    //调整订单跳转
	$$('#kehuzl-bianji').on('click', function() {
		mainView.router.loadPage("user/user-form-bianji.html?user_id="+userId);
	});

});

//用户详情
myApp.template7Data['page:user-xiangqing-page'] = function(){
  var result; 
  var userIdParam = localStorage['user_id_param'];
   $$.ajax({
          type : "GET",
          url:siteAPIPath+"user/get_user_list_detail.json?user_id="+userIdParam,
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

;myApp.onPageBeforeInit('user-list-page', function (page) {
	var amId = localStorage['am_id'];
	var userId = localStorage['user_id'];
	var orderStatus = localStorage['order_status'];
	var postdata = {};
	var page = 1;
	var loading = false;// 加载flag
	
	postdata.am_id = amId;
	postdata.page = page;
	//追加页面，替换相应的值
	var orderListSuccess= function(data, textStatus, jqXHR) {
		myApp.hideIndicator();
		var result = JSON.parse(data.response);
		var orders = result.data;
		var html = $$('#user-list-part').html();
		var resultHtml = '';
		
		
		for(var i = 0; i< orders.length; i++) {
			var order = orders[i];
			var htmlPart = html;
			
			htmlPart = htmlPart.replace(new RegExp('{mobile}',"gm"), order.mobile);
			htmlPart = htmlPart.replace(new RegExp('{service_addr}',"gm"), order.service_addr);
			htmlPart = htmlPart.replace(new RegExp('{am_id}',"gm"), order.am_id);
			htmlPart = htmlPart.replace(new RegExp('{service_times}',"gm"), order.service_times);
			htmlPart = htmlPart.replace(new RegExp('{user_id}',"gm"), order.user_id);
			htmlPart = htmlPart.replace(new RegExp('{order_status_name}',"gm"), order.order_status_name);
			
			resultHtml += htmlPart;
		}
		$$('.card-user-list ul').append(resultHtml);
		
		page = page + 1;
		loading = false;
		
		if (orders.length < 10) {
			$$('#am-user-list-more').css("display", "none");
			return;			
		}
	};	
	//页面加载时获得订单列表
	 $$.ajax({
         type : "GET",
         url  : siteAPIPath+"user/get_user_list.json",
         dataType: "json",
         cache : true,
         data : postdata,
         async : false,
 		statusCode : {
 			200 : orderListSuccess,
 			400 : ajaxError,
 			500 : ajaxError
 		},
 		
 });
	// 注册'infinite'事件处理函数,下拉滚动时触发
	 $$('#am-user-list-more').on('click', function () {
		if (loading) return;//如果正在加载，则退出
		loading = true;// 设置flag
		postdata.am_id = amId;
		postdata.page = page;
		$$.ajax({
			type : "GET",
			url  : siteAPIPath+"user/get_user_list.json",
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
});



function hrefToUserView(userId) {
	localStorage.setItem('user_id_param', userId);
	mainView.router.loadPage("user/user-form-xiangqing.html?user_id="+userId);
}