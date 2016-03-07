$("#serviceTypeId").on('change',function(){
	
	if ( $("#bankId").length <= 0 ) { 
		return false;
	}	

	
	$serviceTypeId = $(this).val();
	if(0 == $serviceTypeId){
		$optionList = '<option value="0">请选择题库</option>';
		$("#bankId").html($optionList);
		return;
	}
	
	//发送ajax请求根据服务类别ID获取题库ID
	$.ajax({
		type: 'GET',
		url: '/jhj-oa/university/get-bank-by-partnerServiceType.json',
		dataType: 'json',
		cache: false,
		data:{serviceTypeId:$serviceTypeId},
		success:function($result){
			if(0 == $result.status){
				var selectBankId = 0;
//				if ( $("#selectAmId").length >0 ) { 
//					selectAmId = $('#selectAmId').val();
//				}		
				

				$bankOptions = '<option value="0">请选择题库</option>';
				//$optionList = "";
				$.each($result.data, function(i, obj) {
					if (obj.bank_id == selectBankId) {
						$bankOptions += '<option value="'+obj.bank_id+'" selected>' + obj.name + "</option>";
					} else {
						$bankOptions += '<option value="'+obj.bank_id+'">' + obj.name + "</option>";
					}

				});
				
				$("#bankId").html($bankOptions);
			}
		},
		error:function(){
			
		}
	});
	
});