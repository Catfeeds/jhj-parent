package com.jhj.service.impl.university;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.university.PartnerServiceTypeMapper;
import com.jhj.po.model.admin.AdminAuthority;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.vo.admin.AdminAuthorityVo;
import com.jhj.vo.bs.NewPartnerServiceVo;
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
		
		
		type.setUnit("");
		type.setDefaultNum((short)0);
		type.setPrice(new BigDecimal(0));
		type.setRemarks("");
		
		type.setServiceImgUrl("");
		
		type.setEnable((short)1); //0=不可用   1=可用
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
	
	@Override
	public List<NewPartnerServiceVo> getTreeList() {
		
		List<NewPartnerServiceVo> listVo = new ArrayList<NewPartnerServiceVo>();
		//根据parentId=0 查询出所用的父节点
		List<PartnerServiceType> list = partMapper.selectByParentId(0L);
		
//		List<PartnerServiceType> list = partMapper.selectNoParentService();
		
//		List<PartnerServiceType> list = partMapper.selectAllNoChildService();
		
		Iterator iterator = list.iterator(); 
		
		while(iterator.hasNext()) {
			
			PartnerServiceType serviceType = (PartnerServiceType) iterator.next();
			
			NewPartnerServiceVo vo2 = transServiceToTree(serviceType.getServiceTypeId());
			
			listVo.add(vo2);
		}
		return listVo;
	}
	
	@Override
	public NewPartnerServiceVo transServiceToTree(Long id) {
		
		NewPartnerServiceVo initVo = initVo();
		
		//根据id查出某对象
		PartnerServiceType serviceType = partMapper.selectByPrimaryKey(id);
		
		//赋值给 树形 VO	
		BeanUtilsExp.copyPropertiesIgnoreNull(serviceType, initVo);

		
		/*
		 *  
		 * 
		 */
		initVo.setId(serviceType.getServiceTypeId().intValue());
		initVo.setVersion(0);
		
		
		//查询 该 结点下的所有  子节点,(不包含孙子节点)
		List<PartnerServiceType> list = partMapper.selectByParentId(id);
		
		for (PartnerServiceType partnerServiceType : list) {
			
			if (partnerServiceType !=null && partnerServiceType.getServiceTypeId() !=null) {
				
				NewPartnerServiceVo vo = transServiceToTree(partnerServiceType.getServiceTypeId());
				
				//递归调用, 得到 某个 根节点 及其 叶子子节点
//				initVo.getChildList().add(vo);
				
				initVo.getChildren().add(vo);
			}
		}
		return initVo;
	}
	
	@Override
	public NewPartnerServiceVo initVo() {
		
		NewPartnerServiceVo serviceVo = new NewPartnerServiceVo();
		
		PartnerServiceType partner = initPartner();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(partner, serviceVo);
		
		serviceVo.setUrl("");
		serviceVo.setMatchUrl("");
		serviceVo.setItemIcon("");
		serviceVo.setParentId(0L);
		serviceVo.setChildList(new ArrayList<NewPartnerServiceVo>());
		
		return serviceVo;
	}
	
	@Override
	public List<PartnerServiceType> selectByParentId(Long id) {
		return partMapper.selectByParentId(id);
	}
}
