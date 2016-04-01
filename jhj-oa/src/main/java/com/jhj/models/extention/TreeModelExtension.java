package com.jhj.models.extention;

import java.util.ArrayList;
import java.util.List;

import com.jhj.models.TreeModel;
import com.jhj.base.model.models.ChainEntity;

public class TreeModelExtension {

	/*public static <PKType extends Number, EntityType extends ChainEntity<PKType, EntityType>> List<EntityType> listChain(List<EntityType> allEntities){

		//List<EntityType> allEntities=listAll();
		List<EntityType> chainEntities =  Lambda.select(allEntities, Lambda.having(Lambda.on().getParent(), Matchers.equalTo(null)));
		toChain(chainEntities, allEntities);

		boolean ff3 = Lambda.exists(ff1,
				Lambda.having(Lambda.on(this.entityClass).getParent(), Matchers.equalTo(null)));

		return chainEntities;

	}*/

	public static <PKType extends Number, EntityType extends ChainEntity<PKType, EntityType>> List<TreeModel> ToTreeModels(List<EntityType> entities, PKType selectedId, List<PKType> checkedIdList, List<PKType> expandedIdList)
	{

		List<TreeModel> treeModels = new ArrayList<TreeModel>();
	    for (EntityType entity : entities)
	    {
	        boolean checked = false;
	        boolean selected = false;
	        boolean collpase = true;
	        List<TreeModel> children = null;

	        if (selectedId!=null && selectedId.equals(entity.getId()))
            	selected = true;
	        if(checkedIdList!=null && !checkedIdList.isEmpty())
	        	checked = checkedIdList.contains(entity.getId());
            if(expandedIdList!=null && !expandedIdList.isEmpty())
            	collpase = !expandedIdList.contains(entity.getId());
            if(entity.getChildren()!=null && !entity.getChildren().isEmpty())
            	children=ToTreeModels(entity.getChildren(), selectedId, checkedIdList, expandedIdList);
            
            
            /*
             *  分析:
             * 	
             *  	此处的 id 是从 基类 Entity 继承而来, 对应数据库的主键 属性
             *  
             *  	从 基类 Entity （只包含id,version属性）----> 多层包装, 构造自定义的  treeModel, 树形结构对象
             *  
             *  but： 对于 没有  “id” 这个字段的对象, 需要 手动 设置    真实主键属性 <---> id  的映射
             *  	
             *  	 下面的 “name” 同理
             *  	
             *   目的: 供 jsp 页面的展示用 ，任意结点,只需提供  id 和 name属性， 即可以满足 展示和 后续的 CRUD 操作
             *  	
             */
            
            
            treeModels.add(new TreeModel(entity.getId().toString(),
            		entity.getId().toString(),
            		entity.getName().toString(),checked, selected, collpase, children));

	        /*if (expanded.IsNotNullOrEmpty())
	            collpase = !expanded.Contains(fd.Id);*/
	        //treeModels.Add(new TreeModel { id = fd.Id.ToString(), value = fd.TheValue, text = fd.Name.HtmlEncode(), selected = isSelected, collpase = collpase, @checked = isChecked, children = fd.Children.IsNotNullOrEmpty() ? ToTreeModels(fd.Children as IList<EntityType>, selected, checkedList, expanded) : null });
	    }
	    return treeModels;
	}

	/*public static <PKType extends Number, EntityType extends ChainEntity<PKType, EntityType>> List<TreeModel> ToTreeModels(List<EntityType> entities, PKType selectedId, List<PKType> checkedIdList)
	{
	    List<TreeModel> treeModels = new ArrayList<TreeModel>();
	    for (EntityType entity : entities)
	    {
	        boolean isChecked = false;
	        boolean isSelected = false;
	        List<TreeModel> children = null;
	        if (checkedIdList!=null && !checkedIdList.isEmpty())
	            isChecked = checkedIdList.contains(entity.getId());
	        if (selectedId!=null && selectedId.equals(entity.getId()))
                isSelected = true;
	        if(entity.getChildren()!=null && !entity.getChildren().isEmpty())
            	children=ToTreeModels(entity.getChildren(), selectedId, checkedIdList);

	        treeModels.add(new TreeModel(entity.getId().toString(),
	        		entity.getTheValue().toString(),
	        		entity.getName().toString(), isChecked, isSelected));
	    }
	    return treeModels;
	}*/

}
