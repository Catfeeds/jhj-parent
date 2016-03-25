package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.Orgs;
import com.jhj.vo.OrgSearchVo;
import com.jhj.vo.org.GroupSearchVo;

public interface OrgsMapper {
	int deleteByPrimaryKey(Long orgId);

    int insert(Orgs record);

    int insertSelective(Orgs record);

    Orgs selectByPrimaryKey(Long orgId);

    int updateByPrimaryKeySelective(Orgs record);

    int updateByPrimaryKey(Orgs record);
    
    //2016年3月8日10:11:48  该方法已修改为取得 所有“小组”，不再支持门店
    List<Orgs> selectAll();
    
    List<Orgs> selectByListPage(OrgSearchVo orgSearchVo);
    
    List<Orgs> selectByOrgName(String orgName);
    
    
    Orgs selectByPoiAddress(String poiAddress);
    
    //2016年3月7日14:50:14  云店管理
    List<Orgs> selectGroupsByListPage(GroupSearchVo searchVo);
    
    //选择所有门店,一级门店
    List<Orgs> selectOrgsNoParent();
    
    //选择所有 云店
    List<Orgs> selectCloudOrgs();
    
    //根据云店，得到对应的上级门店
    Orgs selectOrgByCloudOrg(Long orgId);
}