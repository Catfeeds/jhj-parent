function getUserInfo() {
	
	var userId = localStorage['user_id'];
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
			
			 var userAddr = result.default_user_addr;

	    	  if (userAddr != undefined && userAddr != null) {
	    		  if (userAddr.is_default == 1) {
	    			  
	    			  localStorage.setItem('default_addr_id', userAddr.id);
	    			  localStorage.setItem('default_addr_name', userAddr.name + " " + userAddr.addr);	
	    			  
	    			  console.log("default_addr_id ==" + userAddr.id);
	    		  }
	    	  }
		}
	})
}

getUserInfo();

function orderLink(url, serviceTypeId) {
	
	localStorage.setItem("firstServiceType",serviceTypeId);
	url = url + "?firstServiceType="+serviceTypeId;
	mainView.router.loadPage(url);
}

