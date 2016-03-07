// 路径配置
        require.config({
            paths: {
                echarts: 'http://echarts.baidu.com/build/dist'
            }
        });
        
        // 使用
        require(
            [               
                'echarts',
                'echarts/theme/infographic',
                'echarts/chart/bar',// 使用柱状图就加载bar模块，按需加载
                'echarts/chart/line'           
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('main')); 
                
                var option = {
                		title : {
                			text:'本季度订单收入变化',
                					
                		},
                    tooltip: {
                        show: true
                    },
                    legend: {
                        data:['增长率(%)','微网站来源','App来源','新增用户小计','退单数','退单率(%)']
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true},
                            dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            restore : {show: true},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis : [
                        {
                            type : 'category',
                            data : ['七月','八月','九月']
                        }
                    ],
                    yAxis : [
                        {
                            type : 'value',
                            date : [500, 1000, 1500, 2000]
                        }
                    ],
                    series : [
                        {
                        	name:'增长率(%)',
            			    type:'line',
            			    data:[20,200,700],
                        },
                        {
            	            name:'微网站来源',
            	            type:'line',
            	            data:[782, 1040, 1600],
                        },
                        {
            	            name:'营业额(元)',
            	            type:'line',
            	            data:[564, 873, 1790],
                        },
                        {
                        	name:'App来源',
             	            type:'line',
             	            data:[100,1009,1800],	                        	
                        },
                        {
                        	name:'营业额小计(元)',
            	            type:'line',
            	            data:[250,2049,2406],	                        	
                       }
                    ]
                };
        
                // 为echarts对象加载数据 
                myChart.setOption(option); 
            }
        );