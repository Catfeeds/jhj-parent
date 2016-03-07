package com.jhj.service.dict;

import java.util.List;

import com.jhj.po.model.dict.DictDegreeType;

public interface DictDegreeTypeService {
	
	List<DictDegreeType> selectByListPage(int pageNo, int pageSize);
	
	DictDegreeType  initDegreeType();
	
	int deleteByPrimaryKey(Long id);

    int insert(DictDegreeType record);

    int insertSelective(DictDegreeType record);

    DictDegreeType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DictDegreeType record);

    int updateByPrimaryKey(DictDegreeType record);
	
}
