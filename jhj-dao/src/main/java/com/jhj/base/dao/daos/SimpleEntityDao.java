package com.jhj.base.dao.daos;

import com.jhj.base.dao.interfaces.ISimpleEntityDao;
import com.jhj.base.model.models.SimpleEntity;


public abstract class SimpleEntityDao<PKType extends Number, EntityType extends SimpleEntity<PKType>>
	extends EntityDao<PKType, EntityType> implements ISimpleEntityDao<PKType, EntityType> {

}
