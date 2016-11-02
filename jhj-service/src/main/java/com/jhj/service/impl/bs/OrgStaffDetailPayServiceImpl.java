package com.jhj.service.impl.bs;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.common.Constants;
import com.jhj.po.dao.bs.OrgStaffDetailPayMapper;
import com.jhj.po.model.bs.OrgStaffDetailPay;
import com.jhj.service.bs.OrgStaffDetailPayService;
import com.jhj.vo.staff.OrgStaffDetailPaySearchVo;
import com.jhj.vo.staff.OrgStaffPaySearchVo;
import com.jhj.vo.staff.OrgStaffPayVo;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月6日下午2:44:25
 * @Description:
 *
 */
@Service
public class OrgStaffDetailPayServiceImpl implements OrgStaffDetailPayService {

	@Autowired
	private OrgStaffDetailPayMapper orgStaffDetailPayMapper;

	@Override
	public int deleteByPrimaryKey(Long id) {

		return orgStaffDetailPayMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrgStaffDetailPay record) {

		return orgStaffDetailPayMapper.insert(record);
	}

	@Override
	public int insertSelective(OrgStaffDetailPay record) {

		return orgStaffDetailPayMapper.insertSelective(record);
	}

	@Override
	public OrgStaffDetailPay selectByPrimaryKey(Long id) {

		return orgStaffDetailPayMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrgStaffDetailPay record) {

		return orgStaffDetailPayMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrgStaffDetailPay record) {

		return orgStaffDetailPayMapper.updateByPrimaryKey(record);
	}

	@Override
	public OrgStaffDetailPay initStaffDetailPay() {

		OrgStaffDetailPay record = new OrgStaffDetailPay();

		record.setId(0L);
		record.setStaffId(0L);
		record.setMobile("");
		record.setOrderType(Constants.ORDER_TYPE_0);
		record.setOrderId(0L);
		record.setOrderNo("");
		record.setRemarks("");
		record.setOrderMoney(new BigDecimal(0));
		record.setOrderPay(new BigDecimal(0));
		record.setOrderStatusStr("");
		record.setAddTime(TimeStampUtil.getNowSecond());

		return record;
	}

	@Override
	public List<OrgStaffDetailPay> selectByStaffIdAndTimeListPage(
			OrgStaffPaySearchVo searchVo, int pageNo, int pageSize) {

		PageHelper.startPage(pageNo, pageSize);

		List<OrgStaffDetailPay> list = orgStaffDetailPayMapper
				.selectByStaffIdAndTimeListPage(searchVo);

		return list;
	}

	@Override
	public OrgStaffPayVo getOrgStaffPayVo(OrgStaffDetailPay orgStaffDetailPay) {
		OrgStaffPayVo vo = new OrgStaffPayVo();
		vo.setStaffId(orgStaffDetailPay.getStaffId());
		// 用户手机号
		vo.setMobile(orgStaffDetailPay.getMobile());
		// 发生时间
		Long addTime = orgStaffDetailPay.getAddTime() * 1000;
		vo.setAddTimeStr(TimeStampUtil.timeStampToDateStr(addTime, "MM-dd HH:mm"));
		
		

		// 交易来源名称
		if (orgStaffDetailPay.getOrderType() == 0
				|| orgStaffDetailPay.getOrderType() == 1
				|| orgStaffDetailPay.getOrderType() == 2
				|| orgStaffDetailPay.getOrderType() == 3) {
			BigDecimal orderMoney = orgStaffDetailPay.getOrderMoney();
			String orderMoneyStr = MathBigDecimalUtil.round2(orderMoney);
			vo.setOrderTypeName("订单收入,订单金额:" + orderMoneyStr);
		}
		if (orgStaffDetailPay.getOrderType() == 4)
			vo.setOrderTypeName("还款金额");
		if (orgStaffDetailPay.getOrderType() == 5)
			vo.setOrderTypeName("提现金额");
		if (orgStaffDetailPay.getOrderType() == 6)
			vo.setOrderTypeName("补贴金额");
		if (orgStaffDetailPay.getOrderType() == 7)
			vo.setOrderTypeName("利息金额");
		if (orgStaffDetailPay.getOrderType() == 8)
			vo.setOrderTypeName("各项核检");
		if (orgStaffDetailPay.getOrderType() == 9)
			vo.setOrderTypeName("订单补时");
		// 订单金额
		if (orgStaffDetailPay.getOrderType() == 0
			|| orgStaffDetailPay.getOrderType() == 1
			|| orgStaffDetailPay.getOrderType() == 2
			|| orgStaffDetailPay.getOrderType() == 3
			|| orgStaffDetailPay.getOrderType() == 6
			|| orgStaffDetailPay.getOrderType() == 7
			|| orgStaffDetailPay.getOrderType() == 9) {
			// +号
			vo.setOrderPay("+" + orgStaffDetailPay.getOrderPay());
		}
		if (orgStaffDetailPay.getOrderType() == 4
			|| orgStaffDetailPay.getOrderType() == 8
			|| orgStaffDetailPay.getOrderType() == 5) {
			// -号
			vo.setOrderPay("-" + orgStaffDetailPay.getOrderPay());
		}
		
		return vo;
	}

	@Override
	public List<OrgStaffDetailPay> selectVoByListPage(
			OrgStaffDetailPaySearchVo searchVo, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<OrgStaffDetailPay> list = orgStaffDetailPayMapper.selectVoByListPage(searchVo);
	
		return list;
				 
	}
	
	@Override
	public List<OrgStaffDetailPay> selectBySearchVo(OrgStaffDetailPaySearchVo searchVo) {		
		return orgStaffDetailPayMapper.selectBySearchVo(searchVo);
	}

	@Override
	public Map<String, Double> selectTotalData(OrgStaffDetailPaySearchVo searchVo) {
		return orgStaffDetailPayMapper.selectTotalData(searchVo);
	}

}
