myApp.onPageBeforeInit('alter-order-1-page', function (page) {
	  
	var orderNo = page.query.order_no;
	var amId =  page.query.am_id;
	var servcieTypeAddonsList = JSON.parse(localStorage.getItem("service_type_addons_list"));
	
	var saveExpCleanOrderSuccess = function(data, textStatus, jqXHR) {
		$$("#order-clean-submit").removeAttr('disabled');
		myApp.hideIndicator();
		var results = JSON.parse(data.response);
		if (results.status == "999") {
			myApp.alert(results.msg);
			return;
		}
		if (results.status == "100") {
			myApp.alert(results.msg);
			mainView.router.loadPage("order/order-list.html");
		}
		if (results.status == "0") {
			myApp.alert("订单调整完成");
			mainView.router.loadPage("order/order-list.html");
		}
	} 
	//订单调整保存
	var itemNum = Number(0);
	$$("#order-clean-submit").on("click", function(e) {
		 $$("#order-clean-submit").attr("disabled", true);
		if (expCleanOorderFormValidation() == false) {
			$$("#order-clean-submit").removeAttr('disabled');
			return false;
		}
		var jsonData = "";
		$$("input[name = item_num]").each(function(key,index){
			 itemNum = $$(this).val();
			 var serviceAddonIdObj = $$(this).parent().find('input[name=service_addon_id]');
			 var serviceAddonId = serviceAddonIdObj.val();
			 if (itemNum != undefined  && serviceAddonId != undefined) {
				jsonData+="{";
				jsonData +="serviceAddonId"+":"+serviceAddonId +","+"itemNum"+":"+itemNum+",";
				jsonData = jsonData.substring(0,jsonData.length - 1)+"},";
			 }
		});
		var index = jsonData.lastIndexOf(",");
		jsonData = "[" + jsonData.substring(0,index)+"]";
		var formData = myApp.formToJSON('#alter-order-1-view');
		formData.service_addons_datas=jsonData;
	/*	formData.service_addr = $$("#addr_id").val();
		formData.service_date =  moment(formData.serviceDate + ":00", "yyyy-MM-DD HH:mm:ss").unix();
		*/
		
		
		
//		return false;	
			
		$$.ajax({
			type : "POST",
			url : siteAPIPath+ "order/post_am_clean.json",
			dataType : "json",
			cache : false,
			data : formData,
			statusCode : {
				200 : saveExpCleanOrderSuccess,
				400 : ajaxError,
				500 : ajaxError
			}
		});
	})
	
	
});


//列表显示深度保洁项
myApp.template7Data['page:alter-order-1-page'] = function(){
  var result;

  var amId = getParameterByName('am_id');
  var orderNo = getParameterByName('order_no');
  
   $$.ajax({
          type : "GET",
          url:siteAPIPath+"order/get_am_clean_detail.json?am_id="+amId+"&order_no="+orderNo,
          dataType: "json",
          cache : true,
          async : false,
          success: function(data){
        	  console.log(data);
              result = data.data;
          }
  })
  
  return result;
}

function expCleanOorderFormValidation() {
	var formData = myApp.formToJSON('#alter-order-1-view');

	var flag = true;
	$$("input[name='item_num']").each(function(key,index){
		var item_num = $$(this).val();
			if(item_num ==''||item_num ==undefined ){
				myApp.alert("类别数量不能为空！");
				flag =  false;	
			}	
	});
	
	if(!isMoneyNum(formData.order_money) || formData.order_money < 0){
		myApp.alert("请输入合法的数字");
		flag = false;
	}
	return flag;
}
function ValidateNumber(e, pnumber){
	if (!/^\d+$/.test(pnumber)){
	e.value = /^\d+/.exec(e.value);
	}
	return e.value;
}
//加号处理
function onAddItemNum(obj) {

	var itemNumObj = obj.find('input[name=item_num]'); 
	var v = itemNumObj.val();
	if (v == undefined || !isNum(v)) {
		v = 0;
	} else {
		v = parseInt(v) + 1;
	}
	itemNumObj.val(v);

	setTotal();
}

//减号处理
function onSubItemNum(obj) {
	var itemNumObj = obj.find('input[name=item_num]'); 
	console.log(itemNumObj);
	var v = itemNumObj.val();
	if (v == undefined || !isNum(v) || v == 0) {
		v = 0;
	} else {
		v = parseInt(v) - 1;
	}
	itemNumObj.val(v);
	setTotal();	
}


//计算价格总和
function setTotal() {

	var orderMoney =  itemPrice= Number(0);
	var serviceAddonId = 0;
	var serviceAddonIdObj;
	$$("input[name = item_num]").each(function(key, index) {
		 
		 itemNum = $$(this).val();
		 serviceAddonIdObj = $$(this).parent().find('input[name=service_addon_id]');

		 serviceAddonId = serviceAddonIdObj.val();
		 
		 var price = $$(this).nextAll("span[name=item_uint]").text();
		 
		 var reg = /[1-9][0-9]*/g;
		 
		 itemPrice = price.match(reg);
		 
		 
		 if (itemNum != undefined && isNum(itemNum) && serviceAddonId != undefined) {

//			 itemPrice = getServiceAddonPrice(serviceAddonId);
				 
			 orderMoney = Number(orderMoney) + Number(itemNum) * Number(itemPrice);
			 
		 }
	
	});	
	$$("#order_money_input").attr("value",orderMoney);		
	$$("#order_moneys_span").val(orderMoney);		
}
