package com.meijia.utils.vo;

public class OrderVo {

	/*
	 * 手机号
	 */
	private Long C_VIP_MOBILE = 0L;

	/*
	 * 城市ID
	 */
	private Long C_CITY_ID = 0L;

	/*
	 * 服务类型, 保洁:1, 午餐:2, 晚餐:3, 保洁+午餐:4, 保洁+晚餐:5, 午餐+晚餐:6
	 */
	private Integer S_TYPE = 0;

	/*
	 * 服务模式, 零工(今天/明天/后天):1, 包月:2
	 */
	private Integer S_MODEL = 0;

	/*
	 * 订单开始日期, 格式为 2014-07-01
	 */
	private String S_START_DATE = "";

	/*
	 * 首次服务日期, 格式为 2014-07-01
	 */
	private String S_FIRST_DATE = "";

	/*
	 * 服务频次: 1,3,5 2,4,6 1,2,3,4,5 1,2,3,4,5,6
	 */
	private String S_CYCLE = "0";

	/*
	 * 服务频次: 1,3,5 2,4,6 1,2,3,4,5 1,2,3,4,5,6
	 * 变成中文 一，三，五
	 */
	private String S_CYCLE_NAME = "";

	/*
	 * 服务时长, 零工(今天/明天/后天):0, 包1月:1, 包3月:3, 包6月:6
	 */
	private Integer S_PERIOD = 0;

	/*
	 * 服务开始时间, 格式为: 07:30
	 */
	private String S_START_TIME = "";

	/*
	 * 服务开始时间, 格式为: 07:30
	 */
	private String S_END_TIME = "";

	/*
	 * 是否需要保洁工具 0 = 否 1= 是
	 */
	private Integer C_CLEAR_TOOLS = 0;

	/*
	 * 服务结束时间, 格式为: 09:30
	 */
	private Long C_CELL_ID = 0L;

	/*
	 * 订单编号,默认为0为新增
	 */
	private String C_CONTRACT_CODE = "";

	/*
	 * 门牌号
	 */
	private String S_ADDRESS = "";

	/*
	 * 联系人
	 */
	private String C_CUSTOM_NAME = "";

	/*
	 * 联系电话
	 */
	private String C_CUSTOM_MOBILE = "";

	/*
	 * 付款类型
	 */
	private Integer C_PAY_TYPE = 0;

	/*
	 * 合同金额
	 */
	private Double C_CONTRACT_MONEY = 0.0;

	/*
	 * 服务总次数
	 */
	private int C_SERVICE_COUNT = 0;

	/*
	 * 服务总小时数
	 */
	private int C_SERVICE_COUNT_HOUR = 0;

	/*
	 * 单次服务小时 数
	 */
	private int C_SERVICE_HOUR = 0;

	/*
	 * 法定节假日数
	 */
	private int C_HOLIDAY_COUNT = 0;

	/*
	 * 用户余额
	 */
	private Double C_REST_MONEY = 0.0;

	/*
	 *  折扣
	 */
	private Double C_CONTRACT_DISCOUNT = 0.0;

	/*
	 *  交通费用
	 */
	private Double C_TRAFFIC_EXTRA_PRICE = 0.0;

	/*
	 *  小时服务单价
	 */
	private Double C_PER_HOUR_FEE = 0.0;

	/*
	 *  小时服务单价
	 */
	private Double C_CLEAR_TOOL_FEE = 0.0;

	/*
	 * 	合同金额-折扣价
	 */
	private Double C_CONTRACT_DISCOUNT_MONEY = 0.0;

	/*
	 * 		赠送价格
	 */
	private Double C_SEND_MONEY = 0.0;

	/*
	 * 	折合小时单价
	 */
	private Double C_DISCOUNT_HOUR_FEE =0.0;

	/*
	 * 	折合小时单价
	 */
	private String C_ORDER_FROM = "0";

	/*
	 * 	折合小时单价
	 */
	private int C_TOTAL_DISPATCH = 0;

	/*
	 * 派工阿姨ID
	 */
	private Long C_EMPLOYE_ID = 0L;


	/*
	 * 派工阿姨代码
	 */
	private String C_EMPLOYE_CODE = "";

	/*
	 * 派工阿姨姓名
	 */
	private String C_EMPLOYE_NAME = "";

	/*
	 * 派工阿姨手机号
	 */
	private String C_EMPLOYE_MOBILE = "";



	public Double getC_CONTRACT_DISCOUNT_MONEY() {
		return C_CONTRACT_DISCOUNT_MONEY;
	}


	public void setC_CONTRACT_DISCOUNT_MONEY(Double c_CONTRACT_DISCOUNT_MONEY) {
		C_CONTRACT_DISCOUNT_MONEY = c_CONTRACT_DISCOUNT_MONEY;
	}


	public Double getC_SEND_MONEY() {
		return C_SEND_MONEY;
	}


	public void setC_SEND_MONEY(Double c_SEND_MONEY) {
		C_SEND_MONEY = c_SEND_MONEY;
	}


	public Double getC_DISCOUNT_HOUR_FEE() {
		return C_DISCOUNT_HOUR_FEE;
	}


	public void setC_DISCOUNT_HOUR_FEE(Double c_DISCOUNT_HOUR_FEE) {
		C_DISCOUNT_HOUR_FEE = c_DISCOUNT_HOUR_FEE;
	}


	public Long getC_VIP_MOBILE() {
		return C_VIP_MOBILE;
	}


	public void setC_VIP_MOBILE(Long c_VIP_MOBILE) {
		if (c_VIP_MOBILE != null)
		C_VIP_MOBILE = c_VIP_MOBILE;
	}


	public Double getC_CONTRACT_MONEY() {
		return C_CONTRACT_MONEY;
	}


	public void setC_CONTRACT_MONEY(Double c_CONTRACT_MONEY) {
		if (c_CONTRACT_MONEY != null)
		C_CONTRACT_MONEY = c_CONTRACT_MONEY;
	}


	public int getC_SERVICE_COUNT() {
		return C_SERVICE_COUNT;
	}


	public void setC_SERVICE_COUNT(int c_SERVICE_COUNT) {
		C_SERVICE_COUNT = c_SERVICE_COUNT;
	}


	public int getC_SERVICE_COUNT_HOUR() {
		return C_SERVICE_COUNT_HOUR;
	}


	public void setC_SERVICE_COUNT_HOUR(int c_SERVICE_COUNT_HOUR) {
		C_SERVICE_COUNT_HOUR = c_SERVICE_COUNT_HOUR;
	}


	public Long getC_CITY_ID() {
		return C_CITY_ID;
	}


	public void setC_CITY_ID(Long c_CITY_ID) {
		if (c_CITY_ID != null)
		C_CITY_ID = c_CITY_ID;
	}


	public Integer getS_TYPE() {
		return S_TYPE;
	}


	public void setS_TYPE(Integer s_TYPE) {
		if (s_TYPE != null)
		S_TYPE = s_TYPE;
	}


	public Integer getS_MODEL() {
		return S_MODEL;
	}


	public void setS_MODEL(Integer s_MODEL) {
		if (s_MODEL != null)
		S_MODEL = s_MODEL;
	}


	public String getS_START_DATE() {
		return S_START_DATE;
	}


	public void setS_START_DATE(String s_START_DATE) {
		if (s_START_DATE != null)
		S_START_DATE = s_START_DATE;
	}


	public String getS_CYCLE() {
		return S_CYCLE;
	}


	public void setS_CYCLE(String s_CYCLE) {
		if (s_CYCLE != null)
		S_CYCLE = s_CYCLE;
	}


	public String getS_START_TIME() {
		return S_START_TIME;
	}


	public void setS_START_TIME(String s_START_TIME) {
		if (s_START_TIME != null)
		S_START_TIME = s_START_TIME;
	}


	public Long getC_CELL_ID() {
		return C_CELL_ID;
	}


	public void setC_CELL_ID(Long c_CELL_ID) {
		if (c_CELL_ID != null)
		C_CELL_ID = c_CELL_ID;
	}


	public String getC_CONTRACT_CODE() {
		return C_CONTRACT_CODE;
	}


	public void setC_CONTRACT_CODE(String c_CONTRACT_CODE) {
		if (c_CONTRACT_CODE != null)
		C_CONTRACT_CODE = c_CONTRACT_CODE;
	}


	public String getS_ADDRESS() {
		return S_ADDRESS;
	}


	public void setS_ADDRESS(String s_ADDRESS) {
		if (s_ADDRESS != null)
		S_ADDRESS = s_ADDRESS;
	}


	public String getC_CUSTOM_NAME() {
		return C_CUSTOM_NAME;
	}


	public void setC_CUSTOM_NAME(String c_CUSTOM_NAME) {
		if (c_CUSTOM_NAME != null)
		C_CUSTOM_NAME = c_CUSTOM_NAME;
	}


	public String getC_CUSTOM_MOBILE() {
		return C_CUSTOM_MOBILE;
	}


	public void setC_CUSTOM_MOBILE(String c_CUSTOM_MOBILE) {
		if (c_CUSTOM_MOBILE != null)
		C_CUSTOM_MOBILE = c_CUSTOM_MOBILE;
	}


	public Integer getC_PAY_TYPE() {
		return C_PAY_TYPE;
	}


	public void setC_PAY_TYPE(Integer c_PAY_TYPE) {
		if (c_PAY_TYPE != null)
		C_PAY_TYPE = c_PAY_TYPE;
	}


	public Integer getS_PERIOD() {
		return S_PERIOD;
	}


	public void setS_PERIOD(Integer s_PERIOD) {
		if (s_PERIOD != null)
		S_PERIOD = s_PERIOD;
	}


	public Integer getC_CLEAR_TOOLS() {
		return C_CLEAR_TOOLS;
	}


	public void setC_CLEAR_TOOLS(Integer c_CLEAR_TOOLS) {
		if (c_CLEAR_TOOLS != null)
		C_CLEAR_TOOLS = c_CLEAR_TOOLS;
	}


	public String getS_END_TIME() {
		return S_END_TIME;
	}


	public void setS_END_TIME(String s_END_TIME) {
		if (s_END_TIME != null)
		S_END_TIME = s_END_TIME;
	}


	public Double getC_REST_MONEY() {
		return C_REST_MONEY;
	}


	public void setC_REST_MONEY(Double c_REST_MONEY) {
		if (c_REST_MONEY != null)
		C_REST_MONEY = c_REST_MONEY;
	}


	public Double getC_CONTRACT_DISCOUNT() {
		return C_CONTRACT_DISCOUNT;
	}


	public void setC_CONTRACT_DISCOUNT(Double c_CONTRACT_DISCOUNT) {
		if (c_CONTRACT_DISCOUNT != null)
		C_CONTRACT_DISCOUNT = c_CONTRACT_DISCOUNT;
	}


	public Double getC_TRAFFIC_EXTRA_PRICE() {
		return C_TRAFFIC_EXTRA_PRICE;
	}


	public void setC_TRAFFIC_EXTRA_PRICE(Double c_TRAFFIC_EXTRA_PRICE) {
		if (c_TRAFFIC_EXTRA_PRICE != null)
		C_TRAFFIC_EXTRA_PRICE = c_TRAFFIC_EXTRA_PRICE;
	}


	public Double getC_PER_HOUR_FEE() {
		return C_PER_HOUR_FEE;
	}


	public void setC_PER_HOUR_FEE(Double c_PER_HOUR_FEE) {
		if (c_PER_HOUR_FEE != null)
		C_PER_HOUR_FEE = c_PER_HOUR_FEE;
	}


	public Double getC_CLEAR_TOOL_FEE() {
		return C_CLEAR_TOOL_FEE;
	}


	public void setC_CLEAR_TOOL_FEE(Double c_CLEAR_TOOL_FEE) {
		if (c_CLEAR_TOOL_FEE != null)
		C_CLEAR_TOOL_FEE = c_CLEAR_TOOL_FEE;
	}


	public int getC_SERVICE_HOUR() {
		return C_SERVICE_HOUR;
	}


	public void setC_SERVICE_HOUR(int c_SERVICE_HOUR) {
		C_SERVICE_HOUR = c_SERVICE_HOUR;
	}


	public int getC_HOLIDAY_COUNT() {
		return C_HOLIDAY_COUNT;
	}


	public void setC_HOLIDAY_COUNT(int c_HOLIDAY_COUNT) {
		C_HOLIDAY_COUNT = c_HOLIDAY_COUNT;
	}


	public String getS_CYCLE_NAME() {
		return S_CYCLE_NAME;
	}


	public void setS_CYCLE_NAME(String s_CYCLE_NAME) {
		if (s_CYCLE_NAME != null)
		S_CYCLE_NAME = s_CYCLE_NAME;
	}


	public String getS_FIRST_DATE() {
		return S_FIRST_DATE;
	}


	public void setS_FIRST_DATE(String s_FIRST_DATE) {
		S_FIRST_DATE = s_FIRST_DATE;
	}


	public String getC_ORDER_FROM() {
		return C_ORDER_FROM;
	}


	public void setC_ORDER_FROM(String c_ORDER_FROM) {
		C_ORDER_FROM = c_ORDER_FROM;
	}


	public int getC_TOTAL_DISPATCH() {
		return C_TOTAL_DISPATCH;
	}


	public void setC_TOTAL_DISPATCH(int c_TOTAL_DISPATCH) {
		C_TOTAL_DISPATCH = c_TOTAL_DISPATCH;
	}


	public Long getC_EMPLOYE_ID() {
		return C_EMPLOYE_ID;
	}


	public void setC_EMPLOYE_ID(Long c_EMPLOYE_ID) {
		C_EMPLOYE_ID = c_EMPLOYE_ID;
	}


	public String getC_EMPLOYE_CODE() {
		return C_EMPLOYE_CODE;
	}


	public void setC_EMPLOYE_CODE(String c_EMPLOYE_CODE) {
		C_EMPLOYE_CODE = c_EMPLOYE_CODE;
	}


	public String getC_EMPLOYE_NAME() {
		return C_EMPLOYE_NAME;
	}


	public void setC_EMPLOYE_NAME(String c_EMPLOYE_NAME) {
		C_EMPLOYE_NAME = c_EMPLOYE_NAME;
	}


	public String getC_EMPLOYE_MOBILE() {
		return C_EMPLOYE_MOBILE;
	}


	public void setC_EMPLOYE_MOBILE(String c_EMPLOYE_MOBILE) {
		C_EMPLOYE_MOBILE = c_EMPLOYE_MOBILE;
	}

}