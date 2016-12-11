package com.jhj.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.Users;
import com.jhj.service.ValidateService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UsersService;
import com.meijia.utils.vo.AppResultData;

@Service
public class ValidateServiceImpl implements ValidateService {

	@Autowired
	private UsersService userService;

	@Autowired
	private UserAddrsService userAddrService;

	// 验证用户是否存在
	@Override
	public AppResultData<Object> validateUser(Long userId) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		Users u = userService.selectByPrimaryKey(userId);

		if (u == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}

		return result;
	}

	// 验证是否为用户的地址
	@Override
	public AppResultData<Object> validateUserAddr(Long userId, Long addrId) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		if (addrId.equals(0L)) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_ADDR_NOT_EXIST_MG);
			return result;
		} else {
			UserAddrs userAddr = userAddrService.selectByPrimaryKey(addrId);

			if (userAddr == null) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.USER_ADDR_NOT_EXIST_MG);
				return result;
			}

			if (!userAddr.getUserId().equals(userId)) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.USER_ADDR_NOT_EXIST_MG);
				return result;
			}
		}
		return result;
	}
	
	//验证服务日期
	@Override
	public AppResultData<Object> validateServiceDate(Long serviceDate) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		Long nowTimeStamp =  new Date().getTime()/1000;
		
		if(nowTimeStamp >= serviceDate){
			
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.OLD_TIME);
			return result;
		}
		
		return result;
	}
}
