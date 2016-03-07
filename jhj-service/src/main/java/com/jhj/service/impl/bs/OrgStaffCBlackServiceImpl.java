package com.jhj.service.impl.bs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.bs.OrgStaffBlackMapper;
import com.jhj.po.model.bs.OrgStaffBlack;
import com.jhj.service.bs.OrgStaffBlackService;
import com.jhj.vo.OrgStaffDetailPaySearchVo;
import com.meijia.utils.TimeStampUtil;


/**
 *
 * @author :hulj
 * @Date : 2015年7月6日下午2:44:25
 * @Description: 
 *
 */
@Service
public class OrgStaffCBlackServiceImpl implements OrgStaffBlackService {

	@Autowired
	private OrgStaffBlackMapper orgStaffBlackMapper;

	@Override
	public int deleteByPrimaryKey(Long id) {
		
		return orgStaffBlackMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrgStaffBlack record) {
		
		return orgStaffBlackMapper.insert(record);
	}

	@Override
	public int insertSelective(OrgStaffBlack record) {
	
		return orgStaffBlackMapper.insertSelective(record);
	}

	@Override
	public OrgStaffBlack selectByPrimaryKey(Long id) {
		
		return orgStaffBlackMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrgStaffBlack record) {
		
		return orgStaffBlackMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrgStaffBlack record) {
		
		return orgStaffBlackMapper.updateByPrimaryKey(record);
	}

	@Override
	public OrgStaffBlack initOrgStaffBlack() {
		
		OrgStaffBlack record = new OrgStaffBlack();

		record.setId(0L);
		record.setStaffId(0L);
		record.setMobile("");
		record.setBlackType((short)0);
		record.setRemarks("欠款大于1000元!");
		record.setAddTime(TimeStampUtil.getNowSecond());
		
		return record; 
	}

	@Override
	public OrgStaffBlack selectByStaffId(Long userId) {

		return orgStaffBlackMapper.selectByStaffId(userId);
	}

	@Override
	public List<OrgStaffBlack> selectByListPage(OrgStaffDetailPaySearchVo searchVo,int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<OrgStaffBlack> list = orgStaffBlackMapper.selectVoByListPage(searchVo);
		
		return list;
		
	}

	@Override
	public OrgStaffBlack selectByStaffIdAndType(Long staffId) {
	
		return orgStaffBlackMapper.selectByStaffIdAndType(staffId);
	}


	
}
