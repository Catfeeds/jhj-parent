myApp.onPageInit('quanniandingzhi-page', function(page) {
	
	var serviceTypeId =  page.query.serviceType;
	
	if(serviceTypeId != undefined){
		$$("#serviceTypeId").val(serviceTypeId);
	}
	
	$$.ajax({
		type: "get",
		 url: siteAPIPath + "newPartServiceType/service_type_detail.json",
		data: {
			"service_type_id":serviceTypeId
		},
		success: function (data, status, xhr){
			
			var resultdata = JSON.parse(data);
			
			var result = resultdata.data;
			
			$$("#serviceName").text(result.name);
			
			var singlePrice = result.price;
			
			var time = result.service_times;
			//原价
			var yearPriceOri = singlePrice*time*52;
			
			$$("#price").text("原价:"+yearPriceOri+"元");
			
			var monthPrice = "月付:"+(Number(yearPriceOri)*0.95).toFixed(2)+"元(享95折)";
			
			//月付
			$$("#monthPrice").text(monthPrice);
			var yearPrice = "年付:"+(Number(yearPriceOri)*0.85).toFixed(2)+"元(享85折)";
			//年付
			$$("#yearPrice").text(yearPrice);
			
			$$("#yearTimes").text(time*52+"次");
			
			if(time < 1){
				//取整，舍弃小数
				var weekNum = parseInt(1/time);
				$$("#weekTimes").text(weekNum+"周1次");
			}else{
				$$("#weekTimes").text("每周"+time+"次");
			}
			
			$("#serviceContent").text(result.service_content);
		},
		error: function(status,xhr){
		  	myApp.alert("网络异常,请稍后再试.");
		}
	});

	
	$$("#submitAtOnce").on("click",function(){
		
		var  serviceTypeId = $$("#serviceTypeId").val();
		
		var userId = localStorage['user_id'];
		
		if(userId == undefined){
			
			myApp.alert("您还没有登录");
			mainView.loadPage.router("login.html");
			return false;
		}
		
		$$.ajax({
			type: "get",
			 url: siteAPIPath + "order/order_customiza_year.json",
			data: {
				"service_type_id":serviceTypeId,
				"user_id":userId
			},
			success:function(data,sta,xhr){
				
				var result = JSON.parse(data);
				
				if(result.status == "999"){
					
					myApp.alert(result.msg);
					return false;
				}
				
				myApp.alert(result.msg);
				
				mainView.router.loadPage("index.html");
			}
		});
	});	
});


