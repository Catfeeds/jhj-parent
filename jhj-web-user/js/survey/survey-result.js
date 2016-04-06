myApp.onPageInit('survey-result-page', function(page) {
	
	//对于 完成后  又返回的 情况~
	if(localStorage['survey_user_id'] == undefined){
		
		myApp.alert("请您发起新的定制服务");
		
		mainView.router.loadPage("survey/survey-user.html");
		return false;
	}
	
		/**
		 *  请求成功后,	
		 *  采用virtual list 替换 题目及选项模板
		 */
		var loadResultSuccess = function(datas, textStatus, jqXHR){
			
			var result = JSON.parse(datas.response);
			
			/**
			 * 将服务分为 基础服务、推荐服务、免费服务
			 */
			
			var contentFlag = result.data;
			
			//基础服务（根据答题结果确定）	
			var baseContent = contentFlag[0];
			//推荐服务
			var recommendContent = contentFlag[1];
			//免费服务
			var freeContent = contentFlag[2];
			
			/*
			 * 分析 决定。使用 两套模板,添加 不同分类的 服务
			 * 	
			 *    基础服务 和 推荐服务 格式相同	 
			 * 	  免费服务
			 * 
			 *  流程：  1   使用模板填充数据，
			 *  	 2   用临时变量，拼接 每个填充数据的 模板， 
			 *       3  将填充完的临时变量,加入进 展示区域
			 */
			
			//子服务是单选的模板
			var radioChildTemplateHtml = $$("#childRadioUl").html();
			
			//子服务是多选的模板
			var checkboxChildTemplateHtml = $$("#childCheckboxUl").html();
			
			//子服务是单选的 临时变量
			var radioChildHtml = "";
			//子服务是多选的临时变量
			var checkboxChildHtml = "";
			
			/*
			 * 2016年3月17日16:11:29   点击多选后，由子页面 回到 当前页面时，回显 之前的 修改 结果
			 */
			var storeBase  = localStorage['storeBaseArray'];
			
			var storeRadio = localStorage['storeRadioArray'];
			
			if(storeBase != undefined){
				
				var baseArray =  JSON.parse(storeBase);
				
				//回显 无服务的  服务的  之前选中的 次数
				for (var j=0; j< baseArray.length; j++ ) {
					
					var baseItem = baseArray[j];
					
					for (var i = 0; i < baseContent.length; i++) {
						
						var paramBaseItem = baseContent[i];
						
						if(baseItem.baseContentId == paramBaseItem.content_id){
							
							if(paramBaseItem.base_content_real_time != 0){
								paramBaseItem.base_content_real_time = baseItem.baseContentTimes;
							}else{
								paramBaseItem.default_time = baseItem.baseContentTimes;
							}
							
						}
					}
					
					for(var i = 0; i < recommendContent.length; i++){
						
						var paramRecmmendItem = recommendContent[i];
						
						if(baseItem.baseContentId == paramRecmmendItem.content_id){
							
							if(paramRecmmendItem.base_content_real_time != 0){
								paramRecmmendItem.base_content_real_time = baseItem.baseContentTimes;
							}else{
								paramRecmmendItem.default_time = baseItem.baseContentTimes;
							}
							
						}
					}
					
				}
			}
			
			
			if(storeRadio != undefined){
				
				var radioArray =  JSON.parse(storeRadio);
				
				//回显 无服务的  服务的  之前选中的 次数
				for (var j= 0; j< radioArray.length; j++) {
					
					var radioItem = radioArray[j];
					
					for (var i = 0; i < baseContent.length; i++) {
						
						var paramBaseItem = baseContent[i];
						
						if(radioItem.baseContentId == paramBaseItem.content_id){
							
							paramBaseItem.child_list[0].default_time_child = 
								
								radioItem.childRadioArray[0].childRadioContentTimes;
						}
					}
					
					for(var i = 0; i < recommendContent.length; i++){
						
						var paramRecmmendItem = recommendContent[i];
						
						if(radioItem.baseContentId == paramRecmmendItem.content_id){
							
							paramRecmmendItem.child_list[0].default_time_child = 
								
								radioItem.childRadioArray[0].childRadioContentTimes;
						}
					}
					
				}
				
			}
			
			
			
			/*
			 * 1.基础服务
			 */
			//基础服务 临时变量
			var baseContentHtml = "";
			//基础服务 主体 模板对象
			var baseTemplateHtml = $$("ul[id='baseContentTemplate']").html();
			
			for (var i = 0; i < baseContent.length; i++) {
				var baseI = baseContent[i];
				
				var part = baseTemplateHtml;
				
				//由选项确定的服务次数, 如果为0, 则不展示，不为0，展示
				var optionDecideTime = baseI.base_content_real_time;
				
				//次数 展示  文字 “次/年。。”
				var timeStr = generateTimeWordForTimes(baseI.measurement);
				
				//单选类型,默认 需要提供  默认选中的id
				if(baseI.content_child_type == 1){
					
					//单选的 第一个选项,设置为默认选中,其他的 无所谓
					var defaultRadioId = baseI.child_list[0].id;
					
					//第一个 选项的id 为默认选中的  单选次数
					part = part.replace(new RegExp('{childRadioContentId}',"gm"), defaultRadioId);
					
					var defaultTimes = baseI.child_list[0].default_time_child;
					
					//默认次数
					part = part.replace(new RegExp('{defaultTimes}',"gm"), defaultTimes+timeStr);
				}
				
				//多选题,展示为1次
				if(baseI.content_child_type == 2){
					part = part.replace(new RegExp('{defaultTimes}',"gm"), "1"+timeStr);
				}
				
				//如果无子服务，展示默认次数
				if(baseI.content_child_type == 0){
					
					if(optionDecideTime == 0){
						part = part.replace(new RegExp('{defaultTimes}',"gm"), baseI.default_time+timeStr);	
					}else{
						part = part.replace(new RegExp('{defaultTimes}',"gm"), optionDecideTime+timeStr);	
					}
				}
				
				part = part.replace(new RegExp('{contentName}',"gm"), baseI.name);
				part = part.replace(new RegExp('{price}',"gm"), baseI.price);
				part = part.replace(new RegExp('{contentId}',"gm"), baseI.content_id);
				part = part.replace(new RegExp('{contentChildType}',"gm"), baseI.content_child_type);
				
 				baseContentHtml += part;
			}
			
			$$("#baseDisplay").append(baseContentHtml);
			
			/*
			 * 2. 推荐服务
			 */
			var recommendHtml = "";
			var recommendTemplateHtml = $$("ul[id='baseContentTemplate']").html();
			
			for (var i = 0; i < recommendContent.length; i++) {
				var recommendI = recommendContent[i];
				
				var part = recommendTemplateHtml;
				
				//次数 展示
				var timeStr = generateTimeWordForTimes(recommendI.measurement);
				
				//单选类型,默认 需要提供  默认选中的id
				if(recommendI.content_child_type == 1){
					
					//单选的 第一个选项,设置为默认选中,其他的 无所谓
					var defaultRadioId = recommendI.child_list[0].id;
					
					//第一个 选项的id 为默认选中的  单选次数
					part = part.replace(new RegExp('{childRadioContentId}',"gm"), defaultRadioId);
					
					var defaultTimes = recommendI.child_list[0].default_time_child;
					
					//默认次数
					part = part.replace(new RegExp('{defaultTimes}',"gm"), defaultTimes+timeStr);
				}
				
				if(recommendI.content_child_type == 2){
					part = part.replace(new RegExp('{defaultTimes}',"gm"), "1"+timeStr);
				}
				
				if(recommendI.content_child_type == 0){
					
					//2016-1-13 18:16:46 推荐服务中 可能存在， 次数由 选项决定的 服务，而该服务又未被选中，则默认展示为1
					if(recommendI.default_time == 0){
						part = part.replace(new RegExp('{defaultTimes}',"gm"), "1"+timeStr);
					}else{
						part = part.replace(new RegExp('{defaultTimes}',"gm"), recommendI.default_time+timeStr);
					}
					
				}
				
				part = part.replace(new RegExp('{contentName}',"gm"), recommendI.name);
				part = part.replace(new RegExp('{price}',"gm"), recommendI.price);
				part = part.replace(new RegExp('{contentId}',"gm"), recommendI.content_id);
				part = part.replace(new RegExp('{contentChildType}',"gm"), recommendI.content_child_type);
				
				recommendHtml += part;
			}
			
			$$("#recommendDisplay").append(recommendHtml);
			
			/*
			 * 3.免费服务
			 */
			var freeHtml = "";
			var freeTemplateHtml = $$("ul[id='freeContentTemplate']").html();
			
			for (var i = 0; i < freeContent.length; i++) {
				var freeI = freeContent[i];
				
				var part = freeTemplateHtml;
				
				part = part.replace(new RegExp('{contentName}',"gm"), freeI.name);
				
				freeHtml += part;
			}
			
			$$("#freeDisplay").append(freeHtml);
			
			//数据加载完,打开一个 折叠布局
			myApp.accordionOpen("#baseAccordionItem");
			
			return false;
		};
		
		//进入页面加载,答题结果
		var postData = {};
		
		postData.survey_user_id = localStorage['survey_user_id'];
		
		postData.survey_result = localStorage['surveyResult'];
		
		
		//第一次加载页面时,加载第一题
		$$.ajax({
			type: "post",
			 url: siteAPIPath + "survey/survey_result.json",
			data: postData,
			statusCode: {
	         	200: loadResultSuccess,
	 	    	400: ajaxError,
	 	    	500: ajaxError
	 	    }
		});
		
		
	
		/**
		 * 点击提交 订制结果
		 */
		
		$$("#submitResult").on("click",function(){
			
			$$(this).attr("disabled",true);
			
			storeNowData();
			
			/*
			 * 构造 参数
			 */
			var postData ={};
			
			//无子服务
			postData.base_content_array = localStorage['storeBaseArray'];
			//单选
			postData.radio_content_array = localStorage['storeRadioArray'];
			
			//多选
			if(localStorage['boxChildContent'] != undefined ){
				postData.box_content_array = localStorage['boxChildContent'];
				postData.default_box_content_array = "[]";
			}else{
				/*
				 * 多选的 默认值, 该多选对应的 主 服务 id
				 */
				postData.default_box_content_array = JSON.stringify(childBoxArray);
				
				postData.box_content_array = "[]";
			}
			
			//用户 id
			postData.user_id = localStorage['survey_user_id'];
			
			//对于 完成  又返回的 情况~
			if(localStorage['survey_user_id'] == undefined){
				
				myApp.alert("请勿重复提交");
				mainView.router.loadPage("index.html");
				return false;
			}
			
			
			//发请求
			$$.ajax({
				type: "post",
				 url: siteAPIPath + "survey/submit_survey_result.json",
				data: postData,
				success:function (datas,sta,xhr){
					
					var result = JSON.parse(datas);
					
					if(result.status == "999"){
						myApp.alert(result.msg);
						return false;
					}
					
					
					$$("#submitResult").removeAttr('disabled');  
					
					mainView.router.loadPage("survey/survey-result-price.html");
				},
				error:function(){
					myApp.alert("网络错误");
				}
			});	
		});
		
		
		
		/*
		 * 展开、折叠效果，给出文字提示
		 */
		$$('.accordion-item').on('open', function () {
			  
			changeAccordText(this);
		}); 
			 
		$$('.accordion-item').on('close', function (e) {
			  
			changeAccordText(this);
		}); 	
		
		
		function changeAccordText(obj){
			
			 var accordText =  $$(obj).find("font").text();
			  
			  if(accordText =="展开"){
				  $$(obj).find("font").text("收起");
			  }else{
				  $$(obj).find("font").text("展开");
			  }
		}
		
});

function storeNowData(){
	
	//展示区域 的所有 被选中的  服务	
	var aaObj =   $$("#resultDiv").find("a[id='contentName'][class='button button-fill color-green']");
	
	//存放  无子服务  类型的 {内容：次数}array
	var baseContentArray = [];
	
	//存放 子服务 为 单选的 array {服务,子服务：次数}
	var radioContentArray = [];
	
	//对于多选,如果未修改,但是也选中的话,需要传递  该 服务内容Id，并在后台处理  该服务的 子服务
	var childBoxArray = [];
	
	
	$$(aaObj).each(function(k,v){
		
		//‘调整次数  ’按钮  所在 div,包含隐藏域信息
		var contentDiv = $$(this).parent().parent().find("#contentDiv");
		
		// 次数 所在 div
		var timesDiv =$$(this).parent().parent().find("#timesDiv");
		
		//次数数字 每行 对应的数字
		var times = timesDiv.find("#contentTimes").text().match(/\d/g).join("");
		
		var childType =  contentDiv.find("#contentChildType").val();
		
		/*
		 * 主服务id
		 */
		var baseContentId = contentDiv.find("#contentId").val();
		
		//没有子服务的 类型
		if(childType == 0){
			
			baseContentArray.push({"baseContentId":baseContentId,"baseContentTimes":Number(times)});
		}
		
		//单选类型
		if(childType == 1){
			
			var childRadioContentId =  timesDiv.find("input[id='childRadioContentId']").val();
			
			//存放 单选子服务的 {子服务：次数}
			var childRadioArray = [];
			
			childRadioArray.push({"childRadioContentId":childRadioContentId,"childRadioContentTimes":Number(times)});
			
			radioContentArray.push({"baseContentId":baseContentId,"childRadioArray":childRadioArray});
		}
		
		//多选类型, 在 新页面 已经存储
		if(childType == 2){
			childBoxArray.push({"baseContentId":baseContentId});
		}
		
	});
	
	localStorage.setItem("storeBaseArray",JSON.stringify(baseContentArray));
	
	localStorage.setItem("storeRadioArray",JSON.stringify(radioContentArray));
}



/*
 * 给服务次数,添加期限  --> "次/月"、"次/年"
 * 	
 * 	将 量词字段 转换为 string "次/月"
 */
function generateTimeWordForTimes(obj){
	
	var str = "";
	
	if(obj == 0){
		str = "次/月";
	}
	if(obj == 1){
		str = "次/年"
	}
	if(obj == 2 || obj== 3){
		str = "次";
	}
	
	return str;
}



/*
 * 点击 服务名称 按钮 变色
 */
function changeColor(obj){
	
	var color = $$(obj).attr("class");
	
	if(color.indexOf("color-green")>0){
		$$(obj).attr("class","button button-fill color-gray");
		
		//同时禁用调整次数按钮
		
		var  aButtonObj = $$(obj).parent().parent().find("a[id='changeThisTimes']");
		
		aButtonObj.attr("class","button button-fill color-gray");
		aButtonObj.attr("disabled",true);
		
	}
	
	if(color.indexOf("color-gray")>0){
		$$(obj).attr("class","button button-fill color-green");
		//打开调整次数按钮
		
		var  aButtonObj = $$(obj).parent().parent().find("a[id='changeThisTimes']");
		aButtonObj.attr("class","button button-fill color-orange");
		aButtonObj.removeAttr('disabled');
		
	}
	
}



/*
 * 点击调整次数, 去后台获取该 服务的 子服务 
 */
function changeThisTimes(obj){
	
	//子服务类型
	var childType =  $$(obj).parent().find("#contentChildType").val();
	
	//当前服务  id
	var contentId =  $$(obj).parent().find("#contentId").val();
	
	/**
	 *	经验证, 在弹窗时，可以获得  "单选"时的值
	 *			
	 *		故: 多选题，跳转到新页面进行 + -修改次数操作
	 *		   
	 *		单选和 无子服务的 弹窗修改
	 */
	
	//如果是多选，则跳转页面进行修改次数
	if(childType == 2){
		
		/*
		 *  多选题进入新页面之前，存储 当前页面的 调整结果，返回时 回显
		 */
		storeNowData();
		
		localStorage.setItem("changeContentId",contentId);
		
		var successUrl = "survey/survey-result-child.html";
		
		mainView.router.loadPage(successUrl);
	}
	
	//没有子服务,不发请求
	if(childType == 0){
		
		myApp.prompt('', '次数修改',
		      function (value) {
				 	
					if(!isPositiveNum(value) || Number(value) > 400 || value.indexOf(0) == 0){
						myApp.alert("请输入小于400的整数数字");
						return false;
					}
					
					//次数所在的span对象
					var numberSpanObj = $$(obj).parent().parent().parent().find("span[id='contentTimes']");
					
					//替换显示的内容
					numberSpanObj.text(numberSpanObj.text().replace(/\d+/g,value));
		      });
		return false;	
	}
	
	//当前服务  id
	var contentId =  $$(obj).parent().find("#contentId").val();
	
	var postData = {};
	
	postData.content_id = contentId;
	
	$$.ajax({
		type: "get",
		 url: siteAPIPath + "survey/get_child_content_list.json",
		data: postData,
		success:function (datas,sta,xhr){
			
			var result = JSON.parse(datas);
			
			var reLength = result.data.length;
				
			var childHtml = "";
			
			//如果当前是单选题
			if(childType == 1){
				for (var i = 0; i < reLength; i++) {
					var child = result.data[i];
					
					//设置  默认次数的 选中
					if(child.id == $$(obj).parent().parent().parent().find("#timesDiv").find("#childRadioContentId").val()){
						childHtml += "<li>"
								+	"<input type='hidden' id='childRadioId' value="+child.default_time_child+">"
								+	"<input type='radio' checked name='childRadio' value="+child.id+">"
								+ 	child.option_str	
								+ "</li>" 		
					}else{
						
						childHtml += "<li>"
							+	"<input type='hidden' id='childRadioId' value="+child.default_time_child+">"
							+	"<input type='radio'  name='childRadio' value="+child.id+">"
							+ 	child.option_str	
							+ "</li>"
					}
				}
			}
			
			
			$$("#childReplace").html("");
			$$("#childReplace").append(childHtml);
			
			if(childType != 2){
				
				myApp.confirm($$("#childReplace").html(),'<b>服务次数调整</b>',function(){
					
					/*
					 * 因为每次 点击时，加载的子服务 都会与 当前 服务 对应。（有清空操作）所以无需担心，会有 数据累积的情况
					 */
					
					//如果是单选题,存储 （内容id、子服务id）
					if(childType == 1){
						
						//选中的子服务的次数 --对应的 子服务 id
						var radioChildContentId = $$("input[type='radio'][name='childRadio']:checked").val();
						
						//将该id 赋值给隐藏域,供提交
						$$(obj).parent().parent().parent().find("#timesDiv").find("#childRadioContentId").val(radioChildContentId);
						
						//选中的 子服务的次数 内容，替换默认值
						var radioChildContentNumber = $$("input[type='radio'][name='childRadio']:checked")
												.parent().find("input[id='childRadioId']").val();
						
						//次数所在的 span 对象
						var numberObj = $$(obj).parent().parent().parent().find("span[id='contentTimes']");
						
						//选中后,替换默认次数
						numberObj.text(numberObj.text().replace(/\d+/g,radioChildContentNumber));
						
					}	
				});
			}
		},
		error:function(){
			myApp.alert("网络错误");
		}
	});
};

//判断是否是正整数
function isPositiveNum(s){//是否为正整数 
	var re = /^[0-9]*[1-9][0-9]*$/ ; 
	return re.test(s); 
} 


//处理 子服务 如  ：空调 4次 。。。
function replaceChildContentBoxNumber(str){
	
	//取得 字符串中的 数字
	var number = str.match(/\d/g).join("");
	
	//替换数字为 可输入的输入框
	str = str.replace(/\d+/g,"<input type='tel' id='finalChildTimes' maxlength='3' class='input-child-content-box-style' value='' placeholder='"+number+"' >")
	
	return str;
}




