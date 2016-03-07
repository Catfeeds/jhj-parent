package com.jhj.po.dao.socials;

import java.util.List;

import com.jhj.po.model.socials.SocialCall;
import com.jhj.vo.SocialCallSearchVo;

public interface SocialCallMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SocialCall record);

    int insertSelective(SocialCall record);

    SocialCall selectByPrimaryKey(Long id);

    SocialCall selectBySocialsIdAndUserId(Long socialsId,Long userId);

    int updateByPrimaryKeySelective(SocialCall record);

    int updateByPrimaryKey(SocialCall record);
    
    List<SocialCall> selectByListPage(SocialCallSearchVo socialsSearchVo);
}