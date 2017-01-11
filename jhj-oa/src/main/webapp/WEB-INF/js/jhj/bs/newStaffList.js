function openAuthView(staffId) {
	$("#idAuthViewModal").modal({
		remote: appRootUrl + "newbs/auth-idcard-view?staffId="+staffId,
	});
}

$("#idAuthViewModal").on("hidden.bs.modal", function() {
    $(this).removeData("bs.modal");
});