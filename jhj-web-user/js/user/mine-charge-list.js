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
    	 localStorage.setItem("pay_card_id",cardId);
    	 var cardPay = $$(this).next().val();
    	 var sendMoney = $$(this).next().next().val();
    	 if(cardPay <= 5000){
        	 mainView.router.loadPage("user/charge/mine-charge-way.html?card_id="+cardId);
    	 }
 	});
//     $$(".feed_back_money").on("click",function(){
//    	 mainView.router.loadPage("user/charge/mine-charge-fanquan.html");
//     });
})