/*
 * 门店-助理  下拉菜单的 二级联动，及 回显
 */ 


//根据省份ID获取城市列表
$("#orgId").on('change', function(){

	if ( $("#amId").length <= 0 ) { 
		return false;
	}	

	
	$orgId = $(this).val();
	if(0 == $orgId){
		$optionList = '<option value="0">请选择助理</option>';
		$("#amId").html($optionList);
		return;
	}
	
	//发送ajax请求根据省ID获取城市ID
	$.ajax({
		type: 'GET',
		url: '/jhj-oa/bs/get-am-by-orgId.json',
		dataType: 'json',
		cache: false,
		data:{orgId:$orgId},
		success:function($result){
			if(0 == $result.status){
				var selectAmId = 0;
				if ( $("#selectAmId").length >0 ) { 
					selectAmId = $('#selectAmId').val();
				}		
				
				//针对所在城市的下拉联动

				$amOptions = '<option value="0">请选择助理</option>';
				//$optionList = "";
				$.each($result.data, function(i, obj) {
					if (obj.staff_id == selectAmId) {
						$amOptions += '<option value="'+obj.staff_id+'" selected>' + obj.name + "</option>";
					} else {
						$amOptions += '<option value="'+obj.staff_id+'">' + obj.name + "</option>";
					}

				});
				
				$("#amId").html($amOptions);
			}
		},
		error:function(){
			
		}
	});
});

function nowOrgAm(){
	
	var dianAmId = $('#selectAmId').val();	
		
	$("#amIdDian").find("option").each(function(key,value){
		
		if($(this).val() == dianAmId){
			$(this).attr("selected",true);
		}
		
	});
	
}

window.onload = nowOrgAm;

