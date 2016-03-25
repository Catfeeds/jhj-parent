package com.jhj.vo.bs;

import java.util.ArrayList;
import java.util.List;

import com.jhj.po.model.admin.AdminAuthority;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.vo.bs.staffAuth.StaffAuthVo;

/**
 *
 * @author :hulj
 * @Date : 2016年3月10日下午6:00:16
 * @Description: 
 * 
 * 		jhj2.1   综合 服务人员管理		
 *
 */
/**
 *
 * @author :hulj
 * @Date : 2016年3月11日下午5:02:57
 * @Description: TODO
 *
 */
public class NewStaffFormVo extends OrgStaffs {
	
	
	private List<PartnerServiceType> partServiceList;

	private Long skillId;
	
	private  Long[]  skillIds;
	
	
	//2016年1月22日18:44:36 服务人员 身份认证 Vo
	private List<StaffAuthVo> authList;
	
	//2016年1月22日18:44:46 已经通过认证的 项目 id，从 org_staff_auth表获得
	private String authIds;


	public List<PartnerServiceType> getPartServiceList() {
		return partServiceList;
	}

	public void setPartServiceList(List<PartnerServiceType> partServiceList) {
		this.partServiceList = partServiceList;
	}

	public List<StaffAuthVo> getAuthList() {
		return authList;
	}

	public void setAuthList(List<StaffAuthVo> authList) {
		this.authList = authList;
	}

	public String getAuthIds() {
		return authIds;
	}

	public void setAuthIds(String authIds) {
		this.authIds = authIds;
	}

	public Long getSkillId() {
		return skillId;
	}

	public void setSkillId(Long skillId) {
		this.skillId = skillId;
	}

	public Long[] getSkillIds() {
		return skillIds;
	}

	public void setSkillIds(Long[] skillIds) {
		this.skillIds = skillIds;
	}
	
}
