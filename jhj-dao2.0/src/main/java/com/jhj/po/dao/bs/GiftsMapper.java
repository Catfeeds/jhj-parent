package com.jhj.po.dao.bs;

import java.util.List;
import java.util.Map;

import com.jhj.po.model.bs.Gifts;

public interface GiftsMapper {
    int deleteByPrimaryKey(Long gitfId);

    int insert(Gifts record);

    int insertSelective(Gifts record);

    Gifts selectByPrimaryKey(Long gitfId);

    int updateByPrimaryKeySelective(Gifts record);

    int updateByPrimaryKey(Gifts record);
    List<Gifts> selectByListPage(Map<String,Object> conditions);
    
    
    Gifts selectByNameAndOtherId(Map<String,Object> conditions);

	List<Gifts> selectAll();

	List<Gifts> selectByIds(List<Long> gitfIds);
}