$('#leaveForm').validate({
	errorElement: 'span', //default input error message container
	errorClass: 'help-block', // default input error message class
	focusInvalid: false, // do not focus the last invalid input
	rules: {
		parentId:{
			required: true,
			parentId:"parentId"
		},
		orgId: {
			required: true,
			orgId:"orgId"
		},
		staffId:{
			required: true,
			staffId:"staffId"
		},
		leaveDate:{
			required: true,
		},
		leaveDateEnd:{
			required: true,
		}
	},

	messages: {
		parentId:{
			required: "请选择门店",
		},
		leaveDate: {
			required: "请输入假期开始时间",
		},
		leaveDateEnd: {
			required: "请输入假期结束时间",
		}
	},

	invalidHandler: function (event, validator) { //display error alert on form submit
		$('.alert-error', $('#leaveForm')).show();
	},

	highlight: function (element) { // hightlight error inputs
		$(element)
			.closest('.form-group').addClass('has-error'); // set error class to the control group
	},

	success: function (label) {
		label.closest('.form-group').removeClass('has-error');
		label.remove();
	},

	errorPlacement: function (error, element) {
		error.insertAfter(element.parents(".col-md-5"));
	}

});

$.validator.addMethod("orgId",function(value,elements){
	
	// value 值是  option 选项 在 所有 option 中的 下标，从 0开始
	if(value != 0){
		return true;
	}
},"请选择云店");


$.validator.addMethod("staffId",function(value,elements){
	
	if(value != 0){
		return true;
	}
	
},"请选择服务人员");



$('.form-control.form_datetime').datepicker({
	format : "yyyy-mm-dd",
	language : "zh-CN",
	autoclose : true,
	startView : 0,
	todayBtn:true,
	startDate:new Date(),
});

function fn(){
	var id = $("#id").val();
	if(id >0){
		$("select[name='parentId']").attr("disabled",true);
		$("#orgId").attr("disabled",true);
	}
}
fn();


function staffChange() {
	var staffId = $("#staffId").val();
	var params = {};
	params.staffId = staffId;
	$.ajax({
		type: 'GET',
		url: '/jhj-app/app/staff/get_leave.json',
		dataType: 'json',
		cache: false,
		data: params,
		success:function(result){
			var data = result.data;
			
			if (data == undefined || data == "") return false;
			var startDate = data.start_date;
			var endDate = data.end_date;
			
			$("#leaveDate").val(startDate);
			$("#endDate").val(endDate);
			
			var start = data.start;
			var end = data.end;
			
			
			if (start >= 14) {
				$("#halfDay").val(2);
			}
			if (end <= 14) {
				$("#halfDay").val(1);
			} 
			
			if (start = 0 && end == 23) {
				$("#halfDay").val(0);
			}
			
			
			
		},
	});
}

function leaveDateChange() {
	var leaveDateStr = $("#leaveDate").val();
	var leaveDateEndStr = $("#leaveDateEnd").val();
	console.log(leaveDateStr + "---" + leaveDateEndStr);
	
	var leaveDateObj = moment(leaveDateStr);
	var leaveDateEndObj = moment(leaveDateEndStr);
	
	var d = leaveDateEndObj.diff(leaveDateObj, "days");
	console.log("d == " + d);
	
	if (d == 0) {
		$("#halfDay").removeAttr("disabled"); 
	} else {
		$("#halfDay").attr("disabled", "true");
		$("#halfDay").val(0);
	}
	
}