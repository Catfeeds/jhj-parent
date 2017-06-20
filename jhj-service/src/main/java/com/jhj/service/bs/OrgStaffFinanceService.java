package com.jhj.service.bs;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;
import com.jhj.vo.staff.OrgStaffIncomingVo;

/**
 *
 * @author :hulj
 * @Date : 2015年7月1日上午11:16:44
 *
 */
public interface OrgStaffFinanceService {
	int deleteByPrimaryKey(Long id);

	Long insert(OrgStaffFinance record);

	Long insertSelective(OrgStaffFinance record);

	OrgStaffFinance selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(OrgStaffFinance record);

	int updateByPrimaryKey(OrgStaffFinance record);

	OrgStaffFinance initOrgStaffFinance();

	OrgStaffFinance selectByStaffId(Long userId);

	PageInfo<OrgStaffFinance> selectByListPage(OrgStaffFinanceSearchVo searchVo, int pageNo, int pageSize);

	void orderDone(Orders orders, OrderPrices orderPrices, OrgStaffs orgStaffs);

	List<OrgStaffFinance> selectBySearchVo(OrgStaffFinanceSearchVo searchVo);

	void orderOverWork(Orders orders, OrderPriceExt orderPriceExt, OrgStaffs orgStaffs);
	
	//统计服务人员欠款
	Map<String,Object> totalMoney(OrgStaffFinanceSearchVo searchVo);

	//得到服务人员消费明细
	OrgStaffIncomingVo getStaffInComingDetail(OrgStaffs orgStaff, Orders order, OrderDispatchs orderDispatch);
	
	//计算服务人员消费明细
	OrgStaffIncomingVo calcStaffInComingDetail(OrgStaffs orgStaff, Orders order, OrderDispatchs orderDispatch);

}
