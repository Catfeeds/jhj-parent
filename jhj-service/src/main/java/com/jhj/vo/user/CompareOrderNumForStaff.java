package com.jhj.vo.user;

import java.util.Comparator;

/**
 *
 * @author :hulj
 * @Date : 2016年4月20日下午7:06:16
 * @Description: TODO
 *
 */
public class CompareOrderNumForStaff implements Comparator<UserGetAmVo>{
	
	@Override
	public int compare(UserGetAmVo o1, UserGetAmVo o2) {
		return o2.getOrderNum() - o1.getOrderNum();
	}
}
