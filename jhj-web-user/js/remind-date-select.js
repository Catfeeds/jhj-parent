/*
 * 提醒类  业务 所需 时间选择 插件
 */

function remindDateSelct(){
	

	
	//年
	var serviceYear = [];
	//月-日
	var serviceMon = [];
	//时-分
	var serviceHour = [];
	
	//当前年份
	var nowYear = moment().year();
	
	var today = new Date();
	 
	var pickerInline = myApp.picker({
	    input: '#remindServiceDate',
	    toolbarCloseText:'确认',
	 
	    value: [today.getFullYear(), today.getMonth(), today.getDate(), today.getHours(), (today.getMinutes() < 10 ? '0' + today.getMinutes() : today.getMinutes())],
	 
	    onChange: function (picker, values, displayValues) {
	        var daysInMonth = new Date(picker.value[0], picker.value[1]*1 + 1, 0).getDate();
	        
	        if (values[2] > daysInMonth) {
	            picker.cols[2].setValue(daysInMonth);
	        }
	        
	    },
	 
	    formatValue: function (p, values, displayValues) {
	    	
	    	return values[0] + '-' + displayValues[1] + '-' + values[2] + '  ' + values[3] + ':' + values[4];
	    },
	 
	    cols: [
			//Years
			{
			    values: (function () {
			        var arr = [];
			        for (var i = 0 ; i < 2; i++) { arr.push(nowYear + i); }
			        return arr;
			    })(),
			}, 
	           
	        // Months
	        {
	            values: ('0 1 2 3 4 5 6 7 8 9 10 11').split(' '),
//	            displayValues: ('January February March April May June July August September October November December').split(' '),
	            displayValues:('01 02 03 04 05 06 07 08 09 10 11 12').split(' '),
	            textAlign: 'left'
	        },
	        // Days
	        {
	            values: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31],
	        },
	        
	        // Space divider
	        {
	            divider: true,
	            content: '  '
	        },
	        // Hours
	        {
	            values: (function () {
	                var arr = [];
	                for (var i = 7; i < 20; i++) { arr.push(i); }
	                return arr;
	            })(),
	        },
	        // Divider
	        {
	            divider: true,
	            content: ':'
	        },
	        // Minutes
	        {
	            values: (function () {
	                var arr = [];
	                for (var i = 0; i <= 59; i++) { arr.push(i < 10 ? '0' + i : i); }
	                return arr;
	            })(),
	        }
	    ]
	});                
	
	
}