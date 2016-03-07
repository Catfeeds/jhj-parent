package com.jhj.service.socials;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.socials.SocialCall;
import com.jhj.vo.SocialCallSearchVo;

/**
 * @description：
 * @author： kerryg
 * @date:2015年9月8日 
 */
public interface SocialsCallService {
	int deleteByPrimaryKey(Long id);

    int insert(SocialCall record);

    int insertSelective(SocialCall record);

    SocialCall selectByPrimaryKey(Long id);
    
    SocialCall selectBySocialsIdAndUserId(Long socialsId,Long userId);

    int updateByPrimaryKeySelective(SocialCall record);

    int updateByPrimaryKey(SocialCall record);
    
	PageInfo searchVoListPage(SocialCallSearchVo searchVo,int pageNo,int pageSize);
	
	SocialCall initSocialCall();


}
