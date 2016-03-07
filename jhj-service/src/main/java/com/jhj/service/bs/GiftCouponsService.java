package com.jhj.service.bs;

import java.util.List;

import com.jhj.po.model.bs.GiftCoupons;
import com.jhj.vo.bs.GiftCouponVo;

public interface GiftCouponsService {
	
	int deleteByPrimaryKey(Long id);
	
	int deleteByGiftId(Long giftId);

    int insert(GiftCoupons record);

    int insertSelective(GiftCoupons record);

    GiftCoupons selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GiftCoupons record);

    int updateByPrimaryKey(GiftCoupons record);
    
    List<GiftCoupons> selectByGiftId(Long giftId);
    
    GiftCoupons initGiftCoupons();

	GiftCouponVo changeToGiftCouponVo(GiftCoupons record);

	List<GiftCouponVo> changeToGiftCouponVos(List<GiftCoupons> list);

}
