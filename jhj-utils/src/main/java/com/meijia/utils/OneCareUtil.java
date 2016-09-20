package com.meijia.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 有个管家的常用方法
 *
 */
public class OneCareUtil {

	/**
	 * 判断支付是否正确
	 * @param trade_status 支付状态  
	 * 									  支付宝客户端成功状态为: TRADE_FINISHED 或者为 TRADE_SUCCESS
	 *                                    支付宝网页版成功状态为: success
	 *                                    微信支付成功的状态为: SUCCESS
	 */
	public static boolean isPaySuccess(String tradeStatus) {

		if (StringUtil.isEmpty(tradeStatus)) {
			return false;
		}

		switch (tradeStatus) {
			case "TRADE_FINISHED":
			case "TRADE_SUCCESS":
			case "success":
			case "SUCCESS":
				return true;
		}
		return false;
	}

	
	
	public static String getOrderStausName(Short status){
		String statusName = "";
		switch (status) {
			case 0:
				statusName = "已取消";
				break;
			case 1:
				statusName = "待支付";
				break;
			case 2:
				statusName = "已支付";
				break;
			case 3:
				statusName = "待服务";
				break;
			case 4:
				statusName = "即将服务";
				break;
			case 5:
				statusName = "待评价";
				break;
			case 6:
				statusName = "已完成";
				break;
			case 7:
				statusName = "已关闭";
				break;
			default:
				statusName = "";
		}
		return statusName;
	}
	public static String getStatusName(Short status) {
		String statusName = "";
		switch (status) {
		case 0:
			statusName = "无效";
			break;
		case 1:
			statusName = "有效";
			break;
		default:
			statusName = "";
		}
		return statusName;
	}
	public static String getSendStatusName(Short sendStatus) {
		String sendStatusName = "";
		switch (sendStatus) {
		case 0:
			sendStatusName = "未发送";
			break;
		case 1:
			sendStatusName = "已发送";
			break;
		default:
			sendStatusName = "";
		}
		return sendStatusName;
	}
	public static String getIsRead(Short isRead) {
		String isReadName = "";
		switch (isRead) {
		case 0:
			isReadName = "未读";
			break;
		case 1:
			isReadName = "已读";
			break;
		default:
			isReadName = "";
		}
		return isReadName;
	}

	public static String getRangTypeName(Short rangType) {
		String rangTypeName = "";
		switch (rangType) {
			case 0:
				rangTypeName = "通用";
				break;
			case 1:
				rangTypeName = "唯一";
				break;
			default:
				rangTypeName = "";
		}
		return rangTypeName;
	}
	public static String getCouponTypeName(Short couponType) {
		String couponTypeName = "";
		switch (couponType) {
		case 0:
			couponTypeName = "订单支付 ";
			break;
		case 1:
			couponTypeName = "充值卡充值 ";
			break;
		case 2:
			couponTypeName = "活动相关";
			break;
		default:
			couponTypeName = "";
		}
		return couponTypeName;
	}
	public static String getSexTypeName(Short sexType) {
		String sexTypeName = "";
		switch (sexType) {
		case 0:
			sexTypeName = "男 ";
			break;
		case 1:
			sexTypeName = "女";
			break;
		default:
			sexTypeName = "";
		}
		return sexTypeName;
	}
	public static String getIsUsedName(Short isUsed) {
		String isUsedName = "";
		switch (isUsed) {
		case 0:
			isUsedName = "未使用 ";
			break;
		case 1:
			isUsedName = "已使用";
			break;
		default:
			isUsedName = "";
		}
		return isUsedName;
	}
	public static List<String> getOrderStatus(){
		List<String> list = new ArrayList<String>();
//		list.add(0, "已取消");
//		list.add(1, "待支付");
//		list.add(2, "已支付");
//		list.add(3, "待服务");
//		list.add(4, "即将服务");
//		list.add(5, "待评价");
//		list.add(6, "已完成");
//		list.add(7, "已关闭");

		list.add(0,"已取消");
		list.add(1,"待确认");
		list.add(2,"已确认");
		list.add(3,"待支付");
		list.add(4,"已支付");
		list.add(5,"服务中");
		list.add(6,"待评价");
		list.add(7,"已评价");
		list.add(9,"已关闭");
		
		list.add(10,"提醒已预约");
		list.add(11,"提醒已完成");
		list.add(12,"提醒已取消");
		
		list.add(13,"充值中");
		list.add(14,"充值成功");
		list.add(15,"充值失败");
		//微信支付失败，超过30分钟，设置 为 取消
		list.add(16,"取消充值");
		
		return list;

	}

	public static String getOrderFromName(Short status) {
		String statusName = "";
		switch (status) {
			case 0:
				statusName = "App";
				break;
			case 1:
				statusName = "微网站";
				break;
			case 2:
				statusName = "管理平台";
				break;
			case 999:
				statusName = "所有来源";
				break;
			default:
				statusName = "";
		}
		return statusName;
	}

	public static List<String> getOrderFrom(){
		List<String> list = new ArrayList<String>();
		list.add(0,"APP");
		list.add(1,"微网站");
		list.add(2,"管理平台");
		return list;
	}
	public static List<String> getMsgSendGroupName(){
		List<String> list = new ArrayList<String>();
		list.add(0,"全部用户");
		list.add(1,"已注册未下单用户");
		list.add(2,"下过单用户");
		return list;
	}

	public static List<String> getPayType(){
		List<String> list = new ArrayList<String>();
		list.add(0,"月结");
		list.add(1,"按次结算");
		list.add(2,"预付");
		return list;
	}

	public static String getPayTypeName(Short payType) {
		String payTypeName = "";
		switch (payType) {
			case 0:
				payTypeName = "余额支付";
				break;
			case 1:
				payTypeName = "支付宝支付";
				break;
			case 2:
				payTypeName = "微信支付";
				break;
			case 3:
				payTypeName = "智慧支付";
				break;
			case 4:
				payTypeName = "上门刷卡";
				break;
			case 5:
				payTypeName ="优惠券兑换";
				break;
			case 6:
				payTypeName = "现金支付";
				break;
			default:
				payTypeName = "";
		}
		return payTypeName;
	}

	public static String getserviceTypeName(Short seviceType) {
		String serviceTypeName = "";
		switch (seviceType) {
			case 0:
				serviceTypeName = "全部";
				break;
			case 1:
				serviceTypeName = "做饭";
				break;
			case 2:
				serviceTypeName = "洗衣";
				break;
			case 3:
				serviceTypeName = "家电清洗";
				break;
			case 4:
				serviceTypeName = "保洁";
				break;
			case 5:
				serviceTypeName = "擦玻璃";
				break;
			case 6:
				serviceTypeName = "管道疏通";
				break;
			case 7:
				serviceTypeName = "新居开荒";
				break;
			default:
				serviceTypeName = "";
		}
		return serviceTypeName;
	}

	public static String getPartnerName(String partnerId) {
		String partnerName = "";
		switch (partnerId) {
			case "0":
				partnerName = "自由派工";
				break;
			case "1":
				partnerName = "嘉佣坊";
				break;
			default:
				partnerName = "";
		}
		return partnerName;
	}
	public static String getUserTypeName(Short userTypeId) {
		String userTypeName = "";
		switch (userTypeId) {
		case 0:
			userTypeName = "普通用户";
			break;
		case 1:
			userTypeName = "代理商";
			break;
		default:
			userTypeName = "";
		}
		return userTypeName;
	}
	public static String getOrderTypeName(Short orderTypeId) {
		String orderTypeName = "";
		switch (orderTypeId) {
		case 0:
			orderTypeName = "订单支付";
			break;
		case 1:
			orderTypeName = "购买充值卡";
			break;
		case 2:
			orderTypeName = "购买管家卡";
			break;
		case 3:
			orderTypeName = "订单退款";
			break;
		default:
			orderTypeName = "";
		}
		return orderTypeName;
	}
	public static String getPartnerPayTypeName(Short partnerTyp) {
		String partnerPayTypeName = "";
		switch (partnerTyp) {
		case 0:
			partnerPayTypeName = "月结";
			break;
		case 1:
			partnerPayTypeName = "按次结算";
			break;
		case 2:
			partnerPayTypeName = "预付";
			break;
		default:
			partnerPayTypeName = "";
		}
		return partnerPayTypeName;
	}
	public static String getMsgSendGroup(Short sendGroupId) {
		String msgSendGroup = "";
		switch (sendGroupId) {
		case 0:
			msgSendGroup = "全部用户";
			break;
		case 1:
			msgSendGroup = "未下单用户";
			break;
		case 2:
			msgSendGroup = "已下单用户";
			break;
		default:
			msgSendGroup = "";
		}
		return msgSendGroup;
	}
	public static String getDegreeName(String degreeId) {
		String DegreeName = "";
		switch (degreeId) {
			case "0":
				DegreeName = "小学";
				break;
			case "1":
				DegreeName = "初中";
				break;
			case "2":
				DegreeName = "高中";
				break;
			case "3":
				DegreeName = "专科";
				break;
			case "4":
				DegreeName = "本科";
				break;
			case "5":
				DegreeName = "硕士研究生";
				break;
			case "6":
				DegreeName = "博士研究生";
				break;
			default:
				DegreeName = "";
		}
		return DegreeName;
	}
	public static List<String> getDegreeType(){
		List<String> list = new ArrayList<String>();
		list.add(0,"小学");
		list.add(1,"初中");
		list.add(2,"高中");
		list.add(3,"专科");
		list.add(4,"本科");
		list.add(5,"硕士研究生");
		list.add(6,"博士研究生");
		return list;
	}
	public static String getNationName(String nameId) {
		String NationName = "";
		switch (nameId) {
		case "0":
			NationName = "汉族";
			break;
		case "1":
			NationName = "壮族";
			break;
		case "2":
			NationName = "藏族";
			break;
		case "3":
			NationName = "满族";
			break;
		case "4":
			NationName = "蒙古族";
			break;
		case "5":
			NationName = "哈萨克族";
			break;
		case "6":
			NationName = "朝鲜族";
			break;
		case "7":
			NationName = "苗族";
			break;
		default:
			NationName = "";
		}
		return NationName;
	}
	public static List<String> getNationType(){
		List<String> list = new ArrayList<String>();
		list.add(0,"汉族");
		list.add(1,"蒙古族");
		list.add(2,"回族");
		list.add(3,"壮族");
		list.add(4,"藏族");
		list.add(5,"满族");
		list.add(6,"哈萨克族");
		list.add(7,"朝鲜族");
		list.add(8,"苗族");
		list.add(9,"其他民族");

		return list;
	}
	
	//2016年2月20日15:54:30  已更新为 二期 的 所有订单类型
	public static String getJhjOrderTypeName(Short orderType) {
		String orderTypeName = "";
		switch (orderType) {
		case 0:
			orderTypeName = "钟点工";
			break;
		case 1:
			orderTypeName = "深度保洁";
			break;
		case 2:
			orderTypeName = "助理预约单";
			break;
		case 3:
			orderTypeName = "配送服务单";
			break;
		case 4:
			orderTypeName = "充值订单";
			break;
		case 5:
			orderTypeName = "提醒订单";
			break;
		case 6:	
			orderTypeName = "缴费单";	//2015-10-13 15:58:46 新增业务，充值话费、水电煤气费 类 订单
			break;
		default:
			orderTypeName = "";
		}
		return orderTypeName;
	}
	
	public static String getJhjOrderStausName(Short status) {
		String statusName = "";
		switch (status) {
			case 0:
				statusName = "已取消";
				break;
			case 1:
				statusName = "待确认";
				break;
			case 2:
				statusName = "已确认";
				break;
			case 3:
				statusName = "待支付";
				break;
			case 4:
//				statusName = "已支付";
				statusName = "待服务";
				break;
			case 5:
				statusName = "服务中";
				break;
			case 6:
				statusName = "待评价";
				break;
			case 7:
//				statusName = "已评价";
				statusName = "已完成";
				break;
			case 9:
				statusName = "已关闭";
				break;
			case 10:
				statusName = "提醒已预约";
				break;
			case 11:
				statusName = "提醒已完成";
				break;
			case 12:
				statusName = "提醒已取消";
				break;
				
			case 13:
				statusName = "充值中";
				break;
			case 14:
				statusName = "充值成功";
				break;
			case 15:
				statusName = "充值失败";
				break;
			case 16:
				statusName = "取消充值";
				break;
			default:
				statusName = "";
		}
		return statusName;
	}
	/**
	 * 根据订单类型获取订单状态
	 * @param orderType
	 * @param orderStatus
	 * @return
	 */
	public static String getJhjOrderStausNameFromOrderType(Short orderType,Short orderStatus) {
		String statusName = "";
		switch (orderType) {
			case 0://钟点工
				statusName = getHourOrderStatusName(orderStatus);
				break;
			case 1://深度保洁
				statusName = getDepOrderStatusName(orderStatus);
				break;
			case 2://助理预约单
				statusName = getAmOrderStatusName(orderStatus);
				break;
			case 3://配送服务单
				statusName = getDelOrderStatusName(orderStatus);
				break;
			default:
				statusName = "";
		}
		return statusName;
	}
	/**
	 * 钟点工订单--订单状态
	 * @return
	 */

	public static String getHourOrderStatusName(short orderStatusId){  
		String asTroName = "";
		switch (orderStatusId) {
			case 0:
				asTroName = "已取消";
				break;
			case 1:
				asTroName = "未支付";
				break;
			case 2:
				asTroName = "已支付";
				break;
			case 3:
				asTroName = "已派工";
				break;
			case 5:
				asTroName = "开始服务";
				break;
			case 7:
				asTroName = "完成服务";
				break;
			case 8:
				asTroName = "已评价";
				break;
			case 9:
				asTroName = "已关闭";
				break;
			default:
				asTroName = "";
		}
		return asTroName;
	} 
	/**
	 * 深度保洁订单状态
	 * @param orderStatusId
	 * @return
	 */
	public static String getDepOrderStatusName(short orderStatusId){  
		String asTroName = "";
		switch (orderStatusId) {
			case 0:
				asTroName = "已取消";
				break;
			case 1:
				asTroName = "未支付";
				break;
			case 2:
				asTroName = "已支付";
				break;
			case 3:
				asTroName = "已派工";
				break;
			case 5:
				asTroName = "开始服务";
				break;
			case 7:
				asTroName = "完成服务";
				break;
			case 8:
				asTroName = "已评价";
				break;
			case 9:
				asTroName = "已关闭";
				break;
			default:
				asTroName = "";
		}
		return asTroName;
	} 
	
	/**
	 * 助理订单状态
	 * @param orderStatusId
	 * @return
	 * 
	 * 		2016年3月30日10:57:24  修改  2 =已确认  3=已支付  4=已派工
	 * 
	 */
	public static String getAmOrderStatusName(short orderStatusId){  
		String asTroName = "";
		switch (orderStatusId) {
			case 0:
				asTroName = "已取消";
				break;
			case 1:
				asTroName = "已预约";
				break;
			case 2:
				asTroName = "已确认";
				break;
			case 3:
				asTroName = "已支付";
				break;
			case 4:
				asTroName = "已派工";
				break;
			case 5:
				asTroName = "开始服务";
				break;
			case 7:
				asTroName = "完成服务";
				break;
			case 9:
				asTroName = "已关闭";
				break;
			default:
				asTroName = "";
		}
		return asTroName;
	} 
	/**
	 * 配送订单状态
	 * @param orderStatusId
	 * @return
	 */
	public static String getDelOrderStatusName(short orderStatusId){  
		String asTroName = "";
		switch (orderStatusId) {
			case 0:
				asTroName = "已取消";
				break;
			case 1:
				asTroName = "未支付";
				break;
			case 2:
				asTroName = "已支付";
				break;
			case 3:
				asTroName = "已派工";
				break;
			case 5:
				asTroName = "开始服务";
				break;
			case 6:
				asTroName = "已取货";
				break;
			case 7:
				asTroName = "已送达";
				break;
			case 9:
				asTroName = "已关闭";
				break;
			default:
				asTroName = "";
		}
		return asTroName;
	} 
	
	
	public static List<String> getAstro(){  
		List<String> list = new ArrayList<String>();
		
		list.add(0,"魔羯座");
		list.add(1,"水瓶座");
		list.add(2,"双鱼座");
		list.add(3,"白羊座");
		list.add(4,"金牛座");
		list.add(5,"双子座");
		list.add(6,"巨蟹座");
		list.add(7,"狮子座");
		list.add(8,"处女座");
		list.add(9,"天秤座");
		list.add(10,"天蝎座");
		list.add(11,"射手座");
		
		return list;
	} 
	
	
	public static String getAstroName(Short astro){
		String asTroName = "";
		switch (astro) {
			case 0:
				asTroName = "魔羯座";
				break;
			case 1:
				asTroName = "水瓶座";
				break;
			case 2:
				asTroName = "双鱼座";
				break;
			case 3:
				asTroName = "白羊座";
				break;
			case 4:
				asTroName = "金牛座";
				break;
			case 5:
				asTroName = "双子座";
				break;
			case 6:
				asTroName = "巨蟹座";
				break;
			case 7:
				asTroName = "狮子座";
				break;
			case 8:
				asTroName = "处女座";
				break;
			case 9:
				asTroName = "天秤座";
				break;
			case 10:
				asTroName = "天蝎座";
				break;
			case 11:
				asTroName = "射手座";
				break;
			default:
				asTroName = "";
		}
		
		return asTroName;
	}
	
	public static List<String> getBloodType(){
		List<String> list = new ArrayList<String>();
		
		list.add(0,"O");
		list.add(1,"A");
		list.add(2,"B");
		list.add(3,"AB");
		
		return list;
	}
	
	public static String getBloodTypeName(String bloodTypeId){
		String bloodTypeName = "";
		switch (bloodTypeId) {
		case "0":
			bloodTypeName = "O";
			break;
		case "1":	
			bloodTypeName = "A";
			break;
		case "2":	
			bloodTypeName = "B";
			break;
		case "3":	
			bloodTypeName = "AB";
			break;
		default:
			break;
		}
		
		return bloodTypeName;
	}
	
	
	/*
	 * 2015-12-8 11:15:37  生成 考题选项的 序号 集合 -->{A,B,C......}
	 */
	public static List<String> generateNoForOptions(){
		
		List<String> list = new ArrayList<String>();
		
		String[] array = {
					"A","B","C","D","E","F","G",
					"H","I","J","K","L","M","N",
					"O","P","Q",	"R","S","T",
					"U","V","W",	"X","Y","Z"};
		
		list = Arrays.asList(array);
		
		return list;
	}
	
	
	/*
	 * 2015-12-30 17:16:11   问卷调查, 得到  计价方式对应的名称
	 */
	public static String getSurveyMeasureMentName(Short measure){
		
		String measureName = "";
		switch (measureName) {
		case "0":
			measureName = "月";
			break;
		case "1":	
			measureName = "年";
			break;
		case "2":	
			measureName = "次";
			break;
		case "3":	
			measureName = "免费";
			break;
		default:
			break;
		}
		
		return measureName;
	}
	
	
	/*
	 * 2016-1-7 11:06:58 
	 * 		问卷调查--计算得到 优惠系数			
	 * 	
	 * 	 sumPrice  总价格
	 * 			
	 *   amAndDeepPrice  助理和 深度保洁服务的 价格
	 */
	public static BigDecimal getSurveyRatio(BigDecimal sumPrice, BigDecimal amAndDeepPrice, short suryPayType){
		
		BigDecimal ratio = new BigDecimal(0);
		
		// 保留3位小数, 之后的 四舍五入
		Double div = MathBigDecimalUtil.div(amAndDeepPrice, sumPrice, 3).doubleValue();
		
		//switch 不支持 bigdecimal, 暂时用if/else
		if(div >= 0 && div < 0.11 ){
			
			if(suryPayType == (short)0){
				ratio = new BigDecimal(0.85);
			}else if(suryPayType == (short)1){
				ratio = new BigDecimal(0.9);
			}else{
				ratio = new BigDecimal(0.95);
			}
			
		}else if(div >=0.11 && div < 0.21){
			
			if(suryPayType == (short)0){
				ratio = new BigDecimal(0.8);
			}else if(suryPayType == (short)1){
				ratio = new BigDecimal(0.85);
			}else{
				ratio = new BigDecimal(0.9);
			}
			
		}else if(div >=0.21 && div < 0.31){
			
			if(suryPayType == (short)0){
				ratio = new BigDecimal(0.75);
			}else if(suryPayType == (short)1){
				ratio = new BigDecimal(0.8);
			}else{
				ratio = new BigDecimal(0.85);
			}
			
		}else if(div >=0.31 && div < 0.41){
			
			if(suryPayType == (short)0){
				ratio = new BigDecimal(0.7);
			}else if(suryPayType == (short)1){
				ratio = new BigDecimal(0.75);
			}else{
				ratio = new BigDecimal(0.8);
			}
			
		}else{
			
			if(suryPayType == (short)0){
				ratio = new BigDecimal(0.65);
			}else if(suryPayType == (short)1){
				ratio = new BigDecimal(0.7);
			}else{
				ratio = new BigDecimal(0.75);
			}
		}
		
		return ratio;
	}
	
	
	/*
	 * 2016年3月9日17:45:50     员工级别
	 */
	public static List<String> getStaffLevel(){
		
		List<String> list = new ArrayList<String>();
		
		list.add(0,"初级");
		list.add(1,"中级");
		list.add(2,"金牌");
		list.add(3,"VIP");
		
		return list;
	}
	
}
