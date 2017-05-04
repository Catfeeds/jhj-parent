package com.jhj.service.impl.period;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.period.PeriodOrderMapper;
import com.jhj.po.model.period.PeriodOrder;
import com.jhj.service.period.PeriodOrderService;
import com.meijia.utils.DateUtil;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class PeriodOrderServiceImpl implements PeriodOrderService{
	
	@Autowired
	private PeriodOrderMapper periodOrderMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return periodOrderMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(PeriodOrder record) {
		return periodOrderMapper.insert(record);
	}

	@Override
	public int insertSelective(PeriodOrder record) {
		return periodOrderMapper.insertSelective(record);
	}

	@Override
	public PeriodOrder selectByPrimaryKey(Integer id) {
		return periodOrderMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(PeriodOrder record) {
		return periodOrderMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(PeriodOrder record) {
		return periodOrderMapper.updateByPrimaryKey(record);
	}

	@Override
	public PeriodOrder init() {
		PeriodOrder po = new PeriodOrder();
		po.setAddrId(0);
		po.setAddTime(TimeStampUtil.getNowSecond());
		po.setMobile("");
		po.setOrderFrom(0);
		po.setOrderMoney(new BigDecimal(0));
		po.setOrderNo(String.valueOf(OrderNoUtil.genOrderNo()));
		po.setOrderPrice(new BigDecimal(0));
		po.setOrderStatus(0);
		po.setOrderType(0);
		po.setPayType(0);
		po.setRemarks("");
		po.setPeriodServiceTypeId(0);;
		po.setUpdateTime(TimeStampUtil.getNowSecond());
		po.setUserCouponsId(0);
		po.setUserId(0);
		return po;
	}

	@Override
	public PeriodOrder selectByOrderNo(String orderNo) {
		return periodOrderMapper.selectByOrderNo(orderNo);
	}

	@Override
	public int insertBatch(List<PeriodOrder> periodOrderList) {
		return periodOrderMapper.insertBatch(periodOrderList);
	}

	@Override
	public List<PeriodOrder> periodOrderListPage(PeriodOrder periodOrder,
			int pageNum, int pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);
		List<PeriodOrder> list = periodOrderMapper.periodOrderListPage(periodOrder);
		return list;
	}

	@Override
	public List<PeriodOrder> selectByPeriodOrder(PeriodOrder periodOrder) {
	
		return periodOrderMapper.selectByPeriodOrder(periodOrder);
	}
	
	
   
}