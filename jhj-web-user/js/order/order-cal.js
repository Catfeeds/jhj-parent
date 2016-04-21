myApp.onPageBeforeInit('order-cal-page', function(page) {

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
			console.log("onMonthYearChangeStart= " + p.currentYear + "-" +p.currentMonth);
			
			
			$$('.calendar-custom-toolbar .center').text(
					monthNames[p.currentMonth] + ', ' + p.currentYear);
			
			if (p.currentMonth != loadedMonth) {
				loadTotalByMonth(userId, p.currentYear, p.currentMonth);
			}
			
		},
		
		onDayClick :function (p, dayContainer, year, month, day) {
			var cmonth = parseInt(month) + 1;
			console.log(year + "-" + cmonth + "-" + day);
			
			loadOrderList(userId, 1, year + "-" + cmonth + "-" + day)
		}
				
	};

	var calendarInline = myApp.calendar(calendarParams);
	
	var userId = userId;
	
	
	console.log("year = " + today.getFullYear() + "--- month = " + today.getMonth());
	
	
	
	function loadTotalByMonth(userId, year, month) {
		console.log("loadTotalByMonth month = " + month);
		var params = {};
		params.user_id = userId;
		params.year = year;
		params.month = month + 1;
		
		var apiUrl = "order/user_total_by_month.json";
		
		var totalByMonthSuccess = function(data, textStatus, jqXHR) {
			var result = JSON.parse(data.response);
			console.log(result);
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
	
	loadTotalByMonth(userId, today.getFullYear(), today.getMonth());
	
	
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
				// var img_tag = '<img alt="" src="img/icons/order_type_img_'+
				// order.order_type +'.png ">';
				// htmlPart = htmlPart.replace(new RegExp('{img_tab}',"gm"),
				// img_tag);
				htmlPart = htmlPart.replace(new RegExp('{order_type}', "gm"), order.order_type);
				htmlPart = htmlPart.replace(new RegExp('{order_no}', "gm"), order.order_no);
				htmlPart = htmlPart.replace(new RegExp('{order_hour_type_name}', "gm"),
						order.order_hour_type_name);
				htmlPart = htmlPart.replace(new RegExp('{order_hour_status_name}', "gm"),
						order.order_hour_status_name);

				if (order.order_type == 2) {
					htmlPart = htmlPart.replace(new RegExp('{service_date}', "gm"), moment.unix(
							order.add_time).format("YYYY-MM-DD HH:mm"));
				} else {
					htmlPart = htmlPart.replace(new RegExp('{service_date}', "gm"), moment.unix(
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
//			if (orders.length < 1) {
//				console.log("dispaly < 1");
//				$$('#order-list-more').css("display", "none");
//				return;
//			}
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
	
	
	
	console.log(today.getFullYear() + "-" + today.getMonth() + "-" + today.getDay());
	var todayStr = today.getFullYear() + "-" + (parseInt(today.getMonth()) + 1) + "-" + today.getDay();
	loadOrderList(userId, page, todayStr);
	// 注册'infinite'事件处理函数
	$$('#order-list-more').on('click', function() {
		var cpage = $$("#page").val();
		console.log("cpage = " + page);
		loadOrderList(userId, cpage, curClickDay);
	});

});




