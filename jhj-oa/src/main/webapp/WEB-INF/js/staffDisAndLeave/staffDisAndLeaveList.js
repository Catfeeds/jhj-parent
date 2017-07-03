
$(document).ready(function(){
	
	var jsonServerStr = $("#returnListData").val();
	
	var jsonObj = JSON.parse(jsonServerStr);
	
	$.each(jsonObj,function(k,v){
		
		var staffName = v.staffName;
		var staffId = v.staffId;
		
		$.each(v.timeEventList,function(tk,tv){
			
			var timeStr = tv.timeStr;
			//具体事件
			var eventVoList = tv.eventList;
			$("#resultThead").find("th").each(function(thk,thv){
				if($(this).text() == timeStr){
					var am="";
					var pm="";
					$.each(eventVoList,function(eventVoK,eventVoV){
						var serviceDate=eventVoV.serviceTime;
						var s=moment(serviceDate).format('YYYY-MM-DD')+" 12:00:00";
						var date = moment(s,'YYYY-MM-DD HH:mm:ss').unix();
						var orderNo=eventVoV.orderNo;
						var orderType=eventVoV.orderType;
						var allDay = eventVoV.allDay;
						var allDayStr = eventVoV.allDayStr;
						if (allDay == "" || allDay == "allDay" || allDay == "am") {
							var eventStrAM = "";
							eventStrAM +="<font color='red'>"+ allDayStr +"</font>"+ eventVoV.dateDuration +" "+ eventVoV.eventName+"<br/>";
							if(orderNo==null){
								am=eventStrAM;
							}
							if(orderNo!=null){
								am+="<a href='../order/order-list?orderNo="+orderNo+"'><b>"+eventStrAM+"</b></a>"
							}
						} else {
							var eventStrPM="";
							eventStrPM +="<font color='green'>"+ allDayStr +"</font>"+ eventVoV.dateDuration +" "+ eventVoV.eventName+"<br/>";
							if(orderNo==null){
								pm=eventStrPM;
							}
							if(orderNo!=null){
								pm +="<a href='../order/order-list?orderNo="+orderNo+"'><b>"+eventStrPM+"</b></a>"
							}
							
						}
					});
					$("#resultTr").find("td div").eq(thk-1).html(am);
					$("#resultTr").find("td span").eq(thk-1).html(pm);
				}
			});
		});
		$("#resultTr").find("td").eq(0).text(staffName);
		
		$("#resultDisplay").append("<tr>"+$("#resultTr").html()+"</tr>");
	});
});



$('.form_datetime').datepicker({
	format : "yyyy-mm-dd",
	language: "zh-CN",
	autoclose: true,
	todayBtn:true,
	defaultDate: new Date(),
	startView : 0
});



$("#startTimeStr").datepicker().on('changeDate', function(ev){
	
var startTimeStr =  $(this).val();
	
	var startTimeStamp =  moment(startTimeStr, "yyyy-MM-dd").unix();
	
	//从今天开始算 一个7天的 时间段，用来展示数据
	var aaa =  moment(startTimeStr).add(6,'d');
	
	var endTimeStr =  moment(aaa).format("YYYY-MM-DD");
	
	$("#endTimeStr").val(endTimeStr);
	
});






