package com.jhj.service.impl.university;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.dao.university.StudyStaffPassMapper;
import com.jhj.po.model.university.StudyBank;
import com.jhj.po.model.university.StudyStaffPass;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.university.StudyBankService;
import com.jhj.service.university.StudyStaffPassQueryService;
import com.jhj.service.university.StudyStaffTestService;
import com.jhj.vo.university.AppStaffTestFirstVo;
import com.jhj.vo.university.DaoStaffTestMapVo;

/**
 *
 * @author :hulj
 * @Date : 2016年1月9日上午10:48:12
 * @Description: 
 *					
 *			
 *		服务人员   考试 考核情况	
 *
 */
@Service
public class StudyStaffPassQueryServiceImpl implements StudyStaffPassQueryService {

	
	@Autowired
	private StudyStaffPassMapper passMapper;
	
	@Autowired
	private PartnerServiceTypeService partnerService;
	
	@Autowired
	private StudyStaffTestService testService;
	
	@Autowired
	private StudyBankService bankService;
	
	/*
	 * 运营平台--服务人员审核时使用
	 */
	@Override
	public Map<Long, Short> oaGetStatusByStaffId(Long staffId) {
		
		//所有 一级 服务 的  serviceTypeId
		List<Long> noParentServiceList = partnerService.selectNoParentServiceId();
		
		//构造 <服务Id : 失败>的 map
		Map<Long, Short> resultMap = new HashMap<Long, Short>();
		for (Long long1 : noParentServiceList) {
			resultMap.put(long1, Constants.UNIVERSITY_STAFF_PASS_STATUS_FAIL);
		}
		
		/*
		 *  如果 用户 有通过的  记录， 则 serviceTypeId 也必然是  noParentServiceList中的值，
		 * 
		 * 	利用hashMap 无重复key， 得到 <serviceTypeId：成/败>		
		 */
		List<StudyStaffPass> list = passMapper.selectByStaffId(staffId);
		if(list.size() > 0 && list !=null){
			
			// study_staff_pass表 只存储 ，考试通过的记录
			for (StudyStaffPass studyStaffPass : list) {
				
				Long serviceTypeId = studyStaffPass.getServiceTypeId();
				resultMap.put(serviceTypeId, Constants.UNIVERSITY_STAFF_PASS_STATUS_SUCCESS);
			}
		}
		
		
		return resultMap;
	}
	
	/*
	 * app--叮当大学--首页加载员工考核状态时使用
	 */
	@Override
	public List<AppStaffTestFirstVo> appGetStatusByStaffId(Long staffId) {
		
		//所有 一级 服务 的  serviceTypeId（可能包含不需要考核的service,如深度保洁）
		
		List<Long> noParentServiceList = partnerService.selectNoParentServiceId();
		
		/*
		 * 返回  vo{
		 * 		最近时间：xx, 	//如果已通过，为 study_staff_pass表的 add_time,
		 * 					     如果未通过,则为 study_staff_test表 ，该服务的最新的 add_time	
		 * 		服务Id:xx, 
		 * 		是否通过:xx 
		 * 		}
		 */
		List<AppStaffTestFirstVo> firstVoList = new ArrayList<AppStaffTestFirstVo>();
		
		//已通过的 记录
		List<StudyStaffPass> list = passMapper.selectByStaffId(staffId);
		
		//该staff 已经考试通过的 serviceTypeId
		List<Long> testSuccessIdList = new ArrayList<Long>();
		
		
		if(list.size() > 0 && list !=null){
			
			// study_staff_pass表 只存储 ，考试通过的记录
			for (StudyStaffPass studyStaffPass : list) {
				
				Long serviceTypeId = studyStaffPass.getServiceTypeId();
				
				AppStaffTestFirstVo firstVo = new AppStaffTestFirstVo();
				
				firstVo.setServiceTypeId(serviceTypeId);
				firstVo.setTestStatus(Constants.UNIVERSITY_STAFF_PASS_STATUS_SUCCESS);
				firstVo.setLastTestTime(studyStaffPass.getAddTime());
				
				//1. 已通过的service
				firstVoList.add(firstVo);
				
				testSuccessIdList.add(studyStaffPass.getServiceTypeId());
			}
		}
		
		
		//未通过的 serviceTypeId 集合
		noParentServiceList.removeAll(testSuccessIdList);
		
		for (Long serviceTypeId : noParentServiceList) {
			
			AppStaffTestFirstVo firstVo = new AppStaffTestFirstVo();
			
			firstVo.setServiceTypeId(serviceTypeId);
			firstVo.setTestStatus(Constants.UNIVERSITY_STAFF_PASS_STATUS_FAIL);
			firstVo.setLastTestTime(0L);
			
			firstVoList.add(firstVo);
		}
		
		
		/*
		 * 通过staff_id,从 study_staff_test表中 得到 该服务人员最新的考试记录
		 * 
		 * 		返回 {最新时间：x, bankId:xx, staffId:xx}	
		 * 	
		 * 	注:	某服务可能会有多个 bank,则从 多个 bank中，确定该 服务的最新的 时间
		 */
		
		List<DaoStaffTestMapVo> latestList = testService.selectLatestTimeByStaffId(staffId);
		
		//<服务类型:时间>
		Map<Long, Long> bankMap = new HashMap<Long, Long>();
		
		/*
		 * 通过hashmap无重复key, 得到 同一个serviceType, 两个题库中最新最近 的考试时间
		 */
		
		if(latestList.size() > 0 && latestList !=null){
			
			for (DaoStaffTestMapVo daoStaffTestMapVo : latestList) {
				
				Long bankId = daoStaffTestMapVo.getLatestBankId();
				
				StudyBank studyBank = bankService.selectByPrimaryKey(bankId);
				
				Long serviceTypeId = studyBank.getServiceTypeId();
				
				Long latestAddTime = daoStaffTestMapVo.getLatestAddTime();
				
				bankMap.put(serviceTypeId, latestAddTime);
				
			}
		}
		
		if(bankMap.size()>0 && bankMap !=null){
			
			Set<Entry<Long,Long>> entrySet = bankMap.entrySet();
			
			for (Entry<Long, Long> entry : entrySet) {
				
				Long key = entry.getKey();
				
				for (AppStaffTestFirstVo testVo : firstVoList) {
					
					if(testVo.getServiceTypeId() == key){
						
						/*
						 * 在这里修改 未通过 考试 的 service 的最新最近的考试时间 
						 */
						testVo.setLastTestTime(entry.getValue());
					}
				}
			}
		}
		
		
		return firstVoList;
	}

	@Override
	public Short getStatusByStaffIdAndServiceType(Long staffId, Long serviceTypeId) {
		
		StudyStaffPass staffPass = passMapper.selectByStaffIdAndServiceTypeId(staffId, serviceTypeId);
		
		// 能查到记录 就表示 ,该服务人员、该类别考试 通过
		if(staffPass !=null){
			return Constants.UNIVERSITY_STAFF_PASS_STATUS_SUCCESS;
		}else{
			return Constants.UNIVERSITY_STAFF_PASS_STATUS_FAIL;
		}
	}
}
