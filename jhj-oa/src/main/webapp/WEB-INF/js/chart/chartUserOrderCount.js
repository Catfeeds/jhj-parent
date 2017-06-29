$("#business").on("click",'.modify-business',function(){
	var businessName = $(this).text();
	$("#businessName").val(businessName);
	var html = $("#form2").html();
	layer.tips(html,$(this),{
		tips:[3,'#5FB878'],
		area: ['400px', '160px'],
		time:0,
		closeBtn:2
		
	});
});

function modifyName(){
	
	var name = $("input[type='radio']:checked").val();
	if(name==undefined || name==null || name=='') {
		layer.alert("请选择人员");
		return false;
	}
	var businessName = $("#businessName").val();
	
	var param = {};
	param.name = name;
	param.businessName = businessName;
	
	$.ajax({
		type:"post",
		url:"../cooperate/modifyBorker",
		data:param,
		success:function(data){
			layer.close(layer.index);
			location.reload();
		}
	});
	
	
	
}
