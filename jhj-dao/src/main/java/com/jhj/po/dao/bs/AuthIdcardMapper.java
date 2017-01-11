package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.AuthIdcard;
import com.jhj.vo.AuthIdCardSearchVo;

public interface AuthIdcardMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AuthIdcard record);

    int insertSelective(AuthIdcard record);

    AuthIdcard selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AuthIdcard record);

    int updateByPrimaryKey(AuthIdcard record);
    
    List<AuthIdcard> selectBySearchVo(AuthIdCardSearchVo searchVo);
    
    List<AuthIdcard> selectByListPage(AuthIdCardSearchVo searchVo);
}