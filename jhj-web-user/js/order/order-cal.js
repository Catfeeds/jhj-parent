myApp.onPageBeforeInit('order-cal-page', function(page) {

	var userId = localStorage.getItem("user_id");
	var monthNames = [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月',
			'九月', '十月', '十一月', '十二月' ];

	var calendarInline = myApp.calendar({
		container : '#calendar-inline-container',
		value : [ new Date() ],
		weekHeader : false,
		toolbarTemplate : '<div class="toolbar calendar-custom-toolbar">'
				+ '<div class="toolbar-inner">' + '<div class="left">'
				+ '<a href="#" class="link icon-only"><i class="icon icon-back"></i></a>'
				+ '</div>' + '<div class="center"></div>' + '<div class="right">'
				+ '<a href="#" class="link icon-only"><i class="icon icon-forward"></i></a>'
				+ '</div>' + '</div>' + '</div>',
		onOpen : function(p) {
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
			$$('.calendar-custom-toolbar .center').text(
					monthNames[p.currentMonth] + ', ' + p.currentYear);
		}
	});

});
