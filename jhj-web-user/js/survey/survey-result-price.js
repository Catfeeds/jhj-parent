myApp.onPageInit('survey-result-price-page', function(page) {
	
	var userId = localStorage['survey_user_id'];
	
	//对于 完成后  又返回的 情况~
	if(userId == undefined){
		
		myApp.alert("请您发起新的订制服务");
		mainView.router.loadPage("index.html");
		return false;
	}
	
	
	var postData = {};
	
	postData.user_id = userId;
	
	$$.ajax({
		type: "get",
		 url: siteAPIPath + "survey/get_result_price.json",
		data: postData,
		success:function (datas,sta,xhr){
			
			var result = JSON.parse(datas);
			
			//封装参数
			var yearResult = result.data[0];
			
			//预计年消费,通用
			$$("#sumPrice").text(yearResult.sum_price);
			
			//设置默认显示,年支付
			
			//优惠后价格
			$$("#discountPrice").text(yearResult.discount_price);
			//优惠后月支付
			$$("#monthPrice").text(yearResult.price_by_month);
			
			//单选对应的值
			$$("#yearRadio").val(yearResult.survey_pay_type);
			
			//隐藏域参数
			$$("#yearDiscountPrice").val(yearResult.discount_price);
			$$("#yearPriceByMonth").val(yearResult.price_by_month);
			
			var quarterResult = result.data[1];
			
			$$("#quarterDiscountPrice").val(quarterResult.discount_price);
			$$("#quarterPriceByMonth").val(quarterResult.price_by_month);
			
			$$("#quarterRadio").val(quarterResult.survey_pay_type);
			
			var monthResult = result.data[2];
			$$("#monthDiscountPrice").val(monthResult.discount_price);
			$$("#monthPriceByMonth").val(monthResult.price_by_month);
			
			$$("#monthRadio").val(monthResult.survey_pay_type);
			
		},
		error:function(){
			myApp.alert("网络异常");
		}
	});

	// 支付方式, 变化时, 动态改变 价格
	$$("select[name='payTypeSelect']").on("change",function(){
		
		var nowSelect = $$(this).val();
		
		if(nowSelect == 0){
			$$("#discountPrice").text($$("#yearDiscountPrice").val());
			$$("#monthPrice").text($$("#yearPriceByMonth").val());
		}
		
		//半年
		if(nowSelect == 1){
			
			$$("#discountPrice").text($$("#quarterDiscountPrice").val());
			$$("#monthPrice").text($$("#quarterPriceByMonth").val());
			
		}
		
		//月
		if(nowSelect ==2){
			
			$$("#discountPrice").text($$("#monthDiscountPrice").val());
			$$("#monthPrice").text($$("#monthPriceByMonth").val());
		}
		
	});
	
	
	
	
	//完成订制, 清空 调查相关的 localStorage
	$$("#completeSurvey").on("click",function(){
		
		localStorage.removeItem("boxChildContent");
		localStorage.removeItem("surveyResult");
		localStorage.removeItem("survey_user_id");
		localStorage.removeItem("changeContentId");
		
		mainView.router.loadPage("survey/survey-success.html");
		
	});
});



