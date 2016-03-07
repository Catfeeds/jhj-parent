package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.vo.OrgStaffDetailPaySearchVo;
import com.jhj.vo.OrgStaffFinanceSearchVo;

public interface OrgStaffFinanceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrgStaffFinance record);

    int insertSelective(OrgStaffFinance record);

    OrgStaffFinance selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffFinance record);

    int updateByPrimaryKey(OrgStaffFinance record);

	OrgStaffFinance selectByStaffId(Long staffId);

	List<OrgStaffFinance> selectVoByListPage(OrgStaffFinanceSearchVo searchVo);
}