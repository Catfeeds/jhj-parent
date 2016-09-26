package com.jhj.service.impl.dict;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.service.bs.GiftsService;
import com.jhj.service.dict.ServiceTypeService;
import com.jhj.vo.dict.DictCardTypeVo;
import com.jhj.po.dao.dict.DictCardTypeMapper;
import com.jhj.po.dao.dict.DictServiceAddonsMapper;
import com.jhj.po.dao.dict.DictServiceTypesMapper;
import com.jhj.po.model.bs.Gifts;
import com.jhj.po.model.dict.DictCardType;
import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.dict.DictServiceTypes;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.TimeStampUtil;

@Service
public class ServiceTypeServiceImpl implements ServiceTypeService {

	@Autowired
	private DictServiceTypesMapper serviceTypeMapper;

	@Autowired
	private DictCardTypeMapper dictCardTypeMapper;
	
	@Autowired
	private DictServiceAddonsMapper dictServiceAddonsMapper;
	
	@Autowired
	private GiftsService giftService;
	
	@Override
	public DictServiceTypes initServiceType() {
		DictServiceTypes record = new DictServiceTypes();

		record.setId(0L);
		record.setDescUrl("");
		record.setDisPrice(new BigDecimal(0));
		
		 /*
         * 2015-11-26 10:54:55 适应 新增活动，对数据库表  dict_service_types新增 4个字段
         * 
         * 
         */
        
        record.setSubheadHuodong("");	//(微官网) 限时活动,列表页 --副标题, 建议 30字以内（数据库设计60字）
        
		record.setSubheadAm("");   		//(微官网) 限时活动,家庭助理类型列表 页-- 副标题, 建议 50字以内（数据库设计60字）
		
		record.setServiceRelative(""); 	//(微官网) 家庭助理订单类型, 发起预约页面, 服务范围字段, 建议 200字(设计500字)
		
		record.setServiceFeature("");   //(微官网) 家庭助理订单类型, 发起预约页面, 服务特色字段, 建议 200字(设计500字)
		
		record.setKeyword("");
		record.setName("");
		record.setPrice(new BigDecimal(0));
		record.setAddTime(TimeStampUtil.getNow()/1000);
		record.setUpdateTime(0L);
		record.setEnable((short)0);
		
		//TODO
		record.setDegreeType((short)0); //(微官网)家庭助理订单类型, 列表页, 由于可能会有越来越多的
		
		record.setToDate(0L);	//（限时活动）类似的 服务类型, 需要设置截止时间时间戳, 如果是 家事、跑腿等永久性的类型,设置为0
		
		return record;
	}
	@Override
	public DictCardType initCardType() {
		DictCardType record = new DictCardType();
		
		record.setId(0L);
		record.setName("");
		record.setCardValue(new BigDecimal(0));
		record.setCardPay(new BigDecimal(0));
	    record.setDescription("");
	    record.setAddTime(TimeStampUtil.getNow()/1000);

		return record;
	}
	
	@Override
	public DictServiceAddons initServiceAdd() {
		DictServiceAddons record = new DictServiceAddons();
		
		record.setServiceAddonId(0L);
		record.setServiceType(0L);
        record.setName("");
        record.setKeyword("");
        record.setPrice(new BigDecimal(0));
        record.setDisPrice(new BigDecimal(0));
        record.setDescUrl("");
        record.setTips("");
        record.setAddTime(TimeStampUtil.getNow()/1000);
        record.setUpdateTime(0L);
        record.setEnable((short)0);

		return record;
	}


	/*
	 * 获取表dict_service_types的所有数据
	 * @param
	 * @return  List<DictServiceTypes>
	 */
	@Override
	public List<DictServiceTypes> getServiceTypes() {
		return serviceTypeMapper.selectAll();
	}

	/*
	 * 获取表dict_service_types的单条
	 * @param  id 主键id
	 * @return  DictServiceTypes
	 */
	@Override
	public DictServiceTypes getServiceTypesByPk(Long id) {
		return serviceTypeMapper.selectByPrimaryKey(id);
	}

	@Override
	public int insert(DictServiceTypes record) {
		return serviceTypeMapper.insert(record);
	}

	@Override
	public int updateByPrimaryKeySelective(DictServiceTypes record) {
		return serviceTypeMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public int insertSelect(DictCardType record) {
		
		return dictCardTypeMapper.insert(record);
		
	}

	@Override
	public int updateByidSelective(DictCardType record) {
		
		return dictCardTypeMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public PageInfo searchVoListPage(int pageNo, int pageSize) {
		 PageHelper.startPage(pageNo, pageSize);
         List<DictServiceTypes> list = serviceTypeMapper.selectByListPage();
        PageInfo result = new PageInfo(list);
		return result;
	}
	
	@Override
	public PageInfo selectAll(int pageNo, int pageSize) {
		
		PageHelper.startPage(pageNo, pageSize);
		List<DictCardType> list = dictCardTypeMapper.selectAll();
		
		
		DictCardType item = null;
		for (int i =0; i < list.size(); i++) {
			item = list.get(i);
			
			DictCardTypeVo vo = new DictCardTypeVo();
			BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
			vo.setGiftName("");
			if (item.getGiftId() > 0L) {
				Gifts gift = giftService.selectByPrimaryKey(item.getGiftId());
				if (gift == null) continue;
				vo.setGiftName(gift.getName());
			}
			list.set(i, vo);
		}
		
		
		PageInfo result = new PageInfo(list);
		
		return result;
	}

	@Override
	public int insertSelective(DictServiceTypes record) {
		return serviceTypeMapper.insert(record);
	}

	@Override
	public DictServiceTypes selectByPrimaryKey(Long id) {
		return serviceTypeMapper.selectByPrimaryKey(id);
	}

	//判断是否重复
	@Override
	public DictServiceTypes selectByName(String name) {
		return serviceTypeMapper.selectByName(name);
	}

	//判断服务类型名称是否重复
	@Override
	public DictServiceTypes selectByNameAndOtherId(String name, Long id) {

		HashMap map = new HashMap();
		map.put("id", id);
		map.put("name", name);
		return serviceTypeMapper.selectByNameAndOtherId(map);
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		return serviceTypeMapper.deleteByPrimaryKey(id);
	}
	@Override
	public DictCardType selectById(Long id) {
		
		return dictCardTypeMapper.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(Long serviceAddonId) {
		
		return dictServiceAddonsMapper.deleteByPrimaryKey(serviceAddonId);
	}
	@Override
	public int updateById(DictServiceAddons record) {
		
		return dictServiceAddonsMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public int insertServiceAddSelect(DictServiceAddons record) {
		
		return dictServiceAddonsMapper.insert(record);
	}
	
	 // 判断充值卡名称是否重复
	 
	@Override
	public DictCardType selectByCardNameAndOtherId(String name, Long id) {

		HashMap map = new HashMap();
		map.put("name", name);
		map.put("id", id);
		return dictCardTypeMapper.selectByNameAndOtherId(map);
	}
	
	@Override
	public DictServiceTypes selectByServiceType(Long serviceType) {
		return serviceTypeMapper.selectByServiceType(serviceType);
	}
}
