package com.jhj.service.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.vo.OrgStaffFinanceSearchVo;


/**
 *
 * @author :hulj
 * @Date : 2015年7月1日上午11:16:44
 * @Description: TODO
 *
 */
public interface OrgStaffFinanceService {
	int deleteByPrimaryKey(Long id);

    int insert(OrgStaffFinance record);

    int insertSelective(OrgStaffFinance record);

    OrgStaffFinance selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffFinance record);

    int updateByPrimaryKey(OrgStaffFinance record);
    
    OrgStaffFinance initOrgStaffFinance();

	OrgStaffFinance selectByStaffId(Long userId);

	List<OrgStaffFinance> selectByListPage(OrgStaffFinanceSearchVo searchVo,
			int pageNo, int pageSize);

}
