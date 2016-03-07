myApp.template7Data['page:am-order-hour-view-0-page'] = function(){
    var result; 
    var postdata = {};
	
	var am_id = localStorage['am_id'];
	var order_no = localStorage['order_no_param'];
	
	postdata.order_no = order_no;
	postdata.am_id = am_id;
  
   $$.ajax({
      type : "POST",
      url  : siteAPIPath+"order/am_Order_Hour_Detail.json",
      dataType: "json",
      data : postdata,
      cache : true,
      async : false,	
      success: function(data){
    	  console.log(data);
          result = data.data;
          
          //设置时间显示格式
          var timestamp = moment.unix(result.service_date);
		  var startTime = timestamp.format('YYYY-MM-DD HH:mm');
		  result.service_date = startTime;
		  
		  //动态显示阿姨名称
		  
		  if(result.staff_name == ''){
//			$$("#staffName-span").text('暂未分配服务人员');
			result.staff_name = '暂未分配服务人员';
		  }
		  
		  
		 
		  
      }
   })
  
  return result;
}

myApp.onPageInit('am-order-hour-view-0-page', function (page) {
	
	
	  var orderStatus = $$("#orderStatus").val();
	  if(orderStatus == 3){
		  $$("#couponLi").hide();
		  $$("#realMoneyLi").hide();
	  }else{
		  $$("#couponLi").show();
		  $$("#realMoneyLi").show(); 
	  }
	
	
});
