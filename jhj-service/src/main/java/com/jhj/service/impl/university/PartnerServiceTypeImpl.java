package com.jhj.service.impl.university;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.university.PartnerServiceTypeMapper;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.vo.university.OaPartnerServiceTypeVo;
import com.meijia.utils.BeanUtilsExp;

@Service
public class PartnerServiceTypeImpl implements PartnerServiceTypeService {
	
	@Autowired
	private PartnerServiceTypeMapper  partMapper;
	
	@Override
	public int deleteByPrimaryKey(Long serviceTypeId) {
		return partMapper.deleteByPrimaryKey(serviceTypeId);
	}

	@Override
	public int insert(PartnerServiceType record) {
		return partMapper.insert(record);
	}

	@Override
	public int insertSelective(PartnerServiceType record) {
		return partMapper.insertSelective(record);
	}

	@Override
	public PartnerServiceType selectByPrimaryKey(Long serviceTypeId) {
		return partMapper.selectByPrimaryKey(serviceTypeId);
	}

	@Override
	public int updateByPrimaryKeySelective(PartnerServiceType record) {
		return partMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(PartnerServiceType record) {
		return partMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<PartnerServiceType> selectByListPage(int pageNo, int pageSize) {
		
		PageHelper.startPage(pageNo, pageSize);
		List<PartnerServiceType> list = partMapper.selectByListPage();
		return list;
	}

	@Override
	public PartnerServiceType initPartner() {
		
		PartnerServiceType type = new PartnerServiceType();
		
		type.setServiceTypeId(0L);
		type.setNo((short)0);
		type.setParentId(0L);
		type.setName("");
		type.setViewType((short)0);  //0 = 类别   1=商品
		
		return type;
	}

	@Override
	public List<PartnerServiceType> selectAll() {
		return partMapper.selectAll();
	}

	@Override
	public OaPartnerServiceTypeVo completeVo(PartnerServiceType partner) {
		
		Long parentId = partner.getParentId();
		
		PartnerServiceType serviceType = selectByPrimaryKey(parentId);
		
		OaPartnerServiceTypeVo typeVo = new OaPartnerServiceTypeVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(partner, typeVo);
		
		if(serviceType !=null){
			
			typeVo.setParentName(serviceType.getName());
		}else{
			typeVo.setParentName("");
		}
		
		return typeVo;
	}
	
	
	/*
	 * 所有 一级 serviceType  (parentId == 0)
	 * 	
	 * 	如  钟点工、助理、快送
	 */
	@Override
	public List<Long> selectNoParentServiceId() {
		
		List<PartnerServiceType> list = partMapper.selectNoParentService();
		
		List<Long> serviceTypeIdList = new ArrayList<Long>();
		
		for (PartnerServiceType partnerServiceType : list) {
			
			Long serviceTypeId = partnerServiceType.getServiceTypeId();
			
			serviceTypeIdList.add(serviceTypeId);
		}
		
		return serviceTypeIdList;
	}
	
	
	@Override
	public List<PartnerServiceType> selectNoParentServiceObj() {
		
		return partMapper.selectNoParentService();
	}
}
