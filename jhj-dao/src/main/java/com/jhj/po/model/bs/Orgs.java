package com.jhj.po.model.bs;

public class Orgs {
    private Long orgId;

    private String orgName;

    private String orgAddr;

    private String orgTel;

    private String orgOwner;

    private String orgOwnerTel;

    private String poiLatitude;

    private String poiLongitude;

    private Short poiType;

    private String poiName;

    private String poiAddress;

    private String poiCity;

    private String poiUid;

    private String poiPhone;

    private String poiPostCode;

    private Short orgStatus;

    private Long addTime;

    private Long updateTime;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }

    public String getOrgAddr() {
        return orgAddr;
    }

    public void setOrgAddr(String orgAddr) {
        this.orgAddr = orgAddr == null ? null : orgAddr.trim();
    }

    public String getOrgTel() {
        return orgTel;
    }

    public void setOrgTel(String orgTel) {
        this.orgTel = orgTel == null ? null : orgTel.trim();
    }

    public String getOrgOwner() {
        return orgOwner;
    }

    public void setOrgOwner(String orgOwner) {
        this.orgOwner = orgOwner == null ? null : orgOwner.trim();
    }

    public String getOrgOwnerTel() {
        return orgOwnerTel;
    }

    public void setOrgOwnerTel(String orgOwnerTel) {
        this.orgOwnerTel = orgOwnerTel == null ? null : orgOwnerTel.trim();
    }

    public String getPoiLatitude() {
        return poiLatitude;
    }

    public void setPoiLatitude(String poiLatitude) {
        this.poiLatitude = poiLatitude == null ? null : poiLatitude.trim();
    }

    public String getPoiLongitude() {
        return poiLongitude;
    }

    public void setPoiLongitude(String poiLongitude) {
        this.poiLongitude = poiLongitude == null ? null : poiLongitude.trim();
    }

    public Short getPoiType() {
        return poiType;
    }

    public void setPoiType(Short poiType) {
        this.poiType = poiType;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName == null ? null : poiName.trim();
    }

    public String getPoiAddress() {
        return poiAddress;
    }

    public void setPoiAddress(String poiAddress) {
        this.poiAddress = poiAddress == null ? null : poiAddress.trim();
    }

    public String getPoiCity() {
        return poiCity;
    }

    public void setPoiCity(String poiCity) {
        this.poiCity = poiCity == null ? null : poiCity.trim();
    }

    public String getPoiUid() {
        return poiUid;
    }

    public void setPoiUid(String poiUid) {
        this.poiUid = poiUid == null ? null : poiUid.trim();
    }

    public String getPoiPhone() {
        return poiPhone;
    }

    public void setPoiPhone(String poiPhone) {
        this.poiPhone = poiPhone == null ? null : poiPhone.trim();
    }

    public String getPoiPostCode() {
        return poiPostCode;
    }

    public void setPoiPostCode(String poiPostCode) {
        this.poiPostCode = poiPostCode == null ? null : poiPostCode.trim();
    }

    public Short getOrgStatus() {
        return orgStatus;
    }

    public void setOrgStatus(Short orgStatus) {
        this.orgStatus = orgStatus;
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
}