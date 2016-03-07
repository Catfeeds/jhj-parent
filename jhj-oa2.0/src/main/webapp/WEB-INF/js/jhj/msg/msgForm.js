$(document).ready(function(){
	
	var msgId =  $("#msgId").val();
	
	var sendTime = $("#sendTime").val();
	
	if(msgId >0 && sendTime != 0){
		$("#sendTimeDiv").removeAttr("display");
		$("#sendTimeDiv").show();
		
//		$("input[name='sendWay']").trigger("change");
		
		$("input[name='sendWay']").each(function(k,v){
			
			if($(this).val() == 1){
				
				$(this).attr("checked",true);
				
			}
		})
	}
	
	
});



var formVal = $('#msg-form').validate({
	errorElement : 'span', // default input error message container
	errorClass : 'help-block', // default input error message class
	focusInvalid : false, // do not focus the last invalid input
	rules : {
		title : {
			required : true
		},
		summary : {
			required : true
		},
		content:{
			required: true
		},
	},

	messages : {
		title : {
			required : "请输入消息标题。"
		},
		summary : {
			required : "请输入消息摘要。"
		},
		content : {
			required : "请输入消息内容。"
		},
	},

	invalidHandler : function(event, validator) { // display error alert on
													// form submit
		$('.alert-error', $('#msg-form')).show();
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

$('.msg-form input').keypress(function(e) {
	if (e.which == 13) {
		$("#addMsg_btn").click();
		return false;
	}
});

$("#editMsg_btn").click(function() {
	if (confirm("确认要保存吗?")) {
		
		//提交前，如果选择的定时发送， 判断是否选择了发送时间
		var selectSendWay =  $("input[name='sendWay']:checked").val();
		
		if ($('#msg-form').validate().form()) {
			$('#msg-form').submit();
		}
	}
});

$("#previewMsg_btn").click(function() {
	
	var title = $("#title").val();
	var content = $("#some-textarea").val();
	var summary = $("#summary").val();
	$.ajax({
        type:"post",
        url:"/"+appName+"/interface-promotion/preview-msg.json",
        dataType:"json",
        cache:false,
        data:"title="+title+"&summary="+summary+"&content="+content,
        success :function(data){
        	var data = data.data;
        	window.open(appRootUrl+data);
        },
	    error : function(){
	    }
    });
});

//选择 发送方式,控制 是否隐藏时间选择插件
$("input[name='sendWay']").on('change',function(){
	
	var nowSelect =  $(this).val();
	
	if(nowSelect == 0){
		$("#sendTimeDiv").removeAttr("display");
		$("#sendTimeDiv").hide();
		
		$("#sendTime").val(0);
		
		$("#displaySendTime").val("");
	}
	
	if(nowSelect == 1){
		$("#sendTimeDiv").removeAttr("display");
		$("#sendTimeDiv").show();
		
		
		
	}
});



//带 时分秒的时间选择，需要引入 bootstrap-datetimepicker 相关js
$('.form_datetime').datetimepicker({
	format: "yyyy-mm-dd hh:ii",
	language: "zh-CN",
	autoclose: true,
	todayBtn:true,
	minuteStep:1,
	startDate:"2016-01-15 00:00"
});

//时间改变，校验 事件
$('.form_datetime').datetimepicker().on('changeDate', function(ev){
	
	//当前时间戳
	var nowTime = Math.round(new Date().getTime()/1000);
	
	//将GMT 标准时间转换为“2015-03-19 12:00” 
	var selectTime = formatDateTime(ev.date);
	
	//得到对应的时间戳
	var selectTimeStamp =  moment(selectTime).unix();
	
	//赋值给隐藏域
	$("#sendTime").val(selectTimeStamp-8*3600);
	
	
	/*
	 * ！！ 插件显示的是 格林尼治时间, 但是 ev.date得到的是北京时间,只好手动转了
	 */
	if (selectTimeStamp-8*3600 < nowTime){
		alert("发送时间必须大于当前时间");
		return false;
    }
});



// js处理 格林泥治 (GMT)时间，转换为 “2015-03-19 12:00” 这种形式
var formatDateTime = function (date) {  
    var y = date.getFullYear();  
    var m = date.getMonth() + 1;  
    m = m < 10 ? ('0' + m) : m;  
    var d = date.getDate();  
    d = d < 10 ? ('0' + d) : d;  
    var h = date.getHours();  
    var minute = date.getMinutes();  
    minute = minute < 10 ? ('0' + minute) : minute;  
    return y + '-' + m + '-' + d+' '+h+':'+minute;  
};  

