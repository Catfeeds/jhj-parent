package com.jhj.service.dict;

import java.util.List;

import com.jhj.po.model.dict.DictCardType;
import com.jhj.vo.dict.CardTypeVO;
import com.jhj.vo.dict.DictCardTypeVo;


public interface CardTypeService {

	List<DictCardType> getCardTypes();

    int deleteByPrimaryKey(Long id);

    int insert(DictCardType record);

    int insertSelective(DictCardType record);

    DictCardType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DictCardType record);

    int updateByPrimaryKey(DictCardType record);

	List<DictCardType> selectAll();

	List<DictCardTypeVo> changeToDictCardTypeVo(List<DictCardType> list);
	
	List<DictCardTypeVo> changeToDictCardTypeVo(List<DictCardType> list,Long userId);
	
	//充值送金额
	List<CardTypeVO> cardSendMoney(List<DictCardType> list);

}
