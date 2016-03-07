/**
 *  点击添加，复制一个 选项框
 */

$("#addOption").click(function(){
	
//	//被复制的 div
//	var srcDiv = document.getElementById('optionTemplate');
//	
//	//深copy,包含该节点的子节点及子节点内容； 默认浅copy,只复制当前元素节点 ！！还不能copy 事件！！
//	var cloneNode = srcDiv.cloneNode(true);
	
	// '选项块(div)' 的个数, 最多可设置 4个选项
	var optionNum = $("button[name=delOption]").size();
	
//	if(optionNum === 4){
//		alert("最多设置4个选项");
//		return false;
//	}
	
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

//$("button[name=delOption]").click(function(){
//	
//	alert("1");
//	$(this).parent().parent().remove();
//	
//	
//});

/**
 *   点选 正确答案
 *   
 *    !!!!!! 仅仅需要设置  radio 的 name属性相同,即可实现单选效果！！！！/(ㄒoㄒ)/~~
 */


/*
 * 提交时 封装 选项和 选项描述 为 json, 并构造 json 数组,封装所有选项
 */
$("#questionFormSubmit").on('click',function(){

	
   //服务类别	
   var serviceTypeId = $("#serviceTypeId").val();	
   //题库Id
   var bankId = $("#bankId").find("option:selected").val();
   //题干
   var title = $("#title").val();
   //题目备注	
   var description = $("#description").val();
   //是否必考
   var isNeed = $("input[name='isNeed']:checked").val();
   
   
   //所有选项	
   var optionArray = [];	
  
  //正确答案
  
  /*
   *  此处直接设置为 数组, 应对将来可能的 多选
   */
  var rightOption =[];
  
  
  $("textarea[name=optionText]").each(function(key,strValue){
	  
	  optionArray.push($(this).val());
	  
	  //不用 attr 和 .checked
	  if($(this).parent().find("input[type='radio']").prop("checked")){
		  
		  rightOption.push(key)
	  }
  });	

  
  if(rightOption.length == 0){
	  alert("请选择一个正确答案");
	  return false;
  }
  
  var qId = $("#qId").val();
  
  
 $.ajax({
	   type: 'POST',
		url: '/jhj-oa/university/question_form.json',
		dataType: 'json',
		cache: false,
	traditional: true,  //该参数，实现前后台的数组传值，前台 array,后台list
		data:{
			"qId" : qId,
			"serviceTypeId" : serviceTypeId,
			"bankId" : bankId,
			"title" : title,
			"description" : description,
			"isNeed" : isNeed,
		"optionArray" : optionArray,
		"rightOption" : rightOption
		},
		success:function($result){
			window.location.href = "/jhj-oa/university/question_list";
		},
		error:function(){
			
		}
 	});
 
})




