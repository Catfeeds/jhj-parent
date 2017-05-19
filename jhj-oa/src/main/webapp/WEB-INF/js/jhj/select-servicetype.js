//根据服务大类获取服务子类
$("#parentServiceType").on('change', function(){
	
	var serviceTypeId = $(this).val();
	if(0 == serviceTypeId){
		return;
	}
	
	if (serviceTypeId == 23 || serviceTypeId == 24) {
		$("#orderType").val(0);
	} else {
		$("#orderType").val(1);
	}
	
	//发送ajax请求根据服务大类获取服务类别
	$.ajax({
		type: 'GET',
		url: '/jhj-oa/newbs/get-service-types.json',
		dataType: 'json',
		cache: false,
		data:{parentId:serviceTypeId},
		success:function($result){
			if(0 == $result.status){
				
				//针对服务子项的下拉联动
				var selectServiceType = $("#selectServiceType").val();
				$serviceTypeOptions = '<option value="0">请选择服务子类</option>';
				$.each($result.data, function(i, obj) {
					if (obj.service_type_id == selectServiceType) {
						$serviceTypeOptions += '<option value="'+obj.service_type_id+'" selected>' + obj.name + "</option>";
					} else {
						$serviceTypeOptions += '<option value="'+obj.service_type_id+'">' + obj.name + "</option>";
					}
					
				});
				
				$("#serviceType").html($serviceTypeOptions);
			}
		},
		error:function(){
			
		}
	});
});

$("#parentServiceType").trigger("change");