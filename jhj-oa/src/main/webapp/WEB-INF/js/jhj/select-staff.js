
//根据云店门店选择服务人员
function onChangeStaff() {
	var parentId = $("#parentId").val();
	var orgId = $("#orgId").val();
	
	if (parentId == undefined || parentId == "") parentId = 0;
	if (orgId == undefined || orgId == "") orgId = 0;
	
	var params = {};
	params.parentId = parentId;
	params.orgId = orgId;
	//发送ajax请求根据省ID获取城市ID
	$.ajax({
		type: 'POST',
		url: '/jhj-oa/bs/select-staff-by-org.json',
		dataType: 'json',
		cache: false,
		data:params,
		success:function($result){
			if(0 == $result.status){
				var citySelectedId = 0;
				if ( $("#staffSelectedId").length >0 ) { 
					citySelectedId = $('#staffSelectedId').val();
				}		
				

				$staffOptions = '<option value="0">请选择服务人员</option>';
				//$optionList = "";
				$.each($result.data, function(i, obj) {
					if (obj.staff_id == citySelectedId) {
						$staffOptions += '<option value="'+obj.staff_id+'" selected>' + obj.name + "</option>";
					} else {
						$staffOptions += '<option value="'+obj.staff_id+'">' + obj.name + "</option>";
					}

				});
				
				$("#selectStaff").html($staffOptions);
			}
		},
		error:function(){
			
		}
	});
}



$("#parentId").on('change', function(){
	onChangeStaff();
});

$("#orgId").on('change', function(){
	onChangeStaff();
});
onChangeStaff();
