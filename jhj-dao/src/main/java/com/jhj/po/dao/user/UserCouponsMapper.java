package com.jhj.po.dao.user;

import java.util.List;

import com.jhj.po.model.user.UserCoupons;

public interface UserCouponsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserCoupons record);

    int insertSelective(UserCoupons record);

    UserCoupons selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserCoupons record);

    int updateByPrimaryKey(UserCoupons record);
    
    List<UserCoupons> selectByUserId(Long userId);

    List<UserCoupons> selectByCouponId(Long couponId);

    UserCoupons selectByOrderNo(String orderNo);

    List<UserCoupons> selectByCouponIdAndUserId(Long couponId,Long userId);

    List<UserCoupons> selectByCouponIdNotUserId(Long couponId,Long userId);

	List<UserCoupons> selectAllByUserId(Long userId);
	
	
	//该用户是否有  注册大礼包
	List<Long> selectLoginGift(Long userId);
	
	//批量插入数据
	int insertByList(List<UserCoupons> userCouponsList);
	
    
}