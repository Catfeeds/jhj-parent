

function orderLink(url, serviceTypeId) {
	
	localStorage.setItem("firstServiceType",serviceTypeId);
	url = url + "?firstServiceType="+serviceTypeId;
	mainView.router.loadPage(url);
}

