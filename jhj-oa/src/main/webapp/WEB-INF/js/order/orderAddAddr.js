// ===============地址相关======================================================
// 输入完手机号获取用户信息，根据用户的id获取用户的服务地址

function getAddressList(userId, setAddrId){
	$.ajax({
		type : "get",
		dataType : "json",
		async : false,
		url : "/jhj-app/app/user/get_user_addrs.json?user_id=" + userId,
		success : function(result) {
			var userAddr = result.data;
			setUserAddrSelect(userAddr, setAddrId);
		}
	});
}

//设置用户原有地址.
//1. 需要判断是否有输入过地址.
function setUserAddrSelect(userAddr, setAddrId) {
	var selectid = document.getElementById("addrId");
	var addrId = 0;
	for (var i = 0; i < userAddr.length; i++) {
		if(userAddr[i].is_default=='1'){
			addrId = userAddr[i].id;
		}
		selectid[i + 1] = new Option(userAddr[i].name+" "+userAddr[i].addr, userAddr[i].id,false, false);
	}
	
	if (setAddrId > 0) {
		$("#addrId").val(setAddrId);
	} else {
		if (addrId > 0) {
			$("#addrId").val(addrId);
		}
	}
	
}


function getAddrByMobile(addrId) {
	var mobile = $("#mobile").val();
	if (mobile == undefined || mobile == "") return false;
	$("#addrId").find(":nth-child(2)").remove();
	var reg = /^1[3,5,7,8]\d{9}$/;
	if (reg.test(mobile)) {
		$.ajax({
			type : "post",
			url : "../user/getUser",
			data : {"mobile" : mobile},
			dataType : "json",
			async : false,
			success : function(data) {
				if (data.data != false) {
					var userId = data.data.id;
					$("#userId").data("userId", userId)
					$("#mobile").data("mobile", mobile);
					$("#userId").val(userId);
					var isVip = data.data.isVip;
					$("#isVip").val(isVip);
					if (isVip == 0) $("#userTypeStr").html("普通会员");
					if (isVip == 1) $("#userTypeStr").html("金牌会员");
					serviceTypeChange();
					// changePrice();
					getAddressList(userId, 0);
					
				} else {
					var isResult = confirm("是否添加该用户？");
					if (isResult) {
						regUser(mobile);
					}
					
				}
			}
			
		});
	}
}

getAddrByMobile(0);

function addrChange() {
	var addrName = $("#addrId").find("option:selected").text();
	$("#selectedAddrName").html(addrName);
}

/*
 * 如果用户在系统中不存，注册新用户到系统中
 * 
 */
function regUser(mobile) {
	var mobile = mobile;
	if (mobile != null && mobile.length == 11) {
		$.ajax({
			type : "post",
			url : "/jhj-app/app/user/reg.json",
			data : {
				"mobile" : mobile
			},
			dataType : "json",
			async : false,
			success : function(data) {
				$("#userId").val(data.data);
				$("#userId").data("userId", data.data)
				$("#mobile").data("mobile", $("#mobile").val());
			}
		});
	}
}

// 为新增用户添加注册地址
function address() {
	var mobile = $("#mobile").val();
	if (mobile == "" || mobile == undefined) {
		alert("请先输入手机号码！");
		return false;
	}

}

function saveAddress() {
	
	
	var userId =  $("#userId").val();
	var mobile = $("#mobile").val();
	if (userId == undefined || userId == "" || userId == 0) {
		alert("请在右侧输入手机号");
		return false;
	}
	
	if (mobile == undefined || mobile == "" || mobile == 0) {
		alert("请在右侧输入手机号");
		return false;
	}
	
	var lng = $("#poiLongitude").val();
	var lat = $("#poiLatitude").val();
	var name = $("#suggestId").val();
	var recipientAddr = $("#recipient-addr").val();
	if (lng == undefined || lng == "" || 
		lat == undefined || lat == "" ||
		name == undefined || name == ""
		) {
		alert("请在服务地址下拉中选择!");
		return false;
	}
	
	if (recipientAddr == undefined || recipientAddr == "") {
		alert("请输入门牌号！");
		return false;
	}
	
	var params = {}
	params.user_id = userId;
	params.phone = mobile;
	params.longitude = $("#poiLongitude").val();
	params.latitude = $("#poiLatitude").val();
	params.name = $("#suggestId").val();
	params.addr = recipientAddr;
	params.city = $("#poiCity").val();
	params.addr_id = 0;
	params.is_default = 0;
	form.city = "北京市";
	$.ajax({
		type : "post",
		url : "/jhj-app/app/user/post_user_addrs.json",
		data : params,
		dataType : "json",
		async : false,
		success : function(data) {
			alert("地址添加成功");
			getAddressList(data.data.user_id, data.data.id);
		}
	});
}



function delAddress(){
	var addrId = $("#addrId").val();
	if(addrId==undefined || addrId==null || addrId==''){
		alert("请先选择地址，再点击删除按钮！！");
		return false;
	} 
	$.ajax({
		type:"get",
		url:"/jhj-app/app/user/delete_addr.json?addr_id="+addrId,
		dataType:"json",
		success:function(data){
			if(data.status=='999'){
				alert("您还有未完成的订单，不能删除该地址！");
			}
			if(data.status=='0'){
				$("#addrId option[value='"+addrId+"']").remove();
				$("#recipient-addr").val();
				alert("地址删除成功！");
			}
		}
	});
}

function getAddress(){
	var addrId = $("#addrId").val();
	if(addrId==undefined || addrId==null || addrId==''){
		alert("请先选择地址，再点击修改按钮！！");
		return false;
	} 

	$.ajax({
		type:"get",
		url:"/jhj-app/app/user/get_addr.json?addr_id="+addrId,
		dataType:"json",
		success:function(data){
			if(data.status=='0'){
				var addr = data.data;
				$("#userId").val(addr.user_id);
			    $("#suggestId").val(addr.name);
				$("#recipient-addr").val(addr.addr);
				$("#poiLongitude").val(addr.longitude);
				$("#poiLatitude").val(addr.latitude);
			}
		}
	});
}