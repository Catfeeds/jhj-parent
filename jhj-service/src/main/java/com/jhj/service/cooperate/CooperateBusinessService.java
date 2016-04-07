package com.jhj.service.cooperate;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.jhj.po.model.cooperate.CooperativeBusiness;
import com.jhj.vo.cooperate.CooperateVo;

/**
 *
 * @author :hulj
 * @Date : 2016年4月1日下午3:18:57
 * @Description: 
 *
 */
public interface CooperateBusinessService {
	
	int deleteByPrimaryKey(Long id);

    int insert(CooperativeBusiness record);

    int insertSelective(CooperativeBusiness record);

    CooperativeBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CooperativeBusiness record);

    int updateByPrimaryKey(CooperativeBusiness record);
	
    List<CooperativeBusiness> selectByListPage();
    
    CooperativeBusiness initCooBus();
    
    CooperativeBusiness selectByLogInName(String logInName);
    
    CooperateVo transToFormVo(CooperativeBusiness business);
    
    CooperativeBusiness login(String loginName,String passWord) throws NoSuchAlgorithmException  ;
}
