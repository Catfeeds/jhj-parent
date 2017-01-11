function openAuthView(staffId) {
	$("#idAuthViewModal").modal({
		remote: appRootUrl + "newbs/auth-idcard-view?staffId="+staffId,
	});
}

$("#idAuthViewModal").on("hidden.bs.modal", function() {
    $(this).removeData("bs.modal");
    location.reload();
});

function doAuthIdCard(staffId) {
	if (confirm("认证需要访问黑格科技，需花费1.1元，是否确定认证?")){
		$.ajax({
	    	type:"POST",   // http请求方式
	    	url: appRootUrl +"/newbs/do-auth-id-card.json", // 发送给服务器的url
	    	data:"staffId="+staffId, // 发送给服务器的参数
	    	async: false,
	    	dataType:"json",
	    	success : function(data) {
	    		var status = data.status;
	    		if (status != "0") {
	    			alert(data.msg);
	    			return false;
	    		}
	    		
	    		authIdCardAjaxReview(data.data);
	    	}
	    });
	}
}

function authIdCardAjaxReview(data) {
	console.log("authIdCardAjaxReview");
	var isAuthIdCard = data.isAuthIdCard;
	
	if (isAuthIdCard == 1) {
		$("#authStatus").html("<font color=\"green\">已认证</font>");
	} else {
		$("#authStatus").html("<font color=\"red\">认证失败</font>");
	}
	
	var authData = data.authData;
	console.log(authData);
	$("#authTime").html("认证时间:"+ authData.authTime);
	$("#msg").html("认证情况:"+ authData.msg);
	$("#mobileProv").html("手机号归属地:"+ authData.mobileProv);
	$("#sex").html("性别:"+ authData.sex);
	$("#birthday").html("生日:"+ authData.birthday);
	$("#address").html("地址:"+ authData.address);
}
