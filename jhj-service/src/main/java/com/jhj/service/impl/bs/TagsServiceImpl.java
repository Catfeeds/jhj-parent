package com.jhj.service.impl.bs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.bs.TagsMapper;
import com.jhj.po.model.bs.Tags;
import com.jhj.service.bs.TagsService;
import com.jhj.vo.TagSearchVo;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月7日下午2:40:13
 * @Description: TODO
 *
 */
@Service
public class TagsServiceImpl implements TagsService {
	
	@Autowired
	private TagsMapper tagsMapper;
	
	@Override
	public int deleteByPrimaryKey(Long tagId) {
		return tagsMapper.deleteByPrimaryKey(tagId);
	}

	@Override
	public int insert(Tags record) {
		return tagsMapper.insert(record);
	}

	@Override
	public int insertSelective(Tags record) {
		return tagsMapper.insertSelective(record);
	}

	@Override
	public Tags selectByPrimaryKey(Long tagId) {
		return tagsMapper.selectByPrimaryKey(tagId);
	}

	@Override
	public int updateByPrimaryKeySelective(Tags record) {
		return tagsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Tags record) {
		return tagsMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<Tags> selectByIds(List<Long> tagIds) {
		return tagsMapper.selectByIds(tagIds);
	}

	@Override
	public List<Tags> selectAll(Short tagType) {
		return tagsMapper.selectAll(tagType);
	}

	@Override
	public List<Tags> selectByListPage(TagSearchVo tagSearchVo,int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<Tags> list = tagsMapper.selectByListPage(tagSearchVo);
		return list;
	}

	@Override
	public Tags initTags() {
		Tags tags = new Tags();
		
		tags.setTagId(0L);
		tags.setTagName("");
		tags.setTagType((short)0);
		tags.setIsEnable((short)0);
		tags.setAddTime(TimeStampUtil.getNow() /1000);
		return tags;
	}

	@Override
	public List<Tags> selectByTagName(String name) {
		return tagsMapper.selectByTagName(name);
	}

	@Override
	public List<String> selectTagNamesByTagIds(List<Long> tagIds) {
		return tagsMapper.selectTagNamesByTagIds(tagIds);
	}

	@Override
	public List<Tags> selectList() {
		
		return tagsMapper.selectList();
	}
	
}
