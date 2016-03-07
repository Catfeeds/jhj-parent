
function setSelectContent(){
	var tagIds = $("#selectIds").val();
	if (tagIds == "") return false;
	//处理选择按钮的情况
	$("input:checkbox[name='contentId']").each(function(key, index) {
		var tagId = $(this).val();
		if (tagIds.indexOf(tagId) >= 0) {
			$(this).attr("checked",true);
		}
	});	
	
}




/**
 * 表单提交
 * 
 */
$("#optionRefContentFormSubmit").on('click',function(){
	
	
	var contentIdStr = "";
	
	var flag = 1;
	
	$("input:checkbox[name='contentId']:checked").each(function(){
		
		var nowVal = $(this).val();
		
		contentIdStr += nowVal+",";
		
		if(nowVal == undefined || nowVal == "" ){
			flag = 0;
			
		}
		
		
	});
	
	
	if(flag == 0){
		alert("您尚未勾选附加内容,如果不需要添加服务内容,请返回上一页");
		return false;
	}
	
	
	var qId = $("#qId").val();
	
	var optionNo = $("#optionNo").val();
	
	var flagId = $("#flagId").val();
	
	var contentStr = contentIdStr.substring(0,contentIdStr.length-1);
	
	$.ajax({
		   type: 'POST',
			url: '/jhj-oa/survey/option_ref_content.json',
			dataType: 'json',
			cache: false,
			data:{
				"flagId": flagId,
				"qId" : qId,
			"optionNo" : optionNo,
			"contentIdStr":contentStr
			},
			success:function(datas, textStatus, jqXHR){
				
//				return false;
				var msg = datas.msg;
				
//				return false;
				if(msg == 'success'){
					window.location.href = "/jhj-oa/survey/question_list"
				}else{
					alert("系统错误");
				}
				
			},
			error:function(){
				alert("出错了！");
			}
	 	});
	
	
});

