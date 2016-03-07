

/**
 * 	选择子服务类型时,动态加载不同的结构
 *  
 *  1 = 选择题 2 = 填空题   0=无
 * 
 */
$("input[name='contentChildType']").on('change',function(){
	
	var selectVal = $(this).val();	
	
	//如果选的无
	if(selectVal === "0"){
		$("#childService").html("");
		$("#childService").html("");
	}
	
	//如果选的是 多选题
	if(selectVal === "2"){
//		$("#childService").html("");
//		$("#childService").html(
//				"<input type='text' id='contentChildDescription'  class='form-control' placeholder='如(家电清洗的子服务)空调1次....' maxLength='200' >"
//		);
		
		
		$("#childService").html("");
		$("#childService").html(
				"<div class='input-group m-bot15' >"
					+"<span class='input-group-addon'>"
					+"<button type='button' id='addOption' onclick='myAddOption(this)' title='添加选项'><i class='icon-plus'></i></button>"
					+"</span>"
                +"<textarea name='optionText' maxlength='100' placeholder='提示:点击左边单选框,选择正确答案' class='form-control'></textarea>"
                	+"<span class='input-group-addon'>"
                		+"<button type='button' title='删除该选项' name='delOption' onclick='myDelOption(this)' class='close'>&times;</button>"
                +"</span>"
                +"</div>"
		);
		
	}
	
	//如果选的 单选题
	if(selectVal === "1"){
		$("#childService").html("");
		$("#childService").html(
				"<div class='input-group m-bot15' >"
					+"<span class='input-group-addon'>"
					+"<button type='button' id='addOption' onclick='myAddOption(this)' title='添加选项'><i class='icon-plus'></i></button>"
					+"</span>"
                +"<textarea name='optionText' maxlength='100' placeholder='提示:点击左边单选框,选择正确答案' class='form-control'></textarea>"
                	+"<span class='input-group-addon'>"
                		+"<button type='button' title='删除该选项' name='delOption' onclick='myDelOption(this)' class='close'>&times;</button>"
                +"</span>"
                +"</div>"
		);
	}
	
});

/**
 * 针对子服务的 事件
 */

//添加选项
function myAddOption(obj){

	var srcDiv = document.getElementById('childService');
	//深copy,包含该节点的子节点及子节点内容； 默认浅copy,只复制当前元素节点 ！！还不能copy 事件！！
	var cloneNode = srcDiv.cloneNode(true);
	
	$(obj).parent().parent().parent().before(cloneNode);
	
}
//删除选项
function myDelOption(obj){
	
	$(obj).parent().parent().remove();
}


/**
 * 提交表单
 */
$("#contentFormSubmit").on("click",function(){
	
	//内容Id
	var contentId = $("#contentId").val();
	
	//服务大类
	var serviceId = $("#serviceId").val();
	//服务名称
	var name = $("#name").val();
	//价格
	var price = $("#price").val();
	//价格说明
	var priceDescription = $("#priceDescription").val();	
	//服务说明
	var description = $("#description").val();
	//子服务说明
//	var contentChildDescription = $("#contentChildDescription").val();
	
	//子服务展示类型
	var contentChildType = $("input[name='contentChildType']:checked").val();
	
	//子服务内容
	
	//填空题的内容--> 如 空调清洗1次,家电清理2次。。。。。
	var contentChildDescription = "";
	
	if(contentChildType == "2"){
		contentChildDescription = $("#contentChildDescription").val();
	}
	
	//选择题的内容数组
	var optionArray = [];	
	
	if(contentChildType == "1" || contentChildType == "2"){
		
		  $("textarea[name=optionText]").each(function(key,strValue){
			  optionArray.push($(this).val());
		  });	
	}
	
	//计数期限
	var measurement = $("input[name='measurement']:checked").val();
	//是否可用
	var enable = $("input[name='enable']:checked").val();
	
//	return false;
	$.ajax({
		   type: 'POST',
			url: '/jhj-oa/survey/content_form.json',
			dataType: 'json',
			cache: false,
		traditional: true,  //该参数，实现前后台的数组传值，前台 array,后台list
			data:{
				"contentId" : contentId,
				"serviceId" : serviceId,
					 "name" : name,
					"price" : price,
		 "priceDescription" : priceDescription,
			  "description" : description,
		 "contentChildType" : contentChildType,
  "contentChildDescription" : contentChildDescription,
			  "optionArray" : optionArray,
			  "measurement" : measurement,
			  	   "enable" : enable
			},
			success:function(datas, textStatus, jqXHR){
				
				var msg = datas.msg;
				
				if(msg == 'success'){
					window.location.href = "/jhj-oa/survey/content_list"
				}else{
					alert("系统错误");
				}
				
			},
			error:function(){
				alert("出错了！");
			}
	 	});
	
	
	
});
