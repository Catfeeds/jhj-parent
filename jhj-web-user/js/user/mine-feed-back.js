myApp.onPageInit("mine-feed-back-info", function (page) {
	var userId = localStorage.getItem("user_id");
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	$$(".feed-back-info-submit").on("click",function(){
		var content = $$("#service_content").val();
		if(content !='' && content != undefined){
			if(content.length >100){
				myApp.alert("只能输入100个字");
				return ;
			}
			saveUserFeedBack(userId);
		}else{
			myApp.alert("请输入您的意见详情！");    
			return;
		}
	});
});
var saveUserFeedBackSuccess =function(data, textStatus, jqXHR) {
   	var result = JSON.parse(data.response);
	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
/*    myApp.modal({
        text: '您的意见我们已经收到，感谢您的反馈!',
        buttons: [
          {
            text: '好的',
            bold: true,
          },
        ]
      })*/
	myApp.alert("您的意见我们已经收到，感谢您的反馈!");
	mainView.router.loadPage("user/mine.html");
}
//保存用户意见接口
function saveUserFeedBack(userId) {
	var postdata = {};
    postdata.user_id = userId; 
    postdata.content = $$("#service_content").val();
	$$.ajax({
		type : "POST",
		url : siteAPIPath + "user/post_feed_back.json",
		dataType : "json",
		cache : true,
		data :postdata,
		statusCode: {
         	200: saveUserFeedBackSuccess,
 	    	400: ajaxError,
 	    	500: ajaxError
 	    },
	});
}
