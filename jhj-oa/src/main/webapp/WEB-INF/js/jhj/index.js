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
        			text:'市场订单图表',
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
//                             boundaryGap : false,
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