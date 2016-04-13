$(document).ready(function(){
	
	/*1.页面初始化 */

	  /*1-1 培训学习即当前页面的 内容填充 */	
	  var testId = localStorage['test_id'];	
		
	  //当前测试的服务名称 
	  var serviceName = getUniversityServiceTypeName(testId);
	  
	  $("#testTitle").text(serviceName+"测试营");
	  
	  $("#serviceTypeId").val(testId);
	  
	  //培训资料内容 对象
	  var result =  getUniversityLearningDetail(testId);
	  
	  $("#learningContent").html(result);
	  
	  /*1-2 进入页面已经可以确定题目，预加载所有题目（id集合）*/
	  var loadQuestionListSuccess = function(datas, textStatus, jqXHR){
		  	
//		  var result =  JSON.parse(datas.response);
		  
		  var qIdArray = datas.data;
		  
		  //当前服务下 的 随机题库的 所有 题目id
		  localStorage.setItem("q_id_array",JSON.stringify(qIdArray));
		  
		  //初始化 题号,(qidArray)中的题目的 索引位置
		  localStorage.setItem("q_index",0);
	  };
	  
	  var postData = {};
	  
	  postData.service_type_id = testId;
	  
	  $.ajax({
			type : "get",
			url : "http://"+window.location.host+"/jhj-app/app/"+ "university/load_question_list.json",
			data : postData,
			statusCode : {
				200 : loadQuestionListSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
		});
})
	
  
  /*2.点击开始测试 */
  
  $("#startTest").on("click",function(){
	  
	  var testId = $("#serviceTypeId").val();
	  
	  window.location.href = "university-question.html?test_id="+testId;
  });
