package com.jhj.service.impl.bs;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.po.dao.bs.DictCouponsMapper;
import com.jhj.po.dao.dict.DictServiceTypesMapper;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.dict.DictServiceTypes;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.vo.dict.CouponSearchVo;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class DictCouponsServiceImpl implements DictCouponsService {

	@Autowired
	private DictCouponsMapper dictCouponsMapper;
	
	@Autowired
	private DictServiceTypesMapper dictServiceTypesMapper;


	/*
	 * 获取表dict_coupons的所有数据
	 * @param
	 * @return  List<DictServiceTypes>
	 */
	@Override
	public List<DictCoupons> selectAll() {
		return dictCouponsMapper.selectAll();
	}

	@Override
	public List<DictCoupons> selectByIds(List<Long> ids) {
		return dictCouponsMapper.selectByIds(ids);
	}

	@Override
	public DictCoupons selectByPrimaryKey(Long id) {
		return dictCouponsMapper.selectByPrimaryKey(id);
	}

	@Override
	public DictCoupons selectByCardPasswd(String cardPasswd) {
		return dictCouponsMapper.selectByCardPasswd(cardPasswd);
	}

	@Override
	public int insert(DictCoupons record) {
		return dictCouponsMapper.insert(record);
	}

	@Override
	public int updateByPrimaryKeySelective(DictCoupons record) {
		return dictCouponsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
    public int updateByPrimaryKey(DictCoupons record) {
		return dictCouponsMapper.updateByPrimaryKey(record);
	}

	@Override
	public DictCoupons initRechargeCoupon() {

		DictCoupons po = new DictCoupons();
		po.setId(0L);
		po.setCardNo("");
		po.setCardPasswd("");
		po.setCouponType((short)1);
		po.setValue(new BigDecimal(0));
		po.setMaxValue(new BigDecimal(0));
		po.setRangType((short) 0);
		po.setRangFrom((short) 999);
		po.setServiceType("0");
		po.setIntroduction("");
		po.setDescription("");
		po.setRangMonth((short)1);
		po.setFromDate(new Date());
		po.setAddTime(TimeStampUtil.getNow() / 1000);
		po.setUpdateTime(TimeStampUtil.getNow() / 1000);
		po.setIsValid("1");
		return po;
	}
	@Override
	public DictCoupons initConvertCoupon() {
		
		DictCoupons po = new DictCoupons();
		po.setId(0L);
		po.setCardNo("");
		po.setCardPasswd("");
		po.setCouponType((short)0);
		po.setValue(new BigDecimal(0));
		po.setMaxValue(new BigDecimal(0));
		po.setRangType((short) 0);
		po.setRangFrom((short) 999);
		po.setServiceType("0");
		po.setIntroduction("");
		po.setDescription("");
		po.setRangMonth((short)0);
		po.setFromDate(new Date());
		Long seconds = TimeStampUtil.getMillisOfDate(new Date())/1000;
		seconds = seconds +2592000;
		Date date =TimeStampUtil.timeStampToDateFull(seconds*1000, "yyyy-MM-dd");
		po.setToDate(date);
		po.setAddTime(TimeStampUtil.getNow() / 1000);
		po.setUpdateTime(TimeStampUtil.getNow() / 1000);
		po.setIsValid("1");
		return po;
	}

	@Override
	public PageInfo searchVoListPage(CouponSearchVo searchVo,short couponType,
			int pageNo, int pageSize) {
		HashMap<String,Object> conditions = new HashMap<String,Object>();
		String cardNo = searchVo.getCardNo();
		 String cardPasswd = searchVo.getCardPasswd();
		 String startDate = searchVo.getStartDate();
		String endDate = searchVo.getEndDate();

		if(cardPasswd !=null && !cardPasswd.isEmpty() ){
			conditions.put("cardPasswd", cardPasswd.trim());
		}
		if(cardNo !=null && !cardNo.isEmpty()){
			conditions.put("cardNo",cardNo.trim());
		}
		if(!StringUtil.isEmpty(startDate)){
			conditions.put("startDate",TimeStampUtil.getMillisOfDay(startDate.trim())/1000);
		}
		if(!StringUtil.isEmpty(endDate)){
			conditions.put("endDate",TimeStampUtil.getMillisOfDay(endDate.trim())/1000);
		}
		conditions.put("couponType", couponType);

		 PageHelper.startPage(pageNo, pageSize);
         List<DictCoupons> list = dictCouponsMapper.selectByListPage(conditions);
        PageInfo result = new PageInfo(list);
		return result;
	}

	@Override
	public List<DictCoupons> selectAllByCardNo() {
		return dictCouponsMapper.selectAllByCardNo();
	}

	@Override
	public int insertSelective(DictCoupons record) {
		return dictCouponsMapper.insertSelective(record);
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		return dictCouponsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Map<String, String> getSelectRangMonthSource() {
		Map<String, String> rangeMonth = new HashMap<String, String>();
		rangeMonth.put("1","一个月");
		rangeMonth.put("3","三个月");
		rangeMonth.put("5","五个月");
		rangeMonth.put("6","六个月");
		return rangeMonth;
	}

	@Override
	public Map<String, String> getSelectRangTypeSource() {
		Map<String, String> rangeType = new HashMap<String, String>();
		rangeType.put("0","通用");
		rangeType.put("1","唯一");
		return rangeType;
	}

	@Override
	public Map<Long, String> getSelectServiceTypeSource() {
		Map<Long, String> serviceType = new HashMap<Long, String>();
		List<DictServiceTypes> list = dictServiceTypesMapper.selectAll();
		/*for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			DictServiceTypes dictServiceTypes = (DictServiceTypes) iterator
					.next();
			serviceType.put(dictServiceTypes.getId(), dictServiceTypes.getName());
		}*/
		serviceType.put(0L, "钟点工");
		serviceType.put(1L, "深度保洁");
		serviceType.put(2L, "助理");
		
		/*
		 * 2015-10-21 09:54:36  适应 话费充值 活动，新增一种 优惠券类型
		 * 
		 * 	此处的 serviceType 对应  orders表中的   order_type 字段， 而 充值的 order_type 为 6
		 */
		serviceType.put(6L, "缴费充值");
		return serviceType;
	}

	@Override
	public List<DictCoupons> getCouponsByCouponType(Short couponType) {
		
		return dictCouponsMapper.selectByCouponType(couponType);
	}

	@Override
	public Map<Long, String> getSelectRechargeCouponSource() {
		Map<Long, String> dictCouponsMap = new HashMap<Long, String>();
		List<DictCoupons> list = dictCouponsMapper.selectByCouponType((short)1);;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			DictCoupons dictCoupons = (DictCoupons) iterator.next();
			dictCouponsMap.put(dictCoupons.getId(),dictCoupons.getIntroduction());
		}
		return dictCouponsMap;
	}
	/*
	 * 根据addTime查询导出的优惠券信息
	 */
	@Override
	public List<DictCoupons> selectBySearchVo(CouponSearchVo couponSearchVo) {
		Map<String,Object> conditions = new HashMap<String, Object>();
		String startDate = couponSearchVo.getStartDate();
		String endDate = couponSearchVo.getEndDate();
		Short couponType = couponSearchVo.getCoupontType();
		if(!StringUtil.isEmpty(startDate)){
			conditions.put("startDate",TimeStampUtil.getMillisOfDay(startDate.trim())/1000);
		}
		if(!StringUtil.isEmpty(endDate)){
			conditions.put("endDate", TimeStampUtil.getMillisOfDay(endDate.trim())/1000);
		}
		conditions.put("couponType",couponSearchVo.getCoupontType());
		return dictCouponsMapper.selectByListPage(conditions);
	}

	//按前日期查询优惠券
	@Override
	public List<DictCoupons> getSelectByMap(Map<String,Object> map) {
		List<DictCoupons> couponsList=null;
		if(map!=null){
			couponsList = dictCouponsMapper.selectByMap(map);
		}
		return couponsList;
	}
	
}
