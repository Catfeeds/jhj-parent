package com.jhj.service.university;

import org.json.JSONException;

import com.jhj.vo.university.AppUniversityResultVo;

/**
 *
 * @author :hulj
 * @Date : 2016年1月18日下午5:13:46
 * @Description: 
 *		
 *		处理 答题结果的 service
 */
public interface UniversityResultService {
	
	
   AppUniversityResultVo insertRecord(Long staffId,Long serviceTypeId,String universityResult)  throws JSONException;
	
	
}	
