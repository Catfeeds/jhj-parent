myApp.onPageInit('order-view-page2', function(page) {
	
	var amId = page.query.am_id;
	if (amId == undefined || amId == '' || amId == 0) {
		return;
	}
	var orderNo = page.query.order_no;
	var amId =  page.query.am_id;
	
	 //调整订单跳转
	$$('.ddxq-tiiaozheng').on('click', function() {
		mainView.router.loadPage("order/alter-order.html?am_id="+amId+"&order_no="+orderNo);
	});
	
});
//访问订单详情接口
myApp.template7Data['page:order-view-page2'] = function(){
  var result; 
  var amId = localStorage['am_id'];
  console.log(amId);
  var orderNo = localStorage['order_no_param'];
  
   $$.ajax({
          type : "GET",
          url:siteAPIPath+"order/get_am_order_detail.json?am_id="+amId+"&order_no="+orderNo,
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