package com.jhj.service.impl.bs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.common.Constants;
import com.jhj.po.dao.bs.OrgStaffsMapper;
import com.jhj.po.dao.order.OrderPricesMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.dao.university.PartnerServiceTypeMapper;
import com.jhj.po.model.bs.OrgStaffAuth;
import com.jhj.po.model.bs.OrgStaffSkill;
import com.jhj.po.model.bs.OrgStaffTags;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.bs.Tags;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserRefAm;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffAuthService;
import com.jhj.service.bs.OrgStaffSkillService;
import com.jhj.service.bs.OrgStaffTagsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.bs.TagsService;
import com.jhj.service.dict.DictService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.university.StudyStaffPassQueryService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.TagSearchVo;
import com.jhj.vo.bs.NewStaffFormVo;
import com.jhj.vo.bs.NewStaffListVo;
import com.jhj.vo.bs.OrgStaffVo;
import com.jhj.vo.bs.SecInfoVo;
import com.jhj.vo.bs.staffAuth.StaffAuthVo;
import com.jhj.vo.order.OrderStaffRateVo;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.jhj.vo.staff.OrgStaffSkillSearchVo;
import com.jhj.vo.staff.StaffAuthSearchVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.jhj.vo.user.UserSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.RandomUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月6日下午2:44:25
 * @Description:
 *
 */
@Service
public class OrgStaffsServiceImpl implements OrgStaffsService {
	@Autowired
	private OrgStaffsMapper orgStaMapper;

	@Autowired
	private OrgStaffTagsService orgStaTagService;

	@Autowired
	private TagsService tagService;

	@Autowired
	private DictService dictService;

	@Autowired
	private OrgsService orgService;

	@Autowired
	private UserRefAmService userRefAmService;

	@Autowired
	private UsersService userService;

	@Autowired
	private OrdersMapper orderMapper;

	@Autowired
	private OrderPricesMapper orderPriceMapper;

	@Autowired
	private StudyStaffPassQueryService passQueryService;

	@Autowired
	private PartnerServiceTypeService partnerService;

	@Autowired
	private OrgStaffAuthService authService;

	@Autowired
	private OrgStaffSkillService orgStaffSkillService;

	@Autowired
	private PartnerServiceTypeMapper partServiceMapper;

	@Autowired
	private PartnerServiceTypeService partService;

	@Override
	public int deleteByPrimaryKey(Long staffId) {
		return orgStaMapper.deleteByPrimaryKey(staffId);
	}

	@Override
	public int insert(OrgStaffs orgStaffs) {
		return orgStaMapper.insert(orgStaffs);
	}

	@Override
	public int insertSelective(OrgStaffs orgStaffs) {
//		orgStaffs.setStaffCode(getValidateStaffCode());
		return orgStaMapper.insertSelective(orgStaffs);
	}

	@Override
	public OrgStaffs selectByPrimaryKey(Long staffId) {
		return orgStaMapper.selectByPrimaryKey(staffId);
	}

	@Override
	public int updateByPrimaryKeySelective(OrgStaffs orgStaffs) {
		//修改员工信息， 如果员工信息中没有员工编号，则生成员工编号
		OrgStaffs os = this.selectByPrimaryKey(orgStaffs.getStaffId());
		if(os.getStaffCode()==null && !os.getStaffCode().equals("")){
			orgStaffs.setStaffCode(String.valueOf(1000+os.getStaffId()));
		}
		return orgStaMapper.updateByPrimaryKeySelective(orgStaffs);
	}

	@Override
	public int updateByPrimaryKey(OrgStaffs orgStaffs) {
		return orgStaMapper.updateByPrimaryKey(orgStaffs);
	}

	@Override
	public List<OrgStaffs> selectBySearchVo(StaffSearchVo searchVo) {
		return orgStaMapper.selectBySearchVo(searchVo);
	}

	@Override
	public PageInfo selectByListPage(StaffSearchVo staffSearchVo, int pageNo, int pageSize) {

		PageHelper.startPage(pageNo, pageSize);
		List<OrgStaffs> lists = orgStaMapper.selectByListPage(staffSearchVo);
		PageInfo result = new PageInfo(lists);
		return result;
	}

	@Override
	public OrgStaffs initOrgStaffs() {

		OrgStaffs orgStaffs = new OrgStaffs();

		orgStaffs.setStaffId(0L);

		// orgStaffs.setAmId(0L);
		orgStaffs.setProvinceId(0L);
		orgStaffs.setCityId(0L);
		orgStaffs.setRegionId(0L);
		orgStaffs.setOrgId(0L);
		orgStaffs.setStaffType((short) 0);
		orgStaffs.setStatus((short) 1);
		orgStaffs.setSex((short) 1); // 性别默认 1 （男0， 女 1）
		orgStaffs.setName("");
		orgStaffs.setMobile("");
		orgStaffs.setTel("");
		orgStaffs.setAddr("");
		orgStaffs.setBirth(DateUtil.parse("1980-01-01 00:00:00")); // ???
		orgStaffs.setWorkYear((short) 0);
		orgStaffs.setCardId("");
		orgStaffs.setNation("");
		orgStaffs.setEdu("");
		orgStaffs.setAstro((short) 0);
		orgStaffs.setBloodType("");
		orgStaffs.setHeadImg("http://www.jia-he-jia.com/u/img/default-head-img.png");
		orgStaffs.setIntro("");
		orgStaffs.setAddTime(TimeStampUtil.getNow() / 1000);
		orgStaffs.setUpdateTime(0L);

		orgStaffs.setParentOrgId(0L);
		orgStaffs.setLevel((short) 1); // 员工等级 1=1级 2=2级 3=3级 4=4级
		orgStaffs.setStaffCode("");
		return orgStaffs;
	}

	/*
	 * 这个方法 在 服务人员列表页、表单； 助理人员 列表、表单 都会用到！！！
	 */
	@Override
	public OrgStaffVo genOrgStaffVo(OrgStaffs orgStaff) {

		OrgStaffVo orgStaffVo = initOrgStaffVo();

		BeanUtils.copyProperties(orgStaff, orgStaffVo);

		// //设置员工 对应的 助理名称
		// String amName = getAmName(orgStaff);
		// orgStaffVo.setAmName(amName);
		// orgStaffVo.setAmId(orgStaff.getStaffId());

		String tagNames = "";
		String tagIds = "";

		Long staffId = orgStaff.getStaffId();
		List<Tags> tags = new ArrayList<Tags>();

		// if (staffId > 0L) {
		// // 根据staffId查找对应的 tagId
		// List<OrgStaffTags> staffTags =
		// orgStaTagService.selectByStaffId(staffId);
		// // 处理 form中 标签的 显示 和传值
		// List<Long> tagIdList = new ArrayList<Long>();
		// for (OrgStaffTags item : staffTags) {
		// tagIdList.add(item.getTagId());
		// tagIds += item.getTagId().toString() + ",";
		// }
		// //处理 列表中 标签字段的 展示
		// if (tagIdList.size() > 0) {
		// tags = tagService.selectByIds(tagIdList);
		// for (Tags item : tags) {
		// // 查找 tagId对应的 tagName
		// tagNames += item.getTagName() + " ";
		// }
		// }
		// }

		// 云店名称
		Long orgId = orgStaff.getOrgId();

		Orgs orgs = orgService.selectByPrimaryKey(orgId);
		if (orgs != null) {
			orgStaffVo.setOrgName(orgs.getOrgName());
		}

		// 云店 对应的门店名称
		Long parentOrgId = orgStaff.getParentOrgId();

		Orgs orgs2 = orgService.selectByPrimaryKey(parentOrgId);
		if (orgs2 != null) {
			orgStaffVo.setParentOrgName(orgs2.getOrgName());
		}

		String cityName = dictService.getCityName(orgStaff.getCityId());
		String provinceName = dictService.getProvinceName(orgStaff.getProvinceId());
		// 设置列表页，籍贯字段
		orgStaffVo.setHukou(provinceName + " " + cityName);

		// //助理收入:
		//
		// List<Orders> amOrderList = orderMapper.selectAllAmOrder(staffId);
		//
		// List<Long> orderIdList = new ArrayList<Long>();
		//
		// BigDecimal sumMoney = new BigDecimal(0);
		//
		// if(amOrderList.size()>0){
		// for (Orders order : amOrderList) {
		// orderIdList.add(order.getId());
		// }
		//
		// List<OrderPrices> priceList =
		// orderPriceMapper.selectByOrderIds(orderIdList);
		//
		// //本月流水（订单总额 + 退款手续费 - 退款）的总和
		//
		// for (OrderPrices orderPrices : priceList) {
		//
		// BigDecimal orderMoney = orderPrices.getOrderMoney();
		//
		// BigDecimal orderPayBackFee = orderPrices.getOrderPayBackFee();
		//
		// BigDecimal orderPayBack = orderPrices.getOrderPayBack();
		//
		// BigDecimal subtract =
		// orderMoney.add(orderPayBackFee).subtract(orderPayBack);
		//
		// sumMoney = MathBigDeciamlUtil.add(sumMoney, subtract);
		// }
		// }
		//
		// orgStaffVo.setAmSumMoney(sumMoney);

		orgStaffVo.setTagList(tags);
		orgStaffVo.setTagNames(tagNames);
		orgStaffVo.setTagIds(tagIds);

		// 2016年1月22日18:15:28 该服务人员的认证相关信息
		List<StaffAuthVo> authList = getAuthListForStaff(orgStaff);

		orgStaffVo.setAuthList(authList);

		// 2016年1月22日18:46:10 已通过认证的项目 id （身份认证 id为0）
		String authIds = makeAuthSuccessIds(orgStaff);

		orgStaffVo.setAuthIds(authIds);

		return orgStaffVo;

	}

	@Override
	public SecInfoVo changeSecToVo(OrgStaffs orgStaffs) {

		SecInfoVo secInfoVo = new SecInfoVo();

		BeanUtils.copyProperties(orgStaffs, secInfoVo);

		secInfoVo.setBirthDay(DateUtil.getDefaultDate(orgStaffs.getBirth()));
		secInfoVo.setId(orgStaffs.getStaffId());

		if (secInfoVo.getCityId() > 0L) {

			String cityNameString = dictService.getCityName(secInfoVo.getCityId());
			secInfoVo.setCityName(cityNameString);
		}

		return secInfoVo;
	}

	@Override
	public List<Users> amGetUserList(Long amId, int pageNo, int pageSize) {
		// 助理名下，用户列表
		List<UserRefAm> refAmList = userRefAmService.selectAllUserByAmId(amId);

		List<Long> userList = new ArrayList<Long>();

		for (UserRefAm userRefAm : refAmList) {
			userList.add(userRefAm.getUserId());
		}

		PageHelper.startPage(pageNo, pageSize);

		List<Users> list = new ArrayList<Users>();

		if (!userList.isEmpty()) {
			UserSearchVo searchVo = new UserSearchVo();
			searchVo.setUserIds(userList);
			list = userService.selectBySearchVo(searchVo);
		}

		return list;
	}

	@Override
	public OrgStaffVo initOrgStaffVo() {

		OrgStaffs orgStaffs = initOrgStaffs();

		OrgStaffVo orgStaffVo = new OrgStaffVo();

		BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffs, orgStaffVo);

		orgStaffVo.setAmName("");
		orgStaffVo.setOrgName(""); // 云店名称
		orgStaffVo.setTagList(new ArrayList<Tags>());
		orgStaffVo.setTagNames("");
		orgStaffVo.setTagId("");
		orgStaffVo.setTagIds("");
		orgStaffVo.setHukou("");
		orgStaffVo.setAmSumMoney(new BigDecimal(0));

		orgStaffVo.setNowOrgAmList(new ArrayList<OrgStaffs>());

		// 2016年1月22日17:44:45 ,认证 审核相关
		orgStaffVo.setAuthList(new ArrayList<StaffAuthVo>());

		// 2016年3月9日15:37:31 一级门店名称
		orgStaffVo.setParentOrgName("");

		return orgStaffVo;
	}

	/*
	 * 得到某个服务人员的 认证相关信息
	 * 
	 * 2016年1月22日17:45:49 服务人员认证相关字段
	 * 
	 * 1） 身份认证， 判断条件为 有姓名，手机号和身份证号
	 * 
	 * 2） 钟点工认证 ； 判断条件为 study_staff_pass 为已经通过开始的
	 * 
	 * 3） 助理认证， 判断条件为 study_staff_pass 为已经通过开始的
	 * 
	 * 4） 配送认证。 判断条件为 study_staff_pass 为已经通过开始的
	 */
	private List<StaffAuthVo> getAuthListForStaff(OrgStaffs orgStaffs) {

		List<StaffAuthVo> authList = new ArrayList<StaffAuthVo>();

		// 1.身份认证
		String name = orgStaffs.getName();
		String cardId = orgStaffs.getCardId();
		String mobile = orgStaffs.getMobile();

		StaffAuthVo authVo1 = new StaffAuthVo(0L, "", Constants.STAFF_AUTH_STATUS_FAIL);
		// 身份认证的 serviceTypeId 设置为 0
		authVo1.setServiceTypeId(0L);
		authVo1.setServiceTypeName("身份认证");

		if (!StringUtil.isEmpty(name) && !StringUtil.isEmpty(mobile) && !StringUtil.isEmpty(cardId)) {

			// 2016年1月23日12:22:46 此处 如果 有 三个条件，只表明该“身份认证按钮,可以点击”
			authVo1.setAuthStatus(Constants.STAFF_AUTH_STATUS_SUCCESS);
		}

		// 录入 身份认证
		authList.add(authVo1);

		Long staffId = orgStaffs.getStaffId();

		/*
		 * 三种考试认证
		 */

		// <服务类别Id : 成/败>
		Map<Long, Short> passMap = passQueryService.oaGetStatusByStaffId(staffId);

		Set<Entry<Long, Short>> set = passMap.entrySet();

		for (Entry<Long, Short> entry : set) {

			StaffAuthVo authVo = new StaffAuthVo(0L, "", Constants.STAFF_AUTH_STATUS_FAIL);

			Long key = entry.getKey();

			PartnerServiceType serviceType = partnerService.selectByPrimaryKey(key);

			String name2 = serviceType.getName();

			Short value = entry.getValue();

			authVo.setServiceTypeId(key);
			authVo.setServiceTypeName(name2);

			// 此处的 map的 value值，与 认证的状态，"数值"相等，可直接使用
			authVo.setAuthStatus(value);

			authList.add(authVo);
		}

		return authList;
	}

	// 该服务人员 已经通过 认证的 服务Id，身份认证 id为 0
	private String makeAuthSuccessIds(OrgStaffs orgStaffs) {

		Long staffId = orgStaffs.getStaffId();

		String authIds = "";

		if (staffId > 0L) {
			StaffAuthSearchVo searchVo = new StaffAuthSearchVo();
			searchVo.setStaffId(staffId);
			List<OrgStaffAuth> list = authService.selectBySearchVo(searchVo);

			if (list.size() > 0 && list != null) {
				for (OrgStaffAuth orgStaffAuth : list) {

					authIds += orgStaffAuth.getServiceTypeId() + ",";
				}
			}
		}

		return authIds;
	}

	@Override
	public Boolean userOutBlackSuccessTodo(String mobile) {

		String code = RandomUtil.randomNumber();
		if (mobile.equals("18610807136")) {
			code = "000000";
		}
		String[] content = new String[] { code, Constants.GET_CODE_MAX_VALID };
		HashMap<String, String> sendSmsResult = SmsUtil.SendSms(mobile, Constants.STAFF_OUT_BLACK, content);
		return true;
	}

	@Override
	public NewStaffListVo transToNewStaffListVo(OrgStaffs staffs) {

		NewStaffListVo listVo = new NewStaffListVo();

		BeanUtilsExp.copyPropertiesIgnoreNull(staffs, listVo);

		// 1. 门店名称
		Long parentOrgId = staffs.getParentOrgId();
		Orgs orgs = orgService.selectByPrimaryKey(parentOrgId);

		if (orgs != null) {
			listVo.setParentOrgName(orgs.getOrgName());
		}

		// 2.云店名称
		Long orgId = staffs.getOrgId();
		Orgs orgs2 = orgService.selectByPrimaryKey(orgId);
		listVo.setOrgName("");
		if (orgs2 != null) {
			listVo.setOrgName(orgs2.getOrgName());
		}
		// 3.籍贯
		String cityName = dictService.getCityName(staffs.getCityId());
		String provinceName = dictService.getProvinceName(staffs.getProvinceId());

		listVo.setNativePlace(provinceName + " " + cityName);

		return listVo;
	}

	@Override
	public NewStaffFormVo transToNewStaffFormVo(Long staffId) {

		NewStaffFormVo formVo = initFormVo();

		OrgStaffs orgStaff = initOrgStaffs();

		if (staffId != null && staffId > 0L) {

			orgStaff = selectByPrimaryKey(staffId);

			BeanUtilsExp.copyPropertiesIgnoreNull(orgStaff, formVo);

			// 2016年1月22日18:15:28 该服务人员的认证相关信息
			List<StaffAuthVo> authList = getAuthListForStaff(orgStaff);

			formVo.setAuthList(authList);

			// 2016年1月22日18:46:10 已通过认证的项目 id （身份认证 id为0）
			String authIds = makeAuthSuccessIds(orgStaff);

			formVo.setAuthIds(authIds);

		}

		List<PartnerServiceType> partServiceList = formVo.getPartServiceList();

		OrgStaffSkillSearchVo searchVo = new OrgStaffSkillSearchVo();
		searchVo.setStaffId(staffId);
		List<OrgStaffSkill> skillList = orgStaffSkillService.selectBySearchVo(searchVo);

		for (Iterator iterator = skillList.iterator(); iterator.hasNext();) {
			OrgStaffSkill orgStaffSkill = (OrgStaffSkill) iterator.next();

			if (orgStaffSkill != null) {
				partServiceList.add(partServiceMapper.selectByPrimaryKey(orgStaffSkill.getServiceTypeId()));
			}

		}

		formVo.setPartServiceList(partServiceList);

		/*
		 * 技能标签
		 */
		List<Tags> tags = new ArrayList<Tags>();

		String tagNames = "";
		String tagIds = "";

		if (staffId > 0L) {
			// 根据staffId查找对应的 tagId
			List<OrgStaffTags> staffTags = orgStaTagService.selectByStaffId(staffId);
			// 处理 form中 标签的 显示 和传值
			List<Long> tagIdList = new ArrayList<Long>();
			for (OrgStaffTags item : staffTags) {
				tagIdList.add(item.getTagId());
				tagIds += item.getTagId().toString() + ",";
			}
			// 处理 列表中 标签字段的 展示
			if (tagIdList.size() > 0) {
				
				TagSearchVo searchVo1 = new TagSearchVo();
				searchVo1.setTagIds(tagIdList);
				tags = tagService.selectBySearchVo(searchVo1);
				for (Tags item : tags) {
					// 查找 tagId对应的 tagName
					tagNames += item.getTagName() + " ";
				}
			}
		}

		formVo.setTagIds(tagIds);
		
		TagSearchVo searchVo1 = new TagSearchVo();
		searchVo1.setTagType((short) 0);
		List<Tags> list = tagService.selectBySearchVo(searchVo1);

		formVo.setTagList(list);

		return formVo;
	}

	@Override
	public NewStaffFormVo initFormVo() {

		OrgStaffs staffs = initOrgStaffs();
		NewStaffFormVo formVo = new NewStaffFormVo();
		BeanUtilsExp.copyPropertiesIgnoreNull(staffs, formVo);

		formVo.setPartServiceList(new ArrayList<PartnerServiceType>());
		formVo.setAuthIds("");
		formVo.setAuthList(new ArrayList<StaffAuthVo>());

		formVo.setSkillId(0L);

		// formVo.set。。

		return formVo;
	}

	@Override
	public Map<Long, String> selectSkillEntity() {

		Map<Long, String> map = new HashMap<Long, String>();

		List<PartnerServiceType> list = partServiceMapper.selectAllNoChildService();

		for (PartnerServiceType partnerServiceType : list) {
			map.put(partnerServiceType.getServiceTypeId(), partnerServiceType.getName());
		}

		return map;
	}

	@Override
	public OrgStaffsNewVo initOrgStaffNewVo() {

		OrgStaffsNewVo newVo = new OrgStaffsNewVo();
		OrgStaffs staffs = initOrgStaffs();

		BeanUtilsExp.copyPropertiesIgnoreNull(staffs, newVo);

		newVo.setLat("");
		newVo.setLng("");
		newVo.setLocName("");
		newVo.setDistanceValue(0);
		newVo.setDistanceText("");
		newVo.setDurationValue(0);
		newVo.setDurationText("");
		newVo.setTodayOrderNum(0);
		newVo.setStaffOrgName("");
		newVo.setStaffCloudOrgName("");
		newVo.setDispathStaStr("");
		newVo.setDispathStaFlag(0);

		return newVo;
	}
	
	@Override
	public OrderStaffRateVo getOrderStaffRateVo(OrgStaffs orgStaff) {
		OrderStaffRateVo vo = new OrderStaffRateVo();
		
		vo.setStaffId(orgStaff.getStaffId());
		vo.setName(orgStaff.getName());
		vo.setMobile(orgStaff.getMobile());
		
		String headImg = orgStaff.getHeadImg();
		if (StringUtil.isEmpty(headImg)) headImg = "http://www.jia-he-jia.com/jhj-oa/upload/headImg/default-head-img.png";
		vo.setHeadImg(headImg);
		
		//年龄
		String age = "";
		try {
			age = DateUtil.getAge(orgStaff.getBirth());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!StringUtil.isEmpty(age)) age = age + "岁";
		vo.setAge(age);
		
		String provinceName = dictService.getProvinceName(orgStaff.getProvinceId());
		String cityName = dictService.getCityName(orgStaff.getCityId());
		
		vo.setHukou(provinceName + cityName);
		
		vo.setIntro(orgStaff.getIntro());
		
		String skill = "初级";
		Short level = orgStaff.getLevel();
		if (level.equals((short)1)) skill = "初级";
		if (level.equals((short)2)) skill = "中级";
		if (level.equals((short)3)) skill = "金牌";
		if (level.equals((short)4)) skill = "VIP";
		vo.setSkill(skill);
		
		return vo;
	}
	
}
