package com.jhj.service.impl.orderYear;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.order.OrderCustomizationYearMapper;
import com.jhj.po.model.order.OrderCustomizationYear;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.Users;
import com.jhj.service.orderYear.OrderCustomizYearService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.year.OrderCusYearVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.MathBigDeciamlUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2016年4月2日上午11:18:42
 * @Description: 
 *		
 *		全年订制服务
 */
@Service
public class OrderCusYearImpl implements OrderCustomizYearService {

	@Autowired
	private OrderCustomizationYearMapper yearMapper;
	
	@Autowired
	private UsersService userService;
	
	@Autowired
	private PartnerServiceTypeService partService;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return yearMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrderCustomizationYear record) {
		return yearMapper.insert(record);
	}

	@Override
	public int insertSelective(OrderCustomizationYear record) {
		return yearMapper.insertSelective(record);
	}

	@Override
	public OrderCustomizationYear selectByPrimaryKey(Long id) {
		return yearMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrderCustomizationYear record) {
		return yearMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrderCustomizationYear record) {
		return yearMapper.updateByPrimaryKey(record);
	}

	@Override
	public OrderCustomizationYear initCusYear() {
		
		OrderCustomizationYear year = new OrderCustomizationYear();
		
		year.setId(0L);
		year.setServiceTypeId(0L);
		year.setUserId(0L);
		year.setAddTime(TimeStampUtil.getNowSecond());
		
		return year;
	}

	@Override
	public List<OrderCustomizationYear> selectByListPage() {
		return yearMapper.selectByListPage();
	}

	@Override
	public OrderCusYearVo transToVo(OrderCustomizationYear year) {
		
		OrderCusYearVo yearVo = new OrderCusYearVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(year, yearVo);
		
		Long userId = year.getUserId();
		Users users = userService.selectByUsersId(userId);
		//用户手机号
		yearVo.setUserMobile(users.getMobile());
		
		Long serviceTypeId = year.getServiceTypeId();
		PartnerServiceType type = partService.selectByPrimaryKey(serviceTypeId);
		//服务类型名称
		yearVo.setServiceName(type.getName());
		
		//价格
		BigDecimal price = type.getPrice();
		
		//每周频次
		Long serviceTimes = type.getServiceTimes();
		
		//原价
		BigDecimal mul = MathBigDeciamlUtil.mul(price, new BigDecimal(52*serviceTimes));
		yearVo.setPrice(mul);
		
		//年付
		yearVo.setYearPrice(MathBigDeciamlUtil.round(MathBigDeciamlUtil.mul(mul, new BigDecimal(0.95)), 2));
		
		//月付
		// 原价 每月支付（两位小数）
		BigDecimal div = MathBigDeciamlUtil.div(mul, new BigDecimal(12), 2);
		yearVo.setMonthPrice(MathBigDeciamlUtil.round(MathBigDeciamlUtil.mul(div, new BigDecimal(0.85)), 2));
		
		//年服务频次
		yearVo.setServiceTimeYear(type.getServiceTimes()*52);
		
		String date1 = DateUtil.getDefaultDate(year.getAddTime() * 1000);
		
		yearVo.setAddTimeStr(date1);
		
		return yearVo;
	}

}
