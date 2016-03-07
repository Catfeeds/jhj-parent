myApp.onPageBeforeInit('mine-more', function (page) {
	var userId = localStorage.getItem("user_id");
	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}
	$$("#mine-faq").on("click",function(){
			mainView.router.loadPage("user/faq.html");
		});
	$$("#mine-agreement").on("click",function(){
		mainView.router.loadPage("user/agreement.html");
	});
	$$("#mine-aboutus").on("click",function(){
		mainView.router.loadPage("user/aboutus.html");
	});
});