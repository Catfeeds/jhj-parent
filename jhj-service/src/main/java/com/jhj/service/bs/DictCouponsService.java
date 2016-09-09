package com.jhj.service.bs;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.vo.dict.CouponSearchVo;

public interface DictCouponsService {
	
	int deleteByPrimaryKey(Long id);

	DictCoupons selectByPrimaryKey(Long id);

	int insert(DictCoupons record);

    int insertSelective(DictCoupons record);

	int updateByPrimaryKeySelective(DictCoupons record);

	int updateByPrimaryKey(DictCoupons record);
	
	DictCoupons initRechargeCoupon();
	
	DictCoupons initConvertCoupon();

	List<DictCoupons> selectAll();

	List<DictCoupons> selectBySearchVo(CouponSearchVo couponSearchVo);

	List<DictCoupons> selectAllByCardNo();


	DictCoupons selectByCardPasswd(String cardPasswd);

	List<DictCoupons> selectByIds(List<Long> ids);

	PageInfo searchVoListPage(CouponSearchVo searchVo,short couponType,int pageNo,int pageSize);
	
	Map<String,String> getSelectRangMonthSource();
	
	Map<String,String> getSelectRangTypeSource();
	
	Map<Long,String> getSelectServiceTypeSource();
	
	Map<Long,String> getSelectRechargeCouponSource();
	
	List<DictCoupons> getCouponsByCouponType(Short couponType);

	List<DictCoupons> getSelectByMap(Map<String,Object> map);
}
