myApp.onPageBeforeInit('user-am-detail-page', function(page) {
		
});

//列表显示
myApp.template7Data['page:user-am-list-page'] = function() {
	var result;
	var userId = localStorage['user_id'];

	$$.ajax({
		type : "GET",
		url : siteAPIPath + "user/user_get_org.json?user_id="+ userId,
		dataType : "json",
		cache : true,
		async : false,
		success : function(data) {
			result = data;
		}
	})

	return result;
}

function viewStaffDetail(staffId) {
	localStorage.setItem('staff_id', staffId);
	url = "user/user-am-detail.html?staff_id=" + staffId;
	mainView.router.loadPage(url);
}
