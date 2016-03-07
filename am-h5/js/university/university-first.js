$(document).ready(function(){
	//预加载 考核项目 require-data.js
	var  universityServiceList = JSON.parse(localStorage.getItem("university_service_type_list"));
	
	for (var i = 0; i < universityServiceList.length; i++) {
		var item = universityServiceList[i];
		
		if(item.name == "钟点工"){
			$("#hourTestId").val(item.service_type_id);
		}
		
		if(item.name == "助理"){
			$("#amTestId").val(item.service_type_id);
		}
		
		if(item.name == "快送"){
			$("#fastDeliverTestId").val(item.service_type_id);
		}
	}
	
	//页面加载,获得 当前登录人员的 考核状态
	var getStaffTestStatusSuccess = function(data, textStatus, jqXHR){
		
		var result =  data.data;
		
		//钟点工测试
		var hourTestId =$("#hourTestId").val();
		//助理测试
		var amTestId = $("#amTestId").val();
		//快送测试
		var fastDeliverTestId = $("#fastDeliverTestId").val();
		
		for (var i = 0, j= result.length; i < j ; i++) {
			var testVo = result[i];
			
			$("input[name='testServiceId']").each(function(k,v){
				
				var aaa = $(this).val(); 
				
				if($(this).val() == testVo.service_type_id){
					
					//考核状态
					if(testVo.test_status == 0){
						$(this).parent().parent().find(".jhj-dddxspan2").text("尚未通过考核");
						
					}else{
						$(this).parent().parent().find(".jhj-dddxspan2").text("考核通过");
					}
					//该服务最近的考试时间
					
					$(this).next().val(testVo.last_test_time);
					
				}
			})
		}
	}
	
	var staffId =  getParameterByName("staff_id");
	
	
//	if(localStorage['university_staff_id'] == undefined || localStorage['university_staff_id'] == ""){
//		localStorage.setItem("university_staff_id",staffId);
//	}
	
		
	var postData = {};
	
	if(localStorage['university_staff_id'] != undefined){
		//如果是考试后的跳转新页面
		postData.staff_id = localStorage['university_staff_id'];
	}
	
	if(staffId != undefined && staffId !=""){
		postData.staff_id = staffId;
		//改变为当前用户
		localStorage.setItem("university_staff_id",staffId);
	}
	
	
	$.ajax({
		type : "get",
		url : "http://"+window.location.host+"/jhj-app/app/"+ "university/get_staff_test_status.json",
		data : postData,
		statusCode : {
			200 : getStaffTestStatusSuccess,
			400 : ajaxError,
			500 : ajaxError
		}
	});
	
});	
	
//点击跳转到对应的 测试 页面（加载学习资料）

$(".am-panel").on("click",function(){
	
	if($(this).find(".jhj-dddxspan2").text() == "考核通过"){
		
		sweetAlert("您已通过考核");
		return false;
	}
	
	var latestTime = $(this).find("input[name='latestTime']").val();
	//考试时间间隔1天
	if(latestTime !=0){
		//当前时间
		var nowTimeStamp = Math.round(new Date().getTime()/1000);
		
		//上次考试时间到现在的 时间  秒值
		var betweenTime = nowTimeStamp - Number(latestTime);
		
		//间隔值小于一天时,给出提示
		if(betweenTime < 24*3600){
			
			//毫秒值转换为时分秒
			var sss =  MillisecondToDate((24*3600 - betweenTime)*1000);
			
			sweetAlert("距离下次考试时间还有:"+sss);
			return false;
		}
	}
	
	// 服务id
	var testId =  $(this).find(".jhj-dddxspan1").find("input").val();
	
	//main.js里获得 参数的方法不好用
	
	localStorage.setItem("test_id",testId);
	
	window.location.href = "university-learning.html";
	
});

/*毫秒值转换为 时分秒*/
function MillisecondToDate(msd) {
    var time = parseFloat(msd) / 1000;
    if (null != time && "" != time) {
        if (time > 60 && time < 60 * 60) {
            time = parseInt(time / 60.0) + "分钟" + parseInt((parseFloat(time / 60.0) -
                parseInt(time / 60.0)) * 60) + "秒";
        }
        else if (time >= 60 * 60 && time < 60 * 60 * 24) {
            time = parseInt(time / 3600.0) + "小时" + parseInt((parseFloat(time / 3600.0) -
                parseInt(time / 3600.0)) * 60) + "分钟" +
                parseInt((parseFloat((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60) -
                parseInt((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60)) * 60) + "秒";
        }
        else {
            time = parseInt(time) + "秒";
        }
    }
    return time;
}

