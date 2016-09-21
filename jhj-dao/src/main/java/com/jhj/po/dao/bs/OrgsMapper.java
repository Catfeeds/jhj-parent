package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.Orgs;
import com.jhj.vo.OrgSearchVo;

public interface OrgsMapper {
	int deleteByPrimaryKey(Long orgId);

	int insert(Orgs record);

	int insertSelective(Orgs record);

	int updateByPrimaryKeySelective(Orgs record);

	int updateByPrimaryKey(Orgs record);
	
	Orgs selectByPrimaryKey(Long orgId);

	List<Orgs> selectByListPage(OrgSearchVo orgSearchVo);

	List<Orgs> selectBySearchVo(OrgSearchVo orgSearchVo);
}