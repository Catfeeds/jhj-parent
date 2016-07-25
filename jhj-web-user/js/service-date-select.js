
function serviceDateSelect() {
	//时间 选择 插件。  包含了 赋值 默认值 ，所以要放在 回显 代码 的前面
	var selectedDate, selectedHour;
	var serviceDateValues = [];
	
	var start =0;
	//当前整点小时数
	var todayStr = moment().add(0,'days').format('YYYY-MM-DD');
	var tomorrowStr = moment().add(1,'days').format('YYYY-MM-DD');
	var nowHour=null;
	if(moment().minutes()>=30){
		nowHour= moment().hour()+1;
	}else{
		nowHour= moment().hour();
	}
	
//	var nowHour = 12;
	if (nowHour >= 18) {
		start = 1;
	}	
	
	for (var i = start; i <= 14; i++) {
		var tempDay = moment().add(i,'days').format('YYYY-MM-DD');
		serviceDateValues.push(tempDay);
	}	
	
	var serviceHoursValues = [];
	var defaultServiceHoursValues = [];
	// 可选 小时 的  最大范围 （考虑 时长）， 为  8 ~ 18
	for (var i =8; i <= 18; i++) {
		var tempHour = moment({ hour:i}).format('HH');
		defaultServiceHoursValues.push(tempHour);
	}
	
	if(nowHour >= 0 && nowHour <= 8){
		serviceHoursValues = [09,10,11,12,13,14,15,16,17,18];
	} 

	if(nowHour > 8 && nowHour <= 18){
		for (i = nowHour + 1; i <= 18; i++) {
			var tempHour = moment({ hour:i}).format('HH');
			serviceHoursValues.push(tempHour);
		}
	}
	
	if(nowHour > 18 && nowHour <=23){
		serviceHoursValues = [8,9,10,11,12,13,14,15,16,17,18]; 
	}
	
	var serviceMins = ["00", "30"];
	var wheel = [[
	               {
	            	   label: '日期',
                       values: serviceDateValues,
                       width : 160
	               },
	               {
	            	   label: '小时',
                       values: serviceHoursValues,
                       width : 80
	               },
	               {
	            	   label: '分钟',
                       values: serviceMins,
                       width : 30
	               }
	           ]];
	var defaultValue = serviceDateValues[0] + "  " + serviceHoursValues[0] + ":"  + serviceMins[0];
	$$("#serviceDateSelect").html(defaultValue);
	$$("#serviceDate").val(defaultValue);
	
	$('#serviceDateSelect').mobiscroll({
		theme : "ios", 
		mode : "mixed", // Specify scroller mode like: mode: 'mixed' or omit setting to use default 
		display : "bottom", // Specify display mode like: display: 'bottom' or omit setting to use default 
		lang : "zh",	
		closeOnOverlay : true,
//		readonly : true,		
		tap : true,
		wheels: wheel,
               
       formatResult: function (data) {
    	    return data[0] + ' ' + data[1] + ':' + data[2];
    	},
    	
       	validate: function (html, index, time, dir, inst) {
    		selectedDate = inst._tempWheelArray[0];
    		if (index == 0 ) {
    			if  (todayStr == selectedDate) {
    				wheel[0][1].values = serviceHoursValues;
    			} else if (tomorrowStr == selectedDate && nowHour > 19 && nowHour <=23) {
    				wheel[0][1].values = serviceHoursValues;
    			} else {
    				wheel[0][1].values = defaultServiceHoursValues;
    			}
    			
    			if (wheel[0][1].values != inst._tempWheelArray[1]) {
    				inst.settings.wheels = wheel;
            		inst.changeWheel([1]);
    			}
    		}
    	},
    	
    	onSelect : function (valueText, inst) {
    		$$("#serviceDateSelect").html(valueText);
    		$$("#serviceDate").val(valueText);
		}
	});
}