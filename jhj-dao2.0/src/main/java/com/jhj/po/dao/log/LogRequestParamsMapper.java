package com.jhj.po.dao.log;

import com.jhj.po.model.log.LogRequestParams;

public interface LogRequestParamsMapper {
    int insert(LogRequestParams record);

    int insertSelective(LogRequestParams record);
}