package com.jhj.service.impl.period;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.period.PeriodOrderMapper;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.period.PeriodOrder;
import com.jhj.po.model.period.PeriodOrderAddons;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.period.PeriodOrderAddonsService;
import com.jhj.service.period.PeriodOrderService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.utils.OrderUtils;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.period.PeriodOrderAddonsVo;
import com.jhj.vo.period.PeriodOrderDetailVo;
import com.jhj.vo.period.PeriodOrderSearchVo;
import com.jhj.vo.period.PeriodOrderVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class PeriodOrderServiceImpl implements PeriodOrderService{
	
	@Autowired
	private PeriodOrderMapper periodOrderMapper;
	
	@Autowired
	private UserAddrsService userAddrService;
	
	@Autowired
	private OrderQueryService orderQueryService;
	
	@Autowired
	private PeriodOrderAddonsService periodOrderAddonsService;

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
		po.setPackageType(0);
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
	public List<PeriodOrder> selectByListPage(PeriodOrderSearchVo searchVo,
			int pageNum, int pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);
		List<PeriodOrder> list = periodOrderMapper.selectByListPage(searchVo);
		return list;
	}

	@Override
	public List<PeriodOrder> selectBySearchVo(PeriodOrderSearchVo searchVo) {
	
		return periodOrderMapper.selectBySearchVo(searchVo);
	}
	
	@Override
	public PeriodOrderVo getVos(PeriodOrder item) {
		PeriodOrderVo vo = new PeriodOrderVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
		
		//套餐名称
		Integer packageType = vo.getPackageType();
		String packageTypeName = "套餐" + StringUtil.getZhNum(String.valueOf(packageType));
		vo.setPackageTypeName(packageTypeName);
		
		//地址名称
		String addrName = "";
		Long addrId = Long.valueOf(vo.getAddrId());
		UserAddrs userAddr = userAddrService.selectByPrimaryKey(addrId);
		if (userAddr != null) {
			addrName = userAddr.getName() + " " + userAddr.getAddr();
		}
		vo.setAddrName(addrName);
		
		//订单状态
		String orderStatusName = this.getOrderStatusName(vo.getOrderStatus());
		vo.setOrderStatusName(orderStatusName);
		
		//支付类型名称
		String payTypeName = OrderUtils.getPayTypeName(vo.getOrderStatus().shortValue(), vo.getPayType().shortValue());
		vo.setPayTypeName(payTypeName);
		
		//订单来源名称
		String orderFromName = OneCareUtil.getOrderFromName( vo.getOrderFrom().shortValue() );
		vo.setOrderFromName(orderFromName);
		//获取已录入的订单
		int totalOrder = 0;
		OrderSearchVo orderSearchVo = new OrderSearchVo();
		orderSearchVo.setPeriodOrderId(vo.getId());
		List<Orders> list = orderQueryService.selectBySearchVo(orderSearchVo);
		if (list.isEmpty()) totalOrder = list.size();
		vo.setTotalOrder(totalOrder);
		
		return vo;
	}
	
	@Override
	public PeriodOrderDetailVo getDetailVo(PeriodOrder item) {
		PeriodOrderDetailVo detailVo = new PeriodOrderDetailVo();
		PeriodOrderVo vo = this.getVos(item);
		BeanUtilsExp.copyPropertiesIgnoreNull(vo, detailVo);
		
		Integer periodOrderId = detailVo.getId();
		//找出选择的服务子项
		List<PeriodOrderAddons> periodOrderAddons = periodOrderAddonsService.selectByPeriodOrderId(periodOrderId);
		List<PeriodOrderAddonsVo> PeriodOrderAddonsVos = new ArrayList<PeriodOrderAddonsVo>();
		for (int i = 0; i < periodOrderAddons.size(); i++) {
			PeriodOrderAddons periodOrderAddon = periodOrderAddons.get(i);
			PeriodOrderAddonsVo periodOrderAddonsVo = periodOrderAddonsService.transVo(periodOrderAddon);
			PeriodOrderAddonsVos.add(periodOrderAddonsVo);
		}
		detailVo.setPeriodOrderAddons(PeriodOrderAddonsVos);
		return detailVo;
	}
	
	@Override
	public String getOrderStatusName(Integer orderStatus) {
		String statusName = "";
		switch (orderStatus) {
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
				statusName = "未完成";
				break;
			case 4:
				statusName = "已完成";
				break;
			default:
				statusName = "";
		}
		return statusName;
	}
   
}