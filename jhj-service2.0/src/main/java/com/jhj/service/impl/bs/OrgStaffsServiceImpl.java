package com.jhj.service.impl.bs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.common.Constants;
import com.jhj.po.dao.bs.OrgStaffsMapper;
import com.jhj.po.dao.order.OrderPricesMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.model.bs.OrgStaffAuth;
import com.jhj.po.model.bs.OrgStaffTags;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.bs.Tags;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserRefAm;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffAuthService;
import com.jhj.service.bs.OrgStaffTagsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.bs.TagsService;
import com.jhj.service.dict.DictService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.university.StudyStaffPassQueryService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.StaffSearchVo;
import com.jhj.vo.bs.OrgStaffVo;
import com.jhj.vo.bs.SecInfoVo;
import com.jhj.vo.bs.staffAuth.StaffAuthVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.MathBigDeciamlUtil;
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
		return orgStaMapper.insertSelective(orgStaffs);
	}

	@Override
	public OrgStaffs selectByPrimaryKey(Long staffId) {
		return orgStaMapper.selectByPrimaryKey(staffId);
	}

	@Override
	public int updateByPrimaryKeySelective(OrgStaffs orgStaffs) {
		return orgStaMapper.updateByPrimaryKeySelective(orgStaffs);
	}

	@Override
	public int updateByPrimaryKey(OrgStaffs orgStaffs) {
		return orgStaMapper.updateByPrimaryKey(orgStaffs);
	}

	@Override
	public List<OrgStaffs> selectAll() {
		return orgStaMapper.selectAll();
	}

	@Override
	public List<OrgStaffs> selectByListPage(StaffSearchVo staffSearchVo, int pageNo, int pageSize) {

		PageHelper.startPage(pageNo, pageSize);
		List<OrgStaffs> lists = orgStaMapper.selectByListPage(staffSearchVo);
		// PageInfo result = new PageInfo(lists);
		return lists;
	}

	@Override
	public OrgStaffs initOrgStaffs() {

		OrgStaffs orgStaffs = new OrgStaffs();

		orgStaffs.setStaffId(0L);
		
		orgStaffs.setAmId(0L);
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
		orgStaffs.setHeadImg("");
		orgStaffs.setIntro("");
		orgStaffs.setAddTime(TimeStampUtil.getNow() / 1000);
		orgStaffs.setUpdateTime(0L);

		return orgStaffs;
	}

	@Override
	public OrgStaffs selectOrgIdByStaffId(Long staffId) {
		return orgStaMapper.selectOrgIdByStaffId(staffId);
	}
	
	/*
	 * 传入  服务人员 对象。。得到对应的 助理人员 姓名
	 */
	@Override
	public String getAmName(OrgStaffs orgStaff){
		
		Long amId = orgStaff.getAmId();
		OrgStaffs staff = orgStaMapper.selectByPrimaryKey(amId);
		
		String amName = "";
		if(staff != null){
			amName = staff.getName();
		}
		return amName;
	}
	
	
	/*
	 * 这个方法 在   服务人员列表页、表单；  助理人员 列表、表单 都会用到！！！
	 */
	@Override
	public OrgStaffVo genOrgStaffVo(OrgStaffs orgStaff) {

//		OrgStaffVo orgStaffVo = new OrgStaffVo();
		
		OrgStaffVo orgStaffVo = initOrgStaffVo();
		
		BeanUtils.copyProperties(orgStaff, orgStaffVo);
		
		//设置员工 对应的  助理名称
		String amName = getAmName(orgStaff);
		orgStaffVo.setAmName(amName);
//		orgStaffVo.setAmId(orgStaff.getStaffId());
		
		String tagNames = "";
		String tagIds = "";

		Long staffId = orgStaff.getStaffId();
		List<Tags> tags = new ArrayList<Tags>();

		if (staffId > 0L) {
			// 根据staffId查找对应的 tagId
			List<OrgStaffTags> staffTags = orgStaTagService.selectByStaffId(staffId);
			// 处理 form中 标签的 显示 和传值
			List<Long> tagIdList = new ArrayList<Long>();
			for (OrgStaffTags item : staffTags) {
				tagIdList.add(item.getTagId());
				tagIds += item.getTagId().toString() + ",";
			}
			//处理 列表中 标签字段的 展示
			if (tagIdList.size() > 0) {
				tags = tagService.selectByIds(tagIdList);
				for (Tags item : tags) {
					// 查找 tagId对应的 tagName
					tagNames += item.getTagName() + " ";
				}				
			}
		}
		
		//设置 列表页，门店字段	
		Long orgId = orgStaff.getOrgId();
		Orgs orgs = orgService.selectByPrimaryKey(orgId);
		if(orgs!=null){
			
			orgStaffVo.setOrgName(orgs.getOrgName());
		}else{
			orgStaffVo.setOrgName("");
		}
		
		String cityName = dictService.getCityName(orgStaff.getCityId());
	    String provinceName = dictService.getProvinceName(orgStaff.getProvinceId());
	    //设置列表页，籍贯字段
	    orgStaffVo.setHukou(provinceName+" "+cityName);		
	    
	    //助理收入: 
	    
	    List<Orders> amOrderList = orderMapper.selectAllAmOrder(staffId);
	    
	    List<Long> orderIdList = new ArrayList<Long>();
		
		BigDecimal sumMoney  = new BigDecimal(0);
		
		if(amOrderList.size()>0){
			for (Orders order : amOrderList) {
				orderIdList.add(order.getId());
			}
			
			List<OrderPrices> priceList = orderPriceMapper.selectByOrderIds(orderIdList);
			
			//本月流水（订单总额 + 退款手续费 - 退款）的总和
			
			for (OrderPrices orderPrices : priceList) {
				
				BigDecimal orderMoney = orderPrices.getOrderMoney();
				
				BigDecimal orderPayBackFee = orderPrices.getOrderPayBackFee();
				
				BigDecimal orderPayBack = orderPrices.getOrderPayBack();
				
				BigDecimal subtract = orderMoney.add(orderPayBackFee).subtract(orderPayBack);
				
				sumMoney = MathBigDeciamlUtil.add(sumMoney, subtract);
			}
		}
		
		orgStaffVo.setAmSumMoney(sumMoney);
	    
		orgStaffVo.setTagList(tags);
		orgStaffVo.setTagNames(tagNames);
		orgStaffVo.setTagIds(tagIds);
		
		
		//2016年1月22日18:15:28     该服务人员的认证相关信息
		List<StaffAuthVo> authList = getAuthListForStaff(orgStaff);
		
		orgStaffVo.setAuthList(authList);
		
		//2016年1月22日18:46:10  已通过认证的项目 id  （身份认证 id为0）
		String authIds = makeAuthSuccessIds(orgStaff);
		
		orgStaffVo.setAuthIds(authIds);
		
		return orgStaffVo;

	}


	@Override
	public SecInfoVo changeSecToVo(OrgStaffs orgStaffs) {

			SecInfoVo secInfoVo = new SecInfoVo();
			
			BeanUtils.copyProperties(orgStaffs,secInfoVo);
			

			secInfoVo.setBirthDay(DateUtil.getDefaultDate(orgStaffs.getBirth()));
			secInfoVo.setId(orgStaffs.getStaffId());

			if (secInfoVo.getCityId() > 0L) {

				String cityNameString = dictService.getCityName(secInfoVo
						.getCityId());
				secInfoVo.setCityName(cityNameString);
			}
			
			//SecRef3rd secRef3rd = this.selectBySecIdForIm(orgStaffs.getStaffId());
			/*secInfoVo.setImUserName(secRef3rd.getUsername());
			secInfoVo.setImPassword(secRef3rd.getPassword());*/
			

			return secInfoVo;

		}
	
	@Override
	public List<Users> amGetUserList(Long amId,int pageNo,int pageSize){
		//助理名下，用户列表
	    List<UserRefAm> refAmList = userRefAmService.selectAllUserByAmId(amId);
		
	    List<Long> userList = new ArrayList<Long>();
	    
	    for (UserRefAm userRefAm: refAmList) {
	    	userList.add(userRefAm.getUserId());
		}
	    
	    PageHelper.startPage(pageNo, pageSize);
	    
	    List<Users> list = new ArrayList<Users>();
	    
	    if(!userList.isEmpty()){
	    	 list = userService.selectByUserIds(userList);
	    }
	    
		return list;
	}
	
	
	@Override
	public OrgStaffs selectByCardId(String cardId) {
		return orgStaMapper.selectByCardId(cardId);
	}

	@Override
	public OrgStaffs selectByMobile(String mobile) {
		return orgStaMapper.selectByMobile(mobile);
	}
	@Override
	public List<OrgStaffs> selectByids(List<Long> ids) {
		
		return orgStaMapper.selectByIds(ids);
	}

	@Override
	public List<OrgStaffs> selectByOrgId(Long orgId) {
		return orgStaMapper.selectByOrgId(orgId);
	}
	
	@Override
	public List<OrgStaffs> selectByOrgIdAndType(StaffSearchVo staffSearchVo) {
		return orgStaMapper.selectByOrgIdAndType(staffSearchVo);
	}	
	
	@Override
	public List<OrgStaffs> selectAmByOrgId(Long orgId) {
		return orgStaMapper.selectAmByOrgId(orgId);
	}	

	@Override
	public List<OrgStaffs> selectAllAm() {
		return orgStaMapper.selectAllAm();
	}

	@Override
	public List<OrgStaffs> selectStaffByAmIdListPage(Long amId, int pageNo, int pageSize) {
		
		PageHelper.startPage(pageNo, pageSize);
		
		StaffSearchVo searchVo = new StaffSearchVo();
		
		searchVo.setAmId(amId);
		
		List<OrgStaffs> list = orgStaMapper.selectByListPage(searchVo);
		
		
		return list;
	}

	@Override
	public List<OrgStaffs> selectStaffByOrgId(Long orgId) {
		return orgStaMapper.selectStaffByOrgId(orgId);
	}

	@Override
	public List<OrgStaffs> selectStaffByAmId(Long amId) {
		return orgStaMapper.selectStaffByAmId(amId);
	}

	@Override
	public OrgStaffVo initOrgStaffVo() {
		
		OrgStaffs orgStaffs = initOrgStaffs();
		
		OrgStaffVo orgStaffVo = new OrgStaffVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffs, orgStaffVo);
		
		orgStaffVo.setAmName("");
		orgStaffVo.setOrgName("");
		orgStaffVo.setTagList(new ArrayList<Tags>());
		orgStaffVo.setTagNames("");
		orgStaffVo.setTagId("");
		orgStaffVo.setTagIds("");
		orgStaffVo.setHukou("");
		orgStaffVo.setAmSumMoney(new BigDecimal(0));
		
		orgStaffVo.setNowOrgAmList(new ArrayList<OrgStaffs>());
		
		//2016年1月22日17:44:45 ,认证 审核相关
		orgStaffVo.setAuthList(new ArrayList<StaffAuthVo>());
		
		return orgStaffVo;
	}

	@Override
	public List<OrgStaffs> selectAllStaff() {
		
		List<OrgStaffs> list = orgStaMapper.selectAllStaff();
		return list;
	}

	@Override
	public OrgStaffs selectAmByStaffId(Long staffId) {
		
		OrgStaffs staffs = orgStaMapper.selectAmByStaffId(staffId);
		return staffs;
	}


	/*
	 *	得到某个服务人员的 认证相关信息	
	 *
		 * 2016年1月22日17:45:49  服务人员认证相关字段
		 * 	
		 * 	1） 身份认证， 判断条件为  有姓名，手机号和身份证号

			2） 钟点工认证 ； 判断条件为  study_staff_pass 为已经通过开始的
			
			3） 助理认证，  判断条件为  study_staff_pass 为已经通过开始的
			
			4） 配送认证。 判断条件为  study_staff_pass 为已经通过开始的
	 *
	 */
	private List<StaffAuthVo> getAuthListForStaff(OrgStaffs orgStaffs){
		
		List<StaffAuthVo> authList = new ArrayList<StaffAuthVo>();
		
		
		//1.身份认证
		String name = orgStaffs.getName();
		String cardId = orgStaffs.getCardId();
		String mobile = orgStaffs.getMobile();
		
		StaffAuthVo authVo1 = new StaffAuthVo(0L, "", Constants.STAFF_AUTH_STATUS_FAIL);
		//身份认证的 serviceTypeId 设置为 0
		authVo1.setServiceTypeId(0L); 
		authVo1.setServiceTypeName("身份认证");
		
		if(!StringUtil.isEmpty(name) 
				&& !StringUtil.isEmpty(mobile) 
				&& !StringUtil.isEmpty(cardId)){
			
			//2016年1月23日12:22:46  此处 如果 有 三个条件，只表明该“身份认证按钮,可以点击”
			authVo1.setAuthStatus(Constants.STAFF_AUTH_STATUS_SUCCESS);
		}
		
		// 录入 身份认证
		authList.add(authVo1);
		
		
		Long staffId = orgStaffs.getStaffId();
		
		/*
		 *  三种考试认证
		 */
		
		//<服务类别Id : 成/败>
		Map<Long, Short> passMap = passQueryService.oaGetStatusByStaffId(staffId);
		
		Set<Entry<Long,Short>> set = passMap.entrySet();
		
		for (Entry<Long, Short> entry : set) {
			
			StaffAuthVo authVo = new StaffAuthVo(0L, "", Constants.STAFF_AUTH_STATUS_FAIL);
			
			Long key = entry.getKey();
			
			PartnerServiceType serviceType = partnerService.selectByPrimaryKey(key);
			
			String name2 = serviceType.getName();
			
			Short value = entry.getValue();
			
			authVo.setServiceTypeId(key);
			authVo.setServiceTypeName(name2);
			
			//此处的 map的 value值，与 认证的状态，"数值"相等，可直接使用
			authVo.setAuthStatus(value);
			
			authList.add(authVo);
		}
		
		return authList;
	}
	
	// 该服务人员 已经通过 认证的 服务Id，身份认证 id为 0
	private String makeAuthSuccessIds(OrgStaffs orgStaffs){
		
		Long staffId = orgStaffs.getStaffId();
		
		String authIds = "";
		
		if (staffId > 0L) {
			
			List<OrgStaffAuth> list = authService.selectByStaffId(staffId);
			
			if(list.size() >0 && list !=null){
				for (OrgStaffAuth orgStaffAuth : list) {
					
					authIds += orgStaffAuth.getServiceTypeId()+",";
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
		String[] content = new String[] { code,
				Constants.GET_CODE_MAX_VALID };
		HashMap<String, String> sendSmsResult = SmsUtil.SendSms(mobile,
				Constants.STAFF_OUT_BLACK, content);
		return true;
	}
	
	@Override
	public List<OrgStaffs> selectAbleToSendMsgStaff() {
		return orgStaMapper.selectAbleToSendMsgStaff();
	}
}
