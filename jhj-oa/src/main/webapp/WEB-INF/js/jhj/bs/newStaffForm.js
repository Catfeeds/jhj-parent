/*
 * 表单校验
 */

$('#newStaff-form').validate({
	errorElement : 'span', // default input error message container
	errorClass : 'help-block', // default input error message class
	focusInvalid : false, // do not focus the last invalid input
	rules : {
		name : {
			required : true
		},
		mobile : {
			required : true,
			number : true
		},
		cardId : {
			required : true,
		},
		addr : {
			required : true
		},
		orgId: {
			required: true,
			orgId:"orgId"
		},
	
	},

	messages : {
		name : {
			required : "员工姓名为必填项"
		},
		mobile : {
			required : "手机号码为必填项",
			number : "手机号码必须为数字"
		},
		cardId : {
			required : "身份证号为必填项",
		},
		addr : {
			required : "请输入员工联系地址"
		},

	},

	invalidHandler : function(event, validator) { // display error alert on
		// form submit
		$('.alert-error', $('#newStaff-form')).show();
	},

	highlight : function(element) { // hightlight error inputs
		$(element).closest('.form-group').addClass('has-error'); // set error
		// class to
		// the
		// control
		// group
	},

	success : function(label) {
		label.closest('.form-group').removeClass('has-error');
		label.remove();
	},

	errorPlacement : function(error, element) {
		error.insertAfter(element.parents(".col-md-5"));
	}

});

$.validator.addMethod("orgId",function(value,elements){
	
	// value 值是  option 选项 在 所有 option 中的 下标，从 0开始
	if(value != 0){
		return true;
	}
},"请选择云店");



$('.org-form input').keypress(function(e) {
	if (e.which == 13) {
		$("#addstaff_btn").click();
		return false;
	}
});

$("#orgStaffForm_btn").click(function(form) {
	
	//如果是新增 页面。需要校验 身份证号，
	
	var oldCardId = $("#oldCardId").val();
	
	var oldMobile = $("#oldMobile").val();
	
	var nowCardId = $("#cardId").val();
	
	var nowMobile = $("#mobile").val();
	
	return checkThisForm();
	
	if (confirm("确认要保存吗?")) {
		
		console.log($("#autoIds").val());
		return false;
		if(staffId == 0 ){
			if(ajaxReturn1 != "0" && ajaxReturn2 != "0" && $('#newStaff-form').validate().form()){
				$('#newStaff-form').submit();
			}else{
				return false;
			}
		}else{
			if(oldMobile == nowMobile && oldCardId == nowCardId){
				if ($('#newStaff-form').validate().form()) {
					$('#newStaff-form').submit();
				}else{
					return false;
				}
			}else{
				
				if(ajaxReturn1 != "0" && ajaxReturn2 != "0" && $('#newStaff-form').validate().form()){
					$('#newStaff-form').submit();
				}else{
					return false;
				}
			}
		}
		
	}else{
		return false;
	}
});

$('.input-group.date').datepicker({
	format : "yyyy-mm-dd",
	language : "zh-CN",
	autoclose : true,
	startView : 1,
	defaultViewDate : {
		year : 1960,
		month : 0,
		day : 1
	}
});



/*
 * 2016年1月22日19:07:20 
 * 	
 * 	修改页面加载时，回显当前已经 校验通过的  验证项目
 * 
 */
function setReturnAuthButton(){
	
	var authIds = $("#authIds");
	
	if(authIds.val() == "") return false;
	
	//回显
	$("input[name='authButton']").each(function(key, index) {
		var authId = $(this).attr("id");
		if (authIds.val().indexOf(authId + ",") >= 0) {
			$(this).attr("class","btn btn-success");
		}
	});	
	
}

/*	
 * 	2016年1月22日18:22:55  设置  审核校验时的认证状态
 * 	
 * 	点击变色
 * 
 */
function setAuthButton(obj){
	
	var classStr = $(obj).attr("class");
	
	if(classStr.indexOf('btn-default')>0){
	
		$(obj).attr("class","btn btn-success");
	}
	
	if(classStr.indexOf('btn-success')>0){
		$(obj).attr("class","btn btn-default");
	}
	setAuthIds();
}

/*
 * 提交时，重新设置 authIds ,提交选中的 被审核的
 */
function setAuthIds(){
	
	var authIds = $("#authIds");
	var tagSelected = "";
	//处理选择按钮的情况
	$("input[name='authButton']").each(function(key, index) {
		if ($(this).attr("class") == "btn btn-success") {
			tagSelected = tagSelected + $(this).attr("id") + ",";
		}
	});	

	if (tagSelected != "") {
		tagSelected = tagSelected.substring(0, tagSelected.length - 1);
	}
//	console.log(tagSelected);
	authIds.val(tagSelected);
}


var ajaxReturn1 = "";
//校验手机号是否重复


function validMobileNum(){
	var inputName = encodeURIComponent(document.getElementById("mobile").value);

	if(!validMobile(inputName)){
		alert("手机号码有误！");
		return false;
	}
	
	var option ={
		url:"/jhj-oa/bs/validMobile",	
		type:"get",
		dataType:"text",
		data:{
			name:inputName
		},
		success:function(responseText){
			if(responseText == "no"){
				 $("#showResult").html("手机号已存在");
                 $("#showResult").css("color","red");
                 ajaxReturn1 = "0";
			}else{
				$("#showResult").html("");
				ajaxReturn1 = "1";
			}
		},
		error:function(){
			ajaxReturn1 = "0";
		}
	} ;
	
	$.ajax(option);
}


var ajaxReturn2 ="";

//校验身份证号是否重复
function validCardNum(){
	var inputName = encodeURIComponent(document.getElementById("cardId").value);

	if(!checkIdcard(inputName)){
		alert("身份证号不合法");
		return false;
	}
	
	
	var option ={
		url:"/jhj-oa/bs/validCard",	
		type:"get",
		dataType:"text",
		
		data:{
			name:inputName
		},
		success:function(responseText){
			if(responseText == "no"){
				 $("#showResults").html("身份证号已存在");
                 $("#showResults").css("color","red");
                 ajaxReturn2 = "0";
			}else{
				$("#showResults").html("");
				ajaxReturn2 = "1";
			}
		},
		error:function(){
			ajaxReturn2 = "0";
		}
	} ;
	$.ajax(option);
}


/*
 *  表单提交前 校验  。 是否选择 了 员工技能
 */

function checkThisForm(){
	
	
	if($("#level").find("option:selected").val() == 0){
		  
		  alert("请选择员工等级");
		  return false; 
	  }
	
	
	
	
  var skl = $("#skillId").val();
  
  if(skl.indexOf("Ljava") > 0 || skl.length == 0){
	 
	//如果未选中 任何技能。 不让提交 
	 
	 alert("请您至少为当前服务人员勾选一个技能");
	 
	 return  false;
  }
 
  
  
  return true;
  
}

/*
 *  2016年4月1日10:43:42  
 *  
 *  页面加载时。 对于树形结构。只展示  叶子 节点的 checkbox 
 *  
 */
var setTreehh =  function setTreeDisplay(){
	
	var secondNode = $("#skillId_tree").children("ul").children("li");
	
	
	for(var i = 0,j=secondNode.length; i < j; i++){
		
		$(secondNode).children("input").attr("type","hidden");
		
	}
	
}

window.onload = setTreehh;


/*
 * 技能标签
 */
function setTagIds() {
	
	var tagIds = $("#tagIds");
	var tagSelected = "";
	//处理选择按钮的情况
	$("input[name='tagName']").each(function(key, index) {
		if ($(this).attr("class") == "btn btn-success") {
			tagSelected = tagSelected + $(this).attr("id") + ",";
		}
	});	

	if (tagSelected != "") {
		tagSelected = tagSelected.substring(0, tagSelected.length - 1);
	}
	tagIds.val(tagSelected);
}


function setTagButton(obj) {
	
	var classStr = $(obj).attr("class");
	
	if(classStr.indexOf('btn-default')>0){
	
		$(obj).attr("class","btn btn-success");
	}
	
	if(classStr.indexOf('btn-success')>0){
		$(obj).attr("class","btn btn-default");
	}
	
	setTagIds();
}

function setReturnTagButton(){
	
	var authIds = $("#tagIds");
	
	if(authIds.val() == "") return false;
	
	//回显
	$("input[name='tagName']").each(function(key, index) {
		var authId = $(this).attr("id");
		if (authIds.val().indexOf(authId + ",") >= 0) {
			$(this).attr("class","btn btn-success");
		}
	});	
	
}




