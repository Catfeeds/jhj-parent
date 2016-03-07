package com.jhj.po.model.university;

public class PartnerServiceType {
    private Long serviceTypeId;

    private String name;

    private Long parentId;

    private Short viewType;

    private Short no;

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Short getViewType() {
        return viewType;
    }

    public void setViewType(Short viewType) {
        this.viewType = viewType;
    }

    public Short getNo() {
        return no;
    }

    public void setNo(Short no) {
        this.no = no;
    }
}