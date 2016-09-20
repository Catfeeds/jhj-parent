
//根据省份ID获取城市列表
$("#parentId").on('change', function(){

	if ( $("#parentId").length <= 0 ) { 
		return false;
	}	

	
	$orgId = $(this).val();
	if(0 == $orgId){
		$optionList = '<option value="0">全部</option>';
		$("#cloudId").html($optionList);
		return;
	}
	
	//发送ajax请求根据省ID获取城市ID
	$.ajax({
		type: 'GET',
		url: '/jhj-oa/bs/get-cloud-by-orgid.json',
		dataType: 'json',
		cache: false,
		data:{orgId:$orgId},
		success:function($result){
			if(0 == $result.status){
				
				//针对门店的下拉联动

				$cloudOptions = '<option value="0">全部</option>';
				$.each($result.data, function(i, obj) {
					$cloudOptions += '<option value="'+obj.org_id+'">' + obj.org_name + "</option>";
				});
				
				$("#cloudId").html($cloudOptions);
			}
		},
		error:function(){
			
		}
	});
});
