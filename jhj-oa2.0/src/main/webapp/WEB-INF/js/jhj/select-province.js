
//根据省份ID获取城市列表
$("#provinceId").on('change', function(){

	if ( $("#cityId").length <= 0 ) { 
		return false;
	}	

	
	$provinceId = $(this).val();
	if(0 == $provinceId){
		$optionList = '<option value="0">全部</option>';
		$("#cityId").html($optionList);
		return;
	}
	
	//发送ajax请求根据省ID获取城市ID
	$.ajax({
		type: 'GET',
		url: '/jhj-oa/interface-dict/get-city-by-provinceId.json',
		dataType: 'json',
		cache: false,
		data:{provinceId:$provinceId},
		success:function($result){
			if(0 == $result.status){
				var citySelectedId = 0;
				if ( $("#citySelectedId").length >0 ) { 
					citySelectedId = $('#citySelectedId').val();
				}		
				
				//针对所在城市的下拉联动

				$cityOptions = '<option value="0">全部</option>';
				//$optionList = "";
				$.each($result.data, function(i, obj) {
					if (obj.city_id == citySelectedId) {
						$cityOptions += '<option value="'+obj.city_id+'" selected>' + obj.name + "</option>";
					} else {
						$cityOptions += '<option value="'+obj.city_id+'">' + obj.name + "</option>";
					}

				});
				
				$("#cityId").html($cityOptions);
			}
		},
		error:function(){
			
		}
	});
});
