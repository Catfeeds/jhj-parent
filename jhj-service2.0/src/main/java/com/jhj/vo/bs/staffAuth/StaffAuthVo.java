package com.jhj.vo.bs.staffAuth;

/**
 *
 * @author :hulj
 * @Date : 2016年1月22日下午5:35:28
 * @Description: 
 *		
 *		服务人员详情页，该服务人员 认证相关信息VO	
 *	
	 * jhj2.0 新增， 服务人员审核时的 认证条件
	 * 		
	 * 	 <seriveTypeId，status>
	 * 
	 * 	 4类 ：  身份认证、钟点工考试认证、助理考试认证、快送考试认证
	 * 		
 */
public class StaffAuthVo {
	
	private Long serviceTypeId;
	private String serviceTypeName;
	private Short authStatus;
	
	public StaffAuthVo(Long serviceTypeId, String serviceTypeName, Short authStatus) {
		super();
		this.serviceTypeId = serviceTypeId;
		this.serviceTypeName = serviceTypeName;
		this.authStatus = authStatus;
	}

	/**
	 * @return the serviceTypeId
	 */
	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	/**
	 * @param serviceTypeId the serviceTypeId to set
	 */
	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	/**
	 * @return the serviceTypeName
	 */
	public String getServiceTypeName() {
		return serviceTypeName;
	}

	/**
	 * @param serviceTypeName the serviceTypeName to set
	 */
	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	/**
	 * @return the authStatus
	 */
	public Short getAuthStatus() {
		return authStatus;
	}

	/**
	 * @param authStatus the authStatus to set
	 */
	public void setAuthStatus(Short authStatus) {
		this.authStatus = authStatus;
	}
	
}
