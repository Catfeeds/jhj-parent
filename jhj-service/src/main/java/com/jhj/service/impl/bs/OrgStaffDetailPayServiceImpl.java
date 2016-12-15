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
import com.jhj.utils.OrderUtils;
import com.jhj.vo.staff.OrgStaffDetailPaySearchVo;
import com.jhj.vo.staff.OrgStaffPaySearchVo;
import com.jhj.vo.staff.OrgStaffPayVo;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.OneCareUtil;
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
		
		
		String orderTypeName = OneCareUtil.getOrderTypeForStaffDetailPay(orgStaffDetailPay.getOrderType());
		vo.setOrderTypeName(orderTypeName);
		
		// 订单金额
		if (orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_0
			|| orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_1
			|| orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_2
			|| orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_3
			|| orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_5) {
			// +号
			vo.setOrderPay("+" + orgStaffDetailPay.getOrderPay());
		}
		if (orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_4) {
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
	
	@Override
	public boolean setStaffDetailPay(Long staffId, 
			String mobile, Short orderType, Long orderId, String orderNo, 
			BigDecimal orderMoney , BigDecimal orderPay, String orderStatusStr, String remarks) {
		
		OrgStaffDetailPaySearchVo paySearchVo = new OrgStaffDetailPaySearchVo();
		paySearchVo.setStaffId(staffId);
		paySearchVo.setOrderId(orderId);
		paySearchVo.setOrderType(orderType);
		List<OrgStaffDetailPay> orgStaffDetailPays = this.selectBySearchVo(paySearchVo);
		
		if (!orgStaffDetailPays.isEmpty()) return false;
		
		OrgStaffDetailPay orgStaffDetailPay = this.initStaffDetailPay();
		// 新增收入明细表 org_staff_detail_pay
		orgStaffDetailPay.setStaffId(staffId);
		orgStaffDetailPay.setMobile(mobile);
		orgStaffDetailPay.setOrderType(orderType);
		orgStaffDetailPay.setOrderId(orderId);
		orgStaffDetailPay.setOrderNo(orderNo);
		orgStaffDetailPay.setOrderMoney(orderMoney);
		orgStaffDetailPay.setOrderPay(orderPay);
		orgStaffDetailPay.setOrderStatusStr(orderStatusStr);
		orgStaffDetailPay.setRemarks(remarks);
		this.insert(orgStaffDetailPay);
		
		return true;
	}

}
