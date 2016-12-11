function doReset(){
		$("#mobile1").attr("value","");
		$("#orderNo1").attr("value","");
}

$('.form_datetime').datepicker({
 	format: "yyyy-mm-dd",
 	language: "zh-CN",
 	autoclose: true,
 	startView: 1,
 	todayBtn:true
 });

 function checkEndTime(){  
     var startTime=$("#startTimeStr").val();  
     var start=new Date(startTime.replace("-", "/").replace("-", "/"));  
     var endTime=$("#endTimeStr").val();  
     var end=new Date(endTime.replace("-", "/").replace("-", "/"));  
     if(end<start){ 
     	
     	alert('结束日期必须大于开始时间');
     	 return false;  
     }  
     return true;  
 }
 
 $("#submit").on('click',function(){
	 var url = $(this).val();
	 $("form").attr("action",url);
	 $("form").submit();
 });