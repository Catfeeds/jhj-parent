package com.jhj.po.dao.university;

import java.util.List;

import com.jhj.vo.PartnerServiceTypeSearchVo;
import com.jhj.vo.PartnerServiceTypeVo;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.vo.app.AmSkillVo;

public interface PartnerServiceTypeMapper {
		int deleteByPrimaryKey(Long serviceTypeId);

	    int insert(PartnerServiceType record);

	    int insertSelective(PartnerServiceType record);

	    PartnerServiceType selectByPrimaryKey(Long serviceTypeId);

	    int updateByPrimaryKeySelective(PartnerServiceType record);

	    int updateByPrimaryKey(PartnerServiceType record);
	    
	    
	    List<PartnerServiceType>  selectByListPage();
	    
	    /*
	     * 得到 所有的  一级 服务（parent_id为0）,如 钟点工、助理、快送等
	     */
	    List<PartnerServiceType> selectNoParentService(); 

	    List<PartnerServiceType> selectByIds(List<Long> ids);
	    
	    //得到所有的 子服务(最底层没有子服务 )
	    List<PartnerServiceType> selectAllNoChildService();
	    
	    /* 
	     *  助理页面  技能 树，根据  员工的 技能 得到  技能的 上级 及对应的技能 
	     */
	    List<AmSkillVo> selectSkillNameAndParent(List<Long> childServiceIdList);
	    
	    List<PartnerServiceType> selectByPartnerServiceTypeVo(PartnerServiceTypeVo vo);

		List<PartnerServiceType> selectAll();

		List<PartnerServiceType> selectBySearchVo(PartnerServiceTypeSearchVo searchVo);
}