package com.jhj.service.impl.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.dict.DictDegreeTypeMapper;
import com.jhj.po.model.dict.DictDegreeType;
import com.jhj.service.dict.DictDegreeTypeService;
import com.meijia.utils.TimeStampUtil;

@Service
public class DictDegreeTypeServiceImpl implements DictDegreeTypeService {

	@Autowired
	private DictDegreeTypeMapper degreeMapper;
	
	@Override
	public List<DictDegreeType> selectByListPage(int pageNo, int pageSize) {
		
		PageHelper.startPage(pageNo, pageSize);
		
		List<DictDegreeType> list = degreeMapper.selectByListPage();
		
		return list;
	}

	@Override
	public DictDegreeType initDegreeType() {
		
		DictDegreeType type = new DictDegreeType();
		
		
		type.setId(0L);
		type.setName("");
		type.setEnable((short)1);
		type.setAddTime(TimeStampUtil.getNowSecond());
		type.setUpdateTime(TimeStampUtil.getNowSecond());
		
		return type;
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		return degreeMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(DictDegreeType record) {
		return degreeMapper.insert(record);
	}

	@Override
	public int insertSelective(DictDegreeType record) {
		return degreeMapper.insertSelective(record);
	}

	@Override
	public DictDegreeType selectByPrimaryKey(Long id) {
		return degreeMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(DictDegreeType record) {
		return degreeMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(DictDegreeType record) {
		return degreeMapper.updateByPrimaryKey(record);
	}

}
