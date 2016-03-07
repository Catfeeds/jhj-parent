package com.jhj.service.impl.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.user.UserAddrsMapper;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserRefAm;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserRefAmService;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.baidu.BaiduMapUtil;
import com.meijia.utils.baidu.BaiduPoiVo;

@Service
public class UserAddrsServiceImpl implements UserAddrsService {
	@Autowired
	private UserAddrsMapper userAddrsMapper;
	
	@Autowired
	private UserRefAmService userRefAmService;
	
	@Autowired
	private OrgsService orgsService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;	
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return userAddrsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Long insert(UserAddrs record) {
		return userAddrsMapper.insert(record);
	}

	@Override
	public int insertSelective(UserAddrs record) {
		return userAddrsMapper.insertSelective(record);
	}

	@Override
	public UserAddrs selectByPrimaryKey(Long id) {
		return userAddrsMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public UserAddrs selectByDefaultAddr(Long userId) {
		return userAddrsMapper.selectByDefaultAddr(userId);
	}	

	@Override
	public List<UserAddrs> selectByMobile(String mobile) {
		return userAddrsMapper.selectByMobile(mobile);
	}
	@Override
	public int updataDefaultByMobile(String mobile){
		return userAddrsMapper.updateDefaultByMobile(mobile);
	}

	@Override
	public int updateByPrimaryKey(UserAddrs record) {
		return userAddrsMapper.updateByPrimaryKey(record);
	}



	@Override
	public int updateByPrimaryKey(UserAddrs record, boolean updateOther) {
		if(updateOther) {//取消其他默认地址，修改当前为默认地址
			if(userAddrsMapper.updateByMobile(record.getMobile())>0) {
				return userAddrsMapper.updateByPrimaryKey(record);
			}
			return 0;
		}
		return userAddrsMapper.updateByPrimaryKey(record);
	}


	@Override
	public int updateByPrimaryKeySelective(UserAddrs record) {
		return userAddrsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<UserAddrs> getAll(){
		return userAddrsMapper.selectAll();
	}

	@Override
	public List<UserAddrs> selectByUserId(Long userId) {
		return userAddrsMapper.selectByUserId(userId);
	}

	@Override
	public List<UserAddrs> selectByIds(List<Long> addrIds) {
		return userAddrsMapper.selectByIds(addrIds);
	}

	@Override
	public int updataDefaultByUserId(Long userId) {
		return userAddrsMapper.updateDefaultByUserId(userId);
	}
	
	@Override
	public int updataDefaultById(Long addrId) {
		return userAddrsMapper.updateDefaultById(addrId);
	}

	@Override
	public UserAddrs initUserAddrs() {
		UserAddrs userAddrs = new UserAddrs();
		userAddrs.setId(0L);
		userAddrs.setUserId(0L);
		userAddrs.setMobile(" ");
		userAddrs.setAddr(" ");
		userAddrs.setLongitude(" ");
		userAddrs.setLatitude(" ");
		userAddrs.setPoiType((short)0);
		userAddrs.setName(" ");
		userAddrs.setAddress(" ");
		userAddrs.setCity(" ");
		userAddrs.setUid(" ");
		userAddrs.setPhone(" ");
		userAddrs.setPostCode(" ");
		userAddrs.setUpdateTime(TimeStampUtil.getNow() / 1000);
		userAddrs.setIsDefault((short)1);
		userAddrs.setOrgId(0L);
		return userAddrs;
	}

	@Override
	public UserAddrs selectByUserIdAndAddrId(Long addrId, Long userId) {
		return userAddrsMapper.selectByUserIdAndAddrId(addrId, userId);
	}
	@Override
	public UserAddrs selectUserId(Long userId) {
		return userAddrsMapper.selectUserId(userId);
	}
	
	/**
	 * 分配助理方法
	 */
	@Override
	public Boolean allotAm(Long addrId) {
		UserAddrs userAddrs = userAddrsMapper.selectByPrimaryKey(addrId);
		Long userId = userAddrs.getUserId();
		String fromLat = userAddrs.getLatitude();
		String fromLng = userAddrs.getLongitude();
		//分配助理
		UserRefAm userRefAm = userRefAmService.selectByAmId(userId);
		
		if (userRefAm != null) return true;
		

		//先找出所有的门店. 后续会根据城市来寻找.
		List<Orgs> orgList = orgsService.selectAll();
		
		List<BaiduPoiVo> destAddrs = new ArrayList<BaiduPoiVo>();
		Orgs item = null;
		for (int i =0; i < orgList.size(); i++) {
			BaiduPoiVo baiduPoiVo = new BaiduPoiVo();
			item = orgList.get(i);
			baiduPoiVo.setName(item.getOrgName());
			baiduPoiVo.setLat(item.getPoiLatitude());
			baiduPoiVo.setLng(item.getPoiLongitude());
			destAddrs.add(baiduPoiVo);
		}
		
		List<BaiduPoiVo> resultAddrs = new ArrayList<BaiduPoiVo>();
		try {
			resultAddrs = BaiduMapUtil.getMapRouteMatrix(fromLat, fromLng, destAddrs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (resultAddrs.isEmpty()) return false;

		Collections.sort(resultAddrs, new Comparator<BaiduPoiVo>() {
		    @Override
			public int compare(BaiduPoiVo s1, BaiduPoiVo s2) {
		        return Integer.valueOf(s1.getDistanceValue()).compareTo(s2.getDistanceValue());
		    }
		});
		
		BaiduPoiVo matchItem = resultAddrs.get(0);
		Orgs matchOrg = null;
		for (int i =0; i < orgList.size(); i++) {
			item = orgList.get(i);
			if (matchItem.getLat().equals(item.getPoiLatitude()) &&
				matchItem.getLng().equals(item.getPoiLongitude())) {
				matchOrg = item;
				break;
			}
		}
		
		if (matchOrg == null) return false;

		//根据OrgId 找到对应的助理列表
		List<OrgStaffs> amList = orgStaffsService.selectAmByOrgId(matchOrg.getOrgId());
		
		List<Long> amIds = new ArrayList<Long>();
		for (OrgStaffs orgStaff : amList) {
			amIds.add(orgStaff.getStaffId());
		}
		
		if (amIds.isEmpty()) return false;
		
		//计算每个助理的分配人数，并且平均分配
		List<HashMap> totalAmIds = userRefAmService.totalByAmIds(amIds);
		
		if (totalAmIds.isEmpty()) return false;
		
		String matchAmId = totalAmIds.get(0).get("staff_id").toString();
		
		if (StringUtil.isEmpty(matchAmId)) return false;		
		userRefAm = new UserRefAm();
		userRefAm.setId(0L);
		userRefAm.setStaffId(Long.valueOf(matchAmId));
		userRefAm.setStatus((short) 1);
		userRefAm.setUserId(userId);
		userRefAm.setAddTime(TimeStampUtil.getNowSecond());
		userRefAmService.insert(userRefAm);
		
		return true;
	}

	@Override
	public UserAddrs selectByNameAndAddr(Long userId, String name, String addr) {
		return 	userAddrsMapper.selectByNameAndAddr(userId, name.trim(), addr.trim());
	}

	@Override
	public UserAddrs selectByUidAndUserId(String uid, Long userId) {
		return userAddrsMapper.selectByUidAndUserId(uid, userId);
	}
	

}
	

