package com.jhj.service.university;

import java.util.List;
import java.util.Map;

import com.jhj.vo.university.AppStaffTestFirstVo;

/**
 *
 * @author :hulj
 * @Date : 2016年1月8日下午6:40:28
 * @Description: 
 *		
 *		查询  服务人员的  考试通过状态
 */
public interface StudyStaffPassQueryService {
	
	//运营平台--身份审核
	Map<Long, Short> oaGetStatusByStaffId(Long staffId);
	
	//app--叮当大学首页，加载考试状态
	List<AppStaffTestFirstVo>  appGetStatusByStaffId(Long staffId);
	
	Short getStatusByStaffIdAndServiceType(Long staffId,Long serviceTypeId);
	
}
