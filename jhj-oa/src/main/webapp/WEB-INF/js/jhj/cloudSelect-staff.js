
//根据省份ID获取城市列表
$("#orgId").on('change', function(){

	if ($("#staffId").length <= 0 ) { 
		return false;
	}	

	
	$provinceId = $(this).val();
	if(0 == $provinceId){
		$optionList = '<option value="0">请选择服务人员</option>';
		$("#staffId").html($optionList);
		return;
	}
	
	//发送ajax请求根据省ID获取城市ID
	$.ajax({
		type: 'POST',
		url: '/jhj-oa/interface-dict/select-staff-by-cloudOrg.json',
		dataType: 'json',
		cache: false,
		data:{"orgId":$provinceId},
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
});
