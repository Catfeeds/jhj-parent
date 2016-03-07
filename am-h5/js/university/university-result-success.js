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
	
	  $("#testSuccess").on("click",function(k,v){
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
