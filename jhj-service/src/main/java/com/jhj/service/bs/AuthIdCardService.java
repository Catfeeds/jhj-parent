package com.jhj.service.bs;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.bs.AuthIdcard;
import com.jhj.vo.AuthIdCardSearchVo;

/**
 *
 * @author :hulj
 * @Date : 2015年7月1日上午11:16:44
 * @Description: TODO
 *
 */
public interface AuthIdCardService {
	int deleteByPrimaryKey(Long id);

    int insert(AuthIdcard record);

    int insertSelective(AuthIdcard record);

    AuthIdcard selectByPrimaryKey(Long orgId);

    int updateByPrimaryKeySelective(AuthIdcard record);
    
    int updateByPrimaryKey(AuthIdcard record);
    
    AuthIdcard initAuthIdcard();
    
	List<AuthIdcard> selectBySearchVo(AuthIdCardSearchVo searchVo);
    
	@SuppressWarnings("rawtypes")
	PageInfo selectByListPage(AuthIdCardSearchVo searchVo,int pageNo,int pageSize);
    
}
