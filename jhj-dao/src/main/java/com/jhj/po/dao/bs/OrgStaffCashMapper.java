package com.jhj.po.dao.bs;

import java.math.BigDecimal;
import java.util.List;

import com.jhj.po.model.bs.OrgStaffCash;
import com.jhj.vo.staff.OrgStaffCashSearchVo;

public interface OrgStaffCashMapper {
    int insert(OrgStaffCash record);

    int insertSelective(OrgStaffCash record);
    
    int deleteByPrimaryKey(Long orderId);

    OrgStaffCash selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(OrgStaffCash record);

    int updateByPrimaryKey(OrgStaffCash record);

	List<OrgStaffCash> selectByStaffId(Long userId);

	List<OrgStaffCash> selectVoByListPage(OrgStaffCashSearchVo searchVo);

	BigDecimal getTotalCashMoney(Long staffId);

}
