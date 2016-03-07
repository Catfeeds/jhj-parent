$(function() {
	$("#back-btn").click(function() {
		history.go(-1);
	})
});


function setSelectClass(){
	$("#orgId").removeClass();
};

window.onload = setSelectClass;


////导出excel
//$("#exportExcel").click(function(){
////	console.log("table2excel exportExcel");
//	$("#table2excel").table2excel({
//		exclude: ".noExl",
//		name: "订单列表",
//		filename: "订单列表"
//	});
// });