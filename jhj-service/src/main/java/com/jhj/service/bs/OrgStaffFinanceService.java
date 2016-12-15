package com.jhj.service.bs;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年7月1日上午11:16:44
 * @Description: TODO
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

	PageInfo selectByListPage(OrgStaffFinanceSearchVo searchVo, int pageNo, int pageSize);

	void orderDone(Orders orders, OrderPrices orderPrices, OrgStaffs orgStaffs);

	List<OrgStaffFinance> selectBySearchVo(OrgStaffFinanceSearchVo searchVo);

	void orderOverWork(Orders orders, OrderPriceExt orderPriceExt, OrgStaffs orgStaffs);
	
	//统计服务人员欠款
	Map<String,Object> totalMoney(OrgStaffFinanceSearchVo searchVo);

}
