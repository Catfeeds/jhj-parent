package com.jhj.po.dao.bs;

import java.math.BigDecimal;
import java.util.List;

import com.jhj.po.model.bs.OrgStaffPayDept;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;

public interface OrgStaffPayDeptMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(OrgStaffPayDept record);

    int insertSelective(OrgStaffPayDept record);

    OrgStaffPayDept selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(OrgStaffPayDept record);

    int updateByPrimaryKey(OrgStaffPayDept record);

	OrgStaffPayDept selectByOrderNo(String orderNo);

	List<OrgStaffPayDept> selectVoByListPage(OrgStaffFinanceSearchVo searchVo);

	List<OrgStaffPayDept> selectBySearchVo(OrderSearchVo searchVo);

	BigDecimal totalBySearchVo(OrderSearchVo searchVo);
}