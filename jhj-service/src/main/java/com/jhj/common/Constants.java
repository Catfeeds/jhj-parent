package com.jhj.common;


public class Constants {

	public static String URL_ENCODE = "UTF-8";
	public static int PAGE_MAX_NUMBER = 10;
	/**
	 * 验证码最大值
	 */
	public static int CHECK_CODE_MAX_LENGTH = 999999;

	//'0' COMMENT '用户来源 0 = APP  1 = 微网站  2 = 管理后台'
	public static short USER_APP = 0;
	public static short USER_NET = 1;
	public static short USER_BACK = 2;

	//充值卡类型:1,无忧管家;2,快乐管家;3,超级管家
	public static long DICT_CARD_TYPE_1 = 1;
	public static long DICT_CARD_TYPE_2 = 2;
	public static long DICT_CARD_TYPE_3 = 3;

	//是否使用 0 = 未使用 1= 已使用
	public static short IS_USER_0 = 0;
	public static short IS_USER_1 = 1;

	public static short IS_USER_PROMOTION_1 = 1;
	public static short IS_USER_PROMOTION_0 = 0;

	//地址默认 默认地址 1 = 默认  0 = 不默认
	public static int ADDRESS_DEFAULT_1 = 1;
	public static int ADDRESS_DEFAULT_NOT_0 = 0;
	
	//待确认、待支付、待服务、待评价、已完成/已取消/已关闭
    //1     2      3      4      8    0     9
	public static short ORDER_STATUS_0 = 0;		//已取消
	public static short ORDER_STATUS_1 = 1;     //待确认
	public static short ORDER_STATUS_2 = 2;     //已确认
	
	public static short ORDER_STATUS_3 = 3;		//待支付
	public static short ORDER_STATUS_4 = 4;		//已支付
	public static short ORDER_STATUS_5 = 5;		//服务中
	public static short ORDER_STATUS_6 = 6;		//待评价
	public static short ORDER_STATUS_7 = 7;		//已评价
	
	public static short ORDER_STATUS_8 = 8;	    //已评价
	public static short ORDER_STATUS_9 = 9;     //已关闭
	
	// 新增：  提醒类 业务 订单 状态
	
	public static short ORDER_STATUS_10 = 10;	//提醒已预约 
	public static short ORDER_STATUS_11 = 11;	//提醒已完成
	public static short ORDER_STATUS_12 = 12;	//提醒已取消
	
	/*
	 *  order_type =6 的话费 充值 类型 订单， 
	 *  	在 微信支付 和  服务提供商, 处理服务前后, 设置 订单状态
	 */
	public static short ORDER_STATUS_13 = 13;	//充值中（缴费中）
	public static short ORDER_STATUS_14 = 14;	//充值完成 （缴费成功）
	public static short ORDER_STATUS_15 = 15;	//充值缴费失败
	public static short ORDER_STATUS_16 = 16;	//取消充值缴费 （微信支付超过1小时）
	
	
	//订单评价 0=好  1=一般 2=差
	public static short ORDER_RATE_GOOD = 0;
	public static short ORDER_RATE_GENERAL = 1;
	public static short ORDER_RATE_BAD = 2;

	//1 = 支付成功 2 = 退款成功
	public static short PAY_SUCCESS = 1;
	public static short BACK_SUCCESS = 2;

	//0 = 发送失败 1 = 发送成功
	public static short SMS_SUCCESS= 0;			//2015-11-24 15:46:10，  发送成功和失败都是 0  ？？？？
	public static short SMS_FAIL= 0;
	public static String SMS_SUCCESS_CODE= "000000";
	public static String SMS_STATUS_CODE= "statusCode";
	public static String SMS_STATUS_MSG= "msg";
	
	/*
	 *  短信验证码 类型 2016年3月28日12:13:13
	 */
	public static int SMS_TYPE_0 = 0;		// app登录
	public static int SMS_TYPE_1 = 1;		// app 支付
	public static int SMS_TYPE_2 = 2;
	public static int SMS_TYPE_3 = 3;		// 运营平台。 会员充值 
	
	
	//短信模板定义
	public static String PAY_SUCCESS_SMS_TEMPLE_ID= "9282";
	//public static String GET_CODE_TEMPLE_ID= "8429";
	public static String GET_CODE_TEMPLE_ID= "30064";
	public static String STAFF_POST_BEGIN= "64740";//服务人员点击开始服务  64740
	public static String STAFF_POST_DONE= "64744";//服务人员点击完成服务  64740
	public static String STAFF_JOIN_BLACK= "80275";//服务人员欠款大于1000元被加入黑名单
	public static String STAFF_OUT_BLACK= "65016";//服务人员已经被移除黑名单
	public static String GET_USER_VERIFY_ID= "30064";//jhj 用户获取验证码短信模板Id
	public static String GET_AM_EXP_CLEAN_ORDER_ID= "29167";//jhj 助理获得深度保洁订单通知
	public static String GET_USER_NO_PAY_ORDER_ID= "29163";//jhj 用户收到未支付的预约单通知
	public static String GET_CODE_REMIND_ID= "10923";
	public static String GET_CODE_MAX_VALID= "30";//短信有效时间
	public static String NOTICE_CUSTOMER_Message_ID= "9280";
	public static String NOTICE_STAFF__Message_ID= "15284";
    public static String GET_USER_ORDER_AM_ID="29161";//用户预约下单后给助理发短信
	public static String CANCEL_ORDER_SATUS= "cancel";
	public static String AM_NOTICE_CUSTOMER_Message= "64742";//助理修改助理预约单后给用户发短信
	public static String AM_CLEAN_NOTICE_CUSTOMER_Message= "29160";//助理修改助理预约单后给用户发短信
	public static String NOTICE_USER_REST_MONEY_NOT_ENOUGH= "34652";
	public static String STAFF_PAY_DEPT_SUCCESS =" 80274";   //服务人员支付欠款成功
	public static String STAFF_CASE_REQ = "80272";   //服务人员申请提现

	//支付方式： 0 = 余额支付 1 = 支付宝 2 = 微信支付 3 = 智慧支付(保留,暂不开发)
	//4 = 上门刷卡（保留，暂不开发） 5 = 优惠券兑换
	public static Short PAY_TYPE_0 = 0;
	public static Short PAY_TYPE_1 = 1;
	public static Short PAY_TYPE_2 = 2;
	public static Short PAY_TYPE_3 = 3;
	public static Short PAY_TYPE_4 = 4;
	public static Short PAY_TYPE_5 = 5;
	
	//2015-10-30 16:44:26 新增支付方式  ：针对 助理预约单的  现金支付
	public static Short PAY_TYPE_6 = 6;
	
	//第三方支付.
	public static Short PAY_TYPE_7 = 7;
	
	
	public static int SUCCESS_0 = 0;
	public static int ERROR_999 = 999;
	public static int ERROR_100 = 100;
	public static int ERROR_101 = 101;
	public static int ERROR_102 = 102;
	public static int ERROR_103 = 103;
	public static int ERROR_104 = 104;

	//0 = 未支付 1 = 已支付
	public static Short PAY_STATUS_0 = 0;
	public static Short PAY_STATUS_1 = 1;

	//订单类型
	public static Short ORDER_TYPE_0 = 0;  //钟点工
	public static Short ORDER_TYPE_1 = 1; //深度保洁
	public static Short ORDER_TYPE_2 = 2; //助理订单
	public static Short ORDER_TYPE_3 = 3; //配送服务订单
	public static Short ORDER_TYPE_4 = 4; //充值卡订单
	//新增订单类型： 提醒类 订单
	public static Short ORDER_TYPE_5 = 5;//提醒类 订单，不收费
	//2015年10月13日16:58:48 新增订单 类型 ：  话费、水电煤缴费类订单
	public static Short ORDER_TYPE_6 = 6;
	
	
	//消费类型
	public static Short PAY_ORDER_TYPE_0 = 0;  //订单支付
	public static Short PAY_ORDER_TYPE_1 = 1; //购买充值卡
	
	public static Short PAY_ORDER_TYPE_2 = 2;	//手机话费类充值
	
	

	//服务类型：在此列出，便于查看管理，对应 dict_service_types 表
	public static Short ORDER_SERVICE_TYPE_1 = 1;			//钟点工
	public static Short ORDER_SERVICE_TYPE_2 = 2;			//深度保洁
	public static Short ORDER_SERVICE_TYPE_3 = 3;			//日常家事
	public static Short ORDER_SERVICE_TYPE_4 = 4;			//应急
	public static Short ORDER_SERVICE_TYPE_5 = 5;			//跑腿
	public static Short ORDER_SERVICE_TYPE_6 = 6;			//陪伴
	public static Short ORDER_SERVICE_TYPE_7 = 7;			//提醒
	public static Short ORDER_SERVICE_TYPE_8 = 8;			//代购
	public static Short ORDER_SERVICE_TYPE_9 = 9;			//衣橱整理
	public static Short ORDER_SERVICE_TYPE_10 = 10;			//其他
	public static Short ORDER_SERVICE_TYPE_11 = 11;			//话费充值
	
	
	/*
	 * 问卷调查在 提交问卷,到达结果展示页时,设置标识,用来区分  选择结果、推荐套餐、赠送套餐
	 */
	public static long SURVEY_RESULT_0 = 0L;	//选择结果
	public static long SURVEY_RESULT_1 = 1L;	//推荐服务	
	public static long SURVEY_RESULT_2 = 2L;	//免费赠送服务
	
	//问卷调查 ， 服务对应的 计费方式
	public static Short SURVEY_MEARSUREMENT_0 = 0; // 按月计费
	public static Short SURVEY_MEARSUREMENT_1 = 1; // 按年计费
	public static Short SURVEY_MEARSUREMENT_2 = 2; // 按次计费
	public static Short SURVEY_MEARSUREMENT_3 = 3; // 免费
	
	//问卷调查， 子服务类型
	public static Short SURVEY_CHILD_TYPE_0 = 0;	//不包含 子服务
	public static Short SURVEY_CHILD_TYPE_1 = 1;	//子服务是  单选-->除尘除螨  4/6/8次
	public static Short SURVEY_CHILD_TYPE_2 = 2;	//子服务是 多选--> 家电清洗A  空调1次,擦玻璃2次....
	
	//问卷调查， 计算用户选择服务的 总价时， 需要的 支付方式 
	public static short SURVEY_PAY_TYPE_0 = 0;		//按年支付
	public static short SURVEY_PAY_TYPE_1 = 1;		//按半年支付
	public static short SURVEY_PAY_TYPE_2 = 2;		//按月支付

	/**
	 * jhj2.0 订单状态
	 */
	//钟点工
	public static short ORDER_HOUR_STATUS_0=0;//已取消
	public static short ORDER_HOUR_STATUS_1=1;//未支付
	public static short ORDER_HOUR_STATUS_2=2;//已支付
	
	public static short ORDER_HOUR_STATUS_3=3;//已派工
	public static short ORDER_HOUR_STATUS_5=5;//开始服务
	public static short ORDER_HOUR_STATUS_7=7;//完成服务
	public static short ORDER_HOUR_STATUS_8=8;//已评价
	public static short ORDER_HOUR_STATUS_9=9;//已关闭
	//助理
	public static short ORDER_AM_STATUS_0=0;//已取消
	public static short ORDER_AM_STATUS_1=1;//已预约
	
	//2016年3月30日10:11:18  修正   顺序:  已预约、已确认、已支付、已派工
	
	public static short ORDER_AM_STATUS_2=2;//已确认
	public static short ORDER_AM_STATUS_3=3;//已支付
	public static short ORDER_AM_STATUS_4=4;//已派工
	
	
	public static short ORDER_AM_STATUS_5=5;//开始服务
	public static short ORDER_AM_STATUS_7=7;//完成服务
	public static short ORDER_AM_STATUS_9=9;//已关闭
	//深度保洁
	public static short ORDER_DEP_STATUS_0=0;//已取消
	public static short ORDER_DEP_STATUS_1=1;//未支付
	public static short ORDER_DEP_STATUS_2=2;//已支付
	public static short ORDER_DEP_STATUS_3=3;//已派工
	public static short ORDER_DEP_STATUS_5=5;//开始服务
	public static short ORDER_DEP_STATUS_7=7;//完成服务
	public static short ORDER_DEP_STATUS_8=8;//已评价
	public static short ORDER_DEP_STATUS_9=9;//已关闭
	//配送
	public static short ORDER_DEL_STATUS_0=0;//已取消
	public static short ORDER_DEL_STATUS_1=1;//未支付
	public static short ORDER_DEL_STATUS_2=2;//已支付
	public static short ORDER_DEL_STATUS_3=3;//已派工
	public static short ORDER_DEL_STATUS_5=5;//开始服务
	public static short ORDER_DEL_STATUS_6=6;//已取货
	public static short ORDER_DEL_STATUS_7=7;//已送达
	public static short ORDER_DEL_STATUS_9=9;//已关闭
	
	/**
	 * 推送内容
	 */
	public static String ALERT_STAFF_MSG ="您好，您有新订单啦！请您尽快打开订单列表了解详情吧。";
	
	
	/** 叮当大学 **/
	
	//服务人员 考试 考核情况
	public static short UNIVERSITY_STAFF_PASS_STATUS_FAIL = 0;	//  考试未通过
	public static short UNIVERSITY_STAFF_PASS_STATUS_SUCCESS = 1; // 考试通过
	
	//考试题目 结果是否正确
	public static short UNIVERSITY_QUESTION_FAIL = 0;	//  错误
	public static short UNIVERSITY_QUESTION_SUCCESS = 1; // 正确
	
	
	/** 服务人员认证状态（身份认证、考试认证。。。） **/
	public static short STAFF_AUTH_STATUS_FAIL = 0;		//未通过验证
	public static short STAFF_AUTH_STATUS_SUCCESS = 1;	//已认证通过
	
	
	//消息推送，用户类型
	public static short MSG_USER_TYPE_0 = 0;	//用户
	public static short MSG_USER_TYPE_1 = 1;	//服务人员
	
	
	//图片服务器 地址
	public static String IMG_SERVER_HOST = "http://img.jia-he-jia.com:8080";
	
	
	//2016年3月7日16:06:01  门店 和 小组 的标识
	public static short ORG_OR_GROUP_0 = 0;	//门店
	public static short ORG_OR_GROUP_1 = 1; //小组
	
	
	//2016年3月24日18:07:28  派工表，该条 派工记录 是否有效
	public static Short ORDER_DIS_ENABLE = 1;
	public static Short ORDER_DIS_DISABLE = 0;
	
	
	//2016年3月29日18:44:38  运营平台。 会员充值。 接收验证码的 配置 类型，（暂时没使用）
	public static String OA_CHARGE_SETTING_TYPE = "recharge_mobile";
	
	
	//2016年4月6日16:52:05  微网站--我的--余额--用户消费记录的标识 
	public static Short USER_PAY_FLAG_MINUS = 0;	//用于支付  .用户钱少了
	public static Short USER_PAY_FLAG_PLUS = 1;		//用于 充值 。用户钱多了
	
	
	
	/*
	 *  截止 2016年4月13日18:56:37 
	 *  	jhj2.1      短信模板 
	 */
	
	/****************	用户微信版		********************/
	
	//1. 金牌保洁/厨娘烧饭/企业服务
	
	/*
	 *  订单变更 (订单时间变更)
	 *  
	 *  您预定的{1}服务已经变更为{2}，感谢您的理解，给您带来的不便敬请谅解。
	 */
	public static String MESSAGE_ORDER_DATE_CHANGE = "79653";
	
	/*	
	 * 	 下单成功待服务（支付成功）
	 * 
	 *   您预定的{1}{2}服务已经确认，感谢您的支持，服务人员会尽快与您联系，如有任何疑问请拨打010-56429112
	 */
	public static String MESSAGE_PAY_SUCCESS_TO_SERVICE = "79654";
	
	/*
	 * 服务成功待评价（服务人员点击完成服务）
	 * 
	 *   您定制的{1}{2}服务已完成。为了您下次拥有更好的服务体验，诚邀您对我们的服务进行评价。链接{3}
	 */
	public static String MESSAGE_SERVICE_SUCCESS_TO_RATE = "79655";
	
	
	/*
	 *  套餐预约成功 (提交预约订单)
	 * 
	 *    您定制的{1}套餐已经预约成功，感谢您的支持，我们会尽快与您联系，如有任何疑问请拨打010-56429112
	 * 
	 */
	public static String MESSAGE_SERVICE_PACKAGE_ORDER_SUCCESS = "79656";
	
	
	//贴心家事、深度养护
	
	/*
	 * 预约成功 （提交预约订单）
	 * 
	 *   您预定的{1}服务已经受理，感谢您的支持，我们会尽快与您联系，如有任何疑问请拨打010-56429112
	 * 
	 */
	public static String MESSAGE_SERVICE_ORDER_SUCCESS = "79658";
	
	/*
	 *  待支付 （客服完善订单信息）
	 * 
	 * 	您有一张未支付的订单，请您于30分钟内完成支付，以免耽误您的使用，谢谢。如已支付请忽略。
	 */
	public static String MESSAGE_ORDER_NEED_TO_PAY = "79660";
	
	
	/*
	 * 代付款
	 * 您的订单已确认，请在30分钟之内支付
	 * */
	public static String MESSAGE_ORDER_PAY_HINT ="103194";
	
	/*
	 * 服务完成待评价 (服务人员点击完成服务)
	 * 
	 *  您定制的{1}服务已完成。为了您下次拥有更好的服务体验，诚邀您对我们的服务进行评价。链接{2}
	 */
	public static String MESSAGE_SERVICE_SUCCESS_TO_RATE_2 = "79663";	
	
	//我的
	
	/*
	 * 充值成功
	 * 
	 *  您好，您的账户于{1}充值{2}。定制全年套餐，让美好生活不再有家务之忧。
	 */
	public static String MESSAGE_CHARGE_PAY_SUCCESS = "79664";
	
	/*
	 * 余额不足
	 * 
	 * 您好，您的账户余额已不足，为避免影响您的正常使用，请及时充值，谢谢。
	 */
	public static String MESSAGE_REST_MONEY_LITTLE = "79666";
	
	/*
	 *订单取消提醒
	 *你的{1}的订单{2}已取消，请及时关注叮当到家APP获取相关订单信息。
	 */
	 public static String MESSAGE_ORDER_CANCLE="108638";
	 
	 //派工最远匹配距离
	 public static int MAX_DISTANCE = 20000;
	 
	 //派工服务前2小时占位
	 public static Long SERVICE_PRE_TIME = (long) (2 * 3600);
	
	
}



