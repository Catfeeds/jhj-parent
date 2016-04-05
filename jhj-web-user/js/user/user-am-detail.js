myApp.onPageBeforeInit('user-am-detail-page', function(page) {

});

//列表显示
myApp.template7Data['page:user-am-detail-page'] = function() {
	var result;
	var userId = localStorage['user_id'];
	var staffId = localStorage['staff_id'];

	$$.ajax({
		type : "GET",
		url : siteAPIPath + "user/user_get_staff_detail.json?user_id="+ userId +"&staff_id="+staffId,
		dataType : "json",
		cache : true,
		async : false,
		success : function(data) {
			result = data.data;
		}
	})

	return result;
}
