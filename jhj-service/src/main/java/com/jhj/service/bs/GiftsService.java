package com.jhj.service.bs;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.bs.Gifts;
import com.jhj.vo.bs.GiftVo;
import com.jhj.vo.bs.GiftsSearchVo;

public interface GiftsService {
	
	int deleteByPrimaryKey(Long gitfId);

    int insert(Gifts record);

    int insertSelective(Gifts record);

    Gifts selectByPrimaryKey(Long gitfId);

    int updateByPrimaryKeySelective(Gifts record);

    int updateByPrimaryKey(Gifts record);
    
	PageInfo searchVoListPage(GiftsSearchVo searchVo,int pageNo,int pageSize);
	
	Gifts initGifts();
	
	Gifts selectByNameAndOtherId(String giftName, Long giftId);

	List<Gifts> selectAll();

	GiftVo changeToGiftVo(Gifts gift);

	List<GiftVo> changeToGiftVos(List<Gifts> list);

	List<Gifts> selectByIds(List<Long> gitfIds);


}
