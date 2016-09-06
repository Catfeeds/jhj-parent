package com.jhj.service.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffBlack;
import com.jhj.vo.OrgStaffDetailPaySearchVo;


/**
 *
 * @author :hulj
 * @Date : 2015年7月1日上午11:16:44
 * @Description: TODO
 *
 */
public interface OrgStaffBlackService {
	int deleteByPrimaryKey(Long id);

    int insert(OrgStaffBlack record);

    int insertSelective(OrgStaffBlack record);

    OrgStaffBlack selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffBlack record);

    int updateByPrimaryKey(OrgStaffBlack record);
    
    OrgStaffBlack initOrgStaffBlack();

	OrgStaffBlack selectByStaffId(Long userId);

	List<OrgStaffBlack> selectByListPage(OrgStaffDetailPaySearchVo searchVo, int pageNo, int pageSize);

	List<OrgStaffBlack> selectByStaffIdAndType(Long staffId);

/*	List<OrgStaffBlack> selectByStaffId(Long userId);

	List<OrgStaffBlack> selectVoByListPage(OrgStaffBlackSearchVo searchVo,
			int pageNo, int pageSize);*/

	
	//差评 服务人员 集合
	List<Long> selectAllBadRateStaffId();
	
	List<OrgStaffBlack> selectAllBadRateStaff();
}
