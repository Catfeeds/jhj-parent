package com.jhj.service.impl.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.service.log.LogRequestParamService;
import com.jhj.po.dao.log.LogRequestParamsMapper;
import com.jhj.po.model.log.LogRequestParams;

@Service
public class LogRequestParamServiceImpl implements LogRequestParamService {

	@Autowired
	private LogRequestParamsMapper rParamsMapper;

	@Override
	public int insert(LogRequestParams record) {
		return rParamsMapper.insert(record);
	}

	@Override
	public int insertSelective(LogRequestParams record) {
		return rParamsMapper.insertSelective(record);
	}

}
