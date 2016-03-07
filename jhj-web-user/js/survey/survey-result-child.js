myApp.onPageInit('survey-result-child-page', function(page) {
	
	//对于 完成后  又返回的 情况~
	if(localStorage['survey_user_id'] == undefined){
		
		myApp.alert("请您发起新的订制服务");
		mainView.router.loadPage("index.html");
		return false;
	}
	
	
	var contentId = localStorage['changeContentId'];
	
	$$("#contentId").val(contentId);
	
	var postData = {};
	
	postData.content_id = contentId;
	
	$$.ajax({
		type: "get",
		 url: siteAPIPath + "survey/get_child_content_list.json",
		data: postData,
		success:function (datas,sta,xhr){
			
			var result = JSON.parse(datas);
			
			var childArray =   result.data;
			
			
			var childContentTemplate = $$("#childContentTemplate").html();
			
			var childContentHtml  = "";
			
			
//			//回显
			if(localStorage['boxChildContent'] !=null && localStorage['boxChildContent'].length >0){
				
				var nowArray =  JSON.parse(localStorage['boxChildContent']); 
				
				
				for (var i = 0; i < nowArray.length; i++) {
					var nowArrayCont = nowArray[i];
					
					var baseContentId =  nowArrayCont.baseContentId;
					
					if(contentId == baseContentId){
					
						var childBoxArray = nowArrayCont.childBoxArray;
					
						for (var int = 0; int < childBoxArray.length; int++) {
							
							var  childBox = childBoxArray[int];
							
							for (var i = 0; i < childArray.length; i++) {
								
								var child = childArray[i];
								
								if(child.id == childBox.boxChildContentId){
									
									var number = child.option_str.match(/\d/g).join("");
									
									number = childBox.boxChildContentTimes;
									
									child.option_str = child.option_str.replace(/\d/g,number);
								}
							}
						}
						break;
					}
				}
			}
		
			
			for (var i = 0; i < childArray.length; i++) {
				var child = childArray[i];
				
				var part = childContentTemplate;
				
				//默认次数
				var number = child.option_str.match(/\d/g).join("");
				
				//子服务名称
				var optionName = child.option_str.substring(0,child.option_str.indexOf(number));
				
				part = part.replace(new RegExp('{contentName}',"gm"), optionName);
				
				//子服务id
				part = part.replace(new RegExp('{id}',"gm"), child.id);
				
				//子服务默认次数
				part = part.replace(new RegExp('{number}',"gm"), number);
				
				childContentHtml+=part;
			}
			
			$$("#childDisplay").append(childContentHtml);
			
		},
		error:function(){
			myApp.alert("网络错误");
		}
	});
	
	
	/*
	 *  点击确认调整次数
	 */ 
	
	$$("#confirmChangeTimes").on('click',function(){
		
		var boxArray = [];
		
		var childArray = [];
		
		//遍历存储，子服务及其对应的次数
		$$("#childDisplay").find("input[id='childContentId']").each(function(k,v){
			
			var id = $$(this).val();
			var times =  $$(this).parent().find("#item_num").val();	
			
			var aaa = {"boxChildContentId":id,"boxChildContentTimes":times};
			
			childArray.push(aaa);
		});
		
		var contentId = $$("#contentId").val();
		
		
		/**
		 * 结构 ： {服务,子服务:次数。。。}
		 */
		
		var boxStr = {"baseContentId":contentId,"childBoxArray":childArray};
		
		boxArray.push(boxStr);
		
		
		if(localStorage['boxChildContent'] !=null && localStorage['boxChildContent'].length >0){
			
			//先遍历去重复
			var nowArray =  JSON.parse(localStorage['boxChildContent']); 
			for (var i = 0; i < nowArray.length; i++) {
				var nowStr = nowArray[i];
				
				//去重!!!
				for (var j = 0; j < boxArray.length; j++) {
					var boxStr = boxArray[j];
					
					if(nowStr.contentId == boxStr.contentId){
						nowArray.baoremove(i);
					}
				}
			}
			
			localStorage.setItem("boxChildContent",JSON.stringify(boxArray.concat(nowArray)));
		}else{
			localStorage.setItem("boxChildContent",JSON.stringify(boxArray));
		}
		
		myApp.showPreloader('调整成功！');
	    setTimeout(function () {
	        myApp.hidePreloader();
	    }, 500);
		
		mainView.router.loadPage("survey/survey-result.html");
	});
});


//删除数组元素，传递下标 
Array.prototype.baoremove = function(dx) 
{ 
    if(isNaN(dx)||dx>this.length){return false;} 
    this.splice(dx,1); 
} 




//处理 子服务 如  ：空调 4次 。。。
function replaceChildContentBoxNumber(str){
	
	//取得 字符串中的 数字
	var number = str.match(/\d/g).join("");
	
	//替换数字为 可输入的输入框
	str = str.replace(/\d+/g,"<input type='tel' id='finalChildTimes' maxlength='3' class='input-child-content-box-style' value='' placeholder='"+number+"' >")
	
	return str;
}


//加减求和相关
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

//	setTotal();
}

//减号处理
function onSubItemNum(obj) {
	var itemNumObj = obj.find('input[name=item_num]'); 
	var v = itemNumObj.val();
	if (v == undefined || !isNum(v) || v == 0) {
		v = 0;
	} else {
		v = parseInt(v) - 1;
	}
	itemNumObj.val(v);
//	setTotal();	
}


////计算价格总和
//function setTotal() {
//
//	var orderMoney =  itemPrice= Number(0);
//	var serviceAddonId = 0;
//	var serviceAddonIdObj;
//	$$("input[name = item_num]").each(function(key, index) {
//		 
//		 itemNum = $$(this).val();
//		 serviceAddonIdObj = $$(this).parent().find('input[name=service_addon_id]');
//
//		 serviceAddonId = serviceAddonIdObj.val();
//		 
//		 var price = $$(this).nextAll("span[name=item_uint]").text();
//		 
//		 var reg = /[1-9][0-9]*/g;
//		 
//		 itemPrice = price.match(reg);
//		 
//		 
//		 if (itemNum != undefined && isNum(itemNum) && serviceAddonId != undefined) {
//
////			 itemPrice = getServiceAddonPrice(serviceAddonId);
//				 
//			 orderMoney = Number(orderMoney) + Number(itemNum) * Number(itemPrice);
//			 
//		 }
//	
//	});	
//	$$("#order_money_input").attr("value",orderMoney);		
//	$$("#order_moneys_span").val(orderMoney);		
//}




