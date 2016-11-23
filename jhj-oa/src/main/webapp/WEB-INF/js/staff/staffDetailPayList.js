
$('.form_datetime').datepicker({
	format: "yyyy-mm-dd",
	language: "zh-CN",
	autoclose: true,
	startView: 0,
	todayBtn:true
});

function checkEndTime(){  
    var startTime=$("#startTimeStr").val();  
    var start=new Date(startTime.replace("-", "/").replace("-", "/"));  
    var endTime=$("#endTimeStr").val();  
    var end=new Date(endTime.replace("-", "/").replace("-", "/"));  
    if(end<start){ 
    	
    	alert('结束日期必须大于开始时间');
    	 BootstrapDialog.alert({
    			 title:'提示语',
    			 message:'结束日期必须大于开始时间!'
    	 });
    	 return false;  
    }  
    return true;  
} 