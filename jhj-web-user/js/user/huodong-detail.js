//获取活动的详情
myApp.template7Data['page:huodong-detail'] = function(){
  var result;
  var userId = localStorage.getItem("user_id");
  if(userId ==null || userId== undefined){
 	 	mainView.router.loadPage("login.html");
 	 	return;
	}
  	var socialsId =localStorage.getItem("socials_id");
  	if(socialsId ==null || socialsId== undefined){
	 	socialsId =0;
	}
	var formData={};
	formData.id=socialsId;
	formData.user_id=userId;
$$.ajax({
          type : "GET",
          url  : siteAPIPath+"dict/get_socials_detail.json",
          data : formData,
          dataType: "json",
          cache : true,
          async : false,
          success: function(data){
              result = data.data;
          }
  })
  return result;
}
myApp.onPageInit('huodong-detail', function (page) {
	var userId = localStorage.getItem("user_id");;
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	var socialsId =localStorage.getItem("socials_id");
  	if(socialsId ==null || socialsId== undefined){
	 	socialsId =0;
	}
  	var saveSubmitSuccess = function(data, textStatus, jqXHR){
  		myApp.hideIndicator();
  	   	var result = JSON.parse(data.response);
  		}
  	var formData ={};
  	formData.user_id=userId;
  	formData.socials_id = socialsId;
     $$(".save_submit").on("click",function(){
    	 $$.ajax({
             type : "POST",
             url  : siteAPIPath+"dict/post_socials_call.json",
             data : formData,
             dataType: "json",
             cache : true,
             async : false,
             statusCode: {
              	200: saveSubmitSuccess,
      	    	400: ajaxError,
      	    	500: ajaxError
      	    },
    	 })
     });
});