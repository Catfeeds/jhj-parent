package com.jhj.po.model.dict;

public class CouponsUseCondition {
    private Integer useConditonId;

    private String useConditionDescr;

    public Integer getUseConditonId() {
        return useConditonId;
    }

    public void setUseConditonId(Integer useConditonId) {
        this.useConditonId = useConditonId;
    }

    public String getUseConditionDescr() {
        return useConditionDescr;
    }

    public void setUseConditionDescr(String useConditionDescr) {
        this.useConditionDescr = useConditionDescr == null ? null : useConditionDescr.trim();
    }
}