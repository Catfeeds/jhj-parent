package com.jhj.service.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;


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

	List<OrgStaffFinance> selectByListPage(OrgStaffFinanceSearchVo searchVo,
			int pageNo, int pageSize);

	void orderDone(Orders orders, OrderPrices orderPrices, OrgStaffs orgStaffs);

}
