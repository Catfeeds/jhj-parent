package com.jhj.service.impl.dict;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.service.bs.GiftsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.dict.CardTypeService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.vo.bs.GiftVo;
import com.jhj.vo.dict.CardTypeVO;
import com.jhj.vo.dict.DictCardTypeVo;
import com.jhj.common.Constants;
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

	/**
	 * @param List<DictCardType> 存值类型
	 * <a>该方法作用是存值金额对应得赠送金额
	 * */
	public List<CardTypeVO> cardSendMoney(List<DictCardType> list) {
		
		List<CardTypeVO> cardTypeList=new ArrayList<CardTypeVO>();
		CardTypeVO cardVo=null;
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				cardVo=new CardTypeVO();
				DictCardType dictCardType = list.get(i);
				int cardValue = dictCardType.getCardValue().intValue();
				switch (cardValue) {
					case 500:cardVo.setSendMoney(Constants.SEND_MONEY_1);break;
					case 1000:cardVo.setSendMoney(Constants.SEND_MONEY_2);break;
					case 2000:cardVo.setSendMoney(Constants.SEND_MONEY_3);break;
					case 5000:cardVo.setSendMoney(Constants.SEND_MONEY_4);break;
				default:cardVo.setSendMoney(0);break;
				}
				BeanUtils.copyProperties(dictCardType, cardVo);
				cardTypeList.add(cardVo);
			}
		}
		return cardTypeList;
	}
	
}
