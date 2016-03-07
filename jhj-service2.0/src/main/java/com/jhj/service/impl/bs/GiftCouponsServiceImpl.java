package com.jhj.service.impl.bs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.bs.GiftCouponsMapper;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.bs.GiftCoupons;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.GiftCouponsService;
import com.jhj.vo.bs.GiftCouponVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.TimeStampUtil;

@Service
public class GiftCouponsServiceImpl implements GiftCouponsService {
	
	@Autowired
	private GiftCouponsMapper giftCouponsMapper;
	
	@Autowired
	private DictCouponsService dictCouponService;

	@Override
	public int deleteByPrimaryKey(Long id) {
		return giftCouponsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int deleteByGiftId(Long giftId) {
		return giftCouponsMapper.deleteByGiftId(giftId);
	}

	@Override
	public int insert(GiftCoupons record) {
		return giftCouponsMapper.insert(record);
	}

	@Override
	public int insertSelective(GiftCoupons record) {
		return giftCouponsMapper.insertSelective(record);
	}

	@Override
	public GiftCoupons selectByPrimaryKey(Long id) {
		return giftCouponsMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(GiftCoupons record) {
		return giftCouponsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(GiftCoupons record) {
		return giftCouponsMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<GiftCoupons> selectByGiftId(Long giftId) {
		return giftCouponsMapper.selectByGiftId(giftId);
	}

	@Override
	public GiftCoupons initGiftCoupons() {
		GiftCoupons giftCoupons = new GiftCoupons();
		giftCoupons.setGiftId(0L);
		giftCoupons.setCouponId(0L);
		giftCoupons.setId(0L);
		giftCoupons.setNum((short)0);
		giftCoupons.setAddTime(TimeStampUtil.getNow()/1000);
		return giftCoupons;
	}
	
	@Override
	public GiftCouponVo changeToGiftCouponVo(GiftCoupons record) {
		GiftCouponVo vo = new GiftCouponVo();
		
		Long couponId = record.getCouponId();
		DictCoupons dictCoupon = dictCouponService.selectByPrimaryKey(couponId);
		BeanUtilsExp.copyPropertiesIgnoreNull(record, vo);
		vo.setIntroduction(dictCoupon.getDescription());
		return vo;
	}
	
	@Override
	public List<GiftCouponVo> changeToGiftCouponVos(List<GiftCoupons> list) {
		List<GiftCouponVo> result = new ArrayList<GiftCouponVo>();
		
		List<DictCoupons> dictCoupons = dictCouponService.selectAll();
		
		GiftCoupons record = null;
		for (int i = 0; i < list.size(); i++) {
			record = list.get(i);
			GiftCouponVo vo = new GiftCouponVo();
		
			BeanUtilsExp.copyPropertiesIgnoreNull(record, vo);
			for (DictCoupons item : dictCoupons) {
				if (item.getId().equals(record.getCouponId())) {
					vo.setIntroduction(item.getDescription());
					vo.setValue(item.getValue());
					vo.setRangMonth(item.getRangMonth());
					vo.setServiceType(item.getServiceType());
				}
			}
			
			result.add(vo);
			
		}
		return result;
	}	

}
