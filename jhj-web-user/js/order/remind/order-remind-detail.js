myApp.onPageInit('order-remind-detail-page', function (page) {

	var orderStatus = $$("#nowStatus").val();
	
	if(orderStatus == 10){
		$$("#cancleRemind").show();
	}else{
		$$("#cancleRemind").hide();
	}
	
	
	$$("#cancleRemind").click(function(){
		
		var postdata = {};
		var order_no = $$("#nowOrderNo").val();
		postdata.order_no = order_no;
		
		//根据服务时间，设置取消提示
		var text = "";
		
		// 服务时间  unix时间戳
		var aaa =   $$("#serviceDate").text();
		var bb =  (new Date(aaa).getTime()/1000).toString();
		
		//当前时间 unix时间戳
		var now = Math.round(new Date().getTime()/1000);
		
		//时间差
		var diffValue = Number(bb) - Number(now);
		
		//20分钟内，不能取消
		if(diffValue <= 1200 && diffValue > 0){
			myApp.alert("提醒服务即将开始,现在不能取消哦");
			$$(this).attr("disabled",true);
			return false;
		}else if(diffValue < 0){
			myApp.alert("服务已经生效,您不能取消哦");
			$$(this).attr("disabled",true);
			return false;
		}else{
			text = "提醒服务完全免费,您真的要取消么?";
		}
		
		myApp.confirm(text,function(){
			$$.ajax({
			      type : "GET",
			      url:siteAPIPath + "remind/cancle_remind.json",
			      dataType: "json",
			      data : postdata,
			      cache : true, 	
			      success: function(datas,status,xhr){
			    	  
//			    	 var result = JSON.parse(datas);
					 if(datas.status == 0){
						 myApp.showPreloader('提醒已取消')
					     setTimeout(function () {
					        myApp.hidePreloader();
					     }, 500);
						 
						 mainView.router.loadPage("order/remind/order-remind-list.html");
					 }
					 if(datas.status == 999){
						 myApp.alert(datas.msg);
						 $$("#cancleRemind").attr("disabled",true);
						 return false;
					 }
			      }	
						  
			   })
		});
	});
	
});	
myApp.template7Data['page:order-remind-detail-page'] = function(){
    var result; 
    var postdata = {};
	var order_no = localStorage['remind_order_no_param'];
	postdata.order_no = order_no;

   $$.ajax({
      type : "GET",
      url:siteAPIPath + "remind/remind_detail.json",
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


