package com.jhj.service.bs;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.bs.Orgs;
import com.jhj.vo.OrgSearchVo;

/**
 *
 * @author :hulj
 * @Date : 2015年7月1日上午11:16:44
 * @Description: TODO
 *
 */
public interface OrgsService {
	int deleteByPrimaryKey(Long orgId);

    int insert(Orgs record);

    int insertSelective(Orgs record);

    Orgs selectByPrimaryKey(Long orgId);

    int updateByPrimaryKeySelective(Orgs record);

    int updateByPrimaryKey(Orgs record);
    
    List<Orgs> selectAll();
    
    PageInfo selectByListPage(OrgSearchVo orgSearchVo,int pageNo,int pageSize);
    
    Orgs initOrgs();
    
    List<Orgs> selectByOrgName(String orgName);
    
    Orgs selectByPoiAddress(String poiAddress);
    
    //2016年3月7日14:50:14  小组管理
    List<Orgs> selectGroupsByListPage();
}
