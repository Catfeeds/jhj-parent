package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.Tags;
import com.jhj.vo.TagSearchVo;

public interface TagsMapper {
    int deleteByPrimaryKey(Long tagId);

    int insert(Tags record);

    int insertSelective(Tags record);

    Tags selectByPrimaryKey(Long tagId);

    int updateByPrimaryKeySelective(Tags record);

    int updateByPrimaryKey(Tags record);
    
    List<Tags> selectByIds(List<Long> tagIds);
    
    List<Tags> selectAll(Short tagType);
    
    List<Tags> selectByListPage(TagSearchVo tagSearchVo);
    
    List<Tags>  selectByTagName(String name);
    
    List<String> selectTagNamesByTagIds(List<Long> tagIds);

	List<Tags> selectList();
}