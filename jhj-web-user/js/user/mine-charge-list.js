//列表显示用户充值卡
myApp.template7Data['page:mine-charge-list'] = function(){
  var result;
  var userId = localStorage.getItem("user_id");
  if(userId==null || userId == undefined){
	  userId = 0;
  }
  $$.ajax({
          type : "GET",
          url  : siteAPIPath+"/dict/get_cards.json?user_id="+userId,
          dataType: "json",
          cache : true,
          async : false,
          success: function(data){
              result = data;
          }
  })
  return result;
}
myApp.onPageInit('mine-charge-list', function (page) {
	var userId = localStorage.getItem("user_id");;
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
     $$(".charge-way").on("click",function(){
    	 var cardId = $$(this).prev().prev().prev().val();
    	 var cardPay = $$(this).next().val();
    	 var sendMoney = $$(this).next().next().val();
    	 if(cardPay <= 5000){
        	 mainView.router.loadPage("user/charge/mine-charge-way.html?card_id="+cardId+"&card_pay="+cardPay
        			 +"&send_money="+sendMoney);
    	 }
 	});
//     $$(".feed_back_money").on("click",function(){
//    	 mainView.router.loadPage("user/charge/mine-charge-fanquan.html");
//     });
});

//function chargeLargeValue(obj) {
//	var amId = localStorage.getItem('am_id');
//	var amMobile = localStorage.getItem('am_mobile');
//	
//	if (localStorage.getItem('am_id') == null || localStorage.getItem('am_mobile') == null) {
//		
//		myApp.confirm('您还没有添加地址，点击确定前往添加地址立刻获得家庭助理，点击返回留在此页', "", function () {
//			mainView.router.loadPage("user/mine-add-addr.html?addr_id=0&return_url=user/charge/mine-charge-list.html");
//	    });
//	} else {
//	
//		myApp.confirm('土豪,大额充值可联系您的助理为您上门办理。确定拨打助理电话:'+amMobile, "", function () {
//			$$("#cal_am_mobile_real").attr("href", "tel:"+amMobile);
//			$$("#cal_am_mobile_real").click();
//	    });
//	}
}