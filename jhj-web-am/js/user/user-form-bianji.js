myApp.onPageBeforeInit('user-form-bianji-page', function (page) {
	
	var userId =  page.query.user_id;
	//获取订单详情
	getOrderInfo(userId);	

	//客户资料调整保存
	$$("#user-kehuzl-submit").on("click", function() {
		$$("#mine-addr-save").attr("disabled", true);
		
		if (orderFormValidation() == false) {
			return false;
		}
		
		var formData = myApp.formToJSON('#user-form-bianji');
		
		$$.ajax({
			type : "POST",
			url : siteAPIPath+ "user/post_user_edit.json?user_id="+userId,
			dataType : "json",
			cache : false,
			data : formData,
			statusCode : {
				200 : saveOrderSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
		});
	})
	
});
//获取订单信息
function getOrderInfo(userId) {
	$$.ajax({
		type : "GET",
		url :siteAPIPath+"user/get_user_edit_detail.json?user_id="+userId,
		dataType : "json",
		cache : true,
		statusCode: {
         	200: orderInfoSuccess,
 	    	400: ajaxError,
 	    	500: ajaxError
 	    },
	});
}
var orderInfoSuccess =function(data, textStatus, jqXHR) {
 	var result = JSON.parse(data.response);
 	var orderHourViewVo = result.data; 

	if (result.status == "999") {
		myApp.alert(result.msg);
		return;
	}
	
	var aaa = orderHourViewVo.list;
	
	var bbb = orderHourViewVo.tag_list;
	
	var selectedTagIds = "";
	if(bbb !=null) {
		for(var j = 0; j< bbb.length;j++){
			selectedTagIds+= bbb[j] + ",";
		}	
		
		$$("#tag_ids").val(selectedTagIds);
	}
		
	
	var hasSelected = false;
	var tagHtml = "";
	if(aaa !=null){
	for(var i = 0; i<aaa.length;i++){
		hasSelected = false;
		if(bbb !=null){
		for(var j = 0; j< bbb.length;j++){
			
			if(aaa[i].tag_id == bbb[j]){
				
				hasSelected = true;
				
				break;
			}
		}
		}
		if(hasSelected){
			if (i % 2 == 0 ) {
				tagHtml +=  "<a href='#' id='tag_id_'"+orderHourViewVo.list[i].tag_id+" onclick='tagClick("+orderHourViewVo.list[i].tag_id+", $$(this))' class='button button-cancel button-round kehuxq-biaoqian'  style='margin-left: 2%;'>"+orderHourViewVo.list[i].tag_name+"</a>";
			} else {
				tagHtml +=  "<a href='#' id='tag_id_'"+orderHourViewVo.list[i].tag_id+" onclick='tagClick("+orderHourViewVo.list[i].tag_id+", $$(this))' class='button button-cancel button-round kehuxq-biaoqian'>"+orderHourViewVo.list[i].tag_name+"</a>";
			}
		}else{
			
			if (i % 2 == 0 ) {
				tagHtml +=  "<a href='#' id='tag_id_'"+orderHourViewVo.list[i].tag_id+" onclick='tagClick("+orderHourViewVo.list[i].tag_id+", $$(this))' class='button button-cancel button-round kehuxg-biaoqian'  style='margin-left: 2%;'>"+orderHourViewVo.list[i].tag_name+"</a>";
			} else {
				tagHtml +=  "<a href='#' id='tag_id_'"+orderHourViewVo.list[i].tag_id+" onclick='tagClick("+orderHourViewVo.list[i].tag_id+", $$(this))' class='button button-cancel button-round kehuxg-biaoqian'>"+orderHourViewVo.list[i].tag_name+"</a>";
			}
			
		}		
		
		
	}}
	$$("#tagNames").append(tagHtml);
	console.log(orderHourViewVo);
    
	  $$("#name").text(orderHourViewVo.name);
	  $$("#mobile").text(orderHourViewVo.mobile);

	  $$("#userAddr").text(orderHourViewVo.addr_name);
	  $$("#remarks").text(orderHourViewVo.remarks);
	  $$("#addrId").text(orderHourViewVo.addr_id);
	  /*var formData = {
		

		'name' : orderHourViewVo.name,
		'mobile' : orderHourViewVo.mobile,
		'sex' : orderHourViewVo.sex,
		'remarks' : orderHourViewVo.remarks,
		
		'userAddr' : orderHourViewVo.addr_name,

        'addrId' : orderHourViewVo.addrId,
       // 'tagName' : orderHourViewVo.tagName
       
		
	}
	myApp.formFromJSON('#user-form-bianji', formData);*/
}



function saveOrderSuccess(data, textStatus, jqXHR) {
	 $$("#mine-addr-save").removeAttr('disabled');
	myApp.hideIndicator();
	var results = JSON.parse(data.response);
	
	if (results.status == "999") {
		myApp.alert(results.msg);
		return;
	}
	if (results.status == "0") {
		myApp.alert("信息修改成功");
		mainView.router.loadPage("user/user-list.html");
	}
} 





function orderFormValidation() {
	var formData = myApp.formToJSON('#order-alert-form');

	if (formData.mobile == "") {
		myApp.alert("请输入手机号");
		return false;
	}
	if (formData.remarks == "") {
		myApp.alert("请输入服务要求");
		return false;
	}

	/*if(formData.sex !="男"&formData.sex !="女"){
		myApp.alert("请输入正常的性别");
		return false;
	}*/
	return true;
}

function tagClick(tagId, obj) {
	//如果未选中，则换class为选中   

	var tagIds = $$("#tag_ids").val();
	
	//kehuxq-biaoqian = 选中
	if (obj.is(".kehuxg-biaoqian")) {
		obj.addClass("kehuxq-biaoqian");
		obj.removeClass("kehuxg-biaoqian");
		
		if (tagIds.indexOf(tagId + ",") < 0) {
			tagIds += tagId + ",";
		}
	//kehuxg-biaoqian = 未选中	
	} else {
		obj.removeClass("kehuxq-biaoqian");
		obj.addClass("kehuxg-biaoqian");	
		if (tagIds.indexOf(tagId + ",") >= 0) {
			tagIds = tagIds.replace(tagId + ",", "");
		}		
	}
	
	 $$("#tag_ids").val(tagIds);
	 
	 console.log( $$("#tag_ids").val());
	
	
	
}
