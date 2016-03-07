package com.jhj.service.dict;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.dict.DictAd;

public interface AdService {

	DictAd initAd();

    DictAd selectByPrimaryKey(Long id);

    int insertSelective(DictAd record);

    int updateByPrimaryKeySelective(DictAd record);

	PageInfo searchVoListPage(int pageNo, int pageSize);

	int deleteByPrimaryKey(Long id);

	List<DictAd> selectByAdType(Short adType);

}
