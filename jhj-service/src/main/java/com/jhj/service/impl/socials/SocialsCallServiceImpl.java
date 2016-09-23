package com.jhj.service.impl.socials;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.po.dao.socials.SocialCallMapper;
import com.jhj.po.dao.socials.SocialsMapper;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.socials.SocialCall;
import com.jhj.po.model.socials.Socials;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.socials.SocialsCallService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.SocialCallSearchVo;
import com.jhj.vo.socials.SocialCallVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.TimeStampUtil;

/**
 * @description：
 * @author： kerryg
 * @date:2015年9月8日 
 */
@Service
public class SocialsCallServiceImpl implements SocialsCallService {

	@Autowired
	private SocialCallMapper socialCallMapper;
	
	@Autowired
	private SocialsMapper socialsMapper;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return socialCallMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SocialCall record) {
		return socialCallMapper.insert(record);
	}

	@Override
	public int insertSelective(SocialCall record) {
		return socialCallMapper.insertSelective(record);
	}

	@Override
	public SocialCall selectByPrimaryKey(Long id) {
		return socialCallMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SocialCall record) {
		return socialCallMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(SocialCall record) {
		return socialCallMapper.updateByPrimaryKey(record);
	}

	@Override
	public PageInfo searchVoListPage(SocialCallSearchVo searchVo, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<SocialCall> list =socialCallMapper.selectByListPage(searchVo);
		List<SocialCallVo > listVo = new ArrayList<SocialCallVo>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			SocialCall socialCall = (SocialCall) iterator.next();
			SocialCallVo socialCallVo = new SocialCallVo();
			BeanUtilsExp.copyPropertiesIgnoreNull(socialCall,socialCallVo);
			Users users = usersService.selectByPrimaryKey(socialCall.getUserId());
			if(users !=null){
				socialCallVo.setUserName(users.getName());
			}
			OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(socialCall.getAmId());
			if(orgStaffs !=null){
				socialCallVo.setAmName(orgStaffs.getName());
			}
			Socials socials = socialsMapper.selectByPrimaryKey(socialCall.getSocialId());
			if(socials !=null){
				socialCallVo.setTitle(socials.getTitle());
			}
			listVo.add(socialCallVo);
		}
		for (int i = 0; i < listVo.size(); i++) {
			list.set(i,listVo.get(i));
		}
		 PageInfo result = new PageInfo(list);
		return result;
	}

	@Override
	public SocialCall selectBySocialsIdAndUserId(Long socialsId, Long userId) {
		SocialCall socialCall =  socialCallMapper.selectBySocialsIdAndUserId(socialsId, userId);
		return socialCall;
	}

	@Override
	public SocialCall initSocialCall() {
		SocialCall socialCall = new SocialCall();
		socialCall.setAmId(0L);
		socialCall.setAmMobile("");
		socialCall.setUserId(0L);
		socialCall.setUserMobile("");
		socialCall.setSocialId(0L);
		socialCall.setAddTime(TimeStampUtil.getNowSecond());
		return socialCall;
	}
	
}
