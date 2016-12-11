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
	$$(".charge-way").on("click",function(){
	var cardId = $$(this).next("#id").val();
	if(cardId!=null || cardId!=""){
		sessionStorage.setItem("card_id",cardId);
		mainView.router.loadPage("user/charge/mine-charge-way.html?card_id="+cardId);
	}
});
})
