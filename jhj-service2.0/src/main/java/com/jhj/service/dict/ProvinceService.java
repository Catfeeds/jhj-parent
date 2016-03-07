package com.jhj.service.dict;

import java.util.List;

import com.jhj.po.model.dict.DictProvince;

public interface ProvinceService {

	DictProvince getProvinceById(Long id);

	List<DictProvince> selectAll();

}
