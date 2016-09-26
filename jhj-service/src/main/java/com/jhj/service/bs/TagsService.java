package com.jhj.service.bs;

import java.util.List;

import com.jhj.po.model.bs.Tags;
import com.jhj.vo.TagSearchVo;

/**
 *
 * @author :hulj
 * @Date : 2015年7月7日下午2:39:23
 * @Description: TODO
 *
 */
public interface TagsService {
	int deleteByPrimaryKey(Long tagId);

    int insert(Tags record);

    int insertSelective(Tags record);

    Tags selectByPrimaryKey(Long tagId);

    int updateByPrimaryKeySelective(Tags record);

    int updateByPrimaryKey(Tags record);
    
    List<Tags> selectByListPage(TagSearchVo tagSearchVo,int pageNo,int pageSize);

	Tags initTags();

	List<Tags> selectBySearchVo(TagSearchVo tagSearchVo);
	
}
