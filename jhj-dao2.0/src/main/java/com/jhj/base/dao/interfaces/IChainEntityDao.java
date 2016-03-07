package com.jhj.base.dao.interfaces;

import com.jhj.base.model.models.ChainEntity;


public interface IChainEntityDao<PKType extends Number, EntityType extends ChainEntity<PKType, EntityType>> extends IEnableEntityDao<PKType, EntityType> {

}
