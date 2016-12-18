package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffDetailDept;
import com.jhj.vo.order.OrderSearchVo;

public interface OrgStaffDetailDeptMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrgStaffDetailDept record);

    int insertSelective(OrgStaffDetailDept record);

    OrgStaffDetailDept selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffDetailDept record);

    int updateByPrimaryKey(OrgStaffDetailDept record);

	List<OrgStaffDetailDept> selectBySearchVo(OrderSearchVo searchVo);
}