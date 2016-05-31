
$(document).ready(function(){
	
	var jsonServerStr = $("#returnListData").val();
	
	var jsonObj = JSON.parse(jsonServerStr);
	
	$.each(jsonObj,function(k,v){
		
		var staffName = v.staffName;
		var staffId = v.staffId;
		
		$.each(v.timeEventList,function(tk,tv){
			
			var timeStr = tv.timeStr;
			
			//table 头
			$("#resultThead").find("th").each(function(thk,thv){
				
				if($(this).text() == timeStr){
					//如果 和 头部时间值 相同。
					
					//则在 对应位置的  td 添加数据
					
					//具体事件
					var eventVoList = tv.eventList;
					
					var eventStr = "";
					$.each(eventVoList,function(eventVoK,eventVoV){
						eventStr += eventVoV.eventName +" "+ eventVoV.dateDuration+"<br/>";
					})
					$("#resultTr").find("td").eq(thk).html(eventStr);
				}
			});
		})
		
		$("#resultTr").find("td").eq(0).text(staffName);
		
		$("#resultDisplay").append("<tr>"+$("#resultTr").html()+"</tr>");
	});
})



$('.form_datetime').datepicker({
	format : "yyyy-mm-dd",
	language : "zh-CN",
	autoclose : true,
	startView : 1
});



$("#startTimeStr").datepicker().on('changeDate', function(ev){
	
var startTimeStr =  $(this).val();
	
	var startTimeStamp =  moment(startTimeStr, "yyyy-MM-dd").unix();
	
	//从今天开始算 一个7天的 时间段，用来展示数据
	var aaa =  moment(startTimeStr).add(6,'d');
	
	var endTimeStr =  moment(aaa).format("YYYY-MM-DD");
	
	$("#endTimeStr").val(endTimeStr);
	
});






