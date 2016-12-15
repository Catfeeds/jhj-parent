package com.jhj.service.impl.bs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.bs.OrgStaffBlackMapper;
import com.jhj.po.model.bs.OrgStaffBlack;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.orderReview.JhjSetting;
import com.jhj.service.bs.OrgStaffBlackService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.orderReview.SettingService;
import com.jhj.vo.staff.OrgStaffDetailPaySearchVo;
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
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;
	
	@Autowired
	private OrdersService ordersService;

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
	public List<OrgStaffBlack> selectByStaffIdAndType(Long staffId) {
	
		return orgStaffBlackMapper.selectByStaffIdAndType(staffId);
	}

	@Override
	public List<Long> selectAllBadRateStaffId() {
		
		List<OrgStaffBlack> list = selectAllBadRateStaff();
		
		List<Long> idList = new ArrayList<Long>();
		
		if(list.size() > 0){
			for (OrgStaffBlack orgStaffBlack : list) {
				
				idList.add(orgStaffBlack.getStaffId());
			}
		}
		
		return idList;
	}
	
	@Override
	public List<OrgStaffBlack> selectAllBadRateStaff() {
		return orgStaffBlackMapper.selectAllBadRateStaff();
	}
	
	//检测是否需要加入黑名单
	@Override
	public boolean checkStaffBlank(OrgStaffFinance orgStaffFinance) {
		
		Long staffId = orgStaffFinance.getStaffId();
		String mobile = orgStaffFinance.getMobile();
		BigDecimal totalDept = orgStaffFinance.getTotalDept();
		
		BigDecimal maxOrderDept = new BigDecimal(1000);
		JhjSetting jhjSetting = settingService.selectBySettingType("total-dept-blank");
		if (jhjSetting != null) {
			maxOrderDept = new BigDecimal(jhjSetting.getSettingValue());
		}

		if (totalDept.compareTo(maxOrderDept) >= 0) {
			OrgStaffBlack orgStaffBlack = this.initOrgStaffBlack();
			orgStaffBlack.setStaffId(staffId);
			orgStaffBlack.setMobile(mobile);
			this.insertSelective(orgStaffBlack);

			// 设置黑名单标识.
			orgStaffFinance.setIsBlack((short) 1);
			orgStaffFinance.setUpdateTime(TimeStampUtil.getNowSecond());
			orgStaffFinanceService.updateByPrimaryKey(orgStaffFinance);

			// 欠款大于1000给服务人员发送加入黑名单的短信通知
			ordersService.userJoinBlackSuccessTodo(mobile);
		}
		
		return true;
	}
}
