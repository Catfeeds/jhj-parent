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
			data : [ '总下载量', 'App市场', '线下二维码']
		},
		toolbox : {
			show : true,
			feature : {

				magicType : {
					show : true,
					type : [ 'line', 'bar' ]
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
					name : '总下载量',
					type : 'line',
					data : []
				},
				{
					name : 'App市场',
					type : 'line',
					data : []
				},
				{
					name : '线下二维码',
					type : 'line',
					data : []
				},
				]
	};


	var labelTotal = new Array();
	var labelTotalOnline = new Array();
	var labelTotalOffline = new Array();
	var valueTotal = new Array();
	var valueTotalOnline = new Array();
	var valueTotalOffline = new Array();
	$.ajax({
		url : "/simi-oa/interface-promotion/stat-total.json",
		dataType : "json",
		type : "post",
		success : function(result) {
			var total = result.data.total;

			$.each(total, function(i, p) {
				labelTotal[i] = p['name'];
				valueTotal[i] = p['value'];
			});
			option.xAxis[0].data = labelTotal;
			option.series[0].data = valueTotal;
			console.log(valueTotal);

			var totalOnline = result.data.totalOnline;
			;
			$.each(totalOnline, function(i, p) {
				labelTotalOnline[i] = p['name'];
				valueTotalOnline[i] = p['value'];
			});
			console.log(valueTotalOnline)
			option.xAxis[0].data = labelTotalOnline;
			option.series[1].data = valueTotalOnline;

			var totalOffline = result.data.totalOffline;

			$.each(totalOffline, function(i, p) {
				labelTotalOffline[i] = p['name'];
				valueTotalOffline[i] = p['value'];
			});
			console.log(valueTotalOffline)
			option.xAxis[0].data = labelTotalOffline;
			option.series[2].data = valueTotalOffline;
			console.log(option);
			totalChart.setOption(option);

		}
	});


}
