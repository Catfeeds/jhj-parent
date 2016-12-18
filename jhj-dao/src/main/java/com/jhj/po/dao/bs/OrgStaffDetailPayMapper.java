package com.jhj.po.dao.bs;

import java.util.List;
import java.util.Map;

import com.jhj.po.model.bs.OrgStaffDetailPay;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.staff.OrgStaffPaySearchVo;

public interface OrgStaffDetailPayMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrgStaffDetailPay record);

    int insertSelective(OrgStaffDetailPay record);

    OrgStaffDetailPay selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffDetailPay record);

    int updateByPrimaryKey(OrgStaffDetailPay record);

	List<OrgStaffDetailPay> selectByListPage(OrderSearchVo searchVo);

	List<OrgStaffDetailPay> selectBySearchVo(OrderSearchVo searchVo);
	
	Map<String,Double> selectTotalData(OrderSearchVo searchVo);
}