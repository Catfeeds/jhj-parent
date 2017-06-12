package com.jhj.service.users;

import java.util.List;

import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.share.OrderShare;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.vo.user.UserCouponVo;
import com.jhj.vo.user.UserCouponsVo;
import com.meijia.utils.vo.AppResultData;


public interface UserCouponsService {
	
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

	UserCoupons initUserCoupons();

	UserCouponVo changeToUserCouponVo(UserCoupons record);

	List<UserCouponVo> changeToUserCouponVos(List<UserCoupons> list);

	AppResultData<Object> validateCouponForPay(Long userId, Long userCouponId, Long orderId);

	AppResultData<Object> validateCouponForPost(Users u, DictCoupons dictCoupon);

	List<UserCoupons> searchVoListPage(Long id, int pageNo, int pageSize);

	UserCouponsVo getUsersCounps(UserCoupons userCoupons);

	List<UserCoupons> selectAllByUserId(Long userId);
    
	//该用户是否有  注册大礼包
	List<Long> selectLoginGift(Long userId);
	
	int insertByList(List<UserCoupons> userCouponsList);
	
	//为对象
	UserCoupons initUserCoupons(Long userId,DictCoupons coupons);
	
	List<UserCoupons> selectByUserCoupons(UserCoupons userCoupons);
	
	boolean shareSuccessSendCoupons(OrderShare orderShare,Long userId);
}
