package com.jhj.service.impl.cooperate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.cooperate.CooperativeBusinessMapper;
import com.jhj.po.model.cooperate.CooperativeBusiness;
import com.jhj.service.cooperate.CooperateBusinessService;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2016年4月1日下午3:20:12
 * @Description: 
 *	
 *		第三方登录商户
 */
@Service
public class CooperateBusinessImpl implements CooperateBusinessService {
	
	@Autowired
	private CooperativeBusinessMapper cooMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return cooMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(CooperativeBusiness record) {
		return cooMapper.insert(record);
	}

	@Override
	public int insertSelective(CooperativeBusiness record) {
		return cooMapper.insertSelective(record);
	}

	@Override
	public CooperativeBusiness selectByPrimaryKey(Long id) {
		return cooMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(CooperativeBusiness record) {
		return cooMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(CooperativeBusiness record) {
		return cooMapper.updateByPrimaryKey(record);
	}
	
	@Override
	public CooperativeBusiness initCooBus() {
		
		CooperativeBusiness business = new CooperativeBusiness();
		
		business.setId(0L);
		business.setBusinessName("");
		business.setAppName("");
		business.setLoginName("");
		business.setPassWord("");
		business.setAddTime(TimeStampUtil.getNowSecond());
		business.setUpdateTime(TimeStampUtil.getNowSecond());
		
//		business.setEnable((short)1);	//是否可用 ，1=是 0=否
//		business.setRoleId(0L);
		
		return business;
	}
	
	
	@Override
	public List<CooperativeBusiness> selectByListPage() {
		return new ArrayList<CooperativeBusiness>();
//		return cooMapper.selectByListPage();
	}
	
}
