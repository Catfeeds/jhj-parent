package com.jhj.po.dao.socials;

import java.util.List;

import com.jhj.po.model.socials.Socials;
import com.jhj.vo.SocialsSearchVo;

public interface SocialsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Socials record);

    int insertSelective(Socials record);

    Socials selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Socials record);

    int updateByPrimaryKeyWithBLOBs(Socials record);

    int updateByPrimaryKey(Socials record);
    
    List<Socials> selectByListPage(SocialsSearchVo socialsSearchVo);
    
    List<Socials> getSocialsList();

}