package com.jhj.base.dao.interfaces;

import com.jhj.base.model.models.SimpleEntity;

public interface ISimpleEntityDao<PKType extends Number, EntityType extends SimpleEntity<PKType>> extends IEntityDao<PKType, EntityType> {

}
