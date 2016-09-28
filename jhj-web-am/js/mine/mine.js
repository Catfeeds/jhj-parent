myApp.onPageBeforeInit('am-mine-page', function (page) {
	
	var amId = localStorage['am_id'];
	
	$$('#sec-logout').on('click', function() {
		  localStorage.removeItem("am_id");
		  localStorage.removeItem('am_mobile');
//		  localStorage.removeItem('service_type_addons_list');
		  localStorage.removeItem('service_type_list');
		  mainView.router.loadPage("index.html");
	});
	
	$$("#getStaffDetial").on('click',function(){
		mainView.router.loadPage("mine/staff-list.html?amId="+amId);
	});
});

myApp.template7Data['page:am-mine-page'] = function(){
    var result; 
    var postdata = {};
	var amId = localStorage['am_id'];
	
	postdata.amId = amId;

  
   $$.ajax({
	   type : "GET",
       url  : siteAPIPath+"user/get_am_by_amId.json",
       dataType: "json",
      cache : true,
       data : postdata,
      async : false,	
      success: function(datas){
    	  
    	  console.log(datas.data);
//          result = datas.data;
           result = datas.data;
           
           //根据生日，计算年龄
            var reg =/[\u4E00-\u9FA5]/g;	//去除中文
			var bbb =   moment(result.birth);
			var aaa =  moment(bbb).fromNow();
           
			result.real_age = aaa.replace(reg,'');
			
			//星座名称
			result.astro_name =  getAstroName(result.astro);
			  
      },
   	  error:ajaxErrorClean
   })
  
  return result;
}

var ajaxErrorClean = function(data, textStatus, jqXHR) {	
	// We have received response and can hide activity indicator
    myApp.hideIndicator();		
	myApp.alert('网络繁忙,请稍后再试.');
	
	localStorage.removeItem("am_id");
	localStorage.removeItem('am_mobile');
	localStorage.removeItem('service_type_addons_list');
	localStorage.removeItem('service_type_list');
	mainView.router.loadPage("login.html");
};

