package com.jhj.po.dao.bs;

import java.util.List;
import java.util.Map;

import com.jhj.po.model.bs.DictCoupons;

public interface DictCouponsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DictCoupons record);

    int insertSelective(DictCoupons record);

    DictCoupons selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DictCoupons record);

    int updateByPrimaryKey(DictCoupons record);

	List<DictCoupons> selectAll();
	
	List<DictCoupons> selectAllByCardNo();

	List<DictCoupons> selectByIds(List<Long> ids);

	DictCoupons selectByCardPasswd(String cardPasswd);

	List<DictCoupons> selectByListPage(Map<String,Object> conditions);
	
	List<DictCoupons> selectByCouponType(Short couponType);
	
	List<DictCoupons> selectByMap(Map<String,Object> map);
}