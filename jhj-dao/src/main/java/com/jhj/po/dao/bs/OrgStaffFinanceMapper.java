package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;

public interface OrgStaffFinanceMapper {
    int deleteByPrimaryKey(Long id);

    Long insert(OrgStaffFinance record);

    Long insertSelective(OrgStaffFinance record);

    OrgStaffFinance selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffFinance record);

    int updateByPrimaryKey(OrgStaffFinance record);

	OrgStaffFinance selectByStaffId(Long staffId);

	List<OrgStaffFinance> selectVoByListPage(OrgStaffFinanceSearchVo searchVo);
}