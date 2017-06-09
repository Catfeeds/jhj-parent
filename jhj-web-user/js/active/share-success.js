myApp.onPageInit('share-success', function (page) {

    var shareUserId = page.query.share_user_id;
	sessionStorage.setItem("share_user_id", shareUserId);
    
});

