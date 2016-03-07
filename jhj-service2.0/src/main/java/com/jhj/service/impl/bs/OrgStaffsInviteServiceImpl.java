package com.jhj.service.impl.bs;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.common.Constants;
import com.jhj.po.dao.bs.OrgStaffInviteMapper;
import com.jhj.po.model.bs.OrgStaffInvite;
import com.jhj.service.bs.OrgStaffInviteService;
import com.jhj.vo.OrgStaffFinanceSearchVo;
import com.meijia.utils.RandomUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.TimeStampUtil;
/**
 *
 * @author :Aimee
 * @Date : 2016年1月27日上午11:50
 * @Description: 
 *
 */
@Service
public class OrgStaffsInviteServiceImpl implements OrgStaffInviteService {
	@Autowired
	private OrgStaffInviteMapper orgStaffInviteMapper;

	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return orgStaffInviteMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrgStaffInvite orgStaffInvite) {
		return orgStaffInviteMapper.insert(orgStaffInvite);
	}

	@Override
	public int insertSelective(OrgStaffInvite orgStaffInvite) {
		return orgStaffInviteMapper.insertSelective(orgStaffInvite);
	}

	@Override
	public OrgStaffInvite selectByPrimaryKey(Long id) {
		return orgStaffInviteMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrgStaffInvite orgStaffInvite) {
		return orgStaffInviteMapper.updateByPrimaryKeySelective(orgStaffInvite);
	}

	@Override
	public int updateByPrimaryKey(OrgStaffInvite orgStaffInvite) {
		return orgStaffInviteMapper.updateByPrimaryKey(orgStaffInvite);
	}
	@Override
	public OrgStaffInvite initOrgStaffInvite() {

		OrgStaffInvite orgStaffInvite = new OrgStaffInvite();
	    orgStaffInvite.setId(0L);
	    orgStaffInvite.setStaffId(0L);
	    orgStaffInvite.setInviteMobile("");
	    orgStaffInvite.setInviteStaffId(0L);
	    orgStaffInvite.setInviteOrderCount((short) 0);
	    orgStaffInvite.setInviteStatus((short) 1);
	    orgStaffInvite.setAddTime(TimeStampUtil.getNow() / 1000);
		return orgStaffInvite;
	}

	@Override
	public OrgStaffInvite selectByMobile(String mobile) {
		
		return orgStaffInviteMapper.selectByMobile(mobile);
	}

	@Override
	public Boolean userOrderAmSuccessTodo(String mobile) {
	//	OrderPrices orderPrices = orderPricesService.selectByOrderId(order.getId());
			String code = RandomUtil.randomNumber();
			if (mobile.equals("18610807136")) {
				code = "000000";
			}
			//String orderMoney = orderPrices.getOrderMoney().toString();
			String[] content = new String[] { code , mobile};
			HashMap<String, String> sendSmsResult = SmsUtil.SendSms(mobile,
					Constants.AM_NOTICE_CUSTOMER_Message, content);
		return true;
	}

	@Override
	public List<OrgStaffInvite> selectByInviteStaffIdAndStatus() {
		
		return orgStaffInviteMapper.selectByInviteStaffIdAndStatus();
	}

	@Override
	public List<OrgStaffInvite> selectByListPage(OrgStaffFinanceSearchVo searvhVo,int pageNo, int pageSize) {
		
		PageHelper.startPage(pageNo, pageSize);
		List<OrgStaffInvite> list = orgStaffInviteMapper.selectByListPage(searvhVo);
		
		return list;
		
	}
}
