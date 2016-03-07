
$(document).ready(function(){		
	
	  var testId = localStorage['test_id'];	
	  
	  
	  if(testId == undefined || localStorage["university_return_result"] == undefined){
		  sweetAlert("请勿重复提交");
		  window.location.href= "university/university-first.html?staff_id="+localStorage['university_staff_id'];
		  return false;
	  }
	  
	  //当前测试的服务名称 
	  var serviceName = getUniversityServiceTypeName(testId);
	  
	  $("#testTitle").text(serviceName+"测试营");
	  
	  
	  //考试结果
	  var testResult =  JSON.parse(localStorage['university_return_result']);
	  
	  //题目总数
	  var qSum =  JSON.parse(localStorage['q_id_array']).length;
	  
	  $("#testNeed").text("您的成绩未达到要求(共计"+qSum+"题,至少答对"+testResult.need_num+"题方可通过),需要重新学习,考试.")
	  
	  //错误题目
	  
	  var rightNum = testResult.right_num;
	  
	  var errorNum = Number(qSum)-Number(rightNum);
	  
	  $("#testRight").text(errorNum+"错误/共计"+qSum+"题");
	  
	  
	  
	  //点击重新学习,清空相关 数据
	  $("#reviewTest").on("click",function(k,v){
		  
		  //题目
		  localStorage.removeItem("q_id_array");
		  localStorage.removeItem("q_index");
		  
		  //被测试的服务id
		  localStorage.removeItem("test_id");
		  
		  //考核结果
		  localStorage.removeItem("university_return_result");	
		  
		  //答题过程
		  localStorage.removeItem("university_result");
		  
		  window.location.href = "university-first.html?staff_id="+localStorage['university_staff_id'];
		  
		  localStorage.removeItem("university_staff_id");
	  });
	  
});
