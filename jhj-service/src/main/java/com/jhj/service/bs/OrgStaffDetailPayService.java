package com.jhj.service.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffDetailPay;
import com.jhj.vo.OrgStaffDetailPaySearchVo;
import com.jhj.vo.staff.OrgStaffPaySearchVo;
import com.jhj.vo.staff.OrgStaffPayVo;


/**
 *
 * @author :hulj
 * @Date : 2015年7月1日上午11:16:44
 * @Description: TODO
 *
 */
public interface OrgStaffDetailPayService {
	int deleteByPrimaryKey(Long id);

    int insert(OrgStaffDetailPay record);

    int insertSelective(OrgStaffDetailPay record);

    OrgStaffDetailPay selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffDetailPay record);

    int updateByPrimaryKey(OrgStaffDetailPay record);
    
    OrgStaffDetailPay initStaffDetailPay();

	List<OrgStaffDetailPay> selectByStaffIdAndTimeListPage(
			OrgStaffPaySearchVo searchVo, int pageNo, int pageSize);

	OrgStaffPayVo getOrgStaffPayVo(OrgStaffDetailPay orgStaffDetailPay);

	List<OrgStaffDetailPay> selectVoByListPage(
			OrgStaffDetailPaySearchVo searchVo, int pageNo, int pageSize);

	List<OrgStaffDetailPay> selectBySearchVo(OrgStaffDetailPaySearchVo searchVo);

}
