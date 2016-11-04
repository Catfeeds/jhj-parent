//Global host+url
var host = window.location.host;
var appName = "jhj-oa";

var appRootUrl = "http://" + host + "/" + appName + "/";
/*---LEFT BAR ACCORDION----*/
$(function() {
	$('#nav-accordion').dcAccordion({
		eventType : 'click',
		autoClose : true,
		saveState : true,
		disableLink : true,
		speed : 'slow',
		showCount : false,
		autoExpand : true,
		// cookie: 'dcjq-accordion-1',
		classExpand : 'dcjq-current-parent'
	});
});

var Script = function() {
	
	// sidebar dropdown menu auto scrolling
	
	jQuery('#sidebar .sub-menu > a').click(function() {
		var o = ($(this).offset());
		diff = 250 - o.top;
		if (diff > 0) $("#sidebar").scrollTo("-=" + Math.abs(diff), 500);
		else $("#sidebar").scrollTo("+=" + Math.abs(diff), 500);
	});
	
	// sidebar toggle
	
	$(function() {
		function responsiveView() {
			var wSize = $(window).width();
			if (wSize <= 768) {
				$('#container').addClass('sidebar-close');
				$('#sidebar > ul').hide();
			}
			
			if (wSize > 768) {
				$('#container').removeClass('sidebar-close');
				$('#sidebar > ul').show();
			}
		}
		$(window).on('load', responsiveView);
		$(window).on('resize', responsiveView);
	});
	
	$('.icon-reorder').click(function() {
		if ($('#sidebar > ul').is(":visible") === true) {
			$('#main-content').css({
				'margin-left' : '0px'
			});
			$('#sidebar').css({
				'margin-left' : '-210px'
			});
			$('#sidebar > ul').hide();
			$("#container").addClass("sidebar-closed");
		} else {
			$('#main-content').css({
				'margin-left' : '210px'
			});
			$('#sidebar > ul').show();
			$('#sidebar').css({
				'margin-left' : '0'
			});
			$("#container").removeClass("sidebar-closed");
		}
	});
	
	// custom scrollbar
	$("#sidebar").niceScroll({
		styler : "fb",
		cursorcolor : "#e8403f",
		cursorwidth : '3',
		cursorborderradius : '10px',
		background : '#404040',
		spacebarenabled : false,
		cursorborder : ''
	});
	
	$("html").niceScroll({
		styler : "fb",
		cursorcolor : "#e8403f",
		cursorwidth : '6',
		cursorborderradius : '10px',
		background : '#404040',
		spacebarenabled : false,
		cursorborder : '',
		zindex : '1000'
	});
	
	// widget tools
	
	jQuery('.panel .tools .icon-chevron-down').click(function() {
		var el = jQuery(this).parents(".panel").children(".panel-body");
		if (jQuery(this).hasClass("icon-chevron-down")) {
			jQuery(this).removeClass("icon-chevron-down").addClass("icon-chevron-up");
			el.slideUp(200);
		} else {
			jQuery(this).removeClass("icon-chevron-up").addClass("icon-chevron-down");
			el.slideDown(200);
		}
	});
	
	jQuery('.panel .tools .icon-remove').click(function() {
		jQuery(this).parents(".panel").parent().remove();
	});
	
	// tool tips
	
	$('.tooltips').tooltip();
	
	// popovers
	
	$('.popovers').popover();
	
	// custom bar chart
	
	if ($(".custom-bar-chart")) {
		$(".bar").each(function() {
			var i = $(this).find(".value").html();
			$(this).find(".value").html("");
			$(this).find(".value").animate({
				height : i
			}, 2000)
		})
	}
	
}();

// 新增事件

function btn_add(path) {
	location.href = appRootUrl + path;
}

// 修改按钮事件
function btn_update(path) {
	// location.href = appRootUrl + "account/register?id=" + id;
	location.href = appRootUrl + path;
}

// 删除按钮事件
function btn_del(path) {
	var statu = confirm("确定要删除吗?");
	if (!statu) {
		return false;
	}
	// location.href = appRootUrl + "account/delete/" + id;
	location.href = appRootUrl + path;
}

// 查看按钮事件
function btn_select(path) {
	location.href = appRootUrl + path;
}

// 菜单点击展开
function setSubMenuId(menuId) {
//	console.log("setSubMenuId ==" + menuId)
	$.cookie("menu-sub-id", menuId, {
		path : "/"
	});
	menuHighLight();
}

function menuHighLight() {
//	console.log("menuHighLight");
	var menuId = $.cookie('menu-sub-id');
	
	if (menuId == undefined) return false;
	if (menuId == "") return false;
	
	$(".sub").each(function() {
		$(this).find('li').each(function() {
			var tmenuId = $(this).attr("id");
			
//			console.log("tmenuId = " + tmenuId + "=== menuId = " + menuId);
			
			if (tmenuId == menuId) {


				$("#" + tmenuId).addClass("active");
//				$("#" + tmenuId).attr("style",""); 
			} else {

				$("#" + tmenuId).removeClass("active");
//				$("#" + tmenuId).attr("style","height: 0px;"); 
				
			}
		});
	});
}

menuHighLight();