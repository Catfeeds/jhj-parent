package com.jhj.po.dao.dict;

import java.util.List;

import com.jhj.po.model.dict.DictDegreeType;

public interface DictDegreeTypeMapper {

	int deleteByPrimaryKey(Long id);

    int insert(DictDegreeType record);

    int insertSelective(DictDegreeType record);

    DictDegreeType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DictDegreeType record);

    int updateByPrimaryKey(DictDegreeType record);
    
    
    
    List<DictDegreeType> selectByListPage();
    
}