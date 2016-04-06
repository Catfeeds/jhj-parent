//列表显示用户充值卡
myApp.template7Data['page:huodong-list-page'] = function(){
  var result;
  var userId = localStorage.getItem("user_id");
  if(userId ==null || userId== undefined){
 	 	mainView.router.loadPage("login.html");
 	 	return;
	}
  
  $$.ajax({
          type : "GET",
          url  : siteAPIPath+"dict/get_socilas.json",
          dataType: "json",
          cache : true,
          async : false,
          success: function(data){
              result = data;
          }
  })
  
  return result;
}
function jumpDetail(id){
	localStorage.setItem("socials_id",id);
	mainView.router.loadPage("huodong-detail.html");
}
