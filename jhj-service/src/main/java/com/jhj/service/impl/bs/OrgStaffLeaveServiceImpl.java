package com.jhj.service.impl.bs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.po.dao.bs.OrgStaffLeaveMapper;
import com.jhj.po.model.bs.OrgStaffLeave;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.service.bs.OrgStaffLeaveService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.vo.bs.LeaveStaffVo;
import com.jhj.vo.org.LeaveSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2016年5月12日上午11:36:05
 * @Description: 
 *
 */
@Service
public class OrgStaffLeaveServiceImpl implements OrgStaffLeaveService {
	
	@Autowired
	private OrgStaffLeaveMapper leaveMapper;
	
	@Autowired
	private OrgStaffsService staffService;
	
	@Autowired
	private OrgsService orgService;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return leaveMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrgStaffLeave record) {
		return leaveMapper.insert(record);
	}


	@Override
	public OrgStaffLeave selectByPrimaryKey(Long id) {
		return leaveMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrgStaffLeave record) {
		return leaveMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public LeaveStaffVo transToVO(OrgStaffLeave leave){
		
		LeaveStaffVo leaveVo = initLeaveVo(); 
		
		BeanUtilsExp.copyPropertiesIgnoreNull(leave, leaveVo);
		
		if(leave.getId() != 0L){
			
			Long staffId = leave.getStaffId();
			
			OrgStaffs staffs = staffService.selectByPrimaryKey(staffId);
			if(staffs!=null){
				//服务人员信息
				leaveVo.setStaffName(staffs.getName());
				leaveVo.setStaffMobile(staffs.getMobile());
				// 请假日期
				Date leaveDate = leave.getLeaveDate();
				
//				Short start = leave.getStart();
//				Short end = leave.getEnd();
				
				String startStr = DateUtil.format(leaveDate, "yyyy-MM-dd"); //+ " "+start + ":00:00";
				
				//假期时间 展示
				leaveVo.setLeaveDateStr(startStr);// +" "+start+"点~"+end+"点");
				
				Date leaveDateEnd = leave.getLeaveDateEnd();
				String endStr=null;
				long endLong = 0L;
				long startLong = 0L;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(leaveDateEnd!=null){
					endStr =  DateUtil.format(leaveDateEnd, "yyyy-MM-dd");   ///+" "+end + ":00:00";
					leaveVo.setLeaveDateEndStr(endStr);
					try {
						Date endDate = sdf.parse(endStr);
						endLong = endDate.getTime()/1000;
						Date startDate = sdf.parse(startStr);
						startLong = startDate.getTime()/1000;
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				
				//跟当前时间比较
//				Long nowSecond = TimeStampUtil.getNowSecond();
				if(leaveVo.getLeaveStatus().equals("1") && leaveDateEnd!=null ){
					if(DateUtil.compare(leaveVo.getLeaveDateEndStr(),DateUtil.getNow("yyyy-MM-dd"))){
						leaveVo.setLeaveStatus("2");
						leave.setLeaveStatus("2");
					}
					leaveMapper.updateByPrimaryKeySelective(leave);
				}
//				
				Long adminId = leave.getAdminId();
				
				OrgStaffs orgStaffs = staffService.selectByPrimaryKey(adminId);
				if(orgStaffs!=null){
					//批复人
					leaveVo.setExcuteStaffName(orgStaffs.getName());
				}
				
				//请假时间段
//				if(start == 8 && end == 12){
//					leaveVo.setLeaveDuration((short)0);
//				}
//				
//				if(start == 8 && end == 21){
//					leaveVo.setLeaveDuration((short)1);
//				}
//				
//				if(start == 12 && end == 21){
//					leaveVo.setLeaveDuration((short)2);
//				}
				
				Long orgId = leave.getOrgId();
				
				Orgs orgs = orgService.selectByPrimaryKey(orgId);
				
				leaveVo.setCloudOrgName(orgs.getOrgName());
			}
			
		}
		return leaveVo;
	}
	
	@Override
	public OrgStaffLeave initLeave() {
		
		OrgStaffLeave staffLeave = new OrgStaffLeave();
		
		staffLeave.setId(0L);
	    staffLeave.setOrgId(0L);
	    staffLeave.setParentId(0L);
	    staffLeave.setStaffId(0L);
	    
	    Long seconds = TimeStampUtil.getMillisOfDate(new Date())/1000;
		Date date =TimeStampUtil.timeStampToDateFull(seconds*1000, "yyyy-MM-dd");
	    
	    staffLeave.setLeaveDate(date);	//假期 时间 yyyy-MM-dd
	    staffLeave.setStart((short)8);	//开始时间点 取值为 8/12
	    staffLeave.setEnd((short)21);	//结束时间点取值为 12 /21
	    staffLeave.setRemarks("");
	    staffLeave.setAdminId(0L);
	    staffLeave.setAddTime(TimeStampUtil.getNowSecond());
	    staffLeave.setLeaveDateEnd(date);
	    staffLeave.setTotalDays(0);
	    staffLeave.setLeaveStatus("1");
		
		return staffLeave;
	}
	
	@Override
	public LeaveStaffVo initLeaveVo() {
		
		LeaveStaffVo staffVo = new LeaveStaffVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(initLeave(), staffVo);
		
		staffVo.setStaffName("");
		staffVo.setStaffMobile("");
//		staffVo.setLeaveStatus((short)0);	// 请假状态的标识
		staffVo.setExcuteStaffName("");
		
		staffVo.setLeaveDuration((short)0);	// 0= 8~12点  1=8~21点  2=12~21点
		
		staffVo.setCloudOrgName("");
		staffVo.setLeaveDateStr("");
		
		return staffVo;
	}
	
	@Override
	public List<OrgStaffLeave> selectBySearchVo(LeaveSearchVo searchVo) {
		return leaveMapper.selectBySearchVo(searchVo);
	}
	
	@Override
	public PageInfo selectByListPage(LeaveSearchVo searchVo, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<OrgStaffLeave> list = leaveMapper.selectByListPage(searchVo);
		PageInfo result = new PageInfo(list);
		return result;
	}
	
	/**
	 * 过滤掉找出服务日期请假的人员，注意的是请假分为全天和上午下午。
	 * 
	 */
	@Override
	public List<Long> checkLeaveConflict(Long serviceDate, double serviceHour) {
		
		List<Long> staffIds = new ArrayList<Long>();
		String serviceDateStr = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, DateUtil.DEFAULT_PATTERN);
		
		String startHourStr = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, "H");
		double startHour = Double.valueOf(startHourStr);
		double endHour = startHour + serviceHour + 1.5;
		
		List<Double> serviceRange = new ArrayList<Double>();
		
		double stepHour = startHour;
		while (stepHour <= endHour) {
			serviceRange.add(stepHour);
			stepHour = stepHour + 0.5;
		}
		
		//先找出当天请假的员工
		LeaveSearchVo leaveSearchVo = new LeaveSearchVo();
		Date leaveDate = DateUtil.parse(serviceDateStr);
		leaveSearchVo.setLeaveDate(leaveDate);
		leaveSearchVo.setLeaveStatus("1");
		
		// 服务时间内 ，同时也在 假期内的 员工
		List<OrgStaffLeave> leaveList = this.selectBySearchVo(leaveSearchVo);
		if (leaveList.isEmpty()) return staffIds;
		
		for (OrgStaffLeave item : leaveList) {
			int start = item.getStart();
			int end = item.getEnd();
			
			double startX = Double.valueOf(start);
			double endY = Double.valueOf(end);
			
			List<Double> leaveDateRange = new ArrayList<Double>();
			stepHour = startX;
			while (stepHour <= endY) {
				leaveDateRange.add(stepHour);
				stepHour = stepHour + 0.5;
			}
			
			
			for (double leaveItem : leaveDateRange) {
				if (serviceRange.contains(leaveItem)) {
					staffIds.add(item.getStaffId());
					break;
				}
			}
			
		}
		
		return staffIds;
	}
	
	/**
	 * 检测某个小时点是否有请假人员
	 * 
	 */
	@Override
	public List<Long> checkLeaveExist(List<OrgStaffLeave> leaveList, String itemServiceHour, double serviceHours) {
		
		List<Long> staffIds = new ArrayList<Long>();
		if (leaveList.isEmpty()) return staffIds;
		
		List<String> serviceDateRange = new ArrayList<String>();
		
		
		String today = DateUtil.getToday();
		Long tmpServiceDate = TimeStampUtil.getMillisOfDayFull(today + " " + itemServiceHour + ":00") / 1000;
		
		double stepHourRange = 0;
		while (stepHourRange <= serviceHours) {
			tmpServiceDate = (long) (tmpServiceDate + stepHourRange * 60 * 60);
			String tmpHourStr = TimeStampUtil.timeStampToDateStr(tmpServiceDate * 1000, "HH:mm");
			serviceDateRange.add(tmpHourStr);
			stepHourRange = stepHourRange + 0.5;
		}
		
		
		for (OrgStaffLeave item : leaveList) {
			int start = item.getStart();
			int end = item.getEnd();
			
			
			List<String> leaveDateRange = new ArrayList<String>();
			int stepHour = start;
			while (stepHour <= end) {
				String hourStr = String.valueOf(stepHour);
				if (hourStr.length() == 1) hourStr = "0" + hourStr;
				
				leaveDateRange.add(hourStr + ":00");
				leaveDateRange.add(hourStr + ":30");
				stepHour = stepHour + 1;
			}

			for (String leaveDate : leaveDateRange) {
				boolean isBreak = false;
				for (String hourRnage : serviceDateRange) {
					if (leaveDate.equals(hourRnage)) {
						staffIds.add(item.getStaffId());
						isBreak = true;
						break;
					}
				}
				if (isBreak) break;
			}
		}
		return staffIds;
	}
}
