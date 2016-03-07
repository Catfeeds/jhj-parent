package com.jhj.base.dao.interfaces;

import java.util.List;

import com.jhj.base.model.models.EnableEntity;

public interface IEnableEntityDao<PKType extends Number, EntityType extends EnableEntity<PKType>> extends ISimpleEntityDao<PKType, EntityType> {

	public List<EntityType> listEnable();
	public List<EntityType> listDisable();
	public void enable(EntityType entity) ;
	public void disable(EntityType entity);

}
