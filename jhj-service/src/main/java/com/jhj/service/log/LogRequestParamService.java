package com.jhj.service.log;

import com.jhj.po.model.log.LogRequestParams;

public interface LogRequestParamService {
	 int insert(LogRequestParams record);

	 int insertSelective(LogRequestParams record);

}
