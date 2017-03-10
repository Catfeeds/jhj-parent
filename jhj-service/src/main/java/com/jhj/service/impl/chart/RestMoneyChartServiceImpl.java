package com.jhj.service.impl.chart;

import com.alibaba.fastjson.JSON;
import com.jhj.po.dao.order.OrderCardsMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.dao.user.UsersMapper;
import com.jhj.service.chart.RestMoneyChartService;
import com.jhj.vo.chart.ChartDataVo;
import com.jhj.vo.chart.ChartMapVo;
import com.jhj.vo.chart.ChartSearchVo;
import com.meijia.utils.ChartUtil;
import com.meijia.utils.DateUtil;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.MathDoubleUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class RestMoneyChartServiceImpl implements RestMoneyChartService {

    @Autowired
    private OrdersMapper orderMapper;

    @Autowired
    private OrderCardsMapper orderCardsMapper;

    @Autowired
    private UsersMapper usersMapper;

    /**
     * 充值卡销售图表
     */
    @Override
    public ChartDataVo statChartDatas(ChartSearchVo chartSearchVo,List<String> timeSeries) {

        ChartDataVo chartDataVo = new ChartDataVo();

        if (timeSeries.isEmpty())
            return chartDataVo;

        String statType = chartSearchVo.getStatType();

        // 1. table 列名
        String[] legendAllArrys = {"五百面值","五百面值占比","一千面值","一千面值占比","两千面值","两千面值占比","三千面值","三千面值占比","营业额小计"};
        List<String> legendAll = Arrays.asList(legendAllArrys);

        // 2. 统计图 图例
        String[] legendArrys = {"五百面值","一千面值","两千面值","三千面值"};
        List<String> legend = Arrays.asList(legendArrys);
        chartDataVo.setLegend(JSON.toJSONString(legend));

        // 3.x轴
        List<String> xAxis = new ArrayList<String>();
        for (int i = 1; i < timeSeries.size(); i++) {
            xAxis.add(ChartUtil.getTimeSeriesName(statType, timeSeries.get(i)));
        }
        chartDataVo.setxAxis(JSON.toJSONString(xAxis));

        // 4.填充table
        List<HashMap<String, String>> tableDatas = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> tableData = null;
        for (int i = 0; i < timeSeries.size(); i++) {
            tableData = new HashMap<String, String>();
            tableData.put("series", timeSeries.get(i));
            for (int j = 0; j < legendAll.size(); j++) {
                tableData.put(legendAll.get(j), "0");
            }
            tableDatas.add(tableData);
        }

        // 4-1.查询SQL获得统计数据 -- 各类面值总钱数
        List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();

        if (chartSearchVo.getStatType().equals("day")) {
            chartSearchVo.setFormatParam("%Y-%m-%e");
            statDatas = orderCardsMapper.saleCardByMonth(chartSearchVo);
        }

        if (chartSearchVo.getStatType().equals("month")) {
            chartSearchVo.setFormatParam("%Y-%m");
            statDatas = orderCardsMapper.saleCardByMonth(chartSearchVo);
        }

        if (chartSearchVo.getStatType().equals("quarter")) {
            statDatas = orderCardsMapper.saleCardByQuarter(chartSearchVo);
        }

        // 4-2.真实数据填充（table表格），计算每行总计，以及各品类占比

        for (HashMap<String, String> tableDataItem : tableDatas) {
        	BigDecimal moneySum = new BigDecimal(0);
        	BigDecimal oneMoney = new BigDecimal(0);
        	BigDecimal twopMoney = new BigDecimal(0);
        	BigDecimal threeMoney = new BigDecimal(0);
        	BigDecimal fiveMoney = new BigDecimal(0);
        	BigDecimal money = new BigDecimal(0);
        	String str2 =tableDataItem.get("series");
        	for (ChartMapVo chartSqlData : statDatas) {
        		String str3 = chartSqlData.getSeries();
            	if(statType.equals("day")){
					if(DateUtil.compareDateStr(str3,str2)==0){
						//1=500 2 = 1000面值 3 = 2000面值 4 =3000面值
						this.setTableDateItem(chartSqlData, money, tableDataItem);
					}
            		
            	}else{
					if (str2.equals(str3)) {
						this.setTableDateItem(chartSqlData, money, tableDataItem);
					}
            	}
            }
        	// 计算百分比
        	fiveMoney = new BigDecimal(tableDataItem.get("五百面值"));
        	oneMoney = new BigDecimal(tableDataItem.get("一千面值"));
        	twopMoney = new BigDecimal(tableDataItem.get("两千面值"));
        	threeMoney = new BigDecimal(tableDataItem.get("三千面值"));
        	moneySum = fiveMoney.add(oneMoney).add(twopMoney).add(threeMoney);
        	tableDataItem.put("营业额小计", moneySum.toString());
        	if (moneySum.intValue() > 0) {
        		tableDataItem.put("五百面值占比",MathDoubleUtil.getPercent(fiveMoney.intValue(),moneySum.intValue()));
        		tableDataItem.put("一千面值占比",MathDoubleUtil.getPercent(oneMoney.intValue(),moneySum.intValue()));
        		tableDataItem.put("两千面值占比",MathDoubleUtil.getPercent(twopMoney.intValue(),moneySum.intValue()));
        		tableDataItem.put("三千面值占比",MathDoubleUtil.getPercent(threeMoney.intValue(),moneySum.intValue()));
        	} else {
        		tableDataItem.put("五百面值占比", "0.00%");
        		tableDataItem.put("一千面值占比", "0.00%");
        		tableDataItem.put("两千面值占比", "0.00%");
        		tableDataItem.put("三千面值占比", "0.00%");
        	}
        }

        // 5. 去掉第一个tableDataItem;
        if (tableDatas.size() > 0)
            tableDatas.remove(0);

        // 6. 根据表格的数据生成图表的数据.
        // 初始化图表数据格式
        this.generateCount(legend, timeSeries, tableDatas, statType, chartDataVo);
        return chartDataVo;
    }
    

    /**
     * 用户余额图表
     * 
     * @param chartSearchVo  查询对象
     * @param timeSeries  查询时间范围
     * 
     */
    @Override
    public ChartDataVo userRestMoneyDatas(ChartSearchVo chartSearchVo,List<String> timeSeries) {

        ChartDataVo chartDataVo = new ChartDataVo();

        if (timeSeries.isEmpty())
            return chartDataVo;

        String statType = chartSearchVo.getStatType();

        // 1. table 列名
        String[] legendAllArrys = {"余额用户","余额总金额","余额<200","余额<200占比","余额200~1k","余额200~1k占比","1K~3K","1K~3K占比","余额>3k","余额>3k占比"};
        List<String> legendAll = Arrays.asList(legendAllArrys);

        // 2. 统计图 图例
        String[] legendArrys = {"余额用户","余额<200","余额200~1k","1K~3K","余额>3k"};
        List<String> legend = Arrays.asList(legendArrys);
       
        chartDataVo.setLegend(JSON.toJSONString(legend));

        // 3.x轴
        List<String> xAxis = new ArrayList<String>();
        for (int i = 1; i < timeSeries.size(); i++) {
            xAxis.add(ChartUtil.getTimeSeriesName(statType, timeSeries.get(i)));
        }
        chartDataVo.setxAxis(JSON.toJSONString(xAxis));

        // 4.填充table
        List<HashMap<String, String>> tableDatas = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> tableData = null;
        for (int i = 0; i < timeSeries.size(); i++) {
            tableData = new HashMap<String, String>();
            tableData.put("series", timeSeries.get(i));

            for (int j = 0; j < legendAll.size(); j++) {
                tableData.put(legendAll.get(j), "0");
            }
            tableDatas.add(tableData);
        }

		/*
         * 关于 余额用户数、 余额总金额 二者是需要累加统计的 如 8月 表示 8月之前 的所有~~~~ 10月 表示 10月之前的 所有~~~~
		 * 
		 * 不同于其他列展示的 当月（当前时间）对应的 数值。
		 * 
		 * 分析：用相同的 时间条件，可单独写sql,处理.
		 */

        // 4-1.查询SQL获得统计数据 -- 用户余额人数count(*)
        List<ChartMapVo> statDatas = new ArrayList<ChartMapVo>();

        if (chartSearchVo.getStatType().equals("day")) {
        	chartSearchVo.setFormatParam("%Y-%m-%e");
            statDatas = usersMapper.userResyMoneyByDay(chartSearchVo);
        }

        if (chartSearchVo.getStatType().equals("month")) {
        	chartSearchVo.setFormatParam("%Y-%m");
            statDatas = usersMapper.userResyMoneyByMonth(chartSearchVo);
        }

        if (chartSearchVo.getStatType().equals("quarter")) {
            statDatas = usersMapper.userResyMoneyByQuarter(chartSearchVo);
        }

        // 循环统计数据，完成表格数据替换
        // 1.先实现表格数据的替换. 用户有余额总人数

        for (HashMap<String, String> tableDataItem : tableDatas) {
            // 处理表格形式的数据.
        	int total =0;
        	for (ChartMapVo chartSqlData : statDatas) {
                String str = tableDataItem.get("series");
                String series = chartSqlData.getSeries();
                if(chartSearchVo.getStatType().equals("day")){
                	if(DateUtil.compareDateStr(series,str)==0){
                		total = total + chartSqlData.getTotal();
					}
                }else{
                	if (str.equals(series)) {
                		total = total + chartSqlData.getTotal();
                	}
                }
            }
        	tableDataItem.put("余额用户",String.valueOf(total));
        }

        // 用户有余额总金额
        statDatas = new ArrayList<ChartMapVo>();

        if (chartSearchVo.getStatType().equals("day")) {
        	chartSearchVo.setFormatParam("%Y-%m-%e");
            statDatas = usersMapper.selectUserAllResyMoneyByDay(chartSearchVo);
        }

        if (chartSearchVo.getStatType().equals("month")) {
        	chartSearchVo.setFormatParam("%Y-%m");
            statDatas = usersMapper.selectUserAllResyMoneyByMonth(chartSearchVo);
        }

        if (chartSearchVo.getStatType().equals("quarter")) {
            statDatas = usersMapper.selectUserAllResyMoneyByQuarter(chartSearchVo);
        }

        for (HashMap<String, String> tableDataItem : tableDatas) {
        	BigDecimal allRestMoney = new BigDecimal(0);
        	for (ChartMapVo chartSqlData : statDatas) {
                String str = tableDataItem.get("series");
                String series = chartSqlData.getSeries();
                if(chartSearchVo.getStatType().equals("day")){
                	if(DateUtil.compareDateStr(series,str)==0){
                		 allRestMoney = chartSqlData.getTotalMoney();
					}
                }else{
                	if (str.equals(series)) {
                		 allRestMoney = chartSqlData.getTotalMoney();
                	}
                }
            }
        	tableDataItem.put("余额总金额", MathBigDecimalUtil.round2(allRestMoney));
        }
        // 4-2.查询SQL获得统计数据 -- 0<余额<200的 ，用户人数count(*)
        statDatas = new ArrayList<ChartMapVo>();

        if (chartSearchVo.getStatType().equals("day")) {
        	chartSearchVo.setFormatParam("%Y-%m-%e");
            statDatas = usersMapper.userResyMoneyLessTwoByDay(chartSearchVo);
        }

        if (chartSearchVo.getStatType().equals("month")) {
        	chartSearchVo.setFormatParam("%Y-%m");
            statDatas = usersMapper.userResyMoneyLessTwoByMonth(chartSearchVo);
        }

        if (chartSearchVo.getStatType().equals("quarter")) {
            statDatas = usersMapper.userResyMoneyLessTwoByQuarter(chartSearchVo);
        }
        for (HashMap<String, String> tableDataItem : tableDatas) {
        	BigDecimal total = new BigDecimal(0);
        	for (ChartMapVo chartSqlData : statDatas) {
            // 处理表格形式的数据.
                String str = tableDataItem.get("series");
                String series = chartSqlData.getSeries();
                if(chartSearchVo.getStatType().equals("day")){
                	if(DateUtil.compareDateStr(series,str)==0){
                		total = total.add(chartSqlData.getTotalMoney());
					}
                }else{
                	if (str.equals(series)) {
                		total = total.add(chartSqlData.getTotalMoney());
                	}
                }
            }
        	tableDataItem.put("余额<200",String.valueOf(total));
        }
        // 4-3.查询SQL获得统计数据 -- 200<余额<=1000的 ，用户人数count(*)
        statDatas = new ArrayList<ChartMapVo>();

        if (chartSearchVo.getStatType().equals("day")) {
        	chartSearchVo.setFormatParam("%Y-%m-%e");
            statDatas = usersMapper.userResyMoneyLessThousandByDay(chartSearchVo);
        }

        if (chartSearchVo.getStatType().equals("month")) {
        	chartSearchVo.setFormatParam("%Y-%m");
            statDatas = usersMapper.userResyMoneyLessThousandByMonth(chartSearchVo);
        }

        if (chartSearchVo.getStatType().equals("quarter")) {
            statDatas = usersMapper.userResyMoneyLessThousandByQuarter(chartSearchVo);
        }
        for (HashMap<String, String> tableDataItem : tableDatas) {
            // 处理表格形式的数据.
        	BigDecimal total = new BigDecimal(0);
        	for (ChartMapVo chartSqlData : statDatas) {
        		 String str = tableDataItem.get("series");
                 String series = chartSqlData.getSeries();
        		if(chartSearchVo.getStatType().equals("day")){
                	if(DateUtil.compareDateStr(series,str)==0){
                		total = total.add(chartSqlData.getTotalMoney());
					}
                }else{
                	if (str.equals(series)) {
                		total = total.add(chartSqlData.getTotalMoney());
                	}
                }
            }
            tableDataItem.put("余额200~1k",String.valueOf(total));
        }
        // 4-4.查询SQL获得统计数据 -- 1K~3K ，用户人数count(*)
        statDatas = new ArrayList<ChartMapVo>();

        if (chartSearchVo.getStatType().equals("day")) {
        	chartSearchVo.setFormatParam("%Y-%m-%e");
            statDatas = usersMapper.userResyMoneyBetweenByDay(chartSearchVo);
        }

        if (chartSearchVo.getStatType().equals("month")) {
        	chartSearchVo.setFormatParam("%Y-%m");
            statDatas = usersMapper.userResyMoneyBetweenByMonth(chartSearchVo);
        }

        if (chartSearchVo.getStatType().equals("quarter")) {
            statDatas = usersMapper.userResyMoneyBetweenByQuarter(chartSearchVo);
        }
        for (HashMap<String, String> tableDataItem : tableDatas) {
            // 处理表格形式的数据.
        	BigDecimal total = new BigDecimal(0);
        	for (ChartMapVo chartSqlData : statDatas) {
        		 String str = tableDataItem.get("series");
                 String series = chartSqlData.getSeries();
                
                if(chartSearchVo.getStatType().equals("day")){
                	if(DateUtil.compareDateStr(series,str)==0){
                		total = total.add(chartSqlData.getTotalMoney());
					}
                }else{
                	if (str.equals(series)) {
                		total = total.add(chartSqlData.getTotalMoney());
                	}
                }
            }
        	tableDataItem.put("1K~3K",String.valueOf(total));
        }
        // 4-5.查询SQL获得统计数据 -- 余额>3k ，用户人数count(*)
        statDatas = new ArrayList<ChartMapVo>();

        if (chartSearchVo.getStatType().equals("day")) {
        	chartSearchVo.setFormatParam("%Y-%m-%e");
            statDatas = usersMapper.userResyMoneyThreeThousandByDay(chartSearchVo);
        }

        if (chartSearchVo.getStatType().equals("month")) {
        	chartSearchVo.setFormatParam("%Y-%m");
            statDatas = usersMapper.userResyMoneyThreeThousandByMonth(chartSearchVo);
        }

        if (chartSearchVo.getStatType().equals("quarter")) {
            statDatas = usersMapper.userResyMoneyThreeThousandByQuarter(chartSearchVo);
        }
        for (HashMap<String, String> tableDataItem : tableDatas) {
        	BigDecimal total = new BigDecimal(0);
        	for (ChartMapVo chartSqlData : statDatas) {
            // 处理表格形式的数据.
                String str = tableDataItem.get("series");
                String series = chartSqlData.getSeries();
                if(chartSearchVo.getStatType().equals("day")){
                	if(DateUtil.compareDateStr(series,str)==0){
                		total = total.add(chartSqlData.getTotalMoney());
					}
                }else{
                	if (str.equals(series)) {
                		total = total.add(chartSqlData.getTotalMoney());
                	}
                }
            }
        	tableDataItem.put("余额>3k",String.valueOf(total));
        }

        // 4-5.各种余额占比

        String lessTwoPercent = "0.00%";
        String lessThousandPercent = "0.00%";
        String lessBetweenPercent = "0.00%";
        String MoreThousandPercent = "0.00%";

        for (HashMap<String, String> tableDataItem : tableDatas) {
        	String totalMoney= tableDataItem.get("余额总金额");
            
            // 每行记录各种余额数量
        	String lessTwo = tableDataItem.get("余额<200");
        	String lessThousand = tableDataItem.get("余额200~1k");
        	String lessBetween = tableDataItem.get("1K~3K");
        	String MoreThousand = tableDataItem.get("余额>3k");
            DecimalFormat df1 = new DecimalFormat("0.00%");
            if(Double.parseDouble(totalMoney)>0.00){
            	lessTwoPercent = df1.format(Double.parseDouble(lessTwo)/Double.parseDouble(totalMoney));
            	tableDataItem.put("余额<200占比", lessTwoPercent);
            	lessThousandPercent = df1.format(Double.parseDouble(lessThousand)/Double.parseDouble(totalMoney));
            	tableDataItem.put("余额200~1k占比", lessThousandPercent);
            	lessBetweenPercent = df1.format(Double.parseDouble(lessBetween)/Double.parseDouble(totalMoney));
            	tableDataItem.put("1K~3K占比", lessBetweenPercent);
            	MoreThousandPercent = df1.format(Double.parseDouble(MoreThousand)/Double.parseDouble(totalMoney));
            	tableDataItem.put("余额>3k占比", MoreThousandPercent);
            }else{
            	tableDataItem.put("余额<200占比", lessTwoPercent);
            	tableDataItem.put("余额200~1k占比", lessThousandPercent);
            	tableDataItem.put("1K~3K占比", lessBetweenPercent);
            	tableDataItem.put("余额>3k占比", MoreThousandPercent);
            }
        }

        // 5.去掉第一个 tableDataItem ??
        if (tableDatas.size() > 0)
            tableDatas.remove(0);
        
        this.generateCount(legend, timeSeries, tableDatas, statType, chartDataVo);

        return chartDataVo;

    }
    
    public HashMap<String, String> setTableDateItem(ChartMapVo chartSqlData,BigDecimal money,HashMap<String, String> tableDataItem){
    	//1=500 2 = 1000面值 3 = 2000面值 4 =3000面值
		if (chartSqlData.getName().equals("1")) {
			money = chartSqlData.getTotalMoney();
            tableDataItem.put("五百面值",
                    MathBigDecimalUtil.round2(money));
        }
        if (chartSqlData.getName().equals("2")) {
        	money = chartSqlData.getTotalMoney();
            tableDataItem.put("一千面值",
                    MathBigDecimalUtil.round2(money));
        }
        if (chartSqlData.getName().equals("3")) {
        	money = chartSqlData.getTotalMoney();
            tableDataItem.put("两千面值",
                    MathBigDecimalUtil.round2(money));
        }
        if (chartSqlData.getName().equals("4")) {
        	money = chartSqlData.getTotalMoney();
            tableDataItem.put("三千面值",
                    MathBigDecimalUtil.round2(money));
        }
        
        return tableDataItem;
    }
    
    
    //统一生成 统计图 数据
    public ChartDataVo generateCount(List<String> legend,List<String> timeSeries,List<HashMap<String, String>> tableDatas,
    		String statType,ChartDataVo chartDataVo){
    	List<HashMap<String, Object>> dataItems = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> chartDataItem = null;
        List<Double> datas = null;
        for (int i = 0; i < legend.size(); i++) {
            chartDataItem = new HashMap<String, Object>();
            chartDataItem.put("name", legend.get(i));
            chartDataItem.put("type", "bar");
            datas = new ArrayList<Double>();

            for (int j = 1; j < timeSeries.size(); j++) {
                for (HashMap<String, String> tableDataItem : tableDatas) {
                    if (timeSeries.get(j).equals(
                            tableDataItem.get("series").toString())) {
                        String valueStr = tableDataItem.get(legend.get(i))
                                .toString();
                        Double v = Double.valueOf(valueStr);
                        datas.add(v);
                    }
                }
            }

            chartDataItem.put("data", datas);
            dataItems.add(chartDataItem);
        }
        chartDataVo.setSeries(JSON.toJSONString(dataItems));

        // x轴 刻度 转换
        for (HashMap<String, String> tableDataItem : tableDatas) {
            String seriesName = tableDataItem.get("series").toString();
            seriesName = ChartUtil.getTimeSeriesName(statType, seriesName);
            tableDataItem.put("series", seriesName);
        }
        chartDataVo.setTableDatas(tableDatas);
        
        return chartDataVo;
    }
    
}
