package com.jhj.vo.staff;

import java.util.Date;

import com.jhj.po.model.user.UserTrailReal;

public class OrgStaffPoiListVo extends UserTrailReal {
	
	private String orgName;
	
	private String parentOrgName;	
	
	private String hukou;
	
    private Long staffId;

    private Long provinceId;

    private Long cityId;

    private Long regionId;

    private Long orgId;

    private Short staffType;

    private Short status;

    private Short sex;

    private String name;

    private String mobile;

    private String tel;

    private String addr;

    private Date birth;

    private Short workYear;

    private String cardId;

    private String nation;

    private String edu;

    private Short astro;

    private String bloodType;

    private String headImg;

    private String intro;

    private Long addTime;

    private Long updateTime;

    private Long parentOrgId;

    private Short level;
    
    private String staffCode;
	
	
    
    private String poiTimeStr;
    
    private int todayPoiStatus;    //今天是否有轨迹记录.
    
    private String staffLeave;


	public String getPoiTimeStr() {
		return poiTimeStr;
	}

	public void setPoiTimeStr(String poiTimeStr) {
		this.poiTimeStr = poiTimeStr;
	}

	public String getHukou() {
		return hukou;
	}

	public void setHukou(String hukou) {
		this.hukou = hukou;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public int getTodayPoiStatus() {
		return todayPoiStatus;
	}

	public void setTodayPoiStatus(int todayPoiStatus) {
		this.todayPoiStatus = todayPoiStatus;
	}

	public String getStaffLeave() {
		return staffLeave;
	}

	public void setStaffLeave(String staffLeave) {
		this.staffLeave = staffLeave;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Short getStaffType() {
		return staffType;
	}

	public void setStaffType(Short staffType) {
		this.staffType = staffType;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Short getSex() {
		return sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public Short getWorkYear() {
		return workYear;
	}

	public void setWorkYear(Short workYear) {
		this.workYear = workYear;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getEdu() {
		return edu;
	}

	public void setEdu(String edu) {
		this.edu = edu;
	}

	public Short getAstro() {
		return astro;
	}

	public void setAstro(Short astro) {
		this.astro = astro;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Long getAddTime() {
		return addTime;
	}

	public void setAddTime(Long addTime) {
		this.addTime = addTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getParentOrgId() {
		return parentOrgId;
	}

	public void setParentOrgId(Long parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public Short getLevel() {
		return level;
	}

	public void setLevel(Short level) {
		this.level = level;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}
    
 
}
