package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffCash;
import com.jhj.po.model.bs.OrgStaffDetailPay;
import com.jhj.vo.OrgStaffDetailPaySearchVo;
import com.jhj.vo.staff.OrgStaffPaySearchVo;

public interface OrgStaffDetailPayMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrgStaffDetailPay record);

    int insertSelective(OrgStaffDetailPay record);

    OrgStaffDetailPay selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffDetailPay record);

    int updateByPrimaryKey(OrgStaffDetailPay record);

	List<OrgStaffDetailPay> selectByStaffIdAndTimeListPage(
			OrgStaffPaySearchVo searchVo);

	List<OrgStaffDetailPay> selectVoByListPage(OrgStaffDetailPaySearchVo searchVo);
}