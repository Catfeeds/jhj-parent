package com.jhj.service.impl.bs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.po.dao.bs.GiftsMapper;
import com.jhj.po.model.bs.GiftCoupons;
import com.jhj.po.model.bs.Gifts;
import com.jhj.service.bs.GiftCouponsService;
import com.jhj.service.bs.GiftsService;
import com.jhj.vo.bs.GiftCouponVo;
import com.jhj.vo.bs.GiftVo;
import com.jhj.vo.bs.GiftsSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class GiftsServiceImpl implements GiftsService {

	@Autowired
	private GiftsMapper giftsMapper;
	
	@Autowired
	private GiftCouponsService giftCouponService;
	
	@Override
	public int deleteByPrimaryKey(Long gitfId) {
		return giftsMapper.deleteByPrimaryKey(gitfId);
	}

	@Override
	public int insert(Gifts record) {
		return giftsMapper.insert(record);
	}

	@Override
	public int insertSelective(Gifts record) {
		return giftsMapper.insertSelective(record);
	}

	@Override
	public Gifts selectByPrimaryKey(Long gitfId) {
		return giftsMapper.selectByPrimaryKey(gitfId);
	}
	
	@Override
	public List<Gifts> selectByIds(List<Long> gitfIds) {
		return giftsMapper.selectByIds(gitfIds);
	}	
	
	@Override
	public List<Gifts> selectAll() {
		return giftsMapper.selectAll();
	}	

	@Override
	public int updateByPrimaryKeySelective(Gifts record) {
		return giftsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Gifts record) {
		return giftsMapper.updateByPrimaryKey(record);
	}

	@Override
	public PageInfo searchVoListPage(GiftsSearchVo searchVo, int pageNo, int pageSize) {
		
		Map<String,Object> conditions = new HashMap<String,Object>();
		String name = searchVo.getName();

		if(name !=null && !name.isEmpty()){
			conditions.put("name",name.trim());
		}
		conditions.put("name", name);

		 PageHelper.startPage(pageNo, pageSize);
         List<Gifts> list = giftsMapper.selectByListPage(conditions);
        PageInfo result = new PageInfo(list);
		return result;
	}

	@Override
	public Gifts initGifts() {
		Gifts gifts = new Gifts();
		gifts.setGiftId(0L);
		gifts.setName("");
		gifts.setRangeMonth((short)1);
		gifts.setAddTime(TimeStampUtil.getNow()/1000);
		gifts.setUpdateTime(TimeStampUtil.getNow()/1000);
		return gifts;
	}

	@Override
	public Gifts selectByNameAndOtherId(String giftName, Long giftId) {

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("giftId",giftId);
		map.put("name",giftName);
		return 	giftsMapper.selectByNameAndOtherId(map);
	}
	
	@Override 
	public GiftVo changeToGiftVo(Gifts gift) {
		
		GiftVo vo = new GiftVo();
		BeanUtilsExp.copyPropertiesIgnoreNull(gift, vo);
		
		List<GiftCoupons> giftCoupons = giftCouponService.selectByGiftId(gift.getGiftId());
		
		if (giftCoupons.isEmpty()) return vo;
		
		List<GiftCouponVo> giftCouponVos = giftCouponService.changeToGiftCouponVos(giftCoupons);
		
		if (giftCouponVos.isEmpty()) giftCouponVos = new ArrayList<GiftCouponVo>();
		
		vo.setGiftCoupons(giftCouponVos);
		BigDecimal totalGiftMoney = new BigDecimal(0);
		for (GiftCouponVo item : giftCouponVos) {
			BigDecimal num = new BigDecimal(item.getNum().intValue());
			BigDecimal itemTotalMoney = MathBigDecimalUtil.mul(item.getValue(), num);
			totalGiftMoney = MathBigDecimalUtil.add(totalGiftMoney, itemTotalMoney);
		}
		vo.setTotalGiftMoney(totalGiftMoney);
		return vo;
	}
	
	@Override 
	public List<GiftVo> changeToGiftVos(List<Gifts> list) {
		List<GiftVo> result = new ArrayList<GiftVo>();
		for (int i = 0; i < list.size(); i++) {
			GiftVo vo = changeToGiftVo(list.get(i));
			result.add(vo);
		}
		
		return result;
	}	
	

}
