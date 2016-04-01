$(function() {
	$("#back-btn").click(function() {
		history.go(-1);
	})
});


function setSelectClass(){
	$("#orgId").removeClass();
};

window.onload = setSelectClass;


