
/**
 * 	
 * 	添加对应关系模板
 */
$("#addOption").on('click',function(){
	
	var list = $("#optionNoList").val();
	
	
	//被复制的 div
	var srcDiv = document.getElementById('optionTemplate');
	//深copy,包含该节点的子节点及子节点内容； 默认浅copy,只复制当前元素节点 ！！还不能copy 事件！！
	var cloneNode = srcDiv.cloneNode(true);
	
	//带 El表达式。不能copy 特殊字符，此处用上边的方法更合适
	
//	var cloneNode = ("<div class='col-md-5' >"
//					+"<c:forEach "+"items="+list+" var = 'optionNo'>"
//					+"<label><input type='checkbox' id='optionNo' value='${optionNo }'>/${optionNo }</label>"
//					+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
//					+"</c:forEach>"					
//					+"<nextQuestionSelect:select selectId='${optionRefQFormModel.nextQId }'/>"																	
//					+"</div>");											
																
	$(this).before(cloneNode);
	
});


/**
 * 用的copy 方法，不能copy事件，需要自定义onclick 函数
 */
function myDelOption(obj){
	
	
	var num =   $("div[id='optionTemplate']").size();
	
	if(num == 1){
		alert("模板不能删除");
		return false;
	}
	
	$(obj).parent().remove();
}


/**
 * 表单提交
 * 
 */
$("#questionFormSubmit").on('click',function(){
	
	//用来封装所有的组合 {选项数组,下一题id}
	var optionRefArray = [];
	
	//是否有未勾选,或者未选择下一题的标识
	var flag = 1;
	
	
	$("select[name='nextQId']").find("option:selected").each(function(){
		
		var nextQId = $(this).val();
		
		if(nextQId == undefined || nextQId == 0 ){
			flag = 0;
			
		}
		
		
		//拼接当前 选择 题目id 同一组的,被选中的选项
		var opStr = "";
		
		$(this).parent().parent().find("label input[name='optionNo']:checked").each(function(){
			
			opStr += $(this).val()+",";
			
			
		});
		
		
		if(opStr.length <= 0){
			
			flag = 0;
		}
		
		
		
		//构造json 格式字符串
		var aaa = {"nextQId":nextQId,"opStr":opStr.substring(0,opStr.length-1)};
		
		//将每一组 json字符串，放进数组
		optionRefArray.push(aaa);
		
	});
	
	
	//最终传递的json参数, 将 json格式的数组,转换为 json
	var endJson =   JSON.stringify(optionRefArray);
	
	
	var qId = $("#qId").val();
	
	
	if(flag == 0){
//		alert("请选择关联的下一题,如果无关联选项,请返回上一页");
//		alert("请至少勾选一个选项");
		
		alert("您未选择下一题或未勾选对应选项");
		return false;
	}
	
	
	
	$.ajax({
		   type: 'POST',
			url: '/jhj-oa/survey/option_relate.json',
			dataType: 'json',
			cache: false,
//		traditional: true,  //该参数，实现前后台的数组传值，前台 array,后台list
			data:{
				"qId" : qId,
			"optionRefArray" : endJson,
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

