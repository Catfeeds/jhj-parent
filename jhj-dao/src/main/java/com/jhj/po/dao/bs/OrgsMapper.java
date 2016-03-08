package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.Orgs;
import com.jhj.vo.OrgSearchVo;

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
    
    //2016年3月7日14:50:14  小组管理
    List<Orgs> selectGroupsByListPage();
}