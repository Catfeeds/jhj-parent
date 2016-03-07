package com.jhj.service.impl.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.service.dict.AdService;
import com.jhj.po.dao.dict.DictAdMapper;
import com.jhj.po.model.dict.DictAd;
import com.meijia.utils.TimeStampUtil;

@Service
public class AdServiceImpl implements AdService {

	@Autowired
	private DictAdMapper adMapper;

	/*
	 * 获取表dict_ad的数据，通过serviceType
	 * @param
	 * @return  List<DictServiceTypes>
	 */


	@Override
	public PageInfo searchVoListPage( int pageNo, int pageSize ) {

		PageHelper.startPage(pageNo, pageSize);
        List<DictAd> list = adMapper.selectByListPage();
//        List<DictAd> listNew = new ArrayList<DictAd>();
//        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
//			DictAd dictAd = (DictAd) iterator.next();
//			DictAdVo dicAdNew = new DictAdVo();
//			 String imgUrl = dictAd.getImgUrl();
//			 
//			 
//			 
////			 if(!imgUrl.equals("")){
////				 String extensionName = imgUrl.substring(imgUrl.lastIndexOf("."));
////	             String beforName = imgUrl.substring(0,(imgUrl.lastIndexOf(".")));
////	             String newImgUrl = beforName+"_small"+extensionName;
////	             dictAd.setImgUrl(newImgUrl);
////			 }
//             
//             listNew.add(dictAd);
//		}
//        for(int i = 0; i < list.size(); i++) {
//       	 if (listNew.get(i) != null) {
//       		 list.set(i, listNew.get(i));
//       	 }
//        }

        PageInfo result = new PageInfo(list);
        
		return result;
	}
	@Override
	public DictAd selectByPrimaryKey(Long id) {
		return adMapper.selectByPrimaryKey(id);
	}
	@Override
	public List<DictAd> selectByAdType(Short adType) {
		return adMapper.selectByAdType(adType);
	}
	@Override
	public int insertSelective(DictAd record) {
		return adMapper.insert(record);
	}
	@Override
	public DictAd initAd() {
		DictAd record = new DictAd();
		record.setId(0L);
		record.setNo((short)0);
		record.setImgUrl("");
		record.setGotoUrl("");
		record.setAdType((short) 0);
		record.setAddTime(TimeStampUtil.getNow()/1000);
		record.setUpdateTime(0L);
		record.setEnable((short)0);

		return record;
	}
	@Override
	public int deleteByPrimaryKey(Long id) {
		return adMapper.deleteByPrimaryKey(id);
	}
	@Override
	public int updateByPrimaryKeySelective(DictAd record) {
		return adMapper.updateByPrimaryKeySelective(record);
	}

}
