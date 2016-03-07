myApp.template7Data['page:am-remind-detail-page'] = function(){
    var result; 
    var postdata = {};
	var order_no = localStorage['am_remind_order_no_param'];
	postdata.order_no = order_no;

   $$.ajax({
      type : "GET",
      url:siteAPIPath + "remind/am_remind_detail.json",
      dataType: "json",
      data : postdata,
      cache : true,
      async : false,	// 不能是异步
      success: function(data){
//		  console.log(data);
	      result = data.data;
          
          //设置时间显示格式
          var timestamp = moment.unix(result.service_date);
		  var startTime = timestamp.format('YYYY-MM-DD HH:mm');
		  result.service_date = startTime;
		  
      }	
			  
   })
  
  return result;
}


