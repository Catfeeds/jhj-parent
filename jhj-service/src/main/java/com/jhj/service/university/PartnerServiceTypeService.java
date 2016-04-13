package com.jhj.service.university;

import java.util.List;

import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.vo.bs.NewPartnerServiceVo;
import com.jhj.vo.university.OaPartnerServiceTypeVo;

public interface PartnerServiceTypeService {
	
	int deleteByPrimaryKey(Long serviceTypeId);

    int insert(PartnerServiceType record);

    int insertSelective(PartnerServiceType record);

    PartnerServiceType selectByPrimaryKey(Long serviceTypeId);

    int updateByPrimaryKeySelective(PartnerServiceType record);

    int updateByPrimaryKey(PartnerServiceType record);
	
    
    List<PartnerServiceType>  selectByListPage(int pageNo, int pageSize);
    
    PartnerServiceType  initPartner();
    
    //下拉选择
    List<PartnerServiceType> selectAll();
    
    OaPartnerServiceTypeVo completeVo(PartnerServiceType partner);
    
    /*
     * 得到 所有的  一级 服务id（parent_id为0）,如 钟点工、助理、快送等
     */
    List<Long> selectNoParentServiceId(); 
    
    /* 
     * 预加载数据
     * 	
     * 得到 所有的  一级 服务（parent_id为0）,如 钟点工、助理、快送等
     */
    List<PartnerServiceType> selectNoParentServiceObj(); 
    
    /**
     * jhj 2.1 
     * 		
     *  得到服务及其子服务
     */
    List<NewPartnerServiceVo> getTreeList();
    
    NewPartnerServiceVo	transServiceToTree(Long id);
    
    NewPartnerServiceVo initVo();
    
    List<PartnerServiceType> selectByParentId(Long id);
    
    List<Long> selectChildIdByParentId(Long id);
    
}
