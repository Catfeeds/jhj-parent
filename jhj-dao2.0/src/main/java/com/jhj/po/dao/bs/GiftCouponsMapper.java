package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.GiftCoupons;

public interface GiftCouponsMapper {
    int deleteByPrimaryKey(Long id);

    int deleteByCouponId(Long couponId);
   
    int deleteByGiftId(Long couponId);

    int insert(GiftCoupons record);

    int insertSelective(GiftCoupons record);

    GiftCoupons selectByPrimaryKey(Long id);
    
    List<GiftCoupons> selectByGiftId(Long giftId);

    int updateByPrimaryKeySelective(GiftCoupons record);

    int updateByPrimaryKey(GiftCoupons record);
}