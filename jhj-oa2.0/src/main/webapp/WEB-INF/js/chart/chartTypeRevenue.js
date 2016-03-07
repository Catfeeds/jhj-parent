
$('.form_datetime').datepicker({
	format: "yyyy-mm-dd",
	language: "zh-CN",
	autoclose: true,
	startView: 1,
	todayBtn:true,
	pickerPosition: "bottom-left"

});


function checkEndTime(){  
    var startTime=$("#startTimeStr").val();  
    var start=new Date(startTime.replace("-", "/").replace("-", "/"));  
    var endTime=$("#endTimeStr").val();  
    var end=new Date(endTime.replace("-", "/").replace("-", "/"));  
    if(end<start){ 
    	
    	alert('结束日期必须大于开始时间');
//    	 BootstrapDialog.alert({
//    			 title:'提示语',
//    			 message:'结束日期必须大于开始时间!'
//    			
//    	 });
    	 return false;  
    }  
    return true;  
} 

function loadOrderChart(legend, xAxis, seriesDatas) {

	// 路径配置
    require.config({
        paths: {
            echarts: 'http://echarts.baidu.com/build/dist',
          /*  'echarts/chart/pie' : 'http://echarts.baidu.com/build/echarts',*/
        }
    });
    
    // 使用
    require(
        [               
            'echarts',
            'echarts/theme/infographic',
            'echarts/chart/line',
            'echarts/chart/bar'
        ],
        function (ec) {
            // 基于准备好的dom，初始化echarts图表
            var myChart = ec.init(document.getElementById('main')); 
            
            var option = {
            	noDataLoadingOption : {
            		text : "此时间段无统计数据.",
            		effect: "bar"
            	},
        		title : {
        			text:'品类收入图表',
        			x:'left',
        			y:'top'
        		},
        		tooltip : {
        	        trigger: 'axis'
        	    },
                legend: {
                    data:legend
                },
                
                
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: false, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                
                calculable : true,
                
                xAxis : [
                         {
                             type : 'category',
                             boundaryGap : false,
                             data : xAxis
                         }
                    ],
                    
                yAxis : [
                     {
                         type : 'value',
                         
                     }
                 ],    
                series : seriesDatas

            };
    
            // 为echarts对象加载数据 
            myChart.setOption(option); 
        }
    );
}

//导出Excel

$("#exportExcel").click(function(){
	console.log("table2excel exportExcel");
	$("#table2excel").table2excel({
		exclude: ".noExl",
		name: "品类收入图表",
		filename: "品类收入图表"
	});
 });
