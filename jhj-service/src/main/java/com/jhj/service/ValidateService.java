package com.jhj.service;

import com.meijia.utils.vo.AppResultData;

public interface ValidateService {

	AppResultData<Object> validateUser(Long userId);

	AppResultData<Object> validateUserAddr(Long userId, Long addrId);

	AppResultData<Object> validateServiceDate(Long serviceDate);
	
	
}