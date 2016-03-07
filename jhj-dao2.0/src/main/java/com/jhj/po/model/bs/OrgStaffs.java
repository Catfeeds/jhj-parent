package com.jhj.po.model.bs;

import java.util.Date;

public class OrgStaffs {
    private Long staffId;

    private Long amId;

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

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getAmId() {
        return amId;
    }

    public void setAmId(Long amId) {
        this.amId = amId;
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
        this.name = name == null ? null : name.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr == null ? null : addr.trim();
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
        this.cardId = cardId == null ? null : cardId.trim();
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation == null ? null : nation.trim();
    }

    public String getEdu() {
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu == null ? null : edu.trim();
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
        this.bloodType = bloodType == null ? null : bloodType.trim();
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg == null ? null : headImg.trim();
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro == null ? null : intro.trim();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addTime == null) ? 0 : addTime.hashCode());
		result = prime * result + ((addr == null) ? 0 : addr.hashCode());
		result = prime * result + ((amId == null) ? 0 : amId.hashCode());
		result = prime * result + ((astro == null) ? 0 : astro.hashCode());
		result = prime * result + ((birth == null) ? 0 : birth.hashCode());
		result = prime * result + ((bloodType == null) ? 0 : bloodType.hashCode());
		result = prime * result + ((cardId == null) ? 0 : cardId.hashCode());
		result = prime * result + ((cityId == null) ? 0 : cityId.hashCode());
		result = prime * result + ((edu == null) ? 0 : edu.hashCode());
		result = prime * result + ((headImg == null) ? 0 : headImg.hashCode());
		result = prime * result + ((intro == null) ? 0 : intro.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nation == null) ? 0 : nation.hashCode());
		result = prime * result + ((orgId == null) ? 0 : orgId.hashCode());
		result = prime * result + ((provinceId == null) ? 0 : provinceId.hashCode());
		result = prime * result + ((regionId == null) ? 0 : regionId.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		result = prime * result + ((staffId == null) ? 0 : staffId.hashCode());
		result = prime * result + ((staffType == null) ? 0 : staffType.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tel == null) ? 0 : tel.hashCode());
		result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
		result = prime * result + ((workYear == null) ? 0 : workYear.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrgStaffs other = (OrgStaffs) obj;
		if (addTime == null) {
			if (other.addTime != null)
				return false;
		} else if (!addTime.equals(other.addTime))
			return false;
		if (addr == null) {
			if (other.addr != null)
				return false;
		} else if (!addr.equals(other.addr))
			return false;
		if (amId == null) {
			if (other.amId != null)
				return false;
		} else if (!amId.equals(other.amId))
			return false;
		if (astro == null) {
			if (other.astro != null)
				return false;
		} else if (!astro.equals(other.astro))
			return false;
		if (birth == null) {
			if (other.birth != null)
				return false;
		} else if (!birth.equals(other.birth))
			return false;
		if (bloodType == null) {
			if (other.bloodType != null)
				return false;
		} else if (!bloodType.equals(other.bloodType))
			return false;
		if (cardId == null) {
			if (other.cardId != null)
				return false;
		} else if (!cardId.equals(other.cardId))
			return false;
		if (cityId == null) {
			if (other.cityId != null)
				return false;
		} else if (!cityId.equals(other.cityId))
			return false;
		if (edu == null) {
			if (other.edu != null)
				return false;
		} else if (!edu.equals(other.edu))
			return false;
		if (headImg == null) {
			if (other.headImg != null)
				return false;
		} else if (!headImg.equals(other.headImg))
			return false;
		if (intro == null) {
			if (other.intro != null)
				return false;
		} else if (!intro.equals(other.intro))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nation == null) {
			if (other.nation != null)
				return false;
		} else if (!nation.equals(other.nation))
			return false;
		if (orgId == null) {
			if (other.orgId != null)
				return false;
		} else if (!orgId.equals(other.orgId))
			return false;
		if (provinceId == null) {
			if (other.provinceId != null)
				return false;
		} else if (!provinceId.equals(other.provinceId))
			return false;
		if (regionId == null) {
			if (other.regionId != null)
				return false;
		} else if (!regionId.equals(other.regionId))
			return false;
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		if (staffId == null) {
			if (other.staffId != null)
				return false;
		} else if (!staffId.equals(other.staffId))
			return false;
		if (staffType == null) {
			if (other.staffType != null)
				return false;
		} else if (!staffType.equals(other.staffType))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (tel == null) {
			if (other.tel != null)
				return false;
		} else if (!tel.equals(other.tel))
			return false;
		if (updateTime == null) {
			if (other.updateTime != null)
				return false;
		} else if (!updateTime.equals(other.updateTime))
			return false;
		if (workYear == null) {
			if (other.workYear != null)
				return false;
		} else if (!workYear.equals(other.workYear))
			return false;
		return true;
	}
    
   
   
   
    
    
}