package com.jhj.service.dict;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.dict.DictCardType;
import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.dict.DictServiceTypes;

public interface ServiceTypeService {

	List<DictServiceTypes> getServiceTypes();

	int insert(DictServiceTypes record);

    int insertSelective(DictServiceTypes record);

    DictServiceTypes selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DictServiceTypes record);

    int deleteByPrimaryKey(Long id);

	DictServiceTypes getServiceTypesByPk(Long id);

	PageInfo searchVoListPage( int pageNo, int pageSize);

	DictServiceTypes initServiceType();

	DictServiceTypes selectByName(String name);

	DictServiceTypes selectByNameAndOtherId(String name, Long id);

	PageInfo selectAll(int pageNo, int pageSize);

	DictCardType initCardType();

	DictCardType selectById(Long id);

	int updateByidSelective(DictCardType record);

	int insertSelect(DictCardType record);


	int deleteById(Long id);

	int updateById(DictServiceAddons record);

	int insertServiceAddSelect(DictServiceAddons record);

	DictCardType selectByCardNameAndOtherId(String name, Long id);

	DictServiceTypes  selectByServiceType(Long serviceType);
}
