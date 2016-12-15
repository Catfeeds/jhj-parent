package com.jhj.service.bs;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.jhj.po.model.bs.OrgStaffDetailPay;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.Orders;
import com.jhj.vo.staff.OrgStaffDetailPaySearchVo;
import com.jhj.vo.staff.OrgStaffPaySearchVo;
import com.jhj.vo.staff.OrgStaffPayVo;


/**
 *
 * @author :hulj
 * @Date : 2015年7月1日上午11:16:44
 * @Description: TODO
 *
 */
public interface OrgStaffDetailPayService {
	int deleteByPrimaryKey(Long id);

    int insert(OrgStaffDetailPay record);

    int insertSelective(OrgStaffDetailPay record);

    OrgStaffDetailPay selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffDetailPay record);

    int updateByPrimaryKey(OrgStaffDetailPay record);
    
    OrgStaffDetailPay initStaffDetailPay();

	List<OrgStaffDetailPay> selectByStaffIdAndTimeListPage(
			OrgStaffPaySearchVo searchVo, int pageNo, int pageSize);

	OrgStaffPayVo getOrgStaffPayVo(OrgStaffDetailPay orgStaffDetailPay);

	List<OrgStaffDetailPay> selectVoByListPage(
			OrgStaffDetailPaySearchVo searchVo, int pageNo, int pageSize);

	List<OrgStaffDetailPay> selectBySearchVo(OrgStaffDetailPaySearchVo searchVo);
	
	Map<String,Double> selectTotalData(OrgStaffDetailPaySearchVo searchVo);

	boolean setStaffDetailPay(Long staffId, String mobile, Short orderType, Long orderId, String orderNo, BigDecimal orderMoney, BigDecimal orderPay,
			String orderStatusStr, String remarks);

}
