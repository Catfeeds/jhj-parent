
function cookDateSelect() {
	//时间 选择 插件。  包含了 赋值 默认值 ，所以要放在 回显 代码 的前面
	var selectedDate, selectedHour;
	var serviceDateValues = [];
	
	var start =0;
	//当前整点小时数
	var todayStr = moment().add(0,'days').format('YYYY-MM-DD');
	var tomorrowStr = moment().add(1,'days').format('YYYY-MM-DD');
	var nowHour = moment().hour();
	
	if (nowHour >= 16) {
		start = 1;
	}	
	
	for (var i = start; i < 14; i++) {
		var tempDay = moment().add(i,'days').format('YYYY-MM-DD');
		serviceDateValues.push(tempDay);
	}	
	
	var serviceHoursValues = [];
	var defaultServiceHoursValues = [];
	
	if(nowHour >= 0 && nowHour < 10){
		serviceHoursValues = [10,11,17,18];
	} 

	if(nowHour >= 10 && nowHour <= 16){
		serviceHoursValues = [17,18];
	}
	
	for (var i =8; i < 20; i++) {
		
		defaultServiceHoursValues = [10,11,17,18];
	}
	
	
	
	var serviceMins = ["00"];
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