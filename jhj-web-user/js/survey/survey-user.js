myApp.onPageInit('survey-user-page', function(page) {
	
	/*
	 * 提交订单
	 */
	
	$$("#submitSurveyUser").click(function(){
		
		if(surveyUserValidation() == false){
			return false;
		}
		
		
		// 文档要求： post请求，必须是 formData类型
		var formData = myApp.formToJSON('#survey-user-form');
		
		$$.ajax({
			type: "post",
			 url: siteAPIPath + "survey/survey_user_submit.json",
			data: formData,
			success: function (data, status, xhr){
			
				var result = JSON.parse(data);
				
				if(result.status == "999"){
					myApp.alert(resut.msg);
					return false;
				}
				
				/**
				 * 存储当前调查填写的用户信息id
				 */
				localStorage.setItem("survey_user_id",result.data);
				
				
				var successUrl = "survey/survey-question.html";
				mainView.router.loadPage(successUrl);
			
		 },
		  error: function(status,xhr){
			  	myApp.alert("网络异常,请稍后再试.");
		 }
		});
	});
	
});

function surveyUserValidation(){
	
	//去除空白字符串
	var userName = $$("#userName").val().replace(/\s/g, "");
	
	if(userName.length < 1 || userName == undefined){
		myApp.alert("请输入您的姓名");
		return false;
	}
	
	var age = $$("#age").val();
	
	if(isNaN(age) || age.indexOf(0) === 0 || age.replace(/\s/g, "") == ""){
		myApp.alert("请输入合法的年龄");
		return false;
	}
	
	var mobile = $$("#mobile").val();
	
	if(!isPhone(mobile)){
		myApp.alert("请输入合法的手机号");
		return false;
	}
}

function isPhone(aPhone) {
	var bValidate = RegExp(
			/^(0|86|17951)?(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|19[0-9])[0-9]{8}$/)
			.test(aPhone);
	if (bValidate) {
		return true;
	} else
		return false;
}

