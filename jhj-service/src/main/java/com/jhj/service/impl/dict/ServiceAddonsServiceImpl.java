package com.jhj.service.impl.dict;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.po.dao.dict.DictServiceAddonsMapper;
import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.service.dict.ServiceAddonsService;
import com.jhj.vo.ServiceAddonSearchVo;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

/**
 * @description：
 * @author： kerryg
 * @date:2015年7月31日
 */
@Service
public class ServiceAddonsServiceImpl implements ServiceAddonsService {

	@Autowired
	private DictServiceAddonsMapper dictServiceAddonsMapper;

	@Override
	public DictServiceAddons initDictServiceAddons() {
		DictServiceAddons record = new DictServiceAddons();

		record.setServiceAddonId(0L);
		record.setServiceType(0L);
		record.setServiceHour(0);
		record.setName("");
		record.setKeyword("");
		record.setPrice(new BigDecimal(0));
		record.setStaffPrice(new BigDecimal(0));
		record.setDisPrice(new BigDecimal(0));
		record.setStaffDisPrice(new BigDecimal(0));
		record.setAprice(new BigDecimal(0));
		record.setStaffAprice(new BigDecimal(0));
		record.setDescUrl("");
		record.setTips("");
		record.setAddTime(TimeStampUtil.getNow() / 1000);
		record.setUpdateTime(0L);
		record.setEnable((short) 0);

		return record;
	}

	@Override
	public int deleteByPrimaryKey(Long serviceAddonId) {
		return dictServiceAddonsMapper.deleteByPrimaryKey(serviceAddonId);
	}

	@Override
	public int deleteByServiceType(Long serviceType) {
		return dictServiceAddonsMapper.deleteByServiceType(serviceType);
	}

	@Override
	public int insert(DictServiceAddons record) {
		return dictServiceAddonsMapper.insert(record);
	}

	@Override
	public int insertSelective(DictServiceAddons record) {
		return dictServiceAddonsMapper.insertSelective(record);
	}

	@Override
	public DictServiceAddons selectByPrimaryKey(Long serviceAddonId) {
		return dictServiceAddonsMapper.selectByPrimaryKey(serviceAddonId);
	}

	@Override
	public int updateByPrimaryKeySelective(DictServiceAddons record) {
		return dictServiceAddonsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(DictServiceAddons record) {
		return dictServiceAddonsMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<DictServiceAddons> selectBySearchVo(ServiceAddonSearchVo searchVo) {
		return dictServiceAddonsMapper.selectBySearchVo(searchVo);
	}

	@Override
	public PageInfo selectByListPage(ServiceAddonSearchVo searchVo, int pageNo, int pageSize) {

		PageHelper.startPage(pageNo, pageSize);
		List<DictServiceAddons> list = dictServiceAddonsMapper.selectByListPage(searchVo);
		PageInfo result = new PageInfo(list);

		return result;
	}

	@Override
	public boolean updateByRequest(HttpServletRequest request, Long serviceTypeId) {

		// 处理服务子项数据

		// 1. 先把修改前的项都置为不可用
		ServiceAddonSearchVo searchVo = new ServiceAddonSearchVo();
		searchVo.setServiceType(serviceTypeId);
		searchVo.setEnable((short) 1);
		List<DictServiceAddons> serviceAddons = this.selectBySearchVo(searchVo);

		if (!serviceAddons.isEmpty()) {
			for (DictServiceAddons item : serviceAddons) {
				item.setEnable((short) 0);
				this.updateByPrimaryKey(item);
			}
		}

		String serviceAddonIds[] = request.getParameterValues("serviceAddonIds");
		String serviceAddonName[] = request.getParameterValues("serviceAddonName");
		String itemUnit[] = request.getParameterValues("itemUnit");
		String price[] = request.getParameterValues("serviceAddonPrice");
		String staffPrice[] = request.getParameterValues("serviceAddonStaffPrice");
		String disPrice[] = request.getParameterValues("serviceAddonDisPrice");
		String staffDisPrice[] = request.getParameterValues("serviceAddonStaffDisPrice");
		String aprice[] = request.getParameterValues("serviceAddonAprice");
		String staffAprice[] = request.getParameterValues("serviceAddonStaffAprice");
		
		
		String defaultNum[] = request.getParameterValues("defaultNum");
		String serviceAddonServiceHour[] = request.getParameterValues("serviceAddonServiceHour");

		String serviceAddonIdsItem = "";
		String serviceAddonNameItem = "";
		String itemUnitItem = "";
		String priceItem = "";
		String staffPriceItem = "";
		String disPriceItem = "";
		String staffDisPriceItem = "";
		String apriceItem = "";
		String staffApriceItem = "";
		String defaultNumItem = "";
		String serviceAddonServiceHourItem = "";

		for (int i = 0; i < serviceAddonIds.length; i++) {

			serviceAddonIdsItem = serviceAddonIds[i];
			serviceAddonNameItem = serviceAddonName[i];
			itemUnitItem = itemUnit[i];
			priceItem = price[i];
			staffPriceItem = staffPrice[i];
			disPriceItem = disPrice[i];
			staffDisPriceItem = staffDisPrice[i];
			apriceItem = aprice[i];
			staffApriceItem = staffAprice[i];
			defaultNumItem = defaultNum[i];
			serviceAddonServiceHourItem = serviceAddonServiceHour[i];

			if (StringUtil.isEmpty(serviceAddonNameItem) && 
				StringUtil.isEmpty(itemUnitItem) && 
				(StringUtil.isEmpty(priceItem) || priceItem.equals("0")) && 
				(StringUtil.isEmpty(staffPriceItem) || staffPriceItem.equals("0")) && 
				(StringUtil.isEmpty(disPriceItem) || disPriceItem.equals("0")) && 
				(StringUtil.isEmpty(staffDisPriceItem) || staffDisPriceItem.equals("0")) && 
				(StringUtil.isEmpty(defaultNumItem) || defaultNumItem.equals("0")) &&
				(StringUtil.isEmpty(serviceAddonServiceHourItem) || serviceAddonServiceHourItem.equals("0.0"))
			   ) {
				continue;
			}

			DictServiceAddons d = this.initDictServiceAddons();
			Long serviceAddonId = 0L;
			if (StringUtil.isEmpty(serviceAddonIdsItem))
				serviceAddonIdsItem = "0";

			if (!serviceAddonIdsItem.equals("0"))
				serviceAddonId = Long.valueOf(serviceAddonIdsItem);

			if (serviceAddonId > 0L) {
				for (DictServiceAddons item : serviceAddons) {
					if (item.getServiceAddonId().equals(serviceAddonId)) {
						d = item;
						break;
					}
				}
			}

			d.setServiceType(serviceTypeId);
			d.setServiceHour(Double.valueOf(serviceAddonServiceHourItem));
			d.setServiceAddonId(serviceAddonId);
			d.setName(serviceAddonNameItem);
			d.setItemUnit(itemUnitItem);
			d.setPrice(new BigDecimal(priceItem));
			d.setStaffPrice(new BigDecimal(staffPriceItem));
			d.setDisPrice(new BigDecimal(disPriceItem));
			d.setStaffDisPrice(new BigDecimal(staffDisPriceItem));
			
			if (!StringUtil.isEmpty(apriceItem)) {
				d.setAprice(new BigDecimal(apriceItem));
			}
			
			if (!StringUtil.isEmpty(staffApriceItem)) {
				d.setStaffAprice(new BigDecimal(staffApriceItem));
			}
			
			
			d.setDefaultNum(Integer.valueOf(defaultNumItem));
			d.setEnable((short) 1);
			d.setUpdateTime(TimeStampUtil.getNowSecond());

			if (serviceAddonId > 0L) {
				this.updateByPrimaryKey(d);
			} else {
				this.insert(d);
			}

		}

		return true;
	}
}
