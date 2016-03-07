/**
 *  点击添加，复制一个 选项框
 */

$("#addOption").click(function(){
	
	var cloneNode = ("<div class='input-group m-bot15'> " 
                            +"<span class='input-group-addon'>"
                            + 		"<input type='radio' name='optionRadio' id='optionRadio'>"
                            +"</span> "
                            +"<textarea name='optionText' maxlength='100' placeholder='提示:建议不超过100字,输入框可拖动' class='form-control'></textarea>"
                            +"<span class='input-group-addon'><button type='button' onclick='myDelOption(this)' name='delOption'  class='close'>&times;</button></span>"
					+"</div>");
	
	$(this).before(cloneNode);
	
});

/**
 * 点击 X 号,删除该选项
 * 	
 * 		用delOption作为 方法名，会出现 function undefined，可能有冲突~
 * 	
 * 	 此处需要自定义 点击事件函数，追加的节点，不能绑定 onclick 事件~	
 * 
 */
function myDelOption(obj){
	
	//删除按钮的个数,选择题最少要有两个选项, 这里用 size() 或 length 都行~
	var optionNum = $("button[name=delOption]").size();
	
	if(optionNum === 2){
		alert("选择题至少有两个选项");
		return false;
	}
	
	$(obj).parent().parent().remove();
}


///*
// *  选择题目位置
// */
//$("input[name=isFirst]").on('change',function(){
//	
//	var selectVal = $(this).val();
//	
//	alert(selectVal == 0);
//	//如果选择的第一题
//	if(selectVal == "0"){
//		$("#selectBefore").hide();
////		$("#selectNext").show();
//	}
//	//如果选择的位于中间
//	if(selectVal == "1"){
//		$("#selectBefore").show();
////		$("#selectNext").show();
//	}
//	//如果选择的最后一题
//	if(selectVal == "2"){
//		$("#selectBefore").show();
////		$("#selectNext").hide();
//	}
//	
//});





/*
 * 提交时 封装 选项和 选项描述 为 json, 并构造 json 数组,封装所有选项
 */
$("#questionFormSubmit").on('click',function(){

	
   //题库Id
   var bankId = $("#bankId").find("option:selected").val();
   //题干
   var title = $("#title").val();
   //是否必考
   var isMulti = $("input[name='isMulti']:checked").val();
   
   //所有选项	
   var optionArray = [];	
  
  
  $("textarea[name=optionText]").each(function(key,strValue){
	  optionArray.push($(this).val());
  });	

  //题目 id
  var qId = $("#qId").val();
  
  //题目位置
  var isFirst = $("input[name='isFirst']:checked").val();
  
  //上一题的id
  var beforeQId = $("#beforeQId").find("option:selected").val();
  
  
//  return false;
 
 $.ajax({
	   type: 'POST',
		url: '/jhj-oa/survey/question_form.json',
		dataType: 'json',
		cache: false,
	traditional: true,  //该参数，实现前后台的数组传值，前台 array,后台list
		data:{
			"qId" : qId,
			"bankId" : bankId,
			"title" : title,
			"isMulti" : isMulti,
			"isFirst" : isFirst, 
			"beforeQId": beforeQId,
		"optionArray" : optionArray,
		},
		success:function(datas, textStatus, jqXHR){
			
			var msg = datas.msg;
			
//			return false;
			if(msg == 'success'){
				window.location.href = "/jhj-oa/survey/question_list";
			}else{
				alert("系统错误");
			}
			
		},
		error:function(){
			alert("出错了！");
		}
 	});
 
})





/*
 *  页面加载事件
 *   	
 */

