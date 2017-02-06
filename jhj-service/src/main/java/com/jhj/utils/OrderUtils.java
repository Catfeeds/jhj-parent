package com.jhj.utils;

import java.text.ParseException;

import com.jhj.common.Constants;
import com.jhj.po.model.cooperate.CooperativeBusiness;
import com.jhj.vo.order.OaOrderListVo;

public class OrderUtils {
	// 获得订单类型名称
	public static String getOrderTypeName(Short orderType) {

		String orderTypeName = "";

		if (orderType.equals(Constants.ORDER_TYPE_0)) {
			orderTypeName = "钟点工";
		}
		if (orderType.equals(Constants.ORDER_TYPE_1)) {
			orderTypeName = "深度保洁";
		}
		if (orderType.equals(Constants.ORDER_TYPE_2)) {
			orderTypeName = "助理预约单";
		}
		if (orderType.equals(Constants.ORDER_TYPE_3)) {
			orderTypeName = "配送服务单";
		}
		if (orderType.equals(Constants.ORDER_TYPE_4)) {
			orderTypeName = "充值订单";
		}
		if (orderType.equals(Constants.ORDER_TYPE_5)) {
			orderTypeName = "订单提醒";
		}
		if (orderType.equals(Constants.ORDER_TYPE_6)) {
			orderTypeName = "缴单费";
		}
		return orderTypeName;

	}

	// 获得订单状态名称

	public static String getOrderStatusName(Short orderType, Short orderStatus) {
		String orderStatusName = "";
		// 1. 深度保洁0. 钟点工
		if (orderType == 0 || orderType == 1) {
			if (orderStatus.equals(Constants.ORDER_STATUS_0)) {
				orderStatusName = "已取消";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_1)) {
				orderStatusName = "未支付";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_2)) {
				orderStatusName = "已支付";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_3)) {
				orderStatusName = "已派工";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_5)) {
				orderStatusName = "开始服务";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_7)) {
				orderStatusName = "完成服务";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_8)) {
				orderStatusName = "已评价";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_9)) {
				orderStatusName = "已关闭";
			}
		}
		// 2. 助理预约单
		if (orderType == 2) {
			if (orderStatus.equals(Constants.ORDER_STATUS_0)) {
				orderStatusName = "已取消";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_1)) {
				orderStatusName = "已预约";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_2)) {
				orderStatusName = "已派工";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_3)) {
				orderStatusName = "已确认";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_4)) {
				orderStatusName = "已支付";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_5)) {
				orderStatusName = "开始服务";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_7)) {
				orderStatusName = "完成服务";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_9)) {
				orderStatusName = "已关闭";
			}
		}
		// 3. 配送服务
		if (orderType == 3) {
			if (orderStatus.equals(Constants.ORDER_STATUS_0)) {
				orderStatusName = "已取消";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_1)) {
				orderStatusName = "未支付";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_2)) {
				orderStatusName = "已支付";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_3)) {
				orderStatusName = "已派工";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_5)) {
				orderStatusName = "开始服务";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_6)) {
				orderStatusName = "已取货";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_7)) {
				orderStatusName = "已送达";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_9)) {
				orderStatusName = "已关闭";
			}
		}

		// 4. 充值订单
		if (orderType == 4) {
			if (orderStatus.equals(Constants.ORDER_STATUS_12)) {
				orderStatusName = "提醒已取消";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_10)) {
				orderStatusName = "提醒已预约";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_11)) {
				orderStatusName = "提醒已完成";
			}
		}
		// 5. 提醒订单
		if (orderType == 5) {
			if (orderStatus.equals(Constants.ORDER_STATUS_13)) {
				orderStatusName = "充值中";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_14)) {
				orderStatusName = "充值成功";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_15)) {
				orderStatusName = "充值失败";
			}
			if (orderStatus.equals(Constants.ORDER_STATUS_16)) {
				orderStatusName = "取消充值";
			}
		}

		return orderStatusName;

	}

	// 根据订单状态和类型判断，button_word上的文字
	public static String getButtonWordName(Short orderType, Short orderStatus) {
		String buttonWordName = "";
		// 0,1钟点工和深度保洁
		if (orderType == 0 || orderType == 1) {
			if (orderStatus == 3) {
				buttonWordName = "开始服务";
			}
			if (orderStatus == 5) {
				buttonWordName = "完成服务";
			}
			if (orderStatus == 7) {
				buttonWordName = "已完成";
			}
			if (orderStatus == 8) {
				buttonWordName = "已关闭";
			}
		}
		// 2.助理预约单
		if (orderType == 2) {
			if (orderStatus == 2) {
				buttonWordName = "调整订单";
			}

			if (orderStatus == 3) {
				buttonWordName = "等待支付";
			}

			if (orderStatus == 4) {
				buttonWordName = "开始服务";
			}
			if (orderStatus == 5) {
				buttonWordName = "完成服务";
			}
			if (orderStatus == 7) {
				buttonWordName = "已完成";
			}
			if (orderStatus == 8) {
				buttonWordName = "已关闭";
			}
		}
		// 3.配送服务单
		if (orderType == 3) {
			if (orderStatus == 3) {
				buttonWordName = "开始服务";
			}
			if (orderStatus == 5) {
				buttonWordName = "已取货";
			}
			if (orderStatus == 6) {
				buttonWordName = "已送达";
			}
			if (orderStatus == 7) {
				buttonWordName = "已关闭";
			}
		}
		return buttonWordName;

	}

	// 根据订单状态和类型判断，button_word上的文字
	public static String getPayTypeName(Short orderStatus, Short payType) {
		String paytypeName = "";

		if (orderStatus == 1) {

			paytypeName = "未支付";

		} else {
			if (payType.equals((short) 0)) {

				paytypeName = "余额支付";

			}
			if (payType.equals((short) 1)) {

				paytypeName = "支付宝";

			}
			if (payType.equals((short) 2)) {

				paytypeName = "微信支付";

			}
			if (payType.equals((short) 3)) {

				paytypeName = "智慧支付";

			}
			if (payType.equals((short) 6)) {

				paytypeName = "现金支付";

			}
		}

		return paytypeName;
	}

	// 订单来源
	public static OaOrderListVo isOrderSrc(short orderFrom, Long orderOpFrom, OaOrderListVo vo, CooperativeBusiness cooperativeBusiness) {
		if (orderFrom == 0) {
			vo.setOrderOpFromName("app");
		}
		if (orderFrom == 1) {
			vo.setOrderOpFromName("微网站");
		}
		if (orderFrom == 2 && orderOpFrom != null) {
			if (orderOpFrom == 1) {
				vo.setOrderOpFromName("来电订单");
			} else {
				if (cooperativeBusiness != null) {
					vo.setOrderOpFromName(cooperativeBusiness.getBusinessName());
				} else {
					vo.setOrderOpFromName("");
				}
			}
		}

		// if(orderOpFrom!=null){
		// if(orderOpFrom==1){
		// oaOrderListVo.setOrderOpFromName("来电订单");
		// }else{
		// CooperativeBusiness cooperativeBusiness =
		// cooperateBusinessService.selectByPrimaryKey(orderOpFrom);
		// if (cooperativeBusiness != null) {
		// oaOrderListVo.setOrderOpFromName(cooperativeBusiness.getBusinessName());
		// }
		// }
		// }
		return vo;
	}

	// 订单来源
	public static String getOrderFromName(short orderFrom, Long orderOpFrom, CooperativeBusiness cooperativeBusiness) {
		String orderFromName = "";
		if (orderFrom == 0) {
			orderFromName = "app";
		}
		if (orderFrom == 1) {
			orderFromName = "微网站";
		}
		if (orderFrom == 2 && orderOpFrom != null) {
			if (orderOpFrom == 1) {
				orderFromName = "来电订单";
			} else {
				if (cooperativeBusiness != null) {
					orderFromName = cooperativeBusiness.getBusinessName();
				} 
			}
		}

		return orderFromName;
	}

	// 获得订单分成比例名称
	public static String getOrderSettingType(Short orderType) {

		String settingType = "";

		if (orderType.equals(Constants.ORDER_TYPE_0)) {
			settingType = "hour-ratio";
		}
		if (orderType.equals(Constants.ORDER_TYPE_1)) {
			settingType = "deep-ratio";
		}
		if (orderType.equals(Constants.ORDER_TYPE_2)) {
			settingType = "am-ratio";
		}
		if (orderType.equals(Constants.ORDER_TYPE_3)) {
			settingType = "dis-ratio";
		}

		return settingType;

	}

	public static void main(String[] args) throws ParseException {

		Short status = 5;
		Short patType = 6;

		System.out.println(OrderUtils.getPayTypeName(status, patType));
	}
}
