package com.jhj.po.model.bs;

public class OrgStaffInvite {
    private Long id;

    private Long staffId;

    private String inviteMobile;

    private Long inviteStaffId;

    private Short inviteOrderCount;

    private Short inviteStatus;

    private Long addTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getInviteMobile() {
        return inviteMobile;
    }

    public void setInviteMobile(String inviteMobile) {
        this.inviteMobile = inviteMobile == null ? null : inviteMobile.trim();
    }

    public Long getInviteStaffId() {
        return inviteStaffId;
    }

    public void setInviteStaffId(Long inviteStaffId) {
        this.inviteStaffId = inviteStaffId;
    }

    public Short getInviteOrderCount() {
        return inviteOrderCount;
    }

    public void setInviteOrderCount(Short inviteOrderCount) {
        this.inviteOrderCount = inviteOrderCount;
    }

    public Short getInviteStatus() {
        return inviteStatus;
    }

    public void setInviteStatus(Short inviteStatus) {
        this.inviteStatus = inviteStatus;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }
}