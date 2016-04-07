package com.jhj.service.impl.cooperate;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.cooperate.CooperativeBusinessMapper;
import com.jhj.po.model.admin.AdminAccount;
import com.jhj.po.model.cooperate.CooperativeBusiness;
import com.jhj.service.cooperate.CooperateBusinessService;
import com.jhj.vo.cooperate.CooperateVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.StringUtil;
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
		business.setBusinessLoginName("");
		business.setBusinessPassWord("");
		business.setAddTime(TimeStampUtil.getNowSecond());
		business.setUpdateTime(TimeStampUtil.getNowSecond());
		
		business.setEnable((short)1);	//是否可用 ，1=是 0=否
		business.setRoleId(0L);		//商户 的 “角色”, 控制权限
		
		return business;
	}
	
	@Override
	public List<CooperativeBusiness> selectByListPage() {
		return cooMapper.selectByListPage();
	}
	
	@Override
	public CooperativeBusiness selectByLogInName(String logInName) {
		return cooMapper.selectBylogInName(logInName);
	}
	 
	@Override
	public CooperateVo transToFormVo(CooperativeBusiness business) {
		
		CooperateVo vo = new CooperateVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(business, vo);
		
		vo.setConfirmPassWord(business.getBusinessPassWord());
		 
		return vo;
	}
	
	@Override
	public CooperativeBusiness login(String loginName, String passWord) throws NoSuchAlgorithmException {
		
		HashMap<String,String> conditions = new HashMap<String,String>();

		conditions.put("username", loginName);
		conditions.put("password", StringUtil.md5(passWord.trim()));
//		AdminAccount po = adminAccountMapper.selectByUsernameAndPassword(conditions);
		
		CooperativeBusiness business = cooMapper.selectByUsernameAndPassword(conditions);
		
		return business;
	}
}
