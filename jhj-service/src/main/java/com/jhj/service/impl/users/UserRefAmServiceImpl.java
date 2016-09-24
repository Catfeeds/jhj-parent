package com.jhj.service.impl.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.user.UserRefAmMapper;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserRefAm;
import com.jhj.po.model.user.Users;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrderStatService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.AmSearchListVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.user.UsersListsVo;

@Service
public class UserRefAmServiceImpl implements UserRefAmService {

	@Autowired
	private UserRefAmMapper userRefAmMapper;

	@Autowired
	private UsersService usersService;

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private UserAddrsService userAddrsService;
	
	@Autowired
	private OrderQueryService orderQueryService;
	
	@Autowired
	private OrderStatService orderStatService;

	@Override
	public UserRefAm selectByAmId(Long userId) {

		return userRefAmMapper.selectByUserId(userId);
	}

	@Override
	public List<HashMap> totalByAmIds(List<Long> amIds) {
		return userRefAmMapper.totalByAmIds(amIds);
	}

	@Override
	public int insert(UserRefAm record) {
		return userRefAmMapper.insert(record);
	}

	@Override
	public List<UserRefAm> selectUserListByAmId(Long staffId, int pageNo,
			int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		AmSearchListVo amSearchListVo = new AmSearchListVo();
		amSearchListVo.setStaffId(staffId);
		List<UserRefAm> list = userRefAmMapper.selectUserListByAmId(amSearchListVo);
		return list;
	}

	@Override
	public UsersListsVo getUserList(UserRefAm userRefAm) {

		UsersListsVo vo = new UsersListsVo();
		BeanUtils.copyProperties(userRefAm, vo);

		// 用户手机号
		vo.setMobile("");
		if (vo.getUserId() > 0L) {
			Users user = usersService.selectByPrimaryKey(vo.getUserId());
			
			if (user == null) {
				return vo;
			}
			vo.setMobile(user.getMobile());
		}
		
		// 用户地址
		vo.setServiceAddr("");

		List<UserAddrs> lists = userAddrsService.selectByUserId(vo.getUserId());
		List<Long> userIds = new ArrayList<Long>();
		for (UserAddrs item : lists) {
			userIds.add(item.getId());
		}
		Collections.sort(userIds);
		UserAddrs userAddrs = userAddrsService.selectByPrimaryKey(userIds.get(0));
		vo.setServiceAddr(userAddrs.getName()+userAddrs.getAddr());
		
		// 服务次数
		vo.setServiceTimes("0");
		// 1.获得用户列表
		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setAmId(vo.getStaffId());
		List<Orders> list = orderQueryService.selectBySearchVo(searchVo);

		// 2.获得用户对应的ids
		for (Orders item : list) {
			userIds.add(item.getUserId());
		}
		// 3.获得ids的数量
		List<HashMap> counts = new ArrayList<HashMap>();
		if (!userIds.isEmpty()) {
			counts = orderStatService.totalByUserIds(userIds);
		}

		for (HashMap serviceCounts : counts) {

			Long userId = Long.valueOf(serviceCounts.get("user_id").toString());
			if (userId.equals(vo.getUserId())) {
				vo.setServiceTimes(serviceCounts.get("total").toString());
			}
		}

		return vo;
	}

	
	/*
	 * 助理名下 所有用户
	 */
	@Override
	public List<UserRefAm> selectAllUserByAmId(Long amId) {
		
		List<UserRefAm> list = userRefAmMapper.selectAllUserByAmId(amId);
		
		return list;
	}

	@Override
	public UserRefAm selectByUserId(Long userId) {
		return userRefAmMapper.selectByUserId(userId);
	}


	@Override
	public int insertSelective(UserRefAm record) {
		return userRefAmMapper.insertSelective(record);
	}

	@Override
	public void delectByStaffId(Map<String, Object> map) {
		userRefAmMapper.delectByStaffId(map);
	}

	@Override
	public List<UserRefAm> selectByUserIdAndAmId(Map<String, Object> map) {
		return userRefAmMapper.selectByUserIdAndAmId(map);
	}

}
