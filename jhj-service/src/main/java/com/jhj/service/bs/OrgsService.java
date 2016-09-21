package com.jhj.service.bs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
    
    Orgs initOrgs();
    
	List<Orgs> selectBySearchVo(OrgSearchVo searchVo);
    
	@SuppressWarnings("rawtypes")
	PageInfo selectByListPage(OrgSearchVo searchVo,int pageNo,int pageSize);
    
}
