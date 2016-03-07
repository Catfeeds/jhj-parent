package com.jhj.service.impl.socials;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.po.dao.socials.SocialsMapper;
import com.jhj.po.model.socials.Socials;
import com.jhj.service.socials.SocialsService;
import com.jhj.vo.SocialsSearchVo;
import com.jhj.vo.socials.SocialsVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
/**
 * @description：
 * @author： kerryg
 * @date:2015年9月7日 
 */
@Service
public class SocialsServiceImpl implements SocialsService {

	@Autowired
	private SocialsMapper socialsMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return socialsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Socials record) {
		return socialsMapper.insert(record);
	}

	@Override
	public int insertSelective(Socials record) {
		return socialsMapper.insertSelective(record);
	}

	@Override
	public Socials selectByPrimaryKey(Long id) {
		return socialsMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Socials record) {
		return socialsMapper.updateByPrimaryKeySelective(record);
	}

	/* (non-Javadoc)
	 * @see com.jhj.service.socials.SocialsService#updateByPrimaryKeyWithBLOBs(com.jhj.po.model.socials.Socials)
	 */
	@Override
	public int updateByPrimaryKeyWithBLOBs(Socials record) {
		return socialsMapper.updateByPrimaryKeyWithBLOBs(record);
	}

	@Override
	public int updateByPrimaryKey(Socials record) {
		return socialsMapper.updateByPrimaryKey(record);
	}

	@Override
	public PageInfo searchVoListPage(SocialsSearchVo searchVo, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<Socials> list =socialsMapper.selectByListPage(searchVo);
		 PageInfo result = new PageInfo(list);
		return result;
	}

	@Override
	public Socials initSocial() {
		Socials socials = new Socials();
		socials.setId(0L);
		socials.setTitle("");
		socials.setTitleImg("");
		socials.setContent("");
		socials.setBeginDate(DateUtil.getNowOfDate());
		Long seconds = TimeStampUtil.getMillisOfDate(DateUtil.getNowOfDate())/1000;
		seconds = seconds +2592000;
		Date date =TimeStampUtil.timeStampToDateFull(seconds*1000, "yyyy-MM-dd");
		socials.setEndDate(date);
		socials.setAddTime(TimeStampUtil.getNowSecond());
		socials.setUpdateTime(TimeStampUtil.getNowSecond());
		socials.setIsPublish((short)0);
		return socials;
	}
	

	@Override
	public SocialsVo initSocialsVo() {
		SocialsVo socials = new SocialsVo();
		socials.setId(0L);
		socials.setTitle("");
		socials.setTitleImg("");
		socials.setContent("");
		socials.setBeginDate(DateUtil.getNowOfDate());
		Long seconds = TimeStampUtil.getMillisOfDate(DateUtil.getNowOfDate())/1000;
		seconds = seconds +2592000;
		Date date =TimeStampUtil.timeStampToDateFull(seconds*1000, "yyyy-MM-dd");
		socials.setEndDate(date);
		socials.setAddTime(TimeStampUtil.getNowSecond());
		socials.setUpdateTime(TimeStampUtil.getNowSecond());
		socials.setIsPublish((short)0);
		socials.setTitleSmallImg("");
		socials.setTitleImgs("");
		socials.setBeginDateStr("");
		socials.setEndDateStr("");
		socials.setAmMobile("");
		return socials;
	}

	@Override
	public List<Socials> getSocialsList() {
		return socialsMapper.getSocialsList();
	}
	
	

}
