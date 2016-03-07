
//var userIdList =  $("#userIdList").val();

//localStorage.clear();

window.onload = function(){
	
	var oneSelect =  localStorage.getItem("oneSelect");
	//在某一页 选择了 全选，则  所有页的 全选 及当页的 checkbox 都会被选中
	if(localStorage['mySelect'] == 1){
		
		$("#selectAll").prop("checked",true);
		
		$("input[name='oneSelect']").each(function(){
			$(this).prop("checked",true);
		});
		
		
	}
	
	//在某一页取消了 全选，则展示时，所有都不选中，并且清空 之前所有被选中 的 行
	if(localStorage['mySelect'] == 0){
		$("input[name='oneSelect']").each(function(){
			$(this).prop("checked",false);
			localStorage.removeItem("oneSelect");
		});
	}
	
	
	//判断  当前 页的  checkbox 是否 在 之前 被选中过，进行回显
	$("input[name='oneSelect']").each(function(){
		
		
		oneSelect =  localStorage.getItem("oneSelect");
		mySelect = localStorage.getItem("mySelect");
		if(oneSelect == null){
			if(mySelect == 1){
				//如果有全选
				$(this).prop("checked",true);
			}else{
				//初次加载 ，为null, 所有都不 选中
				$(this).prop("checked",false);
			}
		}
		
		if(oneSelect !=null){
			//如果 某行，之前被选过，则 显示选中状态
			if(oneSelect.indexOf($(this).val())>=0){
				$(this).prop("checked",true);
			}
		}
	});
	
	
	//手动勾选所有页 的 单选后，全选也被选中  TODO
}


//全选效果
$("#selectAll").click(function(){
	

	if($(this).prop("checked")){
		localStorage.setItem('mySelect',1);
		localStorage.removeItem("oneSelect");
	}else{
		localStorage.setItem('mySelect',0);
		localStorage.removeItem("oneSelect");
	}
	
	//点击全选则当前 展示页，都被选中
	$("input[name='oneSelect']").prop("checked",this.checked);
	
//	alert(localStorage['oneSelect']);
});



//单选
$("input[name='oneSelect']").click(function() {
	
	/*
	 * 点击之后，状态就会 立刻 改变，
	 * 	当   当前页面，只要有一个 是从选中变为未选中，则 全选 变量 就不为 0
	 */
	
	//此处设置为 2，使全选事件不影响 单选，手动勾选全选后，这个值会被覆盖~
	localStorage.setItem("mySelect",2);
	
	$("#selectAll").prop("checked",false);
	
	
	//所有单选 选完了，选中全选
//	var $subs = $("input[name='oneSelect']");
//	$("#selectAll").prop("checked" , $subs.length == $subs.filter(":checked").length ? true :false);
	
	var oneSelect = localStorage.getItem("oneSelect");
	
	if(oneSelect != null){
		/*
		 * 如果  当前 勾选的 checkbox，之前已经被选中，    再次点击变为 未选中，
		 * 	
		 * 	此时，移除 该行 id
		 */
		
		if(oneSelect.indexOf($(this).val())>=0 && !$(this).prop("checked")){
			
			// !!!! 应该有返回值，才能得到 新的 字符串！！！
			oneSelect = oneSelect.replace($(this).val()+",","");
		}
		
		if($(this).prop("checked")){
			oneSelect = oneSelect+$(this).val()+",";
		}
		
//		else{
//			//新值，加入
//			oneSelect = oneSelect + $(this).val()+ ",";
//		}
	}
	
	
	//第一次加载时，oneSelect为 null
	if($(this).prop("checked") && oneSelect == null){
		oneSelect = oneSelect + $(this).val()+ ",";
		oneSelect = oneSelect.replace('null','');
	}
	
	//点击过后，把初始值 null 从 字符串中 过滤
//	alert(oneSelect);
	localStorage.setItem('oneSelect',oneSelect);
	
});


function getAjaxReturn (){
//	$(this).attr("disabled",true);
	
	var postdata = {};
	
	var amId = $("#amId").find("option:selected").val();	//选择的 助理
	
	var oldAmId = $("#oldAmId").val();	//当前的助理
	
	
	var selectUsers = localStorage.getItem("oneSelect");
	
	
	//校验
	if(selectUsers == null || selectUsers == "" || selectUsers == undefined){
		alert("请选择需要调整的用户");
		return ;
	}
	
	if(amId == 0 ){
		alert("请选择助理");
		return;
	}
	
	if(amId == oldAmId){
		alert("请选择新助理");
		return;
	}
	postdata.am_id = amId;		//新助理 id
	
	postdata.old_am_id = oldAmId;	//当前助理id
	
	
	//构造   所需参数
	var userIds = $("#userIdList").val();
	
	
	if(selectUsers != null || selectUsers != ""){
		//去除 末尾逗号
		selectUsers = selectUsers.substring(0,selectUsers.length-1);
		//去除 初始值 --字符串 null
		selectUsers = selectUsers.replace('null','');
		
	}
	
	//如果 当前页 的 全选，被勾选,则 传参 所有 userId
	if($("#selectAll").checked){
		postdata.user_ids = userIds.join(",");
	}else{
		postdata.user_ids = selectUsers;
	}
	
	
	//开始请求之前，清空 localStorage,避免重复操作
	
	localStorage.clear();
	
	$("#changeAm").attr("disabled",true);
	var option ={
			url:"/jhj-oa/bs/changeAmForUser.json",	
			type:"post",
			dataType:"json",
			async:false,
			data:postdata,
			success:function(data){
//				$("#changeAm").attr("disabled",false);
				
				var sta = data.status;
				if(sta == 0 || sta == 1){
					alert(data.msg);
					return false;
				}
				
				if(sta == 2){
					alert(data.msg);
					
					location.reload();
					
				}
			},
			error:function(){
				return false;
			}
		} ;
		
	$.ajax(option);
	
	
};


//更换助理
$("#changeAm").click(function(){
	
	getAjaxReturn();
	
});