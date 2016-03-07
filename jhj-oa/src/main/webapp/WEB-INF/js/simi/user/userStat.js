// 路径配置
require.config({
	paths : {
		echarts : 'http://echarts.baidu.com/build/dist'
	}
});

// 使用
require([ 'echarts',
          'echarts/theme/infographic',
          'echarts/chart/bar',
          'echarts/chart/line'
          
          // 使用柱状图就加载bar模块，按需加载
], drewChart);

function drewChart(ec) {
	drewTotalDownloadsChart(ec);
}

function drewTotalDownloadsChart(ec) {
	// 基于准备好的dom，初始化echarts图表
	var totalChart = ec.init(document.getElementById('totalChart'), "infographic");

	var option = {
		tooltip : {
			trigger : 'axis'
		},
		legend : {
			data : [ '新增人数']
		},
		toolbox : {
			show : true,
			feature : {

				magicType : {
					show : true,
					type : ['bar' ,'line']
				},
				restore : {
					show : true
				},
				saveAsImage : {
					show : true
				}
			}
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			data : []
		} ],
		yAxis : [ {
			type : 'value'
		} ],
		series : [
				{
					names : '新增人数',
					type : 'bar',
					data : []
				},
				]
	};           
	var labelTotal = new Array();
	var valueTotal = new Array();

	$.ajax({
		url : "/"+appName+"/interface-user/userStat-total.json",
		dataType : "json",
		type : "post",
		success : function(result) {
			var total = result.data;

			$.each(total, function(i, p) {
				labelTotal[i] = p['names'];
				valueTotal[i] = p['value'];
			});
			option.xAxis[0].data = labelTotal;
			option.series[0].data = valueTotal;
			totalChart.setOption(option);
		}
	})

}

