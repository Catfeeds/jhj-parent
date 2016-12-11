package com.jhj.service.bs;

import java.math.BigDecimal;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.bs.OrgStaffCash;
import com.jhj.vo.staff.OrgStaffCashSearchVo;
import com.jhj.vo.staff.OrgStaffCashVo;


/**
 *
 * @author :hulj
 * @Date : 2015年7月1日上午11:16:44
 * @Description: TODO
 *
 */
public interface OrgStaffCashService {
	int deleteByPrimaryKey(Long id);

    int insert(OrgStaffCash record);

    int insertSelective(OrgStaffCash record);

    OrgStaffCash selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(OrgStaffCash record);

    int updateByPrimaryKey(OrgStaffCash record);
    
    OrgStaffCash initOrgStaffCash();

	List<OrgStaffCash> selectByStaffId(Long userId);

	PageInfo selectByListPage(OrgStaffCashSearchVo searchVo, int pageNo, int pageSize);

	OrgStaffCashVo transVo(OrgStaffCash orgStaffCash);

	BigDecimal getTotalCashMoney(OrgStaffCashSearchVo searchVo);

	List<OrgStaffCash> selectBySearchVo(OrgStaffCashSearchVo searchVo);

	

}
