myApp.onPageInit('quanniandingzhi-page', function(page) {
	
	var serviceTypeId =  page.query.serviceType;
	
	if(serviceTypeId != undefined){
		$$("#serviceTypeId").val(serviceTypeId);
	}
		
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

myApp.template7Data['page:quanniandingzhi-page'] = function() {
	var result;
	var serviceTypeId = localStorage['service_type_id'];
	
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "newPartServiceType/service_type_detail.json?service_type_id="
				+ serviceTypeId,
		dataType : "json",
		cache : true,
		async : false,
		success : function(data) {
			// console.log(data);
			result = data.data;

		}
	})

	return result;
}


