
//根据服务大类获取服务子类
$(".parentServiceType").on('change', function(){
	
	var serviceTypeId = $("input[name='parentServiceType']:checked").val();
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
//				var selectServiceType = $("#selectServiceType").val();
//				$serviceTypeOptions = '<option value="0">请选择服务子类</option>';
				$serviceTypeOptions = "",
				$.each($result.data, function(i, obj) {
					if (i==0) {
						$serviceTypeOptions += '<label class="checkbox-inline"><input type="radio" value="'+obj.service_type_id+'" checked />' + obj.name +'</label>';
					} else {
						$serviceTypeOptions += '<label class="checkbox-inline"><input type="radio" value="'+obj.service_type_id+'" />' + obj.name +'</label>';
					}
					
				});
				
				$("#serviceType").html($serviceTypeOptions);
			}
		},
		error:function(){
			
		}
	});
});

$(".parentServiceType").trigger("change");