package com.jhj.service.impl.users;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.po.dao.user.UserTrailRealMapper;
import com.jhj.po.model.bs.OrgStaffLeave;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.user.UserTrailReal;
import com.jhj.service.bs.OrgStaffLeaveService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.dict.DictService;
import com.jhj.service.users.UserTrailRealService;
import com.jhj.vo.org.LeaveSearchVo;
import com.jhj.vo.staff.OrgStaffPoiListVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.jhj.vo.user.UserTrailSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class UserTrailRealServiceImpl implements UserTrailRealService {
	@Autowired
	private UserTrailRealMapper userTrailRealMapper;
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Autowired
	private OrgsService orgService;
	
	@Autowired
	private DictService dictService;

	
	@Autowired
	private OrgStaffLeaveService orgStaffLeaveService;

	@Override
	public UserTrailReal initUserTrailReal() {
		UserTrailReal record = new UserTrailReal();

		record.setId(0L);
		record.setUserId(0L);
		record.setUserType((short) 0L);
		record.setLat("");
		record.setLng("");
		record.setPoiName("");
		record.setAddTime(TimeStampUtil.getNow() / 1000);
		return record;
	}

	@Override
	public int deleteByPrimaryKey(Long id) {

		return userTrailRealMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(UserTrailReal record) {

		return userTrailRealMapper.insert(record);
	}

	@Override
	public int insertSelective(UserTrailReal record) {

		return userTrailRealMapper.insertSelective(record);
	}

	@Override
	public UserTrailReal selectByPrimaryKey(Long id) {

		return userTrailRealMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(UserTrailReal record) {

		return userTrailRealMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(UserTrailReal record) {

		return userTrailRealMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<UserTrailReal> selectBySearchVo(UserTrailSearchVo searchVo) {

		return userTrailRealMapper.selectBySearchVo(searchVo);
	}
	
	@Override
	public PageInfo selectByListPage(UserTrailSearchVo searchVo, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<UserTrailReal> list = userTrailRealMapper.selectByListPage(searchVo);
		PageInfo result = new PageInfo(list);
		return result;
	}
	
	@Override
	public PageInfo selectByStaffListPage(StaffSearchVo searchVo, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<UserTrailReal> list = userTrailRealMapper.selectByStaffListPage(searchVo);
		PageInfo result = new PageInfo(list);
		return result;
	}
	
	@Override
	public OrgStaffPoiListVo getOrgStaffPoiListVo(UserTrailReal item) {

		OrgStaffPoiListVo vo = new OrgStaffPoiListVo();

		BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
		Long staffId = item.getUserId();
		OrgStaffs orgStaff = orgStaffsService.selectByPrimaryKey(staffId);
		BeanUtilsExp.copyPropertiesIgnoreNull(orgStaff, vo);
		// 1. 门店名称
		Long parentOrgId = orgStaff.getParentOrgId();
		Orgs orgs = orgService.selectByPrimaryKey(parentOrgId);

		if (orgs != null) {
			vo.setParentOrgName(orgs.getOrgName());
		}

		// 2.云店名称
		Long orgId = orgStaff.getOrgId();
		Orgs orgs2 = orgService.selectByPrimaryKey(orgId);
		vo.setOrgName("");
		if (orgs2 != null) {
			vo.setOrgName(orgs2.getOrgName());
		}
		// 3.籍贯
		String cityName = dictService.getCityName(orgStaff.getCityId());
		String provinceName = dictService.getProvinceName(orgStaff.getProvinceId());

		vo.setHukou(provinceName + " " + cityName);
		
		// 4.最新的地理位置信息.
		vo.setPoiTimeStr("");
		vo.setTodayPoiStatus(0);

		String today = DateUtil.getToday();

		Long addTime = item.getAddTime();
		String poiDay = TimeStampUtil.timeStampToDateStr(addTime * 1000,  DateUtil.DEFAULT_PATTERN);
		vo.setPoiTimeStr(TimeStampUtil.timeStampToDateStr(addTime * 1000, DateUtil.DEFAULT_FULL_PATTERN));
		if (poiDay.equals(today)) {
			vo.setTodayPoiStatus(1);
		}
		
		
		//查看是否在请假范围内
		vo.setStaffLeave("否");
		LeaveSearchVo searchVo3 = new LeaveSearchVo();
		Date leaveDate = DateUtil.parse(today);
		searchVo3.setLeaveDate(leaveDate);
		searchVo3.setLeaveStatus("1");
		searchVo3.setStaffId(staffId);
		List<OrgStaffLeave> leaveList = orgStaffLeaveService.selectBySearchVo(searchVo3);
		if (!leaveList.isEmpty()) {
			vo.setStaffLeave("请假中");
		}
		
		return vo;
	}
}
