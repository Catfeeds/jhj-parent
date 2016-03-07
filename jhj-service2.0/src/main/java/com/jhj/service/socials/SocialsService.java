package com.jhj.service.socials;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.socials.Socials;
import com.jhj.vo.SocialsSearchVo;
import com.jhj.vo.socials.SocialsVo;
/**
 * @description：
 * @author： kerryg
 * @date:2015年9月7日 
 */
public interface SocialsService {
	
	int deleteByPrimaryKey(Long id);

    int insert(Socials record);

    int insertSelective(Socials record);

    Socials selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Socials record);

    int updateByPrimaryKeyWithBLOBs(Socials record);

    int updateByPrimaryKey(Socials record);
    
	PageInfo searchVoListPage(SocialsSearchVo searchVo,int pageNo,int pageSize);
	
	Socials initSocial();
	
	SocialsVo initSocialsVo();

	List<Socials> getSocialsList();

}
