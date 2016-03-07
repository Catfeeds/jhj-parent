
myApp.onPageBeforeInit('mine-add-addrs', function(page) {

	var userId = localStorage['user_id'];
	
	addrId = page.query.addr_id;
	
	var returnUrl = page.query.return_url;
	$$("#mine-addr-save").removeAttr('disabled');

	if (userId == undefined || userId == '' || userId == 0) {
		return;
	}

	if (addrId == undefined || addrId == '') {
		addrId = 0;
	}
	
	if (returnUrl == undefined || returnUrl == '') {
		returnUrl = "user/mine-addr-list.html";
	}

	$$("#user_id").val(userId);
	$$("#addr_id").val(addrId);

	var addrSuccess = function(data, textStatus, jqXHR) {
		myApp.hideIndicator();
		$$("#mine-addr-save").removeAttr('disabled');
		var result = JSON.parse(data.response);
		if (result.status == "999") {
			myApp.alert(result.msg);
			return;
		}
		
		if (result.data.is_default == 1) {
			localStorage['default_addr_id'] = result.data.id;
			localStorage['default_addr_name'] = result.data.name + " " + result.data.addr;			
		}

		
		getAm();
		
		localStorage['am_mobile'] = result.data.amMobile;
		

		
		mainView.router.loadPage(returnUrl);
	}

	$$('#mine-addr-save').on('click', function() {
		console.log("mine-addr-save click");
		$$("#mine-addr-save").attr("disabled", true);
		if (addrFormValidation() == false) {
			$$("#mine-addr-save").removeAttr('disabled');
			return false;
		}

		var formData = myApp.formToJSON('#addr-form');

		$$.ajax({
			type : "POST",
			url : siteAPIPath + "user/post_user_addrs.json",
			dataType : "json",
			cache : false,
			data : formData,
			statusCode : {
				200 : addrSuccess,
				400 : ajaxError,
				500 : ajaxError
			},
		});
		

	});

});

//百度调用接口返回处理
function baiduAutoCompleteSuccess(data, textStatus, jqXHR) {
	var result = JSON.parse(data.response);
	var list = result.data;
	if (list.length > 0)
		$$('#addr-auto-list ul').html("");
	var html = $$('#autocomplete-list-part-li').html();
	var resultHtml = '';
	$$.each(list, function(i, item) {
		if (item.location != undefined) {
			var htmlPart = html;
			htmlPart = htmlPart.replace(new RegExp('{item_name}', "gm"),
					item.name);
			htmlPart = htmlPart.replace(new RegExp('{item_lat}', "gm"),
					item.location.lat);
			htmlPart = htmlPart.replace(new RegExp('{item_lng}', "gm"),
					item.location.lng);
			htmlPart = htmlPart.replace(new RegExp('{item_city}', "gm"),
					item.city);
			htmlPart = htmlPart.replace(new RegExp('{item_uid}', "gm"),
					item.uid);
			resultHtml += htmlPart;
		}
	});
	$$('#addr-auto-list ul').append(resultHtml);

}

//发起请求调用百度关键字提示接口
function getBaiduAutoComplete() {

	var paramData = {};
	paramData.query = $$("#name").val();
	paramData.region = "131";
	paramData.output = "json";
	paramData.ak = "2sshjv8D4AOoOzozoutVb6WT";

	$$.ajax({
		type : "GET",
		url : siteAPIPath + "map/autocomplete.json",
		dataType : 'json',
		cache : true,
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		data : paramData,
		statusCode : {
			200 : baiduAutoCompleteSuccess,
			400 : ajaxError,
			500 : ajaxError
		},
		success : function() {

		}
	});
}

//下拉列表选择后处理.
function selectAddr(objDiv) {
	var obj = objDiv.find('li');
	var latObj = obj.find('input[name=item_lat]');
	var lngObj = obj.find('input[name=item_lng]');
	var cityObj = obj.find('input[name=item_city]');
	var uidObj = obj.find('input[name=item_uid]');
	var nameObj = obj.find('div[class*=item-title]');
	var lng = lngObj.val();
	var lat = latObj.val();
	var city = cityObj.val();
	var name = nameObj.html();
	var uid = uidObj.val();

//		console.log("lng = " + lng);
//		console.log("lat = " + lat);
//		console.log("city = " + lng);
//		console.log("name = " + name);
//		console.log("uid = " + uid);

	$$("#name").val(name);
	$$("#longitude").val(lng);
	$$("#latitude").val(lat);
	$$("#city").val(city);
	$$("#uid").val(uid);

	$$('#addr-auto-list ul').html("");

}

function addrFormValidation() {
	var formData = myApp.formToJSON('#addr-form');
	if (formData.name == "") {
		myApp.alert("请输入小区名或大厦名，并在下拉列表中选择。");
		return false;
	}

	if (formData.longitude == "" || formData.latitude == "") {
		myApp.alert("请输入小区名或大厦名，并在下拉列表中选择。");
		return false;
	}

	if (formData.addr == "") {
		myApp.alert("请输入详细门牌号");
		return false;
	}

	return true;
}
