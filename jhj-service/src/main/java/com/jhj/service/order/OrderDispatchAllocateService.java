package com.jhj.service.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderDispatchVo;
import com.jhj.vo.order.OrgStaffDispatchVo;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年7月20日下午5:16:54
 * @Description: TODO
 *
 */
public interface OrderDispatchAllocateService {

	List<OrgStaffDispatchVo> manualDispatchToday(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, Long sessionOrgId, Long selectedOrgId);

	List<OrgStaffDispatchVo> manualDispatchNotToday(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, Long selectParentId, Long selectOrgId);

	List<OrgStaffDispatchVo> manualDispatchTodayByOrg(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, Long selectParentId,
			Long selectOrgId);

	List<OrgStaffDispatchVo> manualDispatchByOrg(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, Long selectParentId, Long selectOrgId);

	List<OrgStaffDispatchVo> manualDispatchNotTodayByOrg(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, Long selectParentId,
			Long selectOrgId);
    

}
