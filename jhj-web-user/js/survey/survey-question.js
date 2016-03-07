myApp.onPageInit('survey-question-page', function(page) {
	
	
	//对于 完成后  又返回的 情况~
	if(localStorage['survey_user_id'] == undefined){
		
		myApp.alert("请您发起新的订制服务");
		mainView.router.loadPage("survey-user.html");
		return false;
	}
	
	
	var postData = {};
	
	var qId = page.query.q_id;
	
	//如果是第一次加载页面
	
	if(qId == undefined){
		qId = 0;
	}
	
	postData.q_id = qId;
	
	
	/**
	 *  请求成功后,	
	 *  采用virtual list 替换 题目及选项模板
	 */
	var loadQuestionSuccess = function(datas, textStatus, jqXHR){
		
		var result = JSON.parse(datas.response);
		
		/*
		 * 返回值
		 */
		//题目Vo
		var questionVo = result.data;
		
		//选项list
		var optionList = questionVo.option_list;
		
		/*
		 * 填充
		 */
		//题目模板
		var questionHtml = $$("#questionTemplate").html();
			
		questionHtml = questionHtml.replace(new RegExp('{questionId}',"gm"), questionVo.q_id);
		
//		单选
		if(questionVo.is_multi == 0){
			questionHtml = questionHtml.replace(new RegExp('{questionTitle}',"gm"), questionVo.title);
		}
//		多选
		if(questionVo.is_multi == 1){
			questionHtml = questionHtml.replace(new RegExp('{questionTitle}',"gm"), "(多选)"+questionVo.title);
		}
		
		
		questionHtml = questionHtml.replace(new RegExp('{isFirst}',"gm"), questionVo.is_first);
		/*
		 * 每次只显示一道题，  先清空上一题
		 */
		
		$$("#questionDisplay").html("");
		$$("#questionDisplay").append(questionHtml);
		
		//选项模板
		var optionHtml = "";
		if(questionVo.is_multi === 0){
			//如果是单选题,用单选模板
			optionHtml = $$("#optionTemplateRadio").html();
		}else{
			//如果是多选题用多选模板
			optionHtml = $$("#optionTemplateCheckbox").html();
		}
		
		//用来拼接所有选项的临时变量
		var optionHtmlTem = "";
		
		for (var i = 0; i < optionList.length  ; i++) {
			
			var part = optionHtml;
			
			part = part.replace(new RegExp('{optionNo}',"gm"), optionList[i].no);
			part = part.replace(new RegExp('{optionStr}',"gm"), optionList[i].title);
			
			optionHtmlTem += part;
		}
		
		//先清空上一题
		$$("#optionDisplay").html("");
		$$("#optionDisplay").append(optionHtmlTem);
		
		/**
		 * 处理 上一题、下一题按钮
		 * 
		 *   第一题: is_first = 0
		 * 	 处于中间的题: is_first = 1
		 *   最后一题: is_first = 2
		 */
		
//		if(questionVo.is_first === 0){
//			//如果是第一题,隐藏上一题按钮
//			$$("#previousDiv").hide();
//		}
		
		if(questionVo.is_first === 2){
			//如果是最后一题
			
//			$$("#previousDiv").hide();
			$$("#nextDiv").hide();
			$$("#endDiv").show();
		}
		
		
		
	};
	
	
	//第一次加载页面时,加载第一题
	$$.ajax({
		type: "get",
		 url: siteAPIPath + "survey/load_question.json",
		data: postData,
		statusCode: {
         	200: loadQuestionSuccess,
 	    	400: ajaxError,
 	    	500: ajaxError
 	    }
	});
	
	
	/**
	 * 点击下一题
	 * 	1.加载下一题
	 *  2.存储当前选择结果
	 */
	$$("#nextQuestion").on('click',function(){
		
		
		var postDataNext =  generateSurveyResultOption();
		
		if(postDataNext == false){
			return false;
		}
		
		var  aaa = localStorage['surveyResult'];
//		alert(aaa);
		
		$$.ajax({
			type: "get",
			 url: siteAPIPath + "survey/load_question.json",
			data: postDataNext,
			statusCode: {
	         	200: loadQuestionSuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});
	});
	
	
	/**
	 * 点击提交问卷
	 * 	
	 * 	 1.传递用户id 和选择结果	
	 *   2.
	 */
	
	// 合并数组
	$$("#endQuestion").on('click',function(){
		
		var selectResult =  generateSurveyResultOption();
		
		if(selectResult == false){
			return false;
		}
		
		
		var resultPageUrl = "survey/survey-result.html"; 
		
		mainView.router.loadPage(resultPageUrl);
		
	});
	
});

/*
 * 点击下一题或者 提交问卷按钮时，调用公共方法，封装 题目及其对应的勾选的选项
 */
function generateSurveyResultOption(){
	
		var postData = {};
		
		//选中的选项dom对象
		var obj =  $$("#optionDisplay input[name='optionNo']:checked");
		
		if(obj.length == 0){
			myApp.alert("请您任意选择一项,叮当到家将为您提供更为贴心的订制服务");
			return false;
		}
		
		//选中的选项的序号 （A,B,C...）  用 逗号拼接
		var selectOption = "";
		
		obj.each(function(k,v){
			selectOption += $$(this).parent().find("input[id='realOptionNo']").val()+",";
		});
		
		postData.option_str = selectOption.substring(0,selectOption.length-1);
		//当前题目id
		postData.q_id = $$("#questionId").val();
		
		
		var arrayEle = {"questionId":postData.q_id,"optionStr":postData.option_str};
		
		/**
		 * 存储当前的选择结果
		 */
		
		var resultArray = [];
		
		var isFirst = $("#isFirst").val();
		
		resultArray.push(arrayEle);
		
		// 如果是最后一题，提交之前也需要存储选择结果
		if(isFirst == 0){
			//如果是第一题
			localStorage.setItem("surveyResult",JSON.stringify(resultArray));
		}
		
		if(isFirst == 1 || isFirst == 2){
			
			//如果是位于中间位置的题目或最后一题
			/*
			 * html5的localStorage仅能存储字符串类型的数据，所以在存储数组时需要把数据转换为字符串，在使用时需要先parse将字符串转换为数组对象
			 */
			var a1 = JSON.parse(localStorage['surveyResult']);
			var a2 = resultArray.concat(JSON.parse(localStorage['surveyResult']));
			var a3 = JSON.stringify(resultArray.concat(JSON.parse(localStorage['surveyResult'])));
			
			localStorage.setItem("surveyResult",JSON.stringify(resultArray.concat(JSON.parse(localStorage['surveyResult']))));
		}
	
		
		return postData;
}

