$(document).ready(function(){	
	//题目序号
 	var qIndex = localStorage['q_index'];
 	
 	if(qIndex == 0){
 		//第一次进入页面
 		$("#previousQ").attr("class","jhj-small-btn am-active");
 		$("#previousQ").parent().attr("disabled",true);
 	}
 	
 	var qId =  getQIdByQIndex(qIndex);
	
	if(qId == -1){
		sweetAlert("没有这一题");
		return false;
	}
	
	/*1-2 加载题目、选项信息 */
	var loadQuestionSuccess = function(datas, textStatus, jqXHR){

		/*页面标题头,在ajax回调中处理,否则有时会无法显示*/	
	    var testId = localStorage['test_id'];	
		
	    //当前测试的服务名称 
	    var serviceName = getUniversityServiceTypeName(testId);
	  
	    var titleHtml = $("#titleTemplate").html();
		titleHtml = titleHtml.replace(new RegExp('{title}',"gm"), serviceName+"测试营");
	    
	    $("#titleDisplay").html("");
	    $("#titleDisplay").append(titleHtml);
	    
		
	    //ajax请求结果
//		var result = JSON.parse(datas.response);
		
		//题目Vo
		var questionVo = datas.data;
		
		/** 1.构造题干*/
		var questionTitle = questionVo.question;
		
		//题目模板
		var questionHtml = $("#questionTemplate").html();
		//隐藏域
		questionHtml = questionHtml.replace(new RegExp('{questionId}',"gm"), questionTitle.q_id);
//		questionHtml = questionHtml.replace(new RegExp('{isMulti}',"gm"), questionTitle.is_multi);
		
		
//		var isMulti = ""; 
//		
//		if(questionTitle.is_multi == 0){
//			isMulti = "(单 选)";
//		}else{
//			isMulti = "(多 选)";
//		}
		
		questionHtml = questionHtml.replace(new RegExp('{questionTitle}',"gm"), questionTitle.title);
		
		//当前题号
		var qIndex = Number(localStorage['q_index']) + 1;
		questionHtml = questionHtml.replace(new RegExp('{qIndex}',"gm"), qIndex);
		
		//题目总数
		var qSum = JSON.parse(localStorage['q_id_array']).length;	
		questionHtml = questionHtml.replace(new RegExp('{qSum}',"gm"), qSum);
		
		
		//先清空上一题
		$("#questionDisplay").html("");
		$("#questionDisplay").append(questionHtml);
		
		
		/** 2.构造选项 */
		var optionList = questionVo.option_list;
		
		var optionHtml = $("#optionTemplate").html();
		
		//用来拼接所有选项的临时变量
		var optionHtmlTem = "";
		
		for (var i = 0; i < optionList.length  ; i++) {
			
			var part = optionHtml;
			
			part = part.replace(new RegExp('{optionNo}',"gm"), optionList[i].no);
			part = part.replace(new RegExp('{optionStr}',"gm"), optionList[i].title);
			
			optionHtmlTem += part;
		}
		
		$("#questionDisplay").append(optionHtmlTem);
		
		//回显上一题的选择结果
		if(localStorage['university_result'] !=null && localStorage['university_result'].length >0){
			
			var historyArray = JSON.parse(localStorage['university_result']);
			
			//当前展示的题目Id
			var nowQId = $("#questionId").val();
			
			//点击上一题再 点击下一题时，可能会有重复，需要去重。保证每道题目只有一个结果
			for (var i = 0; i < historyArray.length; i++) {
				var nowStr = historyArray[i];
				
				if(nowStr.questionId == nowQId){
					
					$("#questionDisplay").find("div[id='option']").each(function(k,v){
						var spanObj =  $(this).find("span");
						
						if(nowStr.optionNo == spanObj.text()){
							spanObj.attr("class","jhj-cstspan jhj-cstspan1");
						}
					});
				}
			}
		}
		
		//下一题按钮
	 	if(Number(localStorage['q_index'])+1 == JSON.parse(localStorage['q_id_array']).length){
	 		//最后一题
	 		$("#nextQ").parent().attr("style","display:none");
	 		$("#submitQ").parent().show();
	 	}
	 	
	 	//处理上一题按钮
	 	if(localStorage['q_index'] == 0){
	 		//第一题,
	 		$("#previousQ").attr("class","jhj-small-btn am-active");
	 		$("#previousQ").parent().attr("disabled",true);
	 	}else{
	 		
	 		$("#previousQ").attr("class","jhj-small-btn1 am-active");	 	
	 		$("#previousQ").parent().removeAttr("disabled");
	 	}
		
	 	//处理提交按钮
	 	if(Number(localStorage['q_index'])+1 < JSON.parse(localStorage['q_id_array']).length){
	 		//最后一题
	 		$("#nextQ").parent().show();
	 		$("#submitQ").parent().attr("style","display:none");
	 	}
	 	
	}
	
	
	//第一次加载页面
	loadEveryQuestion(qId);

	/* 点击上一题 */
	$("#previousQ").on("click",function(){
		
		//如果当前是第一题，则禁用上一题按钮
		var qId = $("#questionId").val();
		
		var qIndex = getQIndexByQId(qId);
		
		if(qIndex <= 0){
			//如果是第一题 或者不存在
			return false;
		}
		
		
		
		//发送请求
		var previousIndex =  Number(localStorage['q_index']) - 1;
		var previousQId = getQIdByQIndex(previousIndex);
		loadEveryQuestion(previousQId);
		
		//更新索引
		localStorage.setItem("q_index",previousIndex);
	});
	
	/* 点击下一题*/
	$("#nextQ").on("click",function(){
		
		if(!completeSelectResult()){
			sweetAlert("请选择一项您认为的正确答案");
			return false;
		}
		
		//发送请求,  通过当前 题目id 在数组的索引,获取下一题
		var nextQId = getQIdByQIndex(Number(localStorage['q_index'])+1);
		loadEveryQuestion(nextQId);
		
		//更新 题目 索引
		localStorage.setItem("q_index",Number(localStorage['q_index'])+1);
		
	});
		
	//点击上一题和 下一题 发送的是 公共的ajax
	function loadEveryQuestion(qId){
		
		var postData = {};
		postData.q_id = qId;
		$.ajax({
			type : "get",
			url : "http://"+window.location.host+"/jhj-app/app/"+ "university/load_question.json",
			data : postData,
			statusCode : {
				200 : loadQuestionSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
		});
	}
});	
	
	//提交问卷后的回调
	var submitUniversityResult = function(datas, textStatus, jqXHR){
		
		var resultVo =  datas.data;
		
		if(resultVo.result_flag == 1){
			//考核通过,直接跳转成功页面
//			mainView.router.loadPage("university/university-result-success.html");
			
			window.location.href = "university-result-success.html";
			
			return false;
		}
		
		if(resultVo.result_flag == 0){
			//考核未通过，跳转到失败页，并构造相关参数
			window.location.href = "university-result-fail.html";
			
			localStorage.setItem("university_return_result",JSON.stringify(resultVo));
			
		}
		
		return false;
	}
	
	
	/*点击提交问卷*/
	$("#submitQ").on("click",function(){
		
		if(!completeSelectResult()){
			sweetAlert("请选择一项您认为的正确答案");
			return false;
		}
		
		var postData = {};
		
		var bbb =  localStorage['university_result'];
		
		postData.university_result =  localStorage['university_result'];
		postData.staff_id = localStorage['university_staff_id'];
		postData.service_type_id = localStorage['test_id'];
		
		$.ajax({
			type : "post",
			url : "http://"+window.location.host+"/jhj-app/app/"+ "university/submit_university_result.json",
			data : postData,
			statusCode : {
				200 : submitUniversityResult,
				400 : ajaxError,
				500 : ajaxError
			}
		});
		
	});
	
	
	
	//对于 动态生成的元素,需要用 dom重新绑定事件 ，实现单选效果
	$(document).on('click','#option',function(){
		
		var  aaa = $("#questionDisplay").find("div[id='option']").length;
		
		//都是单选题，先全部变灰，再单个处理，实现单选
		$("#questionDisplay").find("div[id='option']").each(function(k,v){
			$(this).find("span").attr("class","jhj-cstspan");
		});
		
		var spanObj = $(this).find("span");
		spanObj.attr("class","jhj-cstspan jhj-cstspan1");
		
	});
	



/*
 * 点击下一题 或 提交时
 *   1.判断是否未选择任何选项
 *   2.存储选择结果
*/
function completeSelectResult(){
	
	//构造json, 同时判断是否为选择答案
	var qRefOptionArray = [];
	
	var questionId = $("#questionId").val();
	
	//1.判断是否有选中选项
	var flag = 0;
	
	$("#questionDisplay").find("div[id='option']").each(function(k,v){
		var spanObj =  $(this).find("span");
		
		if(spanObj.hasClass("jhj-cstspan1")){
			var optionNo =  spanObj.text();
			var strA = {"questionId":questionId,"optionNo":optionNo};
			qRefOptionArray.push(strA);
			
			flag = 1;
		}
	});
	
	if(flag == 0){
		
		return false;
	}
	
	//2. 封装选择结果
	if(localStorage['university_result'] !=null && localStorage['university_result'].length >0){
		var nowArray = JSON.parse(localStorage['university_result']);
		
		//点击上一题再 点击下一题时，可能会有重复，需要去重。保证每道题目只有一个结果
		for (var i = 0; i < nowArray.length; i++) {
			var nowStr = nowArray[i];
			
			for (var j = 0; j < qRefOptionArray.length; j++) {
				var boxStr = qRefOptionArray[j];
				
				if(nowStr.questionId == boxStr.questionId){
					nowArray.baoremove(i);
				}
			}
		}
		
		localStorage.setItem("university_result",JSON.stringify(qRefOptionArray.concat(nowArray)));
	}else{
		localStorage.setItem("university_result",JSON.stringify(qRefOptionArray));
	}
	
	return true;
}

//删除数组元素，传递下标 
Array.prototype.baoremove = function(dx) 
{ 
  if(isNaN(dx)||dx>this.length){return false;} 
  this.splice(dx,1); 
} 


//根据题目索引,在 题目id数组中得到 题目id
function getQIdByQIndex(qIndex){
	
	var qIdArray = JSON.parse(localStorage['q_id_array']);
	
	if(qIndex < qIdArray.length){
		return qIdArray[qIndex];
	}else{
		return -1;
	}
}	

//根据 题目id,得到 该题目 在 题目id数组中的索引,来判断当前题号
function getQIndexByQId(qId){
	
	var qIdArray = JSON.parse(localStorage['q_id_array']);
	
	return qIdArray.indexOf(Number(qId));
}