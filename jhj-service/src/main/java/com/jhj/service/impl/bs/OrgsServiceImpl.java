package com.jhj.service.impl.bs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.po.dao.bs.OrgsMapper;
import com.jhj.po.model.bs.Orgs;
import com.jhj.service.bs.OrgsService;
import com.jhj.vo.org.OrgSearchVo;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月1日上午11:18:23
 * @Description: TODO
 *
 */
@Service
public class OrgsServiceImpl implements OrgsService {
	@Autowired
	private OrgsMapper orgsMapper;
	
	@Override
	public int deleteByPrimaryKey(Long orgId) {
		return orgsMapper.deleteByPrimaryKey(orgId);
	}
	
	@Override
	public int updateByPrimaryKeySelective(Orgs record) {
		return orgsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Orgs record) {
		return orgsMapper.updateByPrimaryKey(record);
	}

	@Override
	public int insert(Orgs record) {
		return orgsMapper.insert(record);
	}

	@Override
	public int insertSelective(Orgs record) {
		return orgsMapper.insertSelective(record);
	}

	@Override
	public Orgs selectByPrimaryKey(Long orgId) {
		return orgsMapper.selectByPrimaryKey(orgId);
	}

	@Override
	public List<Orgs> selectBySearchVo(OrgSearchVo searchVo) {

		return orgsMapper.selectBySearchVo(searchVo);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public PageInfo selectByListPage(OrgSearchVo searchVo,int pageNo,int pageSize) {
		/* 
		 * 代码执行顺序 
		 * 	startPage在   select。。之前才有效果
		 */
		PageHelper.startPage(pageNo, pageSize);
		List<Orgs> lists = orgsMapper.selectByListPage(searchVo);
		PageInfo result = new PageInfo(lists);
		return result;
	}

	@Override
	public Orgs initOrgs() {
		Orgs orgs = new Orgs();
		
		orgs.setOrgId(0L); // 已经在mybatis配置文件中配置了主键自增
		orgs.setOrgName("");
		orgs.setOrgTel("");
		orgs.setOrgAddr("");
		orgs.setOrgOwner("");
		orgs.setOrgOwnerTel("");
		orgs.setPoiLatitude("");
		orgs.setPoiLongitude("");
		orgs.setPoiType((short)0);
		orgs.setPoiName("");
		orgs.setPoiAddress("");
		orgs.setPoiCity("");
		orgs.setPoiUid("");
		orgs.setPoiPhone("");
		orgs.setPoiPostCode("");
		orgs.setOrgStatus((short)1);
		orgs.setAddTime(TimeStampUtil.getNow()/1000);
		orgs.setUpdateTime(0L);
		
		orgs.setParentId(0L); // 默认是 一级门店，即上级门店 id = 0
		
		return orgs;
	}

}
