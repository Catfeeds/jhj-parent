package com.jhj.vo.user;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Tags;

/**
 *
 * @author :hulj
 * @Date : 2015年8月11日上午11:12:00
 * @Description: 
 *		1.用户查看 自己 助理 VO
 *      2.助理版--我的 Vo	
 */
public class UserGetAmVo extends OrgStaffs {

	private List<Tags> tagList;	//助理 技能 
	
	private int orderNum;		//亲密度，该助理为该用户服务过 的订单数量

	private BigDecimal sumMoney;//助理 本月流水
	
	private String orgName;		//助理所属门店
	
	private String astroName;
	
	private String age;
	
	private Map<String, List<String>> skillMap;

	
	public Map<String, List<String>> getSkillMap() {
		return skillMap;
	}

	public void setSkillMap(Map<String, List<String>> skillMap) {
		this.skillMap = skillMap;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public BigDecimal getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(BigDecimal sumMoney) {
		this.sumMoney = sumMoney;
	}

	public List<Tags> getTagList() {
		return tagList;
	}

	public void setTagList(List<Tags> tagList) {
		this.tagList = tagList;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAstroName() {
		return astroName;
	}

	public void setAstroName(String astroName) {
		this.astroName = astroName;
	}

	
}
