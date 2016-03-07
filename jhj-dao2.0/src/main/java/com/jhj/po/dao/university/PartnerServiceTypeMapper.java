package com.jhj.po.dao.university;

import java.util.List;

import com.jhj.po.model.university.PartnerServiceType;

public interface PartnerServiceTypeMapper {
    int deleteByPrimaryKey(Long serviceTypeId);

    int insert(PartnerServiceType record);

    int insertSelective(PartnerServiceType record);

    PartnerServiceType selectByPrimaryKey(Long serviceTypeId);

    int updateByPrimaryKeySelective(PartnerServiceType record);

    int updateByPrimaryKey(PartnerServiceType record);
    
    
    List<PartnerServiceType>  selectByListPage();
    
    List<PartnerServiceType> selectAll();
    
    
    /*
     * 得到 所有的  一级 服务（parent_id为0）,如 钟点工、助理、快送等
     */
    List<PartnerServiceType> selectNoParentService(); 
    
    
}