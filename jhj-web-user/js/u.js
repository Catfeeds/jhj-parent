function getUserInfo() {
	
	var userId = localStorage['user_id'];
	console.log("getUserInfo");
	if (userId == undefined || userId == "" || userId == 0) return false;
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "user/get_userinfo.json?user_id="+userId,
		dataType : "json",
		cache : false,
		async : false,
		success : function(data) {
			result = data.data;
			localStorage.setItem("is_vip",result.is_vip);
			
			 var userAddr = result.default_user_addr;

	    	  if (userAddr != undefined && userAddr != null) {
	    		  if (userAddr.is_default == 1) {
	    			  
	    			  localStorage.setItem('default_addr_id', userAddr.id);
	    			  localStorage.setItem('default_addr_name', userAddr.name + " " + userAddr.addr);	
	    			  
	    			  console.log("default_addr_id ==" + userAddr.id);
	    		  }
	    	  }
		}
	})
}

function orderLink(url, serviceTypeId) {
	
	localStorage.setItem("firstServiceType",serviceTypeId);
	url = url + "?firstServiceType="+serviceTypeId;
	mainView.router.loadPage(url);
}

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
        
        $$("#get_code").css("background","#999");

        var count = 60;
        var countdown = setInterval(CountDown, 1000);
        function CountDown(){
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
        
        $$(this).attr("disabled", true);
//        $$("#get_code").attr("disabled", true);
        
        var smsTokenSuccess = function(data, textStatus, jqXHR) {
    		
    		// We have received response and can hide activity indicator
    	   	myApp.hideIndicator();    			
    	   	myApp.alert("验证码已发送到您的手机，请注意查收。");
    	};
        
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
        
    	
        
        return false;
	});
	
	
	/**
	 *  获取语音验证码	
	 */
	
	$$('#get_code_yuyin').on('click',function(e) {
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
        	url:siteAPIPath+"user/get_yu_yin_token.json",
//    		headers: {"X-Parse-Application-Id":applicationId,"X-Parse-REST-API-Key":restApiKey},
        	contentType:"application/x-www-form-urlencoded; charset=utf-8",
        	type: "GET",
    	    dataType:"json",
    	    cache:true,
    	    data: postdata,

    	    statusCode: {
    	    	201: voiceTokenSuccess,
    	    	400: ajaxError,
    	    	500: ajaxError
    	    }
    	});
        
    	var voiceTokenSuccess = function(data, textStatus, jqXHR) {
    		
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
    	   	
    	  //2015-11-5 16:01:40 防止重复点击，造成多条重复用户
    		$$("#login_btn").removeAttr('disabled');  
    	   	
    	   	
    	   	
    	   	var result = JSON.parse(data.response);
    	   	if (result.status == "999") {
    	   		myApp.alert(result.msg);
    	   		return;
    	   	}
    	   	
    	   	if (result.status == "0") {
    	   	  //登录成功后记录用户有关信息

    	   	  localStorage.setItem("user_mobile",result.data.mobile);
    	   	  
    	   	  localStorage.setItem("user_id",result.data.id);
    	   	  
    	   	  localStorage.setItem("is_vip",result.data.is_vip);
    	   	  	    	  
	    	  //如果有默认地址则设置为默认地址
	    	  var userAddr = result.default_user_addr;

	    	  if (userAddr != undefined && userAddr != null) {
	    		  if (userAddr.is_default == 1) {
	    			  
	    			  localStorage.setItem('default_addr_id', userAddr.id);
	    			  localStorage.setItem('default_addr_name', userAddr.name + " " + userAddr.addr);	
	    		  }
	    	  }
    	   	  
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
        
        
    	
    	
    	
    	//2015-11-5 16:01:40    防止重复点击，造成多条重复用户
    	$$(this).attr("disabled", true);
    	
    	
    	
        var postdata = {};
        postdata.mobile = mobile;
        postdata.sms_token = verifyCode;        
        postdata.login_from = 1;
        postdata.user_type = 0;

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
    
;myApp.onPageBeforeInit('order-appoint', function(page) {
	
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




;myApp.onPageBeforeInit('order-baby-intro', function(page) {
	
	removeSessionData();

});

;myApp.onPageBeforeInit('order-cal-page', function(page) {

	var userId = localStorage.getItem("user_id");
	var monthNames = [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ];

	var today = new Date();
	var curClickDay;
	var weekLater = new Date().setDate(today.getDate() + 7);
	var loadedMonth = today.getMonth();
	var calendarParams = {
		container : '#calendar-inline-container',
		header : false,
		footer : false,
		value : [ new Date() ],
		dateFormat : 'yyyy-mm-dd',
		weekHeader : true,
		firstDay : 1,
		dayNamesShort : [ '周日', '周一', '周二', '周三', '周四', '周五', '周六' ],
		toolbarTemplate : '<div class="toolbar calendar-custom-toolbar">'
				+ '<div class="toolbar-inner">' + '<div class="left">'
				+ '<a href="#" class="link icon-only"><i class="icon icon-back"></i></a>'
				+ '</div>' + '<div class="center"></div>' + '<div class="right">'
				+ '<a href="#" class="link icon-only"><i class="icon icon-forward"></i></a>'
				+ '</div>' + '</div>' + '</div>',

		onOpen : function(p) {
			console.log("onOpen=" + p.currentYear + "-" +p.currentMonth);
			$$('.calendar-custom-toolbar .center').text(
					monthNames[p.currentMonth] + ', ' + p.currentYear);
			$$('.calendar-custom-toolbar .left .link').on('click', function() {
				calendarInline.prevMonth();
			});
			$$('.calendar-custom-toolbar .right .link').on('click', function() {
				calendarInline.nextMonth();
			});
		},
		onMonthYearChangeStart : function(p) {
			console.log("start current = "  +p.currentMonth + "  load = " + loadedMonth);
			
			
			$$('.calendar-custom-toolbar .center').text(
					monthNames[p.currentMonth] + ', ' + p.currentYear);
			
			if (p.currentMonth != loadedMonth) {
				loadTotalByMonth(userId, p.currentYear, p.currentMonth);
			}
			loadTotalByOrder(userId, p.currentYear, p.currentMonth);
		},
		
		
		onDayClick :function (p, dayContainer, year, month, day) {
			var cmonth = parseInt(month) + 1;
			console.log(year + "-" + cmonth + "-" + day);
			
			loadOrderList(userId, 1, year + "-" + cmonth + "-" + day)
		}
				
	};

	var calendarInline = myApp.calendar(calendarParams);
	
	
	var userId = userId;
		
	
	function loadTotalByMonth(userId, year, month) {
		
		var params = {};
		params.user_id = userId;
		params.year = year;
		params.month = month + 1;
		
		var apiUrl = "order/user_total_by_month.json";
		
		var totalByMonthSuccess = function(data, textStatus, jqXHR) {
			var result = JSON.parse(data.response);
			
			var resultData = result.data;
		
			var monthDatas = result.data;

			var events = [];
			for (var i = 0; i < monthDatas.length; i++) {
//				console.log(monthDatas[i].service_date);
				var serviceDate = new Date(monthDatas[i].service_date);
//				console.log(serviceDate.getFullYear() + "-" + serviceDate.getMonth() + "-" + serviceDate.getDate());
				events.push(new Date(serviceDate.getFullYear(), serviceDate.getMonth(),  serviceDate.getDate()));
			}
//			console.log(events);
//			console.log(events.length);
			if (events.length > 0) {
//				calendarParams.events = events;
				calendarInline.params.events = events;
				
				console.log("set month ====" + (month));
				calendarInline.setYearMonth(year, month, 1);
				
				loadedMonth = month;
			}
			
		};
		$$.ajax({
			type : "GET",
			url : siteAPIPath + apiUrl,
			dataType : "json",
			cache : true,
			data : params,
			statusCode : {
				200 : totalByMonthSuccess,
				400 : ajaxError,
				500 : ajaxError
			},

		});
	}	
	
	function loadTotalByOrder(userId, year, month) {
		
		var params = {};
		params.user_id = userId;
		params.year = year;
		params.month = month + 1;
		
		var apiUrl = "order/user_total_order.json";
		
		var totalByOrderSuccess = function(data, textStatus, jqXHR) {
			var result = JSON.parse(data.response);
			
			var monthTotalDatas = result.data;
		
			for (var i = 0; i < monthTotalDatas.length; i++) {
				if (monthTotalDatas[i].total != undefined) {
					$("#total").html(monthTotalDatas[i].total);
				}
				
				if (monthTotalDatas[i].total_uf != undefined) {
					$("#total_uf").html(monthTotalDatas[i].total_uf);
				}
			}
		};
		$$.ajax({
			type : "GET",
			url : siteAPIPath + apiUrl,
			dataType : "json",
			cache : true,
			data : params,
			statusCode : {
				200 : totalByOrderSuccess,
				400 : ajaxError,
				500 : ajaxError
			},

		});
	}	
	
	loadTotalByMonth(userId, today.getFullYear(), today.getMonth());

	loadTotalByOrder(userId, today.getFullYear(), today.getMonth());
	
	var loading = false;// 加载flag
	var page = $$("#page").val();
	function loadOrderList(userId, page, cal) {
		console.log("page = " + page);
		curClickDay = cal;
		var postdata = {};
		
		var apiUrl = "order/user_order_list.json";
		postdata.user_id = userId;
		postdata.page = page;
		postdata.day = cal;

		var orderListSuccess = function(data, textStatus, jqXHR) {

			var result = JSON.parse(data.response);
			var orders = result.data;

			var html = $$('#order-hour-list').html();

			var resultHtmlNow = ''; // 当前订单

			for (var i = 0; i < orders.length; i++) {
				var order = orders[i];

				var htmlPart = html;
				
				htmlPart = htmlPart.replace(new RegExp('{order_type}', "gm"), order.order_type);
				htmlPart = htmlPart.replace(new RegExp('{order_no}', "gm"), order.order_no);
				htmlPart = htmlPart.replace(new RegExp('{order_hour_type_name}', "gm"),
						order.order_hour_type_name);
				htmlPart = htmlPart.replace(new RegExp('{order_hour_status_name}', "gm"),
						order.order_hour_status_name);
				if (order.order_type == 2) {
					htmlPart = htmlPart.replace(new RegExp('{service_date}', "gm"), "预约时间: "+moment.unix(
							order.add_time).format("YYYY-MM-DD HH:mm"));
				} else {
					htmlPart = htmlPart.replace(new RegExp('{service_date}', "gm"), "服务时间: "+moment.unix(
							order.service_date).format("YYYY-MM-DD HH:mm"));
				}

				htmlPart = htmlPart.replace(new RegExp('{address}', "gm"), order.address);

				resultHtmlNow += htmlPart;
			}
			// 当前订单
			if (page == 1) {
				$$("#card-hour-now-list ul").html(resultHtmlNow);
			} else {
				$$("#card-hour-now-list ul").append(resultHtmlNow);
			}

			loading = false;
			
			page = page + 1;
			$$("#page").val(page);
			console.log("page = " + page);
			console.log("len = " + orders.length);
			if (orders.length < 10) {
				$$('#order-list-more').css("display", "none");
			}
		};

		$$.ajax({
			type : "GET",
			url : siteAPIPath + apiUrl,
			dataType : "json",
			cache : true,
			data : postdata,
			statusCode : {
				200 : orderListSuccess,
				400 : ajaxError,
				500 : ajaxError
			},

		});
	}	
	
	
	/*
	 * 
	 *  时间 ：   月份和 天数都比 实际数字 小 1
	 * 
	 */
//	var todayStr = today.getFullYear() + "-" + (parseInt(today.getMonth()) + 1) + "-" + (parseInt(today.getDay()) + 1);
//	loadOrderList(userId, page, todayStr);
	$$(document).once('pageBeforeAnimation', function () {
		var date=new Date()
		var time=date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
		loadOrderList(userId, page, time);
	});
	
	// 注册'infinite'事件处理函数
	$$('#order-list-more').on('click', function() {
		var cpage = ++page;
		loadOrderList(userId, cpage, curClickDay);
	});
	
});




;myApp.onPageBeforeInit('order-deep-choose', function(page) {
	
	var userId = localStorage['user_id'];
	getUserInfo();
	
	var serviceTypeId = page.query.service_type_id;
	sessionStorage.setItem("service_type_id", serviceTypeId);
	
	sessionStorage.setItem("order_type", 1);
		
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "dict/get_service_type_addons.json?service_type_id=" + serviceTypeId,
		dataType : "json",
		async : false,
		success : function(data) {
			var serviceAddons = data.data;
			
			$$.each(serviceAddons, function(i, item) {
				var html = "";
				var name = item.name;
				if (name == "金牌保洁") return false;
				html = "<ul class='order-rili'>";
				html+= "<li>" + name + "</li>"
				html+= '<li><span onclick="onDeepSubItemNum($$(this).parent())">-</span>';
				
				var n = setItemNum(item.service_addon_id, item.default_num);
				html+= '<input name="itemNum" value="'+n+'" onkeyup="onItemNumKeyUp($$(this).parent())"  onafterpaste="onItemNumKeyUp($$(this).parent())"  maxLength="3" autocomplete="off">';
				html+= '<input type="hidden" name="serviceAddonName" value="'+name+'" autocomplete="off">';
				
				html+= '<input type="hidden" name="defaultNum" value="'+item.default_num+'" autocomplete="off">';
				html+= '<input type="hidden" name="serviceAddonServiceHour" value="'+item.service_hour+'" autocomplete="off">';
				
				html+= '<input type="hidden" name="serviceAddonItemUnit" value="'+item.item_unit+'" autocomplete="off">';
				html+= '<input type="hidden" name="serviceAddonPrice" value="'+item.price+'" autocomplete="off">';
				html+= '<input type="hidden" name="serviceAddonDisPrice" value="'+item.dis_price+'" autocomplete="off">';
				html+= '<input type="hidden" name="serviceAddonId" value="'+item.service_addon_id+'" autocomplete="off">';
				html+= '<span onclick="onDeepAddItemNum($$(this).parent())">+</span></li>';
				html+= "</ul>";
					
				$$("#order-deep-content").append(html);
			});
			
		}
	});
	
	// 地址选择处理，1. 是否有默认地址， 2. 是否有选择的地址（最优先）
	var addrId = getItemAddrId();
	var addrName = getItemAddrName();
	
	if (addrId != undefined && addrId != "") {
		$$("#addrId").val(addrId);
	}
	
	if (userId == 4953) {
		myApp.alert("addrId = " + addrId);
		myApp.alert("addrID val = " + $$("#addrId").val() );
	}
	
	if (addrName != undefined || addrName != "") {
		$$("#orderChooseAddrName").html(addrName);
	}
	
	 $$("#chooseServiceTime").on("click",function() {
		 
		 var validateMsg = setDeepTotal();
		 if (validateMsg != undefined && validateMsg != "") {
			 myApp.alert(validateMsg);
			 return false;
		 }
		 
		 var orderMoney = sessionStorage.getItem("order_money");
		 
		 if (orderMoney == undefined || orderMoney == "" || orderMoney == 0) {
			 myApp.alert("请选择服务数量.");
			 return false;
		 }
		 
		 var addrId = $$("#addrId").val();
		 if (addrId == undefined || addrId == "" || addrId == 0) {
				myApp.alert("请选择地址.");
				return false;	
		 }
		 var url = "order/order-lib-cal.html?next_url=order/order-deep-confirm.html"
			 
	     var staffId = sessionStorage.getItem("staff_id");
		 if (staffId != undefined && staffId != "" && staffId != null) {
			 url+="?staff_id="+staffId;
		 }	 
		
		 mainView.router.loadPage(url);
	 });
	
});


function onItemNumKeyUp(obj) {
	var itemNumObj = obj.find('input[name=itemNum]'); 
	
	var tmptxt=itemNumObj.val();
	tmptxt = tmptxt.replace(/\D/g,'');
	itemNumObj.val(tmptxt);
	
	setDeepTotal();
}

//加号处理
function onDeepAddItemNum(obj) {
	var itemNumObj = obj.find('input[name=itemNum]'); 
	var v = itemNumObj.val();
	if (v == undefined || !isNum(v)) {
		v = 0;
	} else {
		v = parseInt(v) + 1;
	}
	itemNumObj.val(v);
	
	var validateMsg = setDeepTotal();
//	console.log("validateMsg == " + validateMsg);
//	if (validateMsg != undefined && validateMsg != "") {
//		myApp.alert(validateMsg);
//		return false;
//	}
}

//减号处理
function onDeepSubItemNum(obj) {
	console.log("onDeepSubItemNum");
	var itemNumObj = obj.find('input[name=itemNum]'); 
	var v = itemNumObj.val();
	if (v == undefined || !isNum(v) || v == 0) {
		v = 0;
	} else {
		v = parseInt(v) - 1;
	}
	
	itemNumObj.val(v);
	
	var validateMsg = setDeepTotal();
//	console.log("validateMsg == " + validateMsg);
//	if (validateMsg != undefined && validateMsg != "") {
//		myApp.alert(validateMsg);
//		return false;
//	}
	
	
}

function checkDefaultNum() {
	//判断如果有起步数量 ，并且有多个，则只要有一个不超过起步数量即可.
	console.log("checkDefaultNum");
	var hasDefaultNum = false;
	
	var inputItemNum = 0;
	var minDefaultNum = 0;
	
	
	$$("input[name = itemNum]").each(function(key, index) {
		itemNum = $$(this).val();
		 
		 if (itemNum == undefined || itemNum == "") {
			 itemNum = 0;
		 } 
		 
		 inputItemNum = Number(inputItemNum) + Number(itemNum);
		 
		 
		 
		 var serviceAddonName = $$(this).parent().find('input[name=serviceAddonName]').val();
		 var defaultNum = $$(this).parent().find('input[name=defaultNum]').val();
		 
		 if (minDefaultNum == 0 ) {
			 minDefaultNum = defaultNum;
		 } else {
			 if (minDefaultNum > defaultNum)   minDefaultNum = defaultNum;
		 }
	});
	console.log("minDefaultNum==" + minDefaultNum );
	console.log("inputItemNum==" + inputItemNum);
	var validateMsg = ""
	if (Number(minDefaultNum) > 0 && Number(inputItemNum) < Number(minDefaultNum) ) {
		validateMsg = "最低数量为" +minDefaultNum;
	}
	
	console.log("validateMsg == " + validateMsg);
	
	
	
	
	return validateMsg;
}

function setItemNum(serviceAddonId, defaultNum) {

	var itemNum = defaultNum;
	var serviceAddonsJson = sessionStorage.getItem("service_addons_json");
	
	console.log("serviceAddonsJson = " + serviceAddonsJson);
	if (serviceAddonsJson == undefined || serviceAddonsJson == "") {
		return itemNum;
	}
	
	var serviceAddons = JSON.parse(serviceAddonsJson);
	$$.each(serviceAddons, function(i, item) {
		var itemServiceAddonId = item.serviceAddonId;
		var tmpItemNum = item.itemNum;
		
		if (itemServiceAddonId == serviceAddonId) {
			itemNum = tmpItemNum;
			return false;
		}
	});
	
	return itemNum;
}

//计算价格总和
function setDeepTotal() {

	var orderMoney = itemPrice = Number(0);
	var totalServiceHour = 0;
	var serviceAddonId = 0;
	var serviceAddonIdObj;
	var serviceAddonsJson = [];
	var serviceAddons = [];
	
	var isVip = localStorage['is_vip'];
	if (isVip == undefined || isVip == "") isVip = 0;

	$$("input[name = itemNum]").each(function(key, index) {
		 
		 itemNum = $$(this).val();
		 
		 if (itemNum == undefined || itemNum == "") {
			 itemNum = 0;
		 } 
		 
		 serviceAddonIdObj = $$(this).parent().find('input[name=serviceAddonId]');
		 serviceAddonId = serviceAddonIdObj.val();
		 var serviceAddonName = $$(this).parent().find('input[name=serviceAddonName]').val();
		 var defaultNum = $$(this).parent().find('input[name=defaultNum]').val();
		
		 
		 if (itemNum == 0 || 
			 serviceAddonId == undefined || 
			 serviceAddonId == 0) {
			 return false;
		 }
		 
		 var serviceAddonServiceHour = $$(this).parent().find('input[name=serviceAddonServiceHour]').val();
		 
		 var price = $$(this).parent().find('input[name=serviceAddonPrice]').val();
		 var disPrice = $$(this).parent().find('input[name=serviceAddonDisPrice]').val();
		
		 
		 var reg = /[1-9][0-9]*/g;
		 if (isVip == 0) itemPrice = price.match(reg);
		 if (isVip == 1) itemPrice = disPrice.match(reg);

		 orderMoney = Number(orderMoney) + Number(itemNum) * Number(itemPrice);
		 
		 var serviceHour = serviceAddonServiceHour * itemNum;
			
		 if(defaultNum != "" && defaultNum != 0) {
			serviceHour = (serviceAddonServiceHour / defaultNum) * itemNum;
		 }
		 
		 totalServiceHour+= serviceHour;
		 
		 
		 var serviceAddonItemUnit = $$(this).parent().find('input[name=serviceAddonItemUnit]').val();
		 
		 
		 var serviceAddonItem = {};
		 serviceAddonItem.serviceAddonName = serviceAddonName;
		 serviceAddonItem.serviceAddonId = serviceAddonId;
		 serviceAddonItem.itemNum = itemNum;
		 serviceAddonItem.itemUnit = serviceAddonItemUnit;
		 serviceAddons.push(serviceAddonItem); 
		 
		 var serviceAddonJson = {}
		 serviceAddonJson.serviceAddonId = serviceAddonId;
		 serviceAddonJson.itemNum = itemNum;
		 
		 
		 serviceAddonsJson.push(serviceAddonJson); 
	});
		
	totalServiceHour = totalServiceHour.toFixed(0);
	sessionStorage.setItem("order_money", orderMoney);
	sessionStorage.setItem("order_pay", orderMoney);
	sessionStorage.setItem("total_service_hour", totalServiceHour);
	sessionStorage.setItem("service_addons", JSON.stringify(serviceAddons));
	sessionStorage.setItem("service_addons_json", JSON.stringify(serviceAddonsJson));
	
	var validateMsg = checkDefaultNum();
	if (validateMsg != undefined && validateMsg != "") {
		return validateMsg;
	}
	
	return validateMsg;
}
;myApp.onPageInit('order-deep-confirm', function(page) {
	
	// payOrderType 订单支付类型 0 = 订单支付 1= 充值支付 2 = 手机话费类充值 3 = 订单补差价
	sessionStorage.setItem("pay_order_type", 0);
	
	var orderType = sessionStorage.getItem("order_type")
	$$("#orderType").val(orderType);
	
	//获取服务类别基本信息
	var serviceTypeId = sessionStorage.getItem("service_type_id");
	$$("#serviceType").val(serviceTypeId);
	$$.ajax({
	      type : "GET",
	      url: siteAPIPath+"dict/get_service_type.json?service_type_id="+serviceTypeId,
	      dataType: "json",
	      cache : true,
	      async : false,
	      success: function(data) {
	    	var serviceType = data.data;
	    	console.log(serviceType);
	    	if (serviceType == undefined || serviceType == "") {
	    		return false;
	    	}
	    	
	    	$$("#serviceTypeName").html(serviceType.name);
	    	$$("#serviceContent").val(serviceType.name);
	    	
	    	var isMulti = serviceType.is_multi;
	    	
	    	if (isMulti == 1) {
	    		$$("#isMultiStr").html("两人以上服务人员")
	    	}
	      }
	});
	
	var orderMoney = sessionStorage.getItem("order_money");
	console.log("orderMoney =" + orderMoney);
	$$("#priceStr").html(orderMoney);
	$$("#orderMoneyStr").html(orderMoney+ "元");
	$$("#orderMoney").val(orderMoney);
	
	$$("#serviceHourStr").html(sessionStorage.getItem("total_service_hour") + "小时");
	
	var userId = localStorage['user_id'];
	$$("#userId").val(userId);
	
	// 地址选择处理，1. 是否有默认地址， 2. 是否有选择的地址（最优先）
	var addrId = getItemAddrId();
	var addrName = getItemAddrName();
	
	if (addrId != undefined && addrId != "" && addrId != 0) {
		$$("#orderDeepAddrId").val(addrId);
	}
	if (addrName != undefined || addrName != "") {
		$$("#orderHourAddrName").html(addrName);
	}
	
	if (userId == 4953) {
		myApp.alert("addrId = " + addrId);
		myApp.alert("oDAddrId val = " + $$("#orderDeepAddrId").val() );
		myApp.alert("session addr_id = " + sessionStorage.getItem('addr_id'));
	}
	
	//设置服务时间.
	if (sessionStorage.getItem('service_date') != null) {
		$$("#serviceDate").val(sessionStorage.getItem('service_date'));
	}
	
	if (sessionStorage.getItem('service_date_str') != null) {
		$$("#serviceDateStr").html(sessionStorage.getItem('service_date_str'));
	}
	
	//设置优惠劵
	if (sessionStorage.getItem('user_coupon_id') != null) {
		$$("#userCouponId").val(sessionStorage.getItem('user_coupon_id'));
		
		if (sessionStorage.getItem('user_coupon_name') != null) {
			$$("#userCouponName").html(sessionStorage.getItem('user_coupon_name'));
		}

		if (sessionStorage.getItem('user_coupon_value') != null) {
			var userCouponValue = sessionStorage.getItem('user_coupon_value');
			if(userCouponValue==undefined || userCouponValue=="" ||userCouponValue==null){
				userCouponValue=0;
			}
			$$("#userCouponValue").val(userCouponValue);
			console.log("userCouponValue = " + $$("#userCouponValue").val())
			console.log("orderMoney =" + orderMoney);
			var orderPayStr = orderMoney - userCouponValue;
			if (orderPayStr < 0) orderPayStr = 0;
			sessionStorage.setItem("order_pay", orderPayStr);
		}
	} else {
		//读取用户可用的优惠劵
		var params = {};
		params.user_id = $$("#userId").val();
		params.service_type = $$("#serviceType").val();
		params.service_date = $$("#serviceDate").val();
		params.order_money = $$("#orderMoney").val();
		
		$$.ajax({
		      type : "GET",
		      url: siteAPIPath+"user/get_validate_coupons.json",
		      dataType: "json",
		      cache : true,
		      data : params,
		      async : true,
		      success: function(data) {
		    	  var couponList = data.data;
		    	  var nums = 0;
		    	  if (couponList == undefined || couponList == "") {
		    		  nums = 0;
		    	  } else {
		    		  nums = couponList.length;
		    	  }
		    	  var userCouponValue = 0;
	    		  var userCouponId = 0;
	    		  var userCouponName = "";
		    	  if(nums>0){
		    		  $$.each(couponList,function(i,item){
		    			  if(item.value>userCouponValue){
		    				  userCouponValue = item.value;
		    				  userCouponId = item.id;
		    				  userCouponName = "￥" + userCouponValue;
		    			  }
		    		  });
		    		  sessionStorage.setItem("user_coupon_id", userCouponId);
	    			  sessionStorage.setItem("user_coupon_name", userCouponName);
	    			  sessionStorage.setItem("user_coupon_value", userCouponValue);
		    	  }
		    	  var userCouponNameStr = nums + "张可用";
		    	  if (userCouponId != 0 && userCouponValue != 0) {
		    		  var orderPayStr = $$("#orderMoney").val() - userCouponValue;
					  if (orderPayStr < 0) orderPayStr = 0;
					  sessionStorage.setItem("order_pay", orderPayStr);
					  userCouponNameStr = userCouponName;
		    	  }
		    	  
		    	  console.log("order_pay = " + sessionStorage.getItem("order_pay"));
		    	  $$("#userCouponName").html(userCouponNameStr);
		    	  $$("#orderPayStr").html(sessionStorage.getItem("order_pay") + "元");
		    	  
//		    	  $$("#userCouponName").html(nums + "张可用")
		      }
		});
	}
	
	$$("#orderPayStr").html(sessionStorage.getItem("order_pay") + "元");
	/*
	 * 提交订单
	 */
	$$("#orderHourSubmit").click(function() {
		
		
		// 表单校验不通过，不能提交
		if (formValidation() == false) {
			return false;
		}
		
		var params = {};
		params.user_id = $$("#userId").val();
		params.mobile =  localStorage['user_mobile'];
		params.service_type = $$("#serviceType").val();
		params.service_date = $$("#serviceDate").val();
		params.serviceHour = sessionStorage.getItem("total_service_hour");
		params.addr_id = $$("#orderDeepAddrId").val();
		params.service_addons_datas = sessionStorage.getItem("service_addons_json");
		params.remarks = $$("#remarks").val();
		params.order_from = $$("#orderFrom").val();
		
		var staffId = sessionStorage.getItem("staff_id");
		if (staffId == undefined || staffId == "" || staffId == null) {
			staffId = 0;
		}
		params.staff_id = staffId;
		
		console.log(params);

		$$.ajax({
			type : "post",
			url : siteAPIPath + "order/post_exp.json",
			data : params,
			success : function(data, status, xhr) {
				
				var result = JSON.parse(data);
				
				if (result.status == "999") {
					myApp.alert(result.msg);
					return;
				}

				sessionStorage.setItem('order_no', result.data.order_no);
				sessionStorage.setItem('order_id', result.data.id);
				/*
				 * 提交 校验通过后，，清空当前页面回显的数据
				 */
				sessionStorage.removeItem("service_date");
				sessionStorage.removeItem("service_dateStr");
				sessionStorage.removeItem("addr_id");
				sessionStorage.removeItem("addr_name");
				
				myApp.formDeleteData("orderHour-Form");
				
				/*
				 * 此处 逻辑： 如果 响应 成功 。 不管 是否 成功分配阿姨，都跳转到 支付 页面。 在 进行 支付
				 * 操作的时候，再给出提示，是否有可以服务的阿姨
				 */
				var successUrl = "order/order-pay.html";
				
				mainView.router.loadPage(successUrl);
				
			},
			error : function(status, xhr) {
				myApp.alert("网络异常,请稍后再试.");
			}
		});
	});
});

// 表单校验
function formValidation() {
	var formDatas = myApp.formToJSON('#orderHour-Form');
	
	//校验服务时间是否正确
	var serviceDate = formDatas.serviceDate;
	var now = moment().unix();
	if (serviceDate == "" || serviceDate == 0 || serviceDate <= now) {
		myApp.alert("现在时间：" + moment().format("YYYY-MM-DD HH:MM ") + "\r\n" + "请选择合适的服务时间");
		return false;
	}
	
	//校验服务地址是否为空
	var addrId = $$("#orderDeepAddrId").val;
	if (addrId == 0 || addrId == "" || addrId == undefined) {
		alert("请选择服务地址");
		return false;
	}
	
	return true;
};myApp.onPageBeforeInit('order-deep-intro', function(page) {
	
	removeSessionData();
	
	var serviceTypeId = page.query.service_type_id;

	sessionStorage.setItem("service_type_id",serviceTypeId);

	function getData(serviceTypeId){
		$$.get("order/deepIntro/order-deep-intro-"+serviceTypeId+".html",function(data){
			$$("#serviceTypeIntroDiv").html(data);
		});
	}

	switch (parseInt(serviceTypeId)){
		case 34: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/chuangpuchuman/banner-34.png"); break;
		case 35: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/chuweixiaodu/banner-35.png"); break;
		case 36: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/youyanji/banner-36.png"); break;
		case 50: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/bingxiang/banner-50.png"); break;
		case 51: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/kongtiaoqingxi/banner-51.png"); break;
		case 52: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/dibandala/banner-52.png"); break;
		case 53: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/fangwu/banner-53.png"); break;
		case 54: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/caboli/banner-54.png"); break;
		case 56: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/kaihuang/banner-56.png"); break;
		case 60: getData(serviceTypeId);$$("#order-deep-img").attr("src","img/xiyiji/banner-60.png"); break;
	}


	var result="";

	if(serviceTypeId==undefined || serviceTypeId=="" || serviceTypeId==null) return ;
	//获取服务子类信息
	$$.ajax({
		type:"GET",
		url:siteAPIPath+"dict/get_service_type_addons.json?service_type_id="+serviceTypeId,
		dataType:"json",
		async:false,
		success:function(data){
			var _data=data.data;
			for(var i=0;i<_data.length;i++){
				var name =_data[i].name;
				if(name!="金牌保洁"){
					result+="<ul class='shendu-bx-xd11'>" +
					"<li>"+_data[i].name+"</li>"+
					"<li>"+_data[i].price+"/"+_data[i].item_unit+"</li>"+
					"<li>"+_data[i].dis_price+"/"+_data[i].item_unit+"</li></ul>"
				}
			}
		}
	});

	$$("#serviceAddons").append(result);
	
	$$("#order-deep-click").on("click", function() {
		var url = "order/order-deep-choose.html?service_type_id="+serviceTypeId;
		mainView.router.loadPage(url);
	});
	

});

;myApp.onPageInit('order-hour-choose', function(page) {
	
	
	var userId = localStorage['user_id'];
	getUserInfo();
	
	var serviceTypeId = page.query.service_type_id;
	sessionStorage.setItem("service_type_id", serviceTypeId);
		
	//获取服务类别基本信息
	var serviceTypeId = sessionStorage.getItem("service_type_id");
	$$("#serviceType").val(serviceTypeId);
	$$.ajax({
	      type : "GET",
	      url: siteAPIPath+"dict/get_service_type.json?service_type_id="+serviceTypeId,
	      dataType: "json",
	      cache : true,
	      async : false,
	      success: function(data) {
	    	var serviceType = data.data;
	    	console.log(serviceType);
	    	if (serviceType == undefined || serviceType == "") {
	    		return false;
	    	}
	    	console.log(serviceType.name);
	    	
	    	$$("#price").val(serviceType.price);
	    	$$("#mprice").val(serviceType.mprice);
	    	$$("#pprice").val(serviceType.pprice);
	    	$$("#mpprice").val(serviceType.mpprice);
	    	
	    	var isVip = localStorage['is_vip'];
	    	if (isVip == undefined || isVip == "") isVip = 0;
	    	
	    	if (isVip == 1) $$("#orderHourPayStr").html(serviceType.mpprice + "元");
	      }
	});
	
	var maxServiceHour = 6;
	var minServiceHour = 3;
	
	if (serviceTypeId == 29) {
		minServiceHour = 2;
	}
	$$("#maxServiceHour").val(maxServiceHour);
	$$("#minServiceHour").val(minServiceHour);
	$$("#serviceHours").val(minServiceHour);
	
	sessionStorage.setItem("order_type", 0);
	
	// 地址选择处理，1. 是否有默认地址， 2. 是否有选择的地址（最优先）
	var addrId = getItemAddrId();
	var addrName = getItemAddrName();
	
	if (addrId != undefined && addrId != "") {
		$$("#addrId").val(addrId);
	}
	
	if (userId == 4953) {
		myApp.alert("addrId = " + addrId);
		myApp.alert("addrID val = " + $$("#addrId").val() );
	}
	
	if (addrName != undefined && addrName != "") {
		$$("#orderChooseAddrName").html(addrName);
	}
	
	initOrderHour();
	
	 $$("#chooseServiceTime").on("click",function(){
		 
		 var checkOrderHour = setOrderHourTotal();
		 
		 if (checkOrderHour == false) return false;
		 
		var addrId = $$("#addrId").val();
		if (addrId == undefined || addrId == "" || addrId == 0) {
			myApp.alert("请选择地址.");
			return false;	
		}

		 var url = "order/order-lib-cal.html?next_url=order/order-hour-confirm.html"
		 
		 var staffId = sessionStorage.getItem("staff_id");
		 if (staffId != undefined && staffId != "" && staffId != null) {
			 url+="?staff_id="+staffId;
		 }
		 
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
		$$("#staffNums").val(1);
		return false;
	}
	
	if (staffNums > 5) {
		alert("预约服务人员数量最多可以指定5人.");
		$$("#staffNums").val(5);
		return false;
	}
	
	var minServiceHour = $$("#minServiceHour").val();
	var maxServiceHour = $$("#maxServiceHour").val();
	if (serviceHours == undefined || serviceHours == "" || serviceHours < minServiceHour) {
		alert("预约服务时间最少为"+ minServiceHour +"小时.");
		
		$$("#serviceHours").val(minServiceHour);
		return false;
	}
	
	if (serviceHours > maxServiceHour) {
		alert("预约服务时间最多为"+maxServiceHour+"小时.");
		$$("#serviceHours").val(maxServiceHour);
		return false;
	}
	
	var price = $$("#price").val();
	var mprice = $$("#mprice").val();
	var pprice = $$("#pprice").val();
	var mpprice = $$("#mpprice").val();
	var isVip = localStorage['is_vip'];
	if (isVip == undefined || isVip == "") isVip = 0;
	console.log("is_vip ==" + isVip);
	var orderHourPay = pprice;
	var orderHourPrice = price;
	if (isVip == 1) {
		orderHourPay = mpprice;
		orderHourPrice = mprice;
	}
	
	if (staffNums > 1 || serviceHours > minServiceHour) {
		orderHourPay = orderHourPrice * serviceHours * staffNums;
	}
	
	$$("#orderHourPayStr").html(orderHourPay + "元");
	
	sessionStorage.setItem("order_money", orderHourPay);
	sessionStorage.setItem("order_pay", orderHourPay);
	sessionStorage.setItem("total_staff_nums", staffNums);
	sessionStorage.setItem("total_service_hour", serviceHours);
	
	console.log("orderHourPay = " + orderHourPay);
	return true;
}


function initOrderHour() {
	var staffNums = sessionStorage.getItem("total_staff_nums");
	if (staffNums == undefined || staffNums == "" || staffNums <= 0) return false;
	$$("#staffNums").val(staffNums);
	
	var minServiceHour = $$("#minServiceHour").val();
	var maxServiceHour = $$("#maxServiceHour").val();
	
	var serviceHours = sessionStorage.getItem("total_service_hour");
	if (serviceHours == undefined || serviceHours == "" || serviceHours < minServiceHour) return false;
	$$("#serviceHours").val(serviceHours);
	
	var orderHourPay = sessionStorage.getItem("order_pay");
	if (orderHourPay == undefined || orderHourPay == "") return false;
	$$("#orderHourPayStr").html(orderHourPay + "元");
};myApp.onPageBeforeInit('order-hour-confirm', function(page) {
	
	// payOrderType 订单支付类型 0 = 订单支付 1= 充值支付 2 = 手机话费类充值 3 = 订单补差价
	sessionStorage.setItem("pay_order_type", 0);
	
	var orderType = sessionStorage.getItem("order_type")
	$$("#orderType").val(orderType);
	
	//获取服务类别基本信息
	var serviceTypeId = sessionStorage.getItem("service_type_id");
	$$("#serviceType").val(serviceTypeId);
	$$.ajax({
	      type : "GET",
	      url: siteAPIPath+"dict/get_service_type.json?service_type_id="+serviceTypeId,
	      dataType: "json",
	      cache : true,
	      async : false,
	      success: function(data) {
	    	var serviceType = data.data;
	    	console.log(serviceType);
	    	if (serviceType == undefined || serviceType == "") {
	    		return false;
	    	}
	    	console.log(serviceType.name);
	    	$$("#serviceTypeName").html(serviceType.name);
	    	$$("#serviceContent").val(serviceType.name);
	      }
	});
	
	
	var userId = localStorage['user_id'];
	$$("#userId").val(userId);
	
	// 地址选择处理，1. 是否有默认地址， 2. 是否有选择的地址（最优先）
	var addrId = getItemAddrId();
	var addrName = getItemAddrName();
	
	if (addrId != undefined && addrId != "" && addrId != 0) {
		$$("#orderHourAddrId").val(addrId);
	}
	if (addrName != undefined && addrName != "") {
		$$("#orderHourAddrName").html(addrName);
	}
	
	if (userId == 4953) {
		myApp.alert("addrId = " + addrId);
		myApp.alert("oHAddrId val = " + $$("#orderHourAddrId").val() );
		myApp.alert("session addr_id = " + sessionStorage.getItem('addr_id'));
	}
	
	//设置服务时间.
	if (sessionStorage.getItem('service_date') != null) {
		$$("#serviceDate").val(sessionStorage.getItem('service_date'));
	}
	
	if (sessionStorage.getItem('service_date_str') != null) {
		$$("#serviceDateStr").html(sessionStorage.getItem('service_date_str'));
	}
	
	//设置服务人数和服务小时数
	var staffNums = sessionStorage.getItem('total_staff_nums');
	var serviceHours = sessionStorage.getItem('total_service_hour');
	var orderHourPay = sessionStorage.getItem('order_pay');
	var orderHourMoney = sessionStorage.getItem('order_money');
	$$("#staffNums").val(staffNums);
	$$("#serviceHour").val(serviceHours);
	$$("#serviceHourStr").html(serviceHours + "小时");
	$$("#orderMoney").val(orderHourMoney);
	$$("#priceStr").html(orderHourMoney);
	$$("#orderMoneyStr").html(orderHourMoney + "元");
	
	$$("#isMultiStr").html(staffNums + "位服务人员")
	
	//设置优惠劵
	if (sessionStorage.getItem('user_coupon_id') != null) {
		$$("#userCouponId").val(sessionStorage.getItem('user_coupon_id'));
		
		if (sessionStorage.getItem('user_coupon_name') != null) {
			$$("#userCouponName").html(sessionStorage.getItem('user_coupon_name'));
		}

		if (sessionStorage.getItem('user_coupon_value') != null) {
			var userCouponValue = sessionStorage.getItem('user_coupon_value');
			if(userCouponValue==undefined || userCouponValue=="" ||userCouponValue==null){
				userCouponValue=0;
			}
			$$("#userCouponValue").val(userCouponValue);
			console.log("userCouponValue = " + $$("#userCouponValue").val())
			var orderPayStr = $$("#orderMoney").val() - userCouponValue;
			if (orderPayStr < 0) orderPayStr = 0;
			sessionStorage.setItem("order_pay", orderPayStr);
		}
	} else {
		//读取用户可用的优惠劵
		var params = {};
		params.user_id = $$("#userId").val();
		params.service_type = $$("#serviceType").val();
		params.service_date = sessionStorage.getItem('service_date');
		params.order_money = $$("#orderMoney").val();
		
		console.log(params);
		$$.ajax({
		      type : "GET",
		      url: siteAPIPath+"user/get_validate_coupons.json",
		      dataType: "json",
		      cache : true,
		      data : params,
		      async : true,
		      success: function(data) {
		    	  var couponList = data.data;
		    	  var nums = 0;
		    	  if (couponList == undefined || couponList == "") {
		    		  nums = 0;
		    	  } else {
		    		  nums = couponList.length;
		    	  }
		    	  
		    	  //如果有优惠劵，则默认选择最大面值的优惠劵.
		    	  var userCouponValue = 0;
	    		  var userCouponId = 0;
	    		  var userCouponName = "";
	    		  
		    	  if (nums > 0) {
		    		 
		    		  $$.each(couponList, function(i, item) {
		    			  if (item.value > userCouponValue) {
		    				  userCouponValue = item.value;
		    				  userCouponId = item.id;
		    				  userCouponName = "￥" + userCouponValue;
		    			  }
		    		  });
		    		  
		    		  sessionStorage.setItem("user_coupon_id", userCouponId);
	    			  sessionStorage.setItem("user_coupon_name", userCouponName);
	    			  sessionStorage.setItem("user_coupon_value", userCouponValue);
		    	  }
		    	  
		    	  var userCouponNameStr = nums + "张可用";
		    	  if (userCouponId != 0 && userCouponValue != 0) {
		    		  var orderPayStr = $$("#orderMoney").val() - userCouponValue;
					  if (orderPayStr < 0) orderPayStr = 0;
					  sessionStorage.setItem("order_pay", orderPayStr);
					  userCouponNameStr = userCouponName;
		    	  }
		    	  
		    	  console.log("order_pay = " + sessionStorage.getItem("order_pay"));
		    	  $$("#userCouponName").html(userCouponNameStr);
		    	  $$("#orderHourPayStr").html(sessionStorage.getItem("order_pay") + "元");
		      }
		});
	}
	$$("#orderHourPayStr").html(sessionStorage.getItem("order_pay") + "元");
	/*
	 * 提交订单
	 */
	$$("#orderHourSubmit").click(function() {
		
		
		// 表单校验不通过，不能提交
		if (formValidation() == false) {
			return false;
		}
		
		var addrId = $$("#orderHourAddrId").val();
		if (addrId == undefined || addrId == "" || addrId == 0) {
			alert("请选择地址.");
			return false;
		}
			
		
		var params = {};
		params.userId = $$("#userId").val();
		params.serviceType = $$("#serviceType").val();
		params.serviceContent = $$("#serviceContent").val();
		params.serviceDate = $$("#serviceDate").val();
		params.addrId = addrId;
		params.staffNums = $$("#staffNums").val();
		params.serviceHour = $$("#serviceHour").val();
		params.remarks = $$("#remarks").val();
		params.orderFrom = $$("#orderFrom").val();
		
		var staffId = sessionStorage.getItem("staff_id");
		if (staffId == undefined || staffId == "" || staffId == null) {
			staffId = 0;
		}
		params.staff_id = staffId;
		
		console.log(params);

		$$.ajax({
			type : "post",
			url : siteAPIPath + "order/post_hour.json",
			data : params,
			success : function(data, status, xhr) {
				
				var result = JSON.parse(data);
				
				if (result.status == "999") {
					myApp.alert(result.msg);
					return;
				}

				sessionStorage.setItem('order_no', result.data.order_no);
				sessionStorage.setItem('order_id', result.data.id);
				/*
				 * 提交 校验通过后，，清空当前页面回显的数据
				 */
				sessionStorage.removeItem("service_date");
				sessionStorage.removeItem("service_dateStr");
				sessionStorage.removeItem("addr_id");
				sessionStorage.removeItem("addr_name");
				
				myApp.formDeleteData("orderHour-Form");
				
				/*
				 * 此处 逻辑： 如果 响应 成功 。 不管 是否 成功分配阿姨，都跳转到 支付 页面。 在 进行 支付
				 * 操作的时候，再给出提示，是否有可以服务的阿姨
				 */
				var successUrl = "order/order-pay.html";
				
				mainView.router.loadPage(successUrl);
				
			},
			error : function(status, xhr) {
				myApp.alert("网络异常,请稍后再试.");
			}
		});
	});
});

// 表单校验
function formValidation() {
	var formDatas = myApp.formToJSON('#orderHour-Form');
	
	//校验服务时间是否正确
	var serviceDate = formDatas.serviceDate;
	var now = moment().unix();
	if (serviceDate == "" || serviceDate == 0 || serviceDate <= now) {
		myApp.alert("现在时间：" + moment().format("YYYY-MM-DD HH:MM ") + "\r\n" + "请选择合适的服务时间");
		return false;
	}
	
	//校验服务地址是否为空
	var addrId = $$("#orderHourAddrId").val;
	if (addrId == 0 || addrId == "" || addrId == undefined) {
		alert("请选择服务地址");
		return false;
	}
	
	return true;
};myApp.onPageBeforeInit('order-hour-intro', function(page) {
	removeSessionData();
});

;/**
 * Created by hulj on 2016/10/20.
 */
myApp.onPageInit('order-lib-cal',function(page) {

    var nextUrl = page.query.next_url;
    console.log("nextUrl = " + nextUrl);

    var url=page.url;
    var staffId=url.split("staff_id=")[1];
    if(staffId==undefined || staffId==null || staffId==''){
    	staffId=0;
    }

    //获取当前日期
    var date=moment().format("YYYY-MM-DD");
    var nowDate=date;

    var weekDay=['周日','周一','周二','周三','周四','周五','周六'];
    var tempWeek=['周日','周一','周二','周三','周四','周五','周六'];
    var time=['08:00','08:30','09:00','09:30','10:00','10:30','11:00','11:30','12:00','12:30','13:00','13:30',
        '14:00','14:30','15:00','15:30','16:00','16:30','17:00','17:30','18:00'];
    var nowHour=moment().hour();
    var count=0;
    var dayTime="";
    var selectDay="#rilikongjian3-day li[class='beijingse']";

    //日历天数显示
    function getDay(cal){
        var contentDay="";
        var contentWeek="";
        if(cal==undefined || cal == null || cal =="") return ;
        var cmp=moment(cal).format("YYYY-MM-DD");
        for(var i=0;i<7;i++){
            var d = moment(cal).add(i,'days');
            var week=d.format('d');
            contentDay+="<li>"+d.format('DD')+"</li>";
            //显示今天明天
            if(cmp==date){
                if(i==0){
                    tempWeek[week]="今天";
                    if(parseInt(week)+1>6){
                        tempWeek[0]="明天";
                    }else{
                        tempWeek[parseInt(week)+1]="明天";
                    }
                }
                contentWeek+="<li>"+tempWeek[week]+"</li>"
            }else{
                contentWeek+="<li>"+weekDay[week]+"</li>"
            }
        }
        $$("#rilikongjian2-week").html(contentWeek);
        $$("#rilikongjian3-day").html(contentDay);
        $$(".rilikongjian p").text(moment(nowDate).format("YYYY"));
        $$("#rilikongjian1-month").text(moment(nowDate).format("MM"));

        $$("#rilikongjian3-day li").on("click",function(){
            selectDay = $$(this);
            $$("#rilikongjian3-day").find("li").removeClass("beijingse");
            $$("#rilikongjian3-dateTime").find("li").removeClass("hour-beijingse beijingse");
            $$("#all-button2").removeClass("all-button2").addClass("all-button11");
            $$(this).addClass("beijingse");
            tomm();
            var s=getServiceDate();
            if(getServiceDate()==date){
                if(nowHour>=16){
                    $$("#rilikongjian3-day li").removeClass("beijingse");
                    $$(selectDay).addClass("beijingse");
                    $$("#rilikongjian3-dateTime li").addClass("hour-beijingse");
                }
            }
            if(getServiceDate()<date){
            	$$("#rilikongjian3-dateTime li").addClass("hour-beijingse");
            }
            noSelectHour();
            filterServiceDate();
        });
        $$("#rilikongjian3-dateTime li").removeClass("hour-beijingse");
        filterServiceDate();
        noSelectHour();
        $$("#rilikongjian3-day").find(":first-child").addClass("beijingse");
        if(cmp==date){
            tomm(cmp);
        }
    }
    getDay(date);

    //前一天
    function getPreDay(c){
        var preDay = moment(date).add(c, 'days');
        nowDate=preDay;
        getDay(preDay);
    }

    //后一天
    function getNextDay(c){
        var afterDay = moment(date).add(c, 'days');
        nowDate=afterDay;
        getDay(afterDay);
    }

    //日历减1天
    $$("#rilikongjian1-left").click(function(){
        $$("#rilikongjian3-dateTime").find("li").removeClass("beijingse");
        $$("#all-button2").removeClass("all-button2").addClass("all-button11");
        var dy=$$(selectDay).parent().find(":first-child").text();
        if(dy==null){
        	dy = $$("#rilikongjian3-day").find("li[class='beijingse']").text();
        }
        var cmp='';
        if(dy==null ||dy==""){
            dy=moment(getServiceDate()).subtract(1, 'days');
            if(dy.format("YYYY-MM-DD")<=date) return false;
            var com_dy=moment(getServiceDate()).format("DD");
            var dy_month = dy.format("YYYY-MM");
            var com_month = moment(getServiceDate()).format("YYYY-MM");
            var ss=$$(dy).parent().find(":first-child").text();
            if(ss>com_dy && dy_month<=com_month){
            	cmp = getServiceDate();
            }else if(ss<com_dy &&dy_month<=com_month){
            	cmp = getServiceDate();
            }else{
            	cmp = moment(date).format("YYYY-MM")+"-"+dy.format("DD");
            }
        }else{
        	cmp=moment().format("YYYY-MM")+"-"+dy;
        }
        if(cmp<=date) return false;
        count--;
        getPreDay(count);
    });

    //日历加1天
    $$("#rilikongjian1-right").click(function(){
        $$("#rilikongjian3-dateTime").find("li").removeClass("beijingse");
        $$("#all-button2").removeClass("all-button2").addClass("all-button11");
        count++;
        getNextDay(count);
    });

    //遍历时间
    function getTime(){
        var dateTime='';
        for(var i=0;i<time.length;i++){
            var notSelectTime=['11:30','12:00','12:30'];
            if(time[i]==notSelectTime[0] || time[i]==notSelectTime[1] || time[i]==notSelectTime[2]){
                dateTime+="<li class='rilikongjian3-1 hour-beijingse'>"+time[i]+"</li>";
            }else{
                dateTime+="<li class='rilikongjian3-1'>"+time[i]+"</li>";
            }
        }
        $$("#rilikongjian3-dateTime").html(dateTime);
    }
    getTime();

    function noSelectHour(){
        var li=$$("#rilikongjian3-dateTime").find("li");
        for(var i=7;i<=9;i++){
            $$(li[i]).addClass("hour-beijingse");
            console.log(li[i]);
            dayTime=$$(li[i]).text();
            dayTime="";
        }
    }
    //获取当前选择的时间，如何没有选择时间默认是当前时间
    function getServiceDate(){
        var serviceDate='';
        var year = $$(".rilikongjian p").text();
        var month = $$("#rilikongjian1-month").text();
        var day = $$(selectDay).text();
        if($$(selectDay).text()==undefined ||$$(selectDay).text()=="" || $$(selectDay).text()==null){
            day=moment(date).add(count, 'days').format("DD");
        }
        var pre_li = $$(selectDay).prevAll("li");
        var after_li = $$(selectDay).nextAll("li");
        var flag1=false;
        var flag2=false;
        if(pre_li.length>0 && after_li.length>0){
            for(var i=0;i<pre_li.length;i++){
                var val = pre_li[i].innerHTML;
                if(val<day){
                    flag1=true;
                }else{
                    flag1=false;
                }
            }
            for(var j=0;j<after_li.length;j++){
                var val=after_li[j].innerHTML;
                if(val>day ||val<day){
                    flag2=true;
                }
            }
            if(flag1 && flag2){
                serviceDate=year+"-"+month+"-"+day;
            }else{
				serviceDate = moment(year+"-"+month+"-"+day).add(1,'M').format("YYYY-MM-DD");
            }
        }

        if(pre_li.length==0){
            var nextVal=$$(after_li[0]).text();
            var next5Val=$$(after_li[5]).text();
            if(nextVal>day || (nextVal<day && next5Val<day)){
                serviceDate=year+"-"+month+"-"+day;
            }else{
                serviceDate = moment(year+"-"+month+"-"+day).add(1,'M').format("YYYY-MM-DD");
            }
        }
        if(after_li.length==0){
            var preVal=$$(pre_li[0]).text();
            var pre5Val=$$(pre_li[5]).text();
            if(preVal<day && pre5Val<day){
                serviceDate=year+"-"+month+"-"+day;
            }else{
                serviceDate = moment(year+"-"+month+"-"+day).add(1,'M').format("YYYY-MM-DD");
            }
        }
        if(after_li.length==0 && pre_li.length==0){
            serviceDate=year+"-"+month+"-"+day;
        }
        return serviceDate;
    }

    $$("#rilikongjian3-dateTime li").on("click",function(){
        $$("#rilikongjian3-dateTime").find("li").removeClass("beijingse");
        var dy=$$(selectDay).text();
        $$(this).addClass("beijingse");
        if($$(this).hasClass("hour-beijingse")){
            dayTime="";
            $$("#all-button2").removeClass("all-button2").addClass("all-button11");
        }else{
            dayTime=$$(this).text();
        }
        if(''!=dayTime && dy!=null){
            $$("#all-button2").removeClass("all-button11").addClass("all-button2");
        }
    });

    /**
     *根据当前时间，判断下单可以选择的时间
     *
     * */
    function tomm(val){
    	var nyr
    	if(val==undefined ||val==null || val==''){
    		var nyr=getServiceDate();
    	}else{
    		nyr=val;
    	}
        if(nyr==moment().format("YYYY-MM-DD")){
            var lis = $$("#rilikongjian3-dateTime").find("li");

            if(nowHour>=0 && nowHour<=4){
                for(var i=0;i<=lis.length;i++){
                    if(i<2){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour>=4 && nowHour<=6){
                for(var i=0;i<=lis.length;i++){
                    if(i<4){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour==7){
                for(var i=0;i<=lis.length;i++){
                    if(i<6){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour>=8 && nowHour<=9){
                for(var i=0;i<=lis.length;i++){
                    if(i<10){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour==10){
                for(var i=0;i<=lis.length;i++){
                    if(i<12){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour==11){
                for(var i=0;i<=lis.length;i++){
                    if(i<14){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour==12){
                for(var i=0;i<=lis.length;i++){
                    if(i<16){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour==13){
                for(var i=0;i<=lis.length;i++){
                    if(i<18){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour>=14 && nowHour<=15){
                for(var i=0;i<=lis.length;i++){
                    if(i<20){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
            }
            if(nowHour>=16 && nowHour<=19){
                var d=moment().add(1,"days").format("DD");
                var lisd = $$("#rilikongjian3-day").find("li");
                for(var i=0;i<=lisd.length;i++){
                    var val = $$(lisd[i]).text();
                    if(d==val){
                        $$("#rilikongjian3-day li").removeClass("beijingse");
                        $$(lisd[i]).addClass("beijingse");

                    }
                }
            }
            if(nowHour>=20 && nowHour<=23){
//                $$(lis[0]).addClass("hour-beijingse");
                for(var i=0;i<=lis.length;i++){
                    if(i<2){
                        $$(lis[i]).addClass("hour-beijingse");
                    }
                }
                $$("#rilikongjian3-day li").removeClass("beijingse");
                $$("#rilikongjian3-day").find("li:nth-child(2)").addClass("beijingse");
            }
        }
        if(nyr>moment().format("YYYY-MM-DD")){
            $$("#rilikongjian3-dateTime").find("li").removeClass("hour-beijingse");
        }
        noSelectHour();
    }
    tomm();

    //查询服务人员的已有派工的服务时间
    if(staffId!=0 && staffId!=null && staffId!=''){
    	$$.ajax({
    		type : "GET",
    		url:siteAPIPath+"/staff/get_dispatch_dates.json?staff_id="+staffId,
    		dataType:"json",
    		success:function(data){
    			if(data.data.length>0){
    				sessionStorage.setItem("serDate",data.data);
    			}
    		}
    	});
    }

    //过滤服务人有已占有的服务时间
    function filterServiceDate(){
        var serviceDateArr = sessionStorage.getItem("serDate");
        var serviceHour = sessionStorage.getItem("total_service_hour");
        if(serviceDateArr=="" ||serviceDateArr==null ||serviceDateArr==undefined) return ;
        var d = serviceDateArr.split(",");
        for(var i=0;i<d.length;i++){
        	var sd=d[i];
        	var serviceDateAdd = moment(sd).add(serviceHour,"hours").add(2,"hours");
        	var serviceDateSub = moment(sd).subtract(serviceHour,"hours").subtract(2,"hours");
        	var date = moment(serviceDateAdd).format("YYYY-MM-DD");
        	var hour1 = moment(sd).format("HH:mm");
            var hour2 = serviceDateAdd.format("HH:mm");
            var hoursub = serviceDateSub.format("HH:mm");
            var s = d[i].split(" ");
            if(date==getServiceDate()){
                for(var i=0;i<time.length;i++){
                	var index=i;
                	if(hoursub>=time[0] && time[i]>hoursub && time[i]<=hour1){
                		 var tli = $$("#rilikongjian3-dateTime li");
                         $$(tli[index]).addClass("hour-beijingse");
                	}
                	if(hoursub<time[0] && time[i]<=hour1){
                		 var tli = $$("#rilikongjian3-dateTime li");
                         $$(tli[index]).addClass("hour-beijingse");
                	}
                    if(time[i]>=hour1 && time[i]<=hour2){
                       var tli = $$("#rilikongjian3-dateTime li");
                       $$(tli[index]).addClass("hour-beijingse");
                    }
                }
            }

        }
    }

    //获取选择的服务时间
    $$("#all-button2").click(function(){
        var st = getServiceDate()+" "+dayTime+":00";
        if(dayTime!=""){
            console.log("serviceTime = " + st)
            sessionStorage.setItem('service_date_str',getServiceDate()+"("+ weekDay[moment(getServiceDate()).format("d")]+")"+dayTime);
            var serviceDate = moment(st).unix();
            sessionStorage.setItem('service_date', serviceDate);
            mainView.router.loadPage(nextUrl);
        }else{
            return;
        }
    });

});
;myApp.onPageBeforeInit('order-list', function(page) {
	
	removeSessionData();
	
	var userId = localStorage['user_id'];
	var loading = false;// 加载flag
	var page = $$("#page").val();
	
	var orderListSuccess = function(data, textStatus, jqXHR) {

		var result = JSON.parse(data.response);
		var orders = result.data;

		var htmlTemplate = $$('#order-list-part').html();

		var html = ''; // 当前订单

		for (var i = 0; i < orders.length; i++) {
			var order = orders[i];
			var htmlPart = htmlTemplate;
			htmlPart = htmlPart.replace(new RegExp('{orderType}', "gm"), order.order_type);
			htmlPart = htmlPart.replace(new RegExp('{orderId}', "gm"), order.id);
			htmlPart = htmlPart.replace(new RegExp('{orderNo}', "gm"), order.order_no);
			htmlPart = htmlPart.replace(new RegExp('{serviceTypeId}', "gm"), order.service_type);
			htmlPart = htmlPart.replace(new RegExp('{orderPay}', "gm"), order.order_pay);
			
			htmlPart = htmlPart.replace(new RegExp('{serviceTypeName}', "gm"), order.service_type_name);
			htmlPart = htmlPart.replace(new RegExp('{orderStatusStr}', "gm"), order.order_status_name);
			htmlPart = htmlPart.replace(new RegExp('{serviceDateStr}', "gm"), order.service_date_str);
			htmlPart = htmlPart.replace(new RegExp('{addrName}', "gm"), order.address);
			htmlPart = htmlPart.replace(new RegExp('{orderPayStr}', "gm"), order.order_pay + "元");
			
			console.log("order.coupon_id = " + order.coupon_id);
			
			htmlPart = htmlPart.replace(new RegExp('{userCouponId}', "gm"), order.coupon_id);
			htmlPart = htmlPart.replace(new RegExp('{userCouponValue}', "gm"), order.coupon_value);
			htmlPart = htmlPart.replace(new RegExp('{orderStatus}', "gm"), order.order_status);
			
			htmlPart = htmlPart.replace(new RegExp('{staffNames}', "gm"), order.staff_names);
			
			var orderStatus = order.order_status;
			console.log("orderStatus = " + orderStatus);
			//如果未支付，则显示去支付按钮.
			var doOrderPayStyle = 'none';
			if (orderStatus == 1) {
				doOrderPayStyle = 'block';
			}
			htmlPart = htmlPart.replace(new RegExp('{doOrderPayStyle}', "gm"), doOrderPayStyle);
			
			//1.必须为已支付的订单，未完成服务的订单可以补差价
			//2.现金支付不能有补差价功能.
			var payType = order.pay_type;
			var priceExtendStyle = 'none';
			if (orderStatus >= 3 && orderStatus < 7 && payType != 6) {
				priceExtendStyle = 'block';
			}

			htmlPart = htmlPart.replace(new RegExp('{priceExtendStyle}', "gm"), priceExtendStyle);
			
			var orderRateStr = "立即评价";
			var orderRateStyle = "none"
			if (orderStatus >= 3 && orderStatus <= 8) {
				orderRateStyle = "block";
			}
			if (orderStatus == 8) orderRateStr = "已评价";
			htmlPart = htmlPart.replace(new RegExp('{orderRateStr}', "gm"), orderRateStr);
			htmlPart = htmlPart.replace(new RegExp('{orderRateStyle}', "gm"), orderRateStyle);
			
			html+= htmlPart;
		}
		// 当前订单
		if (page == 1) {
			$$("#order-list-div").html(html);
		} else {
			$$("#order-list-div").append(html);
		}

		loading = false;
		
		$$("#page").val(page);
		console.log("page = " + page);
		console.log("len = " + orders.length);
		if (orders.length >= 10) {
			console.log("order-list-more block");
			$$('#order-list-more').css("display", "block");
		} else {
			$$('#order-list-more').css("display", "none");
		}
	};
	
	
	function loadOrderList(userId, page) {
		console.log("page = " + page);
		var postdata = {};
		
		var apiUrl = "order/user_order_list.json";
		postdata.user_id = userId;
		postdata.page = page;

		$$.ajax({
			type : "GET",
			url : siteAPIPath + apiUrl,
			dataType : "json",
			cache : true,
			data : postdata,
			statusCode : {
				200 : orderListSuccess,
				400 : ajaxError,
				500 : ajaxError
			},

		});
	}	
		
	// 注册'infinite'事件处理函数
	$$('#order-list-more').on('click', function() {
		var cpage = ++page;
		loadOrderList(userId, cpage);
	});
	
	
	
	loadOrderList(userId, page);
});

function orderView(orderType, orderNo) {
	var url = "";

	if (orderType == 0) url = "order/order-view-0.html";
	if (orderType == 1) url = "order/order-view-1.html";
	
	sessionStorage.setItem("order_type", orderType);
	sessionStorage.setItem("order_no", orderNo);
	
	mainView.router.loadPage(url);
}

function doOrderPay(obj) {
	
	var orderStatus = obj.find('input[name=orderStatus]').val();
	
	if (orderStatus != 1) return false;
	
	var serviceTypeId = obj.find('input[name=serviceTypeId]').val();
	var orderId = obj.find('input[name=orderId]').val();
	var orderNo = obj.find('input[name=orderNo]').val();
	var orderPay = obj.find('input[name=orderPay]').val();
	var userCouponId = obj.find('input[name=userCouponId]').val();
	var userCouponValue = obj.find('input[name=userCouponValue]').val();
	
	sessionStorage.setItem("service_type_id", serviceTypeId);
	sessionStorage.setItem("order_id", orderId);
	sessionStorage.setItem("order_no", orderNo);
	sessionStorage.setItem("order_pay", orderPay);
	sessionStorage.setItem("user_coupon_id", userCouponId);
	sessionStorage.setItem("user_coupon_value", userCouponValue);
	
	// 订单类型 0 = 钟点工 1 = 深度保洁 2 = 助理订单 6= 话费充值类订单 7 = 订单补差价
	sessionStorage.setItem("pay_order_type", 0);
	
	mainView.router.loadPage("order/order-pay.html");
	
}

function doOrderPayExt(obj) {
	
	var orderStatus = obj.find('input[name=orderStatus]').val();
	
	if (orderStatus >= 3 && orderStatus < 7) {
	
		var serviceTypeId = obj.find('input[name=serviceTypeId]').val();
		var orderId = obj.find('input[name=orderId]').val();
		var orderNo = obj.find('input[name=orderNo]').val();
		var orderPay = obj.find('input[name=orderPay]').val();
		var staffNames = obj.find('input[name=staffNames]').val();
		
		sessionStorage.setItem("service_type_id", serviceTypeId);
		sessionStorage.setItem("order_id", orderId);
		sessionStorage.setItem("order_no", orderNo);
		sessionStorage.setItem("order_pay", orderPay);
		sessionStorage.setItem("staff_names", staffNames);
		
		console.log("staffNames = " + sessionStorage.getItem("staff_names"));
		
		mainView.router.loadPage("order/order-pay-ext.html");
	}
	
}


function linkOrderRate(obj) {
	
	var orderStatus = obj.find('input[name=orderStatus]').val();
	
	if (orderStatus <= 2) return false;
	
	
	var orderId = obj.find('input[name=orderId]').val();
	sessionStorage.setItem("order_id", orderId);
	
	var orderNo = obj.find('input[name=orderNo]').val();
	sessionStorage.setItem("order_no", orderNo);
	
	var orderType = obj.find('input[name=orderType]').val();
	sessionStorage.setItem("order_type", orderType);
		
	var staffNames = obj.find('input[name=staffNames]').val();
	sessionStorage.setItem("staff_names", staffNames);
	
	var orderRateUrl = "order/order-rate.html";
	if (orderStatus == 8) {
		orderRateUrl = "order/order-user-rate.html";
	}
	
	mainView.router.loadPage(orderRateUrl);
}


;myApp.onPageBeforeInit('order-pay-ext', function(page) {
	
	var userId = localStorage['user_id'];

	// 点击支付的处理
	$$("#orderPriceExtSumbit").click(function(){
		
		var orderPayExt = $$("#orderPayExt").val();
		
		if (orderPayExt == "" || orderPayExt == 0 || orderPayExt == undefined) {
			myApp.alert("请输入金额.");
			return false;
		}
		
		// payOrderType 订单支付类型 0 = 订单支付 1= 充值支付 2 = 手机话费类充值 3 = 订单补差价
		sessionStorage.setItem("pay_order_type", 3);
		sessionStorage.setItem("order_pay", orderPayExt);
		mainView.router.loadPage("order/order-pay.html");
	});
});


//列表显示，获取用户的信息
myApp.template7Data['page:order-pay-ext']=function(){
	var result = {};
	var staffNames = sessionStorage.getItem("staff_names");
	result.staffNames = staffNames;
	return result;
}


;myApp.onPageInit('order-pay-success', function(page) {
	console.log("order-pay-success-page");
	var orderNo = page.query.order_no;
	
	var orderType = page.query.order_type;
	
	var serviceTypeId = page.query.service_type_id;
	
	if (sessionStorage.getItem('service_type_id') != "") {
		serviceTypeId = sessionStorage.getItem('service_type_id');
	}

	var recoList = orderRecomment(serviceTypeId);
	
	console.log(recoList);
	if (recoList != undefined && recoList != "") {
		var htmlStr = "";
		$$.each(recoList, function(i, item) {
			console.log(item.name);
			htmlStr+='<a href="'+item.url+'" class="special-color2"><span>'+item.name+'</span></a>';
		});
		
		$$("#recoList").append(htmlStr);
	}
	
	
	
});


;myApp.onPageInit('order-pay', function(page) {
		
	var userId = localStorage['user_id'];
	var serviceTypeId = sessionStorage.getItem('service_type_id');
	var orderNo = sessionStorage.getItem('order_no');
	var orderId = sessionStorage.getItem('order_id');
	var orderPay = sessionStorage.getItem('order_pay');
	var userCouponId = sessionStorage.getItem('user_coupon_id');
	var userCouponValue = sessionStorage.getItem('user_coupon_value');
	if (userCouponValue == undefined || userCouponValue == "" || userCouponValue == null) {
		userCouponValue = 0;
	}
	
	// payOrderType 订单支付类型 0 = 订单支付 1= 充值支付 2 = 手机话费类充值 3 = 订单补差价
	var payOrderType = sessionStorage.getItem("pay_order_type");
	
	
	$$("#userId").val(userId);
	$$("#orderNo").val(orderNo);
	$$("#orderId").val(orderId);
	$$("#orderPay").val(orderPay);
	$$("#orderMoneyStrLi").html("￥"+orderPay+"元");
	$$("#orderPayStrLi").html("￥"+orderPay+"元");
	$$("#userCouponId").val(userCouponId);
	$$("#userCouponValue").val(userCouponValue);
	$$("#userCouponValueStr").html(userCouponValue + "元");
		
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "user/get_userinfo.json?user_id="+userId,
		dataType : "json",
		cache : true,
		async : false,
		success : function(data) {
			var restMoney = data.data.rest_money;
			$$("#restMoney").val(restMoney);
			$$("#restMoneyStr").html("余额"+restMoney+"元");
		}
	})
	
	//默认支付类型
	var orderPayType = 0;
	
	var isWx = isWeiXin();
	
	if (isWx) {
		$$("#select-wxpay").css("display", "block");
		$$("#select-alipay").css("display", "none");
		$$('#img-restpay').attr("src","img/dingdan-pay/dingdan-pay2.png");
		$$('#img-alipay').attr("src","img/dingdan-pay/dingdan-pay2.png");
		$$('#img-wxpay').attr("src","img/dingdan-pay/dingdan-pay1.png");
		$$("#orderPayType").val(2);
	} else  {
		$$("#select-wxpay").css("display", "none");
		$$("#select-alipay").css("display", "block");
		$$('#img-restpay').attr("src","img/dingdan-pay/dingdan-pay2.png");
		$$('#img-wxpay').attr("src","img/dingdan-pay/dingdan-pay2.png");
		$$('#img-alipay').attr("src","img/dingdan-pay/dingdan-pay1.png");
		$$("#orderPayType").val(1);
	}
	
	var postOrderPaySuccess =function(data, textStatus, jqXHR) {
		$$("#orderPaySubmit").removeAttr('disabled');  
	 	var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}

		orderPayType = result.data.pay_type;
		orderType = result.data.order_type;
		serviceTypeId = result.data.service_type;
		
		//补差价需要单独处理订单ID 和 订单编号
		if (payOrderType == 3) {
			orderNo = result.data.order_no_ext;
			orderId = result.data.id;
			
			sessionStorage.setItem('order_id_ext', orderId);
			sessionStorage.setItem('order_no_ext', orderNo);
		}
		
		console.log("orderPayType = " + orderPayType);
		
		//如果为余额支付或者 现金支付，则直接跳到完成页面
		if (orderPayType == 0) {
			//支付成功之后，把优惠劵的信息清空.
			sessionStorage.removeItem("user_coupon_id");
			sessionStorage.removeItem("user_coupon_value");
			sessionStorage.removeItem("user_coupon_name");
			mainView.router.loadPage("order/order-pay-success.html?service_type_id="+serviceTypeId);
		}
		
		
		//如果为支付宝支付，则跳转到支付宝手机网页支付页面
		if (orderPayType == 1) {
			var orderPay = result.data.order_pay;
			var alipayUrl = localUrl + "/" + appName + "/pay/alipay_order_api.jsp";
			alipayUrl +="?orderNo="+orderNo;
			alipayUrl +="&orderPay="+orderPay;
			alipayUrl +="&orderType="+orderType;
			alipayUrl +="&serviceTypeId="+serviceTypeId;
			alipayUrl +="&payOrderType="+payOrderType;
			location.href = alipayUrl;
		}
		
		//如果为微信支付，则需要跳转到微信支付页面.
		if (orderPayType == 2) {
			 var userCouponId = $$("#userCouponId").val();
			 if (userCouponId == undefined) userCouponId = 0;
			 var wxPayUrl = localUrl + "/" + appName + "/wx-pay-pre.jsp";
			 wxPayUrl +="?orderId="+orderId;
			 wxPayUrl +="&userCouponId="+userCouponId;
			 wxPayUrl +="&orderType=0";
			 wxPayUrl +="&payOrderType="+payOrderType;
			 wxPayUrl +="&serviceTypeId="+serviceTypeId;
			 location.href = wxPayUrl;
		}
	};
	
	
	function doOrderPay() {
		var params = {};
		params.user_id = userId;
		params.order_no = orderNo;
		var userCouponId = $$("#userCouponId").val();
		if (userCouponId == undefined) userCouponId = 0;
		params.user_coupon_id = userCouponId;
		params.order_pay_type = $$("#orderPayType").val();
		
		console.log(params);

		
		$$.ajax({
			type: "post",
			 url: siteAPIPath + "order/post_pay.json",
			data: params,
			statusCode: {
	         	200: postOrderPaySuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});
	}
	
	function doOrderPayExt() {
		var params = {};
		params.user_id = userId;
		params.order_no = orderNo;
		params.order_pay_ext = orderPay;
		params.order_pay_type = $$("#orderPayType").val();
		
		console.log(params);

		$$.ajax({
			type: "post",
			 url: siteAPIPath + "order/post_pay_ext.json",
			data: params,
			statusCode: {
	         	200: postOrderPaySuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});
	}
	
	//点击支付的处理
	$$("#orderPaySubmit").click(function(){
		$$("#orderPaySubmit").attr("disabled", true);
		
		if (payOrderType == 0) {
			doOrderPay();
		}
		
		//补差价订单
		if (payOrderType == 3) {
			doOrderPayExt();
		}
	});
	
});

function changePayType(imgPayType, orderPayType) {
	
	$$("#orderPayType").val(orderPayType);
	var imgPayTypes = ['img-restpay', 'img-wxpay', 'img-alipay'];
	
	$$.each(imgPayTypes,function(n,value) {  
		console.log("value = " + value + "=== imgPayType=" + imgPayType);
		if (value == imgPayType) {
			$$('#' + value).attr("src","img/dingdan-pay/dingdan-pay1.png");
		} else {
			$$('#' + value).attr("src","img/dingdan-pay/dingdan-pay2.png");
		}
	});
};myApp.onPageBeforeInit('order-rate-staff', function(page) {
	
	var userId = localStorage['user_id'];
	
	var staffId = page.query.staff_id;
	

	
	var orderStaffRateSuccess = function(data, textStatus, jqXHR) {
		
		var result = JSON.parse(data.response);
		var orderRates = result.data;
		
		if (orderRates == undefined || orderRates == "") return false;
		
		var htmlTemplate = $$('#staffTotalRateTemplate').html();
		
		var html = ''; // 当前订单
		
		for (var i = 0; i < orderRates.length; i++) {
			var rate = orderRates[i];
			var htmlPart = htmlTemplate;
			htmlPart = htmlPart.replace(new RegExp('{staffName}', "gm"), rate.name);
			htmlPart = htmlPart.replace(new RegExp('{age}', "gm"), rate.age);
			htmlPart = htmlPart.replace(new RegExp('{skill}', "gm"), rate.skill);
			htmlPart = htmlPart.replace(new RegExp('{hukou}', "gm"), rate.hukou);
			htmlPart = htmlPart.replace(new RegExp('{totalArrival}', "gm"), rate.total_arrival);
			htmlPart = htmlPart.replace(new RegExp('{intro}', "gm"), rate.intro);
			htmlPart = htmlPart.replace(new RegExp('{staffId}', "gm"), rate.staff_id);
			//头像
			var headImg = rate.head_img;
			var headImgHtml = '<img src="'+headImg+'" alt="">';
			htmlPart = htmlPart.replace(new RegExp('{headImg}', "gm"), headImgHtml);
			
			//客户好评度
			var totalRateStarHtml = "";
			var totalRateStar = rate.total_rate_star;
			
			if (totalRateStar > 0) {
				for (var i = 1 ; i <= 5; i++) {
					if (i <= totalRateStar) {
						totalRateStarHtml+='<img src="img/yudingayi/xx.png" alt="">';
					} else {
						totalRateStarHtml+='<img src="img/yudingayi/xx1.png" alt="">';
					}
					
				}
			}
			htmlPart = htmlPart.replace(new RegExp('{totalRateStar}', "gm"), totalRateStarHtml);
			html += htmlPart;
		}
		
		$$("#staffTotalRate").html(html);
		
	};
	
	function loadOrderStaffRate(staffId) {
		
		var postdata = {};
		
		var apiUrl = "order/get_staff_total_rate.json";
		postdata.staff_id = staffId;
		
		$$.ajax({
			type : "GET",
			url : siteAPIPath + apiUrl,
			dataType : "json",
			cache : true,
			data : postdata,
			statusCode : {
				200 : orderStaffRateSuccess,
				400 : ajaxError,
				500 : ajaxError
			},
		
		});
	}
	
	loadOrderStaffRate(staffId);
	
});

function reStaffOrder(staffId, staffName) {
	if (staffId == undefined || staffId == "" || staffId == 0) return false;
		
	sessionStorage.setItem("staff_id", staffId);
	sessionStorage.setItem("staff_names", staffName);
	mainView.router.loadPage("order/order-appoint.html");
}
;myApp.onPageBeforeInit('order-rate-success', function(page) {
	
	var userId = localStorage['user_id'];
	
	var orderId = sessionStorage.getItem("order_id");
	if(orderId==null || orderId==''||orderId==undefined){
		orderId = page.query.order_id;
	}
	
	var orderType = sessionStorage.getItem("order_type");
	
	var orderRateSuccess = function(data, textStatus, jqXHR) {
		
		var result = JSON.parse(data.response);
		var orderRates = result.data;
		
		if (orderRates == undefined || orderRates == "") return false;
		
		var htmlTemplate = $$('#staffRateTemplate').html();
		
		var html = ''; // 当前订单
		
		for (var i = 0; i < orderRates.length; i++) {
			var rate = orderRates[i];
			var htmlPart = htmlTemplate;
			htmlPart = htmlPart.replace(new RegExp('{staffName}', "gm"), rate.name);
			htmlPart = htmlPart.replace(new RegExp('{age}', "gm"), rate.age);
			htmlPart = htmlPart.replace(new RegExp('{skill}', "gm"), rate.skill);
			htmlPart = htmlPart.replace(new RegExp('{hukou}', "gm"), rate.hukou);
			htmlPart = htmlPart.replace(new RegExp('{totalArrival}', "gm"), rate.total_arrival);
			htmlPart = htmlPart.replace(new RegExp('{intro}', "gm"), rate.intro);
			htmlPart = htmlPart.replace(new RegExp('{staffId}', "gm"), rate.staff_id);
			//头像
			var headImg = rate.head_img;
			var headImgHtml = '<img src="'+headImg+'" alt="">';
			htmlPart = htmlPart.replace(new RegExp('{headImg}', "gm"), headImgHtml);
			
			//客户好评度
			var totalRateStarHtml = "";
			var totalRateStar = rate.total_rate_star;
			
			if (totalRateStar > 0) {
				for (var i = 1 ; i <= 5; i++) {
					if (i <= totalRateStar) {
						totalRateStarHtml+='<img src="img/yudingayi/xx.png" alt="">';
					} else {
						totalRateStarHtml+='<img src="img/yudingayi/xx1.png" alt="">';
					}
					
				}
			}
			htmlPart = htmlPart.replace(new RegExp('{totalRateStar}', "gm"), totalRateStarHtml);
			html += htmlPart;
		}
		
		$$("#staffRate").html(html);
		
	};
	
	function loadOrderRate(userId, orderId) {
		var staffId = page.query.staff_id;
		var postdata = {};
		
		var apiUrl = "order/get_order_rate.json";
		postdata.user_id = userId;
		postdata.order_id = orderId;
		postdata.staff_id = staffId;
		
		$$.ajax({
			type : "GET",
			url : siteAPIPath + apiUrl,
			dataType : "json",
			cache : true,
			data : postdata,
			statusCode : {
				200 : orderRateSuccess,
				400 : ajaxError,
				500 : ajaxError
			},
		
		});
	}
	
	loadOrderRate(userId, orderId);
	
});

function reOrder(staffId, staffName) {
	if (staffId == undefined || staffId == "" || staffId == 0) return false;
		
	sessionStorage.setItem("staff_id", staffId);
	sessionStorage.setItem("staff_names", staffName);
	mainView.router.loadPage("order/order-appoint.html");
}
;myApp.onPageBeforeInit('order-rate', function(page) {

	var userId = localStorage['user_id'];

	var orderId = sessionStorage.getItem("order_id");

	var staffNames = sessionStorage.getItem("staff_names");

	var orderNo = sessionStorage.getItem("order_no");

	var orderType = sessionStorage.getItem("order_type");

	console.log("orderType = " + orderType);
	var postdata = {};
	postdata.order_no = orderNo;
	if (orderType == 0) {
		$$.ajax({
			type : "GET",
			url : siteAPIPath + "order/order_hour_detail.json",
			dataType : "json",
			data : postdata,
			cache : true,
			async : false, // 不能是异步
			success : function(data) {
				console.log(data);
				var staffNameStr = "";
				list = data.data.order_dispatchs;
				console.log(list);
				$$.each(list, function(i, item){
					console.log(i);
					staffNameStr+='<a href="order/order-rate-staff.html?staff_id='+item.staff_id+'">'+item.staff_name+'</a>&nbsp;';
				});
				console.log(staffNameStr);
				$$("#staffNameStr").html(staffNameStr);
			}
		});
	} else if (orderType == 1) {
		$$.ajax({
			type : "GET",
			url : siteAPIPath + "order/get_exp_clean_order_detail.json",
			dataType : "json",
			data : postdata,
			cache : true,
			async : false, // 不能是异步
			success : function(data) {
				var staffNameStr = "";
				list = data.data.order_dispatchs;
				$$.each(list, function(i, item){
					staffNameStr+='<a href="order/order-rate-staff.html?staff_id='+item.staff_id+'">'+item.staff_name+'</a>&nbsp;';
				});
				$$("#staffNameStr").html(staffNameStr);
			}
		})
	}


	$$("#orderId").val(orderId);
//	$$("#staffNameStr").html(staffNames);

	$$("#rateAttitude").val(0);
	$$("#rateSkill").val(0);

	//最多只能输入254个字
	$$("#rateContent").keydown(function() {
		var curLength = $$("#rateContent").val().length;
		if (curLength >= 253) {
			var content = $$("#rateContent").val().substr(0, 254);
			$$("#rateContent").val(content);
		}
	});

	$$("#rateSubmit").on("click",function(){

		var rateArrival = $$("#rateArrival").val();
		var rateAttitude = $$("#rateAttitude").val();
		var rateSkill = $$("#rateSkill").val();

		if (rateArrival == "") {
			myApp.alert("请评价到达时间.");
			return false;
		}

		if (rateAttitude == "" || rateAttitude == 0) {
			myApp.alert("请评价服务态度.");
			return false;
		}

		if (rateSkill == "" || rateSkill == 0) {
			myApp.alert("请评价服务技能.");
			return false;
		}



		var params = {};
		params.user_id = userId;
		params.order_id = orderId;
		params.rate_arrival = $$("#rateArrival").val();
		params.rate_attitude = $$("#rateAttitude").val();
		params.rate_skill = $$("#rateSkill").val();

		var rateContent = $$("#rateContent").val();
		if (rateContent == undefined) rateContent = "";
		params.rate_content = rateContent;
		
//		return false;

		$$.ajax({
			type : "POST",
			url : siteAPIPath + "order/post_rate.json",
			dataType : "json",
			cache : true,
			async : false,
			data : params,
			success : function(data) {

				var s = data.status;
				if (s == "999") {
					myApp.alert(data.msg);
					return false;
				}

				mainView.router.loadPage("order/order-rate-success.html");
			}
		})
	});

//	$$("#rateImgTrigger").on("click",function(){
//		$$("#rateImgFile").trigger("click");
//	});


});

function doRateArrival(v) {
	if (v == 0) {
		$$("#rateArrival_0").removeClass("waiter1-2").addClass("waiter1-1");
		$$("#rateArrival_1").removeClass("waiter1-1").addClass("waiter1-2");
	}

	if (v == 1) {
		$$("#rateArrival_0").removeClass("waiter1-1").addClass("waiter1-2");
		$$("#rateArrival_1").removeClass("waiter1-2").addClass("waiter1-1");
	}

	$$("#rateArrival").val(v);
}

function doRateAttitude(v) {

	for (var i = 1; i <= 5; i++) {
		if (v >= i) {
			$$("#rateAttitude_" + i).attr("src", "img/yudingayi/xx.png");
		} else {
			$$("#rateAttitude_" + i).attr("src", "img/yudingayi/xx1.png");
		}
	}
	$$("#rateAttitude").val(v);
}

function doRateSkill(v) {

	for (var i = 1; i <= 5; i++) {
		if (v >= i) {
			$$("#rateSkill_" + i).attr("src", "img/yudingayi/xx.png");
		} else {
			$$("#rateSkill_" + i).attr("src", "img/yudingayi/xx1.png");
		}
	}
	$$("#rateSkill").val(v);
}

//function rateUpload() {
//	$$("#rateImgFile").focus().trigger('click');
//}


//预览图片
function setImagePreviews(obj) {
	var files = obj.files;
	if(files.length>0){
	    var hml='';
	    var index=0;
	    for(var i= 0,len=files.length;i<len;i++){
	    	var reader = new FileReader();
			var f = files[i];
			if(!/image\/\w+/.test(f.type)){
				alert("文件必须为图片！"); 
				return false;
			}
			reader.readAsDataURL(f);
			reader.onload=function(e){
				hml+="<div class='waiter4-1'><img src='"+e.target.result+"' alt=''/></div>"
				+"<div class='waiter4-2' id='delImg' onclick='delImg(this)'>×</div>";
				index++;
				if(index<=4){
					$$("#img").html(hml);
				}
			}
	    }
    }
}

//删除图片
function delImg(obj){
	var ele=$$(obj).prev();
	ele.remove();
	$$(obj).remove();
}
;myApp.onPageBeforeInit('order-user-rate', function(page) {
	
	removeSessionData();
	
	var userId = localStorage['user_id'];
	var loading = false;// 加载flag
	var page = $$("#page").val();
	
	var orderListSuccess = function(data, textStatus, jqXHR) {

		var result = JSON.parse(data.response);
		var orderRates = result.data;

		var htmlTemplate = $$('#order-user-rate-temp').html();
		var html = ''; // 当前订单

		for (var i = 0; i < orderRates.length; i++) {
			var or = orderRates[i];
			var htmlPart = htmlTemplate;
			
			if(or.staff_list!=null){
				var staffhtml="";
				var headImgHtml="";
				var staffs=or.staff_list;
				var staffLen=staffs.length;
				var orderId=or.order_id;
//				sessionStorage.setItem("order_id",or.order_id);
				for(var k=0;k<staffLen;k++){
					staffhtml +="<a href='order/order-rate-success.html?order_id="+orderId+"&staff_id="+staffs[k].staff_id+"'><span class='special-color2'>"+staffs[k].name+"</span></a>";
					if(k<2){
						headImgHtml+="<a href='order/order-rate-success.html?order_id="+orderId+"&staff_id="+staffs[k].staff_id+"'><div id='staffId-img' class='waiter10-2-1'><img src='"+staffs[k].head_img+"' alt='' /></div></a>";
					}
				}
				htmlPart = htmlPart.replace(new RegExp('{staff_name}', "gm"), staffhtml);
				htmlPart = htmlPart.replace(new RegExp('{head_img}', "gm"), headImgHtml);
			}else{
				htmlPart = htmlPart.replace(new RegExp('{staff_name}', "gm"), "");
			}
			
			htmlPart = htmlPart.replace(new RegExp('{service_type_name}', "gm"), or.service_type_name);
			htmlPart = htmlPart.replace(new RegExp('{service_date_str}', "gm"), or.service_date_str);
			if(or.rate_content!='' && or.rate_content!=null){
				htmlPart = htmlPart.replace(new RegExp('{rate_content}', "gm"), or.rate_content);
			}else{
				htmlPart = htmlPart.replace(new RegExp('{rate_content}', "gm"), "暂无评价");
			}
			
			var rateArrival="";
			if(or.rate_arrival==0){
				rateArrival="<span class='waiter10-3-1'>准时</span><span class='waiter10-3-2'>迟到</span></li>";
			}else if(or.rate_arrival==1){
				rateArrival="<span class='waiter10-3-2'>准时</span><span class='waiter10-3-1'>迟到</span></li>"
			}
			htmlPart = htmlPart.replace(new RegExp('{rate_arrival}', "gm"), rateArrival);

			//服务态度
			var rateAttitude = "";
			if (or.rate_attitude > 0) {
				for (var s = 1 ; s <= 5; s++) {
					if (s <= or.rate_attitude) {
						rateAttitude+='<img src="img/yudingayi/xx.png" alt="">';
					} else {
						rateAttitude+='<img src="img/yudingayi/xx1.png" alt="">';
					}

				}
			}
			htmlPart = htmlPart.replace(new RegExp('{rate_attitude}', "gm"), rateAttitude);
			
			//技能
			var rateSkill = "";
			if (or.rate_attitude > 0) {
				for (var o = 1 ; o <= 5; o++) {
					if (o <= or.rate_attitude) {
						rateSkill+='<img src="img/yudingayi/xx.png" alt="">';
					} else {
						rateSkill+='<img src="img/yudingayi/xx1.png" alt="">';
					}

				}
			}
			htmlPart = htmlPart.replace(new RegExp('{rate_skill}', "gm"), rateSkill);
			
			//图片
			var rateUrl="";
			if(or.order_rate_url!=null){
				var rateUrlLen=or.order_rate_url.length;
				for(var j=0;j<rateUrlLen;j++){
					if(j<4){
						rateUrl += "<div class='waiter10-4-1'><img src='"+or.order_rate_url[j]+"' alt=''></div>";
					}
				}
				htmlPart = htmlPart.replace(new RegExp('{rate_url}', "gm"), rateUrl);
			}else{
				htmlPart = htmlPart.replace(new RegExp('{rate_url}', "gm"), "");
			}
			
			html+= htmlPart;
		}
		// 当前订单
		if (page == 1) {
			$$("#order-user-rate").html(html);
		} else {
			$$("#order-user-rate").append(html);
		}

		loading = false;
		
		$$("#page").val(page);
		if (orderRates.length >= 10) {
			console.log("order-list-more block");
			$$('#order-list-more').css("display", "block");
		} else {
			$$('#order-list-more').css("display", "none");
		}
	};
	
	function loadOrderList(userId, page) {
		console.log("page = " + page);
		var postdata = {};
		
		var apiUrl = "order/get_user_rates.json";
		postdata.user_id = userId;
		postdata.page = page;

		$$.ajax({
			type : "GET",
			url : siteAPIPath + apiUrl,
			dataType : "json",
			cache : true,
			data : postdata,
			statusCode : {
				200 : orderListSuccess,
				400 : ajaxError,
				500 : ajaxError
			},

		});
	}	
		
	$$('#order-list-more').on('click', function() {
		var cpage = ++page;
		loadOrderList(userId, cpage);
	});
	
	loadOrderList(userId, page);
	
	$$('#order-user-rate .waiter10-1 #staffId').on("click",function(){
//		<a href='order/order-rate-success.html'>
		alert();
		var staffId = $$("#staff_id").val();
		sessionStorage.setItem('staff_id',staffId);
		mainView.router.loadPage("order/order-rate-success.html");
	});
	
	$$('').on("click",function(){
//		<a href='order/order-rate-success.html'>
		alert();
		var staffId = $$("#staff_id").val();
		sessionStorage.setItem('staff_id',staffId);
		mainView.router.loadPage("order/order-rate-success.html");
	});
	
	
});





;/**
 * 清除临时会话数据
 */
function removeSessionData() {
	
	//服务类别临时会话数据
	sessionStorage.removeItem("service_type_id");
	
	//时间临时会话数据
	sessionStorage.removeItem("service_date");
	sessionStorage.removeItem("service_date_str");

	//地址临时会话数据
	sessionStorage.removeItem("addr_id");
	sessionStorage.removeItem("addr_name");

	//订单临时会话数据
	sessionStorage.removeItem("order_type");
	sessionStorage.removeItem("order_no");
	sessionStorage.removeItem("order_id");
	sessionStorage.removeItem("order_pay");
	sessionStorage.removeItem("order_money");
	sessionStorage.removeItem("service_addons");
	sessionStorage.removeItem("service_addons_json");
	sessionStorage.removeItem("total_service_hour");
	sessionStorage.removeItem("total_staff_nums");
	sessionStorage.removeItem("pay_order_type");

	//优惠劵临时会话数据
	sessionStorage.removeItem("user_coupon_id");
	sessionStorage.removeItem("user_coupon_value");
	sessionStorage.removeItem("user_coupon_name");
	
	//服务人员信息
	sessionStorage.removeItem("staff_id");
	sessionStorage.removeItem("staff_names");
	
	//服务人员已有的服务日期
	sessionStorage.removeItem("serDate");
	
	//充值卡ID
	sessionStorage.removeItem("card_id");
}


function getItemAddrId() {
	var selectAddrId = "";
	if (localStorage['default_addr_id'] != undefined && localStorage['default_addr_id'] != null && localStorage['default_addr_id'] != "undefined") {
		selectAddrId = localStorage['default_addr_id'];
	}
	
	if (sessionStorage.getItem('addr_id') != undefined  && sessionStorage.getItem('addr_id') != null && sessionStorage.getItem('addr_id') != "undefined") {
		selectAddrId = sessionStorage.getItem('addr_id');
	}
	
	return selectAddrId;
}

function getItemAddrName() {
	var selectAddrName = "";
	if (localStorage['default_addr_name'] != undefined && localStorage['default_addr_name'] != null && localStorage['default_addr_name'] != "undefined") {
		selectAddrName = localStorage['default_addr_name'];
	}
	
	if (sessionStorage.getItem('addr_name') != undefined  && sessionStorage.getItem('addr_name') != null && sessionStorage.getItem('addr_name') != "undefined") {
		selectAddrName = sessionStorage.getItem('addr_name');
	}
	
	return selectAddrName;
}



/**
 * 推荐服务
 *
 * */

function orderRecomment(serviceTypeId){
	var recoArr=[];

	if(serviceTypeId==undefined || serviceTypeId=='' || serviceTypeId==null) return ;

	//金牌保洁
	var jinpai=[
		{"name":"家务包月","url":"order/fiveservice/order-cus-hostwork-month.html","serviceTypeId":"61"},
		{"name":"油烟机清洗","url":"order/order-deep-intro.html?service_type_id=36","serviceTypeId":"36"},
		{"name":"擦玻璃","url":"order/order-deep-intro.html?service_type_id=54","serviceTypeId":"54"},
		{"name":"洗衣机清洗","url":"order/order-deep-intro.html?service_type_id=60","serviceTypeId":"60"}
	];

	//冰箱清洗
	var bingxiang=[
		{"name":"油烟机清洗","url":"order/order-deep-intro.html?service_type_id=36","serviceTypeId":"36"},
		{"name":"金牌保洁","url":"order/order-hour-intro.html","serviceTypeId":"28"},
		{"name":"洗衣机清洗","url":"order/order-deep-intro.html?service_type_id=60","serviceTypeId":"60"},
		{"name":"空调清洗","url":"order/order-deep-intro.html?service_type_id=51","serviceTypeId":"51"}
	];

	//擦玻璃
	var caboli=[
		{"name":"油烟机清洗","url":"order/order-deep-intro.html?service_type_id=36","serviceTypeId":"36"},
		{"name":"金牌保洁","url":"order/order-hour-intro.html","serviceTypeId":"28"},
		{"name":"洗衣机清洗","url":"order/order-deep-intro.html?service_type_id=60","serviceTypeId":"60"},
		{"name":"地板保养打蜡","url":"order/order-deep-intro.html?service_type_id=52","serviceTypeId":"52"}
	];

	//厨卫消毒清洁杀菌
	var chuwei=[
		{"name":"擦玻璃","url":"order/order-deep-intro.html?service_type_id=54","serviceTypeId":"54"},
		{"name":"金牌保洁","url":"order/order-hour-intro.html","serviceTypeId":"28"},
		{"name":"床铺除螨杀菌","url":"","serviceTypeId":"34"},
		{"name":"地板保养打蜡","url":"order/order-deep-intro.html?service_type_id=52","serviceTypeId":"52"}
	];

	//床铺除螨杀菌
	var chuangpu=[
		{"name":"擦玻璃","url":"order/order-deep-intro.html?service_type_id=54","serviceTypeId":"54"},
		{"name":"地板保养打蜡","url":"order/order-deep-intro.html?service_type_id=52","serviceTypeId":"52"},
		{"name":"洗衣机清洗","url":"order/order-deep-intro.html?service_type_id=60","serviceTypeId":"60"},
		{"name":"冰箱清洗","url":"order/order-deep-intro.html?service_type_id=50","serviceTypeId":"50"}
	];

	//地板保养打蜡
	var diban=[
		{"name":"擦玻璃","url":"order/order-deep-intro.html?service_type_id=54","serviceTypeId":"54"},
		{"name":"金牌保洁","url":"order/order-hour-intro.html","serviceTypeId":"28"},
		{"name":"床铺除螨杀菌","url":"","serviceTypeId":"34"},
		{"name":"房屋大扫除","url":"order/order-deep-intro.html?service_type_id=53","serviceTypeId":"53"}
	];

	//房屋大扫除
	var fangwu=[
		{"name":"擦玻璃","url":"order/order-deep-intro.html?service_type_id=54","serviceTypeId":"54"},
		{"name":"金牌保洁","url":"order/order-hour-intro.html","serviceTypeId":"28"},
		{"name":"床铺除螨杀菌","url":"order/order-deep-intro.html?service_type_id=34","serviceTypeId":"34"},
		{"name":"房屋大扫除","url":"order/order-deep-intro.html?service_type_id=53","serviceTypeId":"53"}
	];

	//空调清洗
	var kongtiao=[
		{"name":"油烟机清洗","url":"order/order-deep-intro.html?service_type_id=36","serviceTypeId":"36"},
		{"name":"金牌保洁","url":"order/order-hour-intro.html","serviceTypeId":"28"},
		{"name":"洗衣机清洗","url":"order/order-deep-intro.html?service_type_id=60","serviceTypeId":"60"},
		{"name":"冰箱清洗","url":"order/order-deep-intro.html?service_type_id=50","serviceTypeId":"50"}
	];

	//洗衣机清洗
	var xiyiji=[
		{"name":"油烟机清洗","url":"order/order-deep-intro.html?service_type_id=36","serviceTypeId":"36"},
		{"name":"金牌保洁","url":"order/order-hour-intro.html","serviceTypeId":"28"},
		{"name":"空调清洗","url":"order/order-deep-intro.html?service_type_id=51","serviceTypeId":"51"},
		{"name":"冰箱清洗","url":"order/order-deep-intro.html?service_type_id=50","serviceTypeId":"50"}
	];

	//新居开荒
	var xinju=[
		{"name":"家务包月","url":"order/fiveservice/order-cus-hostwork-month.html","serviceTypeId":"61"},
		{"name":"地板保养打蜡","url":"order/order-deep-intro.html?service_type_id=52","serviceTypeId":"52"},
		{"name":"洗衣机清洗","url":"order/order-deep-intro.html?service_type_id=60","serviceTypeId":"60"},
		{"name":"金牌保洁","url":"order/order-hour-intro.html","serviceTypeId":"28"}
	];

	//油烟机清洗
	var youyanji=[
		{"name":"冰箱清洗","url":"order/order-deep-intro.html?service_type_id=50","serviceTypeId":"50"},
		{"name":"洗衣机清洗","url":"order/order-deep-intro.html?service_type_id=60","serviceTypeId":"60"},
		{"name":"金牌保洁","url":"order/order-hour-intro.html","serviceTypeId":"28"},
		{"name":"家务包月","url":"order/fiveservice/order-cus-hostwork-month.html","serviceTypeId":"61"}
	];

	//家务包月
	var jiawu=[
		{"name":"油烟机清洗","url":"order/order-deep-intro.html?service_type_id=36","serviceTypeId":"36"},
		{"name":"洗衣机清洗","url":"order/order-deep-intro.html?service_type_id=60","serviceTypeId":"60"},
		{"name":"空调清洗","url":"order/order-deep-intro.html?service_type_id=51","serviceTypeId":"51"},
		{"name":"厨卫深度清洁杀菌","url":"order/order-deep-intro.html?service_type_id=35","serviceTypeId":"35"}
	];

	//孕家洁
	var yunjiajie=[
		{"name":"月子房","url":"order/order-yuezifang-intro.html","serviceTypeId":"62"},
		{"name":"家务包月","url":"order/fiveservice/order-cus-hostwork-month.html","serviceTypeId":"61"},
		{"name":"油烟机清洗","url":"order/order-deep-intro.html?service_type_id=36","serviceTypeId":"36"},
		{"name":"洁宝宝","url":"order/order-cleanbaby-intro.html","serviceTypeId":"64"}
	];

	//月子房
	var yuezifang=[
		{"name":"洁宝宝","url":"","serviceTypeId":"64"},
		{"name":"家务包月","url":"order/fiveservice/order-cus-hostwork-month.html","serviceTypeId":"61"},
		{"name":"油烟机清洗","url":"order/order-deep-intro.html?service_type_id=36","serviceTypeId":"36"},
		{"name":"洗衣机清洗","url":"order/order-deep-intro.html?service_type_id=60","serviceTypeId":"60"}
	];
	
	//安居宝
	var anjubao=[
		{"name":"洁宝宝","url":"order-cleanbaby-intro.html","serviceTypeId":"64"},
		{"name":"家务包月","url":"order/fiveservice/order-cus-hostwork-month.html","serviceTypeId":"61"},
		{"name":"油烟机清洗","url":"order/order-deep-intro.html?service_type_id=36","serviceTypeId":"36"},
		{"name":"厨卫深度清洁杀菌","url":"order/order-deep-intro.html?service_type_id=35","serviceTypeId":"35"}
	];

	//洁宝宝
	var jiebaobao=[
		{"name":"月子房","url":"order/order-yuezifang-intro.html","serviceTypeId":"62"},
		{"name":"家务包月","url":"order/fiveservice/order-cus-hostwork-month.html","serviceTypeId":"61"},
		{"name":"油烟机清洗","url":"order/order-deep-intro.html?service_type_id=36","serviceTypeId":"36"},
		{"name":"洗衣机清洗","url":"order/order-deep-intro.html?service_type_id=60","serviceTypeId":"60"}
	];
	
	recoArr=[
		{"serviceTypeId":"28","list":jinpai},
		{"serviceTypeId":"50","list":bingxiang},
		{"serviceTypeId":"54","list":caboli},
		{"serviceTypeId":"35","list":chuwei},
		{"serviceTypeId":"34","list":chuangpu},
		{"serviceTypeId":"52","list":diban},
		{"serviceTypeId":"53","list":fangwu},
		{"serviceTypeId":"51","list":kongtiao},
		{"serviceTypeId":"60","list":xiyiji},
		{"serviceTypeId":"56","list":xinju},
		{"serviceTypeId":"36","list":youyanji},
		{"serviceTypeId":"61","list":jiawu},
		{"serviceTypeId":"63","list":yunjiajie},
		{"serviceTypeId":"62","list":yuezifang},
		{"serviceTypeId":"65","list":anjubao},
		{"serviceTypeId":"29","list":jinpai},
		{"serviceTypeId":"64","list":jiebaobao},
		{"serviceTypeId":"67","list":jinpai}
	]

	for(var i=0;i<recoArr.length;i++){
		var reco=recoArr[i];
		var _serviceTypeId = reco.serviceTypeId;
		if(_serviceTypeId==serviceTypeId){
			return reco.list;
		}
	}
};myApp.onPageInit('order-view-0', function(page) {


});

myApp.template7Data['page:order-view-0'] = function() {
	var result;
	var postdata = {};
	var orderNo = sessionStorage.getItem("order_no");
	postdata.order_no = orderNo;
	console.log("order_no = " + orderNo);
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "order/order_hour_detail.json",
		dataType : "json",
		data : postdata,
		cache : true,
		async : false, // 不能是异步
		success : function(data) {
			console.log(data);
			result = data.data;
		}
	})

	return result;
}
;myApp.onPageInit('order-view-1', function(page) {


});

myApp.template7Data['page:order-view-1'] = function() {
	var result;
	var postdata = {};
	var orderNo = sessionStorage.getItem("order_no");
	postdata.order_no = orderNo;
	console.log("order_no = " + orderNo);
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "order/get_exp_clean_order_detail.json",
		dataType : "json",
		data : postdata,
		cache : true,
		async : false, // 不能是异步
		success : function(data) {
			console.log(data);
			result = data.data;
		}
	})

	return result;
};myApp.onPageInit('cook-page', function(page) {

	// 根服务类型Id
	var parentServiceTypeId = page.query.firstServiceType;

	$$("#parentServiceType").val(parentServiceTypeId);

	$$(document).on(
			"click",
			"#cook-div",
			function() {
				var userId = localStorage['user_id'];

				if (userId == undefined || userId.length == 0) {

					myApp.alert("您还没有登录");

					mainView.router.loadPage("login.html");
					return false;
				}

				var enable = $$(this).find("input[name='enable']").val();
				
				if (enable == 0) {
					return false;
				}

				var serviceTypeId = $$(this).find("input[name='serviceTypeId']").val();

				//2016年4月29日11:39:23 由于有返回 页面操作。此处写死
				var parentServiceTypeId = 24;

				var serviceProperty = $$(this).find("input[name='serviceProperty']").val();

				var url = "order/order-form-zhongdiangong.html?serviceType=" + serviceTypeId
						+ "&parentServiceTypeId=" + 24;

				if (serviceProperty == 1) {
					// 全年订制
					localStorage.setItem('service_type_id', serviceTypeId);
					url = "order/fiveservice/QuanNianDingZhi.html?serviceType=" + serviceTypeId;
					
				}

				mainView.router.loadPage(url);
			});
	
	// 2. 点击banner 图进入服务介绍
	$$("#cook-banner").on('click',function(){
		
		if(parentServiceTypeId == 24){
			mainView.router.loadPage("order/service-introduce/service-chuniang.html");
		}
		
	});
	
});

// 列表显示
myApp.template7Data['page:cook-page'] = function() {
	var result;
	var parentServiceTypeId = 24;

	$$.ajax({
		type : "GET",
		url : siteAPIPath + "newPartServiceType/second_service_type.json?service_type_id="
				+ parentServiceTypeId,
		dataType : "json",
		cache : true,
		async : false,
		success : function(data) {
			// console.log(data);
			result = data;

		}
	})

	return result;
}
;myApp.onPageInit('jinpaibaojie-page', function(page) {

	// 根服务类型Id
	var parentServiceTypeId = page.query.firstServiceType;

	$$("#parentServiceType").val(parentServiceTypeId);

	$$(document).on(
			"click",
			"#jinpaibaojiediv",
			function() {

				var userId = localStorage['user_id'];

				if (userId == undefined || userId.length == 0) {

					myApp.alert("您还没有登录");

					mainView.router.loadPage("login.html");
					return false;
				}

				var enable = $$(this).find("input[name='enable']").val();

				if (enable == 0) {
					return false;
				}

				var serviceTypeId = $$(this).find("input[name='serviceTypeId']").val();

				var parentServiceTypeId = 23;

				var serviceProperty = $$(this).find("input[name='serviceProperty']").val();

				var url = "order/order-form-zhongdiangong.html?serviceType=" + serviceTypeId
						+ "&parentServiceTypeId=" + 23;

				if (serviceProperty == 1) {
					// 全年订制
					localStorage.setItem('service_type_id', serviceTypeId);
					url = "order/fiveservice/QuanNianDingZhi.html?serviceType=" + serviceTypeId;

				}

				mainView.router.loadPage(url);
			});
	
	// 2. 点击banner 图进入服务介绍
	$$("#jinpaibaojie-banner").on('click',function(){
		
		if(parentServiceTypeId == 23){
			mainView.router.loadPage("order/service-introduce/service-jinpai.html");
		}
		
	});
	
	
});

// 列表显示
myApp.template7Data['page:jinpaibaojie-page'] = function() {
	var result;
	var parentServiceTypeId = 23;

	$$.ajax({
		type : "GET",
		url : siteAPIPath + "newPartServiceType/second_service_type.json?service_type_id="
				+ parentServiceTypeId,
		dataType : "json",
		cache : true,
		async : false,
		success : function(data) {
			// console.log(data);
			result = data;

		}
	})

	return result;
}
;//列表显示
myApp.template7Data['page:qiyefuwu-page'] = function() {
	var result;
	
	var parentServiceTypeId = localStorage['firstServiceType'];
	
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "newPartServiceType/second_service_type.json",
		data:{
			"service_type_id":parentServiceTypeId
		},
		dataType : "json",
		cache : true,
		async : false,
		success : function(data) {
			result = data;
			
		}
	})

	return result;
}


myApp.onPageInit('qiyefuwu-page', function(page) {

	// 根服务类型Id
	var parentServiceTypeId = localStorage['firstServiceType'];
	
	$$("#parentServiceTypeId").val(parentServiceTypeId);

	$$("div[name='tiexinjiashidiv']").on("click",function() {

		var userId = localStorage['user_id'];

		if (userId == undefined || userId.length == 0) {

			myApp.alert("您还没有登录");

			mainView.router.loadPage("login.html");
			return false;
		}

		var enable = $$(this).find("input[name='enable']").val();

		if (enable == 0) {
			return false;
		}

		var serviceTypeId = $$(this).find("input[name='serviceTypeId']").val();
		
		//在 助理 服务下单页面。使用模板。此处需要存储url 参数值
		localStorage.setItem("am_faqiyuyue_service_type_id",serviceTypeId);
		localStorage.setItem("am_faqiyuyue_parent_service_type_id",localStorage['firstServiceType']);
		
		mainView.router.loadPage("order/order-am-faqiyuyue.html");
				
	});
	
	
	//点击banner进入 服务介绍
	$$("#qiye-banner").on('click',function(){
		
		if(parentServiceTypeId == 27){
			mainView.router.loadPage("order/service-introduce/service-qiye.html");
		}
		
	});
});



;myApp.onPageInit('quanniandingzhi-page', function(page) {
	
	var serviceTypeId =  page.query.serviceType;
	
	if(serviceTypeId != undefined){
		$$("#serviceTypeId").val(serviceTypeId);
	}
		
	$$("#submitAtOnce").on("click",function(){
		
		var  serviceTypeId = $$("#serviceTypeId").val();
		
		var userId = localStorage['user_id'];
		
		if(userId == undefined){
			
			myApp.alert("您还没有登录");
			mainView.loadPage.router("login.html");
			return false;
		}
		
		$$.ajax({
			type: "get",
			 url: siteAPIPath + "order/order_customiza_year.json",
			data: {
				"service_type_id":serviceTypeId,
				"user_id":userId
			},
			success:function(data,sta,xhr){
				
				var result = JSON.parse(data);
				
				if(result.status == "999"){
					
					myApp.alert(result.msg);
					return false;
				}
				
				myApp.alert(result.msg);
				
				mainView.router.loadPage("index.html");
			}
		});
	});	
});

myApp.template7Data['page:quanniandingzhi-page'] = function() {
	var result;
	var serviceTypeId = localStorage['service_type_id'];
	
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "newPartServiceType/service_type_detail.json?service_type_id="
				+ serviceTypeId,
		dataType : "json",
		cache : true,
		async : false,
		success : function(data) {
			// console.log(data);
			result = data.data;

		}
	})

	return result;
}


;//列表显示
myApp.template7Data['page:tiexinjiashi-page'] = function() {
	var result;
	
	var parentServiceTypeId = localStorage['firstServiceType'];
	
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "newPartServiceType/second_service_type.json",
		data:{
			"service_type_id":parentServiceTypeId
		},
		dataType : "json",
		cache : true,
		async : false,
		success : function(data) {
			result = data;
			
		}
	})

	return result;
}


myApp.onPageInit('tiexinjiashi-page', function(page) {

	// 根服务类型Id
	var parentServiceTypeId = localStorage['firstServiceType'];
	
	$$("#parentServiceTypeId").val(parentServiceTypeId);
	
	
	// 1.  点击 到 下单页面
	$$("div[name='tiexinjiashidiv']").on("click",function() {

		var userId = localStorage['user_id'];

		if (userId == undefined || userId.length == 0) {

			myApp.alert("您还没有登录");

			mainView.router.loadPage("login.html");
			return false;
		}

		var enable = $$(this).find("input[name='enable']").val();

		if (enable == 0) {
			return false;
		}

		var serviceTypeId = $$(this).find("input[name='serviceTypeId']").val();
		
		
		//在 助理 服务下单页面。使用模板。此处需要存储url 参数值
		localStorage.setItem("am_faqiyuyue_service_type_id",serviceTypeId);
		localStorage.setItem("am_faqiyuyue_parent_service_type_id",localStorage['firstServiceType']);
		
//		mainView.router.loadPage("order/order-am-faqiyuyue.html?serviceType="+serviceTypeId
//				+ "&parentServiceTypeId=" + localStorage['firstServiceType']);
				
		mainView.router.loadPage("order/order-am-faqiyuyue.html");
	});
	
	//2. 点击banner图进入 服务说明
	$$("#tiexinjiashi-banner").on('click',function(){
		
		if(parentServiceTypeId == 25){
			mainView.router.loadPage("order/service-introduce/service-tiexin.html");
		}
		
		if(parentServiceTypeId == 26){
			mainView.router.loadPage("order/service-introduce/service-shendu.html");
		}
		
	})
	
});



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
		
		if (result.data.is_default == 1) {
			localStorage['default_addr_id'] = result.data.id;
			localStorage['default_addr_name'] = result.data.name + " " + result.data.addr;			
		}

		
		//getAm();
		
		//localStorage['am_mobile'] = result.data.amMobile;

		
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
	
	//先清空
	$$('#addr-auto-list ul').html("");
	
	//如果没有搜索结果，直接返回
	if(list.length <= 0){
		return false;
	}
	
	
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
	$$("#addr-auto-list ul").html(resultHtml);

}



//发起请求调用百度关键字提示接口
function getBaiduAutoComplete() {

	var paramData = {};
	
	paramData.query = $$("#name").val();
	paramData.region = "北京市";
	paramData.output = "json";
	paramData.ak = "2sshjv8D4AOoOzozoutVb6WT";
	
	/*
	 * 	当 name 值为空时, 参数query 不会传递,导致请求失败
	 */
	if($$("#name").val().length <= 0){
		return false;
	}
	
	
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
			if (localStorage['default_addr_id'] == null) {
				$$.each(data, function(i,item) {
					if (item.is_default == 1) {
						localStorage['default_addr_id'] = item.id;
						localStorage['default_addr_name'] = item.name + " " + item.addr;
						return false;
					}
				})
			}
			console.log("default_addr_id = " + localStorage['default_addr_id']);
			console.log("default_addr_name = " + localStorage['default_addr_name']);
			result = data;
		}
	})

	return result;
}


//地址添加
myApp.onPageInit('mine-addr-list', function (page) {
	var userId = localStorage['user_id'];
	$$(".all-button9").on("click",function(){
		mainView.router.loadPage("user/mine-add-addr.html?addr_id=0");
	});
});


function clickToSetDefault(obj,addrId){
	//当前被点击的地址
	var addrNameClick= $$(obj).find("#mine-add-addr-link").text();

	if(addrNameClick.indexOf("默认")>0){
		localStorage.setItem("default_addr_name",addrNameClick.replace("[默认]", "").trim());
		localStorage.setItem("default_addr_id",addrId);
		goBackToOrder(localStorage['default_addr_id'], localStorage['default_addr_name']);
		return false;
	}

	var userId = localStorage['user_id'];
	var paramData = {};
	paramData.user_id = userId;
	paramData.addr_id = addrId;

	myApp.confirm('设置为默认地址?',
		function(){
			$$.ajax({
				type : "POST",
				url : siteAPIPath + "user/post_set_addr_default.json",
				data:{"user_id":userId, "addr_id":addrId},
				async:false,
				success : function() {
					//设置当前的为默认的地址.
					$$(".addr-dizhi").each(function(key, index) {
						var addrIdObj = $$(this).find("#addr_id").val();
						var addrNameObj = $$(this).find("#mine-add-addr-link");
						var addrNameHtml = addrNameObj.html();
						if (addrId == addrIdObj) {
							if (addrNameHtml.indexOf("默认") < 0) {
								localStorage['default_addr_id'] = addrId;
								localStorage['default_addr_name'] = addrNameHtml.trim();
								addrNameObj.html("[默认] " + addrNameHtml);
							}
						} else {
							addrNameObj.html(addrNameHtml.replace("[默认]", ""));
						}
						myApp.swipeoutClose($$(this));
					});
					goBackToOrder(localStorage['default_addr_id'], localStorage['default_addr_name']);
				}
			});
		},
		//  点击 "取消/返回",不设置默认地址,但是 把该点击地址 作为 选中项，传回上一页
		function (){
			goBackToOrder(addrId,addrNameClick);
		}
	);
}

//点选 ‘取消/返回’  1.不修改默认地址  2.把该地址 传回下单页
function goBackToOrder(addrId, addrName){
	console.log("goBackToOrder addr_id = " + addrId);
	console.log(" goBackToOrder addr_name = " + addrName);
	var returnPage = "";
	for (var i =1; i < 5; i++) {	// 判断前一页是不是 下单页,如果是,则作为 返回页
		var historyPage = mainView.history[mainView.history.length-i];

		if (historyPage == undefined) continue;
		console.log("historyPage = " + historyPage);
		if (historyPage.indexOf("order-hour-choose") >= 0 ||
			historyPage.indexOf("order-deep-choose") >= 0 
		) {
			returnPage = historyPage;
			break;
		}
	}

	if (returnPage == "") return;

	sessionStorage.setItem('addr_id', addrId);
	sessionStorage.setItem('addr_name', addrName);
	console.log("addr_id = " + sessionStorage.getItem("addr_id"));
	console.log("addr_name = " + sessionStorage.getItem("addr_name"));
	mainView.router.loadPage(returnPage);

}

//删除地址
function delAddr(obj){
	var id=$$(obj).next().val();
	if(id==null || id=='' || id==undefined) return false;
	$$.ajax({
		type:"get",
		url:siteAPIPath + "user/delete_addr.json",
		data:{"addr_id":id},
		success:function(){
			
		}
	});
}
;//列表显示用户充值卡
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
	$$(".charge-way").on("click",function(){
	var cardId = $$(this).next("#id").val();
	if(cardId!=null || cardId!=""){
		sessionStorage.setItem("card_id",cardId);
		mainView.router.loadPage("user/charge/mine-charge-way.html?card_id="+cardId);
	}
});
})
;myApp.onPageBeforeInit('mine-charge-pay-success', function(page) {
	
	var userId = localStorage.getItem("user_id");
//	var paycardId=localStorage.getItem("pay_card_id");
	var userInfoSuccess = function(data, textStatus, jqXHR) {
	  	myApp.hideIndicator();
		var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(data.msg);
			return;
		}
		var userInfo = result.data; 
//		$$("#rest_money").text(userInfo.rest_money);

		localStorage.setItem("is_vip",userInfo.is_vip);
		
	};
	
	//获取用户充值信息接口
    $$.ajax({
        type:"get",
        url:siteAPIPath+"user/getUserRestMoneyInfo.json?user_id="+userId,
        dataType:"json",
        cache:false,
        statusCode : {
			200 : userInfoSuccess,
			400 : ajaxError,
			500 : ajaxError
		}
    });
//    
//    $$('#charge-pay-success-btn').click(function(){
//    	mainView.router.loadPage("user/mine.html");
//    });
//	
});;myApp.onPageBeforeInit('mine-charge-way', function(page) {

	var userId = localStorage.getItem("user_id");
	var cardId = page.query.card_id;
//	var cardPay = page.query.card_pay;
	var orderPayType = 2;
	localStorage.removeItem("pay_card_id");
//	localStorage.setItem("pay_card_id",cardId);
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}

	$$("#mobile").text(localStorage.getItem("user_mobile"));

//	var html = compiledTemplate(cardPay);

//	if (cardPay == undefined || cardPay == '' || cardPay == 0) {
//		var cardInfoSuccess = function(data, textStatus, jqXHR) {
//			myApp.hideIndicator();
//			var result = JSON.parse(data.response);
//			if (result.status == "999") {
//				myApp.alert(data.msg);
//				return;
//			}
//			var cardInfo = result.data;
//			$$("#card_pay_view").text(cardInfo.card_value);
//		};

//		$$.ajax({
//			type:"GET",
//			url:siteAPIPath+"dict/get_card_detail.json?card_id="+cardId,
//			dataType:"json",
//			cache:false,
//			statusCode : {
//				200 : cardInfoSuccess,
//				400 : ajaxError,
//				500 : ajaxError
//			}
//		});
//	}


	var postCardBuySuccess =function(data, textStatus, jqXHR) {

		var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}
		var orderId = result.data.id;
		//如果为微信支付，则需要跳转到微信支付页面.
		if (orderPayType == 2) {
			var wxPayUrl = localUrl + "/" + appName + "/wx-pay-pre.jsp";
			wxPayUrl +="?orderId="+orderId;
			wxPayUrl +="&userCouponId=0";
			wxPayUrl +="&orderType=4";
			wxPayUrl +="&payOrderType=1";
			location.href = wxPayUrl;
		}
	};

	//点击支付的处理
	$$("#chongzhi-submit").click(function(){
		var postdata = {};
		postdata.user_id = userId;
		postdata.card_type = cardId;
		postdata.pay_type = 2;
		postdata.staff_code=$$("#staffCode").val();

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
});

myApp.template7Data['page:mine-charge-way'] = function(){
	var result
	var cardId = sessionStorage.getItem("card_id");
	$$.ajax({
		type:"GET",
		url:siteAPIPath+"dict/get_card_detail.json?card_id="+cardId,
		dataType:"json",
		cache:true,
		async:false,
		success :function(data) {
			result=data.data;
		}
	});
//	sessionStorage.removeItem("card_id");
	return result;
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
;myApp.onPageInit('mine-rest-money-detail-page', function (page) {
	
	var userId = localStorage['user_id'];
	var loading = false;// 加载flag
	var page = $$("#page").val();
	
	var restDetailListSuccess = function(data, textStatus, jqXHR) {

		var result = JSON.parse(data.response);
		var datas = result.data;

		var htmlTemplate = $$('#rest-detail-list-part').html();

		var html = ''; // 当前订单

		for (var i = 0; i < datas.length; i++) {
			var item = datas[i];
			var htmlPart = htmlTemplate;
			
			
			var orderType = item.order_type;
			var detailImg = "";
			if (orderType == 0  || orderType == 2 || orderType == 3) {
				detailImg = '<img src="img/laonian/jianhao.png" alt="" class="all-img7">';
			} else if (orderType == 1) {
				detailImg = '<img src="img/laonian/jiahao.png" alt="" class="all-img7">';
			}
			htmlPart = htmlPart.replace(new RegExp('{detailImg}', "gm"), detailImg);
			
			htmlPart = htmlPart.replace(new RegExp('{orderTypeName}', "gm"), item.order_type_name);
			htmlPart = htmlPart.replace(new RegExp('{orderPay}', "gm"), item.order_pay);
			htmlPart = htmlPart.replace(new RegExp('{addTimeStr}', "gm"), item.add_time_str);
			
			html+= htmlPart;
		}
		// 当前订单
		if (page == 1) {
			$$("#rest-detail-list").html(html);
		} else {
			$$("#rest-detail-list").append(html);
		}

		loading = false;
		
		$$("#page").val(page);
		if (datas.length >= 10) {
			$$('#pay-detail-list-more').css("display", "block");
		} else {
			$$('#pay-detail-list-more').css("display", "none");
		}
	};
	
	
	function loadRestDetailList(userId, page) {
		console.log("page = " + page);
		var postdata = {};
		
		var apiUrl = "user/pay_detail_list.json";
		postdata.user_id = userId;
		postdata.page = page;

		$$.ajax({
			type : "GET",
			url : siteAPIPath + apiUrl,
			dataType : "json",
			cache : true,
			data : postdata,
			statusCode : {
				200 : restDetailListSuccess,
				400 : ajaxError,
				500 : ajaxError
			},

		});
	}	
		
	// 注册'infinite'事件处理函数
	$$('#pay-detail-list-more').on('click', function() {
		var cpage = ++page;
		loadRestDetailList(userId, cpage);
	});
	
	loadRestDetailList(userId, page);
});
;myApp.onPageBeforeInit('mine', function (page) {
	var userId = localStorage.getItem("user_id");;
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	
	//优惠券
	$$("#mine-coupons").on("click",function(){
		mainView.router.loadPage("user/user-coupons.html?user_id="+userId);
	});

	//关于我们
	$$("#mine-about-us").on("click",function(){
		mainView.router.loadPage("user/aboutus.html");
	});
	
	//常见问题
	$$("#mine-issue").on("click",function(){
		mainView.router.loadPage("user/faq.html");
	});
	
	//地址管理
	$$("#mine-addr-manager").on("click",function(){
		mainView.router.loadPage("user/mine-addr-list.html?user_id="+userId);
	});
	
	//用户协议
	$$("#mine-agreement").on("click",function(){
		mainView.router.loadPage("user/agreement.html");
	});
	
	// 点击 余额 --消费明细
//	$$("#restMoneyDiv").on('click',function(){
//		mainView.router.loadPage("user/mine-rest-money-detail.html");
//	});

	//退出登录
	$$('#user-logout').on('click', function() {
		localStorage.removeItem("mobile");
		localStorage.removeItem('user_id');
		localStorage.removeItem('is_vip');
		localStorage.removeItem('im_username');
		localStorage.removeItem('im_password');

		localStorage.removeItem("am_id");
		localStorage.removeItem("am_mobile");
		
		localStorage.removeItem("default_addr_id");
		localStorage.removeItem("default_addr_name");
		
		removeSessionData();
		mainView.router.loadPage("index.html");
	});

});

//列表显示，获取用户的信息
myApp.template7Data['page:mine']=function(){
	var result="";
	var userId = localStorage.getItem("user_id");
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}

	$$.ajax({
		type : "GET",
		url : siteAPIPath + "user/get_userinfo.json?user_id="+userId,
		dataType : "json",
		cache : true,
		async : false,
		success : function(data) {
			result = data.data;

			localStorage.setItem("is_vip",result.is_vip);
			
			
		}
	})
	return result;
}


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
});;myApp.onPageInit('mine-coupons-list', function (page) {

	var userId = localStorage.getItem("user_id");
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}

	var backUrl = page.query.back_url;
	var serviceTypeId = sessionStorage.getItem('service_type_id');
	var orderMoney = sessionStorage.getItem('order_money');

	//处理订单调整到当前页面选择优惠劵的
	if(serviceTypeId!=null && serviceTypeId!='' && serviceTypeId!=undefined){
		$$(document).on('click', '#mine-coupons-use', function (e) {

			e.stopImmediatePropagation(); //防止重复点击

			if (backUrl == "") return;

			var couponOrderType =  $$(this).find(".service_type").val();
			var userCouponId = $$(this).find("#user_coupon_id").val();
			var userCouponValue = $$(this).find("#user_coupon_value").val();
			var maxVlaue = $$(this).find("#max_value").val();
			console.log("maxVlaue="+maxVlaue);
			var userCouponName = "￥" + userCouponValue;
			
			var fromDate = $$(this).find("#from_date").val();
			var toDate = $$(this).find("#to_date").val();
			var serviceDate = sessionStorage.getItem("service_date")*1000;
//			var couponsTypeId = $$(this).find("#coupons_type_id").val();
			
//			if(couponsTypeId!=1){
				//判断优惠券的使用类型
			if(couponOrderType>0){
				if (couponOrderType.indexOf(serviceTypeId) < 0) {
					myApp.alert("当前优惠劵不适用!");
					return false;
				}
			}
			
			
			if(parseFloat(orderMoney) < parseFloat(maxVlaue)){
				myApp.alert("当前优惠劵不适用!");
				return false;
			}
//			}
			
			//判断有效期
			if(serviceDate<=fromDate || serviceDate>=toDate){
				myApp.alert("当前优惠劵不适用!");
				return false;
			}

			sessionStorage.setItem("user_coupon_id", userCouponId);
			sessionStorage.setItem("user_coupon_name", userCouponName);
			sessionStorage.setItem("user_coupon_value", userCouponValue);

			mainView.router.loadPage(backUrl);

		});
	}
});

//列获取优惠券列表
myApp.template7Data['page:mine-coupons-list'] = function(){
	var result="";
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
};myApp.onPageBeforeInit('user-wancheng', function (page) {
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
};myApp.onPageInit('survey-question-page', function(page) {
	
	
	//对于 完成后  又返回的 情况~
	if(localStorage['survey_user_id'] == undefined){
		
		myApp.alert("请您发起新的订制服务");
		mainView.router.loadPage("survey-user.html");
		return false;
	}
	
	
	var postData = {};
	
	var qId = page.query.q_id;
	
	//如果是第一次加载页面
	
	if(qId == undefined){
		qId = 0;
	}
	
	postData.q_id = qId;
	
	
	/**
	 *  请求成功后,	
	 *  采用virtual list 替换 题目及选项模板
	 */
	var loadQuestionSuccess = function(datas, textStatus, jqXHR){
		
		var result = JSON.parse(datas.response);
		
		/*
		 * 返回值
		 */
		//题目Vo
		var questionVo = result.data;
		
		//选项list
		var optionList = questionVo.option_list;
		
		/*
		 * 填充
		 */
		//题目模板
		var questionHtml = $$("#questionTemplate").html();
			
		questionHtml = questionHtml.replace(new RegExp('{questionId}',"gm"), questionVo.q_id);
		
//		单选
		if(questionVo.is_multi == 0){
			questionHtml = questionHtml.replace(new RegExp('{questionTitle}',"gm"), questionVo.title);
		}
//		多选
		if(questionVo.is_multi == 1){
			questionHtml = questionHtml.replace(new RegExp('{questionTitle}',"gm"), "(多选)"+questionVo.title);
		}
		
		
		questionHtml = questionHtml.replace(new RegExp('{isFirst}',"gm"), questionVo.is_first);
		/*
		 * 每次只显示一道题，  先清空上一题
		 */
		
		$$("#questionDisplay").html("");
		$$("#questionDisplay").append(questionHtml);
		
		//选项模板
		var optionHtml = "";
		if(questionVo.is_multi === 0){
			//如果是单选题,用单选模板
			optionHtml = $$("#optionTemplateRadio").html();
		}else{
			//如果是多选题用多选模板
			optionHtml = $$("#optionTemplateCheckbox").html();
		}
		
		//用来拼接所有选项的临时变量
		var optionHtmlTem = "";
		
		for (var i = 0; i < optionList.length  ; i++) {
			
			var part = optionHtml;
			
			part = part.replace(new RegExp('{optionNo}',"gm"), optionList[i].no);
			part = part.replace(new RegExp('{optionStr}',"gm"), optionList[i].title);
			
			optionHtmlTem += part;
		}
		
		//先清空上一题
		$$("#optionDisplay").html("");
		$$("#optionDisplay").append(optionHtmlTem);
		
		/**
		 * 处理 上一题、下一题按钮
		 * 
		 *   第一题: is_first = 0
		 * 	 处于中间的题: is_first = 1
		 *   最后一题: is_first = 2
		 */
		
//		if(questionVo.is_first === 0){
//			//如果是第一题,隐藏上一题按钮
//			$$("#previousDiv").hide();
//		}
		
		if(questionVo.is_first === 2){
			//如果是最后一题
			
//			$$("#previousDiv").hide();
			$$("#nextDiv").hide();
			$$("#endDiv").show();
		}
		
		
		
	};
	
	
	//第一次加载页面时,加载第一题
	$$.ajax({
		type: "get",
		 url: siteAPIPath + "survey/load_question.json",
		data: postData,
		statusCode: {
         	200: loadQuestionSuccess,
 	    	400: ajaxError,
 	    	500: ajaxError
 	    }
	});
	
	
	/**
	 * 点击下一题
	 * 	1.加载下一题
	 *  2.存储当前选择结果
	 */
	$$("#nextQuestion").on('click',function(){
		
		
		var postDataNext =  generateSurveyResultOption();
		
		if(postDataNext == false){
			return false;
		}
		
		var  aaa = localStorage['surveyResult'];
//		alert(aaa);
		
		$$.ajax({
			type: "get",
			 url: siteAPIPath + "survey/load_question.json",
			data: postDataNext,
			statusCode: {
	         	200: loadQuestionSuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});
	});
	
	
	/**
	 * 点击提交问卷
	 * 	
	 * 	 1.传递用户id 和选择结果	
	 *   2.
	 */
	
	// 合并数组
	$$("#endQuestion").on('click',function(){
		
		var selectResult =  generateSurveyResultOption();
		
		if(selectResult == false){
			return false;
		}
		
		
		var resultPageUrl = "survey/survey-result.html"; 
		
		mainView.router.loadPage(resultPageUrl);
		
	});
	
});

/*
 * 点击下一题或者 提交问卷按钮时，调用公共方法，封装 题目及其对应的勾选的选项
 */
function generateSurveyResultOption(){
	
		var postData = {};
		
		//选中的选项dom对象
		var obj =  $$("#optionDisplay input[name='optionNo']:checked");
		
		if(obj.length == 0){
			myApp.alert("请您任意选择一项,叮当到家将为您提供更为贴心的订制服务");
			return false;
		}
		
		//选中的选项的序号 （A,B,C...）  用 逗号拼接
		var selectOption = "";
		
		obj.each(function(k,v){
			selectOption += $$(this).parent().find("input[id='realOptionNo']").val()+",";
		});
		
		postData.option_str = selectOption.substring(0,selectOption.length-1);
		//当前题目id
		postData.q_id = $$("#questionId").val();
		
		
		var arrayEle = {"questionId":postData.q_id,"optionStr":postData.option_str};
		
		/**
		 * 存储当前的选择结果
		 */
		
		var resultArray = [];
		
		var isFirst = $("#isFirst").val();
		
		resultArray.push(arrayEle);
		
		// 如果是最后一题，提交之前也需要存储选择结果
		if(isFirst == 0){
			//如果是第一题
			localStorage.setItem("surveyResult",JSON.stringify(resultArray));
		}
		
		if(isFirst == 1 || isFirst == 2){
			
			//如果是位于中间位置的题目或最后一题
			/*
			 * html5的localStorage仅能存储字符串类型的数据，所以在存储数组时需要把数据转换为字符串，在使用时需要先parse将字符串转换为数组对象
			 */
			var a1 = JSON.parse(localStorage['surveyResult']);
			var a2 = resultArray.concat(JSON.parse(localStorage['surveyResult']));
			var a3 = JSON.stringify(resultArray.concat(JSON.parse(localStorage['surveyResult'])));
			
			localStorage.setItem("surveyResult",JSON.stringify(resultArray.concat(JSON.parse(localStorage['surveyResult']))));
		}
	
		
		return postData;
}

;myApp.onPageInit('survey-result-child-page', function(page) {
	
	//对于 完成后  又返回的 情况~
	if(localStorage['survey_user_id'] == undefined){
		
		myApp.alert("请您发起新的订制服务");
		mainView.router.loadPage("index.html");
		return false;
	}
	
	
	var contentId = localStorage['changeContentId'];
	
	$$("#contentId").val(contentId);
	
	var postData = {};
	
	postData.content_id = contentId;
	
	$$.ajax({
		type: "get",
		 url: siteAPIPath + "survey/get_child_content_list.json",
		data: postData,
		success:function (datas,sta,xhr){
			
			var result = JSON.parse(datas);
			
			var childArray =   result.data;
			
			
			var childContentTemplate = $$("#childContentTemplate").html();
			
			var childContentHtml  = "";
			
			
//			//回显
			if(localStorage['boxChildContent'] !=null && localStorage['boxChildContent'].length >0){
				
				var nowArray =  JSON.parse(localStorage['boxChildContent']); 
				
				
				for (var i = 0; i < nowArray.length; i++) {
					var nowArrayCont = nowArray[i];
					
					var baseContentId =  nowArrayCont.baseContentId;
					
					if(contentId == baseContentId){
					
						var childBoxArray = nowArrayCont.childBoxArray;
					
						for (var int = 0, lengthA = childBoxArray.length; int < lengthA; int++) {
							
							var  childBox = childBoxArray[int];
							
							for (var i = 0, lengthB =  childArray.length ; i < lengthB ; i++) {
								
								var child = childArray[i];
								
								if(child.id == childBox.boxChildContentId){
									
//									var number = child.option_str.match(/\d/g).join("");
									
									var  realNumber = childBox.boxChildContentTimes;
									
									child.option_str = child.option_str.replace(/\d+/g,realNumber);
								}
							}
						}
						break;
					}
				}
			}
		
			
			for (var i = 0; i < childArray.length; i++) {
				var child = childArray[i];
				
				var part = childContentTemplate;
				
				//默认次数
				var number = child.option_str.match(/\d/g).join("");
				
				//子服务名称
				var optionName = child.option_str.substring(0,child.option_str.indexOf(number));
				
				part = part.replace(new RegExp('{contentName}',"gm"), optionName);
				
				//子服务id
				part = part.replace(new RegExp('{id}',"gm"), child.id);
				
				//子服务默认次数
				part = part.replace(new RegExp('{number}',"gm"), number);
				
				childContentHtml+=part;
			}
			
			$$("#childDisplay").append(childContentHtml);
			
		},
		error:function(){
			myApp.alert("网络错误");
		}
	});
	
	
	/*
	 *  点击确认调整次数
	 */ 
	
	$$("#confirmChangeTimes").on('click',function(){
		
		var boxArray = [];
		
		var childArray = [];
		
		//遍历存储，子服务及其对应的次数
		$$("#childDisplay").find("input[id='childContentId']").each(function(k,v){
			
			var id = $$(this).val();
			var times =  $$(this).parent().find("#item_num").val();	
			
			var aaa = {"boxChildContentId":id,"boxChildContentTimes":times};
			
			childArray.push(aaa);
		});
		
		var contentId = $$("#contentId").val();
		
		
		/**
		 * 结构 ： {服务,子服务:次数。。。}
		 */
		
		var boxStr = {"baseContentId":contentId,"childBoxArray":childArray};
		
		boxArray.push(boxStr);
		
		
		if(localStorage['boxChildContent'] !=null && localStorage['boxChildContent'].length >0){
			
			//先遍历去重复
			var nowArray =  JSON.parse(localStorage['boxChildContent']); 
			for (var i = 0; i < nowArray.length; i++) {
				var nowStr = nowArray[i];
				
				//去重!!!
				for (var j = 0; j < boxArray.length; j++) {
					var boxStr = boxArray[j];
					
					if(nowStr.contentId == boxStr.contentId){
						nowArray.baoremove(i);
					}
				}
			}
			
			localStorage.setItem("boxChildContent",JSON.stringify(boxArray.concat(nowArray)));
		}else{
			localStorage.setItem("boxChildContent",JSON.stringify(boxArray));
		}
		
		myApp.showPreloader('调整成功！');
	    setTimeout(function () {
	        myApp.hidePreloader();
	    }, 500);
		
		mainView.router.loadPage("survey/survey-result.html");
	});
});


//删除数组元素，传递下标 
Array.prototype.baoremove = function(dx) 
{ 
    if(isNaN(dx)||dx>this.length){return false;} 
    this.splice(dx,1); 
} 




//处理 子服务 如  ：空调 4次 。。。
function replaceChildContentBoxNumber(str){
	
	//取得 字符串中的 数字
	var number = str.match(/\d/g).join("");
	
	//替换数字为 可输入的输入框
	str = str.replace(/\d+/g,"<input type='tel' id='finalChildTimes' maxlength='3' class='input-child-content-box-style' value='' placeholder='"+number+"' >")
	
	return str;
}


//加减求和相关
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
	itemNumObj.val("");
	
	itemNumObj.val(v);

//	setTotal();
}

//减号处理
function onSubItemNum(obj) {
	var itemNumObj = obj.find('input[name=item_num]'); 
	var v = itemNumObj.val();
	if (v == undefined || !isNum(v) || v == 0) {
		v = 0;
	} else {
		v = parseInt(v) - 1;
	}
	
	itemNumObj.val("");
	
	itemNumObj.val(v);
//	setTotal();	
}


////计算价格总和
//function setTotal() {
//
//	var orderMoney =  itemPrice= Number(0);
//	var serviceAddonId = 0;
//	var serviceAddonIdObj;
//	$$("input[name = item_num]").each(function(key, index) {
//		 
//		 itemNum = $$(this).val();
//		 serviceAddonIdObj = $$(this).parent().find('input[name=service_addon_id]');
//
//		 serviceAddonId = serviceAddonIdObj.val();
//		 
//		 var price = $$(this).nextAll("span[name=item_uint]").text();
//		 
//		 var reg = /[1-9][0-9]*/g;
//		 
//		 itemPrice = price.match(reg);
//		 
//		 
//		 if (itemNum != undefined && isNum(itemNum) && serviceAddonId != undefined) {
//
////			 itemPrice = getServiceAddonPrice(serviceAddonId);
//				 
//			 orderMoney = Number(orderMoney) + Number(itemNum) * Number(itemPrice);
//			 
//		 }
//	
//	});	
//	$$("#order_money_input").attr("value",orderMoney);		
//	$$("#order_moneys_span").val(orderMoney);		
//}




;myApp.onPageInit('survey-result-price-page', function(page) {
	
	//来到价格明细页面时，清除 结果页, 修改过程中的, “中间调整值”
	localStorage.removeItem("storeBaseArray");
	localStorage.removeItem("storeRadioArray");
	
	
	var userId = localStorage['survey_user_id'];
	
	//对于 完成后  又返回的 情况~
	if(userId == undefined){
		
		myApp.alert("请您发起新的定制服务");
		mainView.router.loadPage("index.html");
		return false;
	}
	
	
	var postData = {};
	
	postData.user_id = userId;
	
	$$.ajax({
		type: "get",
		 url: siteAPIPath + "survey/get_result_price.json",
		data: postData,
		success:function (datas,sta,xhr){
			
			var result = JSON.parse(datas);
			
			//封装参数
			var yearResult = result.data[0];
			
			//预计年消费,通用
			$$("#sumPrice").text(yearResult.sum_price);
			
			//设置默认显示,年支付
			
			//优惠后价格
			$$("#discountPrice").text(yearResult.discount_price);
			//优惠后月支付
			$$("#monthPrice").text(yearResult.price_by_month);
			
			//单选对应的值
			$$("#yearRadio").val(yearResult.survey_pay_type);
			
			//隐藏域参数
			$$("#yearDiscountPrice").val(yearResult.discount_price);
			$$("#yearPriceByMonth").val(yearResult.price_by_month);
			
			var quarterResult = result.data[1];
			
			$$("#quarterDiscountPrice").val(quarterResult.discount_price);
			$$("#quarterPriceByMonth").val(quarterResult.price_by_month);
			
			$$("#quarterRadio").val(quarterResult.survey_pay_type);
			
			var monthResult = result.data[2];
			$$("#monthDiscountPrice").val(monthResult.discount_price);
			$$("#monthPriceByMonth").val(monthResult.price_by_month);
			
			$$("#monthRadio").val(monthResult.survey_pay_type);
			
		},
		error:function(){
			myApp.alert("网络异常");
		}
	});

	// 支付方式, 变化时, 动态改变 价格
	$$("select[name='payTypeSelect']").on("change",function(){
		
		var nowSelect = $$(this).val();
		
		if(nowSelect == 0){
			$$("#discountPrice").text($$("#yearDiscountPrice").val());
			$$("#monthPrice").text($$("#yearPriceByMonth").val());
		}
		
		//半年
		if(nowSelect == 1){
			
			$$("#discountPrice").text($$("#quarterDiscountPrice").val());
			$$("#monthPrice").text($$("#quarterPriceByMonth").val());
			
		}
		
		//月
		if(nowSelect ==2){
			
			$$("#discountPrice").text($$("#monthDiscountPrice").val());
			$$("#monthPrice").text($$("#monthPriceByMonth").val());
		}
		
	});
	
	
	
	
	//完成订制, 清空 调查相关的 localStorage
	$$("#completeSurvey").on("click",function(){
		
		localStorage.removeItem("boxChildContent");
		localStorage.removeItem("surveyResult");
		localStorage.removeItem("survey_user_id");
		localStorage.removeItem("changeContentId");
		
		mainView.router.loadPage("survey/survey-success.html");
		
	});
});



;myApp.onPageInit('survey-result-page', function(page) {
	
	//对于 完成后  又返回的 情况~
	if(localStorage['survey_user_id'] == undefined){
		
		myApp.alert("请您发起新的定制服务");
		
		mainView.router.loadPage("survey/survey-user.html");
		return false;
	}
	
		/**
		 *  请求成功后,	
		 *  采用virtual list 替换 题目及选项模板
		 */
		var loadResultSuccess = function(datas, textStatus, jqXHR){
			
			var result = JSON.parse(datas.response);
			
			/**
			 * 将服务分为 基础服务、推荐服务、免费服务
			 */
			
			var contentFlag = result.data;
			
			//基础服务（根据答题结果确定）	
			var baseContent = contentFlag[0];
			//推荐服务
			var recommendContent = contentFlag[1];
			//免费服务
			var freeContent = contentFlag[2];
			
			/*
			 * 分析 决定。使用 两套模板,添加 不同分类的 服务
			 * 	
			 *    基础服务 和 推荐服务 格式相同	 
			 * 	  免费服务
			 * 
			 *  流程：  1   使用模板填充数据，
			 *  	 2   用临时变量，拼接 每个填充数据的 模板， 
			 *       3  将填充完的临时变量,加入进 展示区域
			 */
			
			//子服务是单选的模板
			var radioChildTemplateHtml = $$("#childRadioUl").html();
			
			//子服务是多选的模板
			var checkboxChildTemplateHtml = $$("#childCheckboxUl").html();
			
			//子服务是单选的 临时变量
			var radioChildHtml = "";
			//子服务是多选的临时变量
			var checkboxChildHtml = "";
			
			/*
			 * 2016年3月17日16:11:29   点击多选后，由子页面 回到 当前页面时，回显 之前的 修改 结果
			 */
			var storeBase  = localStorage['storeBaseArray'];
			
			var storeRadio = localStorage['storeRadioArray'];
			
			if(storeBase != undefined){
				
				var baseArray =  JSON.parse(storeBase);
				
				//回显 无服务的  服务的  之前选中的 次数
				for (var j=0; j< baseArray.length; j++ ) {
					
					var baseItem = baseArray[j];
					
					for (var i = 0; i < baseContent.length; i++) {
						
						var paramBaseItem = baseContent[i];
						
						if(baseItem.baseContentId == paramBaseItem.content_id){
							
							if(paramBaseItem.base_content_real_time != 0){
								paramBaseItem.base_content_real_time = baseItem.baseContentTimes;
							}else{
								paramBaseItem.default_time = baseItem.baseContentTimes;
							}
							
						}
					}
					
					for(var i = 0; i < recommendContent.length; i++){
						
						var paramRecmmendItem = recommendContent[i];
						
						if(baseItem.baseContentId == paramRecmmendItem.content_id){
							
							if(paramRecmmendItem.base_content_real_time != 0){
								paramRecmmendItem.base_content_real_time = baseItem.baseContentTimes;
							}else{
								paramRecmmendItem.default_time = baseItem.baseContentTimes;
							}
							
						}
					}
					
				}
			}
			
			
			if(storeRadio != undefined){
				
				var radioArray =  JSON.parse(storeRadio);
				
				//回显 无服务的  服务的  之前选中的 次数
				for (var j= 0; j< radioArray.length; j++) {
					
					var radioItem = radioArray[j];
					
					for (var i = 0; i < baseContent.length; i++) {
						
						var paramBaseItem = baseContent[i];
						
						if(radioItem.baseContentId == paramBaseItem.content_id){
							
							paramBaseItem.child_list[0].default_time_child = 
								
								radioItem.childRadioArray[0].childRadioContentTimes;
						}
					}
					
					for(var i = 0; i < recommendContent.length; i++){
						
						var paramRecmmendItem = recommendContent[i];
						
						if(radioItem.baseContentId == paramRecmmendItem.content_id){
							
							paramRecmmendItem.child_list[0].default_time_child = 
								
								radioItem.childRadioArray[0].childRadioContentTimes;
						}
					}
					
				}
				
			}
			
			
			
			/*
			 * 1.基础服务
			 */
			//基础服务 临时变量
			var baseContentHtml = "";
			//基础服务 主体 模板对象
			var baseTemplateHtml = $$("ul[id='baseContentTemplate']").html();
			
			for (var i = 0; i < baseContent.length; i++) {
				var baseI = baseContent[i];
				
				var part = baseTemplateHtml;
				
				//由选项确定的服务次数, 如果为0, 则不展示，不为0，展示
				var optionDecideTime = baseI.base_content_real_time;
				
				//次数 展示  文字 “次/年。。”
				var timeStr = generateTimeWordForTimes(baseI.measurement);
				
				//单选类型,默认 需要提供  默认选中的id
				if(baseI.content_child_type == 1){
					
					//单选的 第一个选项,设置为默认选中,其他的 无所谓
					var defaultRadioId = baseI.child_list[0].id;
					
					//第一个 选项的id 为默认选中的  单选次数
					part = part.replace(new RegExp('{childRadioContentId}',"gm"), defaultRadioId);
					
					var defaultTimes = baseI.child_list[0].default_time_child;
					
					//默认次数
					part = part.replace(new RegExp('{defaultTimes}',"gm"), defaultTimes+timeStr);
				}
				
				//多选题,展示为1次
				if(baseI.content_child_type == 2){
					part = part.replace(new RegExp('{defaultTimes}',"gm"), "1"+timeStr);
				}
				
				//如果无子服务，展示默认次数
				if(baseI.content_child_type == 0){
					
					if(optionDecideTime == 0){
						part = part.replace(new RegExp('{defaultTimes}',"gm"), baseI.default_time+timeStr);	
					}else{
						part = part.replace(new RegExp('{defaultTimes}',"gm"), optionDecideTime+timeStr);	
					}
				}
				
				part = part.replace(new RegExp('{contentName}',"gm"), baseI.name);
				part = part.replace(new RegExp('{price}',"gm"), baseI.price);
				part = part.replace(new RegExp('{contentId}',"gm"), baseI.content_id);
				part = part.replace(new RegExp('{contentChildType}',"gm"), baseI.content_child_type);
				
				part = part.replace(new RegExp('{serviceColor}',"gm"), "button button-fill service-button1");
				
				part = part.replace(new RegExp('{serviceChangeButtonColor}',"gm"), "button button-fill service-button");
				
 				baseContentHtml += part;
			}
			
			$$("#baseDisplay").append(baseContentHtml);
			
			/*
			 * 2. 推荐服务
			 */
			var recommendHtml = "";
			var recommendTemplateHtml = $$("ul[id='baseContentTemplate']").html();
			
			for (var i = 0; i < recommendContent.length; i++) {
				var recommendI = recommendContent[i];
				
				var part = recommendTemplateHtml;
				
				//次数 展示
				var timeStr = generateTimeWordForTimes(recommendI.measurement);
				
				//单选类型,默认 需要提供  默认选中的id
				if(recommendI.content_child_type == 1){
					
					//单选的 第一个选项,设置为默认选中,其他的 无所谓
					var defaultRadioId = recommendI.child_list[0].id;
					
					//第一个 选项的id 为默认选中的  单选次数
					part = part.replace(new RegExp('{childRadioContentId}',"gm"), defaultRadioId);
					
					var defaultTimes = recommendI.child_list[0].default_time_child;
					
					//默认次数
					part = part.replace(new RegExp('{defaultTimes}',"gm"), defaultTimes+timeStr);
				}
				
				if(recommendI.content_child_type == 2){
					part = part.replace(new RegExp('{defaultTimes}',"gm"), "1"+timeStr);
				}
				
				if(recommendI.content_child_type == 0){
					
					//2016-1-13 18:16:46 推荐服务中 可能存在， 次数由 选项决定的 服务，而该服务又未被选中，则默认展示为1
					if(recommendI.default_time == 0){
						part = part.replace(new RegExp('{defaultTimes}',"gm"), "1"+timeStr);
					}else{
						part = part.replace(new RegExp('{defaultTimes}',"gm"), recommendI.default_time+timeStr);
					}
					
				}
				
				part = part.replace(new RegExp('{contentName}',"gm"), recommendI.name);
				part = part.replace(new RegExp('{price}',"gm"), recommendI.price);
				part = part.replace(new RegExp('{contentId}',"gm"), recommendI.content_id);
				part = part.replace(new RegExp('{contentChildType}',"gm"), recommendI.content_child_type);
				
				part = part.replace(new RegExp('{serviceColor}',"gm"), "button button-fill service-button2");
				
				part = part.replace(new RegExp('{serviceChangeButtonColor}',"gm"), "button button-fill service-button2");
				
				recommendHtml += part;
			}
			
			$$("#recommendDisplay").append(recommendHtml);
			
			/*
			 * 3.免费服务
			 */
			var freeHtml = "";
			var freeTemplateHtml = $$("ul[id='freeContentTemplate']").html();
			
			for (var i = 0; i < freeContent.length; i++) {
				var freeI = freeContent[i];
				
				var part = freeTemplateHtml;
				
				part = part.replace(new RegExp('{contentName}',"gm"), freeI.name);
				
				freeHtml += part;
			}
			
			$$("#freeDisplay").append(freeHtml);
			
			//数据加载完,打开一个 折叠布局
			myApp.accordionOpen("#baseAccordionItem");
			
			return false;
		};
		
		//进入页面加载,答题结果
		var postData = {};
		
		postData.survey_user_id = localStorage['survey_user_id'];
		
		postData.survey_result = localStorage['surveyResult'];
		
		
		//第一次加载页面时,加载第一题
		$$.ajax({
			type: "post",
			 url: siteAPIPath + "survey/survey_result.json",
			data: postData,
			statusCode: {
	         	200: loadResultSuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});
		
		
	
		/**
		 * 点击提交 订制结果
		 */
		
		$$("#submitResult").on("click",function(){
			
			$$(this).attr("disabled",true);
			
			storeNowData();
			
			/*
			 * 构造 参数
			 */
			var postData ={};
			
			//无子服务
			postData.base_content_array = localStorage['storeBaseArray'];
			//单选
			postData.radio_content_array = localStorage['storeRadioArray'];
			
			//多选
			if(localStorage['boxChildContent'] != undefined ){
				postData.box_content_array = localStorage['boxChildContent'];
				postData.default_box_content_array = "[]";
			}else{
				/*
				 * 多选的 默认值, 该多选对应的 主 服务 id
				 */
				postData.default_box_content_array = localStorage['storeChildBoxArray'];
				
				postData.box_content_array = "[]";
			}
			
			//用户 id
			postData.user_id = localStorage['survey_user_id'];
			
			//对于 完成  又返回的 情况~
			if(localStorage['survey_user_id'] == undefined){
				
				myApp.alert("请勿重复提交");
				mainView.router.loadPage("index.html");
				return false;
			}
			
			
			//发请求
			$$.ajax({
				type: "post",
				 url: siteAPIPath + "survey/submit_survey_result.json",
				data: postData,
				success:function (datas,sta,xhr){
					
					var result = JSON.parse(datas);
					
					if(result.status == "999"){
						myApp.alert(result.msg);
						return false;
					}
					
					
					$$("#submitResult").removeAttr('disabled');  
					
					mainView.router.loadPage("survey/survey-result-price.html");
				},
				error:function(){
					myApp.alert("网络错误");
				}
			});	
		});
		
		
		
		/*
		 * 展开、折叠效果，给出文字提示
		 */
		$$('.accordion-item').on('open', function () {
			  
			changeAccordText(this);
		}); 
			 
		$$('.accordion-item').on('close', function (e) {
			  
			changeAccordText(this);
		}); 	
		
		
		function changeAccordText(obj){
			
			 var accordText =  $$(obj).find("font").text();
			  
			  if(accordText =="展开"){
				  $$(obj).find("font").text("收起");
			  }else{
				  $$(obj).find("font").text("展开");
			  }
		}
		
});

function storeNowData(){
	
	//展示区域 的所有 被选中的  服务	
	var aaObj =   $$("#resultDiv").find("a[id='contentName'][class='button button-fill service-button1']");
	
	//存放  无子服务  类型的 {内容：次数}array
	var baseContentArray = [];
	
	//存放 子服务 为 单选的 array {服务,子服务：次数}
	var radioContentArray = [];
	
	//对于多选,如果未修改,但是也选中的话,需要传递  该 服务内容Id，并在后台处理  该服务的 子服务
	var childBoxArray = [];
	
	
	$$(aaObj).each(function(k,v){
		
		//‘调整次数  ’按钮  所在 div,包含隐藏域信息
		var contentDiv = $$(this).parent().parent().find("#contentDiv");
		
		// 次数 所在 div
		var timesDiv =$$(this).parent().parent().find("#timesDiv");
		
		//次数数字 每行 对应的数字
		var times = timesDiv.find("#contentTimes").text().match(/\d/g).join("");
		
		var childType =  contentDiv.find("#contentChildType").val();
		
		/*
		 * 主服务id
		 */
		var baseContentId = contentDiv.find("#contentId").val();
		
		//没有子服务的 类型
		if(childType == 0){
			
			baseContentArray.push({"baseContentId":baseContentId,"baseContentTimes":Number(times)});
		}
		
		//单选类型
		if(childType == 1){
			
			var childRadioContentId =  timesDiv.find("input[id='childRadioContentId']").val();
			
			//存放 单选子服务的 {子服务：次数}
			var childRadioArray = [];
			
			childRadioArray.push({"childRadioContentId":childRadioContentId,"childRadioContentTimes":Number(times)});
			
			radioContentArray.push({"baseContentId":baseContentId,"childRadioArray":childRadioArray});
		}
		
		//多选类型, 在 新页面 已经存储
		if(childType == 2){
			childBoxArray.push({"baseContentId":baseContentId});
		}
		
	});
	
	localStorage.setItem("storeBaseArray",JSON.stringify(baseContentArray));
	
	localStorage.setItem("storeRadioArray",JSON.stringify(radioContentArray));
	
	localStorage.setItem("storeChildBoxArray",JSON.stringify(childBoxArray));
}



/*
 * 给服务次数,添加期限  --> "次/月"、"次/年"
 * 	
 * 	将 量词字段 转换为 string "次/月"
 */
function generateTimeWordForTimes(obj){
	
	var str = "";
	
	if(obj == 0){
		str = "次/月";
	}
	if(obj == 1){
		str = "次/年"
	}
	if(obj == 2 || obj== 3){
		str = "次";
	}
	
	return str;
}



/*
 * 点击 服务名称 按钮 变色
 */
function changeColor(obj){
	
	var color = $$(obj).attr("class");
	
	if(color.indexOf("service-button1")>0){
		$$(obj).attr("class","button button-fill service-button2");
		
		//同时禁用调整次数按钮
		
		var  aButtonObj = $$(obj).parent().parent().find("a[id='changeThisTimes']");
		
		aButtonObj.attr("class","button button-fill service-button2");
		aButtonObj.attr("disabled",true);
		
	}
	
	if(color.indexOf("service-button2")>0){
		$$(obj).attr("class","button button-fill service-button1");
		//打开调整次数按钮
		
		var  aButtonObj = $$(obj).parent().parent().find("a[id='changeThisTimes']");
		aButtonObj.attr("class","button button-fill service-button");
		aButtonObj.removeAttr('disabled');
		
	}
	
}



/*
 * 点击调整次数, 去后台获取该 服务的 子服务 
 */
function changeThisTimes(obj){
	
	
	//如果是 灰色 ，不让点击
	if($$(obj).attr("class").indexOf("button2")>0){
		
		return false;
	}
	
	//子服务类型
	var childType =  $$(obj).parent().find("#contentChildType").val();
	
	//当前服务  id
	var contentId =  $$(obj).parent().find("#contentId").val();
	
	/**
	 *	经验证, 在弹窗时，可以获得  "单选"时的值
	 *			
	 *		故: 多选题，跳转到新页面进行 + -修改次数操作
	 *		   
	 *		单选和 无子服务的 弹窗修改
	 */
	
	//如果是多选，则跳转页面进行修改次数
	if(childType == 2){
		
		/*
		 *  多选题进入新页面之前，存储 当前页面的 调整结果，返回时 回显
		 */
		storeNowData();
		
		localStorage.setItem("changeContentId",contentId);
		
		var successUrl = "survey/survey-result-child.html";
		
		mainView.router.loadPage(successUrl);
	}
	
	//没有子服务,不发请求
	if(childType == 0){
		
		myApp.prompt('', '次数修改',
		      function (value) {
				 	
					if(!isPositiveNum(value) || Number(value) > 700 || value.indexOf(0) == 0){
						myApp.alert("请输入小于700的整数数字");
						return false;
					}
					
					//次数所在的span对象
					var numberSpanObj = $$(obj).parent().parent().parent().find("span[id='contentTimes']");
					
					//替换显示的内容
					numberSpanObj.text(numberSpanObj.text().replace(/\d+/g,value));
		      });
		return false;	
	}
	
	//当前服务  id
	var contentId =  $$(obj).parent().find("#contentId").val();
	
	var postData = {};
	
	postData.content_id = contentId;
	
	$$.ajax({
		type: "get",
		 url: siteAPIPath + "survey/get_child_content_list.json",
		data: postData,
		success:function (datas,sta,xhr){
			
			var result = JSON.parse(datas);
			
			var reLength = result.data.length;
				
			var childHtml = "";
			
			//如果当前是单选题
			if(childType == 1){
				for (var i = 0; i < reLength; i++) {
					var child = result.data[i];
					
					//设置  默认次数的 选中
					if(child.id == $$(obj).parent().parent().parent().find("#timesDiv").find("#childRadioContentId").val()){
						childHtml += "<li>"
								+	"<input type='hidden' id='childRadioId' value="+child.default_time_child+">"
								+	"<input type='radio' checked name='childRadio' value="+child.id+">"
								+ 	child.option_str	
								+ "</li>" 		
					}else{
						
						childHtml += "<li>"
							+	"<input type='hidden' id='childRadioId' value="+child.default_time_child+">"
							+	"<input type='radio'  name='childRadio' value="+child.id+">"
							+ 	child.option_str	
							+ "</li>"
					}
				}
			}
			
			
			$$("#childReplace").html("");
			$$("#childReplace").append(childHtml);
			
			if(childType != 2){
				
				myApp.confirm($$("#childReplace").html(),'<b>服务次数调整</b>',function(){
					
					/*
					 * 因为每次 点击时，加载的子服务 都会与 当前 服务 对应。（有清空操作）所以无需担心，会有 数据累积的情况
					 */
					
					//如果是单选题,存储 （内容id、子服务id）
					if(childType == 1){
						
						//选中的子服务的次数 --对应的 子服务 id
						var radioChildContentId = $$("input[type='radio'][name='childRadio']:checked").val();
						
						//将该id 赋值给隐藏域,供提交
						$$(obj).parent().parent().parent().find("#timesDiv").find("#childRadioContentId").val(radioChildContentId);
						
						//选中的 子服务的次数 内容，替换默认值
						var radioChildContentNumber = $$("input[type='radio'][name='childRadio']:checked")
												.parent().find("input[id='childRadioId']").val();
						
						//次数所在的 span 对象
						var numberObj = $$(obj).parent().parent().parent().find("span[id='contentTimes']");
						
						//选中后,替换默认次数
						numberObj.text(numberObj.text().replace(/\d+/g,radioChildContentNumber));
						
					}	
				});
			}
		},
		error:function(){
			myApp.alert("网络错误");
		}
	});
};

//判断是否是正整数
function isPositiveNum(s){//是否为正整数 
	var re = /^[0-9]*[1-9][0-9]*$/ ; 
	return re.test(s); 
} 


//处理 子服务 如  ：空调 4次 。。。
function replaceChildContentBoxNumber(str){
	
	//取得 字符串中的 数字
	var number = str.match(/\d/g).join("");
	
	//替换数字为 可输入的输入框
	str = str.replace(/\d+/g,"<input type='tel' id='finalChildTimes' maxlength='3' class='input-child-content-box-style' value='' placeholder='"+number+"' >")
	
	return str;
}




;myApp.onPageInit('survey-user-page', function(page) {
	
	/*
	 * 提交订单
	 */
	
	$$("#submitSurveyUser").click(function(){
		
		if(surveyUserValidation() == false){
			return false;
		}
		
		
		// 文档要求： post请求，必须是 formData类型
		var formData = myApp.formToJSON('#survey-user-form');
		
		$$.ajax({
			type: "post",
			 url: siteAPIPath + "survey/survey_user_submit.json",
			data: formData,
			success: function (data, status, xhr){
			
				var result = JSON.parse(data);
				
				if(result.status == "999"){
					myApp.alert(resut.msg);
					return false;
				}
				
				/**
				 * 存储当前调查填写的用户信息id
				 */
				localStorage.setItem("survey_user_id",result.data);
				
				
				var successUrl = "survey/survey-question.html";
				mainView.router.loadPage(successUrl);
			
		 },
		  error: function(status,xhr){
			  	myApp.alert("网络异常,请稍后再试.");
		 }
		});
	});
	
});

function surveyUserValidation(){
	
	//去除空白字符串
	var userName = $$("#userName").val().replace(/\s/g, "");
	
	if(userName.length < 1 || userName == undefined){
		myApp.alert("请输入您的姓名");
		return false;
	}
	
	var age = $$("#age").val();
	
	if(isNaN(age) || age.indexOf(0) === 0 || age.replace(/\s/g, "") == ""){
		myApp.alert("请输入合法的年龄");
		return false;
	}
	
	var mobile = $$("#mobile").val();
	
	if(!isPhone(mobile)){
		myApp.alert("请输入合法的手机号");
		return false;
	}
}

function isPhone(aPhone) {
	var bValidate = RegExp(
			/^(0|86|17951)?(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|19[0-9])[0-9]{8}$/)
			.test(aPhone);
	if (bValidate) {
		return true;
	} else
		return false;
}

