package com.jhj.service.impl.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.service.dict.ProvinceService;
import com.jhj.po.dao.dict.DictProvinceMapper;
import com.jhj.po.model.dict.DictProvince;

@Service
public class ProvinceServiceImpl implements ProvinceService {

	@Autowired
	private DictProvinceMapper provinceMapper;

	/*
	 * 获取表dict_province的数据，通过provinceId
	 * @param
	 * @return  List<DictProvince>
	 */
	@Override
	public DictProvince getProvinceById(Long id) {
		return provinceMapper.selectByPrimaryKey(id);
	}

	/*
	 * 获取表dict_province的数据
	 * @param
	 * @return  List<DictProvince>
	 */
	@Override
	public List<DictProvince> selectAll() {
		return provinceMapper.selectAll();
	}
}
