package com.jhj.po.dao.dict;

import com.jhj.po.model.dict.DictSeniorType;

public interface DictSeniorTypeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DictSeniorType record);

    int insertSelective(DictSeniorType record);

    DictSeniorType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DictSeniorType record);

    int updateByPrimaryKey(DictSeniorType record);
}