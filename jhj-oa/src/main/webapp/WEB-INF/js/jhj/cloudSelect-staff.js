
//根据门店获取员工
//根据省份ID获取城市列表
$("#parentId").on('change', function(){

	if ($("#staffId").length <= 0 ) { 
		return false;
	}	
	setStaffIdOption();
});

//根据云店获取员工
$("#orgId").on('change', function(){

	if ($("#staffId").length <= 0 ) { 
		return false;
	}	
	setStaffIdOption();
});


function setStaffIdOption() {
	var parentId = $("#parentId").val();
	if (parentId == null || parentId == "" ) parentId == 0;

	var orgId = $("#orgId").val();
	if (orgId == null || orgId == "") orgId == 0;
	if ( parentId == 0 && orgId == 0) {
		return false;
	}
	
	var params = {};
	params.parentId = parentId;
	params.orgId = orgId;
	$.ajax({
		type: 'POST',
		url: '/jhj-oa/interface-dict/select-staff-by-cloudOrg.json',
		dataType: 'json',
		cache: false,
		data:params,
		success:function($result){
			if(0 == $result.status){
				var citySelectedId = 0;
				if ( $("#staffSelectedId").length >0 ) { 
					citySelectedId = $('#staffSelectedId').val();
				}		
				

				$cityOptions = '<option value="0">请选择服务人员</option>';
				//$optionList = "";
				$.each($result.data, function(i, obj) {
					if (obj.staff_id == citySelectedId) {
						$cityOptions += '<option value="'+obj.staff_id+'" selected>' + obj.name + "</option>";
					} else {
						$cityOptions += '<option value="'+obj.staff_id+'">' + obj.name + "</option>";
					}

				});
				
				$("#staffId").html($cityOptions);
			}
		},
		error:function(){
			
		}
	});
	
}