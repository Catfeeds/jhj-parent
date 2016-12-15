var formVal = $('form').validate({
	errorElement : 'span', // default input error message container
	errorClass : 'help-block', // default input error message class
	focusInvalid : false, // do not focus the last invalid input
	rules : {
		smsTempId : {
			required : true
		},
		
		userGroupTypeList : {
			required : true
		}
	},

	messages : {

		smsTempId : {
			required : "请输入短信ID"
		},
		
		userGroupTypeList : {
			required : "请选择发送用户类型"
		}
	},

	highlight : function(element) { // hightlight error inputs
		$(element).closest('.form-group').addClass('has-error'); // set error
	},

	success : function(label) {
		label.closest('.form-group').removeClass('has-error');
		label.remove();
	},

	errorPlacement : function(error, element) {
		error.insertAfter(element.parents(".col-md-5"));
	}
});

function selectOne(){
	var userGroupTypeList = $("input[name='userGroupTypeList']");
	var checked = document.getElementById("isAll").checked; 
	for(var i=1;i<userGroupTypeList.length;i++){
		if(checked){
			userGroupTypeList[i].checked=false;
		}
	}
}

$(".isVip").on('click',function(){
	var chk = $(".isVip");
	var chkOther = $(".other");
	for(var i=0;i<chk.length;i++){
		if(chk[i].checked){
			document.getElementById("isAll").checked=false;
		
		}
	}
	for(var i=0;i<chkOther.length;i++){
		if(chkOther[i].checked){
			chkOther[i].checked=false;
		}
	}
});

$(".other").on('click',function(){
	var chk = $(".other");
	var chkIsVip = $(".isVip");
	for(var i=0;i<chk.length;i++){
		if(chk[i].checked){
			document.getElementById("isAll").checked=false;
		}
	}
	for(var i=0;i<chkIsVip.length;i++){
		if(chkIsVip[i].checked){
			chkIsVip[i].checked=false;
		}
	}
});

$("#sendSms").on('click',function(){
	var marketSmsId= $("#marketSmsId").val();
	if(confirm("请确认是否发送短信？")){
		location.href="send-marketsms?marketSmsId="+marketSmsId;
	}
	
});

//测试发送短信
$("#testsendSms").on('click',function(){
	var marketSmsId= $("#marketSmsId").val();
	if(confirm("请确认是否发送短信？")){
		location.href="send-marketsms?marketSmsId="+marketSmsId+"&testuserGroupType=99";
	}
	
});


