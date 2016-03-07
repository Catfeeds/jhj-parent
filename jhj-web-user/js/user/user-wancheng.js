myApp.onPageBeforeInit('user-wancheng', function (page) {
	var userId = localStorage.getItem("user_id");;
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	getMineInfos(userId);
	$$("#user_mine_submit").on("click",function(){
		$$("#user_mine_submit").attr("disabled", true);
		var formData = myApp.formToJSON('#user_wancheng');
		$$.ajax({
			type : "POST",
			url : siteAPIPath+ "user/post_user_info.json?user_id="+userId,
			dataType : "json",
			cache : false,
			data : formData,
			statusCode : {
				200 : saveUserSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
		});
		});
});
function saveUserSuccess(data, textStatus, jqXHR) {
	 $$("#user_mine_submit").removeAttr('disabled');
	myApp.hideIndicator();
	var results = JSON.parse(data.response);
	if (results.status == "999") {
		myApp.alert(results.msg);
		return;
	}
	if (results.status == "0") {
		mainView.router.loadPage("user/mine.html");
	}
} 
var onMineInfoSuccess =function(data, textStatus, jqXHR) {
   	var result = JSON.parse(data.response);
	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
	var user = result.data;
	 $$("#user_mobile_span").text(user.mobile);
	 var formData = {
		'name' : user.name,
	}
	 myApp.formFromJSON('#user_wancheng', formData);
}
//获取用户信息接口
function getMineInfos(userId) {
	var postdata = {};
    postdata.user_id = userId;    
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "user/get_userinfo.json",
		dataType : "json",
		cache : true,
		data :postdata,
		statusCode: {
         	200: onMineInfoSuccess,
 	    	400: ajaxError,
 	    	500: ajaxError
 	    },
	});
}