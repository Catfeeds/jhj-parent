/**
 * Created by hulj on 2016/10/20.
 */

function showCalendar(){
	var html = $("#calendar-show").html();
	
	layer.open({
		  type: 1,
		  title: '日历',
		  closeBtn: 1, //不显示关闭按钮
		  shadeClose: false,
		  shade: [0],
		  id:"layer-calendar1",
		  area: ['700px', '550px'],
		  content: html
	});
}

$(function(){
	var weekDay=['周日','周一','周二','周三','周四','周五','周六'];
	var tempWeek=['周日','周一','周二','周三','周四','周五','周六'];
	var time=['08:00','08:30','09:00','09:30','10:00','10:30','11:00','11:30','12:00','12:30','13:00','13:30',
	          '14:00','14:30','15:00','15:30','16:00','16:30','17:00','17:30','18:00'];
	var dayNum=7;
	//获取当前日期
	var date=moment().format("YYYY-MM-DD");
	var currentDate=moment().format("YYYY-MM-DD");
	var nowHour=moment().hour();
	var dayTime=""; 
	var selectDay="#show-day li p[class='rili-day']";
	
	//展示时间 
	function showTime(selectDate,val){
		var dateTime='';
		var orderServiceHour=0;
		var html = $("#show-dateTime").html();
		for(var i=0;i<time.length;i++){
			if(val==undefined || val==null || val==''){
				dateTime+="<li>"+time[i]+"</li>";
			}else{
				orderServiceHour = val[0].order_service_hour;
				for(var j = 0 ; j < val.length;j++){
					if(time[i]==val[j].service_hour){
						if(val[j].is_full==0){
							dateTime+="<li>"+time[i]+"</li>";
						}
						if(val[j].is_full==1){
							dateTime+="<li class='rili-time-no'><p>"+time[i]+"</p><p>约满</p></li>";
						}
					}
				}
			}
		}
		$(".rili1-5").html(dateTime);
		$(document).on("click",'#show-dateTime li',function(obj){
			$("#show-dateTime li").removeClass("rili-time");
			$(this).addClass("rili-time");
			if($(this).hasClass("rili-time-no")){
				dayTime="";
				$(this).removeClass("rili-time");
				$(this).parent().next(".rili1-6").find("a #checkDate").removeClass("rili1-6-1").addClass("rili1-6-2");
			}else{
				dayTime=$(this).text();
				$(this).parent().next(".rili1-6").find("a #checkDate").removeClass("rili1-6-2").addClass("rili1-6-1");
			}
			
//	        var selectTime = dayTime.replace(":",".");
//	        var selectTimeStart = parseFloat(selectTime)-2;
//	        var selectTimeEnd = parseFloat(selectTime)+parseFloat(orderServiceHour)+1.5;
//	        for(var k=0;k<val.length;k++){
//	        	var valTime = val[k].service_hour;
//	        	var parseTime = valTime.replace(":",".");
//	        	if(parseTime>=selectTimeStart && parseTime<=selectTimeEnd){
//	        		if(val[k].is_full==1){
//	        			alert("这个时间不能选择");
//	        			break;
//	        		}
//	        	}
//	        }
		});
		tomm(currentDate);
	}
	showTime();
	
	//显示年月
	function showYearMonth(date){
		var calendar = date;
		$(".rili1-1").text(moment(calendar).format("YYYY"));
		$(".show-month").text(moment(calendar).format("MM"));
	}
	showYearMonth(date);
	
	//获取当前选择的时间，如何没有选择时间默认是当前时间
	function getServiceDate(element){
		var serviceDate='';
		var year = $("#show-year").text();
		var month = $("#show-month").text();
		var day = $("#show-day li p[class='rili-day']").text();
		if(day==undefined || day==null || day==''){
			day = $(element).text();
		}
		var pre_li = $(element).prevAll("li");
		var after_li = $(element).nextAll("li");
		var flag=0;
		var flag1=0;
		var flag2=0;
		if(pre_li.length>0 && after_li.length>0){
			for(var i=0;i<pre_li.length;i++){
				var val = $(pre_li[i]).text();
				if(val<day){
					flag++;
				}else{
					flag1++;
				}
			}
			for(var j=0;j<after_li.length;j++){
				var val=$(after_li[j]).text();
				if(val>day ||val<day){
					flag2++;
				}
			}
			if(flag==pre_li.length && flag2==after_li.length){
				serviceDate=year+"-"+month+"-"+day;
			}else{
				serviceDate = moment(year+"-"+month+"-"+day).add(1,'M').format("YYYY-MM-DD");
			}
		}
		
		if(pre_li.length==0){
			var nextVal=$(after_li[0]).text();
			var next5Val=$(after_li[5]).text();
			if(nextVal>day || (nextVal<day && next5Val<day)){
				serviceDate=year+"-"+month+"-"+day;
			}else{
				serviceDate = moment(year+"-"+month+"-"+day).add(1,'M').format("YYYY-MM-DD");
			}
		}
		if(after_li.length==0){
			var preVal=$(pre_li[0]).text();
			var pre5Val=$(pre_li[5]).text();
			if(preVal<day && pre5Val<day){
				serviceDate=year+"-"+month+"-"+day;
			}else{
				serviceDate = moment(year+"-"+month+"-"+day).add(1,'M').format("YYYY-MM-DD");
			}
		}
		
//        date = serviceDate;
		return  serviceDate;
	}
	
	//是否约满
	function isFull(serviceDateStr){
		
		var host = window.location.host;
		var appName = "jhj-app";
		var localUrl = "http://" + host;
		var siteAPIPath = localUrl + "/" + appName + "/app/";
		var url = "";
		
		var param = {};
		var serviceTypeId = $("#serviceType").val();
		if (serviceTypeId == undefined || serviceTypeId == 0) return false;
		param.service_type_id = $("#serviceType").val();
		
		var addrId = $("#addrId").val();
		if (addrId == undefined || addrId == 0) {
			var lat = $("#poiLatitude").val();
			var lng = $("#poiLatitude").val();
			if (lat == undefined || lat == "" || lng == undefined || lng == "") return false;
			param.lat = lat;
			param.lng = lng;
			url = siteAPIPath+"order/check_dispatch_by_poi.json";
		} else {
			param.addr_id = $("#addrId").val();
			url = siteAPIPath+"order/check_dispatch.json";
		}
		
		
		
		if(serviceDateStr==undefined || serviceDateStr==null || serviceDateStr==''){
			serviceDateStr = moment().format("YYYY-MM-DD");
		}
		param.service_date_str = serviceDateStr;
		param.staff_id = 0;
		
		$.ajax({
			type:"POST",
			url:url,
			data:param,
			success:function(data){
				if(data.status=='0' && data.msg=='ok'){
					var result = data.data;
					showTime(serviceDateStr,result);
				}
			}
		});
	}
	//日历天数显示
	function getDay(cal){
		var contentDay="";
		var contentWeek="";
		if(cal==undefined || cal == null || cal =="") return ;
		var cmp=moment(cal).format("YYYY-MM-DD");
		for(var i=0;i<7;i++){
			var d = moment(cal).add(i,'days');
			var week=d.format('d');
			contentDay+="<li><p>"+d.format('DD')+"</p></li>";
			//显示今天明天
			if(cmp==currentDate){
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
		$(".rili1-3").html(contentWeek);
		$(".rili1-4").html(contentDay);
		
		$(document).on("click",'#show-day li',function(){
			selectDay = $(this);
			$(".rili1-4 li p").removeClass("rili-day");
			$(".rili1-5 li").removeClass("rili-time");
			$(this).parent().nextAll(".rili1-6").find("a #checkDate").removeClass("rili1-6-1").addClass("rili1-6-2");
			$(this).find("p").addClass("rili-day");
			var selectDate = getServiceDate(selectDay);
			if(selectDate==currentDate){
				if(nowHour>=16){
					$(".rili1-4 li p").removeClass("rili-day");
//					$(this).find("p").addClass("rili-day");
					$(selectDay).find("p").addClass("rili-day");
					$(".rili1-5 li").addClass("rili-time");
				}
				tomm(currentDate);
			}
			date = selectDate;
			isFull(selectDate);
		});
		$(".rili1-5 li").removeClass("rili-time-no");
		$(".rili1-4").find(":first-child p").addClass("rili-day");
		var compareDate = date;
		isFull(compareDate);
	}
	getDay(date);
	
	//日历增加或减少日期
	function get7Day(selectDay,countDay){
		var afterDay
		if(selectDay==undefined || selectDay==null || selectDay==''){
			afterDay = moment(date).add(countDay, 'days');
		}else{
			afterDay = moment(selectDay).add(countDay, 'days');
		}
		date = afterDay;
		showYearMonth(afterDay);
		getDay(afterDay);
	}
	
	//日历减1天
	$(document).on('click','#substranc-day',function(){
		$("#show-dateTime").find("li").removeClass("rili-time");
		$("#checkDate").removeClass("rili1-6-1").addClass("rili1-6-2");
		dayTime="";
		var service_date = date;
		var comp_day = moment(service_date).add(-7,'days').format("YYYY-MM-DD");
		if(comp_day>=currentDate){
			get7Day(service_date,-dayNum);
		}else{
			var days = moment(service_date).diff(moment(currentDate),'days');
			get7Day(service_date,-days);
		}
		/*if(comp_day<=currentDate && service_date>currentDate){
			var days = moment(service_date).diff(moment(currentDate),'days');
			get7Day(service_date,-days);
		}*/
	});
	
	//日历加1天
	$(document).on('click','#add-day',function(){
		$("#show-dateTime").find("li").removeClass("rili-time");
		$("#checkDate").removeClass("rili1-6-1").addClass("rili1-6-2");
		dayTime="";
		var service_date = date;
		get7Day(service_date,dayNum);
	});
	
	/**
	 *根据当前时间，判断下单可以选择的时间
	 *
	 * */
	function tomm(val){
		if(val==currentDate){
			var lis = $(".rili1-5").find("li");
			switch (nowHour) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:filterBackDate(lis,2);break;
			case 5:
			case 6:filterBackDate(lis,4);break;
			case 7:
			case 8:filterBackDate(lis,5);break;
			case 9:filterBackDate(lis,6);break;
			case 10:filterBackDate(lis,10);break;
			case 11:filterBackDate(lis,11);break;
			case 12:filterBackDate(lis,13);break;
			case 13:filterBackDate(lis,15);break;
			case 14:filterBackDate(lis,17);break;
			case 15:filterBackDate(lis,19);break;
			case 16:
			case 17:
			case 18:
			case 19: filterBackDate(lis,21);break;
			case 20:
			case 21:
			case 22:
			case 23:filterBackDate(lis,2);
			$("#show-day li").removeClass("rili-time");
			$("#show-day").find("li:nth-child(2)").addClass("rili-time");
				break;
			default:
				break;
			}
		}else{
			
		}
	}
	
	function filterBackDate(arrys,compNum){
		var html = '';
		for(var i=0;i<arrys.length;i++){
			var listext = $(arrys[i]).text();
			if(i<compNum){
				if(!$(arrys[i]).hasClass('rili-time-no')){
					$(arrys[i]).addClass("rili-time-no");
					html+="<li class='rili-time-no'><p>"+listext+"</p><p>约满</p></li>";
				}
			}else{
				html+="<li>"+listext+"</li>";
			}
		}
		$(".rili1-5").html(html);
	}
	
	//选择时间
	$(document).on("click",'#show-dateTime li', function(obj){
		$("#show-dateTime").find("li").removeClass("rili-time");
		$(this).addClass("rili-time");
		if($(this).hasClass("rili-time-no")){
			dayTime="";
			$(this).removeClass("rili-time");
			$("#checkDate").removeClass("rili1-6-1").addClass("rili1-6-2");
		}else{
			dayTime=$(this).text();
			$("#checkDate").removeClass("rili1-6-2").addClass("rili1-6-1");
		}
	});
	
	//获取选择的服务时间
	$(document).on('click','#checkDate',function(){
		var st = date+" "+dayTime+":00";
		var st =moment(date).format("YYYY-MM-DD")+" "+dayTime+":00";
		if(dayTime!=""){
			$("#serviceDate").val(st);
			layer.close(layer.index);
		}else{
			return false;
		}
	});
})


function selectServiceDateTime(){
	var addr_id = $("#addrId").val();
	if(addr_id==undefined || addr_id==null || addr_id==''){
		
		var lat = $("#poiLatitude").val();
		var lng = $("#poiLatitude").val();
		var name = $("#suggestId").val();
		if (lat == undefined || lat == "" || 
			lng == undefined || lng == "" ||
			name == undefined || name == "") {
			alert("请选择服务地址！");
			return false;
		}
	}
	
	var serviceTypeId = $("#serviceType").val();
	if (serviceTypeId == undefined || serviceTypeId == "") {
		alert("请选择服务类型!");
		return false;
	}
	
	showCalendar();
}
