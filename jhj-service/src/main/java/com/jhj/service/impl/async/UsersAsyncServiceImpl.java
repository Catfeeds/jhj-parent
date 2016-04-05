package com.jhj.service.impl.async;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserRefAm;
import com.jhj.po.model.user.UserRefOrg;
import com.jhj.service.async.UsersAsyncService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UserRefOrgService;
import com.jhj.service.users.UsersService;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.baidu.BaiduMapUtil;
import com.meijia.utils.baidu.BaiduPoiVo;


@Service
public class UsersAsyncServiceImpl implements UsersAsyncService {

	@Autowired
	public UsersService usersService;
	
	@Autowired
	private UserAddrsService userAddrsService;
	
	@Autowired
	private UserRefAmService userRefAmService;
	
	@Autowired
	private UserRefOrgService userRefOrgService;
	
	@Autowired
	private OrgsService orgsService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;	
	/**
	 * 分配云店的方法
	 */
	@Async
	@Override
	public Future<Boolean> allotOrg(Long addrId) {
		
		UserAddrs userAddrs = userAddrsService.selectByPrimaryKey(addrId);
		Long userId = userAddrs.getUserId();
		String fromLat = userAddrs.getLatitude();
		String fromLng = userAddrs.getLongitude();
		
		UserRefOrg userRefOrg = userRefOrgService.selectByUserId(userId);
		
		if (userRefOrg == null) userRefOrg = userRefOrgService.initUserRefOrg();
		
		//找出所有的云店.
		List<Orgs> orgList = orgsService.selectCloudOrgs();
		
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
		
		if (resultAddrs.isEmpty()) return new AsyncResult<Boolean>(true);

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
		
		if (matchOrg == null) return new AsyncResult<Boolean>(true);
		
		userRefOrg.setUserId(userId);
		userRefOrg.setOrgId(matchOrg.getOrgId());
		userRefOrg.setParentOrgId(matchOrg.getParentId());
		
		if (userRefOrg.getId() > 0L) {
			
			userRefOrgService.updateByPrimaryKeySelective(userRefOrg);
		} else {
			userRefOrgService.insert(userRefOrg);
		}
		
		return new AsyncResult<Boolean>(true);
	}
	
	
	
	/**
	 * 分配助理方法
	 */
	@Async
	@Override
	public Future<Boolean> allotAm(Long addrId) {
		UserAddrs userAddrs = userAddrsService.selectByPrimaryKey(addrId);
		Long userId = userAddrs.getUserId();
		String fromLat = userAddrs.getLatitude();
		String fromLng = userAddrs.getLongitude();
		//分配助理
		UserRefAm userRefAm = userRefAmService.selectByAmId(userId);
		
		if (userRefAm != null) return new AsyncResult<Boolean>(true);
		

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
		
		if (resultAddrs.isEmpty()) return new AsyncResult<Boolean>(true);

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
		
		if (matchOrg == null) return new AsyncResult<Boolean>(true);

		//根据OrgId 找到对应的助理列表
		List<OrgStaffs> amList = orgStaffsService.selectAmByOrgId(matchOrg.getOrgId());
		
		List<Long> amIds = new ArrayList<Long>();
		for (OrgStaffs orgStaff : amList) {
			amIds.add(orgStaff.getStaffId());
		}
		
		if (amIds.isEmpty()) return new AsyncResult<Boolean>(true);
		
		//计算每个助理的分配人数，并且平均分配
		List<HashMap> totalAmIds = userRefAmService.totalByAmIds(amIds);
		
		if (totalAmIds.isEmpty()) return new AsyncResult<Boolean>(true);
		
		String matchAmId = totalAmIds.get(0).get("staff_id").toString();
		
		if (StringUtil.isEmpty(matchAmId)) return new AsyncResult<Boolean>(true);	
		userRefAm = new UserRefAm();
		userRefAm.setId(0L);
		userRefAm.setStaffId(Long.valueOf(matchAmId));
		userRefAm.setStatus((short) 1);
		userRefAm.setUserId(userId);
		userRefAm.setAddTime(TimeStampUtil.getNowSecond());
		userRefAmService.insert(userRefAm);
		
		return new AsyncResult<Boolean>(true);
	}	


}
