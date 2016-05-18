package com.jhj.service.bs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.bs.Orgs;
import com.jhj.vo.OrgSearchVo;
import com.jhj.vo.org.GroupSearchVo;

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
    List<Orgs> selectGroupsByListPage(GroupSearchVo searchVo);
    
  //选择所有门店,一级门店
    List<Orgs> selectOrgsNoParent();
    
  //选择所有 云店
    List<Orgs> selectCloudOrgs();
    
    //根据云店，得到对应的上级门店
    Orgs selectOrgByCloudOrg(Long orgId);
    
    
    /*  
     *  2016年3月25日17:50:19
     *  
     *  	选择 登录 店长 所在门店下的 云店
     */
    List<Orgs> selectCloudOrgByParentOrg(GroupSearchVo searchVo);
    
    
}
