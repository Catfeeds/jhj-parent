function getUserInfo(userId) {
	console.log("getUserInfo");
	if (userId == undefined || userId == "" || userId == 0) return false;
	$$.ajax({
		type : "GET",
		url : siteAPIPath + "user/get_userinfo.json?user_id="+userId,
		dataType : "json",
		cache : false,
		async : false,
		success : function(data) {
			result = data.data;
			localStorage.setItem("is_vip",result.is_vip);
		}
	})
}

function orderLink(url, serviceTypeId) {
	
	localStorage.setItem("firstServiceType",serviceTypeId);
	url = url + "?firstServiceType="+serviceTypeId;
	mainView.router.loadPage(url);
}

