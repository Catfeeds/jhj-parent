package com.jhj.service.impl.dict;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.service.bs.GiftsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.dict.CardTypeService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.vo.bs.GiftVo;
import com.jhj.vo.dict.DictCardTypeVo;
import com.jhj.po.dao.dict.DictCardTypeMapper;
import com.jhj.po.model.bs.Gifts;
import com.jhj.po.model.dict.DictCardType;
import com.meijia.utils.BeanUtilsExp;

@Service
public class CardTypeServiceImpl implements CardTypeService {

	@Autowired
	private DictCardTypeMapper cardTypeMapper;
	
	@Autowired
	private GiftsService giftService;
	
	@Autowired
	private UserRefAmService userRefAmService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;

	/*
	 * 获取表dict_card_types的数据
	 * @param
	 * @return  List<DictCardType>
	 */
	@Override
	public List<DictCardType> getCardTypes() {
		return cardTypeMapper.selectAll();
	}

	@Autowired
	private DictCardTypeMapper dictCardTypeMapper;

	@Override
	public int deleteByPrimaryKey(Long id) {
		return dictCardTypeMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(DictCardType record) {
		return dictCardTypeMapper.insert(record);
	}

	@Override
	public int insertSelective(DictCardType record) {
		return dictCardTypeMapper.insertSelective(record);
	}

	@Override
	public List<DictCardType> selectAll() {
		return dictCardTypeMapper.selectAll();
	}

	@Override
	public DictCardType selectByPrimaryKey(Long id) {
		return dictCardTypeMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(DictCardType record) {
		return dictCardTypeMapper.updateByPrimaryKey(record);
	}

	@Override
	public int updateByPrimaryKeySelective(DictCardType record) {
		return dictCardTypeMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public List<DictCardTypeVo> changeToDictCardTypeVo(List<DictCardType> list) {
		List<DictCardTypeVo> result = new ArrayList<DictCardTypeVo>();
		
		DictCardType item = null;
		List<Long> giftIds = new ArrayList<Long>();
		for (int i = 0; i < list.size(); i++) {
			item = list.get(i);
			if (item.getGiftId() > 0L) giftIds.add(item.getGiftId());
		}
		
		List<GiftVo> giftVos = new ArrayList<GiftVo>();
		if (!giftIds.isEmpty()) {
			List<Gifts> gifts = giftService.selectByIds(giftIds);
			giftVos = giftService.changeToGiftVos(gifts);			
		}

		for (int i = 0; i < list.size(); i++) {
			item = list.get(i);
			DictCardTypeVo vo = new DictCardTypeVo();
			BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
			//GiftVo defaultGift = new GiftVo();
			//vo.setGift(defaultGift);
			vo.setGiftName("");
			for (GiftVo record : giftVos) {
				if (record.getGiftId().equals(vo.getGiftId())) {
					vo.setGift(record);
					vo.setGiftName(record.getName());;
				}
			}
			result.add(vo);
		}		
		
		return result;
	}
	@Override
	public List<DictCardTypeVo> changeToDictCardTypeVo(List<DictCardType> list,Long userId) {
		List<DictCardTypeVo> result = new ArrayList<DictCardTypeVo>();
		
		DictCardType item = null;
		List<Long> giftIds = new ArrayList<Long>();
		for (int i = 0; i < list.size(); i++) {
			item = list.get(i);
			if (item.getGiftId() > 0L) giftIds.add(item.getGiftId());
		}
		
		List<GiftVo> giftVos = new ArrayList<GiftVo>();
		if (!giftIds.isEmpty()) {
			List<Gifts> gifts = giftService.selectByIds(giftIds);
			giftVos = giftService.changeToGiftVos(gifts);			
		}
		
		for (int i = 0; i < list.size(); i++) {
			item = list.get(i);
			DictCardTypeVo vo = new DictCardTypeVo();
			BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
			//GiftVo defaultGift = new GiftVo();
			//vo.setGift(defaultGift);
			vo.setGiftName("");
			for (GiftVo record : giftVos) {
				if (record.getGiftId().equals(vo.getGiftId())) {
					vo.setGift(record);
					vo.setGiftName(record.getName());;
				}
			}
			result.add(vo);
		}		
		return result;
	}
	
}
