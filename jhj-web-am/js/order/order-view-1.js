myApp.onPageInit('order-view-page1', function(page) {
	
	var amId = page.query.am_id;
	if (amId == undefined || amId == '' || amId == 0) {
		return;
	}
	var orderNo = page.query.order_no;
	var amId =  page.query.am_id;
		
    //调整订单跳转
	$$('.ddxq-tiiaozheng').on('click', function() {
		mainView.router.loadPage("order/alter-order-view-1.html?am_id="+amId+"&order_no="+orderNo);
	});
	
	
});

//列表显示深度保洁项
myApp.template7Data['page:order-view-page1'] = function(){
  var result; 
  var amId = localStorage['am_id'];
//  console.log(amId);
  var orderNo = localStorage['order_no_param'];
  
   $$.ajax({
          type : "GET",
          url:siteAPIPath+"order/get_am_clean_detail.json?am_id="+amId+"&order_no="+orderNo,
          dataType: "json",
          cache : true,
          async : false,
          success: function(data){
//        	  console.log(data);
              result = data.data;
              
          }
  })
  
  return result;
}



