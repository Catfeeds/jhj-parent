myApp.onPageInit('user-xiangqing-page', function(page) {
	//var amId = page.query.am_id;
	var userId = page.query.user_id; 

    //调整订单跳转
	$$('#kehuzl-bianji').on('click', function() {
		mainView.router.loadPage("user/user-form-bianji.html?user_id="+userId);
	});

});

//用户详情
myApp.template7Data['page:user-xiangqing-page'] = function(){
  var result; 
  var userIdParam = localStorage['user_id_param'];
   $$.ajax({
          type : "GET",
          url:siteAPIPath+"user/get_user_list_detail.json?user_id="+userIdParam,
          dataType: "json",
          cache : true,
          async : false,
          success: function(data){
        	  console.log(data);
              result = data.data;
          }
  })
  
  return result;
}

